package com.example.ibrhm.sayac;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by ibrhm on 27.01.2017.
 */

public class StartReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(StartReceiver.this.context, "alarm", Toast.LENGTH_LONG).show();

    }
}
