package com.example.dealin.user.deliver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dealin.R;
import com.example.dealin.connection.Connection;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DeliverExpandableAdapter extends BaseExpandableListAdapter {

    TextView orderId,itemName,itemPrice,itemDescription,payMode,venue,buyerName,buyerEmail,buyerPhone;
    ImageView deliverStatus;
    Button call,message,delivered;
    Context context;
    ArrayList<Deliver>deliver;
    LinearLayout ll;

    public static final int REQUEST_CODE=11;
    DeliverExpandableAdapter(Context context, ArrayList<Deliver> deliver)
    {
        this.context=context;
        this.deliver=deliver;
    }
    @Override
    public int getGroupCount() {
        return deliver.size();
    }

    @Override
    public int getChildrenCount(int i) {
        //for deliverd products
       if(deliver.get(i).getDeliveryStatus()==1)
       {
           return 0;
       }
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return deliver.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return deliver.get(i);
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
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.deliver_list_group,null);
        }
        ll=(LinearLayout)view.findViewById(R.id.deliver_top_head);
        orderId=(TextView)view.findViewById(R.id.deliver_order_id);
        itemName=(TextView)view.findViewById(R.id.deliver_item_name);
        itemPrice=(TextView)view.findViewById(R.id.deliver_item_price);
        deliverStatus=(ImageView)view.findViewById(R.id.item_delivery_status);
        //values
        orderId.setText(String.valueOf(deliver.get(i).getOrderId()));
        itemName.setText(deliver.get(i).getItemName());
        itemPrice.setText(deliver.get(i).getItemPrice());
        if(deliver.get(i).getDeliveryStatus()==0)
        {
            deliverStatus.setImageResource(R.drawable.ic_undelivered_24dp);
        }else{
            deliverStatus.setImageResource(R.drawable.ic_delivered_24dp);
        }


        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.deliver_list_item,null);
        }

      final  int order_id=deliver.get(i).getOrderId();

        itemDescription=(TextView)view.findViewById(R.id.deliver_item_description);
        payMode=(TextView)view.findViewById(R.id.deliver_item_paymode);
        venue=(TextView)view.findViewById(R.id.deliver_item_venue);
        buyerName=(TextView)view.findViewById(R.id.deliver_buyer_name);
        buyerEmail=(TextView)view.findViewById(R.id.deliver_buyer_email);
        buyerPhone=(TextView)view.findViewById(R.id.deliver_buyer_phone);
        call=(Button)view.findViewById(R.id.call_buyer);
        message=(Button)view.findViewById(R.id.message_buyer);
        delivered=(Button)view.findViewById(R.id.item_delivered);
        //values
        itemDescription.setText(deliver.get(i).getItemDescription());
        payMode.setText(deliver.get(i).getPayMode());
        venue.setText(deliver.get(i).getDeliveryVenue());
        buyerName.setText(deliver.get(i).getBuyerName());
        buyerEmail.setText(deliver.get(i).getBuyerEmail());
        buyerPhone.setText(deliver.get(i).getBuyerMobile());
   call.setOnClickListener(new View.OnClickListener() {
       @SuppressLint("MissingPermission")
       @Override
       public void onClick(View view) {

           Intent intentCall=new Intent(Intent.ACTION_CALL);
           String phno=buyerPhone.getText().toString();
           intentCall.setData(Uri.parse("tel:"+phno));
           intentCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           context.startActivity(intentCall);
       }
   });

   message.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {

           //pop window
           LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           View popup=layoutInflater.inflate(R.layout.send_message,null);
         final EditText  setDate=(EditText) popup.findViewById(R.id.message_date);
          final EditText setTime=(EditText) popup.findViewById(R.id.message_time);
          Button cancel=(Button)popup.findViewById(R.id.cancel_message);
          Button send=(Button)popup.findViewById(R.id.send_message);
          final EditText msgVenue=(EditText)popup.findViewById(R.id.message_venue);
          msgVenue.setText(venue.getText().toString());
         final PopupWindow  popupWindow=new PopupWindow(popup, RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
           popupWindow.showAtLocation(ll, Gravity.CENTER,0,0);
           popupWindow.setFocusable(true);//keyboard can open
           popupWindow.update();

           //dsimiss
           cancel.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   popupWindow.dismiss();
               }
           });
           //sending message
           send.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String mdate,mtime,mvenue,msg;
                   mdate=setDate.getText().toString();
                   mtime=setTime.getText().toString();
                   mvenue=msgVenue.getText().toString();

                   if(TextUtils.isEmpty(mdate)||TextUtils.isEmpty(mtime)||TextUtils.isEmpty(mvenue))
                   {
                     Toast.makeText(context,"Fill all Fields",Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                       if(sendMessage(order_id,mvenue,messageCreator(mdate,mtime,mvenue,order_id)))
                       {
                           Toast.makeText(context,"Message sent",Toast.LENGTH_SHORT).show();
                       }else
                       {
                           Toast.makeText(context,"Message can't be sent",Toast.LENGTH_SHORT).show();
                       }

                       popupWindow.dismiss();
                   }


               }
           });

       }
   });

   delivered.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           Toast.makeText(context,"delivered"+order_id,Toast.LENGTH_SHORT).show();
       }
   });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    private boolean sendMessage(int order_id,String venue,String msg)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="send_message";
        try {
            HttpURLConnection conn = Connection.createConnection();
            //output
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String dataEncode = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") +
                    "&" + URLEncoder.encode("order_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(order_id), "UTF-8") +
                    "&" + URLEncoder.encode("venue", "UTF-8") + "=" + URLEncoder.encode(venue, "UTF-8") +
                    "&" + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(msg), "UTF-8");
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
            Log.d("Send Message",e.toString());
        }
        return false;
    }

    private String messageCreator(String date,String time,String place,int order_id)
    {
        String msg="Order-Number-"+order_id+"\n"+
                    "Date-"+date+"\t\t"+"Time-"+time+"\n"+
                    "Message- Meet me at "+place;

        return msg;
    }

}
