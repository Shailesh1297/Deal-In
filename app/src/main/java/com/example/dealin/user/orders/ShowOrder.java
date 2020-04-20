package com.example.dealin.user.orders;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.user.home.Product;
import com.google.gson.Gson;

public class ShowOrder extends AppCompatActivity implements View.OnClickListener {

    TextView orderName,orderCategory,orderDescription,orderPayMode,orderAmount,orderVenue,sellerName,sellerEmail,sellerPhone,title;
    ImageView orderImage,location,back;
    Button call;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        addWidgets();
        addActionBar();

        Intent intent=getIntent();
        Gson gson=new Gson();
        String od=intent.getExtras().getString("order");
        Order order=gson.fromJson(od,Order.class);
        try{
            orderId=order.getOrderId();
            orderName.setText(order.getItemName());
            orderCategory.setText(order.getItemCategory());
            orderDescription.setText(order.getItemDescription());
            orderPayMode.setText(order.getPaymentMode());
            orderAmount.setText(order.getItemPrice());
            orderVenue.setText(order.getVenue());
            sellerName.setText(order.getSellerName());
            sellerEmail.setText(order.getSellerEmail());
            sellerPhone.setText(order.getSellerMobile());
            orderImage.setImageBitmap(order.getItemImage());

        }catch (Exception e)
        {

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
      Intent intentCall=new Intent(Intent.ACTION_CALL);
      String phno=sellerPhone.getText().toString();
      if(!phno.trim().isEmpty())
      {
          intentCall.setData(Uri.parse("tel:"+phno));
      }
      if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
      {
          requestPermission();
      }else
      {
          startActivity(intentCall);
      }
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(ShowOrder.this,new String[]{Manifest.permission.CALL_PHONE},1);
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

    private void addWidgets()
    {
        orderName=findViewById(R.id.order_name);
        orderCategory=findViewById(R.id.order_category);
        orderDescription=findViewById(R.id.order_description);
        orderPayMode=findViewById(R.id.order_paymode);
        orderAmount=findViewById(R.id.order_price);
        orderVenue=findViewById(R.id.order_venue);
        sellerName=findViewById(R.id.order_seller_name);
        sellerEmail=findViewById(R.id.order_seller_email);
        sellerPhone=findViewById(R.id.order_seller_phone);
        orderImage=findViewById(R.id.order_image);
        call=findViewById(R.id.call_seller);
        call.setOnClickListener(this);
    }


}
