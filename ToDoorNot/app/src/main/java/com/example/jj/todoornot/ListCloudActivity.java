package com.example.jj.todoornot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ListCloudActivity extends AppCompatActivity {

    private static final String TAG = "ListCloudActivity";
    private static final String URL = "http://www.youcode.ca/";

    private ListView cloudItemListView;
    private TextView cloudListViewReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cloud);

        cloudItemListView = (ListView) findViewById(R.id.cloud_items_listview);
        cloudListViewReminder = (TextView) findViewById(R.id.activity_cloud_item_list_reminder);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String mBackgroundTitleListview = sharedPreferences.getString("background_color_pref","#ffffff");
        cloudItemListView.setBackgroundColor(Color.parseColor(mBackgroundTitleListview));
        cloudListViewReminder.setBackgroundColor(Color.parseColor(mBackgroundTitleListview));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        YoucodeTodoListService service = retrofit.create(YoucodeTodoListService.class);
        SharedPreferences prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userName = prefs.getString("username_pref","default");
        String password = prefs.getString("password_pref","default");
        Call<String> getCall = service.itemListJSONServlet(userName, password);
        getCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String respondBodyStr = response.body();
                    //Toast.makeText(getApplicationContext(), "success_get_data", Toast.LENGTH_SHORT).show();
                    if(respondBodyStr.equals("")){
                        cloudListViewReminder.setVisibility(View.VISIBLE);
                    }else {
                        List<CloudItem> cloudItemList = new ArrayList<>();
                        String[] cloudItemArray = respondBodyStr.split("\n");
                        int countItem = cloudItemArray.length / 4;
                        if (countItem != 0 && cloudItemArray.length % 4 == 0) {
                            for (int i = 1; i <= countItem; i++) {
                                CloudItem currentCloudItem = new CloudItem();
                                currentCloudItem.setPostDate(cloudItemArray[(i - 1) * 4]);
                                currentCloudItem.setListTitle(cloudItemArray[(i - 1) * 4 + 1]);
                                currentCloudItem.setContent(cloudItemArray[(i - 1) * 4 + 2]);
                                currentCloudItem.setCompletedFlag(cloudItemArray[(i - 1) * 4 + 3]);
                                cloudItemList.add(currentCloudItem);
                            }
                            ListView cloudItemListView = findViewById(R.id.cloud_items_listview);
                            CloudItemListViewAdapter cloudItemListViewAdapter = new CloudItemListViewAdapter(getApplicationContext(), cloudItemList);
                            cloudItemListView.setAdapter(cloudItemListViewAdapter);
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "failure_fetch_data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "error_call_service_fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}