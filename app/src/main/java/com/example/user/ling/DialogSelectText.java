package com.example.user.ling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.ling.orm2.ISession;

import java.io.File;

/**
 * Created by USER on 07.10.2016.
 */

public class DialogSelectText extends DialogFragment {


    private ISelectText mISelectText;
    private File[] mFiles;
    private String[] strings;

    public void setData(ISelectText mISelectText, File[] files){
        this.mFiles=files;
        this.mISelectText=mISelectText;
        strings=new String[files.length];
        for (int i=0;i<files.length;i++){
            String ss = files[i].getPath().substring(files[i].getPath().lastIndexOf(File.separator) + 1);
            strings[i]=ss;
        }

    }




    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        View v = vi.inflate(R.layout.dialog_select_text, null);
        ListView listView= (ListView) v.findViewById(R.id.list_view_text);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, strings);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               mISelectText.activate(mFiles[position]);
                dismiss();
            }
        });
        builder.setView(v);
        return builder.create();
    }

    public interface ISelectText{
        void activate(File file);
    }

}
