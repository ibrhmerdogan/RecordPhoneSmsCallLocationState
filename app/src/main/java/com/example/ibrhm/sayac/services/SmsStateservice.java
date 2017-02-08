package com.example.ibrhm.sayac.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.ibrhm.sayac.Data.DbOperations.SmsDBOperations;
import com.example.ibrhm.sayac.Data.SmsStateDB;
import com.example.ibrhm.sayac.variable.SmsVeriable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public class SmsStateservice extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    String adres;
    String type;
    Context context;
    SmsStateDB smsDatabase;
    Intent intent;
    SmsDBOperations operations = new SmsDBOperations();

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        context=this;


    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
        //Toast.makeText(getApplicationContext(),"he",Toast.LENGTH_LONG).show();
        smsFunction();
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

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
        Map<Integer, List<SmsVeriable>> smsMap = getAllSms();

        for (Map.Entry<Integer, List<SmsVeriable>> entry : smsMap.entrySet()) {
            try {

            Log.d("sms_sample", String.format("Month %d: %d sms", entry.getKey(), entry.getValue().size()));
            String mesaj = smsMap.toString();
                //Toast.makeText(context,"mesajlar"+mesaj,Toast.LENGTH_LONG).show();
            intent =new Intent("sms");
                intent.putExtra("SmsVeriable", mesaj);
            sendBroadcast(intent);
            } catch (Exception exception) {
            }
        }
    }

    public Map<Integer, List<SmsVeriable>> getAllSms() {
        Map<Integer, List<SmsVeriable>> smsMap = new TreeMap<Integer, List<SmsVeriable>>();
        SmsVeriable objSms = null;
            Uri message = Uri.parse("content://sms/");
        ContentResolver cr = getContentResolver();
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);

            Cursor c = cr.query(message, null, null, null, null);

            int totalSMS = c.getCount();

            if (c.moveToFirst()) {
                for (int i = 0; i < totalSMS; i++) {

                    objSms = new SmsVeriable();
                    objSms.setId(c.getInt(c.getColumnIndexOrThrow("_id")));
                    objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")));
                    objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                    objSms.setReadState(c.getString(c.getColumnIndex("read")));
                    objSms.setTime(c.getLong(c.getColumnIndexOrThrow("date")));
                    if (c.getString(c.getColumnIndex("read")).contains("1")) {
                        objSms.setReadState("Read");
                    } else {
                        objSms.setReadState("NonRead");
                    }
                    if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                        objSms.setFolderName("inbox");
                    } else {
                        objSms.setFolderName("sent");
                    }

                    smsDatabase = new SmsStateDB(SmsStateservice.this);
                    try {

                        Cursor cursor2 = null;

                        try {
                            SQLiteDatabase db1 = smsDatabase.getReadableDatabase();

                            cursor2 = db1.query("informationDB", new String[]{"smsID"}, "smsID=" + objSms.getId(), null, null, null, null);
                            cursor2.moveToFirst();

                        } catch (java.lang.IllegalArgumentException e) {
                            Toast.makeText(context, "SmsStateService read database ERROR:" + e, Toast.LENGTH_LONG).show();
                        }


                        if (cursor2.getCount() == 0) {

                            //  Toast.makeText(context, "not same sms ID", Toast.LENGTH_LONG).show();
                            try {
                                operations.recordAdd(objSms.getId(), objSms.getAddress(), objSms.getMsg(), objSms.getReadState(), String.valueOf(objSms.getTime()), objSms.getFolderName(), smsDatabase);
                                operations.deleteRecord(smsDatabase);
                            } catch (Exception e) {
                                Toast.makeText(context, "SmsStateService record ERROR:" + e, Toast.LENGTH_LONG).show();
                            } finally {
                                if (cursor2 != null)
                                    cursor2.close();
                            }


                        } else {
                            cursor2 = null;
                            //Toast.makeText(context, "same sms id", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(context, "SmsStateService ERROR:" + e, Toast.LENGTH_LONG).show();
                    } finally {
                        smsDatabase.close();
                    }

                    cal.setTimeInMillis(objSms.getTime());
                    int month = cal.get(Calendar.MONTH);

                    if (!smsMap.containsKey(month))
                        smsMap.put(month, new ArrayList<SmsVeriable>());

                    smsMap.get(month).add(objSms);

                    c.moveToNext();
                }
            }
            c.close();

            return smsMap;
        }
}