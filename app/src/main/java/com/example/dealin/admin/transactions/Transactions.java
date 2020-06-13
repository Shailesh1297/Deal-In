package com.example.dealin.admin.transactions;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.login.User;
import com.example.dealin.user.orders.Order;
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

public class Transactions extends AppCompatActivity implements View.OnClickListener {

    ImageView back,location;
    TextView title;
    ArrayList<Order> transactions;
    ExpandableListView transactionsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        addActionBar();
        addWidgets();
        if(getTransactions())
        {
            TransactionAdapter ta=new TransactionAdapter(getApplicationContext(),transactions);
            transactionsList.setAdapter(ta);
        }
    }
    void addActionBar()
    {
        //actionbar customisation
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_back);
        View v=getSupportActionBar().getCustomView();
        back=(ImageView)v.findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        title=(TextView)v.findViewById(R.id.bar_title);
        title.setText("Transactions");
        location=(ImageView)v.findViewById(R.id.action_bar_location);
        location.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        if(view==back)
        {
            onBackPressed();
        }
    }

    private void addWidgets()
    {
        transactionsList=findViewById(R.id.transaction_list);
    }

    public boolean getTransactions()
    {

        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="transaction_list";
        try{
            HttpURLConnection conn= Connection.createConnection();
            //output
            OutputStream outputStream=conn.getOutputStream();
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            String dataEncode= URLEncoder.encode("page","UTF-8")+"="+URLEncoder.encode(page,"UTF-8")+
                    "&" + URLEncoder.encode("college_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(getCollegeId()), "UTF-8");
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

                transactions=new ArrayList<>();
                for(int i=0;i<length;i++)
                {
                    jsonObject=jsonArray.getJSONObject(i)  ;
                    Order od=new Order();
                    od.setOrderId(jsonObject.getInt("order_id"));
                    od.setPaymentMode(jsonObject.getString("mode"));
                    od.setDeal(jsonObject.getInt("deal"));
                    od.setItemName(jsonObject.getString("item_name"));
                    od.setItemPrice(jsonObject.getString("item_price"));
                    od.setItemCategory(jsonObject.getString("category"));
                    od.setItemId(jsonObject.getInt("item_id"));
                    od.setBuyerId(jsonObject.getInt("buyer_id"));
                    od.setSellerId(jsonObject.getInt("seller_id"));
                    od.setOrderedOn(jsonObject.getString("ordered_on"));
                    od.setDeliveredOn(jsonObject.getString("delivered_on"));
                    transactions.add(od);
                }
                conn.disconnect();
                return true;

            }

            conn.disconnect();

        }catch (Exception e)
        {
            Log.d("Transactions",e.toString());
        }

        return false;

    }

    private int getCollegeId()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("USER_KEY",0);
        String userJson=sharedPreferences.getString("user",null);
        if(userJson!=null)
        {
            Gson gson=new Gson();
            User user=gson.fromJson(userJson,User.class);
            return user.getCollegeid();

        }
        return 0;
    }
}
