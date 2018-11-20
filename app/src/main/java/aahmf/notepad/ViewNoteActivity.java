package aahmf.notepad;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import static java.security.AccessController.getContext;

public class ViewNoteActivity extends AppCompatActivity {
    EditText NoteText;
    TextView TitleText;
    static  String  Title;
    MainMenuActivity Main = new MainMenuActivity();
    String path = Main.getPath();
    int position;
    int i=0;
    Button buttonDel;
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;
    private  ArrayList<Uri> Images = new ArrayList<Uri>();
    private static final int PERMISSIONS_REQUEST_READ_MEDIA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        NoteText = findViewById(R.id.NoteText);
        TitleText = findViewById(R.id.Title);
        buttonDel = findViewById(R.id.btnDelete);
        gvGallery = findViewById(R.id.gv);

        NoteText.setFocusable(false);
        NoteText.setClickable(false);
        Title = NoteEntryAdapter.getTitle();
        TitleText.setText(Title);
        loadXML(Title);



        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

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


                        if (tagname.equalsIgnoreCase("Image" + i)) {
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
}
