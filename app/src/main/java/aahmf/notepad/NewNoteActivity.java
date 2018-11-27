package aahmf.notepad;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewNoteActivity extends AppCompatActivity {

    private Button ExportNote;
    private Button ImportNote;
    private Button CancelNote;
    private Spinner noteClr;
    private static final String[] coloursTwo = {"White", "Green", "Yellow", "Red"};
    int bgColor;
    private EditText WriteNote,Title, Keywords;


    private String NoteTitle, Date;
    private List<Uri> filePaths;
    private final int File_Request_Code = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        filePaths=new ArrayList<>();
        Gallery.ImagePaths=new ArrayList<>();
        ExportNote = findViewById(R.id.btnExport);
        ImportNote = findViewById(R.id.btnImport);
        CancelNote = findViewById(R.id.btnCancelNewNote);
        noteClr = findViewById(R.id.spNoteClr);
        noteClr.setPrompt("Priority");
        WriteNote = findViewById(R.id.etWriteNote);
        Keywords = findViewById(R.id.etKeywords);
        Calendar calendar = Calendar.getInstance();
        Date = DateFormat.getDateInstance().format(calendar.getTime());


        CancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewNoteActivity.this, MainMenuActivity.class));
            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newnote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.save:
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
                            Gallery.ImagePaths=new ArrayList<>();
                        }

                    });

                    DialogBuilder.create();
                    DialogBuilder.show();

                }
                break;

            case R.id.addfile:
                Intent intent=new Intent(NewNoteActivity.this,FileGallery.class);
                for(int z=0;z<filePaths.size();z++){
                    intent.putExtra("File"+z,filePaths.get(z).toString());
                }
                startActivityForResult(intent,File_Request_Code);
                break;
            case R.id.image:
                startActivity(new Intent(NewNoteActivity.this, Gallery.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getDate() {
        return Date;
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
            for(int i = 0;i<Gallery.ImagePaths.size();i++)
            {
                xmlSerializer.startTag(null,"Image"+i);
                xmlSerializer.text(Gallery.ImagePaths.get(i).toString());
                xmlSerializer.endTag(null,"Image"+i);
            }
            for(int i = 0;i<filePaths.size();i++)
            {
                xmlSerializer.startTag(null,"File"+i);
                xmlSerializer.text(filePaths.get(i).toString());
                xmlSerializer.endTag(null,"File"+i);
            }
            xmlSerializer.startTag(null, "keywords");
            String kwords = Keywords.getText().toString();
            xmlSerializer.text(kwords);
            xmlSerializer.endTag(null, "keywords");
            xmlSerializer.startTag(null, "Date");
            xmlSerializer.text(Date);
            xmlSerializer.endTag(null, "Date");
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

            Intent intent=new Intent(NewNoteActivity.this,MainMenuActivity.class);
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
                        if(!filePaths.contains(temp)){
                            filePaths.add(temp);
                        }
                        z=z+1;
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
