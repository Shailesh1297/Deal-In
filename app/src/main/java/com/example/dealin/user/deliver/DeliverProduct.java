package com.example.dealin.user.deliver;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.login.User;
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
public class DeliverProduct extends Fragment  {

    Spinner delivery_types;
    ExpandableListView deliveries;
    View v;
    ArrayList<Deliver>deliver;
    public DeliverProduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_deliver_product, container, false);
        addWidgets();
        if(getDeliveries(getUserId()))
        {
            DeliverExpandableAdapter da=new DeliverExpandableAdapter(getActivity().getBaseContext(),deliver);
            deliveries.setAdapter(da);
        }

        return v;
    }


    public boolean getDeliveries(int user_id)
    {

        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="user_delivery";
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

                deliver=new ArrayList<>();
                for(int i=0;i<length;i++)
                {
                    jsonObject=jsonArray.getJSONObject(i)  ;
                    Deliver d=new Deliver();
                    d.setOrderId(jsonObject.getInt("order_id"));
                    d.setPayMode(jsonObject.getString("mode"));
                    d.setDeliveryStatus(jsonObject.getInt("deal"));
                    d.setDeliveryVenue(jsonObject.getString("venue"));
                    d.setItemName(jsonObject.getString("item_name"));
                    d.setItemPrice(jsonObject.getString("item_price"));
                    d.setItemCategory(jsonObject.getString("category"));
                    d.setItemDescription(jsonObject.getString("description"));
                    d.setBuyerName(jsonObject.getString("name"));
                    d.setBuyerEmail(jsonObject.getString("email"));
                    d.setBuyerMobile(jsonObject.getString("mobile"));
                    deliver.add(d);
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
        delivery_types=(Spinner)v.findViewById(R.id.deliver_type_dropdown);
        delivery_types.setVisibility(View.GONE);
        deliveries=(ExpandableListView)v.findViewById(R.id.delivery_list);

    }


}
