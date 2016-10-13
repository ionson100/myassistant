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
import android.widget.Toast;

import com.example.user.ling.orm2.Configure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class DialogSearshWord extends DialogFragment {

    private  ListView listView;
    private List<MDictionary> mDictionaryList;
    public void setDictionary(List<MDictionary> mDictionaryList){
        this.mDictionaryList=mDictionaryList;
    }

    private MyArrayAdapterWord adapter;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        View v = vi.inflate(R.layout.dialog_select_text, null);
        listView= (ListView) v.findViewById(R.id.list_view_text);
        adapter = new MyArrayAdapterWord(getContext(), R.layout.simple_list_item_1, mDictionaryList);
        listView.setAdapter(adapter);
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
                final int position = aMenuInfo.position;
                final MDictionary mDictionary=mDictionaryList.get(position);
                if(!mDictionary.isSelect){
                    contextMenu.add(R.string.add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            mDictionary.isSelect=true;
                            mDictionary.index=++Utils.indexSurogat;
                            Configure.getSession().update(mDictionary);
                            Toast.makeText(getContext(), R.string.add, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
                }else{
                    contextMenu.add(R.string.remove).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            mDictionary.isSelect=false;
                            mDictionary.index=0;
                            mDictionaryList.remove(mDictionary);
                            Configure.getSession().update(mDictionary);
                            adapter.notifyDataSetChanged();
                            ((MainActivity)getActivity()).notifyE();
                            Toast.makeText(getContext(), R.string.removed, Toast.LENGTH_SHORT).show();


                            return false;
                        }
                    });
                }
                contextMenu.add(R.string.edit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        DialogEditWord editWord=new DialogEditWord();
                        editWord.setWord(mDictionary);
                        editWord.setIAction(new IAction() {
                            @Override
                            public void action(Object o) {
                             Configure.getSession().update(o) ;
                                adapter.notifyDataSetChanged();
                                ((MainActivity)getActivity()).notifyE();
                                Toast.makeText(getContext(), R.string.edited, Toast.LENGTH_SHORT).show();
                            }
                        });

                        editWord.show(getActivity().getSupportFragmentManager(),"ada");
                        return false;

                    }

                });
                contextMenu.add(R.string.addWord).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        DialogAddWord editWord=new DialogAddWord();
                        editWord.setIAction(new IAction() {
                            @Override
                            public void action(Object o) {
                                Configure.getSession().insert(o);
                                MainActivity.mDictionaryList.add(0,(MDictionary) o);
                                mDictionaryList.add((MDictionary) o);




                                 ((MainActivity)getActivity()).listRefrach();
                                 adapter.notifyDataSetChanged();
                                 Toast.makeText(getContext(), R.string.addnew, Toast.LENGTH_SHORT).show();
                            }
                        });
                        editWord.show(getActivity().getSupportFragmentManager(),"ada");

                        return false;
                    }
                });

                contextMenu.add(R.string.delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem menuItem) {
                        Utils.messageBox(getString(R.string.asddd), getString(R.string.dasffsf), getActivity(), new IAction() {
                            @Override
                            public void action(Object o) {

                                Configure.getSession().delete(mDictionary);
                                mDictionaryList.remove(mDictionary);
                                MainActivity.mDictionaryList.remove(mDictionary);
                                ((MainActivity)getActivity()).listRefrach();
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), R.string.removed_permanent, Toast.LENGTH_SHORT).show();

                            }
                        });
                        return false;
                    }
                });

            }
        });

        builder.setView(v);
        return builder.create();
    }

}
