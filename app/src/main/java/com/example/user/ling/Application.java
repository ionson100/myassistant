package com.example.user.ling;

import android.os.Environment;

import com.example.user.ling.orm2.Configure;

import java.io.File;

public class Application extends android.app.Application {
    static final String sPath1 =Environment.getExternalStorageDirectory().toString() + "/linqion/linq.sqlite";
    static final String sPath2 =Environment.getExternalStorageDirectory().toString() + "/linqion/data";
    @Override
    public void onCreate() {
        super.onCreate();
        new Configure(sPath1,getApplicationContext(), false);
        File d=new File(sPath2);
        if(!d.exists()){
            d.mkdirs();
        }
        com.settings.ion.mylibrary.Reanimator.intContext(getApplicationContext());
    }
}
