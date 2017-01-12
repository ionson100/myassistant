package com.example.user.ling;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;


class SpeechSearch{

    static final int SPEECH=23;


    private ImageButton imageButton;
    private final MainActivity activity;

    SpeechSearch(MainActivity activity){
        this.imageButton = (ImageButton) activity.findViewById(R.id.image_microphone);
        this.activity = activity;
    }
    void activate(){

        if(!isSpeechRecognitionActivityPresented(activity)){
            activity.findViewById(R.id.parent_microphon).setVisibility(View.GONE);
            return;
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // создаем Intent с действием RecognizerIntent.ACTION_RECOGNIZE_SPEECH
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                // добавляем дополнительные параметры:

                if(Settings.core().directTraslate){

                }else{
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                }

                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Голосовой поиск");
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"ru-RU" );//RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

                // стартуем Activity и ждем от нее результата
                activity.startActivityForResult(intent, SPEECH);
            }
        });
    }
    private static boolean isSpeechRecognitionActivityPresented(Activity ownerActivity) {
        try {
            PackageManager pm = ownerActivity.getPackageManager();
            List activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
            if (activities.size() != 0) {
                return true;
            }
        } catch (Exception ignore) {}

        return false;
    }
}
