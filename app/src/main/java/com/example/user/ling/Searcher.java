package com.example.user.ling;

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
        Utils.SenderYandex(word,list2,activity);
        for (MDictionary dictionary : list2) {
            list.add(0,dictionary);
        }
        return list;
    }
}
