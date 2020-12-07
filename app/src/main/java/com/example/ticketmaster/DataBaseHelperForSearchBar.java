package com.example.ticketmaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelperForSearchBar extends SQLiteOpenHelper {

    //Intitialize varriable for database
    public static final String DATABASE_NAME = "searchlist.db";
    public static final String TABLE_NAME = "city";
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";

    public DataBaseHelperForSearchBar (Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //This method is used for creating table in database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "NAME TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //this method is used for insert data in table
    public boolean addSearchData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    //this method is used for retrive all data
    public Cursor getSearchListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

}
