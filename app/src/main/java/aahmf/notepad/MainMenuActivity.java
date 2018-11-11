package aahmf.notepad;


import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class MainMenuActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NoteEntryAdapter adapter;
    List<NoteEntry> noteEntryList;
    private FirebaseAuth kAuth;




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
        }

        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        kAuth.signOut();
            startActivity(new Intent(MainMenuActivity.this, LogInActivity.class));
            Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();

        }

}
