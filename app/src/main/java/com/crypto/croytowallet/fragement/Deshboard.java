package com.crypto.croytowallet.fragement;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.crypto.croytowallet.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Deshboard extends Fragment {


    public Deshboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deshboard, container, false);
    }

}
