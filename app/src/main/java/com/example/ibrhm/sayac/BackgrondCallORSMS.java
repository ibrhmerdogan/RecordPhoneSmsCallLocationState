package com.example.ibrhm.sayac;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public class BackgrondCallORSMS extends Service
{
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 1;


    Context context;

    Intent intent;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        context=this;
    }

    @Override
    public void onStart(Intent intent, int startId) {

    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
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

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }



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




    public void smsFunction() {
        Map<Integer, List<Sms>> smsMap = getAllSms();

        for (Map.Entry<Integer, List<Sms>> entry : smsMap.entrySet()) {
            Log.d("sms_sample", String.format("Month %d: %d sms", entry.getKey(), entry.getValue().size()));
            String mesaj = smsMap.toString();
            intent =new Intent("sms");
            intent.putExtra("Sms",mesaj);
            sendBroadcast(intent);

        }
    }
        public Map<Integer, List<Sms>> getAllSms() {
            Map<Integer, List<Sms>> smsMap = new TreeMap<Integer, List<Sms>>();
            Sms objSms = null;
            Uri message = Uri.parse("content://sms/");
            ContentResolver cr = getContentResolver();
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);

            Cursor c = cr.query(message, null, null, null, null);

            int totalSMS = c.getCount();

            if (c.moveToFirst()) {
                for (int i = 0; i < totalSMS; i++) {

                    objSms = new Sms();
                    objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                    objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")));
                    objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                    objSms.setReadState(c.getString(c.getColumnIndex("read")));
                    objSms.setTime(c.getLong(c.getColumnIndexOrThrow("date")));

                    if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                        objSms.setFolderName("inbox");
                    } else {
                        objSms.setFolderName("sent");
                    }

                    cal.setTimeInMillis(objSms.getTime());
                    int month = cal.get(Calendar.MONTH);

                    if (!smsMap.containsKey(month))
                        smsMap.put(month, new ArrayList<Sms>());

                    smsMap.get(month).add(objSms);

                    c.moveToNext();
                }
            }
            // else {
            // throw new RuntimeException("You have no SMS");
            // }
            c.close();

            return smsMap;
        }




}