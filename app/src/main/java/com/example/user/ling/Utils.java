package com.example.user.ling;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Display;

import com.example.user.ling.tranlate.Language;
import com.example.user.ling.tranlate.Translate;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.R.attr.path;


public class Utils {

    static int sIndexSurogat;
    static final String[] sListABC = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","Y","Z" };

    public static void zerabledSelect(){
        selectList=null;
    }
    private static List<MDictionary> selectList;
    synchronized static List<MDictionary> getSelectWordses(){

        if(selectList==null){
            selectList =new ArrayList<>();
            for (MDictionary mDictionary : MainActivity.mDictionaryList) {
                if(mDictionary.isSelect()){
                    selectList.add(mDictionary);
                }
            }
            Collections.sort(selectList, new Comparator<MDictionary>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public int compare(MDictionary mDictionary, MDictionary t1) {
                    return Integer.compare(mDictionary.index,t1.index);
                }
            });
        }

            return selectList;
    }







     static String readFile(String filename)
    {
        String content = "";
        File file = new File(filename);
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader !=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }


     static void messageBox(final String title, final String message, final Activity activity, final IAction iAction) {
        if (activity == null) return;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder
                        .setTitle(title)
                        .setMessage(message)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (iAction != null) {
                                    iAction.action(null);
                                }
                                dialog.dismiss();
                            }

                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }





    public static ProgressDialog factoryDialog(Activity mActivity, String msg, final IAction iAction) {
        ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setMessage(msg);
        dialog.setIndeterminate(true);
        if (iAction != null) {
            dialog.setCancelable(false);
        } else {
            dialog.setCancelable(true);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if(iAction!=null)
                    iAction.action(null);
                }
            });
        }


        return dialog;
    }

    static void SenderYandex(String selectedText, List<MDictionary> dictionaryArrayList, Activity activity,IAction iAction) {
        String translatedText="";
        try {
            Translate.setKey("trnsl.1.1.20161006T095643Z.9887c471401acf62.edb45ac820c51c1dc67ee16076cb0390c4806133");
            if(Settings.core().directTraslate==false){
                translatedText = Translate.execute(selectedText, Language.ENGLISH, Language.RUSSIAN,activity,iAction);
            }else{
                translatedText = Translate.execute(selectedText, Language.RUSSIAN, Language.ENGLISH,activity,iAction);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            Gson sd3 = new Gson();
            TempSender res = sd3.fromJson(translatedText, TempSender.class);
            StringBuilder stringBuilder = new StringBuilder(selectedText+" - ");
            for (String string : res.text) {
                stringBuilder.append(string).append("\n");
            }
            MDictionary mDictionary=new MDictionary();
            mDictionary.keyWord=selectedText;
            mDictionary.valueWord=stringBuilder.toString();
            dictionaryArrayList.add(mDictionary);
        }catch (Exception ex){
            MDictionary mDictionary=new MDictionary();
            mDictionary.valueWord=translatedText;
            dictionaryArrayList.add(mDictionary);
        }
    }
    public static void showHelpNote(Activity activity, String s) {
        AssetManager am = activity.getAssets();
        InputStream is = null;
        try {
            is = am.open(s);
        } catch (IOException ignored) {
            return;
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (IOException ignored) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder
                .setMessage(Html.fromHtml(total.toString()))
                .setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton(activity.getString(R.string.close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        alert.getWindow().setLayout(width-30, height-30);

    }

    public static File[] getArrayFiles(File  fi){

        if(!fi.exists()) return null;

        List<File> filesFolder= new ArrayList<>();
        List<File> filesCore= new ArrayList<>();
        for (File file : fi.listFiles()) {
            if(file.isDirectory()){
                filesFolder.add(file);
            }else{
                filesCore.add(file);
            }
        }

        Collections.sort(filesCore, new Comparator<File>() {
            @Override
            public int compare(File file, File t1) {
                return file.getPath().compareTo(t1.getPath());
            }
        });

        filesFolder.addAll(filesCore);



        return filesFolder.toArray(new File[filesFolder.size()]);

    }

    public static void copyAssets(Context activity, String path, String filen) {
        AssetManager assetManager = activity.getAssets();
        String[] files = new  String[1];
        files[0]=filen;

        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(path+"/"+ filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {

            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {

                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {

                    }
                }
            }
        }
    }
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public static String read(File file) {
        StringBuilder stringBuffer = new StringBuilder();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return stringBuffer.toString();
    }

}

