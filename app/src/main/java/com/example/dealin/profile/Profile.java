package com.example.dealin.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.login.Login;
import com.example.dealin.login.User;
import com.example.dealin.user.home.Product;
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
import java.util.ArrayList;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    TextView txtName,txtCollege,txtCity;
    EditText userName,userEmail,userPhone,userOldPassword,userNewPassword;
    Button update,change,logout;
    private int userId,collegeId,userType;
    private long mobile;
    private String userCollege,userCity,email,password,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        StrictMode.ThreadPolicy threadPolicy=new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(threadPolicy);
        addWidgets();
        getUser();
        getCollege(collegeId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        txtName.setText(name);
        txtCollege.setText(userCollege);
        txtCity.setText(userCity);
        userName.setText(name);
        userEmail.setText(email);
        userPhone.setText(String.valueOf(mobile));

    }

    @Override
    public void onClick(View view) {
        //updating profile
        if(view==update)
        {
           String name,email,phone;
           name=userName.getText().toString();
           email=userEmail.getText().toString();
           phone=userPhone.getText().toString();

           if(TextUtils.isEmpty(name))
           {
               userName.requestFocus();
               userName.setError("Field can't be empty");

           }else if(TextUtils.isEmpty(email))
           {
               userEmail.requestFocus();
               userEmail.setError("Field can't be empty");
           }else if(TextUtils.isEmpty(phone))
           {
               userPhone.requestFocus();
               userPhone.setError("Field can't be empty");

           }else
               {

                       if(updateProfile(userId,name,email,phone))
                       {
                           Toast.makeText(this,"Profile Updated",Toast.LENGTH_SHORT).show();
                           setUser(userName.getText().toString(),userEmail.getText().toString(),Long.parseLong(userPhone.getText().toString()));

                       }else
                       {
                           Toast.makeText(this,"Something Wrong",Toast.LENGTH_SHORT).show();
                       }

                 }
        }

        //changing password
        if(view==change)
        {
            String old,nova;
               old=userOldPassword.getText().toString();

                   nova=userNewPassword.getText().toString();
                   if(TextUtils.isEmpty(old))
                   {
                       userOldPassword.requestFocus();
                       userOldPassword.setError("Field can't be empty");

                   }else if(TextUtils.isEmpty(nova))
                   {
                       userNewPassword.requestFocus();
                       userNewPassword.setError("Field can't be empty");

                   }else if(!password.equals(old)){
                       Toast.makeText(this,"Old password doesn't match",Toast.LENGTH_SHORT).show();

                   } else if(old.equals(nova))
                   {
                       Toast.makeText(this,"Old and New passwords are same?",Toast.LENGTH_SHORT).show();
                   }else
                   {
                       if(updatePassword(userId,old,nova))
                       {
                           password=userNewPassword.getText().toString();
                           userOldPassword.setText("");
                           userNewPassword.setText("");
                           setUser(name,email,mobile);
                           Toast.makeText(this,"Password Changed",Toast.LENGTH_SHORT).show();
                       }else
                       {
                           Toast.makeText(this,"Password NOT Changed",Toast.LENGTH_SHORT).show();
                       }

                   }





        }

        //logout
        if(view==logout)
        {

            AlertDialog.Builder alert=new AlertDialog.Builder(Profile.this);
            alert.setTitle("Are You Sure?");
            alert.setMessage("Confirm to Logout");
            alert.setCancelable(false);
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent =new Intent(Profile.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog ad=alert.create();
            ad.show();

        }

    }

    //updating password
    private boolean updatePassword(int user_id,String oldpass,String newpass)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="password_update";
        try {
            HttpURLConnection conn = Connection.createConnection();
            //output
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String dataEncode = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") +
                    "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(user_id), "UTF-8")+
                    "&" + URLEncoder.encode("newpass", "UTF-8") + "=" + URLEncoder.encode(newpass, "UTF-8");
            bufferedWriter.write(dataEncode);
            bufferedWriter.close();
            outputStreamWriter.close();
            outputStream.close();
            //input
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            String dataDecode = stringBuilder.toString().trim();

            JSONObject jsonObject = new JSONObject(dataDecode);
            int flag = jsonObject.getInt("flag");
            Log.d("flagData", "" + flag);
            conn.disconnect();
            if (flag == 1) return true;
        }
        catch (Exception e)
        {
            Log.d("Update Password",e.toString());
        }
        return false;
    }



    private boolean updateProfile(int user_id,String name,String email,String phone)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="user_update";
        try {
            HttpURLConnection conn = Connection.createConnection();
            //output
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String dataEncode = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") +
                    "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(user_id), "UTF-8") +
                    "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") +
                    "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") +
                    "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") ;
            bufferedWriter.write(dataEncode);
            bufferedWriter.close();
            outputStreamWriter.close();
            outputStream.close();
            //input
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            String dataDecode = stringBuilder.toString().trim();

            JSONObject jsonObject = new JSONObject(dataDecode);
            int flag = jsonObject.getInt("flag");
            Log.d("flagData", "" + flag);
            conn.disconnect();
            if (flag == 1) return true;
        }
        catch (Exception e)
        {
            Log.d("Update Profile",e.toString());
        }

        return false;
    }


    private void getCollege(int college_id)
    {

        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="college";
        try{
            HttpURLConnection conn= Connection.createConnection();
            //output
            OutputStream outputStream=conn.getOutputStream();
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            String dataEncode= URLEncoder.encode("page","UTF-8")+"="+URLEncoder.encode(page,"UTF-8")+
                    "&" + URLEncoder.encode("college_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(college_id), "UTF-8");
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
            JSONArray jsonArray=new JSONArray(dataDecode);
            int length=jsonArray.length();
            JSONObject jsonObject=null;

            for(int i=0;i<length;i++)
            {
                jsonObject=jsonArray.getJSONObject(i);
                userCollege=jsonObject.getString("college_name");
                userCity=jsonObject.getString("city");
            }

            conn.disconnect();
        }catch (Exception e)
        {
            Log.d("User College",e.toString());
        }

    }

    void addWidgets()
    {
        txtName=findViewById(R.id.profile_name_text);
        txtCollege=findViewById(R.id.profile_college_text);
        txtCity=findViewById(R.id.profile_city_txt);
        userName=findViewById(R.id.profile_name);
        userEmail=findViewById(R.id.profile_email);
        userPhone=findViewById(R.id.profile_phone);
        userOldPassword=findViewById(R.id.old_password);
        userNewPassword=findViewById(R.id.new_password);
        update=findViewById(R.id.profile_update);
        update.setOnClickListener(this);
        change=findViewById(R.id.password_reset);
        change.setOnClickListener(this);
        logout=findViewById(R.id.profile_logout);
        logout.setOnClickListener(this);



    }

    private void setUser(String name,String email,long phone)
    {
        User userOb=new User(userId,name,password,email,collegeId,phone,userType);
        Gson gson=new Gson();
        String user=gson.toJson(userOb, User.class);
        SharedPreferences sharedPreferences=getSharedPreferences("USER_KEY",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("user",user);
        editor.commit();

    }

    private void getUser()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("USER_KEY",0);
        String userJson=sharedPreferences.getString("user",null);
        if(userJson!=null)
        {
            Gson gson=new Gson();
            User user=gson.fromJson(userJson,User.class);
            userId=user.getUserid();
            collegeId=user.getCollegeid();
            mobile=user.getMobile();
            email=user.getEmail();
            password=user.getPassword();
            name=user.getName();
            userType=user.getUsertype();
        }

    }


}
