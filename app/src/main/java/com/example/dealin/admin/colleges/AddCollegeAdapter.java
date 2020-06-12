package com.example.dealin.admin.colleges;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dealin.R;

import java.util.ArrayList;

public class AddCollegeAdapter extends BaseAdapter {

    ArrayList<College>colleges;
    Context context;
    LayoutInflater layoutInflater;
    AddCollegeAdapter(Context context,ArrayList<College>colleges)
    {
        this.context=context;
        this.colleges=colleges;
        layoutInflater= LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return colleges.size();
    }

    @Override
    public Object getItem(int i) {
        return colleges.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=layoutInflater.inflate(R.layout.admin_college_list,null);
        TextView collegeName,collegeId,collegeCity;
        collegeName=(TextView)view.findViewById(R.id.admin_college_name);
        collegeId=(TextView)view.findViewById(R.id.admin_college_id);
        collegeCity=(TextView)view.findViewById(R.id.admin_college_city);
        collegeName.setText(colleges.get(i).getCollegeName());
        collegeId.setText(""+colleges.get(i).getCollegeId());
        collegeCity.setText(colleges.get(i).getCollegeCity());
        return view;
    }
}
