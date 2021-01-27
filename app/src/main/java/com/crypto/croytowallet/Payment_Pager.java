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

public class Payment_Pager extends Fragment {

    RecyclerView recyclerView;

    public Payment_Pager() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_payment__pager, container, false);
        // Inflate the layout for this fragment
        recyclerView=view.findViewById(R.id.Recyclerview_payment);
        String[] data={"This is a india","This is a Pakistan","This is a india","This is a Pakistan","This is a india","This is a Pakistan"};
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        KnowlegeAdapter adapter = new KnowlegeAdapter(data);
        recyclerView.setAdapter(adapter);
        return view;
    }
}