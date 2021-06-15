package app;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.notetestserver.MainActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import interfaces.NoteStateChangeListener;
import object.NoteObject;

public class app {

    public static int pos;
    public static enum ToasetType {
        error, warning, sucsess, info
    }


    public static class main {
        public static String tag = "note test server";
        //   public static final String url = "http://10.0.2.2/phptest/lfnote/";
        public static final String url = "http://kazemiehtest.ir/kazemieh/lfnote/";
    }

    public static void l(String message) {
        Log.e(main.tag, message);
    }

    public static void t(String message, ToasetType type) {
        switch (type) {
            case error:
                Toasty.error(Application.getContext(), message, Toasty.LENGTH_LONG).show();
                break;
            case warning:
                Toasty.warning(Application.getContext(), message, Toasty.LENGTH_LONG).show();
                break;
            case sucsess:
                Toasty.success(Application.getContext(), message, Toasty.LENGTH_LONG).show();
                break;
            case info:
                Toasty.info(Application.getContext(), message, Toasty.LENGTH_LONG).show();
                break;
        }
    }

    public static Drawable getImage(NoteObject object) {
        if (object.getTitle().length() > 1) {
            String text = object.getTitle().substring(0, 1).toUpperCase();
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color2 = generator.getColor("id" + object.getId());

            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig().useFont(MainActivity.font).endConfig()
                    .buildRect(text, color2);

            return drawable;
        }

        return null;
    }

    public static RequestParams getRequestParams() {
        RequestParams params = new RequestParams();
        params.put(ROUTER.SESSION, spref.get().getString(ROUTER.SESSION, ""));
        params.put(ROUTER.USER_ID, spref.get().getInt(ROUTER.USER_ID, -1));
        return params;
    }

    public static void animateError(View view) {
        YoYo.with(Techniques.Bounce).duration(1500).playOn(view);
    }

    public static void changeNoteState(int position, NoteObject object, NoteStateChangeListener listener) {

        int newState = object.getSeen() == 0 ? 1 : 0;
        RequestParams params = app.getRequestParams();
        params.put(ROUTER.ROUTE, ROUTER.ROUTE_NOTES);
        params.put(ROUTER.ACTION, ROUTER.ACTION_CHANGE_STATE);
        params.put(ROUTER.INPUT_NOTE_ID, object.getId());
        params.put(ROUTER.INPUT_STATE, newState);
        NoteClient.post(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respnce = new String(responseBody);
                app.l(respnce);
                if (respnce.equals(ROUTER.SUCSESS)) {
                    app.l(position+"     dfsdfds");
                    listener.onChange(position, object.getId(), newState, true);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(respnce);
                    if (jsonObject.has("error")) {
                        app.t(jsonObject.getString("error"), app.ToasetType.error);
                        listener.onChange(position, object.getId(), 0, false);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onStart() {
                listener.onStart();
                super.onStart();
            }

            @Override
            public void onFinish() {
                listener.onFinish();
                super.onFinish();
            }
        });
    }

}
