package com.example.jj.todoornot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ToDoLisItemDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "to_do_or_not.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TO_DO_LIST_ITEMS = "table_to_do_or_not_items";
    private static final String T_ITEMS_COLUMN_DATE = "date";
    private static final String T_ITEMS_COLUMN_DESCRIPTION = "description";
    private static final String T_ITEMS_COLUMN_COMPLETED_FLAG = "isCompleted";
    private static final String T_ITEMS_COLUMN_TITLE_ID = "titleID";

    private static final String TABLE_TO_DO_LIST_LIST = "table_to_do_or_not_list";
    private static final String T_LIST_COLUMN_LISTNAME = "listname";
    private static final String T_LIST_COLUMN_DATE = "date";


    public ToDoLisItemDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql_create_list_table ="CREATE TABLE " +TABLE_TO_DO_LIST_LIST
                + " (_id INTEGER PRIMARY KEY, "
                + T_LIST_COLUMN_LISTNAME + " TEXT, "
                +T_LIST_COLUMN_DATE + " TEXT)";

        db.execSQL(sql_create_list_table);

        String sql_create_items_table ="CREATE TABLE " + TABLE_TO_DO_LIST_ITEMS
                + " (_id INTEGER PRIMARY KEY, "
                + T_ITEMS_COLUMN_DESCRIPTION + " TEXT, "
                + T_ITEMS_COLUMN_DATE + " TEXT, "
                + T_ITEMS_COLUMN_COMPLETED_FLAG + " TEXT, "
                + T_ITEMS_COLUMN_TITLE_ID + " INTEGER, "
                + "FOREIGN KEY( " + T_ITEMS_COLUMN_TITLE_ID + " ) REFERENCES " + TABLE_TO_DO_LIST_LIST + " (_id));";
        db.execSQL(sql_create_items_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_TO_DO_LIST_ITEMS);
        db.execSQL("DROP TABLE " + TABLE_TO_DO_LIST_LIST);
        onCreate(db);
    }

    public long createNewTitileName(String titleName) {
        SQLiteDatabase db =getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(T_LIST_COLUMN_LISTNAME,titleName);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//set date format
        Date date = new Date();
        values.put(T_LIST_COLUMN_DATE, df.format(date));
        long a =  db.insert(TABLE_TO_DO_LIST_LIST,null,values);
        return  a;
    }

    public Cursor getTitleList() {
        SQLiteDatabase db = getReadableDatabase();
        String queryStatement = "SELECT _id, "
                + T_LIST_COLUMN_LISTNAME + ", "
                + T_LIST_COLUMN_DATE
                + " FROM " + TABLE_TO_DO_LIST_LIST
                + " ORDER BY " + T_LIST_COLUMN_DATE + " DESC";
        //execute the raw query
        return db.rawQuery(queryStatement,null);
    }

    public void deleteListTitle(long id) {
        SQLiteDatabase db = getWritableDatabase();
        String queryItemStatement = "Delete From " + TABLE_TO_DO_LIST_ITEMS + " WHERE titleID = " + id;
        String queryStatement = "Delete From " + TABLE_TO_DO_LIST_LIST + " WHERE _id = " + id;
        db.execSQL(queryItemStatement);
        db.execSQL(queryStatement);

    }

    public String getListTitleById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String queryStatement = "Select " + T_LIST_COLUMN_LISTNAME + " From " + TABLE_TO_DO_LIST_LIST + " WHERE _id = " + id;
        Cursor c =  db.rawQuery(queryStatement,null);
        return "";
    }

    public void updateListTitle(String toString, long id) {
        SQLiteDatabase db = getWritableDatabase();
        String queryStatement = "Update " + TABLE_TO_DO_LIST_LIST + "  set " + T_LIST_COLUMN_LISTNAME + " = '" + toString + "' WHERE _id = " + id;
        db.execSQL(queryStatement);
    }

    public long createNewItem(String itemDesp, long listTitleID) {
        SQLiteDatabase db =getWritableDatabase();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//set date format
        Date date = new Date();
        String queryStatement = "Insert Into "
                + TABLE_TO_DO_LIST_ITEMS
                + "  ("
                + T_ITEMS_COLUMN_DATE + ", "
                + T_ITEMS_COLUMN_DESCRIPTION + ", "
                + T_ITEMS_COLUMN_COMPLETED_FLAG + ", "
                + T_ITEMS_COLUMN_TITLE_ID + ") Values" + " ('"
                + df.format(date) + "', '"
                + itemDesp + "', '"
                + "Not Completed" + "', "
                + listTitleID + ") ";
        try {
            db.execSQL(queryStatement);
            return 1;
        }
        catch (Exception e){
            return  -1;
        }

    }

    public Cursor getTitleItems(long titleId) {
        SQLiteDatabase db = getReadableDatabase();
        String queryStatement = "SELECT _id, "
                + T_ITEMS_COLUMN_DATE + ", "
                + T_ITEMS_COLUMN_DESCRIPTION + ", "
                + T_ITEMS_COLUMN_COMPLETED_FLAG + ", "
                + T_ITEMS_COLUMN_TITLE_ID
                + " FROM " + TABLE_TO_DO_LIST_ITEMS
                + " Where " + T_ITEMS_COLUMN_TITLE_ID + " = " + titleId
                + " ORDER BY " + T_ITEMS_COLUMN_DATE + " DESC";
        //execute the raw query
        return db.rawQuery(queryStatement,null);
    }

    public void updateItem(String toString, long id) {
        SQLiteDatabase db = getWritableDatabase();
        String queryStatement = "Update " + TABLE_TO_DO_LIST_ITEMS + "  set " + T_ITEMS_COLUMN_DESCRIPTION + " = '" + toString + "' WHERE _id = " + id;
        db.execSQL(queryStatement);
    }

    public void deleteItemByID(long id) {
        SQLiteDatabase db = getWritableDatabase();
        String queryItemStatement = "Delete From " + TABLE_TO_DO_LIST_ITEMS + " WHERE _id = " + id;
//        db.rawQuery(queryItemStatement,null);
        db.execSQL(queryItemStatement);
    }

    public void updateItemCompletedFlagByID(long id) {
        SQLiteDatabase db = getWritableDatabase();
        String queryStatement = "Update " + TABLE_TO_DO_LIST_ITEMS + "  set " + T_ITEMS_COLUMN_COMPLETED_FLAG + " = 'Completed" + "' WHERE _id = " + id;
        db.execSQL(queryStatement);
    }

    public Cursor archiveTitle(long id) {
        SQLiteDatabase db = getReadableDatabase();
//        String queryStatement = "SELECT " + BaseColumns._ID + ", " + COLUMN_DESCRIPTION + ", " + COLUMN_AMOUNT + ", " + COLUMN_DATE
//                + " FROM " + TABLE_EXPENSE
//                + " WHERE " + BaseColumns._ID + " = ?";

        String queryState = "select table_to_do_or_not_items._id, listname, description, isCompleted" +
                " from table_to_do_or_not_items , table_to_do_or_not_list " +
                "where table_to_do_or_not_list._id = table_to_do_or_not_items.titleID " +
                "and  table_to_do_or_not_list._id = " + id;
        Cursor cursor = db.rawQuery(queryState, null);

        return cursor;
    }
}
