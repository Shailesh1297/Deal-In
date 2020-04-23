package com.example.dealin.login;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.utility.Mailer;

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
import java.util.Random;


public class ForgetPassword extends AppCompatActivity implements View.OnClickListener {
    TextView title,password;
    ImageView location,back;
    EditText userEmail,passwordKey;
    LinearLayout passwordKeyLayout,passwordLayout;
    Button submit,recover;
    private String userPassword,userPasswordKey;
    private boolean check=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        addActionBar();
        addWidgets();

        userEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

               if(b)
               {
                   passwordKeyLayout.setVisibility(View.GONE);
                   passwordLayout.setVisibility(View.GONE);
                   passwordKey.setText("");
               }
            }
        });

    }

    @Override
    public void onClick(View view) {

        //email checking and password recovery
            if(view==submit && check)
            {
                String mail=userEmail.getText().toString().trim();
                if(TextUtils.isEmpty(mail))
                {
                    userEmail.requestFocus();
                    userEmail.setError("Field can't be empty");
                }else
                {
                    if(recoverPassword(mail))
                    {
                        Log.d("key",randomKey());
                        sendEmail(mail);
                        Toast.makeText(this,"Password Key Sent to Your Email",Toast.LENGTH_LONG).show();
                        passwordKeyLayout.setVisibility(View.VISIBLE);
                        submit.setEnabled(false);
                        submit.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                submit.setEnabled(true);
                            }
                        },30000);


                    }else
                    {
                        passwordKeyLayout.setVisibility(View.GONE);
                        Toast.makeText(this,"Email is incorrect or Not Registered user",Toast.LENGTH_LONG).show();
                    }
                }
            }

        //recovering password
        if(view==recover)
        {
            String key=passwordKey.getText().toString();
            if(TextUtils.isEmpty(key))
            {
                userEmail.requestFocus();
                userEmail.setError("Field can't be empty");
            }else
            {
                if(key.equals(userPasswordKey))
                {
                    passwordLayout.setVisibility(View.VISIBLE);
                    password.setText(userPassword);
                    recover.setEnabled(false);
                    userEmail.setText("");
                    recover.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            passwordLayout.setVisibility(View.GONE);
                            passwordKeyLayout.setVisibility(View.GONE);
                            recover.setEnabled(true);

                        }
                    },30000);
                }else
                {
                    Toast.makeText(this,"Key Do not matched",Toast.LENGTH_SHORT).show();
                }
            }
        }
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
        location.setVisibility(View.INVISIBLE);
        title=(TextView)v.findViewById(R.id.bar_title);
        title.setVisibility(View.INVISIBLE);
        back=(ImageView)v.findViewById(R.id.action_bar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


        //recover password
    private boolean recoverPassword(String email)
    {

        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="password_recovery";
        try{
            HttpURLConnection conn= Connection.createConnection();
            //output
            OutputStream outputStream=conn.getOutputStream();
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            String dataEncode= URLEncoder.encode("page","UTF-8")+"="+URLEncoder.encode(page,"UTF-8")+
                    "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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
                for(int i=0;i<length;i++)
                {
                    jsonObject=jsonArray.getJSONObject(i)  ;
                    userPassword=jsonObject.getString("password");
                }
                conn.disconnect();
                return true;

            }

            conn.disconnect();

        }catch (Exception e)
        {
            Log.d("Recover Password",e.toString());
        }

        return false;

    }

    //sending email for recovery
    private void sendEmail(String mail) {
        //Getting content for email
        String email =mail;
        String subject ="Password Key From Dealin";
        String message ="Password Key-"+randomKey();

        //Creating SendMail object
        Mailer m = new Mailer(this, email, subject, message);

        //Executing sendmail to send email
        m.execute();
    }

    private String randomKey()
    {
        int max=9999;
        int min=1000;
       int random=new Random().nextInt(max-min+1)+min;
       userPasswordKey=String.valueOf(random);
       return userPasswordKey;

    }


    void addWidgets()
    {
        userEmail=findViewById(R.id.recover_email);
        passwordKey=findViewById(R.id.password_key);
        password=findViewById(R.id.password_text);
        submit=findViewById(R.id.recover_email_but);
        recover=findViewById(R.id.recover_password_but);
        passwordKeyLayout=findViewById(R.id.password_key_layout);
        passwordLayout=findViewById(R.id.password_layout);
        passwordLayout.setVisibility(View.GONE);
        passwordKeyLayout.setVisibility(View.GONE);

        submit.setOnClickListener(this);
        recover.setOnClickListener(this);

    }


}
