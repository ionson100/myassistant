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
    public boolean isSelect;

    @Column("index_surogate")
    public int index;
}
