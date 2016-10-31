package com.example.user.ling;

/**
 * Created by USER on 28.10.2016.
 */
public class WrapperAction {
    public  int id;
    public IAction iAction;
    public WrapperAction(int id,IAction iAction){

        this.id = id;
        this.iAction = iAction;
    }

}
