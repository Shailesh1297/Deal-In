package com.example.dealin.connection;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class Connection {

    public static HttpURLConnection createConnection(String filePHP)
    {
        HttpURLConnection connection=null;
        try
        {
            //change url on different networks
            URL url=new URL("http://192.168.43.147/dealin/"+filePHP);
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);

        }catch(Exception e)
        {
            Log.d("connection failed",e.toString());
        }
        return connection;
    }
}
