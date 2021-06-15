package com.example.notetestserver;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import interfaces.NoteStateChangeListener;
import object.LoginObject;
import object.NoteObject;
import app.*;

public class ShowNoteAvtivity extends AppCompatActivity implements View.OnClickListener {
    ImageView back, icon, state, delete;
    TextView title, message;
    FloatingActionButton floatingActionButton;
    SpinKitView spinKitView;
    NoteObject noteObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note_avtivity);
        initViews();
    }

    private void initViews() {
        noteObject = (NoteObject) getIntent().getSerializableExtra("object");
        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);
        state = findViewById(R.id.state);
        icon = findViewById(R.id.icon);
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);

        floatingActionButton = findViewById(R.id.floatingActionButton2);
        spinKitView = findViewById(R.id.progressBar);

        title.setTypeface(MainActivity.font);
        message.setTypeface(MainActivity.font);


        back.setOnClickListener(this::onClick);
        delete.setOnClickListener(this::onClick);
        state.setOnClickListener(this::onClick);
        floatingActionButton.setOnClickListener(this::onClick);

        init();
    }

    private void init() {

        app.l(noteObject.getTitle());
        title.setText(noteObject.getTitle());
        message.setText(noteObject.getMessage());
        icon.setImageDrawable(app.getImage(noteObject));
        state.setImageResource(
                noteObject.getSeen() == 1 ? R.drawable.ic_round_remove_red_eye_24 : R.drawable.ic_round_visibility_off_24
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                finish();
                break;
            }
            case R.id.delete: {
                startDelete();
                break;
            }
            case R.id.state: {
                changeState();
                break;
            }
            case R.id.floatingActionButton2: {
                Intent intent = new Intent(this, EditNoteActivity.class);
                intent.putExtra("object", noteObject);
                startActivity(intent);
                break;
            }
        }

    }
    private void startDelete(){
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("elete note");
        alert.setMessage("واقعا می خوایی حذف کنی؟");
        alert.setPositiveButton("ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete();
            }
        });
        alert.setNegativeButton("nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    private void delete() {
        RequestParams params = app.getRequestParams();
        params.put(ROUTER.ROUTE, ROUTER.ROUTE_NOTES);
        params.put(ROUTER.ACTION, ROUTER.ACTION_DELETE);
        params.put(ROUTER.INPUT_NOTE_ID, noteObject.getId());
        NoteClient.post(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responce = new String(responseBody);
                if (responce.equals(ROUTER.SUCSESS)) {
                    NoteBroadCast.send(NoteBroadCast.ACTION_DELETE, noteObject);
                    finish();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        if (jsonObject.has("error")) {
                            app.t(jsonObject.getString("error"), app.ToasetType.error);

                        }
                    } catch (Exception e) {

                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onStart() {
                spinKitView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                spinKitView.setVisibility(View.GONE);
            }
        });

    }

    private void changeState() {

        app.changeNoteState(app.pos, noteObject, new NoteStateChangeListener() {
            @Override
            public void onChange(int position, int noteId, int state, boolean sucses) {
                if (sucses) {
                    noteObject.setSeen(state);
                    NoteBroadCast.send(position, NoteBroadCast.ACTION_EDIT, noteObject);
                    ShowNoteAvtivity.this.state.setImageResource(
                            noteObject.getSeen() == 1 ? R.drawable.ic_round_remove_red_eye_24 : R.drawable.ic_round_visibility_off_24
                    );
                }
            }

            @Override
            public void onStart() {
                spinKitView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                spinKitView.setVisibility(View.GONE);
            }
        });

    }
}