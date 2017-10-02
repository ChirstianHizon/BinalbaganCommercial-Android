package com.example.chris.bcconsole.Delivery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.bcconsole.DeliveryMainActivity;
import com.example.chris.bcconsole.LoginActiviy;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.SQLite.DBController;
import com.example.chris.bcconsole.Service.LocatorService;
import com.example.chris.bcconsole.SettingsActivity;

public class DeliveryOnProgress extends AppCompatActivity {

    private Button btnend;
    private Activity context =this;
    private DBController myDb;
    private static Boolean isDeliveryFinised = false;
    private SharedPreferences preferences;
    private TextView tvlat;
    private TextView tvlng;
    private TextView tvcounter;
    private TextView tvorderno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_on_progress);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = new DBController(this);
        
        Intent intent = getIntent();
        String order_id = intent.getStringExtra("ID");
        runLocator(order_id);

        btnend = (Button)findViewById(R.id.btn_end_delivery);
        tvlat = (TextView)findViewById(R.id.tv_lat);
        tvlng = (TextView)findViewById(R.id.tv_lng);
        tvcounter = (TextView)findViewById(R.id.tv_counter);
        tvorderno = (TextView)findViewById(R.id.tv_orderno);

        tvorderno.setText(order_id);

        btnend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endLocator();
                btnend.setText("Uploading Data");
                if(isDeliveryFinised){

                    preferences = getSharedPreferences("DELIVERY",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

                    Intent intx = new Intent(context, LocatorService.class);
                    stopService(intx);

                    Intent intent = new Intent(context, DeliveryMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(context, "Attempting to Connect to Server...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        double latitude = intent.getDoubleExtra(LocatorService.EXTRA_LATITUDE, 0);
                        double longitude = intent.getDoubleExtra(LocatorService.EXTRA_LONGITUDE, 0);
                        int routecount = intent.getIntExtra(LocatorService.EXTRA_COUNTER, 0);
                        String status = intent.getStringExtra(LocatorService.EXTRA_STATUS);

                        if(status != null || !status.equals("")){
                            btnend.setText("CLOSE DELIVERY");
                        }else{
                            btnend.setText("END DELIVERY");
                        }
                        tvlat.setText(String.valueOf( latitude ));
                        tvlng.setText(String.valueOf( longitude ));
                        tvcounter.setText(String.valueOf( routecount ));

                    }
                }, new IntentFilter(LocatorService.ACTION_LOCATION_BROADCAST)
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {

            SharedPreferences preferences = getSharedPreferences("USER", MODE_PRIVATE);
            preferences.edit().remove("uid").apply();

            Intent intent = new Intent(context, LoginActiviy.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }else if(id == R.id.action_settings){

            Intent intent = new Intent(context, SettingsActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    private void runLocator(String id){
        Intent intent = new Intent(context, LocatorService.class);
        intent.putExtra("ID", id);
        intent.putExtra("STATUS", false);
        startService(intent);

    }

    private void endLocator(){
        Intent intent = new Intent(context, LocatorService.class);
        intent.putExtra("STATUS", true);
        startService(intent);
    }

    public static void setDeliveryStatus(boolean stat){

        isDeliveryFinised = stat;
    }

}
