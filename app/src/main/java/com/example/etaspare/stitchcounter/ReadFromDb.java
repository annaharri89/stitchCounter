package com.example.etaspare.stitchcounter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by ETASpare on 6/28/2017.
 */

public class ReadFromDb extends AsyncTask<Void, Void, SimpleCursorAdapter> {
    private Context mContext;
    private View mView;
    Cursor cursor;
    SQLiteDatabase db;

    public ReadFromDb (Context context, View view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    protected SimpleCursorAdapter doInBackground(Void... params) {
        StitchCounterDbHelper dbHelper = new StitchCounterDbHelper(this.mContext); //TODO might be the wrong context
        db = dbHelper.getReadableDatabase(); // Gets the data repository in read mode
        SimpleCursorAdapter mAdapter; // Adapter used to display the list's data

        /*
            Defines a projection that specifies which columns from the database will actually
            be used after this query.
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

        /* How the results are sorted in the resulting Cursor */
        String SORTORDER = StitchCounterContract.CounterEntry.COLUMN_TITLE + " ASC";

        cursor = db.query(
                StitchCounterContract.CounterEntry.TABLE_NAME,  // The table to query
                PROJECTION,                                     // The columns to return
                null,                                           // The columns for the WHERE clause
                null,                                           // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                SORTORDER                                       // The sort order
        );

        /* For the cursor adapter, specify which columns go into which views */
        /* Double Counter */
        String[] fromColumnsDouble = {StitchCounterContract.CounterEntry.COLUMN_TITLE,
                StitchCounterContract.CounterEntry.COLUMN_ROW_COUNTER_NUM};
        int[] toViewsDouble = {R.id.text2, R.id.progressBar_list_item};
            /* Single Counter */
            /*
            String[] fromColumnsSingle = {StitchCounterContract.CounterEntry.COLUMN_TITLE};
            int[] toViewsSingle = {R.id.text_project_name_list_item_single_counter};
            */
        String[] fromColumnsSingle = {StitchCounterContract.CounterEntry.COLUMN_TITLE};
        int[] toViewsSingle = {android.R.id.text1};

        try {
            // Create an empty adapter we will use to display the loaded data.
            // We pass null for the cursor, then update it in onLoadFinished()
            mAdapter = new SimpleCursorAdapter(this.mContext, android.R.layout.simple_list_item_1, cursor, fromColumnsSingle, toViewsSingle, 0);

        } finally {
            //db.close();
        }
        return mAdapter;
    }

    @Override
    protected void onPostExecute(SimpleCursorAdapter adapter) {

        //ListView listView = getListView();
        ListView listView = (ListView) this.mView;
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor tempCursor = (Cursor)parent.getItemAtPosition(position);
                long _id = Long.parseLong(tempCursor.getString(0));
                String type = tempCursor.getString(1);
                String name = tempCursor.getString(2);
                int stitch_counter_number = Integer.parseInt(tempCursor.getString(3));
                int stitch_adjustment = Integer.parseInt(tempCursor.getString(4));
                int row_counter_number = Integer.parseInt(tempCursor.getString(5));
                int row_adjustment = Integer.parseInt(tempCursor.getString(6));
                int total_rows = Integer.parseInt(tempCursor.getString(7));
                double progress_percent = Double.parseDouble(tempCursor.getString(8));

                Bundle extras = new Bundle();

                Log.d("kjdnvsncslcnd", type);

                switch(type) {
                    case "Double":
                        Log.d("type", type);
                        extras.putLong("_id", _id);
                        extras.putString("name", name);
                        extras.putInt("stitch_counter_number", stitch_counter_number);
                        extras.putInt("stitch_adjustment", stitch_adjustment);
                        extras.putInt("row_counter_number", row_counter_number);
                        extras.putInt("row_adjustment", row_adjustment);
                        extras.putInt("total_rows", total_rows);
                        extras.putDouble("progress_percent", progress_percent);

                        Intent intentDouble = new Intent(mContext, DoubleCounterActivity.class);
                        intentDouble.putExtras(extras);
                        mContext.startActivity(intentDouble);
                        //TODO FIGURE OUT WHERE TO CLOSE THE CURSOR
                        //tempCursor.close();
                        //cursor.close();
                        //db.close();
                        break;
                    case "Single":
                        Log.d("type", type);
                        extras.putLong("_id", _id);
                        extras.putString("name", name);
                        extras.putInt("stitch_counter_number", stitch_counter_number);
                        extras.putInt("stitch_adjustment", stitch_adjustment);

                        Intent intentSingle = new Intent(mContext, SingleCounterActivity.class);
                        intentSingle.putExtras(extras);
                        mContext.startActivity(intentSingle);
                        //tempCursor.close();
                        //cursor.close();
                        //db.close();
                        break;
                }
            }
        });

    }
}
