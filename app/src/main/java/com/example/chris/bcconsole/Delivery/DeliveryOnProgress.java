package com.example.chris.bcconsole.Delivery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.chris.bcconsole.DeliveryMainActivity;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.SQLite.DBController;
import com.example.chris.bcconsole.Service.LocatorService;

import org.json.JSONArray;

public class DeliveryOnProgress extends AppCompatActivity {

    private Button btnend;
    private Activity context =this;
    private DBController myDb;
    private JSONArray coord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_on_progress);
        myDb = new DBController(this);
        
        Intent intent = getIntent();
        String order_id = intent.getStringExtra("ID");
        runLocator(order_id);

        btnend = (Button)findViewById(R.id.btn_end_delivery);

        btnend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endLocator();

                SharedPreferences preferences = getSharedPreferences("DELIVERY",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(context, DeliveryMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
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

}
