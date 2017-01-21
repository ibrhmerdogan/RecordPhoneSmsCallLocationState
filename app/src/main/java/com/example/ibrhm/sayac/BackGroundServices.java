package com.example.ibrhm.sayac;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class BackGroundServices extends Service {
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 6000;

    Context context;
    int counter;
    Intent intent;
    public MyLocationListener listener;
    public LocationManager locationManager;
    public Location previousBestLocation;

    /* renamed from: com.example.ibrhm.sayac.BackGroundServices.1 */
    static class C01511 extends Thread {
        final /* synthetic */ Runnable val$runnable;

        C01511(Runnable runnable) {
            this.val$runnable = runnable;
        }

        public void run() {
            this.val$runnable.run();
        }
    }

    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location loc) {
            Log.i("**********", "Location changed");
            if (BackGroundServices.this.isBetterLocation(loc, BackGroundServices.this.previousBestLocation)) {
                loc.getLatitude();
                loc.getLongitude();
                Toast.makeText(BackGroundServices.this.context, "Latitude" + loc.getLatitude() + "\nLongitude" + loc.getLongitude(),Toast.LENGTH_LONG).show();
                Intent intentt = new Intent("location_update");

                intentt.putExtra("coordinates", loc.getLatitude() + " " + loc.getLongitude());
                sendBroadcast(intentt);
            }
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(BackGroundServices.this.getApplicationContext(), "Gps Disabled", Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String provider) {
            Toast.makeText(BackGroundServices.this.getApplicationContext(), "Gps Enabled", Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    public BackGroundServices() {
        this.previousBestLocation = null;
        this.counter = 0;
    }

    public void onCreate() {
        super.onCreate();
        this.intent = new Intent(BROADCAST_ACTION);
        this.context = this;
    }

    public void onStart(Intent intent, int startId) {
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.listener = new MyLocationListener();
        //noinspection MissingPermission
        this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, this.listener);
        //noinspection MissingPermission
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, this.listener);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > 60000;
        boolean isSignificantlyOlder = timeDelta < -60000;
        boolean isNewer = timeDelta > 0;
        if (isSignificantlyNewer) {
            return true;
        }
        if (isSignificantlyOlder) {
            return false;
        }
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());
        if (isMoreAccurate) {
            return true;
        }
        if (isNewer && !isLessAccurate) {
            return true;
        }
        if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        } else {
            return provider1.equals(provider2);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        //noinspection MissingPermission
        this.locationManager.removeUpdates(this.listener);
    }

    public static Thread performOnBackgroundThread(Runnable runnable) {
        Thread t = new C01511(runnable);
        t.start();
        return t;
    }
}
