package com.example.ibrhm.sayac.Data.DbOperations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.example.ibrhm.sayac.Data.PhoneStateDB;

import java.util.Date;

/**
 * Created by ibrhm on 31.01.2017.
 */

public class PhoneStateDBOperation {

    int count = 0;
    int trailer = 0, trailer1 = 0;
    public void recordState(String state, PhoneStateDB phoneStateDB) {
        SQLiteDatabase db = phoneStateDB.getReadableDatabase();
        ContentValues data = new ContentValues();
        Date date = new Date();
        data.put("date", date.toString());
        data.put("state", state);
        db.insertOrThrow("informationDB", null, data);

    }

    public void displayState(TextView textView, PhoneStateDB stateDB) {
        textView.setText("");
        SQLiteDatabase db = stateDB.getReadableDatabase();
        Cursor cursor = db.query("informationDB", new String[]{"id", "date", "state"}, null, null, null, null, null);


        StringBuilder builder = new StringBuilder();


        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String date = cursor.getString((cursor.getColumnIndex("date")));
            String state = cursor.getString((cursor.getColumnIndex("state")));
            builder.append(id).append("date: ");
            builder.append(date).append("\nstate:");
            builder.append(state).append("\n");
            textView.setText(builder);
        }
    }

    public void deleteRecord(PhoneStateDB phoneStateDB) {
        SQLiteDatabase db = phoneStateDB.getReadableDatabase();
        Cursor cursor = db.query("informationDB", new String[]{"id", "date", "state"}, null, null, null, null, null);


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

