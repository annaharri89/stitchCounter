package com.example.etaspare.stitchcounter;

import android.content.ComponentName;
import android.content.Intent;
import android.opengl.Visibility;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final private StitchCounterMenu toolBarMenu = new StitchCounterMenu(this);
    ConstraintLayout topLayout;
    View connector1;
    View connector2;
    View help1;
    View help2;

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

        /* Instantiating help views */
        connector1 = findViewById(R.id.help_main_activity_1_connector);
        connector2 = findViewById(R.id.help_main_activity_2_connector);
        help1 = findViewById(R.id.help_main_activity_1);
        help2 = findViewById(R.id.help_main_activity_2);

        /* Closes help mode, hides the annotation bubbles */
        topLayout = (ConstraintLayout) findViewById(R.id.top_layout);
        topLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                topLayout.setVisibility(View.INVISIBLE);
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
    Opens "help mode" Called when help button is clicked in the action bar. Sets the top layer
    visible, showing the annotation bubbles.
    */
    public void openHelpMode() {
        /*
        connector1.setVisibility(visibility);
        connector2.setVisibility(visibility);
        help1.setVisibility(visibility);
        help2.setVisibility(visibility);
        */
        topLayout.setVisibility(View.VISIBLE);

    }
}
