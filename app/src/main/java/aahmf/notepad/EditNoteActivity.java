package aahmf.notepad;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

public class EditNoteActivity extends AppCompatActivity {
    private final int File_Request_Code = 2;
    private int STORAGE_PERMISSION_CODE = 1;
    private static String Title;
    private EditText WriteNote;
    private ImageButton btnCancel, btnExport, btnImport;
    private Spinner noteClr;
    private static final String[] coloursTwo = {"White", "Green", "Yellow", "Red"};
    int bgColor;
    private String date, keywords;
    static ArrayList<Uri> Images = new ArrayList<Uri>();
    ArrayList<Uri> filesUrisOld = new ArrayList<Uri>(), filesUris = new ArrayList<Uri>();

    private String exportPath;

    private static final int XML_BROWSER = 43;
    private DatabaseReference mImages = FirebaseDatabase.getInstance().getReference("Images");
    private DatabaseReference mFiles = FirebaseDatabase.getInstance().getReference("Files");
    private DatabaseReference Text = FirebaseDatabase.getInstance().getReference("Content");
    private int id = 1, imageid = 1, fileid = 1, fileNoteId;
    private FirebaseUser user = LogInActivity.getUser();
    private DatabaseReference mNotes = FirebaseDatabase.getInstance().getReference("Notes");
    private int Id = 0;
    private String uid2 = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        btnCancel = findViewById(R.id.btnCancelEdit);
        btnExport = findViewById(R.id.btnExportEdit);
        btnImport = findViewById(R.id.btnImportEdit);
        WriteNote = findViewById(R.id.etWriteNoteEdit);
        noteClr = findViewById(R.id.spNoteClrEdit);
        noteClr.setPrompt("Priority");
        date = NoteEntryAdapter.getDate();
        keywords = NoteEntryAdapter.getKeywords();

        Title = NoteEntryAdapter.getTitle();
        GalleryEdit.ImagePaths3.clear();
        //loadXML(Title);
        GetOnlineText(uid2, Title);
        incrementCounterImages();
        incrementCounterFiles();

        GetOnlineNotes(uid2, Title);

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                if (WriteNote.getText().toString().matches("")) {
                    Toast.makeText(EditNoteActivity.this, "Write Something Before Exporting", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(EditNoteActivity.this);

                    DialogBuilder.setTitle("Give Note Title");


                    DialogBuilder.setMessage("Give note title to export");
                    EditText tit = new EditText(EditNoteActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    tit.setLayoutParams(lp);
                    DialogBuilder.setView(tit);

                    DialogBuilder.setNeutralButton("Choose directory", new DialogInterface.OnClickListener() {
                        boolean m_newFolderEnabled = true;

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestStoragePermission();
                            if (ContextCompat.checkSelfPermission(EditNoteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(EditNoteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                // Create DirectoryChooserDialog and register a callback
                                DirectoryChooserDialog directoryChooserDialog =
                                        new DirectoryChooserDialog(EditNoteActivity.this,
                                                new DirectoryChooserDialog.ChosenDirectoryListener() {
                                                    @Override
                                                    public void onChosenDir(String chosenDir) {
                                                        exportPath = chosenDir;
                                                        Toast.makeText(
                                                                EditNoteActivity.this, "Chosen directory: " +
                                                                        exportPath, Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                // Toggle new folder button enabling
                                directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
                                // Load directory chooser dialog for initial 'm_chosenDir' directory.
                                // The registered callback will be called upon final directory selection.
                                directoryChooserDialog.chooseDirectory(exportPath);
                                m_newFolderEnabled = !m_newFolderEnabled;

                            } else {
                                requestStoragePermission();
                            }
                        }
                    });


                    DialogBuilder.setPositiveButton("Export", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String NoteText = WriteNote.getText().toString();


                            try {
                                File dir = new File(exportPath);
                                if (!dir.exists()) {
                                    dir.mkdir();
                                }
                                File file = new File(dir, Title + ".xml");
                                FileOutputStream fos = new FileOutputStream(file);
                                XmlSerializer xmlSerializer = Xml.newSerializer();
                                StringWriter writer = new StringWriter();
                                xmlSerializer.setOutput(writer);
                                xmlSerializer.startDocument("UTF-8", true);
                                xmlSerializer.startTag(null, "userData");
                                xmlSerializer.startTag(null, "Text");
                                xmlSerializer.text(NoteText);
                                xmlSerializer.endTag(null, "Text");
                                for (int i = 0; i < Gallery.ImagePaths.size(); i++) {
                                    xmlSerializer.startTag(null, "Image" + i);
                                    xmlSerializer.text(Gallery.ImagePaths.get(i).toString());
                                    xmlSerializer.endTag(null, "Image" + i);
                                }
                                for (int i = 0; i < filesUris.size(); i++) {
                                    xmlSerializer.startTag(null, "File" + i);
                                    xmlSerializer.text(filesUris.get(i).toString());
                                    xmlSerializer.endTag(null, "File" + i);
                                }
                                xmlSerializer.endTag(null, "userData");
                                xmlSerializer.endDocument();
                                xmlSerializer.flush();
                                String dataWrite = writer.toString();
                                fos.write(dataWrite.getBytes());
                                fos.close();
                                SharedPreferences mSharedPref = getSharedPreferences("NoteColor", MODE_PRIVATE);
                                SharedPreferences.Editor mEditor = mSharedPref.edit();
                                mEditor.putInt(Title, bgColor);
                                mEditor.apply();
                                Toast.makeText(EditNoteActivity.this, "Note exported at" + exportPath, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(EditNoteActivity.this, MainMenuActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } catch (FileNotFoundException e) {

                                e.printStackTrace();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalStateException e) {

                                e.printStackTrace();
                            } catch (IOException e) {

                                e.printStackTrace();
                            }

                        }
                    });
                    DialogBuilder.create();
                    DialogBuilder.show();
                }
            }
        });


        ArrayAdapter<String> adapterOne = new ArrayAdapter<>(EditNoteActivity.this, android.R.layout.
                simple_spinner_item, coloursTwo);
        adapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        noteClr.setAdapter(adapterOne);
        noteClr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //white
                        bgColor = getResources().getColor(R.color.colorWhite);
                        break;
                    case 1:
                        //green
                        bgColor = getResources().getColor(R.color.colorGreen);
                        break;
                    case 2:
                        //yellow
                        bgColor = getResources().getColor(R.color.colorYellow);
                        break;
                    case 3:
                        //red
                        bgColor = getResources().getColor(R.color.colorRed);
                        break;
                }
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bgColor = getResources().getColor(R.color.colorWhite);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditNoteActivity.this, MainMenuActivity.class));
            }


        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newnote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.save:
                DatabaseReference key = mNotes.child(String.valueOf(id));
                DatabaseReference Content = key.child("Content");
                Content.setValue(WriteNote.getText().toString());
                if (ViewNoteActivity.Images.size() != 0)
                {
                    for (int i = ViewNoteActivity.Images.size(); i < GalleryEdit.ImagePaths2.size(); i++)
                    {

                        DatabaseReference imagekey = mImages.child(String.valueOf(imageid));
                        DatabaseReference path = imagekey.child("path");
                        DatabaseReference NoteId = imagekey.child("note_id");
                        path.setValue(GalleryEdit.ImagePaths2.get(i).toString());
                        NoteId.setValue(Id);
                        imageid++;


                    }
                }
                else
                {
                    for (int i = 0; i < GalleryEdit.ImagePaths2.size(); i++)
                    {

                        DatabaseReference imagekey = mImages.child(String.valueOf(imageid));
                        DatabaseReference path = imagekey.child("path");
                        DatabaseReference NoteId = imagekey.child("note_id");
                        path.setValue(GalleryEdit.ImagePaths2.get(i).toString());
                        NoteId.setValue(Id);
                        imageid++;

                    }
                }
                for (int i = 0; i < filesUris.size(); i++)
                {
                    if (!filesUrisOld.contains(filesUris.get(i)))
                    {
                        DatabaseReference filekey = mFiles.child(String.valueOf(fileid));
                        DatabaseReference path = filekey.child("path");
                        DatabaseReference NoteId = filekey.child("note_id");
                        path.setValue(filesUris.get(i).toString());
                        NoteId.setValue(fileNoteId);
                        fileid++;
                    }
                }
                startActivity(new Intent(EditNoteActivity.this, MainMenuActivity.class));
                //WriteXml(Title);
                Images.clear();
                break;

            case R.id.addfile:
                Intent intent = new Intent(EditNoteActivity.this, FileGallery.class);
                for (int z = 0; z < filesUris.size(); z++)
                {
                    intent.putExtra("File" + z, filesUris.get(z).toString());
                }
                startActivityForResult(intent, File_Request_Code);
                break;
            case R.id.image:
                startActivity(new Intent(EditNoteActivity.this, GalleryEdit.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void WriteXml(String xmlFile) {
        String NoteText = WriteNote.getText().toString();
        try
        {
            FileOutputStream fileos = getApplicationContext().openFileOutput(xmlFile, Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "userData");
            xmlSerializer.startTag(null, "Text");
            xmlSerializer.text(NoteText);
            xmlSerializer.endTag(null, "Text");
            if (GalleryEdit.ImagePaths2.size() == 0)
            {
                if (ViewNoteActivity.Images.size() == 0)
                {
                    for (int i = 0; i < ViewNoteActivity.Images.size(); i++)
                    {
                        xmlSerializer.startTag(null, "Image" + i);
                        xmlSerializer.text(ViewNoteActivity.Images.get(i).toString());
                        xmlSerializer.endTag(null, "Image" + i);
                    }
                }
                else
                    {
                    for (int i = 0; i < ViewNoteActivity.Images.size(); i++) {
                        String pp = (ViewNoteActivity.Images.get(i).toString());
                        xmlSerializer.startTag(null, "Image" + i);
                        xmlSerializer.text(pp);
                        xmlSerializer.endTag(null, "Image" + i);
                    }
                }

            }
            else
                {
                if (ViewNoteActivity.Images.size() == 0)
                {
                    for (int i = 0; i < GalleryEdit.ImagePaths2.size(); i++)
                    {
                        xmlSerializer.startTag(null, "Image" + i);
                        xmlSerializer.text(GalleryEdit.ImagePaths2.get(i).toString());
                        xmlSerializer.endTag(null, "Image" + i);
                    }
                }
                else
                    {
                    for (int i = 0; i < GalleryEdit.ImagePaths2.size(); i++)
                    {
                        String pp = (GalleryEdit.ImagePaths2.get(i).toString());
                        xmlSerializer.startTag(null, "Image" + i);
                        xmlSerializer.text(pp);
                        xmlSerializer.endTag(null, "Image" + i);
                    }
                }
            }

            for (int i = 0; i < filesUris.size(); i++)
            {
                String pp = (filesUris.get(i).toString());
                xmlSerializer.startTag(null, "File" + i);
                xmlSerializer.text(pp);
                xmlSerializer.endTag(null, "File" + i);
            }
            xmlSerializer.startTag(null, "keywords");
            xmlSerializer.text(keywords);
            xmlSerializer.endTag(null, "keywords");
            xmlSerializer.startTag(null, "Date");
            xmlSerializer.text(date);
            xmlSerializer.endTag(null, "Date");
            xmlSerializer.endTag(null, "userData");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
            SharedPreferences mSharedPref = getSharedPreferences("NoteColor", MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSharedPref.edit();
            mEditor.putInt(xmlFile, bgColor);
            mEditor.apply();
            Toast.makeText(EditNoteActivity.this, "Note Saved In your phone", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(EditNoteActivity.this, MainMenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void loadXML(String file)
    {

        ArrayList<String> userData = new ArrayList<String>();
        try {
            FileInputStream fis = getApplicationContext().openFileInput(file);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);
            String data = new String(inputBuffer);
            isr.close();
            fis.close();
        } catch (FileNotFoundException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        factory.setNamespaceAware(true);
        XmlPullParser xpp = null;
        try {
            xpp = factory.newPullParser();
        } catch (XmlPullParserException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        try {
            FileInputStream fis = getApplicationContext().openFileInput(file);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);
            String data = new String(inputBuffer);
            xpp.setInput(new StringReader(data));
        } catch (FileNotFoundException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        int eventType = 0;
        try {
            eventType = xpp.getEventType();
        } catch (XmlPullParserException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        while (eventType != XmlPullParser.END_DOCUMENT) {

            switch (eventType) {

                case XmlPullParser.START_TAG:

                    String tagname = xpp.getName();


                    //if (tagname.equalsIgnoreCase("Image" + i)) {
                    if (tagname.contains("Image")) {
                        try {
                            eventType = xpp.next();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                        //eventType=XmlPullParser.TEXT;
                        // if (eventType == XmlPullParser.TEXT)

                        Images.add(Uri.parse(xpp.getText()));
                    }

                    //if (tagname.equalsIgnoreCase("File" + i)) {

                    if (tagname.contains("File")) {
                        try {
                            eventType = xpp.next();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                        //eventType=XmlPullParser.TEXT;
                        // if (eventType == XmlPullParser.TEXT)

                        filesUris.add(Uri.parse(xpp.getText()));
                        //}
                    }


                    break;
            }
            if (eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            } else if (eventType == XmlPullParser.START_TAG) {
                System.out.println("Start tag " + xpp.getName());
            } else if (eventType == XmlPullParser.END_TAG) {
                System.out.println("End tag " + xpp.getName());
            } else if (eventType == XmlPullParser.TEXT) {
                userData.add(xpp.getText());
            }

            try {
                eventType = xpp.next();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        String Text = userData.get(0);
        //String password = userData.get(1);
        WriteNote.setText(Text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == File_Request_Code) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int z = 0;
                Uri temp;
                Bundle extras = data.getExtras();
                if (extras != null) {
                    while (extras.containsKey("File" + z)) {
                        temp = Uri.parse(extras.getString("File" + z));
                        if (!filesUris.contains(temp)) {
                            filesUris.add(temp);
                        }
                        z = z + 1;
                    }
                }
            }
        } else if (requestCode == XML_BROWSER) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri uri = data.getData();
                String realpath = TakeRealPathFromUri.getPath(this, uri);
                loadXML(realpath);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void incrementCounterImages() {


        Query query = mImages.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        imageid = Integer.parseInt(child.getKey());
                        imageid++;
                    }
                } else return;
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void incrementCounterFiles() {

        Query query = mFiles.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        fileid = Integer.parseInt(child.getKey());
                        fileid++;
                    }
                } else return;
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void incrementCounterNotes() {


        mNotes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
                for (int i = 1; i <= size; i++) {

                    if (dataSnapshot.hasChild(String.valueOf(i))) {
                        id++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void GetOnlineNotes(final String uid, final String Tit) {

        mNotes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Query query = mNotes.orderByChild("UserId_Title").equalTo(uid + "_" + Tit);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Id = Integer.parseInt(child.getKey());
                            Query query2 = mImages.orderByChild("note_id").equalTo(Id);
                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Images.clear();
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            String path = child.child("path").getValue().toString();
                                            Images.add(Uri.parse(path));


                                        }
                                    }

                                    Query query3 = mFiles.orderByChild("note_id").equalTo(Id);
                                    query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                    String path = child.child("path").getValue().toString();
                                                    filesUris.add(Uri.parse(path));
                                                }
                                                filesUrisOld.addAll(filesUris);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void GetOnlineText(final String uid, final String Tit) {

        mNotes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Query query = mNotes.orderByChild("UserId_Title").equalTo(uid + "_" + Tit);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Id = Integer.parseInt(child.getKey());
                            fileNoteId = Integer.parseInt(child.getKey());
                            Query query2 = mNotes.orderByChild("UserId_Title").equalTo(uid + "_" + Tit);
                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            String path = child.child("Content").getValue().toString();
                                            WriteNote.setText(path);


                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static ArrayList<Uri> getImages() {
        return Images;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(EditNoteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    private void startSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/xml");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent.createChooser(intent, "Select XML File"), XML_BROWSER);
    }
}