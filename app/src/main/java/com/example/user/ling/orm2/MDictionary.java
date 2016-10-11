package com.example.user.ling.orm2;

/**
 * Created by USER on 10.10.2016.
 */
@Table("dictionary")
public class MDictionary {

    @PrimaryKey("id")
    public int id;

    @Column("keyWord")
    public String keyWord;



    @Column("valueWord")
    public String valueWord;
}
