package aahmf.notepad;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainMenuActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NoteEntryAdapter adapter;
    List<NoteEntry> noteEntryList;
    private FirebaseAuth kAuth;
    private NewNoteActivity NNa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setAnTestFile();
        String path = "/data/user/0/aahmf.notepad/files";
        File directory = new File(path);
        File[] files = directory.listFiles();
       /* for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }*/


        noteEntryList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        for(int i = 0;i<files.length;i++)
        {
            noteEntryList.add(new NoteEntry(i,files[i].getName(),R.drawable.noteicon,""));
        }
       /* noteEntryList.add(
                new NoteEntry(1,"Invoker",R.drawable.ka_el,"Mid"));
        noteEntryList.add(
                new NoteEntry(2,"Akasha",R.drawable.akasha,"Mid"));
        noteEntryList.add(
                new NoteEntry(3,"Krobelus",R.drawable.krobelus,"Mid"));
        noteEntryList.add(
                new NoteEntry(4,"Lanaya",R.drawable.lanaya,"Mid"));
        noteEntryList.add(
                new NoteEntry(5,"Mercurial",R.drawable.mercurial,"Carry"));
        noteEntryList.add(
                new NoteEntry(6,"Mireska",R.drawable.mireska,"Support"));
        noteEntryList.add(
                new NoteEntry(7,"Mortred",R.drawable.mortred,"Carry"));
        noteEntryList.add(
                new NoteEntry(8,"Alleria",R.drawable.alleria,"Mid"));*/
        adapter = new NoteEntryAdapter(this, noteEntryList);
        recyclerView.setAdapter(adapter);
        kAuth = FirebaseAuth.getInstance();




    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_one, menu);
            return true;
        }

     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.sign_out:
                logOut();
                break;
            case R.id.settings:
                startActivity(new Intent(MainMenuActivity.this,SettingsActivity.class));
                break;

            case R.id.newnote:
                startActivity(new Intent(MainMenuActivity.this,NewNoteActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        kAuth.signOut();
            startActivity(new Intent(MainMenuActivity.this, LogInActivity.class));
            Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();

        }

        public void setAnTestFile()
        {
            try {
                FileOutputStream fileOutputStream = openFileOutput("Test", MODE_PRIVATE);
                fileOutputStream.write("Test".getBytes());
                fileOutputStream.close();
            }
            catch (IOException e)
            {

            }

        }

}
