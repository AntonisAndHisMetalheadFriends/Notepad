package aahmf.notepad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private Button EditNote;
    private Button SaveNote;

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
        EditNote = findViewById(R.id.btnEditNote);

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
                        NoteTitle=Title.getText().toString();
                        WriteXml(NoteTitle);
                    }
                });

               DialogBuilder.create();
               DialogBuilder.show();

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
