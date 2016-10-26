package com.example.user.ling;

import com.settings.ion.mylibrary.Reanimator;
import com.settings.ion.mylibrary.SettingField;
import com.settings.ion.mylibrary.TypeField;

import java.io.Serializable;



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
            index = 4,
            title = R.string.color_select_word,
            typeField = TypeField.Color)
    public int colorSelectWords =0xFFFF0000 ;

}
