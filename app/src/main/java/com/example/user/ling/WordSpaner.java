package com.example.user.ling;

import android.app.Activity;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * раскрасчик текста
 */

class WordSpaner {

    private final String baseText;
    private TextView textView;

    private final String[] words;

    WordSpaner(TextView textView){

        this.baseText = textView.getText().toString().toUpperCase();
        this.textView = textView;
        words=new String[Utils.getSelectWordses().size()];
        for (int i = 0; i < Utils.getSelectWordses().size(); i++) {
            words[i]=Utils.getSelectWordses().get(i).keyWord.trim().toUpperCase();
        }
    }

    WordSpaner(TextView textView, String baseText){

        this.baseText = textView.getText().toString().toUpperCase();
        this.textView = textView;
        words=new String[1];
        words[0]=baseText.toUpperCase();
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
    void bold(Activity activity){
        if(textView.getTag()==null){
            textView.setTag(new Spaner((Spannable)textView.getText()));
        }
        Spaner spaner = (Spaner) textView.getTag();
        spaner.clear();


        for (String word : words) {

            if(word==null) continue;
            Pattern pattern = Pattern.compile(word);
            Matcher matcher = pattern.matcher(baseText);


            while (matcher.find()) {
                matcher.start();
                spaner.add(new TextAppearanceSpan(activity, R.style.myStyleBold), matcher.start(), matcher.end());
                //s.setSpan(new ForegroundColorSpan(Settings.core().colorSelectWords), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
