package com.example.ibrhm.sayac.Data.DbOperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.example.ibrhm.sayac.Data.LocationDB;

import java.util.Date;

/**
 * Created by ibrhm on 30.01.2017.
 */

public class LocationDBOperation {
    Context context;
    int count = 0;
    int trailer = 0;
    public void recordLocation(Double langitute, Double longitute, LocationDB locationDB) {
        SQLiteDatabase db = locationDB.getReadableDatabase();
        ContentValues data = new ContentValues();
        Date date = new Date();
        data.put("date", date.toString());
        data.put("langitute", langitute);
        data.put("longitute", longitute);
        db.insertOrThrow("informationDB", null, data);

    }

    public void displayLoc(TextView textView, LocationDB locationDB) {
        textView.setText("");
        SQLiteDatabase db = locationDB.getReadableDatabase();
        Cursor cursor = db.query("informationDB", new String[]{"id", "date", "langitute", "longitute"}, null, null, null, null, null);


        StringBuilder builder = new StringBuilder();


        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            Double langitute = cursor.getDouble(cursor.getColumnIndex("langitute"));
            Double longitute = cursor.getDouble(cursor.getColumnIndex("longitute"));
            String date = cursor.getString((cursor.getColumnIndex("date")));
            builder.append(id).append("langitute: ");
            builder.append(langitute).append("\nlongitute: ");
            builder.append(longitute).append("\ndate: ");
            builder.append(date).append("\n");
            textView.setText(builder);
        }
    }

    public void deleteRecord(LocationDB locationDB) {
        SQLiteDatabase db = locationDB.getReadableDatabase();
        Cursor cursor = db.query("informationDB", new String[]{"id", "langitute", "longitute", "date"}, null, null, null, null, null);


        StringBuilder builder = new StringBuilder();
        count = cursor.getCount();
        trailer = count / 40;
        while (cursor.moveToNext() && trailer > 0) {
            trailer--;
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            db.delete("informationDB", "id=" + id, null);

        }

    }

}
