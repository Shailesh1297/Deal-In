package com.example.dealin.updater;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;

import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.example.dealin.BuildConfig;
import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.utility.Config;

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

import java.util.StringTokenizer;


public class Update extends AppCompatActivity implements View.OnClickListener {


    TextView updateList,title,cVersion,uDate;
    Button checker,updater;
    ImageView back,location;
    LinearLayout downloadBox;
    int major,minor,build,currentMajor,currentMinor,currentBuild;
    String date,updateInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        StrictMode.ThreadPolicy threadPolicy=new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(threadPolicy);
        addActionBar();
        addWidgets();
        getCurrentVersion();


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
        title.setText("Update");
        location=(ImageView)v.findViewById(R.id.action_bar_location);
        location.setVisibility(View.GONE);

    }

    private void addWidgets()
    {


        checker=findViewById(R.id.update_check_button);
        updater=findViewById(R.id.update_downloader);
        updateList=findViewById(R.id.update_list);
        cVersion=findViewById(R.id.current_version);
        uDate=findViewById(R.id.last_updated);
        downloadBox=findViewById(R.id.update_container);
        downloadBox.setVisibility(View.INVISIBLE);
        checker.setOnClickListener(this);
        updater.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view==back)
        {
            onBackPressed();
        }

        if(view==checker)
        {


            boolean flag=false;
                   if(checkUpdate())
                   {
                      if(major>currentMajor)
                      {
                          flag=true;
                      }else if(minor>currentMinor)
                      {
                          flag=true;
                      }else if(build>currentBuild)
                      {
                          flag=true;
                      }

                   }
                   if(flag)
                   {
                      updateList.setText(updateInfo);
                      uDate.setText(date);
                      downloadBox.setVisibility(View.VISIBLE);
                   }else {
                       updateInfo="** You are up to date **";
                       uDate.setText(date);
                       updateList.setText(updateInfo);

                   }

        }

        if(view==updater)
        {

           if( downloadUpdate(Config.DOWNLOAD))
           {
               Log.d("download","downloading");
           }

        }
    }

    private void getCurrentVersion()
    {
        String version= BuildConfig.VERSION_NAME;
        StringTokenizer stringTokenizer=new StringTokenizer(version,".");
           currentMajor=Integer.parseInt(stringTokenizer.nextToken());
             currentMinor=Integer.parseInt(stringTokenizer.nextToken());
             currentBuild=Integer.parseInt(stringTokenizer.nextToken());
             cVersion.setText(""+currentMajor+"."+currentMinor+"."+currentBuild);

    }

    private boolean checkUpdate()
    {

        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="check_update";
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

                major=jsonArray.getInt(0);
                minor=jsonArray.getInt(1);
                build=jsonArray.getInt(2);
                date=jsonArray.getString(3);
                updateInfo="**Update Available**\n";
                updateInfo+="version="+major+"."+minor+"."+build;

                for(int i=4;i<9;i++)
                {
                    if(!jsonArray.getString(i).equals(""))
                    {
                        updateInfo=updateInfo+"\n"+jsonArray.getString(i);
                    }
                }

                Log.d("data",updateInfo+"\n"+major+minor+build);

                conn.disconnect();


                return true;

            }

            conn.disconnect();

        }catch (Exception e)
        {
            Log.d("update info",e.toString());
        }

        return false;
    }

    //downloading file through browser
    private boolean downloadUpdate(String path)
    {

            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(path));
            startActivity(intent);
            return true;

    }

}
