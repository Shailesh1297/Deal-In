package com.example.dealin.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealin.R;
import com.example.dealin.profile.Profile;

public class UserHome extends AppCompatActivity implements View.OnClickListener {

    ImageView userProfile,userLocation,home,orders,add,deliver,messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        addActionBar();
        addWidgets();

    }
    public void addActionBar()
    {
        //actionbar customisation
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);//working
        View v=getSupportActionBar().getCustomView();
        userProfile=(ImageView)v.findViewById(R.id.profile);
        userLocation=(ImageView)v.findViewById(R.id.location);
        userLocation.setVisibility(View.INVISIBLE);
        userProfile.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        if(v==userProfile)
        {
            Intent intent=new Intent(this, Profile.class);
            startActivity(intent);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this,"Menu",Toast.LENGTH_SHORT).show();
        switch(item.getItemId())
        {
            case R.id.about_us:
                break;
            case R.id.settings:
                break;
            default:


        }
        return super.onOptionsItemSelected(item);
    }

    private void addWidgets()
    {
        //widgets
        home=findViewById(R.id.user_home);
        orders=findViewById(R.id.user_orders);
        add=findViewById(R.id.product_add);
        deliver=findViewById(R.id.user_deliver);
        messages=findViewById(R.id.user_messages);

        //listeners
        home.setOnClickListener(this);
        add.setOnClickListener(this);
        orders.setOnClickListener(this);
        deliver.setOnClickListener(this);
        messages.setOnClickListener(this);
    }

}
