package com.example.chris.bcconsole.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.chris.bcconsole.Delivery.DeliveryOnProgress;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.SQLite.DBController;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by chris on 02/10/2017.
 */

public class Service_Location_Tracker extends Service{

    private static final String TAG = "LOCATION_SERVICE";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000*60*1;//2 mins
    private static final float LOCATION_DISTANCE = 0;
    private NotificationManager notificationManager;
    private DBController myDb;
    private String orderid;

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

            if(location.getAccuracy() >= 30.0) {
//            if(true){
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(100);

                Date currentTime = Calendar.getInstance().getTime();

                Log.d(TAG, String.valueOf(currentTime));
                Log.d(TAG, "LOCATION-LAT: "+String.valueOf(location.getLatitude()));
                Log.d(TAG, "LOCATION-LNG: "+String.valueOf(location.getLongitude()));
                Log.d(TAG, "LOCATION-ACC: "+String.valueOf(location.getAccuracy()));

                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
                String accuracy = String.valueOf(location.getAccuracy());

                insertLocation(latitude,longitude,accuracy);
                sendBroadcastMessage(true);

//                Toast.makeText(Service_Location_Tracker.this, String.valueOf(currentTime), Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(Service_Location_Tracker.this, "Accuracy Level Low", Toast.LENGTH_SHORT).show();
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



        sendBroadcastMessage(true);

        SharedPreferences prefs = getSharedPreferences("DELIVERY", MODE_PRIVATE);
        orderid = prefs.getString("id", null);

        if (orderid == null || orderid == "") {

            stopServiceFunction();

        }else{

            Notification();
            Toast.makeText(this, orderid, Toast.LENGTH_SHORT).show();

        }


        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        myDb = new DBController(this);

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
        notificationManager.cancel(0);
        Toast.makeText(this, "Service has Stopped", Toast.LENGTH_SHORT).show();
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }



//    ---------------------------------------------------------------------------------------------------------  //

    public void stopServiceFunction(){
        sendBroadcastMessage(false);
        Toast.makeText(this, "STOPPING SERVICE", Toast.LENGTH_SHORT).show();
        stopSelf();
    }

    public void Notification(){
        Intent intent = new Intent(this, DeliveryOnProgress.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setTicker("Delivery Locator..").setContentTitle("BC Console")
                .setContentText("Delivery on Progress")
                .setSmallIcon(R.drawable.logo)
                .setOngoing(true)
                .setContentIntent(pIntent);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(notificationSound);
        Notification noti = builder.build();
        noti.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);
    }

    private void insertLocation(String lat,String lng,String acc){
        if(orderid != null){
            myDb.insertRoute(lat,lng, orderid,acc);
        }
    }

    private void sendBroadcastMessage(Boolean status) {

        String ACTION_LOCATION_BROADCAST = Service_Location_Tracker.class.getName() + "LocationBroadcast";
        Log.d("ACTION_LOCATION",ACTION_LOCATION_BROADCAST);

        int routecount = myDb.countRoute();
        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra("counter", String.valueOf(routecount));
        intent.putExtra("status", status);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);


    }
}
