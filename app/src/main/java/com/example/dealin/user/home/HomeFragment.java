package com.example.dealin.user.home;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.login.User;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    List<Product>productList;
    RecyclerView recyclerView;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        //strict mode
        StrictMode.ThreadPolicy threadPolicy=new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(threadPolicy);
        allProducts(getUserId());
        recyclerView=(RecyclerView)view.findViewById(R.id.home_recycler_view);
        RecyclerViewAdapter rva=new RecyclerViewAdapter(getActivity().getBaseContext(),productList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getBaseContext(),3));
        recyclerView.setAdapter(rva);
        return view;
    }

    public void products()
    {
       // productList=new ArrayList<>();
//        productList.add(new Product("Facebook","software","social networking","1599",R.drawable.facebook));
//        productList.add(new Product("Youtube","website","video streaming","2599",R.drawable.youtube));
//        productList.add(new Product("Twitter","website","text writing","999",R.drawable.twitter));
//        productList.add(new Product("Instagram","software","photo sharing","1299",R.drawable.instagram));

    }

    public void allProducts(int user_id)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="all_products";
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
            JSONArray jsonArray=new JSONArray(dataDecode);
            int length=jsonArray.length();
            JSONObject jsonObject=null;
            productList=new ArrayList<>();
            for(int i=0;i<length;i++)
            {
                jsonObject=jsonArray.getJSONObject(i);
                Product pdt=new Product();
                pdt.setId(jsonObject.getInt("item_id"));
                pdt.setTitle(jsonObject.getString("item_name"));
                pdt.setPrice(jsonObject.getString("item_price"));
                pdt.setCategory(jsonObject.getString("category"));
                pdt.setDescription(jsonObject.getString("description"));
                pdt.setThumbnail(jsonObject.getString("image"));
                productList.add(pdt);

            }

            conn.disconnect();
        }catch (Exception e)
        {
            Log.d("All products",e.toString());
        }

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
}
