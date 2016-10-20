package com.example.user.ling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by USER on 20.10.2016.
 */

public class ArrayAdapterFolderText extends ArrayAdapter<File> {
    private final IAction iAction;

    public ArrayAdapterFolderText(Context context, int resource, List<File> objects, IAction iAction) {
        super(context, resource, objects);
        this.iAction = iAction;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final File p = getItem(position);

        View mView;

        mView =  LayoutInflater.from(getContext()).inflate(R.layout.item_directory_file, null);
        ImageView imageViewFolder = (ImageView) mView.findViewById(R.id.imageFolder);
        ImageView imageViewFileTxt = (ImageView) mView.findViewById(R.id.image_file_txt);
        ImageView imageViewFileHtml = (ImageView) mView.findViewById(R.id.image_file_html);
        imageViewFileTxt.setVisibility(View.GONE);
        imageViewFileHtml.setVisibility(View.GONE);

        if(p.isDirectory()){
            imageViewFolder.setVisibility(View.VISIBLE);

        }else{
            imageViewFolder.setVisibility(View.GONE);
            String string = p.getName().substring(p.getName().lastIndexOf(".")+1);

            if(string.toUpperCase().equals("TXT")){
                imageViewFileTxt.setVisibility(View.VISIBLE);
            } else if(string.toUpperCase().equals("HTML")){
                imageViewFileHtml.setVisibility(View.VISIBLE);
            }else{
                imageViewFolder.setVisibility(View.INVISIBLE);
            }
        }

        mView.findViewById(R.id.panel_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAction.action(p);
            }
        });

        TextView text_word= (TextView) mView.findViewById(R.id.text_file_name);
        if(p.getName().lastIndexOf(".")==-1){
            text_word.setText(p.getName());
        }else{
            text_word.setText(p.getName().substring(0,p.getName().lastIndexOf(".")));
        }

        mView.setTag(p);
        return mView;
    }
}
