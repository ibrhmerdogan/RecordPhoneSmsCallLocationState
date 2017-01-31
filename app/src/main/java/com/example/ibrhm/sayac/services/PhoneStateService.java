package com.example.ibrhm.sayac.services;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.ibrhm.sayac.Data.DbOperations.PhoneStateDBOperation;
import com.example.ibrhm.sayac.Data.PhoneStateDB;


public class PhoneStateService extends Service
{
    public static final String BROADCAST_ACTION = "Hello World";
    TelephonyManager telephonyManager;
    PhoneStateListener listenerPhone;
    PhoneStateDBOperation operation = new PhoneStateDBOperation();
    PhoneStateDB phoneStateDB = new PhoneStateDB(PhoneStateService.this);
    Context context;

    Intent intentt;


    @Override
    public void onCreate() {
        super.onCreate();
        intentt = new Intent(BROADCAST_ACTION);
        context=this;


    }

    @Override
    public void onStart(Intent intent, int startId) {
         telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        state();
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }




    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        //noinspection MissingPermission
       // telephonyManager.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);

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


    public void state(){
    listenerPhone = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
        String stateString = "N/A";
            switch (state) {
                case (TelephonyManager.CALL_STATE_IDLE):
                    stateString = "idle";
                break;
                case (TelephonyManager.CALL_STATE_RINGING):
                    stateString = "ringing";
                    break;
                case (TelephonyManager.CALL_STATE_OFFHOOK):
                    stateString = "offHook";
                    break;
            }
            operation.recordState(stateString, phoneStateDB);
        Toast.makeText(context, "onCallStateChanged" + String.format("\n :%s", stateString), Toast.LENGTH_SHORT).show();
       intentt = new Intent("PhoneStates");
        intentt.putExtra("onCallStateChanged", stateString);
        sendBroadcast(intentt);
    }


        };
        telephonyManager.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);
    }

}
