package com.example.chris.bcconsole.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chris.bcconsole.Delivery.DeliveryOnProgress;
import com.example.chris.bcconsole.DeliveryMainActivity;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.SQLite.DBController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.delay;
import static com.example.chris.bcconsole.AdminMainActivity.url;
//import static com.example.chris.bcconsole.AdminMainActivity.url;

public class LocatorService extends Service {
    private DBController myDb;
    private NotificationManager notificationManager;
    private Boolean status;
    public static final int DEFAULT_LOCATION_INTERVAL = 1000*60*2;//1 min
    private LocationManager mLocationManager;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private String order_id;
    private int initLocatorCounter = 0;
    private int initCoordCounter = 0;
    private Handler h;
    private JSONArray coord = new JSONArray();
    private boolean locatorStarted = false;
    private Runnable run;

//    public static String defaulturl = "http://192.168.42.197/BinalbaganCommercial-Thesis/php/mobile.php";
//    public static String url = defaulturl;



    public LocatorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        myDb = new DBController(this);

        status = intent.getBooleanExtra("STATUS",false);
        order_id = intent.getStringExtra("ID");


        if(order_id !=  null){
            Log.d("SERVICE", order_id);
        }else{
            stopService();
        }

        //DBFunctions();
        locationtimer();
        Notification();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "GPS Locator Stopped", Toast.LENGTH_SHORT).show();
        notificationManager.cancel(0);
    }


    private void DBFunctions(){
        //TEST FOR DB CONTROLER
        Boolean test = myDb.insertRoute("test","test", order_id);
        Log.d("DB-INSERT", String.valueOf(test));

        Cursor route = myDb.getAllRoute();
        while(route.moveToNext()) {
            String id = route.getString(0);
            String lat = route.getString(1);
            String lng = route.getString(2);
            String datetime = route.getString(3);
            String orderid = route.getString(4);
            Log.d("DB-VIEW", id + ","+lat + ","+lng + ","+datetime+ ","+orderid);
        }

        Integer del = myDb.deleteAllRouteData();
        Log.d("DB-DELETE", String.valueOf(del));

        route = myDb.getAllRoute();
        while(route.moveToNext()) {
            String id = route.getString(0);
            String lat = route.getString(1);
            String lng = route.getString(2);
            String datetime = route.getString(3);
            Log.d("DB-VIEW", id + ","+lat + ","+lng + ","+datetime);
        }
    }

    public void Notification(){
        Intent intent = new Intent(this, DeliveryMainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setTicker("Delivery on Progress..").setContentTitle("BC Console")
                .setContentText("Delivery On Progress...")
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



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void locationtimer(){
        locatorStarted = true;
        h = new Handler();
        run = new Runnable() {
            @Override
            public void run() {
                initLocator();
                h.postDelayed(this,delay);
            }
        };
        initLocator();
        h.postDelayed(run, DEFAULT_LOCATION_INTERVAL);
    }

    private void initLocator() {
        initLocatorCounter++;
        Log.d("LOCATOR-COUNTER", String.valueOf(initLocatorCounter));

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // getting GPS status
        isGPSEnabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (isNetworkAvailable()) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, DEFAULT_LOCATION_INTERVAL, mLocationListener);

        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, DEFAULT_LOCATION_INTERVAL, mLocationListener);
        }
    }

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            initCoordCounter++;
            Log.d("LOCATOR-COORD", String.valueOf(initCoordCounter));

            Toast.makeText(LocatorService.this, "LOCATION ADDED", Toast.LENGTH_SHORT).show();
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());


            //INSERT LOCATION TO DB
            insertLocation(latitude,longitude, order_id);

            Log.d("SERVICE","LAT: "+ latitude);
            Log.d("SERVICE","LNG: "+ longitude);
            mLocationManager.removeUpdates(mLocationListener);

            if(status){
                stopService();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void insertLocation(String lat,String lng,String id){
        if(id != null){
            myDb.insertRoute(lat,lng, order_id);
        }
    }

    private void stopService(){

        if(locatorStarted){
            updateWeb();
            locatorStarted = false;
        }
    }

    private void updateWeb(){
        Cursor route = myDb.getAllRoute();
        while(route.moveToNext()) {
            String id = route.getString(0);
            String lat = route.getString(1);
            String lng = route.getString(2);
            String datetime = route.getString(3);
            String delivid = route.getString(4);
            JSONObject xcord = new JSONObject();
            try {
                xcord.put("id", id);
                xcord.put("lat", lat);
                xcord.put("lng", lng);
                xcord.put("datetime", datetime);
                xcord.put("delivid", delivid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("DB-ROUTE", id + ","+lat + ","+lng + ","+datetime+ ","+delivid);
            coord.put(xcord);
        }
        Log.d("COORD", String.valueOf(coord));
        submit(coord);
        myDb.deleteAllRouteData();
    }

    private void submit(final JSONArray coordobj) {
        Log.d("COORD_URL", url);
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("COORD-RESULT",response);

                        try {
                            JSONObject reader = new JSONObject(response);
                            if(reader.getBoolean("RESULT")){
                                DeliveryOnProgress.setDeliveryStatus(true);
                                h.removeCallbacks(run);
                                Log.d("SERVICE","LOCATOR STOPPED");
                                Toast.makeText(LocatorService.this, "DELIVERY COMPLETE", Toast.LENGTH_SHORT).show();
                                stopSelf();
                            }else{
                                Toast.makeText(LocatorService.this, "SERVER ERROR", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LocatorService.this, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("type", "9");
                params.put("coord", String.valueOf(coordobj));
                Log.d("COORD-POST", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

