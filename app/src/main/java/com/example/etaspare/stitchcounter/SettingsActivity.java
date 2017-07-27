package com.example.etaspare.stitchcounter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    //TODO implement help mode in settings
    //TODO document Utils
    private ExpandableListView mListView;
    private ExpandableListAdapter mAdapter;
    private Utils utils = new Utils(this);

    private static final String NAME = "NAME";
    private String group[] = {"Themes" , "About"};
    private String[][] child = { { "_default", "_defaultDark", "cottonCandy", "cottonCandyDark", "robinsEggBlue", "robinsEggBlueDark"  }, { "Version" } };

    Theme _default = new Theme("Default", Color.parseColor("#3F51B5"), Color.parseColor("#303F9F"), Color.parseColor("#FF4081"));
    Theme _defaultDark = new Theme("Default Dark", Color.parseColor("#3F51B5"), Color.parseColor("#303F9F"), Color.parseColor("#FF4081"));
    Theme cottonCandy = new Theme("Cotton Candy", Color.parseColor("#F48FB1"), Color.parseColor("#F06292"), Color.parseColor("#CE93D8"));
    Theme cottonCandyDark = new Theme("Cotton Candy Dark", Color.parseColor("#F48FB1"), Color.parseColor("#F06292"), Color.parseColor("#CE93D8"));
    Theme robinsEggBlue = new Theme("Robins Egg Blue", Color.parseColor("#18FFFF"), Color.parseColor("#00E5FF"), Color.parseColor("#FFD740"));
    Theme robinsEggBlueDark = new Theme("Robins Egg Blue Dark", Color.parseColor("#18FFFF"), Color.parseColor("#00E5FF"), Color.parseColor("#FFD740"));

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
        utils.updateTheme(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);

        /* TODO Document */
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
                curChildMap.put(NAME, child[i][j]);
            }
            childData.add(children);
        }

        // Set up the adapter
        mAdapter = new MySimpleExpandableListAdapter(
                this,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { NAME },
                new int[] { android.R.id.text1 },
                childData, R.layout.list_item_theme,
                new String[] { NAME },
                new int[] { R.id.textView });
        mListView = (ExpandableListView) findViewById(R.id.list);
        mListView.setAdapter(mAdapter);

        /*TODO Document */
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 0) {
                    switch (childPosition) {
                        default:
                        case 0:
                            utils.updateSharedPreferences(0);
                            finish();
                            startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                            break;
                        case 1:
                            utils.updateSharedPreferences(1);
                            finish();
                            startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                            break;
                        case 2:
                            utils.updateSharedPreferences(2);
                            finish();
                            startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                            break;
                        case 3:
                            utils.updateSharedPreferences(3);
                            finish();
                            startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                            break;
                        case 4:
                            utils.updateSharedPreferences(4);
                            finish();
                            startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                            break;
                        case 5:
                            utils.updateSharedPreferences(5);
                            finish();
                            startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                            break;
                    }
                } else if (groupPosition == 1) {
                    //Do nothing
                }
                return false;
            }
        });
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

    /* A theme is made up of a title and three colors. */
    public class Theme {
        private final String title;
        private final int color1;
        private final int color2;
        private final int color3;

        /* Theme Constructor */
        public Theme(String title, int color1, int color2, int color3) {
            this.title = title;
            this.color1 = color1;
            this.color2 = color2;
            this.color3 = color3;
        }
    }

    class MySimpleExpandableListAdapter extends SimpleExpandableListAdapter {
        private final Context context;
        private final List<? extends List<? extends Map<String, ?>>> data;

        public MySimpleExpandableListAdapter(Context context,
                                             List<? extends Map<String, ?>> groupData,
                                             int groupLayout,
                                             String[] groupFrom,
                                             int[] groupTo,
                                             List<? extends List<? extends Map<String, ?>>> childData,
                                             int childLayout,
                                             String[] childFrom,
                                             int[] childTo) {
            super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout,
                    childFrom, childTo);
            this.context = context;
            this.data = childData;
        }

        @Override
        public int getChildTypeCount() {
            return 2;
        }

        @Override
        public int getChildType(final int groupPosition, final int childPosition) {
            if (groupPosition == 0) {
                return 0;
            } else if (groupPosition == 1) {
                return 1;
            }
            return 0;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            View row = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
            Resources res = getResources();
            int type = getChildType(groupPosition, childPosition);
            if (row != null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                switch (type) {
                    default:
                    case 0:
                        row = inflater.inflate(R.layout.list_item_theme, parent, false);

                        TextView textView1 = (TextView) row.findViewById(R.id.textView);
                        View color1View = row.findViewById(R.id.color_1_view);
                        View color2View = row.findViewById(R.id.color_2_view);
                        View color3View = row.findViewById(R.id.color_3_view);

                        String title = data.get(0).get(childPosition).get("NAME").toString();
                        Theme theme;
                        /* TODO: DOCUMENT */
                        switch (title) {
                            default:
                            case "_default":
                                theme = _default;
                                break;
                            case "_defaultDark":
                                theme = _defaultDark;
                                break;
                            case "cottonCandy":
                                theme = cottonCandy;
                                break;
                            case "cottonCandyDark":
                                theme = cottonCandyDark;
                                break;
                            case "robinsEggBlue":
                                theme = robinsEggBlue;
                                break;
                            case "robinsEggBlueDark":
                                theme = robinsEggBlueDark;
                                break;
                        }

                        textView1.setText(theme.title);
                        color1View.setBackgroundColor(theme.color1);
                        color2View.setBackgroundColor(theme.color2);
                        color3View.setBackgroundColor(theme.color3);

                        /*
                        Sets odd rows background color to darkgrey and textcolor to white, sets even rows
                        background color to white and textcolor to black
                        */
                        if (childPosition%2 == 0) {
                            if (Build.VERSION.SDK_INT < 23) {
                                /* Android Support Library 22 and earlier compatible */
                                textView1.setTextColor(res.getColor(R.color.black));
                            } else {
                                /* Android Support Library 23 compatible */
                                textView1.setTextColor(ContextCompat.getColor(this.context, R.color.black));
                            }
                            row.setBackgroundColor(Color.parseColor("#FFFAFAFA"));
                        } else {
                            if (Build.VERSION.SDK_INT < 23) {
                                 /* Android Support Library 22 and earlier compatible */
                                textView1.setTextColor(res.getColor(R.color.white));
                            } else {
                                /* Android Support Library 23 compatible */
                                textView1.setTextColor(ContextCompat.getColor(this.context, R.color.white));
                            }
                            row.setBackgroundColor(Color.parseColor("#303030"));
                        }
                        break;
                    case 1:
                        row = inflater.inflate(R.layout.list_item_about, parent, false);
                        String versionName = BuildConfig.VERSION_NAME;
                        TextView textVersionName = (TextView) row.findViewById(R.id.version_name);
                        textVersionName.setText(versionName);
                        break;
                }
            }
            return row;
        }
    }
}
