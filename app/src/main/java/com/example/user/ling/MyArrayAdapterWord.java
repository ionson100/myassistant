package com.example.user.ling;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import static com.example.user.ling.R.id.red_star;


class MyArrayAdapterWord extends ArrayAdapter<MDictionary> implements IMDictionaryList{

    private final Context context;
    private final int mResource;
    private final List<MDictionary> objects;
    private final Activity activity;

    MyArrayAdapterWord(Context context, int resource, List<MDictionary> objects, Activity activity) {
        super(context, resource, objects);
        this.context = context;
        this.mResource = resource;
        this.objects = objects;
        this.activity = activity;
        List<MDictionary> rem=new ArrayList<>();
        for (MDictionary object : objects) {
            if(object.keyWord==null||object.valueWord==null){
                rem.add(object);
            }
        }
        objects.removeAll(rem);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final MDictionary p = getItem(position);
        View mView =  LayoutInflater.from(getContext()).inflate(mResource, null);
        TextView text_word= (TextView) mView.findViewById(R.id.text_word);
        ImageView imageView= (ImageView) mView.findViewById(red_star);
        assert p != null;
        text_word.setText(p.valueWord.trim(),TextView.BufferType.SPANNABLE);
        new WordSpaner(text_word,p.keyWord.trim()).bold(activity);
        //text_word.setText(p.valueWord);

        //mTextCore.setText(text, TextView.BufferType.SPANNABLE);
        //new WordColor(mTextCore).paint();


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

    @Override
    public List<MDictionary> getList() {
        return objects;
    }
}
