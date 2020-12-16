package com.crypto.croytowallet.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.signup.Referral_code;
import com.crypto.croytowallet.signup.SignUp;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Login extends AppCompatActivity {
Button login;
TextView forget_password,signup;
EditText username,password;
    KProgressHUD progressDialog;
    String url="http://13.233.136.56:8080/api/user/login";
    ConstraintLayout linearLayout;
    Animation fade_in,blink;
    FusedLocationProviderClient fusedLocationProviderClient;
    String locations,ipAddress,os;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login =findViewById(R.id.login1);
        signup =findViewById(R.id.signup);

        //input
        username = findViewById(R.id.ed_username1);
        password = findViewById(R.id.ed_password1);
       // animation
        linearLayout= findViewById(R.id.login_layout);
        fade_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        blink= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        linearLayout.startAnimation(fade_in);

        forget_password=findViewById(R.id.forget);
        listener();


        //if the user is already logged in we will directly start the profile activity

      /*  if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                login.startAnimation(blink);
            }
        });
    }


    public void signIn() {
        if (validate() == false) {
            onSignupFailed();
            return;
        }
        saveToServerDB();

    }
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Please fill all requirement ", Toast.LENGTH_LONG).show();

        login.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String passwords = password.getText().toString().trim();
        String usernames = username.getText().toString().trim();

        if (passwords.isEmpty() || passwords.length() < 8) {
            password.setError("please enter your password is more then 8 digit");
            requestFocus(password);
            valid = false;
        } else {
            password.setError(null);
        }

        if (usernames.isEmpty()) {
            username.setError("Please enter username");
            requestFocus(username);
            valid = false;
        } else {
            username.setError(null);
        }

        return valid;
    }

    private void saveToServerDB() {

        String passwords = password.getText().toString().trim();
        String usernames = username.getText().toString().trim();

        progressDialog = KProgressHUD.create(Login.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();


                Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                parseVolleyError(error);
                 // Toast.makeText(getBaseContext(), "Some thing is Wrong", Toast.LENGTH_LONG).show();
              //  Toast.makeText(Login.this, ""+error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username",usernames);
                params.put("password",passwords);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                // headers.put("Authorization", "Bearer "+Token);

                return headers;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

    }

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);

            String message=data.getString("error");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        }
    }

    public void getDetails(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(Login.this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getLocation();

        }else{
            ActivityCompat.requestPermissions(Login.this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}
                    ,44);
        }
        getosName();
        Ipaddress();
    }
    public void getosName(){
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                os= String.valueOf(builder.append(fieldName));
                //    builder.append("sdk=").append(fieldValue);

            }
        }
    }
    public void Ipaddress(){
        try {
            //permition
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL myIp = new URL("https://checkip.amazonaws.com/");
            URLConnection c = myIp.openConnection();
            c.setConnectTimeout(1000);
            c.setReadTimeout(1000);

            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            // textView.setText(in.readLine());
            ipAddress=in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                // Initialize Location
                Location location =task.getResult();
                if (location!=null){
                    try {
                        // Initialize geoCoder
                        Geocoder geocoder = new Geocoder(Login.this, Locale.getDefault());
                        // Initialize addressList
                        List<Address> addresses =geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );
                        locations=addresses.get(0).getSubLocality()+","+addresses.get(0).getLocality();
                       /* textView1.setText(Html.fromHtml("<font color='#6200EE'><b>Latitude : </b><br></font>"
                                +addresses.get(0).getLocality()));*//* +addresses.get(0).getLatitude()+"<br>"+"<font color='#6200EE'><b>Longitude : </b><br></font> "
                            +addresses.get(0).getLongitude()));
 *//*               //    Toast.makeText(MainActivity.this, ""+addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
                         */  }catch (Exception e){
                    }

                }
            }
        });
    }
    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


public void listener(){
    forget_password.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
            finish();
        }
    });
    signup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), Referral_code.class));
            finish();
        }
    });
}

}