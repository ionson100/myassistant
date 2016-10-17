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

/**
 * Created by USER on 12.10.2016.
 */
public class DialogAddWord extends DialogFragment {


    private EditText mKey;
    private EditText mValue;
    private IAction mIAction;

    public void setIAction(IAction iAction) {

        this.mIAction = iAction;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        View v = vi.inflate(R.layout.dialog_add_word, null);
        mKey = (EditText) v.findViewById(R.id.key_word);
        mValue = (EditText) v.findViewById(R.id.value_word);
        v.findViewById(R.id.bt_add_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });


        builder.setView(v);
        return builder.create();
    }

    private void save() {
        if(mKey.getText().toString().trim().length()==0){
            Toast.makeText(getContext(), R.string.qw, Toast.LENGTH_SHORT).show();
            return;
        }
        if(mValue.getText().toString().trim().length()==0){
            Toast.makeText(getContext(), R.string.asd, Toast.LENGTH_SHORT).show();
            return;
        }
        if(!mValue.getText().toString().toUpperCase().contains(mKey.getText().toString().toUpperCase())){
            Toast.makeText(getContext(), R.string.asdr, Toast.LENGTH_SHORT).show();
            return;
        }
        MDictionary mDictionary=new MDictionary();
        mDictionary.keyWord= mKey.getText().toString();
        mDictionary.valueWord= mValue.getText().toString();
        mDictionary.setSelect(true);

        mIAction.action(mDictionary);
        dismiss();
    }


}
