package com.example.dealin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.admin.AdminDashboard;
import com.example.dealin.register.Register;
import com.example.dealin.user.UserHome;

public class Login extends AppCompatActivity {

    Button login;
    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addWidgets();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(getBaseContext(), AdminDashboard.class);
                Intent intent=new Intent(getBaseContext(), UserHome.class);
                startActivity(intent);
            }
        });
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
        login=findViewById(R.id.login_but);
        register=findViewById(R.id.don_t_have_);
    }
}
