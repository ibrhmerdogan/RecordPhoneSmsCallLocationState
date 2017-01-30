package com.example.ibrhm.sayac.services;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


public class BackgroundPhonestate extends Service
{
    public static final String BROADCAST_ACTION = "Hello World";
    TelephonyManager telephonyManager;
    PhoneStateListener listenerPhone;

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
        public void onCallStateChanged(int networkType, String incomingNumber) {
        String stateString = "N/A";
            // networkType = telephonyManager.getNetworkType();
            switch (networkType) {
                case (TelephonyManager.NETWORK_TYPE_1xRTT):
                    stateString = "NETWORK_TYPE_1xRTT";
                break;
                case (TelephonyManager.NETWORK_TYPE_CDMA):
                    stateString = "NETWORK_TYPE_CDMA";
                    break;
                case (TelephonyManager.NETWORK_TYPE_EDGE):
                    stateString = "NETWORK_TYPE_EDGE";
                    break;
                case (TelephonyManager.NETWORK_TYPE_EVDO_0):
                    stateString = "NETWORK_TYPE_EVDO_0";
                break;
        }
        Toast.makeText(context, "onCallStateChanged" + String.format("\n :%s", stateString), Toast.LENGTH_SHORT).show();
       intentt = new Intent("PhoneStates");
        intentt.putExtra("onCallStateChanged", stateString);
        sendBroadcast(intentt);
    }


        };
        telephonyManager.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);
    }

}
