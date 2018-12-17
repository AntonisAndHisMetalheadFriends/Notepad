package aahmf.notepad;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Date;


public class MainMenuActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NoteEntryAdapter adapter;
    List<NoteEntry> noteEntryList = new ArrayList<>();
    List<NoteEntry> deleteList = new ArrayList<>();
    private FirebaseAuth kAuth;
    String path = "/data/user/0/aahmf.notepad/files";
    File directory = new File(path);
    File[] files = directory.listFiles();
    private boolean isSelected = NoteEntryAdapter.getSelected();
    private static Menu men;
    private String Date,Kwords;

    private DatabaseReference mNotes = FirebaseDatabase.getInstance().getReference("Notes");
    private DatabaseReference mImages = FirebaseDatabase.getInstance().getReference("Images");
    private DatabaseReference mFiles = FirebaseDatabase.getInstance().getReference("Files");
    private FirebaseUser user = LogInActivity.getUser();



    boolean desc = false;
    String keyz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if(user==null)
            user=FirebaseAuth.getInstance().getCurrentUser();
        GetOnlineNotes(user.getUid());
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






        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*for(int i = 0;i<files.length;i++)
        {
            if(files[i].getName().matches("instant-run"))
            {

            }
            else {
                loadXML(files[i].getName());
                noteEntryList.add(new NoteEntry(i, files[i].getName(), Date, Kwords));
            }
        }*/


        adapter = new NoteEntryAdapter(this, noteEntryList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        kAuth = FirebaseAuth.getInstance();


    }

    private void filter(String text) {
        ArrayList<NoteEntry> filteredList = new ArrayList<>();
        NoteEntryAdapter NEA = new NoteEntryAdapter(this, filteredList);
        for (NoteEntry noteEntry : noteEntryList) {
            if (noteEntry.getKwords()!=null){
                keyz =noteEntry.getKwords();
            }
            else{
                keyz = "";
            }
            if (noteEntry.getTitle().toLowerCase().contains(text.toLowerCase()) || keyz.toLowerCase().contains(text.toLowerCase())
                    ||noteEntry.getDate().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(noteEntry);
            }
        }
        NEA.filterList(filteredList);
        recyclerView.setAdapter(NEA);
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_one, menu);
            men = menu;
            setIconOnOverflowMenu(menu,R.id.newnote,R.string.action_create_new_note,R.drawable.newnote);
            setIconOnOverflowMenu(menu,R.id.sign_out,R.string.sign_out,R.drawable.signout);
            return true;
        }

     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.sign_out:
                logOut();
                break;

            case R.id.newnote:
                startActivity(new Intent(MainMenuActivity.this,NewNoteActivity.class));
                break;

            case  R.id.ShowMap:
                startActivity(new Intent(MainMenuActivity.this,GeoLocationOfNotesActivity.class));
                break;

            case R.id.Sort:
                final CharSequence[] items = {"By Title","By Priority","By Keywords","By Date"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
                builder.setTitle("Sort Method");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0:
                                sortByName(desc);
                                adapter.notifyDataSetChanged();
                                desc ^= true;
                                break;
                            case 1:
                                sortByPriority();
                                adapter.notifyDataSetChanged();
                                break;
                            case 2:
                                sortByKeywords();
                                adapter.notifyDataSetChanged();
                                break;
                            case 3:
                                sortByDate();
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });
                builder.create();
                builder.show();
                break;

            case R.id.CancelSelection:
               NoteEntryAdapter.setIsSelected(false);
               CancelSelection(noteEntryList);
                adapter.notifyDataSetChanged();
                setItemVisible(men,R.id.CancelSelection,false);
                setItemVisible(men,R.id.delete_notes,false);
                //recreate();
                break;
            case R.id.delete_notes:
                NoteEntryAdapter.setIsSelected(false);
                DeleteMultipleNotes();
                setItemVisible(men,R.id.CancelSelection,false);
                setItemVisible(men,R.id.delete_notes,false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        kAuth.signOut();
            startActivity(new Intent(MainMenuActivity.this, LogInActivity.class));
            Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();

        }
    public String findNoteTitle(int pos,List<NoteEntry> list)
    {
        String Title;
        NoteEntry NE = list.get(pos);
        Title = NE.getTitle();
        return Title;
    }

    public void SetCheckBoxtrue(boolean selected)
    {
        if(selected==true)
        {
            setItemVisible(men,R.id.CancelSelection,true);
            setItemVisible(men,R.id.delete_notes,true);
        }
        else
        {
            setItemVisible(men,R.id.CancelSelection,false);
            setItemVisible(men,R.id.delete_notes,false);
        }



    }



    public void CancelSelection(List<NoteEntry> list)
    {
        List<Button> Buttons = adapter.getEdit();
        List<CheckBox> checkBoxes = adapter.getCheck();
        if(isSelected==false)
        {
        for(int i=0;i<Buttons.size();i++)
        {

            Button Edit = Buttons.get(i);
            CheckBox box = checkBoxes.get(i);
            box.setVisibility(View.INVISIBLE);
            Edit.setVisibility(View.VISIBLE);
            box.setChecked(false);
           adapter.notifyDataSetChanged();
        }}
        adapter.getEdit().clear();
        adapter.getCheck().clear();
    }

    public void DeleteMultipleNotes()
    {
        List<Integer> position = adapter.getPositionList();
        deleteList = NoteEntryAdapter.deleteList1;
        int y=deleteList.size();
        for(int i=0;i<y;i++)
        {
            NoteEntry NE = deleteList.get(i);
            String Title = NE.getTitle();
            final Query query = mNotes.orderByChild("NoteTitle").equalTo(Title);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child:dataSnapshot.getChildren())
                    {
                        int Noteid = Integer.parseInt(child.getKey());
                        Query queryremoveimage = mImages.orderByChild("note_id").equalTo(Noteid);
                        queryremoveimage.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child:dataSnapshot.getChildren())
                                {
                                    child.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Query queryremoveFile = mFiles.orderByChild("note_id").equalTo(Noteid);
                        queryremoveFile.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child:dataSnapshot.getChildren())
                                {
                                    child.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        child.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            /*int temppos = position.get(i);
            noteEntryList.remove(temppos);
            recyclerView.removeViewAt(temppos);
            adapter.notifyItemRemoved(temppos);
            adapter.notifyItemRangeRemoved(temppos,noteEntryList.size());*/
        }
        noteEntryList.removeAll(deleteList);
        adapter.notifyDataSetChanged();
        adapter.getPositionList().clear();
        deleteList.clear();
    }
    public String getPath()
    {
        return path;
    }

    public void setIconOnOverflowMenu(Menu men,int menuit,int labelid,int icon)
    {
        MenuItem item = men.findItem(menuit);
        SpannableStringBuilder builder = new SpannableStringBuilder("   "+getResources().getString(labelid));
        builder.setSpan(new ImageSpan(this,icon),0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);
    }

    public void setItemVisible(Menu men,int menit,boolean visibility)
    {
        MenuItem item = men.findItem(menit);
        item.setVisible(visibility);
    }
    public void setDeleteList(List<NoteEntry> deleteList) {
        this.deleteList = deleteList;
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

            switch (eventType) {

                case XmlPullParser.START_TAG:

                    String tagname = xpp.getName();


                    if (tagname.equalsIgnoreCase("Date")) {
                        try {
                            eventType = xpp.next();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                        Date = xpp.getText();


                    }

                    if(tagname.equalsIgnoreCase("keywords"))
                    {
                        try {
                            eventType = xpp.next();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                        Kwords = xpp.getText();
                    }


                    break;
            }
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
            }}
}
    public void sortByName(boolean desc) {
        if (desc == false) {
            Collections.sort(noteEntryList, new Comparator<NoteEntry>() {

                @Override
                public int compare(NoteEntry note1, NoteEntry note2) {
                    return note1.getTitle().toUpperCase().compareTo(note2.getTitle().toUpperCase());
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
                if (note1.getKwords() != null && note2.getKwords() != null) {
                    return note1.getKwords().compareTo(note2.getKwords());
                }
                else {
                    return note2.getTitle().compareTo(note1.getTitle());
                }
            }
        });
    }

    public void sortByDate() {
        Collections.sort(noteEntryList, new Comparator<NoteEntry>() {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date1,date2;
            @Override
            public int compare(NoteEntry note1, NoteEntry note2) {
                if (note1.getDate() != null && note2.getDate() != null) {
                    try {
                        date1 = df.parse(note1.getDate());
                        date2 = df.parse(note2.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return date2.compareTo(date1);
                }
                else {
                    return note2.getTitle().compareTo(note1.getTitle());
                }
            }
        });
    }

    public void GetOnlineNotes(final String uid)
    {

        mNotes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 Query query = mNotes.orderByChild("UserId").equalTo(uid);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int counter = (int)dataSnapshot.getChildrenCount();
                            int y=1;
                            int found=0;
                            if(dataSnapshot.exists())
                            {

                                    while (found!=counter) {

                                        if(dataSnapshot.hasChild(String.valueOf(y))) {
                                            Date = dataSnapshot.child(String.valueOf(y)).child("DateOfCreation").getValue().toString();
                                            Kwords = dataSnapshot.child(String.valueOf(y)).child("Keywords").getValue().toString();
                                            String Title = dataSnapshot.child(String.valueOf(y)).child("NoteTitle").getValue().toString();
                                            String Location = dataSnapshot.child(String.valueOf(y)).child("Address").getValue().toString();
                                            noteEntryList.add(new NoteEntry(y, Title, Date, Kwords,Location));
                                            y++;
                                            adapter.notifyDataSetChanged();
                                            found++;

                                        }
                                        else
                                        {
                                            y++;
                                        }
                                    }



                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

}








