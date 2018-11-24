package aahmf.notepad;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


public class NoteEntry {
    private int id;
    private String title;
    SharedPreferences mSharedPref;

    private int priorityColor;



    public NoteEntry(int id, String title) {
        this.id = id;
        this.title = title;
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
}
