package aahmf.notepad;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


public class NoteEntry {
    private int id;
    private String title, kwords;
    SharedPreferences mSharedPref;
    private int priorityColor;





    public NoteEntry(int id, String title, String kwords) {
        this.id = id;
        this.title = title;
        this.kwords = kwords;


    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public String getKwords() { return kwords; }


    public int getPriority(Context ctx) {
        mSharedPref = ctx.getSharedPreferences("NoteColor", MODE_PRIVATE);
        this.priorityColor = mSharedPref.getInt(title,ctx.getResources().getColor(R.color.colorWhite));
        return priorityColor;
    }
}
