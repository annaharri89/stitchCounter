/*
   Copyright 2017 Anna Harrison

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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

    protected Boolean helpMode = false;
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
                utils.openMainActivity();
                break;
            case R.id.action_help:
                utils.openHelpMode("MainActivity", helpModeArray);
                break;
            case R.id.action_library:
                utils.openLibrary("MainActivity");
                break;
            case R.id.action_settings:
                utils.openSettings();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        utils.updateTheme(false);
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

}
