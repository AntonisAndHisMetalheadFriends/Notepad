package aahmf.notepad;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;

public class ViewNoteActivity extends AppCompatActivity {
    EditText NoteText;
    TextView TitleText;
    static  String  Title;
    MainMenuActivity Main = new MainMenuActivity();
    String path = Main.getPath();
    int position;
    int i=0;
    private GridView gvGallery,gvfGallery;
    private GalleryAdapter galleryAdapter;
    private FileGalleryAdapter fileGalleryAdapter;
    protected  static ArrayList<Uri> Images = new ArrayList<Uri>();
    protected ArrayList<Uri>filesUris = new ArrayList<Uri>();
    private static final int PERMISSIONS_REQUEST_READ_MEDIA = 100;
    private int id = NoteEntryAdapter.getId();
    private DatabaseReference mNotes = FirebaseDatabase.getInstance().getReference("Notes");
    private FirebaseUser user = LogInActivity.getUser();
    private String uid2= user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        NoteText = findViewById(R.id.NoteText);
        TitleText = findViewById(R.id.Title);
        gvGallery = findViewById(R.id.gv);
        gvfGallery = findViewById(R.id.gvf);

        NoteText.setFocusable(false);
        NoteText.setClickable(false);
        Title = NoteEntryAdapter.getTitle();
        GetOnlineNotes(uid2,Title);
        TitleText.setText(Title);
        Images.clear();
        //loadXML(Title);
        gvGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Uri abc = null;
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // TODO Auto-generated method stub

                Intent intent =new Intent(getApplicationContext(),FullScreenActivity.class);
                intent.setData(Images.get(position));
                startActivity(intent);
            }
        });

    }

    public void loadXML(String file) {

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


                        //if (tagname.equalsIgnoreCase("Image" + i)) {
                        if (tagname.contains("Image")) {
                                try {
                                    eventType = xpp.next();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();
                                }
                                //eventType=XmlPullParser.TEXT;
                                // if (eventType == XmlPullParser.TEXT)

                                Images.add(Uri.parse(xpp.getText()));

                                galleryAdapter = new GalleryAdapter(getApplicationContext(), Images);
                                gvGallery.setAdapter(galleryAdapter);
                                gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                        .getLayoutParams();
                                mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);
                                i++;
                                //}
                            }

                        //if (tagname.equalsIgnoreCase("File" + i)) {

                        if (tagname.contains("File")) {
                            try {
                                eventType = xpp.next();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            }
                            //eventType=XmlPullParser.TEXT;
                            // if (eventType == XmlPullParser.TEXT)

                            filesUris.add(Uri.parse(xpp.getText()));

                            fileGalleryAdapter = new FileGalleryAdapter(getApplicationContext(), filesUris);
                            gvfGallery.setAdapter(fileGalleryAdapter);
                            gvfGallery.setVerticalSpacing(gvfGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvfGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvfGallery.getHorizontalSpacing(), 0, 0);
                            i++;
                            //}
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








            String Text = userData.get(0);
            //String password = userData.get(1);
            NoteText.setText(Text);
        }

        public void pinToStatusBar()
        {
            Intent intent = new Intent(this, ViewNoteActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            Notification.Builder NB = new Notification.Builder(this);
            NB.setContentTitle(Title);
            NB.setContentText(NoteText.getText().toString());
            NB.setSmallIcon(R.mipmap.ic_launcher);
            NB.setPriority(Notification.PRIORITY_MAX);
            NB.setContentIntent(pendingIntent);
            NB.setAutoCancel(true);
            notificationManager.notify(id,NB.build());



        }

        public void deleteNote(String filename) {
        try{
            File dir = new File("/data/user/0/aahmf.notepad/files");
            File file =new File (dir,filename);

            if(file.exists()){
                file.delete();
                SharedPreferences mSharedPref = getSharedPreferences("NoteColor", MODE_PRIVATE);
                mSharedPref.edit().remove(Title).apply();
            }
         }catch(Exception e) {
            e.printStackTrace();
            }
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_two,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete_Note:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ViewNoteActivity.this);
                builder1.setTitle("Delete note");
                builder1.setMessage("Are you sure you want to delete this note?");
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNote(Title);
                        Toast.makeText(ViewNoteActivity.this,"Note deleted",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ViewNoteActivity.this, MainMenuActivity.class));
                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder1.create();
                builder1.show();
                break;

            case R.id.PinToStatus:
                pinToStatusBar();
                Toast.makeText(this, "Note Pinned Successfully", Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void GetOnlineNotes(final String uid,final  String Tit)
    {

        mNotes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Query query = mNotes.orderByChild("UserId_Title").equalTo(uid+"_"+Tit);

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
                                    NoteText.setText(dataSnapshot.child(String.valueOf(y)).child("Content").getValue().toString());

                                    y++;
                                    found++;

                                }
                                else
                                {
                                    y++;
                                }
                            }

                        }
                        //Toast.makeText(MainMenuActivity.this,"Den Mphke Sthn IF",Toast.LENGTH_LONG).show();

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
}
