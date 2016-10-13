package com.example.user.ling;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Toast;

import com.example.user.ling.orm2.Configure;
import com.example.user.ling.orm2.ISession;
import com.example.user.ling.tranlate.Language;
import com.example.user.ling.tranlate.Translate;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static android.view.View.Z;


public class Utils {

    public static int indexSurogat;
    public static final String[] listABC = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","Y","Z" };

    synchronized static List<MDictionary> getSelectWordses(){

        List<MDictionary> res=new ArrayList<>();
        for (MDictionary mDictionary : MainActivity.mDictionaryList) {
            if(mDictionary.isSelect){
                res.add(mDictionary);
            }
        }
        Collections.sort(res, new Comparator<MDictionary>() {
            @Override
            public int compare(MDictionary mDictionary, MDictionary t1) {
                return Integer.compare(mDictionary.index,t1.index);
            }
        });
       // List<MDictionary> as=Configure.getSession().getList(MDictionary.class," is_select = 1 ");
            return res;
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

    static void SenderYandex(String selectedText, List<MDictionary> dictionaryArrayList, Activity activity) {
        String translatedText="";
        try {
            Translate.setKey("trnsl.1.1.20161006T095643Z.9887c471401acf62.edb45ac820c51c1dc67ee16076cb0390c4806133");
            translatedText = Translate.execute(selectedText, Language.ENGLISH, Language.RUSSIAN,activity);
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
            mDictionary.valueWord=stringBuilder.toString();
            dictionaryArrayList.add(mDictionary);
        }catch (Exception ex){
            MDictionary mDictionary=new MDictionary();
            mDictionary.valueWord=translatedText;
            dictionaryArrayList.add(mDictionary);
        }
    }
}

interface IAction{
    void action(Object o);
}
