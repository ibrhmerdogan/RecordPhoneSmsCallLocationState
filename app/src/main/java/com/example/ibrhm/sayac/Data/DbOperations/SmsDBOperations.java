package com.example.ibrhm.sayac.Data.DbOperations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.example.ibrhm.sayac.Data.SmsStateDB;

/**
 * Created by ibrhm on 30.01.2017.
 */


public class SmsDBOperations {

  int count = 0;
  int trailer = 0;
  public void recordAdd(int id, String address, String body, String state, String date, String type, SmsStateDB smsDatabase) {
    SQLiteDatabase db = smsDatabase.getWritableDatabase();
    ContentValues data = new ContentValues();
    data.put("smsID", id);
    data.put("address", address);
    data.put("body", body);
    data.put("readState", state);
    data.put("date", date);
    data.put("type", type);
    db.insertOrThrow("informationDB", null, data);
  }


  public void displaySms(TextView textView, SmsStateDB smsStateDB) {
    SQLiteDatabase db = smsStateDB.getReadableDatabase();
    Cursor cursor = db.query("informationDB", new String[]{"smsID", "address", "readState", "type"}, null, null, null, null, null);


    StringBuilder builder = new StringBuilder();


    while (cursor.moveToNext()) {

      int id = cursor.getInt(cursor.getColumnIndex("smsID"));
      String address = cursor.getString((cursor.getColumnIndex("address")));
      String readState = cursor.getString((cursor.getColumnIndex("readState")));
      String type = cursor.getString((cursor.getColumnIndex("type")));
      builder.append(id).append("address:");
      builder.append(address).append(" \nreadState:");
      builder.append(readState).append(" \ntype:");
      builder.append(type).append(" \n");

    }
    textView.setText(builder);
  }

  public void deleteRecord(SmsStateDB smsStateDB) {
    SQLiteDatabase db = smsStateDB.getReadableDatabase();
    Cursor cursor = db.query("informationDB", new String[]{"smsID", "address", "readState", "type"}, null, null, null, null, null);


    StringBuilder builder = new StringBuilder();
    count = cursor.getCount();
    trailer = count / 40;
    while (cursor.moveToNext() && trailer > 0) {
      trailer--;
      int id = cursor.getInt(cursor.getColumnIndex("smsID"));
      db.delete("informationDB", "smsID=" + id, null);

    }

  }
}