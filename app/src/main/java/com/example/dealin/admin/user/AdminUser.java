package com.example.dealin.admin.user;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dealin.R;
import com.example.dealin.location.Location;

public class AdminUser extends AppCompatActivity implements View.OnClickListener {

    ImageView back,location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);
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

        location=(ImageView)v.findViewById(R.id.action_bar_location);
        location.setOnClickListener(this);
    }

@Override
    public void onClick(View v) {

        if (v == back)
        {
            onBackPressed();
        }

        if(v==location)
        {
            Intent intent=new Intent(getBaseContext(), Location.class);
            startActivity(intent);
        }
    }
}
