package com.example.dealin.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.login.Login;

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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText name,email,phone,password;
    Spinner cities,colleges;
    TextView login;
    ArrayAdapter<String>cityAdapter,collegeAdapter;
    Button register;
    String selectedCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //strict mode
        StrictMode.ThreadPolicy threadPolicy=new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(threadPolicy);
        addWidgets();

        //adding cities to spinner


        cityAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cities.setAdapter(cityAdapter);
        getCities();
        cities.setOnItemSelectedListener(this);

        //adding colleges based on cities

        collegeAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        collegeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        colleges.setAdapter(collegeAdapter);
        colleges.setOnItemSelectedListener(this);



        //when already have an account is pressed
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==register)
        {
            String name,email,password,college;
            String phone=this.phone.getText().toString();
            name=this.name.getText().toString();
            email=this.email.getText().toString();
            password=this.password.getText().toString();
            college =colleges.getSelectedItem().toString();
             if(TextUtils.isEmpty(name))
             {
                 this.name.requestFocus();
                 this.name.setError("Field can't be empty");
             }else if(TextUtils.isEmpty(email))
             {
                 this.email.requestFocus();
                 this.email.setError("Field can't be empty");
             }else if(TextUtils.isEmpty(phone))
             {
                 this.phone.requestFocus();
                 this.phone.setError("Field can't be empty");
             }
                else if(TextUtils.isEmpty(password))
             {
                 this.password.requestFocus();
                 this.password.setError("Field can't be empty");
             }else
                 {
                       Long mobile=Long.parseLong(phone);
                         boolean flag = registerUser(name, email, mobile, college, password);
                         if (flag) {
                             Toast.makeText(this, "User Registered", Toast.LENGTH_LONG).show();
                             onBackPressed();

                         } else {
                             Toast.makeText(this, "Email Already Exist", Toast.LENGTH_LONG).show();
                         }
                  }
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView==cities)
        {
            cities.setSelection(i);
             selectedCity=cities.getSelectedItem().toString();
             collegeAdapter.clear();
              getColleges(selectedCity);
        }
        if(adapterView==colleges)
        {
           // Log.d("selectedCollege",colleges.getSelectedItem().toString());
            colleges.setSelection(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //when already have an account is pressed
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    //registering user
    private boolean registerUser(String name,String email,Long phone,String college,String password)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="registration";
        try{
            HttpURLConnection conn= Connection.createConnection();
            //output
            OutputStream outputStream=conn.getOutputStream();
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            String dataEncode= URLEncoder.encode("page","UTF-8")+"="+URLEncoder.encode(page,"UTF-8")+
                    "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")+
                    "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+
                    "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(phone), "UTF-8")+
                    "&" + URLEncoder.encode("college", "UTF-8") + "=" + URLEncoder.encode(college, "UTF-8")+
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
           // JSONObject jsonData=jsonObject.getJSONObject("flag");
            int flag=jsonObject.getInt("flag");
            Log.d("flagData",""+flag);
            conn.disconnect();
            if(flag==1) return true;


        }catch (Exception e)
        {
            Log.d("Register",e.toString());
        }

        return false;
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
            cities.setSelection(0);
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

            }

            collegeAdapter.notifyDataSetChanged();
            colleges.setSelection(0);
            conn.disconnect();
        }catch (Exception e)
        {
            Log.d("Colleges",e.toString());
        }
    }

    //adding views to java
   private void addWidgets()
    {
        login=findViewById(R.id.already_have_account);
        name=findViewById(R.id.register_name);
        email=findViewById(R.id.register_email);
        phone=findViewById(R.id.register_phone);
        password=findViewById(R.id.register_password);
        cities=findViewById(R.id.register_city_spinner);
        colleges=findViewById(R.id.register_college_spinner);
        register=findViewById(R.id.register_but);
        register.setOnClickListener(this);
    }



}
