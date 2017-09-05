package com.example.chris.bcconsole;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class LoginActiviy extends AppCompatActivity {
    private String url = MainActivity.url + "login.php";
    private Button btnlogin;
    private TextView tvusername;
    private TextView tvpassword;
    private TextView tvstatus;
    private Context context;

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
                tvstatus.setText("");
                checkUser();
            }
        });

    }

    private void checkUser() {


        final SharedPreferences.Editor editor = getSharedPreferences("USER", MODE_PRIVATE).edit();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject reader = new JSONObject(response);
                            Boolean status = Boolean.valueOf(reader.getString("status"));
//                            Toast.makeText(LoginActiviy.this, "Status:" + status, Toast.LENGTH_SHORT).show();
//                            tvstatus.setText(response);
                            if (status) {
                                editor.putString("uname", reader.getString("uid"));
                                editor.putString("fname", reader.getString("fname"));
                                editor.putString("lname", reader.getString("lname"));
                                editor.putString("image", reader.getString("image"));
                                editor.putString("type", reader.getString("type"));
                                editor.apply();

                                Toast.makeText(LoginActiviy.this, "Logged In", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActiviy.this, MainActivity.class);
                                startActivity(intent);

                            } else {
                                tvstatus.setText("Invalid Username/Password");
//                                tvstatus.setText(response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvstatus.setText("Server Error");
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
