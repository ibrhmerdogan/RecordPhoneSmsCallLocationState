package com.example.ibrhm.sayac.Data.DbOperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ibrhm.sayac.Data.CallStateDB;

/**
 * Created by ibrhm on 24.01.2017.
 */

public class CallDBOperations {
    CallStateDB database;
    Context context;

    public void recordAdd(String langigute, String longitute) {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("langitute", langigute);
        data.put("longitute", longitute);
        db.insertOrThrow("information", null, data);
    }

    public Cursor KayitGetir() {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query("informationDB", new String[]{"pID", "phoneNumber"}, null, null, null, null, null);
        return cursor;
    }

    public StringBuilder KayitGoster(Cursor cursor) {
        StringBuilder builder = new StringBuilder();


        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("pID"));
            String ad = cursor.getString((cursor.getColumnIndex("phoneNumber")));
            builder.append(id).append(" Adı: ");
            builder.append(ad).append(" Soyadı: \n");

        }
        return builder;
    }


}
