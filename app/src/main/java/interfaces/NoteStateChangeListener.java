package interfaces;

public interface NoteStateChangeListener {
    void onChange(int position, int noteId, int state,boolean sucses);

    void onStart();

    void onFinish();
}
