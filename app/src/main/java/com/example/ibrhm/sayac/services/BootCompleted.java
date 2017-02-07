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
        Intent serviceIntent1 = new Intent(context, SmsStateservice.class);
        context.startService(serviceIntent1);
        Intent serviceIntent2 = new Intent(context, PhoneStateService.class);
        context.startService(serviceIntent2);
        Intent serviceIntent3 = new Intent(context, CallStateService.class);
        context.startService(serviceIntent3);
    }
}