package com.example.dealin.admin.colleges;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dealin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class AddColleges extends AppCompatActivity implements View.OnClickListener {
    ImageView back;
    FloatingActionButton floatingAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_colleges);

        //actionbar customisation
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_back);
        View v=getSupportActionBar().getCustomView();
        back=(ImageView)v.findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        addWidgets();



    }

    public void onClick(View v)
    {
        if(v==back)
        {
            onBackPressed();
        }
        if(v==floatingAdd)
        {
          Toast.makeText(getApplicationContext(),"Floating Button",Toast.LENGTH_LONG).show();
        }

    }
    public void addWidgets()
    {
        floatingAdd=findViewById(R.id.floatingActionButton);
        floatingAdd.setOnClickListener(this);
    }

    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
