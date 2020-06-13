package com.example.dealin.admin.user;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.admin.colleges.College;
import com.example.dealin.connection.Connection;
import com.example.dealin.location.Location;
import com.example.dealin.login.User;
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

public class AdminUser extends AppCompatActivity implements View.OnClickListener {

    ImageView back,location;
    TextView title,totalUsers,usersLocation;
    ExpandableListView userList;
    EditText userSearch;
    Button search;
    ArrayList<User>users;
    String college=null;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);
        addActionBar();
        addWidgets();
    }

    @Override
    protected void onStart() {
        super.onStart();
        count=0;
        if(getUsers())
        {
            AdminUserAdapter aua=new AdminUserAdapter(getApplicationContext(),users);
            userList.setAdapter(aua);
            totalUsers.setText(""+count);
            if(!getCollege().equals(null))
            {
                usersLocation.setText(college);

            }

        }

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
        title=(TextView)v.findViewById(R.id.bar_title);
        title.setText("Users");
        location=(ImageView)v.findViewById(R.id.action_bar_location);
        location.setOnClickListener(this);
    }

@Override
    public void onClick(View v) {

        if (v == back)
        {
            onBackPressed();
        }

        if(v==location)
        {
            Intent intent=new Intent(getBaseContext(), Location.class);
            startActivity(intent);
        }
    }

    public boolean getUsers()
    {

        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="user_list";
        try{
            HttpURLConnection conn= Connection.createConnection();
            //output
            OutputStream outputStream=conn.getOutputStream();
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            String dataEncode= URLEncoder.encode("page","UTF-8")+"="+URLEncoder.encode(page,"UTF-8")+
                    "&" + URLEncoder.encode("college_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(getCollegeId()), "UTF-8") ;
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
                JSONArray jsonArray=jsonObject.getJSONArray("0");
                int length=jsonArray.length();

                users=new ArrayList<>();
                for(int i=0;i<length;i++)
                {
                    jsonObject=jsonArray.getJSONObject(i)  ;
                    User user=new User();
                   user.setUserid(jsonObject.getInt("user_id"));
                   user.setName(jsonObject.getString("name"));
                   user.setEmail(jsonObject.getString("email"));
                    user.setMobile(jsonObject.getLong("mobile"));
                    user.setIsActive(jsonObject.getInt("isactive"));
                    users.add(user);
                    count++;
                }
                conn.disconnect();
                return true;

            }

            conn.disconnect();

        }catch (Exception e)
        {
            Log.d("Admin Users",e.toString());
        }

        return false;

    }


    private void addWidgets()
    {
        totalUsers=findViewById(R.id.admin_total_user);
        usersLocation=findViewById(R.id.user_location);
        userSearch=findViewById(R.id.user_search);
        search=findViewById(R.id.user_search_button);
        userList=findViewById(R.id.user_list);

    }

    private int getCollegeId()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("USER_KEY",0);
        String userJson=sharedPreferences.getString("user",null);
        if(userJson!=null)
        {
            Gson gson=new Gson();
            User user=gson.fromJson(userJson,User.class);
            return user.getCollegeid();

        }
        return 0;
    }

    private String getCollege()
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
                    "&" + URLEncoder.encode("college_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(getCollegeId()), "UTF-8") ;
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
                college=jsonObject.getString("college_name");

            }

            conn.disconnect();
            return college;
        }catch (Exception e)
        {
            Log.d("User College",e.toString());
        }

        return null;
    }
}
