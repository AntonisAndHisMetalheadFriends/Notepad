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
}
