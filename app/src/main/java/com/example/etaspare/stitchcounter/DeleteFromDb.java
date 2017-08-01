package com.example.etaspare.stitchcounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import java.util.ArrayList;

/**
 * Created by ETASpare on 7/3/2017.
 */

public class DeleteFromDb extends AsyncTask<ArrayList<String>, Void, Integer> {

    private Context mContext;

    public DeleteFromDb(Context context) {
        this.mContext = context;
    }

    @Override
    protected Integer doInBackground(ArrayList<String>... ids) {
        StitchCounterDbHelper dbHelper = new StitchCounterDbHelper(this.mContext); //TODO might be the wrong context
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Gets the data repository in write mode
        try {
            for (String id: ids[0]) {
                db.delete(StitchCounterContract.CounterEntry.TABLE_NAME,
                        "_id = ?",
                        new String[]{id});
            }
        } finally {
            db.close();
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer z) {
        LibraryActivity context = (LibraryActivity) this.mContext;
        context.updateCursor();
    }
}
