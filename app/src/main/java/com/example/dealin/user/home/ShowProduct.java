package com.example.dealin.user.home;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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

public class ShowProduct extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextView product_title,product_price,product_category,product_description,title;
    ImageView product_thumbnail,location,back;
    EditText venue;
    Spinner paymentMode;
    Button buy;
    ArrayAdapter<String>paymentAdapter;
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        addwidgets();
        addActionBar();


    }

    @Override
    public void onStart()
    {
        super.onStart();
        Intent intent=getIntent();
        Gson gson=new Gson();
        String pdt=intent.getExtras().getString("product");
        Product product=gson.fromJson(pdt,Product.class);
        try{
            productId=product.getId();                         //intent.getExtras().getInt("id");
            String title=product.getTitle();                //intent.getExtras().getString("Title");
            String price=product.getPrice();                //intent.getExtras().getString("Price");
            String category=product.getCategory();          //intent.getExtras().getString("Category");
            String description=product.getDescription();    //intent.getExtras().getString("Description");
            Bitmap thumbnail=product.getThumbnail();      //intent.getExtras().getString("Thumbnail");

            product_title.setText(title);
            product_category.setText(category);
            product_price.setText("\u20B9"+price);
            product_description.setText(description);
            product_thumbnail.setImageBitmap(thumbnail);
        }catch (Exception e)
        {

        }

        //adding payment modes to spinner
        paymentAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMode.setAdapter(paymentAdapter);
        getPaymentsMethod();
        paymentMode.setOnItemSelectedListener(this);

    }

    public void addActionBar()
    {
        //actionbar customisation
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_back);
        View v=getSupportActionBar().getCustomView();
        location=(ImageView)v.findViewById(R.id.action_bar_location);
        location.setVisibility(View.INVISIBLE);
        title=(TextView)v.findViewById(R.id.bar_title);
        title.setVisibility(View.INVISIBLE);
        back=(ImageView)v.findViewById(R.id.action_bar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
    @Override
    public void onClick(View view) {
        if(view ==buy)
        {
            String v=venue.getText().toString();
            if(TextUtils.isEmpty(v))
            {
                venue.requestFocus();
                venue.setError("Field can't be empty");
            }else
            {
                String mode=paymentMode.getSelectedItem().toString();
                int userId=getUserId();
                if(buyProduct(productId,mode,userId,v))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setMessage("Product Ordered")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onBackPressed();
                                }
                            });
                    AlertDialog alert=builder.create();
                    alert.show();
                }
            }
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        paymentMode.setSelection(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }




    //buying products
    private boolean buyProduct(int item_id,String mode,int buyer_id,String venue)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="order";
        try {
            HttpURLConnection conn = Connection.createConnection();
            //output
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String dataEncode = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") +
                    "&" + URLEncoder.encode("item_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(item_id), "UTF-8") +
                    "&" + URLEncoder.encode("mode", "UTF-8") + "=" + URLEncoder.encode(mode, "UTF-8") +
                    "&" + URLEncoder.encode("buyer_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(buyer_id), "UTF-8")+
                    "&" + URLEncoder.encode("venue", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(venue), "UTF-8");
            bufferedWriter.write(dataEncode);
            bufferedWriter.close();
            outputStreamWriter.close();
            outputStream.close();
            //input
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            String dataDecode = stringBuilder.toString().trim();

            JSONObject jsonObject = new JSONObject(dataDecode);
            int flag = jsonObject.getInt("flag");
            conn.disconnect();
            if (flag == 1) return true;
        }
        catch (Exception e)
        {
            Log.d("Order Product",e.toString());
        }
        return false;
    }


    //getting payments methods
    private void getPaymentsMethod()
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="payments";
        try{
            HttpURLConnection conn= Connection.createConnection();
            //output
            OutputStream outputStream=conn.getOutputStream();
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            String dataEncode= URLEncoder.encode("page","UTF-8")+"="+URLEncoder.encode(page,"UTF-8");
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
            JSONArray jsonArray=new JSONArray(dataDecode);
            int length=jsonArray.length();
            JSONObject jsonObject=null;

            for(int i=0;i<length;i++)
            {
                jsonObject=jsonArray.getJSONObject(i);
                paymentAdapter.add(jsonObject.getString("mode"));
            }
            paymentAdapter.setNotifyOnChange(true);
            paymentMode.setSelection(0);
            conn.disconnect();
        }catch (Exception e)
        {
            Log.d("Payment-Mode",e.toString());
        }
    }

    private int getUserId()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("USER_KEY",0);
        String userJson=sharedPreferences.getString("user",null);
        if(userJson!=null)
        {
            Gson gson=new Gson();
            User user=gson.fromJson(userJson,User.class);
            return user.getUserid();
        }
        return 0;
    }
    void addwidgets()
    {
        product_title=findViewById(R.id.pdt_title);
        product_price=findViewById(R.id.pdt_price);
        product_category=findViewById(R.id.pdt_category);
        product_description=findViewById(R.id.pdt_description);
        product_thumbnail=findViewById(R.id.pdt_thumbnail);
        paymentMode=findViewById(R.id.payment_modes);
        paymentMode.setOnItemSelectedListener(this);
        venue=findViewById(R.id.deal_venue);
        buy=findViewById(R.id.buy_product);
        buy.setOnClickListener(this);
    }




}
