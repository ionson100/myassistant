package com.example.user.ling;

import com.example.user.ling.orm2.Column;
import com.example.user.ling.orm2.PrimaryKey;
import com.example.user.ling.orm2.Table;

import java.util.Date;


@Table("dictionary")
public class MDictionary {

    @PrimaryKey("id")
    public int id;

    @Column("keyWord")
    public String keyWord;



    @Column("valueWord")
    public String valueWord;

    @Column("is_select")
    private boolean isSelect;

    @Column("index_surogate")
    public int index;

    @Column("index_story")
    public long indexStory;


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
        if(select){
            index=++Utils.sIndexSurogat;
            indexStory=new Date().getTime();
        }else{
            index=0;
        }
        Utils.zerabledSelect();

    }
}
