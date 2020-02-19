package com.example.dealin.utility;

import android.content.Context;
import android.widget.Toast;

public class Helper {

    public static void toast(Context context, String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
