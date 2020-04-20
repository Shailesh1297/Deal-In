package com.example.dealin.user.orders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.user.home.Product;
import com.example.dealin.user.home.ShowProduct;
import com.google.gson.Gson;

import java.util.ArrayList;

public class OrderAdapter extends BaseAdapter {

    LinearLayout ll;
    ArrayList<Order>orders;
    Context context;
    LayoutInflater layoutInflater;

    OrderAdapter(Context context, ArrayList<Order> orders)
    {
        this.orders=orders;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view=layoutInflater.inflate(R.layout.order_item,null);
        ll=(LinearLayout)view.findViewById(R.id.order_item_layout);

        TextView orderId,orderName,orderAmount;
        ImageView orderStatus;
        orderId=(TextView)view.findViewById(R.id.product_id);
        orderName=(TextView)view.findViewById(R.id.product_name);
        orderAmount=(TextView)view.findViewById(R.id.product_price);
        orderStatus=(ImageView)view.findViewById(R.id.product_delivery_status);
        orderId.setText(String.valueOf(orders.get(i).getOrderId()));
        orderName.setText(orders.get(i).getItemName());
        orderAmount.setText(orders.get(i).getItemPrice());
                if(orders.get(i).getDeal()==0)
                {
                    orderStatus.setImageResource(R.drawable.ic_undelivered_24dp);
                }else {
                    orderStatus.setImageResource(R.drawable.ic_delivered_24dp);
                }

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, ShowOrder.class);

                    Gson gson=new Gson();
                    String od=gson.toJson(orders.get(i), Order.class);
                    intent.putExtra("order",od);
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
