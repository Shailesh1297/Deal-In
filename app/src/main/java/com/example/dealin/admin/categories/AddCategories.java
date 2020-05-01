package com.example.dealin.admin.categories;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.utility.Helper;

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

public class AddCategories extends AppCompatActivity implements View.OnClickListener {

    EditText addCategory;
    Button add;
    ImageView back,location;
    TextView title;
    ListView categoryList;
    ArrayAdapter<String>categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);
        addActionBar();
        addWidgets();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //adding colleges based on cities
        categoryAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,android.R.id.text1);
        categoryList.setAdapter(categoryAdapter);
        getCategories();
    }

    @Override
    public void onClick(View view) {
            if(view==add)
            {
                String category=addCategory.getText().toString().toLowerCase();
                if(TextUtils.isEmpty(category))
                {
                    addCategory.requestFocus();
                    addCategory.setError("Field can't be empty");
                }else
                {
                        if(addNewCategory(category))
                        {
                            Helper.toast(this,"Category Added");
                            addCategory.setText("");
                            onStart();
                        }else
                        {
                            Helper.toast(this,"Category NOT Added");
                        }
                }
            }
    }
    //adding new category
    private boolean addNewCategory(String category)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="add_category";
        try {
            HttpURLConnection conn = Connection.createConnection();
            //output
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String dataEncode = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") +
                    "&" + URLEncoder.encode("category", "UTF-8") + "=" + URLEncoder.encode(category, "UTF-8");
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
            Log.d("AddCategory",e.toString());
        }
        return false;
    }

    //getting all categories
    private  void getCategories()
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="categories";
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
            JSONArray jsonArray=new JSONArray(dataDecode);
            int length=jsonArray.length();
            JSONObject jsonObject=null;

            for(int i=0;i<length;i++)
            {
                jsonObject=jsonArray.getJSONObject(i);
                categoryAdapter.add(jsonObject.getString("category"));
            }
            categoryAdapter.setNotifyOnChange(true);
            conn.disconnect();
        }catch (Exception e)
        {
            Log.d("Category",e.toString());
        }
    }

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
        title.setText("Categories");
        back=(ImageView)v.findViewById(R.id.action_bar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void addWidgets()
    {
        addCategory=findViewById(R.id.admin_add_category);
        add=findViewById(R.id.add_category_but);
        categoryList=findViewById(R.id.category_list);
        add.setOnClickListener(this);
    }


}
