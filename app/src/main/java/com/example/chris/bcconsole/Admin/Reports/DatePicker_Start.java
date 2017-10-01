package com.example.chris.bcconsole.Admin.Reports;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.chris.bcconsole.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePicker_Start extends AppCompatActivity {
    private Activity context = this;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker_start);

        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Intent intent = getIntent();
        final String type = intent.getStringExtra("Report_Type");

        Button btnview = (Button)findViewById(R.id.btn_view);

        final CalendarView cvstart = (CalendarView)findViewById(R.id.cv_start);


        cvstart.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                date = year+ "-" +String.format("%02d",(month+1)) + "-" + String.format("%02d",day);
            }
        });

        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, date, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, DatePicker_End.class);
                intent.putExtra("Report_Type",type);
                intent.putExtra("Date_Start",date);
                startActivity(intent);
            }
        });
    }
}
