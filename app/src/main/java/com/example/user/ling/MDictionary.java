package com.example.user.ling;

import com.example.user.ling.orm2.Column;
import com.example.user.ling.orm2.PrimaryKey;
import com.example.user.ling.orm2.Table;


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


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
        if(select){
            index=++Utils.sIndexSurogat;
        }else{
            index=0;
        }
        Utils.zerabledSelect();
    }
}
