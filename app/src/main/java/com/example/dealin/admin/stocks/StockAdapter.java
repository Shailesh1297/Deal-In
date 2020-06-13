package com.example.dealin.admin.stocks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.user.home.Product;

import java.util.ArrayList;

public class StockAdapter  extends BaseAdapter {
    Context context;
    ArrayList<Product> products;

    public StockAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(R.layout.stock_item_list,null);
        TextView id,name,price,category;
        id=(TextView)view.findViewById(R.id.stock_item_id);
        name=(TextView)view.findViewById(R.id.stock_item_name);
        price=(TextView)view.findViewById(R.id.stock_item_price);
        category=(TextView)view.findViewById(R.id.stock_item_category);
        id.setText(""+products.get(i).getId());
        name.setText(products.get(i).getTitle());
        price.setText(products.get(i).getPrice());
        category.setText(products.get(i).getCategory());
        return view;
    }
}
