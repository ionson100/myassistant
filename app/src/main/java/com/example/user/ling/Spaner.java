package com.example.user.ling;

import android.text.Spannable;

import java.util.ArrayList;
import java.util.List;


class Spaner{
    private Spannable spannable;

   Spaner(Spannable spannable){
       this.spannable = spannable;
   }

    private List<Object> objectList=new ArrayList<>();

    public void add(Object o,int star,int end){
        objectList.add(o);
        spannable.setSpan(o,star,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    void clear(){
        for (Object o : objectList) {
            spannable.removeSpan(o);
        }
        objectList=new ArrayList<>();
    }

}
