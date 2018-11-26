package aahmf.notepad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
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
    private static String Title;
    private EditText WriteNote;
    private Button btnSave,btnImport,btnExport,btnCancel,btnImage,btnFile;
    private Spinner noteClr;
    private static final String[] coloursTwo = {"White", "Green", "Yellow", "Red"};
    int bgColor;

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



    }

    public void WriteXml(String xmlFile)
    {
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
            xmlSerializer.startTag(null,"Image Paths");
            xmlSerializer.text(String.valueOf(Gallery.ImagePaths));
            xmlSerializer.endTag(null,"Image Paths" );
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
            startActivity(new Intent(EditNoteActivity.this,MainMenuActivity.class));
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
}
