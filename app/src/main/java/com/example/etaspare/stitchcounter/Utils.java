package com.example.etaspare.stitchcounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ETASpare on 7/25/2017.
 */

public class Utils {

    private static final String PREFS_NAME = "PrefsFile";
    private Context mContext;

    /* Constructor */
    public Utils(Context context) {
        this.mContext = context;
    }

    /* Returns color to be used for Active Capsule Button in Counter*/
    protected int determineActiveCapsuleButtonColor() {
        SharedPreferences prefs = this.mContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String theme = prefs.getString("theme", "");
        switch (theme) {
            default:
            case "default":
            case "default_dark":
                return R.color.colorAccent;
            case "pink":
            case "pink_dark":
                return R.color.colorAccent2;
            case "blue":
            case "blue_dark":
                return R.color.colorAccent3;
        }
    }

    /* Returns color to be used for Inactive Capsule Button in Counter */
    protected int determineInActiveCapsuleButtonColor() {
        SharedPreferences prefs = this.mContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String theme = prefs.getString("theme", "");
        switch (theme) {
            default:
            case "default":
            case "pink":
            case "blue":
                return R.color.lightGrey;
            case "default_dark":
            case "pink_dark":
            case "blue_dark":
                return R.color.darkGrey;
        }
    }

    /* Returns text color to be used for Active Capsule Button */
    protected int determineActiveCapsuleButtonTextColor() {
        SharedPreferences prefs = this.mContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String theme = prefs.getString("theme", "");
        switch (theme) {
            default:
            case "default":
            case "pink":
            case "default_dark":
            case "pink_dark":
                return R.color.white;
            case "blue":
            case "blue_dark":
                return R.color.black;
        }
    }

    /* Returns text color to be used for Inactive Capsule Button */
    protected int determineInActiveCapsuleButtonTextColor() {
        SharedPreferences prefs = this.mContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String theme = prefs.getString("theme", "");
        switch (theme) {
            default:
            case "default":
            case "pink":
            case "blue":
                return R.color.darkGrey;
            case "default_dark":
            case "pink_dark":
                return R.color.white;
            case "blue_dark":
                return R.color.silver;
        }
    }

    /* Set's theme based on store shared preference */
    protected void updateTheme(Boolean dialog) {
        SharedPreferences prefs = this.mContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String theme = prefs.getString("theme", "");
        switch (theme) {
            case "default":
                if (dialog) {
                    this.mContext.setTheme(R.style.Theme_MyDialog);
                } else {
                    this.mContext.setTheme(R.style.AppTheme);
                }
                break;
            case "default_dark":
                if (dialog) {
                    this.mContext.setTheme(R.style.Theme_MyDialog_dark);
                } else {
                    this.mContext.setTheme(R.style.AppTheme_dark);
                }
                break;
            case "pink":
                if (dialog) {
                    this.mContext.setTheme(R.style.Theme_MyDialog_pink);
                } else {
                    this.mContext.setTheme(R.style.AppTheme_pink);
                }
                break;
            case "pink_dark":
                if (dialog) {
                    this.mContext.setTheme(R.style.Theme_MyDialog_pink_dark);
                } else {
                    this.mContext.setTheme(R.style.AppTheme_pink_dark);
                }
                break;
            case "blue":
                if (dialog) {
                    this.mContext.setTheme(R.style.Theme_MyDialog_blue);
                } else {
                    this.mContext.setTheme(R.style.AppTheme_blue);
                }
                break;
            case "blue_dark":
                if (dialog) {
                    this.mContext.setTheme(R.style.Theme_MyDialog_blue_dark);
                } else {
                    this.mContext.setTheme(R.style.AppTheme_blue_dark);
                }
        }
    }

    /* Updates shared preference with appropriate theme title so theme can be set */
    protected void updateSharedPreferences(int theme) {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        switch (theme) {
            default:
            case 0:
                editor.putString("theme", "default");
                editor.apply();
                break;
            case 1:
                editor.putString("theme", "default_dark");
                editor.apply();
                break;
            case 2:
                editor.putString("theme", "pink");
                editor.apply();
                break;
            case 3:
                editor.putString("theme", "pink_dark");
                editor.apply();
                break;
            case 4:
                editor.putString("theme", "blue");
                editor.apply();
                break;
            case 5:
                editor.putString("theme", "blue_dark");
                editor.apply();
                break;
        }
    }
}
