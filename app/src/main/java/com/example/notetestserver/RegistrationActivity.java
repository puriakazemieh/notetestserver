package com.example.notetestserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import app.*;
import cz.msebera.android.httpclient.Header;
import object.LoginObject;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email, password, repassword, lname, fname;
    RadioGroup sex;
    Button ok;
    TextView register;
    SpinKitView spin_kit;

    int sexint = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
    }

    private void init() {
        spin_kit = findViewById(R.id.spin_kit);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        lname = findViewById(R.id.lname);
        fname = findViewById(R.id.fname);
        sex = findViewById(R.id.sex);
        ok = findViewById(R.id.ok);
        register = findViewById(R.id.registerr);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ok) {
            Register();
        } else {
            finish();
        }
    }

    private void Register() {
        if (email.getText().toString().length() < 5) {
            app.t("please enter your email ", app.ToasetType.error);
            app.animateError(email);
            return;
        }
        if (fname.getText().toString().length() < 3) {
            app.t("please enter your fname ", app.ToasetType.error);
            app.animateError(fname);
            return;
        }
        if (lname.getText().toString().length() < 3) {
            app.t("please enter your lname ", app.ToasetType.error);
            app.animateError(lname);
            return;
        }
        if (password.getText().toString().length() < 2) {
            app.t("please enter your password ", app.ToasetType.error);
            app.animateError(password);
            return;
        }
        if (!password.getText().toString().equals(repassword.getText().toString())) {
            app.t("password and repassword not match ", app.ToasetType.error);
            app.animateError(password);
            app.animateError(repassword);
            return;
        }

        /*
         * female=0
         * male=1
         * other=2
         * */
        if (sex.getCheckedRadioButtonId() == R.id.female) sexint = 2;
        else if (sex.getCheckedRadioButtonId() == R.id.other) sexint = 2;

        RequestParams params = app.getRequestParams();
        params.put(ROUTER.ROUTE, ROUTER.ROUTE_LOGIN);
        params.put(ROUTER.ACTION, ROUTER.ACTION_REGISTERATION);
        params.put(ROUTER.INPUT_EMAIL, email.getText().toString());
        params.put(ROUTER.INPUT_PASSWORD, password.getText().toString());
        params.put(ROUTER.INPUT_FNAME, fname.getText().toString());
        params.put(ROUTER.INPUT_LNAME, lname.getText().toString());
        params.put(ROUTER.INPUT_SEX, sexint);

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
                            app.l(responce);
                            spref.get().edit()
                                    .putString(ROUTER.SESSION, loginObject.getSession())
                                    .putInt(ROUTER.USER_ID, loginObject.getId())
                                    .putString(ROUTER.INPUT_FNAME, fname.getText().toString())
                                    .putString(ROUTER.INPUT_LNAME, lname.getText().toString())
                                    .putInt(ROUTER.INPUT_SEX, sexint)
                                    .apply();

                            spref.get().edit()
                                    .putInt("nightMode", loginObject.getSettingsObject().getNightMode())
                                    .putString("font", loginObject.getSettingsObject().getSETTINGS_FONT())
                                    .putInt("bgcolor", loginObject.getSettingsObject().getBgColor())
                                    .putInt("textcolor", loginObject.getSettingsObject().getTextColor())
                                    .putInt("fontSize", loginObject.getSettingsObject().getFontSize())
                                  //  .putInt("avatarId", loginObject.getSettingsObject().getAvatarObject().getId())
                                    .putString("name", loginObject.getSettingsObject().getAvatarObject().getName())
                                    .putString("image", loginObject.getSettingsObject().getAvatarObject().getImage())
                                    .apply();

                            String name = fname.getText().toString() + " " +
                                    lname.getText().toString();
                            app.t("  welcome   " + name, app.ToasetType.sucsess);

                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else {

                            app.t("  some things wrong    ", app.ToasetType.error);
                        }
                    }

                } catch (JSONException e) {
                    app.l(e.toString());
                }
                app.l(responce);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

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