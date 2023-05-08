package ravil.amangeldiuly.example.minelivescoreuser.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

public class SharedPreferencesUtil {

    private final static String S_P_FILE_NAME = "LiveScoreUser";

    private SharedPreferencesUtil() {}

    public static void putValue(Context context, String key, String value) {
        context.getSharedPreferences(S_P_FILE_NAME, MODE_PRIVATE).edit()
                .putString(key, value)
                .apply();
    }

    public static String getValue(Context context, String key) {
        return context.getSharedPreferences(S_P_FILE_NAME, MODE_PRIVATE)
                .getString(key, "empty");
    }
}
