package com.example.chris.bcconsole.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.chris.bcconsole.Delivery.DeliveryOnProgress;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.SQLite.DBController;

public class LocatorService extends Service {
    private DBController myDb;
    private NotificationManager notificationManager;
    private Boolean status;

    public LocatorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this, "SERVICE", Toast.LENGTH_SHORT).show();

        myDb = new DBController(this);

        status = intent.getBooleanExtra("STATUS",false);
        Log.d("SERVICE", String.valueOf(status));

        if(status){
            this.stopSelf();
        }

        DBFunctions();

        Notification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "GPS Locator Stopped", Toast.LENGTH_SHORT).show();
        notificationManager.cancel(0);
    }


    private void DBFunctions(){
        Boolean test = myDb.insertRoute("test","test","");
        Log.d("DB-INSERT", String.valueOf(test));

        Cursor route = myDb.getAllRoute();
        while(route.moveToNext()) {
            String id = route.getString(0);
            String lat = route.getString(1);
            String lng = route.getString(2);
            String datetime = route.getString(3);
            Log.d("DB-VIEW", id + ","+lat + ","+lng + ","+datetime);
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void Notification(){
        Intent intent = new Intent(this, DeliveryOnProgress.class);
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
}

