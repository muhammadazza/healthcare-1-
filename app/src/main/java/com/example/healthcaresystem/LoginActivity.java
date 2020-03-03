package com.example.healthcaresystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.telephony.TelephonyManager;
import android.content.pm.PackageManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;


public class LoginActivity extends AppCompatActivity {

    private EditText name, password, patient;
    private TextView noIMEI;
    private Button btn_login;
    private TextView register;
    private Button getIMEI;
    private String IMEI;
    private static final int REQUEST_CODE = 101;
    private static String URL_LOGIN = "http://192.168.41.165:1234/login";
    //private static String URL_LOGIN = "http://192.168.43.194:1234/login";
    //private static String URL_LOGIN = "http://192.168.43.228:1234/login";
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loading = findViewById(R.id.loading);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        noIMEI = findViewById(R.id.noIMEI);
        btn_login = findViewById(R.id.btn_login);
        register = findViewById(R.id.link_regist);
        getIMEI = findViewById(R.id.getIMEI);
        patient =  findViewById(R.id.patient);

        getIMEI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
                    return;
                }
                IMEI = telephonyManager.getDeviceId();
                noIMEI.setText(IMEI);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mName = name.getText().toString().trim();
                final String mPass = password.getText().toString().trim();
                final String mIMEI = noIMEI.getText().toString().trim();
                final String mpatient = patient.getText().toString().trim();

                if (!mName.isEmpty() || !mPass.isEmpty() || !mpatient.isEmpty() || !mIMEI.isEmpty()){
                    Login(mName, mPass, mpatient, mIMEI);
                } else {
                    name.setError("Masukkan nama!");
                    password.setError("Masukkan password");
                    patient.setError("Masukkan nama pasien");
                    noIMEI.setError("Masukkan IMEI");
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    private void Login(final String name, final String password, final String patient, final String IMEI) {
        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("name").trim();
                                    String patient = object.getString("patient").trim();
                                    String IMEI = object.getString("IMEI").trim();

                                    Toast.makeText(LoginActivity.this, "Success Login. \nYour Name : "
                                            +name+"\nYour IMEI : "
                                            +(IMEI), Toast.LENGTH_SHORT)
                                            .show();
                                    loading.setVisibility(View.GONE);
                                    startActivity(new Intent(LoginActivity.this,PulseActivity.class));
                                }
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("password", password);
                params.put("patient", patient);
                params.put("IMEI", IMEI);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
