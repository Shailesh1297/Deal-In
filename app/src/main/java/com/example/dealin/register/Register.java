package com.example.dealin.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.login.Login;

public class Register extends AppCompatActivity {

    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addWidgets();

        ////when already have an account is pressed
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });
    }

    //when already have an account is pressed
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    void addWidgets()
    {
        login=findViewById(R.id.already_have_account);
    }
}
