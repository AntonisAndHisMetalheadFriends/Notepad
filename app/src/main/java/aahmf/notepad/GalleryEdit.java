package aahmf.notepad;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GalleryEdit extends AppCompatActivity {
    private ImageButton btn, Back;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;
    protected static ArrayList<Uri> ImagePaths2 = new ArrayList<Uri>();
    protected static ArrayList<Uri> ImagePaths3 = new ArrayList<Uri>();
    public ArrayList<Uri> FirstImages = EditNoteActivity.getImages();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_edit);
        btn = findViewById(R.id.btnglredit);
        Back = findViewById(R.id.svbglredit);
        gvGallery = (GridView)findViewById(R.id.gv);
        ImagePaths2.clear();



    if (ImagePaths3.size()!=0)
    {
        for (int y = 0; y < ImagePaths3.size(); y++)
        {
            ImagePaths2.add(ImagePaths3.get(y));

            galleryAdapter = new GalleryAdapter(getApplicationContext(), ImagePaths2);
            gvGallery.setAdapter(galleryAdapter);
            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                    .getLayoutParams();
            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);
        }
    }
    else
    {
        for (int y = 0; y < FirstImages.size(); y++)
        {
            ImagePaths2.add(FirstImages.get(y));

            galleryAdapter = new GalleryAdapter(getApplicationContext(), ImagePaths2);
            gvGallery.setAdapter(galleryAdapter);
            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                    .getLayoutParams();
            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);
        }
        FirstImages.clear();
    }



        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int z = 0; z<ImagePaths2.size();z++)
                {
                    ImagePaths3.add(ImagePaths2.get(z));
                }

                finish();
                //startActivity(new Intent(GalleryEdit.this, EditNoteActivity.class));
            }
        });
        gvGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Uri abc = null;
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,long id) {
                // TODO Auto-generated method stub

                Intent intent =new Intent(getApplicationContext(),FullScreenActivity.class);
                intent.setData(ImagePaths2.get(position));
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data)
            {
                // Get the Image from data
                Uri mImageUri;
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                     mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();


                    ImagePaths2.add(mImageUri);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(),ImagePaths2);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                }
                else
                    {
                    if (data.getClipData() != null)
                    {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++)
                        {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);

                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();



                        }
                        for(int y=0;y<mArrayUri.size();y++)
                        {
                            ImagePaths2.add(mArrayUri.get(y));
                            galleryAdapter = new GalleryAdapter(getApplicationContext(),ImagePaths2);
                            gvGallery.setAdapter(galleryAdapter);
                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);
                        }

                    }
                }
            }
            else
                {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
                }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}