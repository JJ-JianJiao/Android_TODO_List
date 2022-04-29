package com.example.jj.todoornot;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CloudItemListViewAdapter extends BaseAdapter {

    private Context context;
    private List<CloudItem> cloudItemList;
    private LayoutInflater layoutInflater;

    public CloudItemListViewAdapter(Context context, List<CloudItem> couldItemList) {
        this.context = context;
        this.cloudItemList = couldItemList;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cloudItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return cloudItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View rowView = layoutInflater.inflate(R.layout.list_cloud_item_items,null);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mFontSize = sharedPreferences.getString("font_size_pref", "24");
        String mFontColor = sharedPreferences.getString("font_color_pref", "Black");

        TextView postDateTextView =rowView.findViewById(R.id.cloud_item_date);
        postDateTextView.setTextColor(Color.BLACK);

        TextView contentTextView =rowView.findViewById(R.id.cloud_item_description);
//        contentTextView.setTextColor(Color.BLACK);
        contentTextView.setTextSize(Float.valueOf(mFontSize));
        contentTextView.setTextColor(Color.parseColor(mFontColor));

        TextView completedFlagTextView =rowView.findViewById(R.id.cloud_item_completed);
        completedFlagTextView.setTextColor(Color.BLACK);
        TextView titleTextView =rowView.findViewById(R.id.cloud_item_title);
        titleTextView.setTextColor(Color.BLACK);
        //titleTextView.setVisibility(View.GONE);


        CloudItem currentCloudItem = cloudItemList.get(position);
        postDateTextView.setText(currentCloudItem.getPostDate());
        contentTextView.setText(currentCloudItem.getContent());
        if(currentCloudItem.getCompletedFlag().equals("1")) {
            completedFlagTextView.setText("Completed");
            completedFlagTextView.setTextColor(Color.GREEN);
        }
        else{
            completedFlagTextView.setText("Not Completed");
            completedFlagTextView.setTextColor(Color.RED);
        }
        titleTextView.setText(currentCloudItem.getListTitle());
        return rowView;
    }
}
