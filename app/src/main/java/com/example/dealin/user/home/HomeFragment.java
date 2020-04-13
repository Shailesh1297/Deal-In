package com.example.dealin.user.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dealin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    List<Product>productList;
    RecyclerView recyclerView;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        products();
        recyclerView=(RecyclerView)view.findViewById(R.id.home_recycler_view);
        RecyclerViewAdapter rva=new RecyclerViewAdapter(getActivity().getBaseContext(),productList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getBaseContext(),3));
        recyclerView.setAdapter(rva);
        return view;
    }

    public void products()
    {
        productList=new ArrayList<>();
        productList.add(new Product("Facebook","software","social networking","1599",R.drawable.facebook));
        productList.add(new Product("Youtube","website","video streaming","2599",R.drawable.youtube));
        productList.add(new Product("Twitter","website","text writing","999",R.drawable.twitter));
        productList.add(new Product("Instagram","software","photo sharing","1299",R.drawable.instagram));

    }
}
