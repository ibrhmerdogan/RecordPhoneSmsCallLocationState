package com.example.ibrhm.sayac.Data.DbOperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.example.ibrhm.sayac.Data.CallStateDB;

import java.util.Date;

/**
 * Created by ibrhm on 24.01.2017.
 */

public class CallDBOperations {

    Context context;
    CallStateDB database = new CallStateDB(this.context);

    public void recordAdd(int id, String pNumber, Date callDate, String callDuration, String callType, CallStateDB database) {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("pID", id);
        data.put("phoneNumber", pNumber);
        data.put("callDate", String.valueOf(callDate));
        data.put("callDuration", callDuration);
        data.put("type", callType);
        db.insertOrThrow("informationDB", null, data);
    }

    public void display(TextView textView, CallStateDB database) {
        textView.setText("");
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query("informationDB", new String[]{"pID", "phoneNumber", "callDate", "callDuration", "type"}, null, null, null, null, null);


        StringBuilder builder = new StringBuilder();


        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("pID"));
            String number = cursor.getString((cursor.getColumnIndex("phoneNumber")));
            String callDuration = cursor.getString((cursor.getColumnIndex("callDuration")));
            builder.append(id).append("number: ");
            builder.append(number).append("\nduration:");
            builder.append(callDuration).append("\n");
            textView.setText(builder);
        }
    }
}
