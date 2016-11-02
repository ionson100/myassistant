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

/**
 * Created by USER on 12.10.2016.
 */

public class DialogEditWord extends DialogFragment {

    private   EditText mEditText;
    private MDictionary mWord;
    private IAction mIAction;

    public DialogEditWord() {}

    public void setmWord(MDictionary s){
        mWord = s;
    }

    public void setIAction(IAction iAction){

        this.mIAction = iAction;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        View v = vi.inflate(R.layout.dialog_edit_word, null);
        mEditText = (EditText) v.findViewById(R.id.edit_text);
        mEditText.setText(mWord.valueWord);
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
        String strNew= mEditText.getText().toString().trim();
        if(strNew.length()==0){
            Toast.makeText(getContext(), "Поле не заполнено", Toast.LENGTH_SHORT).show();
            return;
        }
        mWord.valueWord= mEditText.getText().toString().trim();
        Toast.makeText(getContext(), "Edited", Toast.LENGTH_SHORT).show();
        mIAction.action(mWord);
        dismiss();
    }
}
