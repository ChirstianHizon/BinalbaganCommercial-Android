package com.example.chris.bcconsole;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.chris.bcconsole.AdminMainActivity.url;

public class SettingsActivity extends AppCompatActivity {
    private Activity context = this;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        final EditText edtipconfig = (EditText) findViewById(R.id.edt_ipconfig);
        Button btnsave = (Button) findViewById(R.id.btn_save);
        Button btncheck = (Button)findViewById(R.id.btncheck);
        status = (TextView)findViewById(R.id.tvstat);

        edtipconfig.setText(AdminMainActivity.url);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("USER", MODE_PRIVATE).edit();
                editor.putString("url", edtipconfig.getText().toString());
                editor.apply();
                AdminMainActivity.setNewUrl(edtipconfig.getText().toString());
                Toast.makeText(SettingsActivity.this, "Saved Settings", Toast.LENGTH_SHORT).show();
                Toast.makeText(SettingsActivity.this, AdminMainActivity.url, Toast.LENGTH_SHORT).show();

            }
        });

        btncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pingWebServer();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void pingWebServer() {
        status.setText("Connecting...");
//        Toast.makeText(context, AdminMainActivity.url, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject main = new JSONObject(response);
                            Boolean connection = main.getBoolean("main");
                            if(connection){
                                status.setText("Connection established");
                            }else{
                                status.setText("Server Error");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    };
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                status.setText("Unable to Established Connection");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("type", "0");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
