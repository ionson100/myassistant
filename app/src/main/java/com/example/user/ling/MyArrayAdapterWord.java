package com.example.user.ling;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import static com.example.user.ling.R.id.red_star;


class MyArrayAdapterWord extends ArrayAdapter<MDictionary> {

    private final int mResource;

    MyArrayAdapterWord(Context context, int resource, List<MDictionary> objects) {
        super(context, resource, objects);
        this.mResource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final MDictionary p = getItem(position);
        View mView =  LayoutInflater.from(getContext()).inflate(mResource, null);
        TextView text_word= (TextView) mView.findViewById(R.id.text_word);
        ImageView imageView= (ImageView) mView.findViewById(red_star);
        assert p != null;
        text_word.setText(p.valueWord);
        if(imageView!=null){
            if(p.isSelect()){
                imageView.setVisibility(View.VISIBLE);
            }else{
                imageView.setVisibility(View.INVISIBLE);
            }
        }
        mView.setTag(p);
        return mView;
    }
}
