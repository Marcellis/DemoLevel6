package com.example.marmm.demolevel5;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class UpdateActivity extends AppCompatActivity {

    private EditText mReminderView;

    private long mID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Init local variables
        mReminderView = (EditText) findViewById(R.id.editText_update);

//Obtain the parameters provided by MainActivity
        mID = getIntent().getLongExtra(MainActivity.REMINDER_POSITION, -1);
//If no "position in list" can be found, the default value is -1. This could be used to recognize an issue.

        Uri singleUri = ContentUris.withAppendedId(RemindersContract.CONTENT_URI,mID);
        Cursor mCursor =   getContentResolver().query (singleUri,null,null, null, null);
        if (mCursor != null)
            mCursor.moveToFirst();

        mReminderView.setText(mCursor.getString(mCursor.getColumnIndex(RemindersContract.ReminderEntry.COLUMN_NAME_REMINDER)));


//        final Reminder reminderUpdate =   MainActivity.mReminders.get(position);
 //       mReminderView.setText (reminderUpdate.getmReminderText());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Return entered data to MainActivity (if not empty, else throw a snackbar message
                String updatedReminderText = mReminderView.getText().toString();


                //(reminderUpdate.setmReminderText(updatedReminderText)));
                if (!TextUtils.isEmpty(updatedReminderText)){


                    ContentValues values = new ContentValues();
                    values.put(RemindersContract.ReminderEntry.COLUMN_NAME_REMINDER,updatedReminderText);
                    Uri singleUri = ContentUris.withAppendedId(RemindersContract.CONTENT_URI,mID);
                    getContentResolver().update(singleUri, values, null, null);

                 //   Toast.makeText(UpdateActivity.this, reminderUpdate+" updated to"+ updatedReminderText, Toast.LENGTH_SHORT).show();
                     finish();
                } else {
                    Snackbar.make(view, "Enter some data", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }




}
