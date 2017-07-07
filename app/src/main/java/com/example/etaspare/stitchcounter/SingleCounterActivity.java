package com.example.etaspare.stitchcounter;

import android.content.res.Resources;
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

public class SingleCounterActivity extends AppCompatActivity {

    /* TODO remove cursor from edittext after input */
    /* TODO think about implementing a favorites list*/

    private Counter counter;
    final private StitchCounterMenu toolBarMenu = new StitchCounterMenu(this);
    ConstraintLayout topLayout;

    /* TODO WHAT"S THE DIFFERENCE BETWEEN
    TODO StitchCounterMenu toolBarMenu = new StitchCounterMenu(this);           and
    TODO TextView textCounter = (TextView) findViewById(R.id.text_counter);
    TODO Why declare one as new and (i believe casting is occurring) cast the other one
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        setContentView(R.layout.activity_single_counter);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);

        /* Closes help mode, hides the annotation bubbles */
        topLayout = (ConstraintLayout) findViewById(R.id.top_layout);
        topLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                topLayout.setVisibility(View.INVISIBLE);
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
                if (actionId == EditorInfo.IME_ACTION_DONE) {
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

        /* TODO Document*/
        Resources res = this.getResources();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long _id = extras.getLong("_id");
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
                textCounter.setText(String.format(res.getString(R.string.counter_number_stitch), stitch_counter_number));
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
        topLayout.setVisibility(View.VISIBLE);
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
