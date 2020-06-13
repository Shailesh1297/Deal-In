package com.example.dealin.admin.suggestions;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.login.User;
import com.example.dealin.user.message.Message;
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

public class AdminSuggestions extends AppCompatActivity implements View.OnClickListener {

    ImageView back,location;
    TextView title;
    ArrayList<Suggestion>suggestions;
    ExpandableListView suggestionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_suggestions);
        addActionBar();
        addWidgets();

        if(getSuggestions())
        {
            AdminSuggestionAdapter asa=new AdminSuggestionAdapter(getApplicationContext(),suggestions);
            suggestionList.setAdapter(asa);
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
        title.setText("Suggestions");
        location=(ImageView)v.findViewById(R.id.action_bar_location);
        location.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        if(view==back)
        {
            onBackPressed();
        }
    }

    private void addWidgets()
    {
        suggestionList=findViewById(R.id.suggestion_list);
    }

    public boolean getSuggestions()
    {

        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="suggestion_list";
        try{
            HttpURLConnection conn= Connection.createConnection();
            //output
            OutputStream outputStream=conn.getOutputStream();
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            String dataEncode= URLEncoder.encode("page","UTF-8")+"="+URLEncoder.encode(page,"UTF-8")+
                    "&" + URLEncoder.encode("college_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(getCollegeId()), "UTF-8");
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

                suggestions=new ArrayList<>();
                for(int i=0;i<length;i++)
                {
                    jsonObject=jsonArray.getJSONObject(i)  ;
                   Suggestion s=new Suggestion();
                   s.setUserId(jsonObject.getInt("user_id"));
                   s.setSuggestionId(jsonObject.getInt("suggestion_id"));
                    s.setDate(jsonObject.getString("date"));
                    s.setStatus(jsonObject.getInt("status"));
                    s.setSuggestion(jsonObject.getString("suggestion"));
                    suggestions.add(s);
                }
                conn.disconnect();
                return true;
            }

            conn.disconnect();

        }catch (Exception e)
        {
            Log.d("Admin Suggestions",e.toString());
        }

        return false;

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
}
