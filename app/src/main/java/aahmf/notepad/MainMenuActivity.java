package aahmf.notepad;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainMenuActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NoteEntryAdapter adapter;
    List<NoteEntry> noteEntryList = new ArrayList<>();
    private FirebaseAuth kAuth;
    String path = "/data/user/0/aahmf.notepad/files";
    File directory = new File(path);
    File[] files = directory.listFiles();
    boolean desc = false;
    String keyz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        EditText editText = findViewById(R.id.edittext);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


        setAnTestFile();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0; i < files.length; i++) {
            noteEntryList.add(new NoteEntry(i, files[i].getName()));
        }
        adapter = new NoteEntryAdapter(this, noteEntryList);
        recyclerView.setAdapter(adapter);
        kAuth = FirebaseAuth.getInstance();


    }

    private void filter(String text) {
        ArrayList<NoteEntry> filteredList = new ArrayList<>();
        NoteEntryAdapter NEA = new NoteEntryAdapter(this, filteredList);
        for (NoteEntry noteEntry : noteEntryList) {
            if (noteEntry.getTheKeywords(MainMenuActivity.this)!=null){
                keyz =noteEntry.getTheKeywords(MainMenuActivity.this);
            }
            else{
                keyz = "";
            }
            if (noteEntry.getTitle().toLowerCase().contains(text.toLowerCase()) || keyz.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(noteEntry);
            }
        }
        NEA.filterList(filteredList);
        recyclerView.setAdapter(NEA);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_one, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                logOut();
                break;

            case R.id.newnote:
                startActivity(new Intent(MainMenuActivity.this, NewNoteActivity.class));
                break;

            case R.id.sortByName:
                sortByName(desc);
                adapter.notifyDataSetChanged();
                desc ^= true;
                break;
            case R.id.sortByPrio:
                sortByPriority();
                adapter.notifyDataSetChanged();
                break;
            case R.id.sortByKeywords:
                sortByKeywords();
                adapter.notifyDataSetChanged();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        kAuth.signOut();
        startActivity(new Intent(MainMenuActivity.this, LogInActivity.class));
        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();

    }

    public void setAnTestFile() {
        try {
            FileOutputStream fileOutputStream = openFileOutput("Test", MODE_PRIVATE);
            fileOutputStream.write("Test".getBytes());
            fileOutputStream.close();
        } catch (IOException e) {

        }

    }

    public String findNoteTitle(int pos, List<NoteEntry> list) {
        String Title;
        NoteEntry NE = list.get(pos);
        Title = NE.getTitle();
        return Title;
    }

    public String getPath() {
        return path;
    }

    public void LoadFiles(String path1, List<NoteEntry> EntryList) {
        EntryList = new ArrayList<>();
        File directory1 = new File(path1);
        File[] list1 = directory1.listFiles();

        for (int i = 0; i < list1.length; i++) {
            EntryList.add(new NoteEntry(i, list1[i].getName()));
        }

    }


    public void sortByName(boolean desc) {
        if (desc == false) {
            Collections.sort(noteEntryList, new Comparator<NoteEntry>() {

                @Override
                public int compare(NoteEntry note1, NoteEntry note2) {
                    return note1.getTitle().compareTo(note2.getTitle());
                }

            });
        } else {
            Collections.sort(noteEntryList, new Comparator<NoteEntry>() {

                @Override
                public int compare(NoteEntry note1, NoteEntry note2) {
                    return note2.getTitle().compareTo(note1.getTitle());
                }

            });
        }
    }

   public void sortByPriority() {
            Collections.sort(noteEntryList, new Comparator<NoteEntry>() {
                @Override
                public int compare(NoteEntry note1, NoteEntry note2) {
                    return Integer.compare(note1.getPriority(MainMenuActivity.this),note2.getPriority(MainMenuActivity.this));
                     }
                });
    }

    public void sortByKeywords() {
        Collections.sort(noteEntryList, new Comparator<NoteEntry>() {
            @Override
            public int compare(NoteEntry note1, NoteEntry note2) {
                if (note1.getTheKeywords(MainMenuActivity.this) != null && note2.getTheKeywords(MainMenuActivity.this) != null) {
                    return note1.getTheKeywords(MainMenuActivity.this).compareTo(note2.getTheKeywords(MainMenuActivity.this));
                }
                else {
                    return note2.getTitle().compareTo(note1.getTitle());
                }
                }
        });
    }

}



