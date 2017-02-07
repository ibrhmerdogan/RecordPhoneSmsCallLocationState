package com.example.ibrhm.sayac.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.ibrhm.sayac.Data.CallStateDB;
import com.example.ibrhm.sayac.Data.DbOperations.CallDBOperations;
import com.example.ibrhm.sayac.variable.CallStateVeriable;

import java.util.Date;

/**
 * Created by ibrhm on 24.01.2017.
 */

@SuppressWarnings("deprecation")
public class CallStateService extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    Context context;
    Intent intent;
    CallStateDB database = new CallStateDB(CallStateService.this);
    CallStateVeriable callObj = new CallStateVeriable();
    CallDBOperations operations = new CallDBOperations();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        context = this;
        //Call();
    }

    @Override
    public void onStart(Intent intent, int startId) {

        call();
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }



    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        //noinspection MissingPermission

    }

    static class C01511 extends Thread {
        final /* synthetic */ Runnable val$runnable;

        C01511(Runnable runnable) {
            this.val$runnable = runnable;
        }

        public void run() {
            this.val$runnable.run();
        }
    }

    public static Thread performOnBackgroundThread(Runnable runnable) {
        Thread t = new CallStateService.C01511(runnable);
        t.start();
        return t;
    }

    public void call() {

        try (Cursor cursor1 = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null)) {

            int id = cursor1.getColumnIndex(CallLog.Calls._ID);
            int number = cursor1.getColumnIndex(CallLog.Calls.NUMBER);
            int date = cursor1.getColumnIndex(CallLog.Calls.DATE);
            int duration = cursor1.getColumnIndex(CallLog.Calls.DURATION);
            int type = cursor1.getColumnIndex(CallLog.Calls.TYPE);
            StringBuilder sb = new StringBuilder();

            while (cursor1.moveToNext()) {

                callObj.set_id(cursor1.getInt(id));
                callObj.setpNumber(cursor1.getString(number));
                callObj.setCallDuration(cursor1.getString(duration));
                callObj.setCallType(cursor1.getString(type));
                String calldate = cursor1.getString(date);

                Date date1 = new Date(Long.valueOf(calldate));
                callObj.setCallDate(date1);
                String callTypeStr = "";
                switch (Integer.parseInt(callObj.getCallType())) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        callTypeStr = "OutgoingCall";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        callTypeStr = "IncomingCall";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callTypeStr = "missedCall";
                        break;
                }
                callObj.setCallType(callTypeStr);
                try {
                    Cursor cursor2 = null;
                    try {
                        SQLiteDatabase db1 = database.getReadableDatabase();

                        cursor2 = db1.query("informationDB", new String[]{"pID"}, "pID=" + callObj.get_id(), null, null, null, null);
                        cursor2.moveToFirst();

                    } catch (java.lang.IllegalArgumentException e) {
                        Toast.makeText(context, "CallStateService Read Database ERROR:" + e, Toast.LENGTH_LONG).show();
                    }

                    if (cursor2.getCount() == 0) {

                        // Toast.makeText(context, "not same call id", Toast.LENGTH_LONG).show();
                        try {

                            operations.recordAdd(callObj.get_id(), callObj.getpNumber(), callObj.getCallDate(), callObj.getCallDuration(), callObj.getCallType(), database);
                            operations.deleteRecord(database);
                        } catch (Exception e) {
                            Toast.makeText(context, "CallStateService Record ERROR:" + e, Toast.LENGTH_LONG).show();
                        }


                    } else {
                        cursor2 = null;
                        // Toast.makeText(context, "same call id", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(context, "CallStateService ERROR" + e, Toast.LENGTH_LONG).show();
                }
                sb.append("pID:" + callObj.get_id());
                sb.append("\nPhoneNumber:" + callObj.getpNumber());
                sb.append("\nCallDate:" + callObj.getCallDate());
                sb.append("\nCallDuration:" + callObj.getCallDuration());
                sb.append("\nCallType:" + callObj.getCallType());

                //  sb.append(System.getProperty("line.seperator"));

                //  Toast.makeText(context, "call" + sb, Toast.LENGTH_LONG).show();

            }
        }



    }

    @Deprecated
    public final Cursor managedQuery(Uri uri, String[] projection, String selection,
                                     String[] selectionArgs, String sortOrder) {
        Activity acv=new Activity();
        Cursor c = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (c != null) {
              acv.startManagingCursor(c);
        }
        return c;
    }

}