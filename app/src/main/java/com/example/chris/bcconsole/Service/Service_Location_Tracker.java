package com.example.chris.bcconsole.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by chris on 02/10/2017.
 */

public class Service_Location_Tracker extends Service{

    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000*60*2;
    private static final float LOCATION_DISTANCE = 0;

    private class LocationListener implements android.location.LocationListener
    {

        Location mLastLocation;
        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {

            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            if(location.getAccuracy() < 210.0) {

                Date currentTime = Calendar.getInstance().getTime();

                Log.d(TAG, String.valueOf(currentTime));
                Log.d(TAG, "LOCATION-LAT: "+String.valueOf(location.getLatitude()));
                Log.d(TAG, "LOCATION-LNG: "+String.valueOf(location.getLongitude()));

                Toast.makeText(Service_Location_Tracker.this, String.valueOf(currentTime), Toast.LENGTH_SHORT).show();

            }


        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

            Log.e(TAG, "onStatusChanged: " + s);

        }

        @Override
        public void onProviderEnabled(String s) {

            Log.e(TAG, "onProviderEnabled: " + s);

        }

        @Override
        public void onProviderDisabled(String s) {

            Log.e(TAG, "onProviderDisabled: " + s);


        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Toast.makeText(this, "SERVICE START", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
