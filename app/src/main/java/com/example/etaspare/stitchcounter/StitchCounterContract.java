package com.example.etaspare.stitchcounter;

import android.provider.BaseColumns;

/**
 * Created by ETASpare on 6/15/2017.
 */

public final class StitchCounterContract {
    // To prevent someone from accidentally instantiating the contract class,
    // the constructor is private.
    private StitchCounterContract() {
    }

    /* Inner class that defines the table contents */
    public static class CounterEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_STITCH_COUNTER_NUM = "stitch_counter_number";
        public static final String COLUMN_STITCH_ADJUSTMENT = "stitch_adjustment";
        public static final String COLUMN_ROW_COUNTER_NUM = "row_counter_number";
        public static final String COLUMN_ROW_ADJUSTMENT = "row_adjustment";
        public static final String COLUMN_TOTAL_ROWS = "total_rows";
    }
}

