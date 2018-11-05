package aahmf.notepad;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainMenuActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NoteEntryAdapter adapter;
    List<NoteEntry> noteEntryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        noteEntryList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteEntryList.add(
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
                new NoteEntry(8,"Alleria",R.drawable.alleria,"Mid"));
        adapter = new NoteEntryAdapter(this, noteEntryList);
        recyclerView.setAdapter(adapter);
    }


}
