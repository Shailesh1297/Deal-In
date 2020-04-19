package com.example.dealin.user.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;

public class Product {

    private String title;
    private int id;
    private String category;
    private String description;
    private String price;
    private Bitmap bitmap;
    private String thumbnail;
    public Product()
    {

    }
    public Product(int id,String title, String category, String description, String price, String thumbnail) {
        this.id=id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.price = price;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Bitmap getThumbnail() throws Exception
    {
        bitmap= BitmapFactory.decodeStream((InputStream)new URL(thumbnail).getContent());
        return bitmap;
    }

    public String getThumbnailString()
    {
        return thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
