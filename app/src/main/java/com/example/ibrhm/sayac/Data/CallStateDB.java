package com.example.ibrhm.sayac.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ibrhm on 27.01.2017.
 */

public class CallStateDB extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "callDBDBDB";
    // Contacts table name
    private static final String TABLE_COUNTRIES = "informationDB";

    public CallStateDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE informationDB (pID INTEGER PRIMARY KEY ,phoneNumber TEXT,callDate TEXT,callDuration TEXT,type TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
        onCreate(db);
    }

}
