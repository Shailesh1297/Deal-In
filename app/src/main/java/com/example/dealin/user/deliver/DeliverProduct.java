package com.example.dealin.user.deliver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dealin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliverProduct extends Fragment {

    public DeliverProduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deliver_product, container, false);
    }
}