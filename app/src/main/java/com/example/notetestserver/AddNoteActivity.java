package com.example.notetestserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import app.*;
import cz.msebera.android.httpclient.Header;
import object.NoteObject;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back, save;
    EditText title, message;

    SpinKitView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        init();
    }


    private void init() {
        back = findViewById(R.id.back);
        save = findViewById(R.id.save);
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        progressBar = findViewById(R.id.progressBar);
        title.setText(spref.get().getString(ROUTER.INPUT_TITLE, ""));
        message.setText(spref.get().getString(ROUTER.INPUT_MESSAGE, ""));
        title.setTypeface(MainActivity.font);
        message.setTypeface(MainActivity.font);
        back.setOnClickListener(this);
        save.setOnClickListener(this);


        title.setTypeface(MainActivity.font);
        message.setTypeface(MainActivity.font);
    }


    public void onClick(View view) {
        if (view == back) {
            spref.get().edit()
                    .putString(ROUTER.INPUT_TITLE, title.getText().toString())
                    .putString(ROUTER.INPUT_MESSAGE, message.getText().toString())
                    .apply();
            finish();
        } else {
            saveNote();
        }
    }

    private void saveNote() {
        if (title.getText().toString().length() == 0) {
            app.t("enter title ", app.ToasetType.error);
            app.animateError(title);
            return;
        }
        if (message.getText().toString().length() == 0) {
            app.t("enter message ", app.ToasetType.error);
            app.animateError(title);
            return;
        }
        RequestParams params = app.getRequestParams();
        params.put(ROUTER.ROUTE, ROUTER.ROUTE_NOTES);
        params.put(ROUTER.ACTION, ROUTER.ACTION_ADD);
        params.put(ROUTER.INPUT_TITLE, title.getText().toString());
        params.put(ROUTER.INPUT_MESSAGE, message.getText().toString());
        app.l(params.toString());
        NoteClient.post(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responce = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(responce);
                    if (jsonObject.has("error")) {
                        app.t(jsonObject.getString("error"), app.ToasetType.error);
                    } else if (jsonObject.has("stateus") && jsonObject.getString("stateus").equals("SUCSESS")) {
                        int id = jsonObject.getInt("id");
                        spref.get().edit().remove(ROUTER.INPUT_TITLE).remove(ROUTER.INPUT_MESSAGE).apply();
                        NoteObject noteObject = new NoteObject(
                                id,
                                spref.get().getInt(ROUTER.USER_ID, -1),
                                title.getText().toString(),
                                message.getText().toString(),
                                0,
                                "");
                        NoteBroadCast.send(NoteBroadCast.ACTION_ADD, noteObject);
                        finish();
                    }

                } catch (
                        JSONException e) {

                }
                app.l(responce);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                    error) {

            }

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
                super.onStart();
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                super.onFinish();
            }
        });

    }
}