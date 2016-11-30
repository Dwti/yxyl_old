package com.yanxiu.gphone.student.utils;

import com.yanxiu.gphone.student.inter.CorpFinishListener;
import com.yanxiu.gphone.student.inter.CorpListener;

import java.util.ArrayList;
import java.util.List;

/**
 * com.yanxiu.gphone.student.utils
 * Created by cangHaiXiao.
 * Time : 2016/11/30 16:53.
 * Function :解答题等上传答案的fragment需要
 */

public class CorpUtils {

    private static CorpUtils utils;
    private static List<CorpListener> list=new ArrayList<CorpListener>();
    private static List<CorpFinishListener> data=new ArrayList<CorpFinishListener>();


    public static CorpUtils getInstence(){
        utils=new CorpUtils();
        return utils;
    }

    public void AddListener(CorpListener listener){
        if (list.size()>0){
            list.clear();
        }
        list.add(listener);
    }


    public void AddFinishListener(CorpFinishListener listener){
        if (data.size()>0){
            data.clear();
        }
        data.add(listener);
    }

    public CorpListener getCorpListener(){
        if (list.size()>0) {
            return list.get(0);
        }else {
            return null;
        }
    }

    public CorpFinishListener getCorpFinishListener(){
        if (data.size()>0) {
            return data.get(0);
        }else {
            return null;
        }
    }

    public void RemoveListener(CorpListener listener){
        if (list.contains(listener)) {
            list.remove(listener);
        }
    }

    public void setClear(){
        list.clear();
    }

    public void RemoveFinishListener(CorpFinishListener listener){
        if (data.contains(listener)) {
            data.remove(listener);
        }
    }

    public void setFinishClear(){
        data.clear();
    }


}
