package com.example.dealin.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealin.R;

public class Test extends AppCompatActivity {

    TextView title,menuTxt;
    ImageView back,bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        menuTxt=findViewById(R.id.menu_text_show);
    }

    public void addActionBar()
    {
        //actionbar customisation
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_back);
        View v=getSupportActionBar().getCustomView();
        bluetooth=(ImageView)v.findViewById(R.id.action_bar_location);
        bluetooth.setImageResource(R.drawable.ic_bluetooth_searching_black_24dp);
        title=(TextView)v.findViewById(R.id.bar_title);
        title.setText("Menu Options");
        back=(ImageView)v.findViewById(R.id.action_bar_back);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.sample_options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this,"Menu",Toast.LENGTH_SHORT).show();
        switch(item.getItemId())
        {
            case R.id.menu_item_1:
                break;
            case R.id.menu_item_2:
                break;
            case R.id.menu_item_3:
                break;
            default:


        }
        return super.onOptionsItemSelected(item);
    }
}
