package com.example.dealin.user.message;

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
public class MessageFragment extends Fragment {

    Spinner msg_type;
    ListView messages;
    View v;
    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_message, container, false);
        addWidgets();
        return v;
    }

    void addWidgets()
    {
        msg_type=(Spinner)v.findViewById(R.id.message_type_dropdown);
        messages=(ListView)v.findViewById(R.id.message_list);
    }
}
