package com.crypto.croytowallet.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SignUpData;
import com.crypto.croytowallet.SharedPrefernce.SignUpRefernace;
import com.crypto.croytowallet.TransactionPin.TransactionPin;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.login.Change_Password;
import com.crypto.croytowallet.login.Login;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    Animation fade_in;
    ConstraintLayout constraintLayout;
    Button ready_to1;
    EditText name1,username1,email1,phoneno1,pass1,repass1;
    KProgressHUD progressDialog;
    String url="http://13.233.136.56:8080/api/user/register";
    //RequestQueue requestQueue;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // animation
        constraintLayout =findViewById(R.id.signup1);
        fade_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        sharedPreferences=getSharedPreferences("MyData", Context.MODE_PRIVATE);

      //  constraintLayout.startAnimation(fade_in);



      //  requestQueue= Volley.newRequestQueue(this);


        name1=findViewById(R.id.name1);
        username1=findViewById(R.id.username1);
        email1=findViewById(R.id.email1);
        phoneno1=findViewById(R.id.phone1);
        pass1=findViewById(R.id.pass1);
        repass1=findViewById(R.id.re_pass1);

        ready_to1=findViewById(R.id.ready_to12);
        ready_to1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           signup(v);
                hideKeyboard((Button)v);
            }
        });

    }
    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }
    public void signup(View parentView) {
        if (validate() == false) {
            onSignupFailed();
            return;
        }
        saveToServerDB(parentView);
    }


    public void onSignupFailed() {
     //   Toast.makeText(getBaseContext(), "Please fill all requirement ", Toast.LENGTH_LONG).show();

        Snackbar warningSnackBar = Snacky.builder()
                .setActivity(SignUp.this)
                .setText("Please fill all requirement")
                .setTextColor(getResources().getColor(R.color.white))
                .setDuration(Snacky.LENGTH_SHORT)
                .warning();
        warningSnackBar.show();
        ready_to1.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = name1.getText().toString();
        String username = username1.getText().toString();
        String email = email1.getText().toString();
        String mobile = phoneno1.getText().toString();
        String password = pass1.getText().toString();
        String reEnterPassword = repass1.getText().toString();

        if (name.isEmpty() ) {
            name1.setError("Please enter your name");
            requestFocus(name1);
            valid = false;
        } else {
            name1.setError(null);
        }
        if (username.isEmpty() ) {
            username1.setError("Please enter your username");
            requestFocus(username1);
            valid = false;
        } else {
            username1.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email1.setError("enter a valid email address");
            requestFocus(email1);
            valid = false;
        } else {
            email1.setError(null);
        }
        if (mobile.isEmpty() || mobile.length()<10) {
            phoneno1.setError("enter a Mobile");
            requestFocus(phoneno1);
            valid = false;
        } else {
            phoneno1.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 ) {
            pass1.setError("please enter your password is more then 8 digit");
            requestFocus(pass1);
            valid = false;
        } else {
            pass1.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4  || !(reEnterPassword.equals(password))) {
            repass1.setError("Password Do not match");
            requestFocus(repass1);
            valid = false;
        } else {
            repass1.setError(null);
        }

        return valid;
    }

    private void saveToServerDB(View parentView) {

        progressDialog=  KProgressHUD.create(SignUp.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        String name = name1.getText().toString();
        String username = username1.getText().toString();
        String email = email1.getText().toString();
        String mobile = phoneno1.getText().toString();
        String password = pass1.getText().toString();
        Bundle bundle = getIntent().getExtras();
        String refercode = bundle.getString("referral");
        Call<ResponseBody> call= RetrofitClient
                .getInstance()
                .getApi().register(name,email,username,password,refercode,mobile);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s=null;
                hidepDialog();

                if (response.code()==200) {

                    try {
                        s=response.body().string();
                        JSONObject object=new JSONObject(s);
                        String result=object.getString("result");
                        JSONObject object1=new JSONObject(result);

                        String googlekey =object1.getString("googleAuthenticatorKey");

                        JSONObject object2 = new JSONObject(googlekey);
                    SignUpData user = new SignUpData(
                                object1.getString("username"),
                                object1.getString("mnemonic"),
                                 object2.getString("key")
                        );

                        //storing the user in shared preferences
                        SignUpRefernace.getInstance(getApplicationContext()).UserSignUP(user);

                        Intent intent = new Intent(getApplicationContext(),TransactionPin.class);
                        startActivity(intent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else if (response.code()==400)
                {

                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snackbar snackbar = Snackbar
                                .make(parentView, "Oops username or email address is already exist", Snackbar.LENGTH_LONG)
                                .setAction("ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Snackbar snackbar1 = Snackbar.make(parentView, " "+error, Snackbar.LENGTH_SHORT);
                                        snackbar1.show();
                                    }
                                });

                        snackbar.show();
                       // Toast.makeText(SignUp.this, jsonObject1.getString("error")+"", Toast.LENGTH_SHORT).show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                    //    Toast.makeText(ContactNumberActivity.this, "Please enter your details", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Toast.makeText(SignUp.this, "net", Toast.LENGTH_SHORT).show();

            }
        });





        //   Toast.makeText(SignUp.this,name+username+email+mobile+password+refercode, Toast.LENGTH_SHORT).show();

       /* StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                Toast.makeText(SignUp.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
              parseVolleyError(error);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name",name);
                params.put("email",email);
                params.put("username",username);
               *//* params.put("referalCode",refercode);
               *//* params.put("phone",mobile);
                params.put("password",password);

            *//*    {
                    "name":"devender singh",
                        "email":"Deveder@gmail.com",
                        "username":"dev123",
                        "password":"12345678",
                        "referalCode":"0rbvw8imgw",
                        "countryCode":"+91",
                        "phone":"7017760600",
                        "country":"INDIA"

                }*//*
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();


                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);*/

    }
    /*
    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);

            String message=data.getString("error");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        }
    }*/
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), Referral_code.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void signup_login(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public void signup_signup(View view) {
        Intent intent = new Intent(getApplicationContext(), Referral_code.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}