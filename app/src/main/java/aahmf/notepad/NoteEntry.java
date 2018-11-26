package aahmf.notepad;

import android.widget.Button;
import android.widget.CheckBox;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import static android.content.Context.MODE_PRIVATE;


public class NoteEntry {
    private int id;
    private String title;
    private boolean isSelected;
    private CheckBox checkBox;
    private Button editButton;
    private String kwords;
    private String date;
    private String title;
    SharedPreferences mSharedPref;
    private int priorityColor;
    private String keywords;




    public NoteEntry(int id, String title,String date,String kwords) {
        this.id = id;
        this.title = title;
        this.date=date;
        this.kwords=kwords;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public int getPriority(Context ctx) {
        mSharedPref = ctx.getSharedPreferences("NoteColor", MODE_PRIVATE);
        this.priorityColor = mSharedPref.getInt(title,ctx.getResources().getColor(R.color.colorWhite));
        return priorityColor;
    }

    public String getTheKeywords(Context ctx)  {
        try{
            FileInputStream fis = ctx.openFileInput(title);
            StringBuilder sb = new StringBuilder();
            String fileContent;
            while (fis.available() > 0) {
                sb.append((char)fis.read());
            }
            fileContent = sb.toString();
            fis.close();
            int indexStart = fileContent.indexOf("<keywords>") + 10;
            int indexEnd = fileContent.indexOf("</keywords>");
            try {
                this.keywords = fileContent.substring(indexStart,indexEnd);
            }
            catch (StringIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.keywords;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setKwords(String kwords) {
        this.kwords = kwords;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public Button getEditButton() {
        return editButton;
    }

    public void setEditButton(Button editButton) {
        this.editButton = editButton;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
