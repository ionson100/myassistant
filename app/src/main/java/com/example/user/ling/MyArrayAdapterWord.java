package com.example.user.ling;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import static android.R.id.list;
import static com.example.user.ling.R.id.red_star;


public class MyArrayAdapterWord extends ArrayAdapter<String> {



    public MyArrayAdapterWord(Context context, int resource, List<String> objects) {
        super(context, resource, objects);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;

        final String p = getItem(position);

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            mView = vi.inflate(R.layout.simple_list_item_1, null);
        TextView text_word= (TextView) mView.findViewById(R.id.text_word);
        ImageView imageView= (ImageView) mView.findViewById(red_star);
        if(Utils.containsWord(p)){
            imageView.setVisibility(View.VISIBLE);
        }else{
            imageView.setVisibility(View.INVISIBLE);
        }
        text_word.setText(p);


        return mView;
    }

}
