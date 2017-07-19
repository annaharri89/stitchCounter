package com.example.etaspare.stitchcounter;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class SingleCounterActivity extends AppCompatActivity {

    /* TODO remove cursor from edittext after input */
    /* TODO think about implementing a favorites list*/

    private Counter counter;
    final private StitchCounterMenu toolBarMenu = new StitchCounterMenu(this);
    ConstraintLayout layout;
    Boolean helpMode = false;
    ArrayList<View> helpModeArray;
    TextView help1;
    TextView help2;
    TextView help3;
    TextView help4;
    View tip1;
    View tip2;
    View tip4;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_counter);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);

        /* Help Mode Setup*/
        help1 = (TextView) findViewById(R.id.help_single_counter_activity_1);
        help2 = (TextView) findViewById(R.id.help_single_counter_activity_2);
        help3 = (TextView) findViewById(R.id.help_single_counter_activity_3);
        help4 = (TextView) findViewById(R.id.help_single_counter_activity_4);
        tip1 = findViewById(R.id.help_single_counter_activity_1_tip);
        tip2 = findViewById(R.id.help_single_counter_activity_2_tip);
        tip4 = findViewById(R.id.help_single_counter_activity_4_tip);
        helpModeArray = new ArrayList<>();
        helpModeArray.add(help1);
        helpModeArray.add(help2);
        helpModeArray.add(help3);
        helpModeArray.add(help4);
        helpModeArray.add(tip1);
        helpModeArray.add(tip2);
        helpModeArray.add(tip4);

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

        /* Project Name Listener*/
        final EditText textProjectName = (EditText) findViewById(R.id.text_project_name);
        textProjectName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /*
            When the done button is pressed, if a project name has been entered, setProjectName is called
            */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String projectName;
                if (actionId == EditorInfo.IME_ACTION_DONE) { //TODO: what if they dismiss the keyboard without hitting the done button. Look into other IME_ACTIONs
                    projectName = textProjectName.getText().toString();
                    if (projectName.length() > 0) {
                        counter.setProjectName(projectName);
                    }
                }
                return false;/* TODO Why does the documentation return true */
            }
        });

        /* Counter */
        final TextView textCounter = (TextView) findViewById(R.id.text_counter);
        final Button buttonPlus = (Button) findViewById(R.id.button_counter_plus);
        final Button buttonMinus = (Button) findViewById(R.id.button_counter_minus);
        final Button buttonReset = (Button) findViewById(R.id.button_counter_reset);
        final Button buttonCapsuleLeft = (Button) findViewById(R.id.button_capsule_left);
        final Button buttonCapsuleMiddle = (Button) findViewById(R.id.button_capsule_middle);
        final Button buttonCapsuleRight = (Button) findViewById(R.id.button_capsule_right);

        counter = new Counter(this, textCounter, R.string.counter_number_basic, buttonCapsuleLeft, buttonCapsuleMiddle, buttonCapsuleRight);

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter.incrementCounter();
            }
        });
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                counter.decrementCounter();
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                counter.resetCounter();
            }
        });
        buttonCapsuleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter.changeAdjustment(1);
            }
        });
        buttonCapsuleMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter.changeAdjustment(5);
            }
        });
        buttonCapsuleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter.changeAdjustment(10);
            }
        });

        /*
        + If savedInstanceState bundle is not null, gets all pertinent counter data from
          savedInstanceState bundle and updates the counters and the UI where needed. Used to keep
          counter progress up to date when orientation change occurs.
        + If savedInstanceState is null (meaning a new counter has been created or a counter has been
          loaded from the library) and extras is not null, gets all pertinent counter data from
          extras bundle and updates the counters and the UI where needed.
        */
        Bundle extras = getIntent().getExtras();
        if (savedInstanceState != null) {
            int _id = savedInstanceState.getInt("_id");
            String name = savedInstanceState.getString("name");
            int stitch_counter_number = savedInstanceState.getInt("stitch_counter_number");
            int stitch_adjustment = savedInstanceState.getInt("stitch_adjustment");

            if (_id > 0) {
                counter.ID = _id;
            }
            if (name != null && name.length() > 0) {
                counter.setProjectName(name);
                textProjectName.setText(name);
            }
            if (stitch_adjustment > 0) {
                counter.changeAdjustment(stitch_adjustment);
            }
            if (stitch_counter_number > 0) {
                counter.counterNumber = stitch_counter_number;
                counter.setCounter();
            }
        } else if (extras != null) {
            int _id = extras.getInt("_id");
            String name = extras.getString("name");
            int stitch_counter_number = extras.getInt("stitch_counter_number");
            int stitch_adjustment = extras.getInt("stitch_adjustment");

            if (_id > 0) {
                counter.ID = _id;
            }
            if (name != null && name.length() > 0) {
                counter.setProjectName(name);
                textProjectName.setText(name);
            }
            if (stitch_adjustment > 0) { //TODO Will this work if bundle is not sent
                counter.changeAdjustment(stitch_adjustment);
            } else {
                /* Sets default colors for adjustment buttons */
                counter.changeAdjustment(1);
            }
            if (stitch_counter_number > 0) {
                counter.counterNumber = stitch_counter_number;
                counter.setCounter();
            }
        }

        /* Save Counter project to DB if counter project doesn't already exist in the db*/
        if (counter.ID == 0) {
            counter.saveCounter(counter, null);
        }
    }

    /*
    Opens "help mode" Called when help button is clicked in the action bar. Sets the top layer
    visible, showing the annotation bubbles.
    */
    public void openHelpMode() {
        if (!helpMode) {
            for (View view: helpModeArray) {
                view.setVisibility(View.VISIBLE);
            }
            helpMode = true;
        }
    }

    /* Called when the user taps the "+" button (new counter) in the toolbar */
    public void openMainActivity () {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /*
    Called when the user taps the "Library" button in the overflow menu. Sends the stitchCounter
    and rowCounter in an parcelable array to the LibraryActivity so they can be saved.
    */
    public void openLibrary () {
        sendResults();
    }

    /*
    Creates a new intent which gets extras put in it in setUpExtras. Sends the intent and extras
    to the new activity.
    */
    protected void sendResults() {
        Intent intent = new Intent();
        setUpExtras(intent);
        setResult(RESULT_OK, intent);
        finish();
    }

    /* Adds stitchCounter and rowCounter as extras in a parcelable array to the passed intent. */
    protected void setUpExtras(Intent i) {
        ArrayList<Counter> counterList= new ArrayList<>();
        counterList.add(counter);
        i.putParcelableArrayListExtra("counters", counterList);
    }

    /* Starts a new activity/sends results/extras to new activity when back button is pressed. */
    @Override
    public void onBackPressed() {
        sendResults();
    }

    /*
    Saves all of the pertinent counter data to savedInstanceState bundle so it can be used to
    populate the activity if orientation change occurs.
    */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("_id", counter.ID);
        savedInstanceState.putString("name", counter.projectName);
        savedInstanceState.putInt("stitch_counter_number", counter.counterNumber);
        savedInstanceState.putInt("stitch_adjustment", counter.adjustment);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onStop() {
        /* Save Counter to DB */
        counter.saveCounter(counter, null);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        /* Save Counter to DB */
        counter.saveCounter(counter, null);
        super.onDestroy();
    }
}
