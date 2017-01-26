package com.example.ibrhm.sayac;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.ibrhm.sayac.services.Sms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;


public class BackgrondSMSservice extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 1;
    String adres;
    String type;
    Cursor cursor2 = null;
    Context context;
    SmsDatabase smsDatabase;
    Intent intent;
    int counter = 0;
    Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        context=this;

    }
    @Override
    public void onStart(Intent intent, int startId) {

        smsFunction();


    }

    public void again() {
        timer = new Timer();
        timer.schedule(new TimerTask() {  //her 60 sn de bir bildirimGonder(); metodu çağırılır.
            @Override
            public void run() {
                smsFunction();
            }

        }, 0, 60000);
    }
    @Override
    public IBinder onBind(Intent intent){
        return null;
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
            try {

            Log.d("sms_sample", String.format("Month %d: %d sms", entry.getKey(), entry.getValue().size()));
            String mesaj = smsMap.toString();
                //Toast.makeText(context,"mesajlar"+mesaj,Toast.LENGTH_LONG).show();
            intent =new Intent("sms");
            intent.putExtra("Sms",mesaj);
            sendBroadcast(intent);
            }
            catch (Exception exception){
                            }
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

                    smsDatabase = new SmsDatabase(BackgrondSMSservice.this);
                    try {

                        Cursor cursor;

                        try {
                            SQLiteDatabase db1 = smsDatabase.getReadableDatabase();

                            cursor2 = db1.query("informationDB", new String[]{"id", "smsID", "type"}, "smsID=" + String.valueOf(objSms.getId()), null, null, null, null);
                            cursor2.moveToFirst();

                        } catch (java.lang.IllegalArgumentException e) {
                            Toast.makeText(context, "hata course" + e, Toast.LENGTH_LONG).show();
                        }


                        if (cursor2.getCount() == 0) {

                            Toast.makeText(context, "not same smsID", Toast.LENGTH_LONG).show();
                            try {


                                SQLiteDatabase db = smsDatabase.getWritableDatabase();
                                ContentValues data = new ContentValues();
                                data.put("smsID", objSms.getId());
                                data.put("type", objSms.getFolderName());
                                db.insertOrThrow("informationDB", null, data);
                            } catch (Exception e) {
                                Toast.makeText(context, "hatatt" + e, Toast.LENGTH_LONG).show();
                            }


                        } else {
                            cursor2 = null;
                            Toast.makeText(context, "same id", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(context, "hata" + e, Toast.LENGTH_LONG).show();
                    } finally {
                        smsDatabase.close();
                    }

                    cal.setTimeInMillis(objSms.getTime());
                    int month = cal.get(Calendar.MONTH);

                    if (!smsMap.containsKey(month))
                        smsMap.put(month, new ArrayList<Sms>());

                    smsMap.get(month).add(objSms);

                    c.moveToNext();
                }
            }
            c.close();

            return smsMap;
        }

    public int getRowCount() {
        // Bu method bu uygulamada kullanılmıyor ama her zaman lazım olabilir.Tablodaki row sayısını geri döner.
        //Login uygulamasında kullanacağız
        String countQuery = "SELECT  * FROM " + "informationDB";
        SQLiteDatabase db = smsDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }



}