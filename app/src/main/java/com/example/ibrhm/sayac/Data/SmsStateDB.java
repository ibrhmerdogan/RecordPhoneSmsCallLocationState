package com.example.ibrhm.sayac.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ibrhm on 25.01.2017.
 */

public class SmsStateDB extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "SMSInf";
    // Contacts table name
    private static final String TABLE_COUNTRIES = "informationDB";

    public SmsStateDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE informationDB (smsID  INTEGER PRIMARY KEY,address TEXT,body TEXT,readState TEXT,date TEXT,type TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
        onCreate(db);
    }
}
