package com.example.ibrhm.sayac;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by ibrhm on 24.01.2017.
 */

public class CallStateService extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 1;
    private  CStateDbOperation operation;
    Context context;
    Intent intent;
    int counter = 0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        context=this;
    }

    @Override
    public void onStart(Intent intent, int startId) {

      Call();
    }

   /* protected boolean isBetterLocation(Call sms, Call currentBestSms) {
        if (currentBestSms == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = sms.getTime() - currentBestSms.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }
        return true;
    }
*/

    /** Checks whether two providers are the same */
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

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    public void Call(){



        try ( Cursor cursor1 = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null)) {
            int number = cursor1.getColumnIndex(CallLog.Calls.NUMBER);
            int date = cursor1.getColumnIndex(CallLog.Calls.DATE);
            int duration = cursor1.getColumnIndex(CallLog.Calls.DURATION);
            int type = cursor1.getColumnIndex(CallLog.Calls.TYPE);
            StringBuilder sb = new StringBuilder();

            while (cursor1.moveToNext()) {

                String pnumber = cursor1.getString(number);
                String callduration = cursor1.getString(duration);
                String calltype = cursor1.getString(type);
                String calldate = cursor1.getString(date);

                Date date1 = new Date(Long.valueOf(calldate));


                String callTypeStr = "";
                switch (Integer.parseInt(calltype)) {
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
                sb.append("PhoneNumber:" + pnumber);
                sb.append("CallDate:" + date1);
                sb.append("CallDuration:" + callduration);
                sb.append("CallType:" + callTypeStr);
                sb.append("****************");
              //  sb.append(System.getProperty("line.seperator"));
                try{
                 operation.recordAdd(pnumber,date1,callduration,callTypeStr);
                }catch (Exception e) {
                    Toast.makeText(context, "call" + sb, Toast.LENGTH_LONG).show();
                }
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