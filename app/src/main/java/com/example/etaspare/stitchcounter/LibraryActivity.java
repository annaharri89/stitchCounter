package com.example.etaspare.stitchcounter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    /* TODO: Implement help button */
    /* TODO: implement layouts for different sized devices */
    /* TODO: Remove unneeded import statements throughout the app */
    /* TODO: have a textview that says "You have no saved projects" when there are no saved projects */
    /* TODO: REMOVE SUPERFLOUS CODE/COMMENTS */
    /* TODO: LOOK INTO LOAD BALANCERS */
    /* TODO: implement add image(s) to counter project */
    /* TODO: Implement share project to facebook, twitter, etc. */
    /*TODO Provide thorough documentation throughout app*/
    /* TODO FIgure out why changing a counter, then going to library, then loading the same counter, results in the counter not being updated, look into adapter.notifyDataSetChanged(); */
    /* TODO Figure out why changing a double counter's progress, hitting the back button, results in list item's progress not updating */
    /* TODO Figure out why clicking into a counter, hitting the back button, results in first item being displayed as both the first item and the last item in the listview */
    /* TODO fix issue: app crashes when you try to start a double counter without putting total rows */

    final private StitchCounterMenu toolBarMenu = new StitchCounterMenu(this);
    ConstraintLayout topLayout;
    private Button deleteSingle;
    private Button deleteMany;
    private Button cancelMany;
    private Context context = this;
    private CounterAdapter mAdapter;
    private ListView mListView; //TODO look into converting to local variable
    private Cursor tempCursor;
    private ArrayList<String> deleteManyArray = new ArrayList<>();
    protected Boolean deleteManyMode = false;

    /*
    Defines a projection that specifies which columns from the database will actually
    be used for the query.
     */
    String[] PROJECTION = {
            StitchCounterContract.CounterEntry._ID,
            StitchCounterContract.CounterEntry.COLUMN_TYPE,
            StitchCounterContract.CounterEntry.COLUMN_TITLE,
            StitchCounterContract.CounterEntry.COLUMN_STITCH_COUNTER_NUM,
            StitchCounterContract.CounterEntry.COLUMN_STITCH_ADJUSTMENT,
            StitchCounterContract.CounterEntry.COLUMN_ROW_COUNTER_NUM,
            StitchCounterContract.CounterEntry.COLUMN_ROW_ADJUSTMENT,
            StitchCounterContract.CounterEntry.COLUMN_TOTAL_ROWS,
            StitchCounterContract.CounterEntry.COLUMN_PROGRESS_PERCENT
    };

    /* Sort the results in the Cursor in ascending order */
    String SORTORDER =
            StitchCounterContract.CounterEntry.COLUMN_TITLE + " ASC";

    /*
    Was for the cursor adapter, specifies which columns go into which views, but is actually
    unused because of the CounterAdapter method getView overrides them. Still needed to pass
    to mAdpter when it's initialized.
    */
    String[] fromColumns = {StitchCounterContract.CounterEntry.COLUMN_TITLE};
    int[] toViews = {R.id.text1};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_delete).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            turnOnDeleteManyMode();
            return true;
        } else if (item.getItemId() == R.id.action_help) {
            openHelpMode();
            return true;
        } else {
            return toolBarMenu.handleMenu(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
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

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        float screenWidth = dm.widthPixels / dm.density;
        float screenHeight = dm.heightPixels / dm.density;
        Log.d("width", Float.toString(screenWidth));
        Log.d("height", Float.toString(screenHeight));
        //TODO: nexus 5x: width 411, height 683 NORMAL SIZE layout-w321dp-port and layout-h321dp-land
        //TODO: galaxy s5 width 360, height 640 NORMAL SIZE layout-w321dp-port and layout-h321dp-land
        //TODO: LG lucky L16 width 320, height 480 NORMAL SIZE layout-w320dp-h300dp-port and layout-h320dp-land
        //TODO: 3.4 WQVGA width 320, height 576 Normal Size layout-h500dp and layout-h320dp-land

        deleteMany = (Button) findViewById(R.id.delete_many);
        cancelMany = (Button) findViewById(R.id.cancel_many);

        /*
        Create an empty adapter that will be used to display the loaded data.
        Pass null for the cursor, then update it in onLoadFinished()
        */
        mAdapter = new CounterAdapter(getBaseContext(),
                R.layout.list_item_single_counter,
                null,
                fromColumns,
                toViews,
                0);
        mListView = (ListView) findViewById(R.id.list);
        mListView.setAdapter(mAdapter);

        /* Creating a loader for populating listview from sqlite database */
        /* This statement invokes the method onCreatedLoader() */
        getSupportLoaderManager().initLoader(0, null, this);

        /*
        + When deleteManyMode is off, parses db data for appropriate item and sends the data to
          appropriate activity (SingleCounterActivity or DoubleCounterActivity)/ starts the activity.
        + When deleteManyMode is on, checks and adds id to deleteManyArray or unchecks the check box
          and removes id from deleteManyArray
        */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tempCursor = (Cursor)parent.getItemAtPosition(position);
                if (!deleteManyMode) {
                    DatabaseUtils.dumpCursor(tempCursor); //TODO Use to figure out why counters don't save and clicking into a counter, hitting the back button, results in first item being displayed as both the first item and the last item in the listview
                    int _id = tempCursor.getInt(tempCursor.getColumnIndex(StitchCounterContract.CounterEntry._ID));
                    String type = tempCursor.getString(tempCursor.getColumnIndex(StitchCounterContract.CounterEntry.COLUMN_TYPE));
                    String name = tempCursor.getString(tempCursor.getColumnIndex(StitchCounterContract.CounterEntry.COLUMN_TITLE));
                    int stitch_counter_number = tempCursor.getInt(tempCursor.getColumnIndex(StitchCounterContract.CounterEntry.COLUMN_STITCH_COUNTER_NUM));
                    int stitch_adjustment = tempCursor.getInt(tempCursor.getColumnIndex(StitchCounterContract.CounterEntry.COLUMN_STITCH_ADJUSTMENT));
                    int row_counter_number = tempCursor.getInt(tempCursor.getColumnIndex(StitchCounterContract.CounterEntry.COLUMN_ROW_COUNTER_NUM));
                    int row_adjustment = tempCursor.getInt(tempCursor.getColumnIndex(StitchCounterContract.CounterEntry.COLUMN_ROW_ADJUSTMENT));
                    int total_rows = tempCursor.getInt(tempCursor.getColumnIndex(StitchCounterContract.CounterEntry.COLUMN_TOTAL_ROWS));
                    double progress_percent = tempCursor.getDouble(tempCursor.getColumnIndex(StitchCounterContract.CounterEntry.COLUMN_PROGRESS_PERCENT));

                    Bundle extras = new Bundle();
                    switch(type) {
                        case "Double":
                            extras.putInt("_id", _id);
                            extras.putString("name", name);
                            extras.putInt("stitch_counter_number", stitch_counter_number);
                            extras.putInt("stitch_adjustment", stitch_adjustment);
                            extras.putInt("row_counter_number", row_counter_number);
                            extras.putInt("row_adjustment", row_adjustment);
                            extras.putInt("total_rows", total_rows);
                            extras.putDouble("progress_percent", progress_percent);

                            Intent intentDouble = new Intent(getBaseContext(), DoubleCounterActivity.class);
                            intentDouble.putExtras(extras);
                            startActivity(intentDouble);
                            break;
                        case "Single":
                            extras.putInt("_id", _id);
                            extras.putString("name", name);
                            extras.putInt("stitch_counter_number", stitch_counter_number);
                            extras.putInt("stitch_adjustment", stitch_adjustment);

                            Intent intentSingle = new Intent(getBaseContext(), SingleCounterActivity.class);
                            intentSingle.putExtras(extras);
                            startActivity(intentSingle);
                            break;
                    }
                } else {
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        deleteManyArray.remove(tempCursor.getString(tempCursor.getColumnIndex(StitchCounterContract.CounterEntry._ID)));
                    } else {
                        checkBox.setChecked(true);
                        deleteManyArray.add(tempCursor.getString(tempCursor.getColumnIndex(StitchCounterContract.CounterEntry._ID)));
                    }
                }
            }
        });

        /*
        Called when a list item is long clicked. If not in deleteManyMode, sets the tempCursor,
        sets the delete button visible, sets the onclicklistener for the delete button,
        sets previously visible delete buttons invisible.
        */
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (deleteManyMode) {
                    return false;
                }
                tempCursor = (Cursor)parent.getItemAtPosition(position);
                if (deleteSingle != null) {
                    deleteSingle.setVisibility(View.INVISIBLE);
                }
                deleteSingle = (Button) view.findViewById(R.id.button_delete);
                deleteSingle.setOnClickListener(deleteClickListener);
                deleteSingle.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    /*
    Called when the delete button is clicked. Sets the delete button invisible and calls
    deleteFromDb AsyncTask
    */
    private View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String id = tempCursor.getString(tempCursor.getColumnIndex(StitchCounterContract.CounterEntry._ID));
            ArrayList<String> ids = new ArrayList<>();
            ids.add(id);
            v.setVisibility(View.INVISIBLE);
            DeleteFromDb deleteFromDb = new DeleteFromDb(context);
            deleteFromDb.execute(ids); //TODO look into warning
        }
    };

    /*
    Called when the deleteMany button is pressed. Sends the deleteManyArray to the deleteFromDb
    AsyncTask if there's at least 1 id in the array and then turns off deleteManyMode.
    */
    protected void deleteMany(View view) {
        if (deleteManyArray.size() > 0) {
            DeleteFromDb deleteFromDb = new DeleteFromDb(context);
            deleteFromDb.execute(deleteManyArray);
        }
        turnOffDeleteManyMode(view);
    }

    /*
    Sets the bottom edge of the list view to the edge (top or bottom, passed through
    constrainingEdge) of the element (referenced by elementID).
    */
    protected void setListConstraints(int elementID, int constrainingEdge) {
        ConstraintSet set = new ConstraintSet();
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.library_layout);
        set.clone(layout);
        set.connect(R.id.list, ConstraintSet.BOTTOM, elementID, constrainingEdge, 0);
        set.applyTo(layout);
    }

    /*
    + Sets deleteManyMode to true, thereby changing how the itemClickListener handles clicks and
      blocking long clicks so that single delete buttons cannot appear.
    + Calls setListConstraints to connect the bottom of list to the top of the delete_many button
    + Sets the delete and cancel buttons to visible
    */
    public void turnOnDeleteManyMode() {
        deleteManyMode = true;
        setListConstraints(R.id.delete_many, ConstraintSet.TOP);
        deleteMany.setVisibility(View.VISIBLE);
        cancelMany.setVisibility(View.VISIBLE);
    }

    /*
    + Sets the deleteManyMode to false, thereby changing how the itemClickListener handles clicks and
      allowing long clicks to happen so single delete buttons can appear
    + Calls setListConstraints to connect the bottom of list to the bottom of the constraint layout.
    + Sets the delete and cancel buttons to invisible
    */
    protected void turnOffDeleteManyMode(View view) {
        deleteManyMode = false;
        setListConstraints(R.id.library_layout, ConstraintSet.BOTTOM);
        deleteMany.setVisibility(View.INVISIBLE);
        cancelMany.setVisibility(View.INVISIBLE);
    }

    /*
    Called from DeleteFromDb AsyncTask during onPostExecute (after project gets deleted from db).
    Updates the listview by removing appropriate project item(s)
    */
    protected void updateCursor() {
        getSupportLoaderManager().restartLoader(0, null, this);
        mAdapter.notifyDataSetChanged();
    }

    /* A callback method invoked by the loader when initLoader() is called */
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        Uri uri = CounterProject.CONTENT_URI;
        return new CursorLoader(this, uri, PROJECTION, null, null, SORTORDER);
    }

    /* A callback method, invoked after the requested content provider has returned all the data */
    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        mAdapter.swapCursor(arg1);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onResume() {
        /* Allows list to function properly when accessed through back button */
        getSupportLoaderManager().restartLoader(0, null, this); //TODO ASK tony if this should be restartLoader or initLoader
        super.onResume();
    }

    /*
    Opens "help mode" Called when help button is clicked in the action bar. Sets the top layer
    visible, showing the annotation bubbles.
    */
    public void openHelpMode() {
        topLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        if (tempCursor != null && !tempCursor.isClosed()) {
            tempCursor.close();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (tempCursor != null && !tempCursor.isClosed()) {
            tempCursor.close();
        }
        super.onDestroy();
    }

    public class CounterAdapter extends SimpleCursorAdapter {

        Context mContext;
        Cursor cursor;

        public CounterAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            this.mContext = context;
        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public int getItemViewType(int position) {
            cursor = mAdapter.getCursor();
            cursor.moveToPosition(position);
            String type = cursor.getString(1);
            if (type.equals("Double") && !deleteManyMode) {
                return 0;
            } else if (type.equals("Single") && !deleteManyMode) {
                return 1;
            } else if (type.equals("Double") && deleteManyMode)  {
                return 2;
            } else if (type.equals("Single") && deleteManyMode) {
                return 3;
            }
            return 0;
        }

        /*
        Handles the four different list item layouts: double counter, double counter in delete many
        mode, single counter, and single counter in delete many mode.
        */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            int type = getItemViewType(position);
            cursor = mAdapter.getCursor();
            cursor.moveToPosition(position);
            if (v == null) {
                // Inflate the layout according to the view type
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                TextView textTitle;
                CheckBox checkTitle;
                ProgressBar progress;
                switch(type) {
                    case 0:
                        v = inflater.inflate(R.layout.list_item_double_counter, parent, false);

                        textTitle = (TextView) v.findViewById(R.id.text2);
                        progress = (ProgressBar) v.findViewById(R.id.progressBar_list_item);

                        textTitle.setText(cursor.getString(2));
                        progress.setMax(cursor.getInt(7));
                        progress.setProgress(cursor.getInt(5));
                        break;
                    case 1:
                        v = inflater.inflate(R.layout.list_item_single_counter, parent, false);

                        textTitle = (TextView) v.findViewById(R.id.text1);

                        textTitle.setText(cursor.getString(2));
                        break;
                    case 2:
                        v = inflater.inflate(R.layout.list_item_double_counter_check, parent, false);

                        checkTitle = (CheckBox) v.findViewById(R.id.checkBox);
                        progress = (ProgressBar) v.findViewById(R.id.progressBar_list_item);

                        checkTitle.setText(cursor.getString(2));
                        progress.setMax(cursor.getInt(7));
                        progress.setProgress(cursor.getInt(5));
                        break;
                    case 3:
                        v = inflater.inflate(R.layout.list_item_single_counter_check, parent, false);
                        checkTitle = (CheckBox) v.findViewById(R.id.checkBox);

                        checkTitle.setText(cursor.getString(2));
                }
            }
            return v;
        }
    }
}

