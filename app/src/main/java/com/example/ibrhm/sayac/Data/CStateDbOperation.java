package com.example.ibrhm.sayac.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * Created by ibrhm on 24.01.2017.
 */

public class CStateDbOperation {
    LocationDB locDatabase;
    Context context;

    public void recordAdd(String langigute, String longitute) {
        SQLiteDatabase db = locDatabase.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("langitute", langigute);
        data.put("longitute", longitute);
        db.insertOrThrow("information", null, data);
    }

    public void KayitGoster(Cursor cursor) {
        StringBuilder builder = new StringBuilder();

        while (cursor.moveToNext()) {

            long id = cursor.getLong(cursor.getColumnIndex("id"));
            String ad = cursor.getString((cursor.getColumnIndex("langitute")));
            String soyad = cursor.getString((cursor.getColumnIndex("longitute")));
            builder.append(id).append(" Adı: ");
            builder.append(ad).append(" Soyadı: ");
            builder.append(soyad).append("\n");
            Toast.makeText(CStateDbOperation.this.context, "locat" + builder, Toast.LENGTH_LONG).show();
        }

    }

    public Cursor KayitGetir() {
        SQLiteDatabase db = locDatabase.getReadableDatabase();
        Cursor cursor = db.query("information", new String[]{"id", "langitute", "longitute"}, null, null, null, null, null);
        return cursor;
    }





}
