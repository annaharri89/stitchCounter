package com.example.etaspare.stitchcounter;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Boolean helpMode = false;
    private ArrayList<View> helpModeArray;
    private Utils utils = new Utils(this);

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_delete).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_new_counter:
                openMainActivity();
                break;
            case R.id.action_library:
                openLibrary();
                break;
            case R.id.action_help:
                openHelpMode();
                break;
            case R.id.action_settings:
                openSettings();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        utils.updateTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);

        /* Help Mode Setup*/
        TextView help1 = (TextView) findViewById(R.id.help_main_activity_1);
        TextView help2 = (TextView) findViewById(R.id.help_main_activity_2);
        View tip1 = findViewById(R.id.help_main_activity_1_tip);
        View tip2 = findViewById(R.id.help_main_activity_2_tip);
        helpModeArray = new ArrayList<>();
        helpModeArray.add(help1);
        helpModeArray.add(help2);
        helpModeArray.add(tip1);
        helpModeArray.add(tip2);

        /* Closes Help Mode, hides the annotation bubbles */
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (helpMode) {
                    for (View view: helpModeArray) {
                        view.setVisibility(View.INVISIBLE);
                    }
                    helpMode = false;
                }
                return false;
            }
        });
    }

    /* Called when the user taps the "New Basic Counter" button in the main activity */
    public void createNewSingleCounter (View view) {
        Intent intent = new Intent(this, NewCounterActivity.class);
        intent.putExtra("Layout", "single");
        startActivity(intent);
    }

    /* Called when the user taps the "New Advanced Counter" button in the main activity */
    public void createNewDoubleCounter (View view) {
        Intent intent = new Intent(this, NewCounterActivity.class);
        intent.putExtra("Layout", "double");
        startActivity(intent);
    }

    /*
    Opens "help mode" Called when help button is clicked in the action bar.
    Shows annotation bubbles.
    */
    public void openHelpMode() {
        if (!helpMode) {
            for (View view: helpModeArray) {
                view.setVisibility(View.VISIBLE);
            }
            helpMode = true;
        }
    }

    /* Called when the user taps the "Settings" button in the overflow menu */
    public void openSettings () {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /* Called when the user taps the "+" button (new counter) in the toolbar */
    public void openMainActivity () {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /* Called when the user taps the "Library" button in the overflow menu */
    public void openLibrary () {
        Intent intent = new Intent(this, LibraryActivity.class);
        startActivity(intent);
    }
}
