package com.example.dealin.user.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MessageExpandableAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<Message> message;
    TextView senderName,messageContent;
    ImageView callSender,messageStatus;
    LinearLayout ll;

    public MessageExpandableAdapter(Context context, ArrayList<Message> message) {
        this.context = context;
        this.message = message;
    }

    @Override
    public int getGroupCount() {
        return message.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return message.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return message.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);

        if(message.get(groupPosition).getMessageStatus()==0)
        {
            changeMessageStatus(message.get(groupPosition).getMessageId());
        }

    }

    @Override
    public View getGroupView(final int i, final boolean b, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.message_list_group,null);
        }

        ll=(LinearLayout)view.findViewById(R.id.message_head);
        senderName=(TextView)view.findViewById(R.id.message_from);
        callSender=(ImageView)view.findViewById(R.id.call_message_sender);
        messageStatus=(ImageView)view.findViewById(R.id.message_status);
        senderName.setText(message.get(i).getSenderName());
        if(message.get(i).getMessageStatus()==0)
        {
            messageStatus.setImageResource(R.drawable.ic_undelivered_24dp);
        }else{
            messageStatus.setImageResource(R.drawable.ic_delivered_24dp);
        }

        callSender.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                Intent intentCall=new Intent(Intent.ACTION_CALL);
                String phno=message.get(i).getSenderPhone();
                intentCall.setData(Uri.parse("tel:"+phno));
                intentCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentCall);
            }
        });


        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.message_list_item,null);
        }
        messageContent=(TextView)view.findViewById(R.id.message_content);
        messageContent.setText(message.get(i).getMessage());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private void changeMessageStatus(int messageId)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="message_readed";
        try {
            HttpURLConnection conn = Connection.createConnection();
            //output
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String dataEncode = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") +
                    "&" + URLEncoder.encode("message_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(messageId), "UTF-8");
            bufferedWriter.write(dataEncode);
            bufferedWriter.close();
            outputStreamWriter.close();
            outputStream.close();
            //input
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        }catch (Exception e)
        {
            Log.d("Message status",e.toString());
        }
    }
}
