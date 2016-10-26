package com.example.user.ling;

import android.media.MediaScannerConnection;
import android.os.Environment;

import com.example.user.ling.orm2.Configure;

import java.io.File;

import static android.R.attr.path;

public class Application extends android.app.Application {
    static final String sPath1 =Environment.getExternalStorageDirectory().toString() + "/FigliMigli/linq.sqlite";
    static final String sPath2 =Environment.getExternalStorageDirectory().toString() + "/FigliMigli/data";
    static final String sPath3 =Environment.getExternalStorageDirectory().toString() + "/FigliMigli/data/Учебные материалы";
    static final String sPath4 =Environment.getExternalStorageDirectory().toString() + "/FigliMigli/data/Тексты для перевода";
    @Override
    public void onCreate() {
        super.onCreate();
        {
            File d=new File(sPath2);
            if(!d.exists()){
                d.mkdirs();
                d.setExecutable(true);
                d.setReadable(true);
                d.setWritable(true);
                MediaScannerConnection.scanFile(this, new String[] {sPath2}, null, null);
            }
        }
        {
            File d=new File(sPath3);
            if(!d.exists()){
                d.mkdirs();
                d.setExecutable(true);
                d.setReadable(true);
                d.setWritable(true);
                MediaScannerConnection.scanFile(this, new String[] {sPath3}, null, null);
            }
        }
        {
            File d=new File(sPath4);
            if(!d.exists()){
                d.mkdirs();
                d.setExecutable(true);
                d.setReadable(true);
                d.setWritable(true);
                MediaScannerConnection.scanFile(this, new String[] {sPath4}, null, null);
            }
        }
        if(!new File(sPath3+"/"+"have.html").exists()){
            Utils.copyAssets(getApplicationContext(),sPath3,"have.html");
            MediaScannerConnection.scanFile(this, new String[] {sPath3+"/"+"have.html"}, null, null);
        }
        if(!new File(sPath4+"/"+"lesson.txt").exists()){
            Utils.copyAssets(getApplicationContext(),sPath4,"lesson.txt");
            MediaScannerConnection.scanFile(this, new String[] {sPath4+"/"+"lesson.txt"}, null, null);
        }



        new Configure(sPath1,getApplicationContext(), false);
        com.settings.ion.mylibrary.Reanimator.intContext(getApplicationContext());
    }
}
