package com.yanxiu.gphone.hd.student.utils;

import java.util.Observable;

/**
 * 注册所有跟“我的信息”相关的变化通知
 * Created by Administrator on 2016/2/1.
 */
public class HeadInfoObserver  extends Observable{
    private HeadInfoObserver(){
    }
    private static HeadInfoObserver mHeadInfoOb;

    public static  HeadInfoObserver getInstance(){
        if(mHeadInfoOb==null){
            mHeadInfoOb=new HeadInfoObserver();
        }
        return mHeadInfoOb;
    }
    public void resetHeadInfoObserver(){
        mHeadInfoOb=null;
    }

    public void notifyChange(){
            setChanged();
            notifyObservers();
    }
}
