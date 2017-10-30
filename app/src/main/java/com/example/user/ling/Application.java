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

    }
}
