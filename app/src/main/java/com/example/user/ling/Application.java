package com.example.user.ling;

import android.media.MediaScannerConnection;
import android.os.Environment;

import com.example.user.ling.orm2.Configure;

import java.io.File;

import static android.R.attr.path;

public class Application extends android.app.Application {
    static final String sPath1 =Environment.getExternalStorageDirectory().toString() + "/linqion/linq.sqlite";
    static final String sPath2 =Environment.getExternalStorageDirectory().toString() + "/linqion/data";
    @Override
    public void onCreate() {
        super.onCreate();

        File d=new File(sPath2);
        if(!d.exists()){
            d.mkdirs();
            d.setExecutable(true);
            d.setReadable(true);
            d.setWritable(true);
            MediaScannerConnection.scanFile(this, new String[] {sPath2}, null, null);
        }
        new Configure(sPath1,getApplicationContext(), false);
        com.settings.ion.mylibrary.Reanimator.intContext(getApplicationContext());
    }
}
