package com.example.jj.todoornot;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ListTitleCursorAdapter extends SimpleCursorAdapter {

    private LayoutInflater layoutInflater;
    private Context mContext;
    public ListTitleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        mContext = context;
//        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rowView = layoutInflater.inflate(layout,null);
//
//        TextView textView = rowView.findViewById(R.id.list_title_listname);
//        textView.setTextSize(1.0f);
//        textView.setTextColor(Color.WHITE);
    }

    public ListTitleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void setViewText(TextView v, String text) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String mFontSize = sharedPreferences.getString("font_size_pref","24");
        String mFontColor = sharedPreferences.getString("font_color_pref","Black");
        v.setTextColor(Color.parseColor(mFontColor));
        v.setTextSize(Float.valueOf(mFontSize));
        super.setViewText(v, text);
    }
}
