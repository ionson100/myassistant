package com.example.user.ling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DialogWordRandom extends DialogFragment {

    String word="";

    public void setWord(String word){
        this.word=word;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        View v = vi.inflate(R.layout.dialog_word, null);
        TextView textView= (TextView) v.findViewById(R.id.word_e);
        v.findViewById(R.id.add_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        textView.setText(word);
        v.findViewById(R.id.add_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(word.trim().length()>0){
                    Utils.addSelectWord(word,getContext());

                }
            }
        });
        builder.setView(v);


        return builder.create();
    }
}

