package com.yanxiu.gphone.hd.student.utils;

import com.yanxiu.gphone.hd.student.inter.ResetInter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/2.
 */
public class ResetManager {
    private static  ResetManager mResetManager;
    public static  ResetManager getInstance(){
        if(mResetManager==null){
            mResetManager=new ResetManager();
        }
        return mResetManager;
    }
    private ResetManager(){

    }
    private List<ResetInter>  resetInters=new ArrayList<>();

    public void addObservers(ResetInter mResettInter){
        resetInters.add(mResettInter);
    }

    public void deleteObservers(ResetInter mResettInter){
     resetInters.remove(mResettInter);
    }

    public void clearInstance(){
        if(resetInters!=null){
            resetInters.clear();
        }
        resetInters=null;
        mResetManager=null;
    }

    public void notifyAllObservers(){
        int size=resetInters.size();
        for(int i=0;i<size;i++){
            resetInters.get(i).onReset();
        }
    }



}
