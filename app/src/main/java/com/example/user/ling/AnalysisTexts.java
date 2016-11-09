package com.example.user.ling;

import android.app.Activity;
import android.content.Context;
import android.media.midi.MidiDeviceInfo;
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

import static com.example.user.ling.MainActivity.mDictionaryList;


public class AnalysisTexts {


    public static void run(MainActivity activity, ListView mListView) {


        File file=new File(Application.sPath4);
        if(!file.exists()) return;
        List<File> fileList=new ArrayList<>();
        recursion(file,fileList);
        Map<String,MDictionary> map=new HashMap<>();
        for (File ff : fileList) {
            String string = Utils.read(ff);
            if(string!=null&&string.length()>0){
                String[] s =string.split(" ");
                for (String s1 : s) {
                    if(s1.length()<2){
                        continue;
                    }
                    if(map.containsKey(s1)){
                        map.get(s1).anInt++;
                    }else{
                        MDictionary dictionary=new MDictionary();
                        dictionary.keyWord=s1;
                        dictionary.anInt=1;
                        map.put(s1,dictionary);
                    }
                }
            }
        }
        List<MDictionary> pairWords=new ArrayList<>(map.size());

        for (Map.Entry<String, MDictionary> ss : map.entrySet()) {
            pairWords.add( ss.getValue());
        }
        Collections.sort(pairWords, new Comparator<MDictionary>() {
            @Override
            public int compare(MDictionary o1, MDictionary o2) {
                return Integer.compare(o1.anInt,o2.anInt);
            }
        });

        Collections.reverse(pairWords);

        Map<String,MDictionary> dictionaryMap=new HashMap<>();
        for (MDictionary dictionary : activity.mDictionaryList) {
            String string = dictionary.keyWord.trim().toUpperCase();
            if(dictionaryMap.containsKey(string)){

            }else{
                dictionaryMap.put(string,dictionary);
            }
        }
        for (MDictionary pairWord : pairWords) {
            String ss=pairWord.keyWord.trim().toUpperCase().replace(".","").replace(",","");
            if(dictionaryMap.containsKey(ss)){
                pairWord.valueWord=dictionaryMap.get(ss).valueWord;
            }
        }

        activity.mDictionaryListAnalise=pairWords;



        ArrayAdapter<MDictionary> mAdapter=new MyArrayAdapterAnalises(activity,R.layout.item_analises,pairWords,activity,activity.mDictionaryList,dictionaryMap);
        mListView.setAdapter(mAdapter);
        Settings.core().setPath(null);

    }
    static void recursion(File file, List<File> fileList){
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

class MyArrayAdapterAnalises extends ArrayAdapter<MDictionary> implements IMDictionaryList {

    private final Context context;
    private final int mResource;
    private final List<MDictionary> objects;
    private Activity activity;
    private List<MDictionary> mDictionaryList;
    private Map<String, MDictionary> map;


    MyArrayAdapterAnalises(Context context, int resource, List<MDictionary> objects, Activity activity, List<MDictionary> mDictionaryList,Map<String,MDictionary> map) {
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
        final MDictionary p = getItem(position);
        View mView =  LayoutInflater.from(getContext()).inflate(mResource, null);

        TextView word= (TextView) mView.findViewById(R.id.word_x);
        TextView count= (TextView) mView.findViewById(R.id.word_count_x);
        final TextView tranclate= (TextView) mView.findViewById(R.id.word_translate_x);
        final ImageView imageView= (ImageView) mView.findViewById(R.id.bt_x);
        if(p.valueWord!=null){
            tranclate.setText(p.valueWord);
            imageView.setVisibility(View.GONE);
        }else {
            tranclate.setText("");
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity==null){
                    activity=MainActivity.getActivity();
                }
                List<MDictionary> list= new ArrayList<>();
                Utils.SenderYandex(p.keyWord,list,activity);
                DialogSearshWord selectText=new DialogSearshWord();
                selectText.setDictionary(list);
                selectText.setiAction(new IAction() {
                    @Override
                    public void action(Object o) {
                        p.valueWord=o.toString();
                        String ss=p.keyWord.trim().toUpperCase().replace(".","").replace(",","");
                        MDictionary dd= new MDictionary();
                        dd.keyWord=p.keyWord;
                        dd.valueWord=o.toString();
                        Configure.getSession().insert(dd);
                        mDictionaryList.add(dd);
                        if(map!=null){
                            map.put(ss,dd);
                        }

                        tranclate.setText(o.toString());
                        imageView.setVisibility(View.GONE);
                    }
                });

                    selectText.show(((MainActivity)activity).getSupportFragmentManager(),"skdsjf");

            }
        });


        word.setText(p.keyWord);
        count.setText(String.valueOf(p.anInt));


        mView.setTag(p);
        return mView;
    }

    @Override
    public List<MDictionary> getList() {
        return objects;
    }
}
