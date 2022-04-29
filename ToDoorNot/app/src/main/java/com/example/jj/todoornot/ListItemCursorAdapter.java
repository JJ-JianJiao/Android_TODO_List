package com.example.jj.todoornot;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ListItemCursorAdapter extends SimpleCursorAdapter {
    private Context mContext;
    public ListItemCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        mContext = context;
    }

    public ListItemCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void setViewText(TextView v, String text) {
        if(v.getId()==R.id.item_description) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                String mFontSize = sharedPreferences.getString("font_size_pref", "24");
                String mFontColor = sharedPreferences.getString("font_color_pref", "Black");
                v.setTextColor(Color.parseColor(mFontColor));
                v.setTextSize(Float.valueOf(mFontSize));
        }

        if(v.getId()==R.id.item_completed){
            if(text.equals("Completed")){
                v.setTextColor(Color.GREEN);
            }else{
                v.setTextColor(Color.RED);
            }
        }
        super.setViewText(v, text);
    }
}
