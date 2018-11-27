package aahmf.notepad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

public class EditNoteActivity extends AppCompatActivity {
    private final int File_Request_Code=2;
    private static String Title;
    private EditText WriteNote;
    private Button btnSave,btnImport,btnExport,btnCancel,btnImage,btnFile;
    private Spinner noteClr;
    private static final String[] coloursTwo = {"White", "Green", "Yellow", "Red"};
    int bgColor;
    ArrayList<Uri>Images = new ArrayList<Uri>(),filesUris= new ArrayList<Uri>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        btnCancel = findViewById(R.id.btnCancelEdit);
        btnExport=findViewById(R.id.btnExportEdit);
        btnFile=findViewById(R.id.btnAddFileEdit);
        btnImport=findViewById(R.id.btnImportEdit);
        btnImage = findViewById(R.id.btnAddImageEdit);
        btnSave = findViewById(R.id.button9Edit);
        WriteNote = findViewById(R.id.etWriteNoteEdit);
        noteClr = findViewById(R.id.spNoteClrEdit);
        noteClr.setPrompt("Priority");

        Title=NoteEntryAdapter.getTitle();

        loadXML(Title);


        ArrayAdapter<String> adapterOne = new ArrayAdapter<>(EditNoteActivity.this,android.R.layout.
                simple_spinner_item,coloursTwo);
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteXml(Title);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditNoteActivity.this,MainMenuActivity.class));
            }


        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditNoteActivity.this,GalleryEdit.class));
            }


        });
        btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditNoteActivity.this,FileGallery.class);
                for(int z=0;z<filesUris.size();z++){
                    intent.putExtra("File"+z,filesUris.get(z).toString());
                }
                startActivityForResult(intent,File_Request_Code);
            }


        });


    }

    public void WriteXml(String xmlFile) {
        String NoteText = WriteNote.getText().toString();
        try {
            FileOutputStream fileos= getApplicationContext().openFileOutput(xmlFile, Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "userData");
            xmlSerializer.startTag(null,"Text");
            xmlSerializer.text(NoteText);
            xmlSerializer.endTag(null, "Text");
            if(ViewNoteActivity.Images.size()==0)
            {
                for(int i = 0; i<GalleryEdit.ImagePaths2.size(); i++)
                {
                    xmlSerializer.startTag(null,"Image"+i);
                    xmlSerializer.text(GalleryEdit.ImagePaths2.get(i).toString());
                    xmlSerializer.endTag(null,"Image"+i);
                }
            }
            else
            {
                for(int i = 0; i<GalleryEdit.ImagePaths2.size(); i++)
                {
                    String pp = (GalleryEdit.ImagePaths2.get(i).toString());
                    xmlSerializer.startTag(null,"Image"+i);
                    xmlSerializer.text(pp);
                    xmlSerializer.endTag(null,"Image"+i);
                }
            }

            for(int i = 0; i<filesUris.size(); i++) {
                String pp = (filesUris.get(i).toString());
                xmlSerializer.startTag(null,"File"+i);
                xmlSerializer.text(pp);
                xmlSerializer.endTag(null,"File"+i);
            }

            xmlSerializer.endTag(null, "userData");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
            SharedPreferences mSharedPref = getSharedPreferences("NoteColor", MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSharedPref.edit();
            mEditor.putInt(xmlFile,bgColor);
            mEditor.apply();
            Toast.makeText(EditNoteActivity.this,"Note Saved In your phone",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(EditNoteActivity.this,MainMenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalStateException e) {

            e.printStackTrace();
        }
        catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void loadXML(String file){
        ArrayList<String> userData = new ArrayList<String>();

        try {
            FileInputStream fis = getApplicationContext().openFileInput(file);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);
            String data = new String(inputBuffer);
            isr.close();
            fis.close();
        }
        catch (FileNotFoundException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
        }
        catch (XmlPullParserException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        factory.setNamespaceAware(true);
        XmlPullParser xpp = null;
        try {
            xpp = factory.newPullParser();
        }
        catch (XmlPullParserException e2) {
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
        }
        catch (FileNotFoundException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (XmlPullParserException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        int eventType = 0;
        try {
            eventType = xpp.getEventType();
        }
        catch (XmlPullParserException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        while (eventType != XmlPullParser.END_DOCUMENT){
            if (eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            }
            else if (eventType == XmlPullParser.START_TAG) {
                System.out.println("Start tag "+xpp.getName());
            }
            else if (eventType == XmlPullParser.END_TAG) {
                System.out.println("End tag "+xpp.getName());
            }
            else if(eventType == XmlPullParser.TEXT) {
                userData.add(xpp.getText());
            }
            String tagname = xpp.getName();
            switch (eventType){
                case XmlPullParser.START_TAG:
                    for(int i=0;i<=XmlPullParser.END_TAG;i++)
                    {
                        if(tagname.equalsIgnoreCase("Image"+i))
                        {
                            if(eventType==XmlPullParser.TEXT)
                                Images.add(Uri.parse(xpp.getText()));
                        }else if(tagname.equalsIgnoreCase("File"+i)){
                            if(eventType==XmlPullParser.TEXT)
                                filesUris.add(Uri.parse(xpp.getText()));
                        }
                    }
                    break;
            }
            try {
                eventType = xpp.next();
            }
            catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e) {
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
            if (resultCode == Activity.RESULT_OK && data!=null) {
                int z=0;
                Uri temp;
                Bundle extras=data.getExtras();
                if(extras!=null){
                    while (extras.containsKey("File"+z)){
                        temp=Uri.parse(extras.getString("File"+z));
                        if(!filesUris.contains(temp)){
                            filesUris.add(temp);
                        }
                        z=z+1;
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
