package com.example.etaspare.stitchcounter;

import android.provider.BaseColumns;

/**
 * Created by ETASpare on 6/15/2017.
 */

public final class StitchCounterContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private StitchCounterContract() {
    }

    /*
    SINGLE COUNTER
    public static class CounterEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_COUNTER_NUM = "counter number";
        public static final String COLUMN_NAME_ADJUSTMENT = "adjustment";
    }

    1 of the DOUBLE COUNTERS
    public static class CounterEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_COUNTER_NUM = "counter number";
        public static final String COLUMN_NAME_ADJUSTMENT = "adjustment";
        public static final String COLUMN_NAME_TOTAL_ROWS = "total rows";
        public static final String COLUMN_NAME_PROGRESS_PERCENT = "progress percent";
    }

    DOUBLE COUNTER
    public static class CounterEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_COUNTER_STITCH_NUM = "stitch counter number";
        public static final String COLUMN_NAME_STITCH_ADJUSTMENT = "stitch adjustment";
        public static final String COLUMN_NAME_COUNTER_ROW_NUM = "row counter number";
        public static final String COLUMN_NAME_ROW_ADJUSTMENT = "row adjustment";
        public static final String COLUMN_NAME_TOTAL_ROWS = "total rows";
        public static final String COLUMN_NAME_PROGRESS_PERCENT = "progress percent";
    }

    */
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

