package aahmf.notepad;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FileGallery extends AppCompatActivity {
    private static final String TAG = "FileGallery";
    protected ArrayList<Uri> FilePaths = new ArrayList<Uri>();
    private ImageButton btn, Back;
    private GridView gvGallery;
    private FileGalleryAdapter galleryAdapter;
    private ArrayList<Integer> mSelected = new ArrayList<>();
    int PICK_FILE_MULTIPLE = 2;
    String FileEncoded;
    List<String> fileEncodedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_gallery);

        btn = findViewById(R.id.btn);
        Back = findViewById(R.id.svb);

        gvGallery = (GridView) findViewById(R.id.gv);
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            int z=0;
            Uri temp;
            while (extras.containsKey("File"+z)){
                temp=Uri.parse(extras.getString("File"+z));
                if(!FilePaths.contains(temp)){
                    FilePaths.add(temp);
                }
                z=z+1;
            }
        }

        for (int y = 0; y < FilePaths.size(); y++) {
            galleryAdapter = new FileGalleryAdapter(getApplicationContext(), FilePaths);
            gvGallery.setAdapter(galleryAdapter);
            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                    .getLayoutParams();
            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select a File"), PICK_FILE_MULTIPLE);
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                for(int z=0;z<FilePaths.size();z++){
                    resultIntent.putExtra("File"+z,FilePaths.get(z).toString());
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an File is picked
            if (requestCode == PICK_FILE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the File from data
                Uri mFileUri;
                String[] filePathColumn = {MediaStore.Files.FileColumns.DATA};
                fileEncodedList = new ArrayList<String>();
                if (data.getData() != null) {

                    mFileUri = data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mFileUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    FileEncoded = cursor.getString(columnIndex);
                    cursor.close();

                    if(!FilePaths.contains(mFileUri)){
                        FilePaths.add(mFileUri);
                    }
                    galleryAdapter = new FileGalleryAdapter(getApplicationContext(), FilePaths);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);

                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            FileEncoded = cursor.getString(columnIndex);
                            fileEncodedList.add(FileEncoded);
                            cursor.close();


                        }
                        for (int y = 0; y < mArrayUri.size(); y++) {
                            if(!FilePaths.contains(mArrayUri.get(y))){
                                FilePaths.add(mArrayUri.get(y));
                            }
                            galleryAdapter = new FileGalleryAdapter(getApplicationContext(), FilePaths);
                            gvGallery.setAdapter(galleryAdapter);
                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);
                        }

                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked any files",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private GridView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Log.d(TAG,"Position Clicked ["+position+"] with item id ["+FilePaths.get(position)+"]");

        }
    };


    @Override
    protected void onDestroy() {
        Intent resultIntent = new Intent();
        for(int z=0;z<FilePaths.size();z++){
            resultIntent.putExtra("File"+z,FilePaths.get(z).toString());
        }
        setResult(Activity.RESULT_OK, resultIntent);
        super.onDestroy();
    }
}
