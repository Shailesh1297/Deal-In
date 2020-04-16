package com.example.dealin.user.product;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.dealin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends Fragment implements View.OnClickListener {

    EditText pdt_name,pdt_price,pdt_description;
    Spinner pdt_category;
    ImageView pdt_img;
    Button save;
    View v;

    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_add_product, container, false);
        addwidgets();
        return v;
    }

    public void addwidgets()
    {
        pdt_name=(EditText)v.findViewById(R.id.add_product_name);
        pdt_price=(EditText)v.findViewById(R.id.add_product_price);
        pdt_category=(Spinner)v.findViewById(R.id.add_product_category);
        pdt_description=(EditText)v.findViewById(R.id.add_product_description);
        pdt_img=(ImageView)v.findViewById(R.id.add_product_img);
        save=(Button)v.findViewById(R.id.add_product);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
