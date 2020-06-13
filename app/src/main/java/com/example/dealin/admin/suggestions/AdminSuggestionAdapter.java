package com.example.dealin.admin.suggestions;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

public class AdminSuggestionAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<Suggestion>suggestions;

    public AdminSuggestionAdapter(Context context, ArrayList<Suggestion> suggestions) {
        this.context = context;
        this.suggestions = suggestions;
    }

    @Override
    public int getGroupCount() {
        return suggestions.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return suggestions.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return suggestions.get(i);
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

        if(suggestions.get(groupPosition).getStatus()==0)
        {
            changeSuggestionStatus(suggestions.get(groupPosition).getSuggestionId());
        }

    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.admin_suggestion_group,null);
        }
        TextView userId,date;
        ImageView status;
        userId=(TextView)view.findViewById(R.id.suggestion_from);
        date=(TextView)view.findViewById(R.id.suggestion_date);
        status=(ImageView)view.findViewById(R.id.suggestion_status);
        userId.setText(""+suggestions.get(i).getUserId());
        date.setText(suggestions.get(i).getDate());
        if(suggestions.get(i).getStatus()==1)
        {
            status.setImageResource(R.drawable.ic_delivered_24dp);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.admin_suggestion_content,null);
        }
        TextView content=(TextView)view.findViewById(R.id.suggestion_content);
        content.setText(suggestions.get(i).getSuggestion());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
    private void changeSuggestionStatus(int suggestionId)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="suggestion_readed";
        try {
            HttpURLConnection conn = Connection.createConnection();
            //output
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String dataEncode = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") +
                    "&" + URLEncoder.encode("suggestion_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(suggestionId), "UTF-8");
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
            Log.d("Suggestion status",e.toString());
        }
    }
}
