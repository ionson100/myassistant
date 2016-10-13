package com.example.user.ling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.user.ling.orm2.Configure;
import com.example.user.ling.orm2.ISession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 12.10.2016.
 */

public class DialogEditWord extends DialogFragment {

    private   EditText editText;
    private MDictionary word;
    private IAction iAction;

    public DialogEditWord() {}

    public void setWord(MDictionary s){
        word = s;
    }

    public void setIAction(IAction iAction){

        this.iAction = iAction;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        View v = vi.inflate(R.layout.dialog_edit_word, null);
        editText= (EditText) v.findViewById(R.id.edit_text);
        editText.setText(word.valueWord);
        v.findViewById(R.id.bt_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        builder.setView(v);
        return builder.create();
    }

    void save(){
        String strNew=editText.getText().toString().trim();
        if(strNew.length()==0){
            Toast.makeText(getContext(), "Поле не заполнено", Toast.LENGTH_SHORT).show();
            return;
        }

        ISession session = Configure.getSession();
        word.valueWord=editText.getText().toString().trim();


        Toast.makeText(getContext(), "Edited", Toast.LENGTH_SHORT).show();
        iAction.action(word);
        dismiss();
    }
}
