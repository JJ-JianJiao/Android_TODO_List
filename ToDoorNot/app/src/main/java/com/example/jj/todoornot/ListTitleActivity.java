package com.example.jj.todoornot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ListTitleActivity extends AppCompatActivity{


    private static final String TAG = "ListTitleActivity";
    private static final String URL = "http://www.youcode.ca/";
    private ToDoLisItemDB mToDoListItemDB;
    private ListView titlelistView;
    private TextView titleTextView;

    private TextView reminderTextView;

    private Button deleteBtn;
    private Button archiveBtn;
    private Button viewBtn;

//    private SideSlipListView mSlideSlipListView;

//    private SwipeMenuListView titleSwipeListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_title);

        titleTextView = findViewById(R.id.activity_title_title);
        titlelistView = findViewById(R.id.activity_title_listview);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String mBackgroundTitleListview = sharedPreferences.getString("background_color_pref","#ffffff");
        titlelistView.setBackgroundColor(Color.parseColor(mBackgroundTitleListview));

//        titleSwipeListView = (SwipeMenuListView)findViewById(R.id.activity_title_listview);

        reminderTextView = findViewById(R.id.activity_list_title_reminder);
        reminderTextView.setBackgroundColor(Color.parseColor(mBackgroundTitleListview));
        DrawableUtil drawableUtil = new DrawableUtil(titleTextView, new DrawableUtil.OnDrawableListener() {
            @Override
            public void onRight(View v, Drawable right) {
                //Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
                dialogEditText("Add Title",null,"add", -1);
            }
        });

        mToDoListItemDB = new ToDoLisItemDB(this);
        bindListTitleView();





        titlelistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(deleteBtn!=null){
                    deleteBtn.setVisibility(View.GONE);
                }
                if(archiveBtn!=null){
                    archiveBtn.setVisibility(View.GONE);
                }
                if(viewBtn!=null){
                    viewBtn.setVisibility(View.GONE);
                }

                deleteBtn = (Button)view.findViewById(R.id.delete_button);
                deleteBtn.setVisibility(View.VISIBLE);

                archiveBtn = (Button)view.findViewById(R.id.archive_button);
                archiveBtn.setVisibility(View.VISIBLE);

                viewBtn = (Button)view.findViewById(R.id.view_button);
                viewBtn.setVisibility(View.VISIBLE);

//                deleteBtn.setOnClickListener((View aview) ->
//                        Toast.makeText(getApplicationContext(), "deleteBtn clicked!", Toast.LENGTH_SHORT).show()
//
//                );
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getApplicationContext(), "deleteBtn clicked!", Toast.LENGTH_SHORT).show();
//                        mToDoListItemDB.deleteListTitle(id);
//                        bindListTitleView();
                        alertDialog("deleteTitle","This action will delete all items in this title",id);

                    }
                });

//                archiveBtn.setOnClickListener((View aview) ->
//                        Toast.makeText(getApplicationContext(), "archiveBtn clicked!", Toast.LENGTH_SHORT).show()
//                );

                archiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getApplicationContext(), "archiveBtn clicked!", Toast.LENGTH_SHORT).show();
                        alertDialog("archiveItems","This action will archive all items in this title",id);

                    }
                });

//                viewBtn.setOnClickListener((View aview) ->
//                        Toast.makeText(getApplicationContext(), "viewBtn clicked!", Toast.LENGTH_SHORT).show()
//                );
                viewBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getApplicationContext(), "viewBtn clicked!", Toast.LENGTH_SHORT).show();
                        Intent itemsActivityIntent =new Intent(getApplicationContext(),ListItemActivity.class);
                        itemsActivityIntent.putExtra("listNamePosition",position);
                        startActivity(itemsActivityIntent);
                    }
                });

//                mToDoListItemDB.deleteListTitle(id);
//                bindListTitleView();
                return true;
            }
        });

        titlelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent itemsActivityIntent =new Intent(getApplicationContext(),ListItemActivity.class);
////                startActivity(itemsActivityIntent);
////                Toast.makeText(getApplicationContext(),"you click the items",Toast.LENGTH_SHORT).show();
////                Log.i(TAG,"open items Activity");
  //              String message =((TextView)view).getText().toString();
//                String message = mToDoListItemDB.getListTitleById(id);
                if(deleteBtn!=null){
                    deleteBtn.setVisibility(View.GONE);
                }
                if(archiveBtn!=null){
                    archiveBtn.setVisibility(View.GONE);
                }
                if(viewBtn!=null){
                    viewBtn.setVisibility(View.GONE);
                }
                TextView textView = view.findViewById(R.id.list_title_listname);
                String message = textView.getText().toString();
                dialogEditText("Edit Title",message,"update",id);
            }
        });
    }

    private void bindListTitleView(){
        Cursor dbCursor = mToDoListItemDB.getTitleList();
        if(dbCursor.getCount() > 0) {
            reminderTextView.setVisibility(View.GONE);
            String[] fromFields = {"listname"};
            int[] toViews = new int[]{
                    R.id.list_title_listname};

//            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_title_title_item, dbCursor, fromFields, toViews);
            ListTitleCursorAdapter   simpleCursorAdapter = new ListTitleCursorAdapter(this, R.layout.list_title_title_item, dbCursor, fromFields, toViews);
            titlelistView.setAdapter(simpleCursorAdapter);
        }else{
            reminderTextView.setVisibility(View.VISIBLE);
            titlelistView.setAdapter(null);
        }
    }


    private void dialogEditText(String titleStr, String message, String state, final long id){
        final EditText editText = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,0);
        if(message!=null){
            editText.setText(message);
        }
        builder.setTitle(titleStr);
//        builder.setIcon(R.mipmap.ic_launcher);
        builder.setIcon(R.drawable.edit);
        builder.setView(editText);
        if (state == "update") {
            builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
                    onUpdateListTitle(editText.getText().toString(),id);
                    bindListTitleView();
                    Toast.makeText(getApplicationContext(),R.string.create_new_title,Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (state == "add") {
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
                    onAddListTitle(editText.getText().toString());
                    bindListTitleView();
                    Toast.makeText(getApplicationContext(),R.string.create_new_title,Toast.LENGTH_SHORT).show();
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

    private void onUpdateListTitle(String toString,long id) {
        mToDoListItemDB.updateListTitle(toString,id);
    }

    private void onAddListTitle(String titleName){
        long primaryKeyId =mToDoListItemDB.createNewTitileName(titleName);
    }

    private void alertDialog(String actionType, String prompt, long id){
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:

                        if(actionType.equals("deleteTitle")) {
                            mToDoListItemDB.deleteListTitle(id);
                        }
                        else if(actionType.equals("archiveItems")){
                            archiveItemsByTitleID(id);
                        }
                        bindListTitleView();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
//        builder.setIcon(R.mipmap.ic_launcher);
        builder.setIcon(R.drawable.warning);
        builder.setTitle("Warning");
        builder.setMessage("This action will delete all items in this title");
        builder.setPositiveButton("Sure", dialogOnclicListener);
        builder.setNegativeButton("Cancel", dialogOnclicListener);
        builder.create().show();

    }

    private void archiveItemsByTitleID(long id){
        Cursor itemsCursor = mToDoListItemDB.archiveTitle(id);

        if(itemsCursor==null || itemsCursor.getCount()<1){
            Toast.makeText(getApplicationContext(), "No Item in this Title", Toast.LENGTH_SHORT).show();
        }else{
            List<ArchiveTitleItems> archiveItems = new LinkedList<ArchiveTitleItems>();

            itemsCursor.moveToFirst();
            do{
                ArchiveTitleItems archiveTitleItem = new ArchiveTitleItems();
                String itemId =itemsCursor.getString(0);
                String itemListname =itemsCursor.getString(1);
                String itemDescription =itemsCursor.getString(2);
                String itemIsCompeted =itemsCursor.getString(3);
                archiveTitleItem.setItemId(itemId);
                archiveTitleItem.setTitleName(itemListname);
                archiveTitleItem.setItemDescription(itemDescription);
                archiveTitleItem.setCompletedFlag(itemIsCompeted);
                archiveItems.add(archiveTitleItem);
            }while(itemsCursor.moveToNext());

            SharedPreferences prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String userName = prefs.getString("username_pref","default");
            String password = prefs.getString("password_pref","default");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//set date format
            String date = df.format(new Date());

            for (ArchiveTitleItems currentArchiveTitleItem :
                    archiveItems) {
                PostItemToServer(currentArchiveTitleItem.getTitleName(),currentArchiveTitleItem.getItemDescription(),currentArchiveTitleItem.getCompletedFlag()
                                    ,userName,password,date,Long.valueOf(currentArchiveTitleItem.getItemId()));
            }

            Toast.makeText(getApplicationContext(), R.string.post_title_all_items, Toast.LENGTH_SHORT).show();
        }
    }

    private void PostItemToServer(String listTitleStr, String descripiton, String completedFlag, String userName, String password, String date, long itemID){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        YoucodeTodoListService service = retrofit.create(YoucodeTodoListService.class);
        Call<String> postCall = service.postToDoItem(listTitleStr,descripiton,completedFlag,userName,password,date);
        postCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    //Toast.makeText(getApplicationContext(), R.string.post_item_success, Toast.LENGTH_SHORT).show();
                    mToDoListItemDB.deleteItemByID(itemID);
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

}