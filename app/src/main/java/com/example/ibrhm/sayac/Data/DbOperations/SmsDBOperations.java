package com.example.ibrhm.sayac.Data.DbOperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ibrhm.sayac.Data.SmsStateDB;

/**
 * Created by ibrhm on 30.01.2017.
 */


public class SmsDBOperations {
    SmsStateDB databases;
    Context context;

    public void recordAdd(String langigute, String longitute) {
        SQLiteDatabase db = databases.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("smsID", langigute);
        data.put("type", longitute);
        db.insertOrThrow("informationDB", null, data);
    }


    public StringBuilder KayitGoster(SmsStateDB database) {
        database = new SmsStateDB(this.context);
        SQLiteDatabase db = database.getReadableDatabase();
        StringBuilder builder = new StringBuilder();
        Cursor cursor = db.query("informationDB", new String[]{"id", "smsID", "type"}, null, null, null, null, null);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String smsID = cursor.getString((cursor.getColumnIndex("smsID")));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            builder.append(smsID).append("smsID:");
            builder.append(type).append("type:\n");

        }
        return builder;
    }


}
