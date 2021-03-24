package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.database.RetrofitCountryName;
import com.crypto.croytowallet.database.RetrofitGraph;
import com.crypto.croytowallet.signup.SignUp;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kyc extends AppCompatActivity {
    Spinner countryNameSpinner;
    String countryName;
    KProgressHUD progressDialog;
    ArrayList<String> coun_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);
        countryNameSpinner = findViewById(R.id.CountryNameSpinner);

        coun_name = new ArrayList<String>();

        getCountryName();
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onSaveInstanceState(new Bundle());
    }

    public void getCountryName() {

        progressDialog = KProgressHUD.create(Kyc.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody>call = RetrofitCountryName.getInstance().getApiCountryName().getCountryname();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s=null;
                hidepDialog();

                if (response.isSuccessful()){
                    try {
                        s = response.body().string();

                        JSONArray jsonArray = new JSONArray(s);
                        for (int i =0; i<=jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            String name = object.getString("name");
                            coun_name.add(name);

                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayAdapter<String> adp = new ArrayAdapter<String> (Kyc.this,android.R.layout.simple_spinner_dropdown_item,coun_name);
                    countryNameSpinner.setAdapter(adp);

                    countryNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            countryName  = parent.getItemAtPosition(position).toString();
                            Toast.makeText(parent.getContext(), countryName, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }else {
                    Snacky.builder()
                            .setActivity(Kyc.this)
                            .setText("Api not is working")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
          hidepDialog();

                Snacky.builder()
                        .setActivity(Kyc.this)
                        .setText("Internet problem")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
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

}