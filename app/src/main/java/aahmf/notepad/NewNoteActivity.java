package aahmf.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewNoteActivity extends AppCompatActivity {

    private Button ExportNote;
    private Button ImportNote;
    private Button AddImage;
    private Button AddFile;
    private Button CancelNote;
    private Button EditNote;
    private Button SaveNote;

    private EditText WriteNote;

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

        SaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(NewNoteActivity.this);

                DialogBuilder.setTitle("Give Note Title");


                EditText Title = new EditText(NewNoteActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                Title.setLayoutParams(lp);
                DialogBuilder.setView(Title);
                NoteTitle=Title.getText().toString();

                DialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SaveNote(NoteTitle);
                    }
                });

               DialogBuilder.create();
               DialogBuilder.show();

            }
        });
    }

    public void SaveNote(String name) {
        String Note = WriteNote.getText().toString();
        try {
            FileOutputStream fileOutputStream =openFileOutput(name,MODE_PRIVATE);
            fileOutputStream.write(Note.getBytes());
            fileOutputStream.close();
            Toast.makeText(NewNoteActivity.this, "Text Saved", Toast.LENGTH_LONG).show();
            startActivity(new Intent(NewNoteActivity.this,MainMenuActivity.class));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
