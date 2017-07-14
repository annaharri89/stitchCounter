package com.example.etaspare.stitchcounter;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fenchtose.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.Objects;

public class DoubleCounterActivity extends AppCompatActivity {

    /* TODO remove cursor from edittext after input */

    private Counter stitchCounter;
    private Counter rowCounter;
    final private StitchCounterMenu toolBarMenu = new StitchCounterMenu(this);
    Boolean helpMode = false;
    ConstraintLayout layout;
    EditText textProjectName;
    EditText totalRows;
    ProgressBar progress;
    Button buttonCapsuleMiddleStitch;
    Button buttonPlusRow;
    Button buttonResetStitch;
    TextView help1;
    TextView help2;
    TextView help3;
    TextView help4;
    TextView help5;
    TextView help6;
    Tooltip tooltip1;
    Tooltip tooltip2;
    Tooltip tooltip3;
    Tooltip tooltip4;
    Tooltip tooltip5;
    Tooltip tooltip6;

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
        setContentView(R.layout.activity_double_counter);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);

        /* Help Mode Setup*/
        layout = (ConstraintLayout) findViewById(R.id.layout);
        textProjectName = (EditText) findViewById(R.id.text_project_name_2);
        totalRows = (EditText) findViewById(R.id.text_total_rows_input);
        progress = (ProgressBar) findViewById(R.id.progress_bar);
        buttonCapsuleMiddleStitch = (Button) findViewById(R.id.button_capsule_middle_stitch);
        buttonPlusRow = (Button) findViewById(R.id.button_counter_plus_row);
        buttonResetStitch = (Button) findViewById(R.id.button_counter_reset_stitch);
        help1 = new TextView(this);
        help2 = new TextView(this);
        help3 = new TextView(this);
        help4 = new TextView(this);
        help5 = new TextView(this);
        help6 = new TextView(this);
        ArrayList<TextView> bubbleArray = new ArrayList<>();
        bubbleArray.add(help1);
        bubbleArray.add(help2);
        bubbleArray.add(help3);
        bubbleArray.add(help4);
        bubbleArray.add(help5);
        bubbleArray.add(help6);
        setUpHelpBubbles(bubbleArray);

        /* Project Name Listener*/
        //final EditText textProjectName = (EditText) findViewById(R.id.text_project_name_2); TODO remove
        textProjectName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /*
            When the done button is pressed, if a project name has been entered, setProjectName is called
            */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String projectName;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    projectName = textProjectName.getText().toString();
                    if (projectName.length() > 0) {
                        rowCounter.setProjectName(projectName);
                    }
                }
                return false;/* TODO Why does the documentation return true */
            }
        });

        /* Stitch Counter */
        final TextView textCounterStitch = (TextView) findViewById(R.id.text_counter_stitch);
        final Button buttonPlusStitch = (Button) findViewById(R.id.button_counter_plus_stitch);
        final Button buttonMinusStitch = (Button) findViewById(R.id.button_counter_minus_stitch);
        final Button buttonCapsuleTopStitch = (Button) findViewById(R.id.button_capsule_top_stitch);
        //final Button buttonCapsuleMiddleStitch = (Button) findViewById(R.id.button_capsule_middle_stitch); TODO remove
        final Button buttonCapsuleBottomStitch = (Button) findViewById(R.id.button_capsule_bottom_stitch);
        //final Button buttonResetStitch = (Button) findViewById(R.id.button_counter_reset_stitch); TODO remove

        stitchCounter = new Counter(this, textCounterStitch, null, R.string.counter_number_stitch, R.string.counter_progress, buttonCapsuleTopStitch, buttonCapsuleMiddleStitch, buttonCapsuleBottomStitch, null);

        buttonPlusStitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stitchCounter.incrementCounter();
            }
        });
        buttonMinusStitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stitchCounter.decrementCounter();
            }
        });
        buttonResetStitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stitchCounter.resetCounter();
            }
        });
        buttonCapsuleTopStitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stitchCounter.changeAdjustment(1);
            }
        });
        buttonCapsuleMiddleStitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stitchCounter.changeAdjustment(5);
            }
        });
        buttonCapsuleBottomStitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stitchCounter.changeAdjustment(10);
            }
        });


        /* Row Counter */
        final TextView textCounterRow = (TextView) findViewById(R.id.text_counter_row);
        final TextView textProgress = (TextView) findViewById(R.id.text_counter_progress);
        //final Button buttonPlusRow = (Button) findViewById(R.id.button_counter_plus_row); TODO REMOVE
        final Button buttonMinusRow = (Button) findViewById(R.id.button_counter_minus_row);
        final Button buttonResetRow = (Button) findViewById(R.id.button_counter_reset_row);
        final Button buttonCapsuleTopRow = (Button) findViewById(R.id.button_capsule_top_row);
        final Button buttonCapsuleMiddleRow = (Button) findViewById(R.id.button_capsule_middle_row);
        final Button buttonCapsuleBottomRow = (Button) findViewById(R.id.button_capsule_bottom_row);
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progress_bar);

        rowCounter = new Counter(this, textCounterRow, textProgress, R.string.counter_number_row, R.string.counter_progress, buttonCapsuleTopRow, buttonCapsuleMiddleRow, buttonCapsuleBottomRow, progress);

        buttonPlusRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowCounter.incrementCounter();
            }
        });
        buttonMinusRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowCounter.decrementCounter();
            }
        });
        buttonResetRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowCounter.resetCounter();
            }
        });
        buttonCapsuleTopRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowCounter.changeAdjustment(1);
            }
        });
        buttonCapsuleMiddleRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowCounter.changeAdjustment(5);
            }
        });
        buttonCapsuleBottomRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowCounter.changeAdjustment(10);
            }
        });

        /* Progress Listener*/
        final EditText totalRows = (EditText) findViewById(R.id.text_total_rows_input);
        totalRows.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /*

            */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String totalRowsValueString;
                int totalRowsValueInt;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    totalRowsValueString = totalRows.getText().toString();
                    if (totalRowsValueString.length() > 0) {
                        totalRowsValueInt = Integer.parseInt(totalRowsValueString);
                    } else {
                        totalRowsValueInt = 0;
                    }
                    if (totalRowsValueInt > 0) {
                        rowCounter.setProgressBarMax(totalRowsValueInt);
                    }
                }
                return false;/* TODO Why does the documentation return true */
            }
        });

        /* TODO Document*/
        Resources res = this.getResources();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int _id = extras.getInt("_id");
            String name = extras.getString("name");
            int stitch_counter_number = extras.getInt("stitch_counter_number");
            int stitch_adjustment = extras.getInt("stitch_adjustment");
            int row_counter_number = extras.getInt("row_counter_number");
            int row_adjustment = extras.getInt("row_adjustment");
            int total_rows = extras.getInt("total_rows");
            double progress_percent = extras.getDouble("progress_percent");

            if (_id > 0) {
                stitchCounter.ID = _id;
            }
            if (name != null && name.length() > 0) {
                rowCounter.setProjectName(name);
                textProjectName.setText(name);
            }
            if (stitch_adjustment > 0) { //TODO Will this work if bundle is not sent
                stitchCounter.changeAdjustment(stitch_adjustment);
            } else {
                /* Sets default colors for adjustment buttons */
                stitchCounter.changeAdjustment(1);
            }
            if (row_adjustment > 0) { //TODO Will this work if bundle is not sent
                rowCounter.changeAdjustment(row_adjustment);
            } else {
                /* Sets default colors for adjustment buttons */
                rowCounter.changeAdjustment(1);
            }
            if (progress_percent > 0) {
                String formattedProgressNumber = String.format(res.getString(R.string.counter_progress), progress_percent); //TODO look into
                textProgress.setText(formattedProgressNumber);
            } else {
                /* Sets default progress percent */
                String formattedProgressNumber = String.format(res.getString(R.string.counter_progress), "0.0");
                textProgress.setText(formattedProgressNumber);
            }
            if (stitch_counter_number > 0) {
                stitchCounter.counterNumber = stitch_counter_number;
                textCounterStitch.setText(String.format(res.getString(R.string.counter_number_stitch), stitch_counter_number));
            }
            if (row_counter_number > 0) {
                rowCounter.counterNumber = row_counter_number;
                textCounterRow.setText(String.format(res.getString(R.string.counter_number_row), row_counter_number));
            }
            if (total_rows > 0) {
                rowCounter.setProgressBarMax(total_rows);
                rowCounter.totalRows = total_rows;
                totalRows.setText(Integer.toString(total_rows));
            }
        }

        /* Save Counter project to DB if counter project doesn't already exist in the db*/
        if (stitchCounter.ID == 0) {
            stitchCounter.saveCounter(stitchCounter, rowCounter);
        }
    }

    /*
    Opens "help mode" Called when help button is clicked in the action bar. Shows appropriate tooltips.
    Sets onclicklistener to dismiss the help bubbles.
    */
    public void openHelpMode() {
        if (!helpMode) {
            helpMode = true;
            tooltip1 = new Tooltip.Builder(this)
                    .anchor(textProjectName, Tooltip.RIGHT)
                    .content(help1)
                    .into(layout)
                    .withTip(new Tooltip.Tip(10, 5, ContextCompat.getColor(this, R.color.colorAccent)))
                    .show();

            tooltip2 = new Tooltip.Builder(this)
                    .anchor(buttonCapsuleMiddleStitch, Tooltip.LEFT)
                    .content(help2)
                    .into(layout)
                    .withTip(new Tooltip.Tip(10, 5, ContextCompat.getColor(this, R.color.colorAccent)))
                    .show();

            tooltip3 = new Tooltip.Builder(this)
                    .anchor(buttonPlusRow, Tooltip.BOTTOM)
                    .content(help3)
                    .into(layout)
                    .withTip(new Tooltip.Tip(10, 5, ContextCompat.getColor(this, R.color.colorAccent)))
                    .show();

            tooltip4 = new Tooltip.Builder(this)
                    .anchor(buttonResetStitch, Tooltip.BOTTOM)
                    .content(help4)
                    .into(layout)
                    .withTip(new Tooltip.Tip(10, 5, ContextCompat.getColor(this, R.color.colorAccent)))
                    .show();

            tooltip5 = new Tooltip.Builder(this)
                    .anchor(totalRows, Tooltip.LEFT)
                    .content(help5)
                    .into(layout)
                    .withTip(new Tooltip.Tip(10, 5, ContextCompat.getColor(this, R.color.colorAccent)))
                    .show();

            tooltip6 = new Tooltip.Builder(this)
                    .anchor(progress, Tooltip.BOTTOM)
                    .content(help6)
                    .into(layout)
                    .withTip(new Tooltip.Tip(10, 5, ContextCompat.getColor(this, R.color.colorAccent)))
                    .show();

        /* Closes help mode, hides the annotation bubbles */
            tooltip6.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    helpMode = false;
                    tooltip1.dismiss();
                    tooltip2.dismiss();
                    tooltip3.dismiss();
                    tooltip4.dismiss();
                    tooltip5.dismiss();
                    tooltip6.dismiss();
                    return false;
                }
            });
        }
    }

    /* Sets up the TextViews embedded in the help bubbles */
    public void setUpHelpBubbles(ArrayList<TextView> bubbleList) {
        int i = 1;
        for (TextView v: bubbleList) {
            String dimenID;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                if(getResources().getConfiguration().screenWidthDp <= 320){
                    dimenID = "help_double_counter_activity_port_small_" + Integer.toString(i);
                } else {
                    dimenID = "help_double_counter_activity_port_" + Integer.toString(i);
                }
            } else {
                if(getResources().getConfiguration().screenHeightDp <= 320){
                    dimenID = "help_double_counter_activity_land_small_" + Integer.toString(i);
                } else {
                    dimenID = "help_double_counter_activity_land_" + Integer.toString(i);
                }
            }
            String strID = "help_double_counter_activity_" + Integer.toString(i);
            int strResource = getResources().getIdentifier(strID, "string", getPackageName());
            int dimenResource = getResources().getIdentifier(dimenID, "dimen", getPackageName());
            int width = (int) (getResources().getDimension(dimenResource) / getResources().getDisplayMetrics().density);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(params);
            v.setText(strResource);
            v.setPadding(1, 1, 1, 1);
            v.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
            v.setTextColor(Color.parseColor("#FFFFFF"));
            i += 1;
        }
    }

    @Override
    protected void onStop() {
        /* Save Counters to DB */
        stitchCounter.saveCounter(stitchCounter, rowCounter);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        /* Save Counters to DB */
        stitchCounter.saveCounter(stitchCounter, rowCounter);
        super.onDestroy();
    }
}
