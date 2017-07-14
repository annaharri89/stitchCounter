package com.example.etaspare.stitchcounter;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.fenchtose.tooltip.Tooltip;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    final private StitchCounterMenu toolBarMenu = new StitchCounterMenu(this);
    Boolean helpMode = false;
    ConstraintLayout layout;
    Button singleCounter;
    Button doubleCounter;
    TextView help1;
    TextView help2;
    Tooltip tooltip1;
    Tooltip tooltip2;

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

        /* Help Mode Setup*/
        layout = (ConstraintLayout) findViewById(R.id.layout);
        singleCounter = (Button) findViewById(R.id.button_basic_counter);
        doubleCounter = (Button) findViewById(R.id.button_advanced_counter);
        ArrayList<TextView> bubbleArray = new ArrayList<>();
        help1 = new TextView(this);
        help2 = new TextView(this);
        bubbleArray.add(help1);
        bubbleArray.add(help2);
        setUpHelpBubbles(bubbleArray);
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
            helpMode = true;
            tooltip1 = new Tooltip.Builder(this)
                    .anchor(singleCounter, Tooltip.TOP)
                    .content(help1)
                    .into(layout)
                    .withTip(new Tooltip.Tip(20, 10, ContextCompat.getColor(this, R.color.colorAccent)))
                    .show();

            tooltip2 = new Tooltip.Builder(this)
                    .anchor(doubleCounter, Tooltip.BOTTOM)
                    .content(help2)
                    .into(layout)
                    .withTip(new Tooltip.Tip(20, 10, ContextCompat.getColor(this, R.color.colorAccent)))
                    .show();

        /* Closes help mode, hides the annotation bubbles */
            tooltip2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    helpMode = false;
                    tooltip1.dismiss();
                    tooltip2.dismiss();
                    return false;
                }
            });
        }
    }

    /* Sets up the TextViews embedded in the help bubbles */
    public void setUpHelpBubbles(ArrayList<TextView> bubbleList) {
        int i = 1;
        for (TextView v: bubbleList) {
            String id = "help_main_activity_" + Integer.toString(i);
            int strResource = getResources().getIdentifier(id, "string", getPackageName());
            int dimenResource = getResources().getIdentifier(id, "dimen", getPackageName());
            int width = (int) (getResources().getDimension(dimenResource) / getResources().getDisplayMetrics().density);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(params);
            v.setText(strResource);
            v.setPadding(5, 5, 5, 5);
            v.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
            v.setTextColor(Color.parseColor("#FFFFFF"));
            i += 1;
        }
    }
}
