package com.example.dealin.user.product;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.user.home.Product;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UserProductAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Product> userProduct;

    public UserProductAdapter(Context context, ArrayList<Product> userProduct) {
        this.context = context;
        this.userProduct = userProduct;
        layoutInflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return userProduct.size();
    }

    @Override
    public Object getItem(int i) {
        return userProduct.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view=layoutInflater.inflate(R.layout.user_product_list,null);
        TextView pdt_name,pdt_category,pdt_price;
        LinearLayout ll=(LinearLayout)view.findViewById(R.id.user_product_head);
        pdt_name=(TextView)view.findViewById(R.id.user_product_name);
        pdt_category=(TextView)view.findViewById(R.id.user_product_category);
        pdt_price=(TextView)view.findViewById(R.id.user_product_price);

        pdt_name.setText(userProduct.get(i).getTitle());
        pdt_category.setText(userProduct.get(i).getCategory());
        pdt_price.setText(userProduct.get(i).getPrice());
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, UserProduct.class);
                    Gson gson=new Gson();
                    String pdt=gson.toJson(userProduct.get(i),Product.class);
                    intent.putExtra("product",pdt);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }catch (Exception e)
                {

                }
            }
        });

        return view;
    }
}
