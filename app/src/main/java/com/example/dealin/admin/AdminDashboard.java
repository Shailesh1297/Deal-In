package com.example.dealin.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dealin.R;
import com.example.dealin.location.Location;
import com.example.dealin.profile.Profile;

public class AdminDashboard extends AppCompatActivity {

    ImageView adminProfile,adminLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        addWidgets();

        adminLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(), Location.class);
                startActivity(intent);
            }
        });

        adminProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(), Profile.class);
                startActivity(intent);

            }
        });

    }

    public void addWidgets()
    {
        adminProfile=findViewById(R.id.admin_profile_icon);
        adminLocation=findViewById(R.id.admin_location_icon);
    }
}
