package com.example.dealin.user.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dealin.R;
import com.google.gson.Gson;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Product> mData;


    public RecyclerViewAdapter(Context mContext,List<Product>mData){
        this.mContext=mContext;
        this.mData=mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        view=layoutInflater.inflate(R.layout.cardview_products,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position){
       try {
           holder.tv_product_title.setText(mData.get(position).getTitle());
           holder.tv_product_price.setText(mData.get(position).getPrice());
           holder.iv_product_thumbnail.setImageBitmap(mData.get(position).getThumbnail());
       }catch (Exception e)
       {

       }

           //click listener
           holder.cardView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   try {
                       Intent intent = new Intent(mContext, ShowProduct.class);
                      /* intent.putExtra("id",mData.get(position).getId());
                       intent.putExtra("Title", mData.get(position).getTitle());
                       intent.putExtra("Price", mData.get(position).getPrice());
                       intent.putExtra("Category", mData.get(position).getCategory());
                       intent.putExtra("Description", mData.get(position).getDescription());
                       intent.putExtra("Thumbnail",mData.get(position).getThumbnailString());*/
                        Gson gson=new Gson();
                        String pdt=gson.toJson(mData.get(position),Product.class);
                        intent.putExtra("product",pdt);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       mContext.startActivity(intent);
                   }catch (Exception e)
                   {

                   }

               }
           });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_product_title,tv_product_price;
            ImageView iv_product_thumbnail;
            CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_product_title=(TextView)itemView.findViewById(R.id.product_name);
            tv_product_price=(TextView)itemView.findViewById(R.id.product_price);
            iv_product_thumbnail=(ImageView)itemView.findViewById(R.id.product_image);
            cardView=(CardView)itemView.findViewById(R.id.product_cardview);
        }
    }
}
