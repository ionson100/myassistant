package com.example.user.ling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by USER on 10.10.2016.
 */

public class DialogSearshWord extends DialogFragment {

    private String[] strings;
    public void setStrings(String[] strings){
        this.strings=strings;
    }
    private boolean isNotShowMenu;

    public void notShowMenu(){
        isNotShowMenu=true;
    }
    private ArrayAdapter<String> adapter;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        View v = vi.inflate(R.layout.dialog_select_text, null);
        ListView listView= (ListView) v.findViewById(R.id.list_view_text);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, strings);
        listView.setAdapter(adapter);
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
                final int position = aMenuInfo.position;
                if(!isNotShowMenu){
                    contextMenu.add(R.string.add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Utils.addSelectWord(strings[position],getContext());
                            return false;
                        }
                    });
                }else{
                    contextMenu.add(R.string.remove).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                           if(Utils.removeSelectE(strings[position],getContext())){
                               dismiss();
                           }
                            return false;
                        }
                    });
                }

            }
        });

        builder.setView(v);
        return builder.create();
    }

}
