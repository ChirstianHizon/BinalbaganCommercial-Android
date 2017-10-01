package com.example.chris.bcconsole.Admin.Reports;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.chris.bcconsole.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePicker_End extends AppCompatActivity {

    private String date;
    private Intent intent;

    private Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker_end);

        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

         intent = getIntent();
        final String type = intent.getStringExtra("Report_Type");
        final String start = intent.getStringExtra("Date_Start");

        Toast.makeText(this, type+" | "+start, Toast.LENGTH_SHORT).show();

        CalendarView cvend = (CalendarView)findViewById(R.id.cv_end);

        cvend.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                date = year+ "-" +String.format("%02d",(month+1)) + "-" + String.format("%02d",day);
            }
        });

        Button btnview = (Button)findViewById(R.id.btn_view);


        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, date, Toast.LENGTH_SHORT).show();


                switch (type){
                    case "inventory":
                        intent = new Intent(context, Report_Inventory.class);
                        break;

                    case "sales":

                        intent = new Intent(context, Report_Sales.class);
                        break;

                    case "delivery":
                        intent = new Intent(context, Report_Delivery.class);
                        break;
                }

//                 intent = new Intent(context, DatePicker_End.class);
                intent.putExtra("Report_Type",type);
                intent.putExtra("Date_Start",start);
                intent.putExtra("Date_End",date);
                startActivity(intent);
            }
        });
    }
}
