package com.common.core.manage;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/6/1.
 */
public class CommonActivityManager {
    protected static ArrayList<Activity> list = new ArrayList<Activity>();

    public static void addActicity(Activity activity){
        if(list!=null){
            list.add(0,activity);
        }
    }
    public static void destoryActivity(Activity activity){
        if(list!=null){
            list.remove(activity);
        }
    }
    public static void destory(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                activity.finish();
            }
        }
    }
}
