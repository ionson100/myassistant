package com.example.user.ling;

import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * раскрасчик текста
 */

class WordColor {

    private final String baseText;
    private TextView textView;

    private final String[] words;

    WordColor(TextView textView){

        this.baseText = textView.getText().toString().toUpperCase();
        this.textView = textView;
        words=new String[Utils.getSelectWordses().size()];
        for (int i = 0; i < Utils.getSelectWordses().size(); i++) {
            words[i]=Utils.getSelectWordses().get(i).keyWord.trim().toUpperCase();
        }
    }

    void paint(){

        if(textView.getTag()==null){
            textView.setTag(new Spaner((Spannable)textView.getText()));
        }
        Spaner spaner = (Spaner) textView.getTag();
        spaner.clear();


        for (String word : words) {

            Pattern pattern = Pattern.compile(word);
            Matcher matcher = pattern.matcher(baseText);


            while (matcher.find()) {
                matcher.start();
                spaner.add(new ForegroundColorSpan(Settings.core().colorSelectWords), matcher.start(), matcher.end());
                //s.setSpan(new ForegroundColorSpan(Settings.core().colorSelectWords), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }




    }
}
class Spaner{
    private  Spannable spannable;

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
