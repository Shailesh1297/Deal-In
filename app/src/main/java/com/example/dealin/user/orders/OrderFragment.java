package com.example.dealin.user.orders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.login.User;
import com.example.dealin.user.home.Product;
import com.example.dealin.user.home.ShowProduct;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

    Spinner order_type;
    ListView orders;
    View v;
    ArrayList<Order> order;
    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_order, container, false);
        addWidgets();
        if(getOrders(getUserId()))
        {
            OrderAdapter odap=new OrderAdapter(getActivity().getApplicationContext(),order);
            orders.setAdapter(odap);
        }

        return v;
    }



    public boolean getOrders(int user_id)
    {

        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="user_orders";
        try{
            HttpURLConnection conn= Connection.createConnection();
            //output
            OutputStream outputStream=conn.getOutputStream();
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            String dataEncode= URLEncoder.encode("page","UTF-8")+"="+URLEncoder.encode(page,"UTF-8")+
                    "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(user_id), "UTF-8");
            bufferedWriter.write(dataEncode);
            bufferedWriter.close();
            outputStreamWriter.close();
            outputStream.close();

            //input
            InputStream inputStream=conn.getInputStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"UTF-8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            while((line=bufferedReader.readLine())!=null)
            {
                stringBuilder.append(line+"\n");
            }
            String dataDecode=stringBuilder.toString().trim();
            JSONObject jsonObject=new JSONObject(dataDecode);

            //  JSONObject jsonData=jsonObject.getJSONObject("flag");
            int flag=jsonObject.getInt("flag");
            if(flag==1)
            {
                JSONArray jsonArray=jsonObject.getJSONArray("0");
                int length=jsonArray.length();

                order=new ArrayList<>();
                for(int i=0;i<length;i++)
                {
                    jsonObject=jsonArray.getJSONObject(i)  ;
                    Order od=new Order();
                    od.setOrderId(jsonObject.getInt("order_id"));
                    od.setPaymentMode(jsonObject.getString("mode"));
                    od.setDeal(jsonObject.getInt("deal"));
                    od.setVenue(jsonObject.getString("venue"));
                    od.setItemName(jsonObject.getString("item_name"));
                    od.setItemPrice(jsonObject.getString("item_price"));
                    od.setItemCategory(jsonObject.getString("category"));
                    od.setItemDescription(jsonObject.getString("description"));
                    od.setItemImage(jsonObject.getString("image"));
                    od.setSellerName(jsonObject.getString("name"));
                    od.setSellerEmail(jsonObject.getString("email"));
                    od.setSellerMobile(jsonObject.getString("mobile"));
                    order.add(od);
                }
                conn.disconnect();
                return true;

            }

            conn.disconnect();

        }catch (Exception e)
        {
            Log.d("Order",e.toString());
        }

        return false;

    }
    private int getUserId()
    {
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("USER_KEY",0);
        String userJson=sharedPreferences.getString("user",null);
        if(userJson!=null)
        {
            Gson gson=new Gson();
            User user=gson.fromJson(userJson,User.class);
            return user.getUserid();
        }
        return 0;
    }

    void addWidgets()
    {
        order_type=(Spinner)v.findViewById(R.id.orders_type_dropdown);
        orders=(ListView)v.findViewById(R.id.order_list);
    }
}
