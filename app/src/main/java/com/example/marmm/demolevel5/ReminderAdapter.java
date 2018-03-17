package com.example.marmm.demolevel5;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by marmm on 11/1/17.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    final private ReminderClickListener mReminderClickListener;

    private Cursor mCursor;

    public interface ReminderClickListener{
        void reminderOnClick (long id);
        void reminderOnLongClick (long id);
    }

    public ReminderAdapter(ReminderClickListener mReminderClickListener, Cursor mCursor) {
        this.mReminderClickListener = mReminderClickListener;
        this.mCursor = mCursor;
    }

    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        View view = inflater.inflate(android.R.layout.simple_list_item_1, null);

// Return a new holder instance
        ReminderAdapter.ViewHolder viewHolder = new ReminderAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReminderAdapter.ViewHolder holder, final int position) {

        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null
        String name = mCursor.getString(mCursor.getColumnIndex(RemindersContract.ReminderEntry.COLUMN_NAME_REMINDER));

        holder.textView.setText(name);

    }

    @Override
    public int getItemCount() {
        return (mCursor == null ? 0 : mCursor.getCount());
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{
        public TextView textView;

        public ViewHolder(View itemView) {

            super(itemView);
            textView=  itemView.findViewById(android.R.id.text1);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {



            int clickedPosition =  getAdapterPosition();
            if (!mCursor.moveToPosition(clickedPosition))
                return false; // bail if returned null
            // Update the view holder with the information needed to display
            final long id =
                    mCursor.getLong(mCursor.getColumnIndex(RemindersContract.ReminderEntry._ID));
            mReminderClickListener.reminderOnLongClick(id);
            return true;
        }

        @Override
        public void onClick(View view) {
            int clickedPosition =  getAdapterPosition();
            if (!mCursor.moveToPosition(clickedPosition))
            return; // bail if returned null
            // Update the view holder with the information needed to display
            final long id =
                    mCursor.getLong(mCursor.getColumnIndex(RemindersContract.ReminderEntry._ID));
            mReminderClickListener.reminderOnClick(id);
        }
    }

    public void swapCursor(Cursor newCursor) {

        if (mCursor != null) mCursor.close();

        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

}
