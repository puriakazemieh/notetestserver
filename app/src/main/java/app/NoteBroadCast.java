package app;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import object.NoteObject;

public class NoteBroadCast {
    public static final int ACTION_ADD = 1;
    public static final int ACTION_EDIT = 2;
    public static final int ACTION_DELETE = 3;

    public static void send(int action, NoteObject note) {
        Intent intent = new Intent(app.main.tag);
        intent.putExtra("action", action);
        intent.putExtra("object", note);
        LocalBroadcastManager.getInstance(Application.getContext()).sendBroadcast(intent);
        app.l("broadcast send   ");
    }
    public static void send(int positon,int action, NoteObject note) {
        Intent intent = new Intent(app.main.tag);
        intent.putExtra("action", action);
        intent.putExtra("object", note);
        intent.putExtra("positon", positon);
        LocalBroadcastManager.getInstance(Application.getContext()).sendBroadcast(intent);
        app.l("broadcast send   ");
    }
}
