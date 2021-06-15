package app;

import android.content.Context;
import android.content.SharedPreferences;

public class spref {
    private static final SharedPreferences ourInstance = Application.getContext().getSharedPreferences(app.main.tag, Context.MODE_PRIVATE);

    public static SharedPreferences get() {
        if (ourInstance == null) {
            SharedPreferences ourInstance = Application.getContext().getSharedPreferences(app.main.tag, Context.MODE_PRIVATE);
        }
        return ourInstance;
    }


}
