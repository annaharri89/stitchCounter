package com.example.etaspare.stitchcounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class ThemesActivity extends AppCompatActivity {

    //TODO finish TOOLBAR setup
    //TODO find different main color for robins egg blue
    //TODO implement 3 more themes, in dark mode
    //TODO update capsule button colors in Counter
    //TODO implement text color in styles for help bubbles (robins egg blue needs black text)
    private ListView mListView;
    private ArrayAdapter<Theme> mAdapter;
    private Utils utils = new Utils(this);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_delete).setVisible(false);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        utils.updateTheme(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        //Creates a back button that takes user back to settings.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /* Setup ListView */
        mListView = (ListView) findViewById(R.id.list);
        ArrayList<Theme> themesList = new ArrayList<>();
        Theme _default = new Theme("Default", Color.parseColor("#3F51B5"), Color.parseColor("#303F9F"), Color.parseColor("#FF4081"));
        Theme cottonCandy = new Theme ("Cotton Candy", Color.parseColor("#F48FB1"), Color.parseColor("#F06292"), Color.parseColor("#CE93D8"));
        Theme robinsEggBlue = new Theme("Robins Egg Blue", Color.parseColor("#18FFFF"), Color.parseColor("#00E5FF"), Color.parseColor("#FFD740"));
        themesList.add(_default);
        themesList.add(cottonCandy);
        themesList.add(robinsEggBlue);
        mAdapter = new ThemeAdapter(
                this,
                R.layout.theme_list_item,
                themesList) {
        };
        mListView.setAdapter(mAdapter);

        /*
        Updates SharedPreferences with appropriate theme string. Finishes current activity
        and starts a new activity
        */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    utils.updateSharedPreferences(0);
                    finish();
                    startActivity(new Intent(getBaseContext(), ThemesActivity.class));
                } else if (position == 1) {
                    utils.updateSharedPreferences(1);
                    finish();
                    startActivity(new Intent(getBaseContext(), ThemesActivity.class));
                } else if (position == 2) {
                    utils.updateSharedPreferences(2);
                    finish();
                    startActivity(new Intent(getBaseContext(), ThemesActivity.class));
                }
            }
        });
    }

    /* Starts a new settings activity so that theme changes can be applied*/
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, SettingsActivity.class));
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

    /* Theme adapter allows the theme titles and colors to be displayed. */
    public class ThemeAdapter extends ArrayAdapter<Theme> {
        private final Context context;
        private final ArrayList<Theme> data;
        private final int layoutResourceId;

        public ThemeAdapter(Context context, int layoutResourceId, ArrayList<Theme> data) {
            super(context, layoutResourceId, data);
            this.context = context;
            this.data = data;
            this.layoutResourceId = layoutResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new ViewHolder();
                holder.textView1 = (TextView) row.findViewById(R.id.textView);
                holder.color1View = row.findViewById(R.id.color_1_view);
                holder.color2View = row.findViewById(R.id.color_2_view);
                holder.color3View = row.findViewById(R.id.color_3_view);

                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            Theme theme = data.get(position);

            holder.textView1.setText(theme.title);
            holder.color1View.setBackgroundColor(theme.color1);
            holder.color2View.setBackgroundColor(theme.color2);
            holder.color3View.setBackgroundColor(theme.color3);

            return row;
        }
    }

        static class ViewHolder
        {
            TextView textView1;
            View color1View;
            View color2View;
            View color3View;
        }
    }


