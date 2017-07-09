package com.example.etaspare.stitchcounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

/**
 * Created by ETASpare on 6/15/2017.
 */

public class WriteToDb extends AsyncTask<Counter, Void, Void> {

    private Context mContext;

    public WriteToDb (Context context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Counter... counter) {
        StitchCounterDbHelper dbHelper = new StitchCounterDbHelper(this.mContext); //TODO might be the wrong context
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Gets the data repository in write mode
        try {
            ContentValues values;
            if (counter.length > 1) {
                values = doubleCounterUpdate(counter);
            } else {
                values = singleCounterUpdate(counter);
            }
            if (values.get(StitchCounterContract.CounterEntry._ID) != null) {
                String strFilter = StitchCounterContract.CounterEntry._ID + "=" + values.get(StitchCounterContract.CounterEntry._ID);
                db.update(StitchCounterContract.CounterEntry.TABLE_NAME, values, strFilter, null);
            } else {
                counter[0].ID = (int) db.insert(StitchCounterContract.CounterEntry.TABLE_NAME, null, values);
            }
        } finally {
            db.close();
        }
        return null;
    }

    /* Called when a single counter opject is passed in the asynctask */
    protected ContentValues singleCounterUpdate(Counter... counter) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        if (counter[0].ID > 0) {
            values.put(StitchCounterContract.CounterEntry._ID, counter[0].ID);
        }
        values.put(StitchCounterContract.CounterEntry.COLUMN_TYPE, "Single");
        values.put(StitchCounterContract.CounterEntry.COLUMN_TITLE, counter[0].projectName);
        values.put(StitchCounterContract.CounterEntry.COLUMN_STITCH_COUNTER_NUM, counter[0].counterNumber);
        values.put(StitchCounterContract.CounterEntry.COLUMN_STITCH_ADJUSTMENT, counter[0].adjustment);
        values.put(StitchCounterContract.CounterEntry.COLUMN_ROW_COUNTER_NUM, "");
        values.put(StitchCounterContract.CounterEntry.COLUMN_ROW_ADJUSTMENT, "");
        values.put(StitchCounterContract.CounterEntry.COLUMN_TOTAL_ROWS, "");
        values.put(StitchCounterContract.CounterEntry.COLUMN_PROGRESS_PERCENT, "");
        return values;
    }

    /* Called when two counter objects are passed in the asynctask */
    protected ContentValues doubleCounterUpdate(Counter... counter) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        if (counter[0].ID > 0) {
            values.put(StitchCounterContract.CounterEntry._ID, counter[0].ID);
        }
        values.put(StitchCounterContract.CounterEntry.COLUMN_TYPE, "Double");
        values.put(StitchCounterContract.CounterEntry.COLUMN_TITLE, counter[1].projectName);
        values.put(StitchCounterContract.CounterEntry.COLUMN_STITCH_COUNTER_NUM, counter[0].counterNumber);
        values.put(StitchCounterContract.CounterEntry.COLUMN_STITCH_ADJUSTMENT, counter[0].adjustment);
        values.put(StitchCounterContract.CounterEntry.COLUMN_ROW_COUNTER_NUM, counter[1].counterNumber);
        values.put(StitchCounterContract.CounterEntry.COLUMN_ROW_ADJUSTMENT, counter[1].adjustment);
        values.put(StitchCounterContract.CounterEntry.COLUMN_TOTAL_ROWS, counter[1].totalRows);
        values.put(StitchCounterContract.CounterEntry.COLUMN_PROGRESS_PERCENT, counter[1].progressPercent);
        return values;
    }
}
