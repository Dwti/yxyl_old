package com.yanxiu.gphone.studentold.manager;

import android.app.Activity;

import com.common.core.manage.CommonActivityManager;
import com.yanxiu.gphone.studentold.activity.AnswerReportActivity;
import com.yanxiu.gphone.studentold.activity.GroupHwActivity;
import com.yanxiu.gphone.studentold.activity.LoginActivity;
import com.yanxiu.gphone.studentold.activity.LoginChoiceActivity;
import com.yanxiu.gphone.studentold.activity.MainActivity;
import com.yanxiu.gphone.studentold.activity.MyStageSelectActivity;
import com.yanxiu.gphone.studentold.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.studentold.activity.UserLocationSelectActivity;
import com.yanxiu.gphone.studentold.activity.WebViewActivity;

/**
 * Created by Administrator on 2015/6/1.
 */
public class ActivityManager extends CommonActivityManager{
    public static boolean isExistLoginActivity(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                if(activity instanceof LoginActivity){
                    return true;
                }
            }
        }
        return false;
    }
    public static void destoryAllActivityOFFLogin(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                if(!(activity instanceof LoginActivity)){
                    activity.finish();
                }
            }
        }
    }

    public static void destoryAllActivity(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                if(!(activity instanceof MainActivity)){
                    activity.finish();
                }
            }
        }
    }

    public static void destoryAllEntelliTopActivity(){
        if(list!=null && !list.isEmpty()){
            for(Activity activity : list){
                if(activity instanceof AnswerReportActivity){
                    activity.finish();
                }
                if(activity instanceof ResolutionAnswerViewActivity){
                    activity.finish();
                }
            }
        }
    }

    public static void destoryHwActivity(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                if(activity instanceof GroupHwActivity){
                    activity.finish();
                }
            }
        }
    }
    public static void destoryAllActivityExceptStageActivity(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                if(!(activity instanceof MyStageSelectActivity)){
                    activity.finish();
                }
            }
        }
    }
    public static void destoryAllUserLocationSelectActivity(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                if(activity instanceof UserLocationSelectActivity){
                    activity.finish();
                }
            }
        }
    }

    public static void destoryWebviewActivity(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                if(activity instanceof WebViewActivity){
                    activity.finish();
                }
            }
        }
    }
    public static void destoryExcepLoginChoiceActivity(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                if(!(activity instanceof LoginChoiceActivity)){
                    activity.finish();
                }
            }
        }
    }

}
