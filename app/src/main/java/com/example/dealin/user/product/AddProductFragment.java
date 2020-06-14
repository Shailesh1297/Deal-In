package com.example.dealin.user.product;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.login.User;
import com.example.dealin.user.home.Product;
import com.example.dealin.user.home.RecyclerViewAdapter;
import com.example.dealin.user.orders.Order;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText pdt_name,pdt_price,pdt_description;
    Spinner pdt_category;
    ImageView pdt_img;
    Button save;
    View v;
    LinearLayout pl;
    ProgressBar progressBar;
    ListView productList;
    ArrayAdapter<String>categoryAdapter;
    private String selectedCategory;
    ArrayList<Product> userProduct;
    private Bitmap bitmap=null;
    String option = "";
    private int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 1888;

    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        StrictMode.ThreadPolicy threadPolicy=new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(threadPolicy);
        v= inflater.inflate(R.layout.fragment_add_product, container, false);
        addwidgets();
        categoryAdapter=new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.support_simple_spinner_dropdown_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pdt_category.setAdapter(categoryAdapter);
        getCategories();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        ProductFragmentTask pft=new ProductFragmentTask();
        pft.execute(getUserId());
       /* if(userProducts(getUserId()))
        {
            UserProductAdapter upa=new UserProductAdapter(getActivity().getBaseContext(),userProduct);
            productList.setAdapter(upa);
        }*/

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView==pdt_category)
        {
            pdt_category.setSelection(i);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

        if(view==save)
        {
            String name,price,description,image="";
            name=pdt_name.getText().toString();
            price=pdt_price.getText().toString();
            description=pdt_description.getText().toString();
            if(TextUtils.isEmpty(name))
            {
                pdt_name.requestFocus();
                pdt_name.setError("Field can't be empty");
            }else if(TextUtils.isEmpty(price))
            {
                pdt_price.requestFocus();
                pdt_price.setError("Field can't be empty");
            }else if(TextUtils.isEmpty(description))
            {
                pdt_description.requestFocus();
                pdt_description.setError("Field can't be empty");
            }else if(bitmap==null)
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setMessage("Add Product Image")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog alert=builder.create();
                alert.show();

            }else
            {
                image=getStringImage(bitmap);
                selectedCategory=pdt_category.getSelectedItem().toString();
                int userid=getUserId();
                float fprice=Float.parseFloat(price);

               if(addProduct(userid,name,fprice,selectedCategory,description,image))
               {
                   Toast.makeText(getActivity().getApplicationContext(),"Product Added",Toast.LENGTH_SHORT).show();
                   pdt_name.setText("");
                   pdt_price.setText("");
                   pdt_description.setText("");
                   pdt_category.setSelection(0);
                   pdt_img.setImageResource(R.drawable.ic_photo_camera_blue_24dp);
                   bitmap=null;
                   onStart();
               }
               else
               {
                   Toast.makeText(getActivity().getApplicationContext(),"Something wrong",Toast.LENGTH_SHORT).show();
               }

            }

        }
        //camera
        if(view==pdt_img)
        {
            if (isReadStorageAllowed())
            {
                option="camera";
                takeImageFromCamera(v);
                return;
            }

            requestStoragePermission();
        }
    }

    //camera
    public void takeImageFromCamera(View view)
    {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(option.equals("camera"))
        {
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK)
            {
                bitmap = (Bitmap) data.getExtras().get("data");
                pdt_img.setImageBitmap(bitmap);
            }
        }

    }

    //converting image to string
    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

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
            pdt_category.setSelection(0);
            conn.disconnect();
        }catch (Exception e)
        {
            Log.d("Category",e.toString());
        }
    }


    private boolean addProduct(int user_id,String pdt_name,float pdt_price,String pdt_category,String pdt_description,String pdt_image)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="products";
        try {
            HttpURLConnection conn = Connection.createConnection();
            //output
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String dataEncode = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") +
                    "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(user_id), "UTF-8") +
                    "&" + URLEncoder.encode("pdt_name", "UTF-8") + "=" + URLEncoder.encode(pdt_name, "UTF-8") +
                    "&" + URLEncoder.encode("pdt_price", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pdt_price), "UTF-8") +
                    "&" + URLEncoder.encode("pdt_category", "UTF-8") + "=" + URLEncoder.encode(pdt_category, "UTF-8") +
                    "&" + URLEncoder.encode("pdt_description", "UTF-8") + "=" + URLEncoder.encode(pdt_description, "UTF-8")+
                    "&" + URLEncoder.encode("pdt_image", "UTF-8") + "=" + URLEncoder.encode(pdt_image, "UTF-8");
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
            Log.d("AddProducts",e.toString());
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

    //get user products

    private boolean userProducts(int user_id)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="user_products";
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

                userProduct=new ArrayList<>();
                for(int i=0;i<length;i++)
                {
                    jsonObject=jsonArray.getJSONObject(i)  ;
                    Product pdt=new Product();
                    pdt.setId(jsonObject.getInt("item_id"));
                    pdt.setTitle(jsonObject.getString("item_name"));
                    pdt.setPrice(jsonObject.getString("item_price"));
                    pdt.setCategory(jsonObject.getString("category"));
                    pdt.setDescription(jsonObject.getString("description"));
                    pdt.setThumbnail(jsonObject.getString("image"));
                    userProduct.add(pdt);
                }
                conn.disconnect();
                return true;

            }

            conn.disconnect();

        }catch (Exception e)
        {
            Log.d("UserProduct",e.toString());
        }

        return false;
    }


    //camera permissions
    private boolean isReadStorageAllowed()
    {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }


    private void requestStoragePermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CAMERA))
        { }
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity().getApplicationContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void addwidgets()
    {
        pdt_name=(EditText)v.findViewById(R.id.add_product_name);
        pdt_price=(EditText)v.findViewById(R.id.add_product_price);
        pdt_category=(Spinner)v.findViewById(R.id.add_product_category);
        pdt_category.setOnItemSelectedListener(this);
        pdt_description=(EditText)v.findViewById(R.id.add_product_description);
        pdt_img=(ImageView)v.findViewById(R.id.add_product_img);
        pdt_img.setOnClickListener(this);
        save=(Button)v.findViewById(R.id.add_product);
        save.setOnClickListener(this);
        productList=(ListView)v.findViewById(R.id.products_list);
        pl=(LinearLayout)v.findViewById(R.id.layout_add_product_top);
    }

    class ProductFragmentTask extends AsyncTask<Integer,Void,Boolean>
    {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar=new ProgressBar(getActivity().getApplicationContext(),null,android.R.attr.progressBarStyleLarge);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(100,100);
            params.gravity= Gravity.CENTER;
            pl.addView(progressBar,params);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            boolean gp=userProducts(integers[0]);
            return gp;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean)
            {
                progressBar.setVisibility(View.GONE);
                UserProductAdapter upa=new UserProductAdapter(getActivity().getBaseContext(),userProduct);
                productList.setAdapter(upa);
            }
        }
    }


}
