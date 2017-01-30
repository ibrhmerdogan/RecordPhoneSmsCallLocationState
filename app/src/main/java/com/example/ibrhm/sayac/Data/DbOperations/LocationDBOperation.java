package com.example.ibrhm.sayac.Data.DbOperations;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.example.ibrhm.sayac.Data.LocationDB;

/**
 * Created by ibrhm on 30.01.2017.
 */

public class LocationDBOperation {
    Context context;

    public void recordDisplay(LocationDB database, TextView tv) {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from '" + "informationDB" + "'", null);
        c.moveToFirst();
        StringBuilder bld = new StringBuilder("");
        do {
            bld.append(c.getString(0) + " " + c.getString(1) + " " + c.getInt(2) + " " + c.getInt(3) + "");

        } while (c.moveToNext());
        tv.setText(c.getCount() + "" + bld.toString());
    }

 /*public Cursor getRecord(LocationDB locationDB) {
     SQLiteDatabase dbb = locationDB.getReadableDatabase();
     Cursor cursor = dbb.query("informationDB", new String[]{"id", "date", "langitute", "longitute"}, null, null, null, null, null);
     return  cursor;
         }
  public StringBuilder diplayRecord(Cursor cursor){
     StringBuilder builder=new StringBuilder();
         while (cursor.moveToNext()) {

             long id = cursor.getLong(cursor.getColumnIndex("id"));
             String date = cursor.getString(cursor.getColumnIndex("date"));
             String langitute = cursor.getString((cursor.getColumnIndex("langitute")));
             String longitute = cursor.getString((cursor.getColumnIndex("longitute")));
             builder.append(date).append(" date: ");
             builder.append(langitute).append("langitute:");
             builder.append(longitute).append("\n");

         }

         return builder;
  }*/
}
