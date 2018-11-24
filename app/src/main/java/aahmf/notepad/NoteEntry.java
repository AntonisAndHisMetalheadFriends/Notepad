package aahmf.notepad;

public class NoteEntry {
    private int id;
    private String title;



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


}
