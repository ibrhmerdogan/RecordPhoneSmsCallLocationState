package com.example.ibrhm.sayac;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ibrhm on 25.01.2017.
 */

public class SmsDatabase extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "SMSDATt";
    // Contacts table name
    private static final String TABLE_COUNTRIES = "informationDB";

    public SmsDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE informationDB (id INTEGER PRIMARY KEY AUTOINCREMENT,smsID TEXT,type TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
        onCreate(db);
    }
}
