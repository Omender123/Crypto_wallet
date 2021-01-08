
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

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.crypto.croytowallet.Activity.Security;
import com.crypto.croytowallet.Activity.Setting;
import com.crypto.croytowallet.Activity.Support;
import com.crypto.croytowallet.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
CardView security,setting,support;
LinearLayout profile;
Animation down,blink,right,left;
ImageView share;
TextView get,send;
    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        profile=view.findViewById(R.id.profile1);
        security=view.findViewById(R.id.security1);
        setting=view.findViewById(R.id.setting1);
        support=view.findViewById(R.id.support1);
        share=view.findViewById(R.id.share1);
        get=view.findViewById(R.id.get);
        send=view.findViewById(R.id.send);

        //animation
        down = AnimationUtils.loadAnimation(getContext(),R.anim.silde_down);
        blink = AnimationUtils.loadAnimation(getContext(),R.anim.blink);
        right = AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_right);
        left = AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_left);

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
                startActivity(new Intent(getContext(),Setting.class));
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

       return view; }



}
