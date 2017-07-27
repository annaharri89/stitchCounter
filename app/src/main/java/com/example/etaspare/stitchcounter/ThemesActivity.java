package com.example.etaspare.stitchcounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
        Theme _defaultDark = new Theme("Default Dark", Color.parseColor("#3F51B5"), Color.parseColor("#303F9F"), Color.parseColor("#FF4081"));
        Theme cottonCandy = new Theme ("Cotton Candy", Color.parseColor("#F48FB1"), Color.parseColor("#F06292"), Color.parseColor("#CE93D8"));
        Theme cottonCandyDark = new Theme ("Cotton Candy Dark", Color.parseColor("#F48FB1"), Color.parseColor("#F06292"), Color.parseColor("#CE93D8"));
        Theme robinsEggBlue = new Theme("Robins Egg Blue", Color.parseColor("#18FFFF"), Color.parseColor("#00E5FF"), Color.parseColor("#FFD740"));
        Theme robinsEggBlueDark = new Theme("Robins Egg Blue Dark", Color.parseColor("#18FFFF"), Color.parseColor("#00E5FF"), Color.parseColor("#FFD740"));
        themesList.add(_default);
        themesList.add(_defaultDark);
        themesList.add(cottonCandy);
        themesList.add(cottonCandyDark);
        themesList.add(robinsEggBlue);
        themesList.add(robinsEggBlueDark);
        mAdapter = new ThemeAdapter(
                this,
                R.layout.list_item_theme,
                themesList) {
        };
        mListView.setAdapter(mAdapter);

        /*
        Updates SharedPreferences with appropriate theme string. Finishes current activity
        and starts a new activity so theme can be set.
        */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    default:
                    case 0:
                        utils.updateSharedPreferences(0);
                        finish();
                        startActivity(new Intent(getBaseContext(), ThemesActivity.class));
                        break;
                    case 1:
                        utils.updateSharedPreferences(1);
                        finish();
                        startActivity(new Intent(getBaseContext(), ThemesActivity.class));
                        break;
                    case 2:
                        utils.updateSharedPreferences(2);
                        finish();
                        startActivity(new Intent(getBaseContext(), ThemesActivity.class));
                        break;
                    case 3:
                        utils.updateSharedPreferences(3);
                        finish();
                        startActivity(new Intent(getBaseContext(), ThemesActivity.class));
                        break;
                    case 4:
                        utils.updateSharedPreferences(4);
                        finish();
                        startActivity(new Intent(getBaseContext(), ThemesActivity.class));
                        break;
                    case 5:
                        utils.updateSharedPreferences(5);
                        finish();
                        startActivity(new Intent(getBaseContext(), ThemesActivity.class));
                        break;
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
            Resources res = getResources();
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
            /*
            Sets odd rows background color to darkgrey and textcolor to white, sets even rows
            background color to white and textcolor to black
            */
            if (position%2 == 0) {
                if (Build.VERSION.SDK_INT < 23) {
                    /* Android Support Library 22 and earlier compatible */
                    holder.textView1.setTextColor(res.getColor(R.color.black));
                } else {
                    /* Android Support Library 23 compatible */
                    holder.textView1.setTextColor(ContextCompat.getColor(this.context, R.color.black));
                }
                row.setBackgroundColor(Color.parseColor("#FFFAFAFA"));
            } else {
                if (Build.VERSION.SDK_INT < 23) {
                    /* Android Support Library 22 and earlier compatible */
                    holder.textView1.setTextColor(res.getColor(R.color.white));
                } else {
                    /* Android Support Library 23 compatible */
                    holder.textView1.setTextColor(ContextCompat.getColor(this.context, R.color.white));
                }
                row.setBackgroundColor(Color.parseColor("#303030"));
            }

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


