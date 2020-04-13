package com.example.dealin.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.example.dealin.user.product.AddProductFragment;
import com.example.dealin.user.home.HomeFragment;
import com.example.dealin.user.message.MessageFragment;
import com.example.dealin.user.orders.OrderFragment;
import com.example.dealin.user.deliver.DeliverProduct;

public class UserHome extends AppCompatActivity implements View.OnClickListener {

    ImageView userProfile,userLocation,home,orders,add,products,messages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        addActionBar();
        addWidgets();
        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragments,new HomeFragment()).commit();

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

        if(v==home || v==orders || v==add || v==products || v==messages)
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
        products=findViewById(R.id.user_deliver);
        messages=findViewById(R.id.user_messages);

        //listeners
        home.setOnClickListener(this);
        add.setOnClickListener(this);
        orders.setOnClickListener(this);
        products.setOnClickListener(this);
        messages.setOnClickListener(this);
    }

}
