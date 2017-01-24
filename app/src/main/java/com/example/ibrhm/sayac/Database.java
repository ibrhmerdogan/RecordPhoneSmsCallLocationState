package com.example.ibrhm.sayac;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ibrhm on 24.01.2017.
 */

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME   = "CallStateDB";
    // Contacts table name
    private static final String TABLE_COUNTRIES = "information";
    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_COUNTRIES + "(id INTEGER PRIMARY KEY,pNumber TEXT,callDate REAL,callDuration TEXT,callType TEXT" + ")";
        Log.d("DBHelper", "SQL : " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
        onCreate(db);
    }
}
