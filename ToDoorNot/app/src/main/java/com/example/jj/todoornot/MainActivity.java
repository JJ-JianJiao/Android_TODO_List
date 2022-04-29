package com.example.jj.todoornot;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickTitle(View view) {
        Intent titleActivityIntent =new Intent(this,ListTitleActivity.class);
        startActivity(titleActivityIntent);
        //Toast.makeText(this,"you click the title",Toast.LENGTH_SHORT).show();
        Log.i(TAG,"open title Activity");
    }

    public void onClickItems(View view) {
        Intent itemsActivityIntent =new Intent(this,ListItemActivity.class);
        startActivity(itemsActivityIntent);
        //Toast.makeText(this,"you click the items",Toast.LENGTH_SHORT).show();
        Log.i(TAG,"open items Activity");
    }

    public void onClickCloud(View view) {
        Intent cloudActivityIntent =new Intent(this,ListCloudActivity.class);
        startActivity(cloudActivityIntent);
        //Toast.makeText(this,"you click the cloud",Toast.LENGTH_SHORT).show();
        Log.i(TAG,"open cloud Activity");
    }

    public void onClickSetting(View view) {
        Intent settingActivityIntent =new Intent(this,SettingActivity.class);
        startActivity(settingActivityIntent);
        //Toast.makeText(this,"you click the cloud",Toast.LENGTH_SHORT).show();
        Log.i(TAG,"open cloud Activity");
    }

}