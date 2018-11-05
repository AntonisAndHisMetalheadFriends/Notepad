package aahmf.notepad;

public class NoteEntry {
    private int id;
    private String title;
    private int image;


    public NoteEntry(int id, String title, int image, String shortDesc) {
        this.id = id;
        this.title = title;
        this.image = image;

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }

}
