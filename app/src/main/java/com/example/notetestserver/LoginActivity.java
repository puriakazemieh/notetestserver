package com.example.notetestserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import app.*;
import cz.msebera.android.httpclient.Header;
import object.LoginObject;
import object.UserObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email, password;
    Button ok, register;
    SpinKitView spin_kit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init() {
        spin_kit = findViewById(R.id.spin_kit);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        ok = findViewById(R.id.ok);
        register = findViewById(R.id.register);
        ok.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == ok.getId()) {
            login();
        } else {
            startActivity(new Intent(this, RegistrationActivity.class));
        }

    }

    private void login() {
        if (email.getText().toString().length() < 5) {
            app.t("please enter your email ", app.ToasetType.error);
        }
        if (password.getText().toString().length() < 3) {
            app.t("please enter at least 3 chars as password ", app.ToasetType.error);
        }
        RequestParams params = app.getRequestParams();
        params.put(ROUTER.ROUTE, ROUTER.ROUTE_LOGIN);
        params.put(ROUTER.ACTION, ROUTER.ACTION_LOGIN);
        params.put(ROUTER.INPUT_EMAIL, email.getText().toString());
        params.put(ROUTER.INPUT_PASSWORD, password.getText().toString());

        app.l(params + "");
        NoteClient.post(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responce = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(responce);
                    if (jsonObject.has("error")) {
                        app.t(jsonObject.getString("error"), app.ToasetType.error);
                    } else {
                        LoginObject loginObject = new Gson().fromJson(responce, LoginObject.class);

                        if (loginObject.getState().equals(ROUTER.SUCSESS)) {
                            spref.get().edit()
                                    .putString(ROUTER.SESSION, loginObject.getUserObject().getSession())
                                    .putInt(ROUTER.USER_ID, loginObject.getUserObject().getId())
                                    .putString(ROUTER.INPUT_EMAIL, loginObject.getUserObject().getEmail())
                                    .putString(ROUTER.INPUT_FNAME, loginObject.getUserObject().getFname())
                                    .putString(ROUTER.INPUT_LNAME, loginObject.getUserObject().getLname())
                                    .putInt(ROUTER.INPUT_SEX, loginObject.getUserObject().getSex())
                                    .apply();

                            spref.get().edit()
                                    .putInt("nightMode", loginObject.getSettingsObject().getNightMode())
                                    .putString("font", loginObject.getSettingsObject().getSETTINGS_FONT())
                                    .putInt("bgcolor", loginObject.getSettingsObject().getBgColor())
                                    .putInt("textcolor", loginObject.getSettingsObject().getTextColor())
                                    .putInt("fontSize", loginObject.getSettingsObject().getFontSize())
                                    .putInt("avatarId", loginObject.getSettingsObject().getAvatarObject().getId())
                                    .putString("name", loginObject.getSettingsObject().getAvatarObject().getName())
                                    .putString("image", loginObject.getSettingsObject().getAvatarObject().getImage())
                                    .apply();

                            app.l(spref.get().getString("font", "ttf"));
                            String name = loginObject.getUserObject().getFname() + " " +
                                    loginObject.getUserObject().getLname();
                            app.t("  welcome   " + name, app.ToasetType.sucsess);

                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }else {

                            app.t("  some things wrong    " , app.ToasetType.error);
                        }
                    }

                } catch (JSONException e) {
                    app.l(e.toString());
                }
                app.l(responce);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                app.l("ffffffffffff " + statusCode);
            }

            @Override
            public void onStart() {
                spin_kit.setVisibility(View.VISIBLE);
                super.onStart();
            }

            @Override
            public void onFinish() {
                spin_kit.setVisibility(View.GONE);
                super.onFinish();
            }
        });

    }
}