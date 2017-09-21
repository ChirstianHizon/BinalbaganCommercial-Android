package com.example.chris.bcconsole.Delivery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.Service.LocatorService;

public class DeliveryOnProgress extends AppCompatActivity {

    private Button btnend;
    private Activity context =this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_on_progress);

        btnend = (Button)findViewById(R.id.btn_end_delivery);

        btnend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endLocator();
            }
        });
    }

    private void endLocator(){
        Intent intent = new Intent(context, LocatorService.class);
        intent.putExtra("STATUS", true);
        startService(intent);
    }
}
