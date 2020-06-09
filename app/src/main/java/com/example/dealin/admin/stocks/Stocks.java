package com.example.dealin.admin.stocks;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dealin.R;

public class Stocks extends AppCompatActivity implements View.OnClickListener{

    TextView title;
    ImageView back,location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);
        addActionBar();


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
        title.setText("Stocks");
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
}