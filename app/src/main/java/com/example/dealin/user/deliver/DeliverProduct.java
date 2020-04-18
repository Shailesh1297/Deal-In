package com.example.dealin.user.deliver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.dealin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliverProduct extends Fragment {

    Spinner delivery_types;
    ListView deliveries;
    View v;
    public DeliverProduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_deliver_product, container, false);
        addWidgets();
        return v;
    }

    void addWidgets()
    {
        delivery_types=(Spinner)v.findViewById(R.id.deliver_type_dropdown);
        deliveries=(ListView)v.findViewById(R.id.delivery_list);
    }
}
