package com.yanxiu.gphone.student.utils;

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

    public CorpListener getCorpListener(){
        if (list.size()>0) {
            return list.get(0);
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
}
