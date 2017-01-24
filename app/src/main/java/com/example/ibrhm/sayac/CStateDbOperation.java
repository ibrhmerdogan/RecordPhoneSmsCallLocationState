package com.example.ibrhm.sayac;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by ibrhm on 24.01.2017.
 */

public class CStateDbOperation {
    Database database;
    public void recordAdd(String pNumber, Date callDate, String callDuration, String callType){
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("pNumber", pNumber);
        data.put("callDate", String.valueOf(callDate));
        data.put("callDuration",callDuration);
        data.put("callType",callType);
        db.insertOrThrow("information", null, data);
    }


}
