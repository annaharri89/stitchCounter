package com.example.etaspare.stitchcounter;

import android.content.Context;
import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ETASpare on 7/25/2017.
 */

public class Utils {

    private static final String PREFS_NAME = "PrefsFile";
    private Context mContext;

    /*TODO Document*/
    public Utils(Context context) {
        this.mContext = context;
    }

    /* TODO DOCUMENT */
    protected void updateTheme() {
        SharedPreferences prefs = this.mContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String theme = prefs.getString("theme", "");
        if (theme.equals("default")) {
            this.mContext.setTheme(R.style.AppTheme);
        } else if (theme.equals("pink")) {
            this.mContext.setTheme(R.style.AppTheme_pink);
        } else if (theme.equals("blue")) {
            this.mContext.setTheme(R.style.AppTheme_blue);
        }
    }

    /* TODO Document*/
    protected void updateSharedPreferences(int theme) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        switch (theme) {
            default:
            case 0:
                editor.putString("theme", "default");
                editor.apply();
                break;
            case 1:
                editor.putString("theme", "pink");
                editor.apply();
                break;
            case 2:
                editor.putString("theme", "blue");
                editor.apply();
                break;
        }
    }
}
