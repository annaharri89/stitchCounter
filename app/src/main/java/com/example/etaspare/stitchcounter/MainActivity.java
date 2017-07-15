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

    final private StitchCounterMenu toolBarMenu = new StitchCounterMenu(this);
    Boolean helpMode = false;
    ConstraintLayout layout;
    ArrayList<View> helpModeArray;
    TextView help1;
    TextView help2;
    View tip1;
    View tip2;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) { //TODO Why can't this be implemented in StitchCounterMenu.java ?
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_delete).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            openHelpMode();
            return true;
        } else {
            return toolBarMenu.handleMenu(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);
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
    Opens "help mode" Called when help button is clicked in the action bar. Shows appropriate tooltips.
    Sets onclicklistener to dismiss the help bubbles.
    */
    public void openHelpMode() {
        if (!helpMode) {
            for (View view: helpModeArray) {
                view.setVisibility(View.VISIBLE);
            }
            helpMode = true;
        }
    }

    @Override
    protected void onResume() {
        /* Help Mode Setup*/
        helpModeArray = new ArrayList<>();
        int id = R.id.help_main_activity_1;
        help1 = (TextView) findViewById(R.id.help_main_activity_1);
        help2 = (TextView) findViewById(R.id.help_main_activity_2);
        tip1 = findViewById(R.id.help_main_activity_1_tip);
        tip2 = findViewById(R.id.help_main_activity_2_tip);
        helpModeArray.add(help1);
        helpModeArray.add(help2);
        helpModeArray.add(tip1);
        helpModeArray.add(tip2);

        /* Closes Help Mode, hides the annotation bubbles */
        layout = (ConstraintLayout) findViewById(R.id.layout);
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

        super.onResume();
    }
}
