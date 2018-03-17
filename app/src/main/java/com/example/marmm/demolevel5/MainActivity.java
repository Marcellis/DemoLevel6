package com.example.marmm.demolevel5;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements ReminderAdapter.ReminderClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    //Local variables
    //public static List<Reminder> mReminders;
    private ReminderAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private EditText mNewReminderText;

    //Database related local variables
    private Cursor mCursor;

    //Constants used when calling the update activity
    public static final String REMINDER_POSITION = "Position";
    public static final int REQUESTCODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportLoaderManager().initLoader(0, null, this);

        mNewReminderText = findViewById(R.id.editText_main);
      //  mReminders = new ArrayList<>();

        //Initialize the local variables

        mRecyclerView = findViewById(R.id.recyclerView);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));



        updateUI();
/*

            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                return true;
            }
        });

*/

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the user text from the textfield
                String text = mNewReminderText.getText().toString();
                Reminder newReminder = new Reminder(text);

//Check if some text has been added
                if (!(TextUtils.isEmpty(text))) {
                    //Add the text to the list (datamodel)
                    //mReminders.add(newReminder);
                    ContentValues values = new ContentValues();
                    values.put(RemindersContract.ReminderEntry.COLUMN_NAME_REMINDER, text);
                    getContentResolver().insert(RemindersContract.CONTENT_URI, values);

//Tell the adapter that the data set has been modified: the screen will be refreshed.
                    updateUI();

                    //Initialize the EditText for the next item
                    mNewReminderText.setText("");
                } else {
                    //Show a message to the user if the textfield is empty
                    Snackbar.make(view, "Please enter some text in the textfield", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }
        });

        /*
        Add a touch helper to the RecyclerView to recognize when a user swipes to delete a list entry.
        An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
                and uses callbacks to signal when a user is performing these actions.
                */
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                        //Get the index corresponding to the selected position
                        int clickedPosition = (viewHolder.getAdapterPosition());

                        if (!mCursor.moveToPosition(clickedPosition))
                            return; // bail if returned null
                        // Update the view holder with the information needed to display
                        final long id =
                                mCursor.getLong(mCursor.getColumnIndex(RemindersContract.ReminderEntry._ID));
                         Uri singleUri = ContentUris.withAppendedId(RemindersContract.CONTENT_URI,id);
                        getContentResolver().delete(singleUri, null, null);
                        updateUI();
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


    }

    private void updateUI() {
        mCursor =   getContentResolver().query (RemindersContract.CONTENT_URI, null,null, null, null);

        if (mAdapter == null) {
            mAdapter = new ReminderAdapter (this, mCursor );
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapCursor(mCursor);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*

    @Override
    public void reminderOnLongClick(int i) {
        mReminders.remove(i);
        updateUI();
    }

    @Override
    public void reminderOnClick(int i) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        intent.putExtra(REMINDER_POSITION, i);
        startActivity(intent);


    }
*/


    protected void onResume() {
        super.onResume();
        updateUI();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mCursor != null && !mCursor.isClosed()) mCursor.close();
    }


    @Override
    public void reminderOnClick(long id) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        intent.putExtra(REMINDER_POSITION, id);
        startActivity (intent);
    }


    @Override
    public void reminderOnLongClick(long id) {
        Uri singleUri = ContentUris.withAppendedId(RemindersContract.CONTENT_URI,id);
        getContentResolver().delete(singleUri, null, null);
        updateUI();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        CursorLoader cursorLoader = new CursorLoader(this, RemindersContract.CONTENT_URI, null,
                null, null, null);
        return cursorLoader;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (mAdapter == null) {
            mAdapter = new ReminderAdapter (this, cursor );
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapCursor(cursor);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
