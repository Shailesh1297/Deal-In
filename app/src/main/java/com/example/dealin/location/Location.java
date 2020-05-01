package com.example.dealin.location;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.login.User;
import com.example.dealin.utility.Helper;
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
import java.util.HashMap;

public class Location extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
ImageView back,location;
TextView title;
Spinner cityList;
ListView cityCollegeList;
String selectedCity,email,password,name;
ArrayAdapter<String> cityAdapter,collegeAdapter;
HashMap<String,Integer> collegeIds;
int userId,collegeId,userType;
long mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        //strict mode
        StrictMode.ThreadPolicy threadPolicy=new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(threadPolicy);

        addActionBar();
        addWidgets();
        getUser();
        collegeIds=new HashMap<>();
        //adding cities to spinner
        cityAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityList.setAdapter(cityAdapter);
        getCities();
        cityList.setOnItemSelectedListener(this);

        //adding colleges based on cities
        collegeAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,android.R.id.text1);
        cityCollegeList.setAdapter(collegeAdapter);
        cityCollegeList.setOnItemClickListener(this);



    }

    public void addActionBar()
    {
        //actionbar customisation
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_back);
        View v=getSupportActionBar().getCustomView();
        location=(ImageView)v.findViewById(R.id.action_bar_location);
        location.setVisibility(View.GONE);
        title=(TextView)v.findViewById(R.id.bar_title);
        title.setText("Location");
        back=(ImageView)v.findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==back)
        {
            onBackPressed();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView==cityList)
        {
            cityList.setSelection(i);
            selectedCity=cityList.getSelectedItem().toString();
            collegeAdapter.clear();
            collegeIds.clear();
            getColleges(selectedCity);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
      if(adapterView==cityCollegeList)
      {
          int clgId=collegeIds.get(collegeAdapter.getItem(i).toString());
          setUser(clgId);
          Helper.toast(this,"College Changed");
          onBackPressed();
      }
    }

    //getting cities
    private void getCities()
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="cities";
        try{
            HttpURLConnection conn= Connection.createConnection();
            //output
            OutputStream outputStream=conn.getOutputStream();
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            String dataEncode= URLEncoder.encode("page","UTF-8")+"="+URLEncoder.encode(page,"UTF-8");
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
                cityAdapter.add(jsonObject.getString("city"));
            }
            cityAdapter.setNotifyOnChange(true);
            cityList.setSelection(0);
            conn.disconnect();
        }catch (Exception e)
        {
            Log.d("City",e.toString());
        }
    }


    //searching colleges based on cities
    private void getColleges(String city)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="colleges";
        try{
            HttpURLConnection conn= Connection.createConnection();
            //output
            OutputStream outputStream=conn.getOutputStream();
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            String dataEncode= URLEncoder.encode("page","UTF-8")+"="+URLEncoder.encode(page,"UTF-8")+
                    "&" + URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode(city, "UTF-8");
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
                collegeAdapter.add(jsonObject.getString("college_name"));
                collegeIds.put(jsonObject.getString("college_name"),jsonObject.getInt("college_id"));

            }
            collegeAdapter.notifyDataSetChanged();
            Log.d("collegesId",collegeIds.toString());
            conn.disconnect();
        }catch (Exception e)
        {
            Log.d("Colleges",e.toString());
        }
    }


    private void setUser(int collegeId)
    {
        User userOb=new User(userId,name,password,email,collegeId,mobile,userType);
        Gson gson=new Gson();
        String user=gson.toJson(userOb, User.class);
        SharedPreferences sharedPreferences=getSharedPreferences("USER_KEY",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("user",user);
        editor.commit();

    }

    private void getUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_KEY", 0);
        String userJson = sharedPreferences.getString("user", null);
        if (userJson != null) {
            Gson gson = new Gson();
            User user = gson.fromJson(userJson, User.class);
            userId = user.getUserid();
            collegeId = user.getCollegeid();
            mobile = user.getMobile();
            email = user.getEmail();
            password = user.getPassword();
            name = user.getName();
            userType = user.getUsertype();
        }
    }


    private void addWidgets()
    {
        cityList=findViewById(R.id.city_list);
        cityCollegeList=findViewById(R.id.city_college_list);
    }



}
