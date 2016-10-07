package com.example.user.ling;

import com.example.user.ling.orm2.Configure;
import com.example.user.ling.orm2.ISession;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by USER on 07.10.2016.
 */

public class Utils {
    private static List<SelectWords> list=null;
    public synchronized static List<SelectWords> getSelectWordses(){
        if(list==null){
            list=Configure.getSession().getList(SelectWords.class,null);
        }
        return list;
    }

    public static boolean addSelectWord(String s){
        boolean result=false;
        int hash=  s.hashCode();
        ISession ses = Configure.getSession();
        List<SelectWords> listq=ses.getList(SelectWords.class," hash = "+String.valueOf(hash));
        if(listq.size()==0){
            SelectWords words=new SelectWords();
            words.hash=hash;
            words.text=s;
            ses.insert(words);
            result=true;

        }
        list=null;
        return result;
    }

    public static boolean containsWord( String s){
        boolean res=false;

        for (SelectWords words : getSelectWordses()) {
            if(words.text.equals(s)){
                res=true;
                break;
            }
        }
        return  res;
    }

    public static void removeSelect(SelectWords selectWords) {
        Configure.getSession().delete(selectWords);
        list=null;
    }
    public static String readFile(String filename)
    {
        String content = "";
        File file = new File(filename); //for ex foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader !=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }
}
