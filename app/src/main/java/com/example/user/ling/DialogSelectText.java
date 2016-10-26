package com.example.user.ling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class DialogSelectText extends DialogFragment {

    private ISelectText mISelectText;
    private File[] mFiles;
    private String[] mStrings;

    public void setData(ISelectText mISelectText, File[] files){
        this.mFiles=files;
        this.mISelectText=mISelectText;
        mStrings =new String[files.length];
        for (int i=0;i<files.length;i++){
            String ss = files[i].getPath().substring(files[i].getPath().lastIndexOf(File.separator) + 1);
            mStrings[i]=ss;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        final View v = vi.inflate(R.layout.dialog_select_text, null);
        final ListView listView= (ListView) v.findViewById(R.id.list_view_text);
        final ArrayAdapterFolderText adapter = new ArrayAdapterFolderText(getContext(), 0, new ArrayList<>(Arrays.asList(mFiles)), new IAction() {
            @Override
            public void action(Object o) {
                recursionShowFolders((File) o,  listView);
            }
        });
        listView.setAdapter(adapter);

        builder.setView(v);
        return builder.create();
    }

    private void recursionShowFolders(File o, final ListView listView) {
        File file= o;
        if(file.isDirectory()==false){
            mISelectText.activate(file);
        }else{
            ArrayAdapterFolderText adapter = new ArrayAdapterFolderText(getContext(), 0, new ArrayList<>(Arrays.asList(Utils.getArrayFiles(file))), new IAction() {
                @Override
                public void action(Object o) {
                    recursionShowFolders((File) o,  listView);
                }
            });
            listView.setAdapter(adapter);
        }
    }

    public interface ISelectText{
        void activate(File file);
    }

}
