package com.example.ibrhm.sayac.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.ibrhm.sayac.Data.DbOperations.PhoneStateDBOperation;
import com.example.ibrhm.sayac.Data.PhoneStateDB;

public class MyService extends Service {
    TelephonyManager telephonyManager;
    PhoneStateListener listenerPhone;
    PhoneStateDBOperation operation = new PhoneStateDBOperation();
    PhoneStateDB phoneStateDB = new PhoneStateDB(MyService.this);
    PhoneStateDBOperation phoneStateDBOperation = new PhoneStateDBOperation();

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
        Toast.makeText(getApplicationContext(), "he", Toast.LENGTH_LONG).show();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        state();
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
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void state() {
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
                phoneStateDBOperation.deleteRecord(phoneStateDB);
                // Toast.makeText(getApplicationContext(), "onCallStateChanged" + String.format("\n :%s", stateString), Toast.LENGTH_SHORT).show();
                //  Intent intentt = new Intent("PhoneStates");
                // intentt.putExtra("onCallStateChanged", stateString);
                // sendBroadcast(intentt);
            }


        };
        telephonyManager.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);
    }
}
