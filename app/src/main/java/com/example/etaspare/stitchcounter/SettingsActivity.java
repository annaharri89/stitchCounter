package com.example.etaspare.stitchcounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    //TODO implement help mode
    private ExpandableListView mListView;
    private ExpandableListAdapter mAdapter;

    private static final String NAME = "NAME";
    private static final String COLOR = "COLOR";
    private String group[] = {"Themes" , "About"};
    private String[][] child = { { "Default", "Robins Egg Blue", "Pink" }, { "Alice", "David" } };

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);

        /* Setup ListView */
        mListView = (ExpandableListView) findViewById(R.id.list);

        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
        for (int i = 0; i < group.length; i++) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(NAME, group[i]);

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (int j = 0; j < child[i].length; j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(COLOR, child[i][j]);
            }
            childData.add(children);
        }

        // Set up our adapter
        mAdapter = new SimpleExpandableListAdapter(
                this,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { NAME },
                new int[] { android.R.id.text1 },
                childData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] { COLOR },
                new int[] { android.R.id.text1 });
        mListView.setAdapter(mAdapter);
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

    /*
   Opens "help mode" Called when help button is clicked in the action bar.
   Shows annotation bubbles.
   */
    public void openHelpMode() {
        /*
        if (!helpMode) {
            for (View view: helpModeArray) {
                view.setVisibility(View.VISIBLE);
            }
            helpMode = true;
        }
        */
    }

    /* Called when the user taps the "Settings" button in the overflow menu */
    public void openSettings () {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
