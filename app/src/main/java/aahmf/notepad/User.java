package aahmf.notepad;

public class User {

    private String Name,Username;

    public User()
    {

    }

    public User(String name, String username) {
        Name = name;
        Username = username;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
