package com.example.user.ling;

import android.os.Environment;

import com.example.user.ling.orm2.Configure;

import java.io.File;

public class Application extends android.app.Application {
    static final String path1=Environment.getExternalStorageDirectory().toString() + "/linqion/linq.sqlite";
    static final String path2=Environment.getExternalStorageDirectory().toString() + "/linqion/data";
    @Override
    public void onCreate() {
        super.onCreate();
        new Configure(path1,getApplicationContext(), false);
        File d=new File(path2);
        if(!d.exists()){
            d.mkdirs();
        }
    }
}
