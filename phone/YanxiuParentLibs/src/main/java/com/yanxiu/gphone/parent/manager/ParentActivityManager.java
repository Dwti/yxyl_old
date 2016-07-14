package com.yanxiu.gphone.parent.manager;

import android.app.Activity;

import com.common.core.manage.CommonActivityManager;
import com.yanxiu.gphone.parent.activity.MainForParentActivity;
import com.yanxiu.gphone.parent.activity.ParentLocationSelectActivity;

/**
 * Created by Administrator on 2015/6/1.
 */
public class ParentActivityManager extends CommonActivityManager{


    public static void destoryAllActivity(){

        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                if(!(activity instanceof MainForParentActivity)){
                    activity.finish();
                }
            }
        }

    }

    public static void destoryAllUserLocationSelectActivity(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                if(activity instanceof ParentLocationSelectActivity){
                    activity.finish();
                }
            }
        }
    }

}
