package com.yanxiu.gphone.student.HtmlParser.Utils;

import android.graphics.drawable.Drawable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cangHaiXiao.
 * Time : 2016/12/22 16:48.
 * Function :
 */

public class CatchUtils {

    private static CatchUtils utils;
    private Map<String,WeakReference<Drawable>> map=new HashMap<String,WeakReference<Drawable>>();


    public static CatchUtils getInstence(){
        if (utils==null){
            utils=new CatchUtils();
        }
        return utils;
    }

    public void put(String key,Drawable drawable){
        if (map==null){
            map=new HashMap<String,WeakReference<Drawable>>();
        }
        map.put(key,new WeakReference<Drawable>(drawable));
    }

    public Drawable get(String key){
        if (map!=null){
            WeakReference<Drawable> weak=map.get(key);
            if (weak!=null){
                Drawable drawable=weak.get();
                return drawable;
            }
        }
        return null;
    }

}
