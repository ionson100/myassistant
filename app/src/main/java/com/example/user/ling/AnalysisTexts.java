package com.example.user.ling;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.ling.orm2.Configure;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class AnalysisTexts {
    private final Activity activity;
    private final ListView mListView;
    private List<MDictionary> mDictionaryList;

    public AnalysisTexts(MainActivity activity, ListView mListView,List<MDictionary> mDictionaryList) {
        this.activity = activity;
        this.mListView = mListView;
        this.mDictionaryList = mDictionaryList;
    }

    public void run() {

        File file=new File(Application.sPath4);
        if(!file.exists()) return;
        List<File> fileList=new ArrayList<>();
        recursion(file,fileList);
        Map<String,pairWord> map=new HashMap<>();
        for (File ff : fileList) {
            String string = Utils.read(ff);
            if(string!=null&&string.length()>0){
                String[] s =string.split(" ");
                for (String s1 : s) {
                    if(s1.equals("")||
                            s1.equals(" ")||
                            s1.equals("-")||
                            s1.equals(".")||
                            s1.equals(",")){
                        continue;
                    }
                    if(map.containsKey(s1)){
                        map.get(s1).anInt++;
                    }else{
                        map.put(s1,new pairWord(s1,1));
                    }
                }
            }
        }
        List<pairWord> pairWords=new ArrayList<>(map.size());

        for (Map.Entry<String, pairWord> ss : map.entrySet()) {
            pairWords.add( ss.getValue());
        }
        Collections.sort(pairWords, new Comparator<pairWord>() {
            @Override
            public int compare(pairWord o1, pairWord o2) {
                return Integer.compare(o1.anInt,o2.anInt);
            }
        });

        Collections.reverse(pairWords);

        Map<String,MDictionary> dictionaryMap=new HashMap<>();
        for (MDictionary dictionary : mDictionaryList) {
            String string = dictionary.keyWord.trim().toUpperCase();
            if(dictionaryMap.containsKey(string)){

            }else{
                dictionaryMap.put(string,dictionary);
            }
        }
        for (pairWord pairWord : pairWords) {
            String ss=pairWord.word.trim().toUpperCase().replace(".","").replace(",","");
            if(dictionaryMap.containsKey(ss)){
                pairWord.translate=dictionaryMap.get(ss).valueWord;
            }
        }



        MyArrayAdapterAnalises ad=new MyArrayAdapterAnalises(activity,R.layout.item_analises,pairWords,activity,mDictionaryList,dictionaryMap);
        mListView.setAdapter(ad);

    }
    void recursion(File file, List<File> fileList){
        if(!file.isDirectory()){
            fileList.add(file);
            return;
        }
        File[] files=file.listFiles();
        for (File ff : files) {
            if(ff.isDirectory()==false){
                fileList.add(ff);
            }else{
                recursion(ff,fileList);
            }
        }
    }


}
class pairWord{
    public pairWord(String word,int i){
        this.word=word;
        this.anInt=i;

    }
    public String word;
    public int anInt;
    public String translate;
}
class MyArrayAdapterAnalises extends ArrayAdapter<pairWord> {

    private final Context context;
    private final int mResource;
    private final List<pairWord> objects;
    private final Activity activity;
    private List<MDictionary> mDictionaryList;
    private Map<String, MDictionary> map;


    MyArrayAdapterAnalises(Context context, int resource, List<pairWord> objects, Activity activity, List<MDictionary> mDictionaryList,Map<String,MDictionary> map) {
            super(context, resource, objects);
        this.context = context;
        this.mResource = resource;
        this.objects = objects;
        this.activity = activity;

        this.mDictionaryList = mDictionaryList;
        this.map = map;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final pairWord p = getItem(position);
        View mView =  LayoutInflater.from(getContext()).inflate(mResource, null);

        TextView word= (TextView) mView.findViewById(R.id.word_x);
        TextView count= (TextView) mView.findViewById(R.id.word_count_x);
        final TextView tranclate= (TextView) mView.findViewById(R.id.word_translate_x);
        final ImageView imageView= (ImageView) mView.findViewById(R.id.bt_x);
        if(p.translate!=null){
            tranclate.setText(p.translate);
            imageView.setVisibility(View.GONE);
        }else {
            tranclate.setText("");
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MDictionary> list= new ArrayList<>();
                Utils.SenderYandex(p.word,list,activity);
                DialogSearshWord selectText=new DialogSearshWord();
                selectText.setDictionary(list);
                selectText.setiAction(new IAction() {
                    @Override
                    public void action(Object o) {
                        p.translate=o.toString();
                        String ss=p.word.trim().toUpperCase().replace(".","").replace(",","");
                        MDictionary dd= new MDictionary();
                        dd.keyWord=p.word;
                        dd.valueWord=o.toString();
                        Configure.getSession().insert(dd);
                        mDictionaryList.add(dd);
                        map.put(ss,dd);
                        tranclate.setText(o.toString());
                        imageView.setVisibility(View.GONE);
                    }
                });
                selectText.show(((MainActivity)activity).getSupportFragmentManager(),"skdsjf");
            }
        });


        word.setText(p.word);
        count.setText(String.valueOf(p.anInt));


        mView.setTag(p);
        return mView;
    }
}
