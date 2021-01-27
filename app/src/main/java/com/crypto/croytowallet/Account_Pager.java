package com.crypto.croytowallet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crypto.croytowallet.Adapter.KnowlegeAdapter;


public class Account_Pager extends Fragment {
    RecyclerView recyclerView;


    public Account_Pager() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.account_pager, container, false);

        recyclerView=view.findViewById(R.id.Recyclerview_account);
        String[] data={"This is a india","This is a Pakistan","This is a india","This is a Pakistan","This is a india","This is a Pakistan"};
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        KnowlegeAdapter adapter = new KnowlegeAdapter(data);
        recyclerView.setAdapter(adapter);
        return view;
    }
}