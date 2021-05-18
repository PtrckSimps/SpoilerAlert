package com.mobdeve.simpaop.spoileralert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "item.db";
    private static final String TABLE_NAME = "item";

    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ROWID = "_id";
    private static final String KEY_ITEMNAME = "itemname";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_EXPDATE = "expiry";
    private static final String KEY_PROOF = "proof";
    private static final String KEY_IMAGE = "image";
    private Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //Toast.makeText(context, "constructor called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("create table " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, itemname TEXT, category TEXT, quantity INTEGER, expiry TEXT, proof BLOB, image BLOB)");
            //Toast.makeText(context, "oncreate called", Toast.LENGTH_SHORT).show();
        }catch (SQLException e){
            Toast.makeText(context, ""+e , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }catch (SQLException e){
            Toast.makeText(context, ""+e , Toast.LENGTH_SHORT).show();
        }
    }

    public boolean insertItem(String name, String category, int quantity, String expiry, byte[] proof, byte[] itemimage){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ITEMNAME, name);
        contentValues.put(KEY_CATEGORY, category);
        contentValues.put(KEY_QUANTITY, quantity);
        contentValues.put(KEY_EXPDATE, expiry);
        contentValues.put(KEY_PROOF, proof);
        contentValues.put(KEY_IMAGE, itemimage);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getItems(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor= sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);
        return cursor;
    }

    public Cursor getSpecificItem(int rowID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME + " WHERE " + KEY_ROWID + "='" + rowID + "'", null);
        return cursor;
    }

    //implement delete
    public Integer deleteItem(String rowID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, KEY_ROWID + " = ?", new String[] {rowID});
    }

    //implement update/edit
    public boolean updateItem(String rowID, String name, String category, int quantity, String expiry, byte[] proof, byte[] itemimage){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ITEMNAME, name);
        contentValues.put(KEY_CATEGORY, category);
        contentValues.put(KEY_QUANTITY, quantity);
        contentValues.put(KEY_EXPDATE, expiry);
        contentValues.put(KEY_PROOF, proof);
        contentValues.put(KEY_IMAGE, itemimage);
        sqLiteDatabase.update(TABLE_NAME, contentValues, KEY_ROWID + " = ?", new String[] {rowID});
        return true;
    }
}
