package com.example.dealin.user.message;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.login.User;
import com.example.dealin.user.deliver.Deliver;
import com.example.dealin.user.deliver.DeliverExpandableAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    Spinner msg_type;
    ExpandableListView messages;
    View v;
    ArrayList<Message>message;
    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_message, container, false);
        addWidgets();
        if(getMessages(getUserId()))
        {
            MessageExpandableAdapter da=new MessageExpandableAdapter(getActivity().getBaseContext(),message);
            messages.setAdapter(da);
        }
        return v;
    }

    //getting messages of user
    public boolean getMessages(int user_id)
    {

        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="user_messages";
        try{
            HttpURLConnection conn= Connection.createConnection();
            //output
            OutputStream outputStream=conn.getOutputStream();
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"UTF-8");
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            String dataEncode= URLEncoder.encode("page","UTF-8")+"="+URLEncoder.encode(page,"UTF-8")+
                    "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(user_id), "UTF-8");
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

                message=new ArrayList<>();
                for(int i=0;i<length;i++)
                {
                    jsonObject=jsonArray.getJSONObject(i)  ;
                    Message m=new Message();
                    m.setMessageId(jsonObject.getInt("message_id"));
                    m.setMessageStatus(jsonObject.getInt("msg_status"));
                    m.setMessage(jsonObject.getString("message"));
                    m.setSenderName(jsonObject.getString("name"));
                    m.setSenderPhone(jsonObject.getString("mobile"));
                    message.add(m);
                }
                conn.disconnect();
                return true;
            }

            conn.disconnect();

        }catch (Exception e)
        {
            Log.d("Messages",e.toString());
        }

        return false;

    }


    private int getUserId()
    {
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("USER_KEY",0);
        String userJson=sharedPreferences.getString("user",null);
        if(userJson!=null)
        {
            Gson gson=new Gson();
            User user=gson.fromJson(userJson,User.class);
            return user.getUserid();
        }
        return 0;
    }

    void addWidgets()
    {
        msg_type=(Spinner)v.findViewById(R.id.message_type_dropdown);
        msg_type.setVisibility(View.GONE);
        messages=(ExpandableListView)v.findViewById(R.id.message_list);
    }
}
