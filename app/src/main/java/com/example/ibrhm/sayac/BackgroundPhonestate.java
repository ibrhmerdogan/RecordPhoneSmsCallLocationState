package com.example.ibrhm.sayac;



import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


public class BackgroundPhonestate extends Service
{
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 1;
    TelephonyManager telephonyManager;
    PhoneStateListener listenerPhone;
    Context context;

    Intent intentt;
    int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        intentt = new Intent(BROADCAST_ACTION);
        context=this;
    }

    @Override
    public void onStart(Intent intent, int startId) {
       // locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //listener = new MyLocationListener();
        //noinspection MissingPermission
      //  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        //noinspection MissingPermission
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        state();
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


    public void state(){
    listenerPhone = new PhoneStateListener() {
    public void onCallStateChanged ( int state, String incomingNumber){
        String stateString = "N/A";
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                stateString = "Idle";
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                stateString = "Off Hook";
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                stateString = "Ringing";
                break;
        }
        Toast.makeText(context, "onCallStateChanged" + String.format("\n :%s", stateString), Toast.LENGTH_SHORT).show();
        // textOut.append(String.format("\n: %s",
        /////   stateString));
        intentt = new Intent("PhoneStates");
        intentt.putExtra("onCallStateChanged", stateString);
        sendBroadcast(intentt);
    }

    //Register the listener with the telephony manager

        };
        telephonyManager.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);
    }

}
