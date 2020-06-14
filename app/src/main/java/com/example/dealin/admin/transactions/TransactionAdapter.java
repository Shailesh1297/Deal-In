package com.example.dealin.admin.transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.user.orders.Order;

import java.util.ArrayList;

public class TransactionAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<Order> transactions;

    public TransactionAdapter(Context context, ArrayList<Order> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public int getGroupCount() {
        return transactions.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return transactions.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return transactions.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        if(view==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.transaction_group_list,null);
        }
        TextView tId,pId,price;
        ImageView status;
        tId=(TextView)view.findViewById(R.id.transaction_order_id);
        pId=(TextView)view.findViewById(R.id.transaction_item_id);
        price=(TextView)view.findViewById(R.id.transaction_item_price);
        status=(ImageView)view.findViewById(R.id.item_delivery_status);
        tId.setText(""+transactions.get(i).getOrderId());
        pId.setText(""+transactions.get(i).getItemId());
        price.setText("\u20B9"+transactions.get(i).getItemPrice());
        if(transactions.get(i).getDeal()==1)
        {
            status.setImageResource(R.drawable.ic_delivered_24dp);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        if(view==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.transaction_list_item,null);
        }
        TextView itemName,itemId,sellerId,buyerId,payMode,category,odrOn,dvlOn;
        itemName=(TextView)view.findViewById(R.id.transaction_item_name);
        sellerId=(TextView)view.findViewById(R.id.transaction_seller_id);
        payMode=(TextView)view.findViewById(R.id.transaction_item_paymode);
        itemName=(TextView)view.findViewById(R.id.transaction_item_name);
        odrOn=(TextView)view.findViewById(R.id.transaction_ordered);
        itemId=(TextView)view.findViewById(R.id.transaction_item_id);
        buyerId=(TextView)view.findViewById(R.id.transaction_buyer_id);
        category=(TextView)view.findViewById(R.id.transaction_item_category);
        dvlOn=(TextView)view.findViewById(R.id.transaction_delivered);

        itemName.setText(transactions.get(i).getItemName());
        itemId.setText(""+transactions.get(i).getItemId());
        sellerId.setText(""+transactions.get(i).getSellerId());
        buyerId.setText(""+transactions.get(i).getBuyerId());
        payMode.setText(transactions.get(i).getPaymentMode());
        category.setText(transactions.get(i).getItemCategory());
        odrOn.setText(transactions.get(i).getOrderedOn());
        dvlOn.setText(transactions.get(i).getDeliveredOn());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
