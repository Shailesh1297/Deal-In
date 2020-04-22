package com.example.dealin.user.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealin.R;
import com.example.dealin.connection.Connection;
import com.example.dealin.user.home.Product;
import com.google.gson.Gson;

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

public class UserProduct extends AppCompatActivity implements View.OnClickListener {
  ImageView location,back,updateProductImage;
  Button update,delete;
  TextView title,alert,updateProductCategory;
  EditText updateProductName,updateProductPrice,updateProductDescription;
    private Bitmap bitmap=null;
    String option = "",imageName;
    private int PICK_IMAGE_REQUEST = 1,itemId;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product);
        addActionBar();

        addWidgets();

        Intent intent=getIntent();
        Gson gson=new Gson();
        String ps=intent.getExtras().getString("product");
        Product p=gson.fromJson(ps,Product.class);
        try{
            bitmap=p.getThumbnail();
            itemId=p.getId();
            imageName=p.getThumbnailString();
            updateProductName.setText(p.getTitle());
            updateProductPrice.setText(p.getPrice());
            updateProductCategory.setText(p.getCategory());
            updateProductDescription.setText(p.getDescription());
            updateProductImage.setImageBitmap(bitmap);

                if(checkProduct(itemId))
                {
                    hideWidgets();

                }else{
                    alert.setVisibility(View.GONE);
                    update.setOnClickListener(this);
                    delete.setOnClickListener(this);
                    updateProductImage.setOnClickListener(this);
                }


        }catch (Exception e)
        {

        }

    }

    @Override
    public void onClick(View view) {

        if(view==update)
        {

                String name,price,description,image="";
                name=updateProductName.getText().toString();
                price=updateProductPrice.getText().toString();
                description=updateProductDescription.getText().toString();
                if(TextUtils.isEmpty(name))
                {
                    updateProductName.requestFocus();
                    updateProductName.setError("Field can't be empty");
                }else if(TextUtils.isEmpty(price))
                {
                    updateProductPrice.requestFocus();
                    updateProductPrice.setError("Field can't be empty");
                }else if(TextUtils.isEmpty(description))
                {
                    updateProductDescription.requestFocus();
                    updateProductDescription.setError("Field can't be empty");
                }else
                {
                    image=getStringImage(bitmap);
                    float fprice=Float.parseFloat(price);

                    if(updateProduct(itemId,name,fprice,description,image,imageName)) {
                        Toast.makeText(this,"Product Updated",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }else
                    {
                        Toast.makeText(this,"Product NOT Updated",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            //item deletion
             if(view==delete)
             {

                        AlertDialog.Builder alert=new AlertDialog.Builder(UserProduct.this);
                        alert.setTitle("Are You Sure?");
                        alert.setMessage("Confirm to Delete Product");
                        alert.setCancelable(false);
                        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(deleteProduct(itemId))
                                {
                                    onBackPressed();
                                }
                            }
                        });
                        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        AlertDialog ad=alert.create();
                        ad.show();

             }
        //photo updation
            if(view==updateProductImage)
            {
                if (isReadStorageAllowed())
                {
                    option="camera";
                    takeImageFromCamera(view);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (option.equals("camera")) {
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                bitmap = (Bitmap) data.getExtras().get("data");
                updateProductImage.setImageBitmap(bitmap);
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


    //camera permissions
    private boolean isReadStorageAllowed()
    {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }


    private void requestStoragePermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA))
        { }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    //checking product present in orders
    private boolean checkProduct(int item_id)
    {
        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="check_product";
        try {
            HttpURLConnection conn = Connection.createConnection();
            //output
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String dataEncode = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") +
                    "&" + URLEncoder.encode("item_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(item_id), "UTF-8");
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
            conn.disconnect();
            if (flag == 1) return true;
        }
        catch (Exception e)
        {
            Log.d("Check Product Sold",e.toString());
        }
        return false;
    }

    //delete product
    private boolean deleteProduct(int item_id)
    {

        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="delete_product";
        try {
            HttpURLConnection conn = Connection.createConnection();
            //output
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String dataEncode = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") +
                    "&" + URLEncoder.encode("item_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(item_id), "UTF-8");

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
            if (flag == 1)
            {
                Toast.makeText(this,"Product Deleted",Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        catch (Exception e)
        {
            Log.d("Update Products",e.toString());
        }
        Toast.makeText(this,"Something Wrong",Toast.LENGTH_SHORT).show();
        return false;
    }


    //updating product data
    private boolean updateProduct(int item_id,String item_name,float item_price,String description,String image,String image_name)
    {

        StringBuilder stringBuilder=new StringBuilder();
        String line="";
        String page="update_product";
        try {
            HttpURLConnection conn = Connection.createConnection();
            //output
            OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String dataEncode = URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") +
                    "&" + URLEncoder.encode("item_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(item_id), "UTF-8") +
                    "&" + URLEncoder.encode("item_name", "UTF-8") + "=" + URLEncoder.encode(item_name, "UTF-8") +
                    "&" + URLEncoder.encode("item_price", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(item_price), "UTF-8")+
                    "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8")+
                    "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8")+
                    "&" + URLEncoder.encode("image_name", "UTF-8") + "=" + URLEncoder.encode(image_name, "UTF-8");

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
            Log.d("Update Products",e.toString());
        }
        return false;
    }

    //action bar
    public void addActionBar()
    {
        //actionbar customisation
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_back);
        View v=getSupportActionBar().getCustomView();
        location=(ImageView)v.findViewById(R.id.action_bar_location);
        location.setVisibility(View.INVISIBLE);
        title=(TextView)v.findViewById(R.id.bar_title);
        title.setVisibility(View.INVISIBLE);
        back=(ImageView)v.findViewById(R.id.action_bar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void addWidgets()
    {
        updateProductName=findViewById(R.id.update_product_name);
        updateProductPrice=findViewById(R.id.update_product_price);
        updateProductDescription=findViewById(R.id.update_product_description);
        updateProductCategory=findViewById(R.id.update_product_category);
        updateProductImage=findViewById(R.id.update_product_image);
        update=findViewById(R.id.update_product_but);
        delete=findViewById(R.id.delete_product_but);
        alert=findViewById(R.id.update_sold_message);

    }

    private void hideWidgets()
    {
        updateProductName.setFocusable(false);
        updateProductPrice.setFocusable(false);
        updateProductDescription.setFocusable(false);
        update.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
    }


}
