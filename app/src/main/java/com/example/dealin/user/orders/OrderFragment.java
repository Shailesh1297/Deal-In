package com.example.dealin.user.orders;

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
public class OrderFragment extends Fragment {

    Spinner order_type;
    ListView orders;
    View v;
    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_order, container, false);
        return v;
    }

    void addWidgets()
    {
        order_type=(Spinner)v.findViewById(R.id.orders_type_dropdown);
        orders=(ListView)v.findViewById(R.id.order_list);
    }
}
