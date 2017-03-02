package com.example.user.ling;

import android.app.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 31.10.2016.
 */
public class Searcher {

    public static List<MDictionary> getDictionarys(List<MDictionary> mDictionaries,String word,MainActivity activity){
        List<MDictionary> list=new ArrayList<>();
        for (MDictionary mDictionary : mDictionaries) {
            if(mDictionary.keyWord.trim().toUpperCase().contains(word.trim().toUpperCase())){
                list.add(mDictionary);
            }
        }
        List<MDictionary> list2=new ArrayList<>();
        final ProgressDialog dialog = Utils.factoryDialog(activity, "Запрос на Яндекс", null);
        dialog.show();
        Utils.SenderYandex(word, list2, activity, new IAction() {
            @Override
            public void action(Object o) {
                if(dialog!=null){
                    dialog.cancel();
                }
            }
        });
        for (MDictionary dictionary : list2) {
            list.add(0,dictionary);
        }
        return list;
    }
}
