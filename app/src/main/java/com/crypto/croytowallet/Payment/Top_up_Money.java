package com.crypto.croytowallet.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.croytowallet.Activity.Setting;
import com.crypto.croytowallet.AppUtils;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.RozerPayModelData;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;

public class Top_up_Money extends AppCompatActivity implements PaymentResultWithDataListener {
    ImageView imageView;
    CardView add_money;
    CheckBox checkBox;
    EditText enter_amount;
    KProgressHUD progressDialog;
    String orderId,dbID,payedAmount,status;
    UserData userData;
    RozerPayModelData rozerPayModelData1;
    private PaymentData paymentData1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up__money);
        imageView =findViewById(R.id.back);
        add_money=findViewById(R.id.add_money);
        checkBox=findViewById(R.id.checkbox);
        enter_amount=findViewById(R.id.enter_add_money_amont);
        userData= SharedPrefManager.getInstance(getApplicationContext()).getUser();
        rozerPayModelData1=new RozerPayModelData();
        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   startActivity(new Intent(getApplicationContext(),Complate_payment.class));
                finish();*/

                String amount=enter_amount.getText().toString().trim();
           if (amount.isEmpty()){
                    enter_amount.requestFocus();
                    enter_amount.setError("Please enter amount to add money");
                }else if(!checkBox.isChecked()){
                    checkBox.requestFocus();
                    Snacky.builder()
                            .setView(v)
                            .setText(" Please Accept the Terms & Conditions !")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else{
                    createOrderId();

                }
                hideKeyboard(v);

            }
        });
        back();
    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }
    public void createOrderId(){
        progressDialog = KProgressHUD.create(Top_up_Money.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

      //  String url="http://13.233.136.56:8080/api/razorpay/order";
        String amount=enter_amount.getText().toString().trim();
        String token=userData.getToken();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.URL_CREATE_ORDER_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();

                try {
                    JSONObject object=new JSONObject(response);

                        dbID=object.getString("_id");
                    String alldetails=object.getString("allPaymentDetails");

                    JSONObject object1=new JSONObject(alldetails);
                    orderId=object1.getString("razorpay_order_id");

  //                Toast.makeText(Top_up_Money.this, ""+dbID+"\n"+orderId, Toast.LENGTH_SHORT).show();
               Snacky.builder()
                            .setActivity(Top_up_Money.this)
                            .setText("Your order created successfully")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // This method will be executed once the timer is over
                            startPayment();
                        }
                    }, 300);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                params.put("amount",amount);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", token);

                return headers;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        /*RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);*/
    }

    public void startPayment() {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID(getResources().getString(R.string.razor_api_key));
        String name =userData.getName();
        String email =userData.getEmail();
        String no =userData.getMobile();
        String amount=enter_amount.getText().toString().trim();
      //  checkout.setImage(R.mipmap.ic_launcher_round);
        /**
         * Set your logo here
         */
//        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", name);

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Test order");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", orderId);
        //    options.put("amount", amount+"00");//pass amount in currency subunits
            options.put("currency", "INR");
            options.put("prefill.email", email);
            options.put("prefill.contact",no);
            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
//            options.put("amount", "500");

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        System.out.println("jsonobj " + paymentData.getData().toString());
        this.paymentData1 = paymentData;
        Snacky.builder()
                .setActivity(Top_up_Money.this)
                .setText("Payment paid successfully")
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show();
        String amount=enter_amount.getText().toString().trim();
        System.out.println("suus " + s + " " + paymentData.getExternalWallet() + " " + paymentData.getOrderId() + " pid " +
                paymentData.getPaymentId() + " sig " + paymentData.getSignature() + " " + paymentData.getUserContact() + " " + paymentData.getUserEmail());
        System.out.println("amt pd "+amount);
        rozerPayModelData1=new RozerPayModelData(amount,paymentData.getOrderId(),
                  paymentData.getPaymentId(),paymentData.getSignature());
        Snacky.builder()
                .setActivity(Top_up_Money.this)
                .setText("Please wait  your payment is currently under Processing......")
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                CaptureData();
            }
        }, 500);


    }




    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Snacky.builder()
                .setActivity(Top_up_Money.this)
                .setText(s)
                .setDuration(Snacky.LENGTH_SHORT)
                .setActionText(android.R.string.ok)
                .error()
                .show();

        System.out.println("jsonobj pmnt err " + paymentData.getData().toString());
    }

    public void CaptureData(){
        progressDialog = KProgressHUD.create(Top_up_Money.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

     //   String url="http://13.233.136.56:8080/api/razorpay/capture";
        String amount=enter_amount.getText().toString().trim();
        String token=userData.getToken();
        String razorpay_order_id=rozerPayModelData1.getRazorpayOrderId();
        String razorpay_payment_id=rozerPayModelData1.getRazorpayPaymentId();
        String razorpay_signature=rozerPayModelData1.getRazorpaySignature();


        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.URL_CAPTURE_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                try {
                    JSONObject object=new JSONObject(response);
                    payedAmount=object.getString("amount");
                    status=object.getString("status");
                 //   Toast.makeText(Top_up_Money.this, ""+payedAmount+status, Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(Top_up_Money.this, ""+orderId, Toast.LENGTH_SHORT).show();

                    savedataInDb();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                params.put("amount",amount);
                params.put("razorpay_order_id",razorpay_order_id);
                params.put("razorpay_payment_id",razorpay_payment_id);
                params.put("razorpay_signature",razorpay_signature);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", token);

                return headers;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
       /* RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);*/
    }

public void savedataInDb(){
    progressDialog = KProgressHUD.create(Top_up_Money.this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait.....")
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();

    showpDialog();

    String token=userData.getToken();
    String razorpay_order_id=rozerPayModelData1.getRazorpayOrderId();
    String razorpay_payment_id=rozerPayModelData1.getRazorpayPaymentId();
    String razorpay_signature=rozerPayModelData1.getRazorpaySignature();

    String url= URLs.URL_SAVE_DATA+""+dbID;

    StringRequest stringRequest=new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hidepDialog();
      //      Toast.makeText(Top_up_Money.this, ""+response, Toast.LENGTH_SHORT).show();
            AppUtils.showMessageOKCancel("Money Successfully Added to Your Wallet \n" +"Order No :" + orderId, Top_up_Money.this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Top_up_Money.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

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
            params.put("razorpay_order_id",razorpay_order_id);
            params.put("razorpay_payment_id",razorpay_payment_id);
            params.put("razorpay_signature",razorpay_signature);
            params.put("payedAmount",payedAmount);
            params.put("paymentType","razorpay");
            params.put("status","success");
            return params;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<String, String>();

            headers.put("Authorization", token);

            return headers;
        }

    };
    VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
  /*  RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    queue.add(stringRequest);*/

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



    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent = new Intent(Top_up_Money.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        onSaveInstanceState(new Bundle());
    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(Top_up_Money.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
                onBackPressed();
            }
        });

    }

}