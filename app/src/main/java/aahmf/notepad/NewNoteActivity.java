package aahmf.notepad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewNoteActivity extends AppCompatActivity {

    private Button ExportNote;
    private Button ImportNote;
    private Button AddImage;
    private Button AddFile;
    private Button CancelNote;
    private Button EditNote;
    private Button SaveNote;

    private EditText WriteNote;

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

            }
        });
    }
}
