package aahmf.notepad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class NewNoteActivity extends AppCompatActivity {

    private Button ExportNote;
    private Button ImportNote;
    private Button AddImage;
    private Button AddFile;
    private Button CancelNote;
    private Button SaveNote;
    private Spinner noteClr;
    private static final String[] coloursTwo = {"White", "Green", "Yellow", "Red"};
    int bgColor;
    private EditText WriteNote,Title;

    private String NoteTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ExportNote = findViewById(R.id.btnExport);
        ImportNote = findViewById(R.id.btnImport);
        AddImage = findViewById(R.id.btnAddImage);
        AddFile = findViewById(R.id.btnAddFile);
        CancelNote = findViewById(R.id.btnCancelNewNote);
        noteClr = findViewById(R.id.spNoteClr);
        noteClr.setPrompt("Priority");

        SaveNote = findViewById(R.id.button9);
        WriteNote = findViewById(R.id.etWriteNote);

        CancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewNoteActivity.this, MainMenuActivity.class));
            }
        });

        AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewNoteActivity.this, Gallery.class));
            }
        });

        SaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(WriteNote.getText().toString().matches(""))
                {
                    Toast.makeText(NewNoteActivity.this,"Write Something Before Saving",Toast.LENGTH_LONG).show();
                }
                else{
                AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(NewNoteActivity.this);

                DialogBuilder.setTitle("Give Note Title");


                DialogBuilder.setMessage("Give the note title to the textbox to save the note to your phone");
                Title = new EditText(NewNoteActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                Title.setLayoutParams(lp);
                DialogBuilder.setView(Title);


                DialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteTitle = Title.getText().toString();
                        WriteXml(NoteTitle);
                    }

                });

               DialogBuilder.create();
               DialogBuilder.show();

            }}
        });


            ArrayAdapter<String> adapterOne = new ArrayAdapter<>(NewNoteActivity.this,android.R.layout.
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
            for(int i = 0;i<Gallery.ImagePaths.size();i++)
            {
                xmlSerializer.startTag(null,"Image"+i);
                xmlSerializer.text(Gallery.ImagePaths.get(i).toString());
                xmlSerializer.endTag(null,"Image"+i);
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
            Toast.makeText(NewNoteActivity.this,"Note Saved In your phone",Toast.LENGTH_LONG).show();
            startActivity(new Intent(NewNoteActivity.this,MainMenuActivity.class));
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
}
