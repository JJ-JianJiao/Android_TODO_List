package com.example.jj.todoornot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ListItemActivity extends AppCompatActivity {

    private static final String TAG = "ListItemActivity";
    private static final String URL = "http://www.youcode.ca/";

    private Spinner listTitleSpinner;
    private ToDoLisItemDB mToDoListItemDB;
    private TextView listItemTitle;
    private ListView itemListView;
    private LinearLayout itemBtnsLayout;
    private RelativeLayout mainLayout;

    private Button itemDeleteBtn;
    private Button itemArchiveBtn;
    private Button itemDoneBtn;

    private TextView itemActivitySpinnerReminder;
    private TextView itemActivityListviewReminder;

    private TextView itemActivitySpinnerHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        listTitleSpinner = (Spinner)findViewById(R.id.listTitleSpinner);
        mToDoListItemDB = new ToDoLisItemDB(this);
        itemListView = (ListView) findViewById(R.id.item_listview);
        itemActivitySpinnerReminder = (TextView)findViewById(R.id.activity_list_item_reminder_spinner);
        itemActivityListviewReminder = (TextView)findViewById(R.id.activity_list_item_reminder_itemlistview);
        itemActivitySpinnerHeader = (TextView)findViewById(R.id.titleSpinnerHeaderTextView);
        mainLayout = (RelativeLayout)findViewById(R.id.activity_list_item_main_layout);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String mBackgroundTitleListview = sharedPreferences.getString("background_color_pref","#ffffff");
        itemListView.setBackgroundColor(Color.parseColor(mBackgroundTitleListview));
        itemActivityListviewReminder.setBackgroundColor(Color.parseColor(mBackgroundTitleListview));
        itemActivitySpinnerReminder.setBackgroundColor(Color.parseColor(mBackgroundTitleListview));
        mainLayout.setBackgroundColor(Color.parseColor(mBackgroundTitleListview));

        Intent intent = getIntent();
        int listNameID = intent.getIntExtra("listNamePosition",-1);

        bindListTitleToSpinner(listNameID);

        listItemTitle = (TextView) findViewById(R.id.activity_item_title);

        DrawableUtil mDrawableUtil = new DrawableUtil(listItemTitle, new DrawableUtil.OnDrawableListener() {
            @Override
            public void onRight(View v, Drawable right) {
                //Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
                if(listTitleSpinner.getSelectedItemPosition()!=-1) {
                    dialogEditText("Add Item", null, "add", -1);
                }else{
                    Toast.makeText(getApplicationContext(), R.string.failure_create_new_item, Toast.LENGTH_SHORT).show();
                }
            }
        });
        bindItemsView();

        listTitleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bindItemsView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),"you click the items",Toast.LENGTH_SHORT).show();
                if(itemDeleteBtn!=null){
                    itemDeleteBtn.setVisibility(View.GONE);
                }
                if(itemArchiveBtn!=null){
                    itemArchiveBtn.setVisibility(View.GONE);
                }
                if(itemDoneBtn!=null){
                    itemDoneBtn.setVisibility(View.GONE);
                }

                TextView textView = view.findViewById(R.id.item_description);
                String message = textView.getText().toString();
                dialogEditText("Edit Item",message,"update",id);
            }
        });

        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(itemDeleteBtn!=null){
                    itemDeleteBtn.setVisibility(View.GONE);
                }
                if(itemArchiveBtn!=null){
                    itemArchiveBtn.setVisibility(View.GONE);
                }
                if(itemDoneBtn!=null){
                    itemDoneBtn.setVisibility(View.GONE);
                }

                itemDeleteBtn = view.findViewById(R.id.item_delete_button);
                itemArchiveBtn = view.findViewById(R.id.item_archive_button);
                itemDoneBtn = view.findViewById(R.id.item_done_button);

                itemDeleteBtn.setVisibility(View.VISIBLE);
                itemArchiveBtn.setVisibility(View.VISIBLE);
                TextView textView = view.findViewById(R.id.item_completed);
                if(!textView.getText().equals("Completed")) {
                    itemDoneBtn.setVisibility(View.VISIBLE);
                }

                itemDeleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mToDoListItemDB.deleteItemByID(id);
                        bindItemsView();
                        Toast.makeText(getApplicationContext(), R.string.delete_item, Toast.LENGTH_SHORT).show();
                    }
                });

                itemArchiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor listTitleCursor = (Cursor) listTitleSpinner.getSelectedItem();

                        String listTitleStr = listTitleCursor.getString(listTitleCursor.getColumnIndex("listname"));

                        TextView contentTextView = view.findViewById(R.id.item_description);
                        String content = contentTextView.getText().toString();

                        String completedFlag;
                        TextView completedFlagTextView = view.findViewById(R.id.item_completed);
                        if(completedFlagTextView.getText().toString().equals("Completed")){
                            completedFlag= "1";
                        }else{
                            completedFlag = "0";
                        }

                        SharedPreferences prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        String userName = prefs.getString("username_pref","default");
                        String password = prefs.getString("password_pref","default");
//                        String userName = "JJ";
//                        String password = "123456";

//                        TextView dateTextView = view.findViewById(R.id.item_date);
//                        String dateStr = dateTextView.getText().toString();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//set date format
                        Date date = new Date();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(URL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .build();
                        YoucodeTodoListService service = retrofit.create(YoucodeTodoListService.class);
                        Call<String> postCall = service.postToDoItem(listTitleStr,content,completedFlag,userName,password,df.format(date));
                        postCall.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), R.string.post_item_success, Toast.LENGTH_SHORT).show();
                                    mToDoListItemDB.deleteItemByID(id);
                                    bindItemsView();
                                }else{
                                    Toast.makeText(getApplicationContext(), R.string.post_item_failure, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), R.string.post_item_failure, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                itemDoneBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        mToDoListItemDB.updateItemCompletedFlagByID(id);
                        bindItemsView();
                    }
                });

                return true;
            }
        });
    }

    private void bindListTitleToSpinner(int listNamePosition){
        Cursor dbCursor = mToDoListItemDB.getTitleList();
        if(dbCursor.getCount() > 0) {
//            reminderTextView.setVisibility(View.GONE);
            String[] fromFields = {"listname"};
            int[] toViews = new int[]{
                    R.id.spinner_list_title};

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.spinner_list_title, dbCursor, fromFields, toViews);
//            ListTitleCursorAdapter simpleCursorAdapter = new ListTitleCursorAdapter(this, R.layout.spinner_list_title, dbCursor, fromFields, toViews);
            //simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listTitleSpinner.setAdapter(simpleCursorAdapter);
            if(listNamePosition!=-1) {
                listTitleSpinner.setSelection(listNamePosition);
            }
        }else{
//            reminderTextView.setVisibility(View.VISIBLE);
//            String[] nullTitileMsg ={"You need add a Title first"};
//            ArrayAdapter<CharSequence> adapter =
//                    new ArrayAdapter(
//                            this,
//                            android.R.layout.simple_spinner_item,
//                            nullTitileMsg);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            listTitleSpinner.setAdapter(adapter);
            itemActivitySpinnerReminder.setVisibility(View.VISIBLE);
            listTitleSpinner.setVisibility(View.GONE);
            itemActivitySpinnerHeader.setVisibility(View.GONE);
            itemListView.setVisibility(View.GONE);

        }
    }

    private void dialogEditText(String titleStr, String message, String state, final long id) {
        final EditText editText = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, 0);
        if (message != null) {
            editText.setText(message);
        }
        builder.setTitle(titleStr);
//        builder.setIcon(R.mipmap.ic_launcher);
        builder.setIcon(R.drawable.warning);
        builder.setView(editText);
        if (state == "update") {
            builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onUpdateItem(editText.getText().toString(),id);
                    bindItemsView();
                    Toast.makeText(getApplicationContext(), R.string.update_selected_item, Toast.LENGTH_SHORT).show();
                }
            });
        } else if (state == "add") {
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
//                    listTitleSpinner.getSelectedItemPosition();
//                    listTitleSpinner.getSelectedItem().toString();
                    if(listTitleSpinner.getSelectedItemPosition()!=-1) {
                        long listTitleID = listTitleSpinner.getSelectedItemId();
                        onAddItem(editText.getText().toString(), listTitleID);
                        bindItemsView();
                        Toast.makeText(getApplicationContext(), R.string.create_new_item, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.failure_create_new_item, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void onUpdateItem(String toString, long id) {
        mToDoListItemDB.updateItem(toString,id);
    }

    private void bindItemsView(){
        long listTitleID = listTitleSpinner.getSelectedItemId();
        Cursor dbCursor = mToDoListItemDB.getTitleItems(listTitleID);
        if(dbCursor.getCount() > 0) {
            //reminderTextView.setVisibility(View.GONE);
            itemActivityListviewReminder.setVisibility(View.GONE);
            String[] fromFields = {"date","isCompleted","description"};
            int[] toViews = new int[]{
                    R.id.item_date,
                    R.id.item_completed,
                    R.id.item_description,

            };

//            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item_item_items, dbCursor, fromFields, toViews);
            ListItemCursorAdapter simpleCursorAdapter = new ListItemCursorAdapter(this, R.layout.list_item_item_items, dbCursor, fromFields, toViews);
            itemListView.setAdapter(simpleCursorAdapter);
        }else{
            //reminderTextView.setVisibility(View.VISIBLE);
            itemActivityListviewReminder.setVisibility(View.VISIBLE);
            itemListView.setAdapter(null);
        }
    }

    public void onAddItem(String titleName, long listTitleID){
        long primaryKeyId =mToDoListItemDB.createNewItem(titleName,listTitleID);
    }
}