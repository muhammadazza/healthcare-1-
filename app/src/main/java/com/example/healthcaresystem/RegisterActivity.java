package com.example.healthcaresystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.math.BigInteger;
import java.security.*;
import javax.crypto.*;

import android.content.Context;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, password, c_password;
    private TextView noIMEI;
    private Button btn_regist;
    private Button getIMEI;
    private ProgressBar loading;
    private String IMEI;
    private static final int REQUEST_CODE = 101;
    //private static String URL_REGIST = "http://192.168.43.228:1234/insert";
    private static String URL_REGIST = "http://192.168.41.165:1234/login";
    //private static String URL_REGIST = "http://192.168.43.194:1234/insert";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loading = findViewById(R.id.loading);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        noIMEI = findViewById(R.id.noIMEI);
        btn_regist = findViewById(R.id.btn_regist);
        getIMEI = findViewById(R.id.getIMEI);

        getIMEI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
                    return;
                }
                IMEI = telephonyManager.getDeviceId();
                noIMEI.setText(IMEI);
            }
        });

        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Regist();
            }
        });
    }

    private void Regist() {
        loading.setVisibility(View.VISIBLE);
        btn_regist.setVisibility(View.GONE);

        final String name = this.name.getText().toString().trim();
        final String password = this.password.getText().toString().trim();
//        byte[] password = this.password.getText().toString().getBytes();
        final String IMEI2 = this.noIMEI.getText().toString().trim();

//        MessageDigest sha256 = null;
//        try {
//            sha256 = MessageDigest.getInstance("SHA-256");
//        } catch (NoSuchAlgorithmException e){
//            e.printStackTrace();
//        }
//        sha256.update(password);
//        byte[] hasil = sha256.digest();
//        StringBuffer hexhasil = new StringBuffer();
//        for (int i=0; i<hasil.length; i++)
//            hexhasil.append(Integer.toString((hasil[i]&0xff)+0x100,16).substring(1));
//        String passwordhash.setText(hexhasil);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject =  new JSONObject(response);
                            String success = jsonObject.getString("BERHASIL REGISTRASI");
                            //String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(RegisterActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                //startActivity(Intent(RegisterActivity.this, LoginActivity));
                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Register Error!1" + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            btn_regist.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Register Error!2"+ error.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_regist.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("password", password);
                params.put("IMEI",IMEI2);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}