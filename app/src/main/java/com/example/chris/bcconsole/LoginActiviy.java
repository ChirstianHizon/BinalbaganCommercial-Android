package com.example.chris.bcconsole;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class LoginActiviy extends AppCompatActivity {
    private static final String TAG = "Login Activity";
    private Button btnlogin;
    private TextView tvusername;
    private TextView tvpassword;
    private TextView tvstatus;
    private Activity context = this;
    private ImageButton ivlogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activiy);

        tvstatus = (TextView) findViewById(R.id.status);
        btnlogin = (Button) findViewById(R.id.login);
        tvusername = (TextView) findViewById(R.id.username);
        tvpassword = (TextView) findViewById(R.id.password);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnlogin.setEnabled(false);
                tvstatus.setText("");
                checkUser();
            }
        });

        ivlogo = (ImageButton)findViewById(R.id.iv_logo);
        ivlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActiviy.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        SharedPreferences prefs = getSharedPreferences("USER", MODE_PRIVATE);
        Boolean status = Boolean.valueOf(prefs.getString("uid", "true"));
//        Log.d(TAG, prefs.getString("uid", "ERROR"));
        Intent intent = null;       
        if (!status) {
            String type = prefs.getString("type", "");

            switch (type) {

                case "0":
                    intent = new Intent(LoginActiviy.this, AdminMainActivity.class);

                    break;
                case "1":

                    break;
                case "2":
                    intent = new Intent(LoginActiviy.this, DeliveryMainActivity.class);

                    break;

            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }


    }

    private void checkUser() {
        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
        final SharedPreferences.Editor editor = getSharedPreferences("USER", MODE_PRIVATE).edit();
        final ProgressDialog progDailog = ProgressDialog.show(this,
                "Connecting to Server",
                "Verifying Account....", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LOGIN: ", response);
                        try {
                            JSONObject reader = new JSONObject(response);
                            Boolean status = Boolean.valueOf(reader.getString("status"));
                            if (status) {
                                editor.putString("uid", reader.getString("uid"));
                                editor.putString("uname", reader.getString("uname"));
                                editor.putString("fname", reader.getString("fname"));
                                editor.putString("lname", reader.getString("lname"));
                                editor.putString("image", reader.getString("image"));
                                editor.putString("type", reader.getString("type"));
                                editor.apply();

                                Intent intent = null;
                                switch (reader.getString("type")) {

                                    case "0":
                                        intent = new Intent(LoginActiviy.this, AdminMainActivity.class);

                                        break;
                                    case "1":

                                        break;
                                    case "2":
                                        intent = new Intent(LoginActiviy.this, DeliveryMainActivity.class);

                                        break;

                                }

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                                Toast.makeText(LoginActiviy.this, "Logged In", Toast.LENGTH_SHORT).show();

                            } else {
                                tvstatus.setText("Invalid Username/Password");
                                btnlogin.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progDailog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvstatus.setText("Server Error");
                progDailog.dismiss();
                btnlogin.setEnabled(true);
                Toast.makeText(context, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("uname", tvusername.getText().toString());
                params.put("pass", tvpassword.getText().toString());
                params.put("type", "1");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void uploadtodb() {
        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
        final SharedPreferences.Editor editor = getSharedPreferences("USER", MODE_PRIVATE).edit();
        final ProgressDialog progDailog = ProgressDialog.show(this,
                "Connecting to Server",
                "Verifying Account....", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LOGIN: ", response);
                        try {
                            JSONObject reader = new JSONObject(response);
                            Boolean status = Boolean.valueOf(reader.getString("status"));
                            if (status) {
                                editor.putString("uid", reader.getString("uid"));
                                editor.putString("uname", reader.getString("uname"));
                                editor.putString("fname", reader.getString("fname"));
                                editor.putString("lname", reader.getString("lname"));
                                editor.putString("image", reader.getString("image"));
                                editor.putString("type", reader.getString("type"));
                                editor.apply();

                                Intent intent = null;
                                switch (reader.getString("type")) {

                                    case "0":
                                        intent = new Intent(LoginActiviy.this, AdminMainActivity.class);

                                        break;
                                    case "1":

                                        break;
                                    case "2":
                                        intent = new Intent(LoginActiviy.this, DeliveryMainActivity.class);

                                        break;

                                }

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                                Toast.makeText(LoginActiviy.this, "Logged In", Toast.LENGTH_SHORT).show();

                            } else {
                                tvstatus.setText("Invalid Username/Password");
                                btnlogin.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progDailog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvstatus.setText("Server Error");
                progDailog.dismiss();
                btnlogin.setEnabled(true);
                Toast.makeText(context, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("uname", tvusername.getText().toString());
                params.put("pass", tvpassword.getText().toString());
                params.put("type", "1");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
