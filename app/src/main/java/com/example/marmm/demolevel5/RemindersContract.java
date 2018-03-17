package com.example.marmm.demolevel5;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by marmm on 03/01/2018.
 */

public final class RemindersContract {


    public static final String AUTHORITY = "com.example.provider.reminders";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY  + "/" + RemindersContract.ReminderEntry.TABLE_NAME);

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private RemindersContract() {}

    /* Inner class that defines the table contents */
    public static class ReminderEntry implements BaseColumns {
        public static final String TABLE_NAME = "Reminders";
        public static final String COLUMN_NAME_REMINDER = "reminder";

    }
}

