package com.example.user.ling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import static com.example.user.ling.R.id.red_star;


public class MyArrayAdapterWord extends ArrayAdapter<MDictionary> {


    private final int resource;

    public MyArrayAdapterWord(Context context, int resource, List<MDictionary> objects) {
        super(context, resource, objects);

        this.resource = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;

        final MDictionary p = getItem(position);

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            mView = vi.inflate(resource, null);
        TextView text_word= (TextView) mView.findViewById(R.id.text_word);
        ImageView imageView= (ImageView) mView.findViewById(red_star);
        text_word.setText(p.valueWord);
        if(imageView!=null){
            if(p.isSelect){
                imageView.setVisibility(View.VISIBLE);
            }else{
                imageView.setVisibility(View.INVISIBLE);
            }
        }
        mView.setTag(p);


        return mView;
    }

}
