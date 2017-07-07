package com.example.etaspare.stitchcounter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by ETASpare on 6/13/2017.
 */

public class StitchCounterMenu extends AppCompatActivity {

    private Context mContext;

    public StitchCounterMenu(Context context) {
        this.mContext = context;
    }

    /* Called in Stitch Counter Activities onOptionsItemSelected method. Handles menu item clicks */
    public boolean handleMenu(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_new_counter:
                openMainActivity();
                break;
            case R.id.action_library:
                openLibrary();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /* Called when the user taps the "+" button (new counter) in the toolbar */
    public void openMainActivity () {
        Intent intent = new Intent(this.mContext, MainActivity.class);
        this.mContext.startActivity(intent);
    }

    /* Called when the user taps the "Library" button in the overflow menu */
    public void openLibrary () {
        Intent intent = new Intent(this.mContext, LibraryActivity.class);
        this.mContext.startActivity(intent);
    }
}
