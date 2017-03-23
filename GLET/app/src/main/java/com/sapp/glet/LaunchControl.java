package com.sapp.glet;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Simon on 19.03.2017.
 */

public class LaunchControl {

    private static final String MY_PREFERENCES = "my_preferences";

    public static boolean isFirst(Context context){
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        final boolean is_first = reader.getBoolean("is_first", true);
        if(is_first){
            final SharedPreferences.Editor editor = reader.edit();
            editor.putBoolean("is_first", false);
            editor.commit();
        }
        return is_first;
    }
}
