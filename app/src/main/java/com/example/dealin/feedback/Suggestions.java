package com.example.dealin.feedback;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.login.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class Suggestions extends AppCompatActivity implements View.OnClickListener {

    TextView title;
    ImageView location,back;
    EditText suggestionText;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        addActionBar();
        addWidgets();
    }


    @Override
    public void onClick(View view) {
        if(view==submit)
        {
            String suggestion=suggestionText.getText().toString();
            if(TextUtils.isEmpty(suggestion))
            {
                suggestionText.requestFocus();
                suggestionText.setError("Field can't be empty");
            }else
            {
                if(saveSuggestion(getUserId(),suggestion))
                {
                    Toast.makeText(this,"Suggestion submitted",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else
                {
                    Toast.makeText(this,"Something Wrong",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    private boolean saveSuggestion(int user_id,String suggestion)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="save_suggestion";
        try {
            HttpURLConnection conn = Connection.createConnection();
            //output
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String dataEncode = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") +
                    "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(user_id), "UTF-8") +
                    "&" + URLEncoder.encode("suggestion", "UTF-8") + "=" + URLEncoder.encode(suggestion, "UTF-8") ;
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
            Log.d("Suggestions",e.toString());
        }
        return false;
    }

    //getUserId
    private int getUserId()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("USER_KEY",0);
        String userJson=sharedPreferences.getString("user",null);
        if(userJson!=null)
        {
            Gson gson=new Gson();
            User user=gson.fromJson(userJson,User.class);
            return user.getUserid();
        }
        return 0;
    }

    //action bar
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
        title.setText("Suggestions");
        back=(ImageView)v.findViewById(R.id.action_bar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    //adding widgets

    void addWidgets()
    {
        suggestionText=findViewById(R.id.suggestion_box);
        submit=findViewById(R.id.suggestion_submit);
        submit.setOnClickListener(this);
    }

}
