package com.example.marmm.demolevel5;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marmm on 03/01/2018.
 */


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_FIRST_NAME = "reminders";
    public static final String DATABASE_NAME_EXTENSION = ".db";
    public static final String DATABASE_NAME = DATABASE_FIRST_NAME + DATABASE_NAME_EXTENSION;
    public static final int DATABASE_VERSION = 1;

    // Creating the table
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + RemindersContract.ReminderEntry.TABLE_NAME +
                    "(" +
                    RemindersContract.ReminderEntry._ID + " INTEGER PRIMARY KEY, " +
                    RemindersContract.ReminderEntry.COLUMN_NAME_REMINDER +
                    ");";

    //Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RemindersContract.ReminderEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
