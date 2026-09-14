package com.salisou.fitnesstracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ActivityDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fitness_tracker.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ACTIVITIES = "activities";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STEPS = "steps";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_WORKOUT = "workout";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_DATE = "date";

    public ActivityDatabaseHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE "
                + TABLE_ACTIVITIES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_STEPS + " INTEGER, "
                + COLUMN_CALORIES + " INTEGER, "
                + COLUMN_WORKOUT + " TEXT, "
                + COLUMN_DURATION + " INTEGER, "
                + COLUMN_DATE + " TEXT"
                + ")";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        db.execSQL(
                "DROP TABLE IF EXISTS "
                        + TABLE_ACTIVITIES
        );

        onCreate(db);
    }
}