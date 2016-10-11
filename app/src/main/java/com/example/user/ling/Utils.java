package com.example.user.ling;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.widget.Toast;

import com.example.user.ling.orm2.Configure;
import com.example.user.ling.orm2.ISession;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static android.R.attr.action;
import static android.R.attr.breadCrumbShortTitle;

/**
 * Created by USER on 07.10.2016.
 */

public class Utils {
    private static List<SelectWords> list=null;
    public synchronized static List<SelectWords> getSelectWordses(){
        if(list==null){
            list=Configure.getSession().getList(SelectWords.class,null);
        }
        return list;
    }

    public static boolean addSelectWord(String s,Context context){
        boolean result=false;
        int hash=  s.hashCode();
        ISession ses = Configure.getSession();
        List<SelectWords> listq=ses.getList(SelectWords.class," hash = "+String.valueOf(hash));
        if(listq.size()==0){
            SelectWords words=new SelectWords();
            words.hash=hash;
            words.text=s;
            ses.insert(words);
            result=true;
            Toast.makeText(context, "Add", Toast.LENGTH_SHORT).show();

        }
        list=null;
        return result;
    }

    public static boolean containsWord( String s){
        boolean res=false;

        for (SelectWords words : getSelectWordses()) {
            if(words.text.equals(s)){
                res=true;
                break;
            }
        }
        return  res;
    }

    public static void removeSelect(SelectWords selectWords,Context context) {
        Configure.getSession().delete(selectWords);
        list=null;
        if(context!=null)
        Toast.makeText(context, R.string.removed, Toast.LENGTH_SHORT).show();
    }
    public static String readFile(String filename)
    {
        String content = "";
        File file = new File(filename); //for ex foo.txt
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
   public static String getStringAccert(String fileName, Context context){
        AssetManager am = context.getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        BufferedReader r = null;
        if (is != null) {
            r = new BufferedReader(new InputStreamReader(is));
        }
        StringBuilder total = new StringBuilder();
        String line;
        try {
            if (r != null) {
                while ((line = r.readLine()) != null) {
                    total.append(line).append('\n');
                }
            }
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      return   total.toString();
    }
    public static void messageBox(final String title, final String message, final Activity activity, final IAction iAction) {
        if (activity == null) return;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder
                        .setTitle(title)
                        .setMessage(message)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

    public static boolean removeSelectE(String string, Context context) {
        boolean res=false;
        SelectWords d=null;
        for (SelectWords words : list) {
            if(words.text.equals(string)){
                d=words;
                break;
            }
        }
        if(d!=null){
            Configure.getSession().delete(d);
            list=null;
            Toast.makeText(context, R.string.removed, Toast.LENGTH_SHORT).show();
            res=true;
        }

       return res;

    }

    public static ProgressDialog factoryDialog(Activity mActivity, String msg, final IAction iAction) {
        ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setMessage(msg);
        dialog.setIndeterminate(true);
        if (iAction == null) {
            dialog.setCancelable(false);
        } else {
            dialog.setCancelable(true);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    iAction.action(null);
                }
            });
        }


        return dialog;
    }
}

interface IAction{
    void action(Object o);
}
