package aahmf.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

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

public class ViewNoteActivity extends AppCompatActivity {
    EditText NoteText;
    TextView TitleText;
    static  String  Title;
    MainMenuActivity Main = new MainMenuActivity();
    String path = Main.getPath();
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        NoteText = findViewById(R.id.NoteText);
        TitleText = findViewById(R.id.Title);

        NoteText.setFocusable(false);
        NoteText.setClickable(false);
        Title = NoteEntryAdapter.getTitle();
        TitleText.setText(Title);
        loadXML(Title);


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
                }
            }
            String Text = userData.get(0);
            //String password = userData.get(1);
            NoteText.setText(Text);
        }

}
