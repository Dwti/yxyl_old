package com.yanxiu.gphone.student.manager;

import android.app.Activity;

import com.common.core.manage.CommonActivityManager;
import com.yanxiu.gphone.student.activity.AnswerReportActivity;
import com.yanxiu.gphone.student.activity.GroupHwActivity;
import com.yanxiu.gphone.student.activity.LoginActivity;
import com.yanxiu.gphone.student.activity.LoginChoiceActivity;
import com.yanxiu.gphone.student.activity.MainActivity;
import com.yanxiu.gphone.student.activity.MyStageSelectActivity;
import com.yanxiu.gphone.student.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.student.activity.UserLocationSelectActivity;
import com.yanxiu.gphone.student.activity.WebViewActivity;

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
