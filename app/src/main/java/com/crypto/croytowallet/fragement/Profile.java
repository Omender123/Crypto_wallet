
package com.crypto.croytowallet.fragement;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.crypto.croytowallet.Activity.MyReferral_code;
import com.crypto.croytowallet.Activity.Security;
import com.crypto.croytowallet.Activity.Setting;
import com.crypto.croytowallet.Activity.Support;
import com.crypto.croytowallet.Activity.Threat_Mode;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    CardView security, setting, support, threat_mode,referral_code1,language;
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
        referral_code1 = view.findViewById(R.id.referral_code1);
        language  = view.findViewById(R.id.langauge1);

        //animation
        down = AnimationUtils.loadAnimation(getContext(), R.anim.silde_down);
        blink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        right = AnimationUtils.loadAnimation(getContext(), R.anim.silde_up);
        left = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        loadLocale();

        setting.startAnimation(right);
        support.startAnimation(right);
        security.startAnimation(right);
        share.startAnimation(right);
        get.startAnimation(right);
        send.startAnimation(right);
        threat_mode.startAnimation(right);
        referral_code1.startAnimation(right);
        language.startAnimation(right);

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
                threat_mode.startAnimation(blink);
                resendOTP();

            }
        });
        referral_code1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MyReferral_code.class));
                referral_code1.startAnimation(blink);

            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialod();
            }
        });
        return view;
    }

    private void showChangeLanguageDialod() {
        final  String[] listItem={"English","Hindi","Japanese","ThaiLand","Chinese","Philippines"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Language.......");

        builder.setSingleChoiceItems(listItem, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i==0){
                    setLocale("en");
                    getActivity().recreate();
                }else if(i==1){
                    setLocale("hi");
                    getActivity().recreate();
                }else if(i==2){
                    setLocale("ja");
                    getActivity().recreate();
                }else if(i==3){
                    setLocale("th");
                    getActivity().recreate();
                }else if(i==4){
                    setLocale("zh");
                    getActivity().recreate();
                }else if(i==5){
                    setLocale("phi");
                    getActivity().recreate();
                }

                dialog.dismiss();
            }
        });

        AlertDialog alertDialog =builder.create();
        alertDialog.show();
    }

    private void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration configuration= new Configuration();
        configuration.locale=locale;
        getActivity().getBaseContext().getResources().updateConfiguration(configuration,getActivity().getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();

    }
    // load lanage saved in sharedPreference

    public void loadLocale(){
        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String Language = sharedPreferences.getString("My_Lang","");
        setLocale(Language);
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

                    startActivity(new Intent(getContext(), Threat_Mode.class));
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
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }

}
