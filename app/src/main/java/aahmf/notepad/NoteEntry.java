package aahmf.notepad;

import android.widget.Button;
import android.widget.CheckBox;

public class NoteEntry {
    private int id;
    private String title;
    private boolean isSelected;
    private CheckBox checkBox;
    private Button editButton;
    private String kwords;
    private String date;



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
    public String getKwords() { return kwords; }

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
