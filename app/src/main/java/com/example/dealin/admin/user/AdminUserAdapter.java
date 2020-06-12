package com.example.dealin.admin.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.login.User;

import java.util.ArrayList;

public class AdminUserAdapter extends BaseExpandableListAdapter {

    Context context;
    ArrayList<User> users;

    public AdminUserAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getGroupCount() {
        return users.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return users.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return users.get(i);
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
            view=layoutInflater.inflate(R.layout.admin_user_group,null);
        }
        TextView userId,userName;
        ImageView userStatus;
        userId=(TextView)view.findViewById(R.id.admin_user_id);
        userName=(TextView)view.findViewById(R.id.admin_user_name);
        userStatus=(ImageView)view.findViewById(R.id.admin_user_status);
        userId.setText(""+users.get(i).getUserid());
        userName.setText(users.get(i).getName());
        if(users.get(i).getIsActive()==0)
        {
            userStatus.setImageResource(R.drawable.ic_delivered_24dp);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.admin_user_list,null);
        }
        TextView userEmail,userPhone;
        userEmail=(TextView)view.findViewById(R.id.admin_user_email);
        userPhone=(TextView)view.findViewById(R.id.admin_user_phone);

        userEmail.setText(users.get(i).getEmail());
        userPhone.setText(""+users.get(i).getMobile());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
