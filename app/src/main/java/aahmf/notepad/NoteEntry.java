package aahmf.notepad;

public class NoteEntry {
    private int id;
    private String title, kwords;



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


}
