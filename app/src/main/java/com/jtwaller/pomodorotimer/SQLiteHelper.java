package com.jtwaller.pomodorotimer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteHelper";

    private static final String DATABASE_NAME = "pomo.db";
    // Why do examples start at 2?
    private static final int DATABASE_VERSION = 2;
    private static final String POMO_TABLE_NAME = "pomo";
    private static final String CREATED_DATE = "date";
    private static final String TIME_STARTED = "time_created";
    private static final String POMO_COMPLETED = "completed";

    private static final String POMO_TABLE_CREATE =
            "CREATE TABLE " + POMO_TABLE_NAME + " (" +
                    CREATED_DATE + " TEXT, " +
                    TIME_STARTED + " TEXT, " +
                    POMO_COMPLETED + " BOOLEAN"
                    + ");";

    SQLiteHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(POMO_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }

}
