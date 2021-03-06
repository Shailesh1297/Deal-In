package com.example.dealin.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.example.dealin.R;
import com.example.dealin.feedback.About;
import com.example.dealin.feedback.Suggestions;
import com.example.dealin.profile.Profile;
import com.example.dealin.updater.Update;
import com.example.dealin.user.product.AddProductFragment;
import com.example.dealin.user.home.HomeFragment;
import com.example.dealin.user.message.MessageFragment;
import com.example.dealin.user.orders.OrderFragment;
import com.example.dealin.user.deliver.DeliverProduct;

public class UserHome extends AppCompatActivity implements View.OnClickListener {

    ImageView userProfile,userLocation,home,orders,add,deliver,messages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        addActionBar();
        addWidgets();
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermission();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragments,new HomeFragment()).commit();

    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(UserHome.this,new String[]{Manifest.permission.CALL_PHONE},1);
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

        if(v==home || v==orders || v==add || v==deliver || v==messages)
        {
            Fragment selectedFragment=null;

            switch(v.getId())
            {
                case R.id.user_home:
                                    selectedFragment=new HomeFragment();
                                    break;
                case R.id.user_orders:
                                    selectedFragment=new OrderFragment();
                                    break;
                case R.id.product_add:
                                    selectedFragment=new AddProductFragment();
                                    break;
                case R.id.user_deliver:
                                    selectedFragment=new DeliverProduct();
                                    break;
                case R.id.user_messages:
                                    selectedFragment=new MessageFragment();
                                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragments,selectedFragment).commit();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent=null;
        switch(item.getItemId())
        {

            case R.id.suggestions:
                                intent=new Intent(UserHome.this, Suggestions.class);
                                startActivity(intent);

                break;
            case R.id.about_us:
                                 intent=new Intent(UserHome.this, About.class);
                                startActivity(intent);
                break;
            case R.id.updater:
                                    intent=new Intent(UserHome.this, Update.class);
                                    startActivity(intent);
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
