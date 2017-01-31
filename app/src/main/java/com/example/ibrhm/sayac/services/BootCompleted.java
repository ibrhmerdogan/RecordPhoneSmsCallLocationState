package com.example.ibrhm.sayac.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ibrhm on 31.01.2017.
 */

public class BootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //here we start the service
        Intent serviceIntent = new Intent(context, LocationServices.class);
        context.startService(serviceIntent);
    }
}