package com.example.dealin.user.product;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dealin.R;

public class ShowProduct extends AppCompatActivity {

    TextView product_title,product_price,product_category,product_description;
    ImageView product_thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        addwidgets();
        Intent intent=getIntent();
        String title=intent.getExtras().getString("Title");
        String price=intent.getExtras().getString("Price");
        String category=intent.getExtras().getString("Category");
        String description=intent.getExtras().getString("Description");
        int thumbnail=intent.getExtras().getInt("Thumbnail");

        product_title.setText(title);
        product_category.setText(category);
        product_price.setText(price);
        product_description.setText(description);
        product_thumbnail.setImageResource(thumbnail);
    }

    void addwidgets()
    {
        product_title=findViewById(R.id.pdt_title);
        product_price=findViewById(R.id.pdt_price);
        product_category=findViewById(R.id.pdt_category);
        product_description=findViewById(R.id.pdt_description);
        product_thumbnail=findViewById(R.id.pdt_thumbnail);
    }
}
