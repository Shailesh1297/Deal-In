package com.example.dealin.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dealin.R;
import com.example.dealin.admin.colleges.AddColleges;
import com.example.dealin.location.Location;
import com.example.dealin.profile.Profile;

public class AdminDashboard extends AppCompatActivity implements View.OnClickListener {

    ImageView adminProfile,adminLocation,adminColleges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        //actionbar customisation
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);//working
        View v=getSupportActionBar().getCustomView();
        adminProfile=(ImageView)v.findViewById(R.id.profile);
        adminLocation=(ImageView)v.findViewById(R.id.location);
        addWidgets();



        adminLocation.setOnClickListener(this);
        adminProfile.setOnClickListener(this);
        adminColleges.setOnClickListener(this);

    }

    public void onClick(View v)
    {
        if(v==adminProfile)
        {
            Intent intent=new Intent(getBaseContext(), Profile.class);
            startActivity(intent);
        }

        if(v==adminLocation)
        {
            Intent intent=new Intent(getBaseContext(), Location.class);
            startActivity(intent);
        }
        if(v==adminColleges)
        {
            Intent intent=new Intent(getBaseContext(), AddColleges.class);
            startActivity(intent);
        }
    }

    public void addWidgets()
    {

        adminColleges=findViewById(R.id.add_colleges);
    }
}
