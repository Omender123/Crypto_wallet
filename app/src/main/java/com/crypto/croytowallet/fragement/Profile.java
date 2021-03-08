
package com.crypto.croytowallet.fragement;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.crypto.croytowallet.Activity.Security;
import com.crypto.croytowallet.Activity.Setting;
import com.crypto.croytowallet.Activity.Support;
import com.crypto.croytowallet.Activity.Threat_Mode;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    CardView security, setting, support, threat_mode;
    LinearLayout profile;
    Animation down, blink, right, left;
    ImageView share;
    TextView get, send;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile = view.findViewById(R.id.profile1);
        security = view.findViewById(R.id.security1);
        setting = view.findViewById(R.id.setting1);
        support = view.findViewById(R.id.support1);
        share = view.findViewById(R.id.share1);
        get = view.findViewById(R.id.get);
        send = view.findViewById(R.id.send);
        threat_mode = view.findViewById(R.id.threat_mode);

        //animation
        down = AnimationUtils.loadAnimation(getContext(), R.anim.silde_down);
        blink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        right = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        left = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);

        //set animation
        // profile.startAnimation(right);
       /* setting.startAnimation(left);
        support.startAnimation(left);
        security.startAnimation(left);
        share.startAnimation(left);
        get.startAnimation(left);
        send.startAnimation(left);*/

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Setting.class));
                setting.startAnimation(blink);
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Support.class));
                support.startAnimation(blink);
            }
        });
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Security.class));
                security.startAnimation(blink);
            }
        });

        threat_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Threat_Mode.class));
                threat_mode.startAnimation(blink);
               // resendOTP();

            }
        });

        return view;
    }


    public void resendOTP() {
        UserData userData = SharedPrefManager.getInstance(getContext()).getUser();
        String usernames = userData.getUsername();


        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().sendOtp(usernames);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = null;
                if (response.code() == 200) {

                    Toast.makeText(getContext(), "Otp send in your register Email", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 400) {
                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(getActivity())
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snacky.builder()
                        .setActivity(getActivity())
                        .setText("Please Check Your Internet Connection")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }

}
