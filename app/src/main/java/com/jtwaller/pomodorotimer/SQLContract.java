package com.jtwaller.pomodorotimer;

import android.provider.BaseColumns;

public class SQLContract {
    private SQLContract() {
        // empty to prevent instantiating
    }

    public static final String DATABASE_NAME = "pomo.db";
    public static final int DATABASE_VERSION = 1;

    public static abstract class PomoTable implements BaseColumns {
        public static final String TABLE_NAME = "PomoTable";
        public static final String COLUMN_NAME_DATE_CREATED = "date";
        public static final String COLUMN_NAME_COMPLETED = "completed";

        public static final String TABLE_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_DATE_CREATED + " INT, " +
                        COLUMN_NAME_COMPLETED + " BOOLEAN"
                        + ");";

        public static final String TABLE_DROP =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}