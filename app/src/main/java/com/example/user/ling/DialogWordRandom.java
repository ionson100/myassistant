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
import com.example.user.ling.orm2.Configure;

public class DialogWordRandom extends DialogFragment {

    private MDictionary mDictionary;

    public void setDictionary(MDictionary dictionary){
        this.mDictionary=dictionary;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_word, null);

        TextView mTextCore= (TextView) v.findViewById(R.id.word_e);
        mTextCore.setText(mDictionary.valueWord, TextView.BufferType.SPANNABLE);
        new WordSpaner(mTextCore,mDictionary.keyWord).bold(getActivity());

        v.findViewById(R.id.add_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDictionary.valueWord.trim().length()>0){
                    mDictionary.setSelect(true);

                    Configure.getSession().update(mDictionary);
                    Toast.makeText(getContext(), R.string.add, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setView(v);


        return builder.create();
    }
}

