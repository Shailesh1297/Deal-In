package com.example.dealin.admin.colleges;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.dealin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class AddColleges extends AppCompatActivity implements View.OnClickListener {
    ImageView back;
    FloatingActionButton floatingAdd;
    PopupWindow popupWindow;
    Button add,close;
    EditText college,city;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_colleges);

       addActionBar();
        addWidgets();


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
    }

    public void onClick(View v)
    {
        if(v==back)
        {
            onBackPressed();
        }
        //floating button
        if(v==floatingAdd)
        {
            //pop window
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popup=layoutInflater.inflate(R.layout.add_college_popup,null);
            add=(Button)popup.findViewById(R.id.add_college);
            close=(Button)popup.findViewById(R.id.cancel_popup);
            college=(EditText)popup.findViewById(R.id.college_popup);
            city=(EditText)popup.findViewById(R.id.city_popup);
            popupWindow=new PopupWindow(popup, RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(relativeLayout, Gravity.CENTER,0,0);
            popupWindow.setFocusable(true);//keyboard can open
            popupWindow.update();
            add.setOnClickListener(this);
            close.setOnClickListener(this);
        }
        if(v==add)
        {
            popupWindow.dismiss();
        }

        if(v==close)
        {
           popupWindow.dismiss();
        }

    }
    public void addWidgets()
    {
        floatingAdd=findViewById(R.id.floatingActionButton);
        floatingAdd.setOnClickListener(this);
        relativeLayout=findViewById(R.id.relativelayout_1_add_college);
    }

    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
