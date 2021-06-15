package object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NoteObject implements Serializable {
    //"id":"23","user_id":"44","title":"gyrdetg","message":"tertfer","seen":"0","date":"2021-06-08 15
    private int id;
    private int user_id;
    private String title;
    private String message;
    private int seen;
    private String date;

    public NoteObject() {

    }

    public NoteObject(int id, int user_id, String title, String message, int seen, String date) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.message = message;
        this.seen = seen;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public int getSeen() {
        return seen;
    }

    public String getDate() {
        return date;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }
}
