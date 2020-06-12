package com.example.dealin.admin.colleges;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.user.orders.Order;
import com.example.dealin.utility.Helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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


public class AddColleges extends AppCompatActivity implements View.OnClickListener {
    ImageView back,location;
    FloatingActionButton floatingAdd;
    PopupWindow popupWindow;
    Button add,close;
    EditText college,city;
    RelativeLayout relativeLayout;
    TextView title;
    ListView collegeList;
    ArrayList<College> colleges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_colleges);
       addActionBar();
        addWidgets();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getColleges()){
            AddCollegeAdapter aca=new AddCollegeAdapter(AddColleges.this,colleges);
            collegeList.setAdapter(aca);
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
        title.setText("Colleges");
        location=(ImageView)v.findViewById(R.id.action_bar_location);
        location.setVisibility(View.GONE);

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
            String collegeName,collegeCity;
            collegeName=college.getText().toString();
            collegeCity=city.getText().toString();
            if(TextUtils.isEmpty(collegeName))
            {
                college.requestFocus();
                college.setError("Field can't be empty");
            }else if(TextUtils.isEmpty(collegeCity))
            {
                city.requestFocus();
                city.setError("Field can't be empty");
            }else
            {
                if(addCollege(collegeName,collegeCity))
                {
                    popupWindow.dismiss();
                     Helper.toast(getApplicationContext(),"College Added");
                     onStart();
                }

            }
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
        collegeList=findViewById(R.id.add_college_admin);
    }

    public boolean getColleges()
    {

        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="college_list";
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
            JSONObject jsonObject=new JSONObject(dataDecode);

            //  JSONObject jsonData=jsonObject.getJSONObject("flag");
            int flag=jsonObject.getInt("flag");
            if(flag==1)
            {
                JSONArray jsonArray=jsonObject.getJSONArray("0");
                int length=jsonArray.length();

                colleges=new ArrayList<>();
                for(int i=0;i<length;i++)
                {
                    jsonObject=jsonArray.getJSONObject(i)  ;
                   College college=new College();
                   college.setCollegeId(jsonObject.getInt("college_id"));
                    college.setCollegeName(jsonObject.getString("college_name"));
                    college.setCollegeCity(jsonObject.getString("city"));
                    colleges.add(college);
                }
                conn.disconnect();
                return true;

            }

            conn.disconnect();

        }catch (Exception e)
        {
            Log.d("Get Colleges",e.toString());
        }

        return false;

    }

    private boolean addCollege(String collegeName,String collegeCity)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="add_college";
        try {
            HttpURLConnection conn = Connection.createConnection();
            //output
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String dataEncode = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") +
                    "&" + URLEncoder.encode("college_name", "UTF-8") + "=" + URLEncoder.encode(collegeName, "UTF-8") +
                    "&" + URLEncoder.encode("college_city", "UTF-8") + "=" + URLEncoder.encode(collegeCity, "UTF-8") ;
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
            Log.d("AddCollege",e.toString());
        }
        return false;
    }


}
