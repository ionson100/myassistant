package com.example.user.ling;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ListFragment;

import com.settings.ion.mylibrary.Reanimator;
import com.settings.ion.mylibrary.SettingField;
import com.settings.ion.mylibrary.TypeField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Settings implements Serializable {

    public static Settings core() {
        return (Settings) Reanimator.get(Settings.class);
    }

    public static void save() {
        Reanimator.save(Settings.class);
    }

    @SettingField(
            descriptions = R.string.direct_translate,
            index = 1,
            title = R.string.direct_translate_titl,
            typeField = TypeField.BooleanSwitch)
    public boolean directTraslate ;


    @SettingField(
            descriptions = R.string.synch_translate,
            index = 2,
            title = R.string.dsynch_translate_titl,
            typeField = TypeField.BooleanSwitch)
    public boolean synchTraslate ;


    @SettingField(
            index = 3,
            title = R.string.paint_word_titl,
            typeField = TypeField.BooleanSwitch)
    public boolean paintWords =true ;


    @SettingField(
            descriptions = R.string.direct_translate_speech,
            index = 4,
            title = R.string.direct_translate_speech_titl,
            typeField = TypeField.BooleanSwitch)
    public boolean directTraslateSpeec ;


    @SettingField(
            index = 5,
            title = R.string.color_select_word,
            typeField = TypeField.Color)
    public int colorSelectWords =0xFFFF0000 ;


    public static transient List<WrapperAction>  iActions;

    static {
        iActions=new ArrayList<>();
    }

    public static void Add(WrapperAction wrapperAction){
        if(iActions.size()==0){
            iActions.add(wrapperAction);
        }else{
            WrapperAction d=iActions.get(iActions.size()-1);
            if(d.id!=wrapperAction.id){
                iActions.add(wrapperAction);
            }
        }
    }

    private String path;


    public String getPath() {
        return path;

    }

    public void setPath(String path) {
        this.path = path;
        save();
    }

    public int stateList;

    public int[] stateText=new int[2];
}


