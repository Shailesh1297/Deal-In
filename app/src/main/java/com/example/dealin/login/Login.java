package com.example.dealin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.register.Register;

public class Login extends AppCompatActivity {

    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addWidgets();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(), Register.class);
                startActivity(intent);
            }
        });
    }

    void addWidgets()
    {
        register=findViewById(R.id.don_t_have_);
    }
}
