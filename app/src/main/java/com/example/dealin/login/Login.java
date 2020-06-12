package com.example.dealin.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealin.R;
import com.example.dealin.admin.AdminDashboard;
import com.example.dealin.connection.Connection;
import com.example.dealin.register.Register;
import com.example.dealin.user.UserHome;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class Login extends AppCompatActivity {

    Button login;
    TextView register,forgetPass;
    EditText email,password;
    boolean eye=false;
    private int userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //strict mode
        StrictMode.ThreadPolicy threadPolicy=new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(threadPolicy);
        addWidgets();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailTxt,passwordTxt;
                emailTxt=email.getText().toString();
                passwordTxt=password.getText().toString();
                if(TextUtils.isEmpty(emailTxt))
                {
                    email.requestFocus();
                    email.setError("Field can't be empty");
                }else if(TextUtils.isEmpty(passwordTxt))
                {
                    password.requestFocus();
                    password.setError("Field can't be empty");
                }else{

                    if(loginUser(emailTxt,passwordTxt))
                    {
                        Intent intent=null;
                        if(userType==0)
                        {
                            intent=new Intent(getBaseContext(), UserHome.class);


                        }else if(userType==1)
                        {
                             intent=new Intent(getBaseContext(), AdminDashboard.class);

                        }
                        // intent=new Intent(getBaseContext(), Test.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Login Sucessful",Toast.LENGTH_LONG).show();

                    }else
                    {
                        Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(), Register.class);
                startActivity(intent);
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(), ForgetPassword.class);
                startActivity(intent);
            }
        });
    }

    boolean loginUser(String email,String password)
    {

        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="login";
        try{
            HttpURLConnection conn= Connection.createConnection();
            //output
            OutputStream outputStream=conn.getOutputStream();
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            String dataEncode= URLEncoder.encode("page","UTF-8")+"="+URLEncoder.encode(page,"UTF-8")+
                    "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+
                    "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            bufferedWriter.write(dataEncode);
            bufferedWriter.close();
            outputStreamWriter.close();
            outputStream.close();

            //input
            InputStream inputStream=conn.getInputStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"UTF-8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            while((line=bufferedReader.readLine())!=null)
            {
                stringBuilder.append(line+"\n");
            }
            String dataDecode=stringBuilder.toString().trim();
            JSONObject jsonObject=new JSONObject(dataDecode);

          //  JSONObject jsonData=jsonObject.getJSONObject("flag");
            int flag=jsonObject.getInt("flag");
                if(flag==1)
                {
                    JSONObject jsonData=jsonObject.getJSONObject("0");
                    int usertype=jsonData.getInt("user_type");
                    int isActive=jsonData.getInt("isActive");

                    //saving user data
                    User user=new User();
                    user.setUserid(jsonData.getInt("user_id"));
                    user.setName(jsonData.getString("name"));
                    user.setEmail(jsonData.getString("email"));
                    user.setPassword(jsonData.getString("password"));
                    user.setCollegeid(jsonData.getInt("college_id"));
                    user.setUsertype(usertype);
                    user.setMobile(jsonData.getLong("mobile"));
                    //saving data to preference
                    savePreference(user);
                    if(isActive==0)
                    {
                        userType=usertype;
                        conn.disconnect();
                        return true;
                    }
                }

            conn.disconnect();

        }catch (Exception e)
        {
            Log.d("Login",e.toString());
        }
        return false;
    }

    private void savePreference(User user)
    {
        Gson gson=new Gson();
        String userPref=gson.toJson(user,User.class);

        SharedPreferences sharedPreferences=getSharedPreferences("USER_KEY",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("user",userPref);
        editor.putInt("flag",1);
        editor.commit();


    }

    void addWidgets()
    {
        login=findViewById(R.id.login_but);
        register=findViewById(R.id.don_t_have_);
        email=findViewById(R.id.login_email_input);
        password=findViewById(R.id.login_password_input);
        forgetPass=findViewById(R.id.forget_pass);
        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                final int DRAWABLE_RIGHT = 2;

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (email.getRight() - email.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        //Here is your code when you click drawable right
                        email.setText("");
                        return true;
                    }

                }
                return false;
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                final int DRAWABLE_RIGHT = 2;

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        //Here is your code when you click drawable right
                        if(!eye)
                        {
                            password.setTransformationMethod(null);
                            eye=true;
                        }else
                        {
                            password.setTransformationMethod(new PasswordTransformationMethod());
                            eye=false;
                        }
                        return true;
                    }

                }
                return false;
            }
        });

    }


}
