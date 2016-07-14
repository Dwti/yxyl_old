package com.yanxiu.gphone.hd.student.manager;

import android.app.Activity;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.activity.AnswerReportActivity;
import com.yanxiu.gphone.hd.student.activity.LoginActivity;
import com.yanxiu.gphone.hd.student.activity.MainActivity;
import com.yanxiu.gphone.hd.student.activity.MyStageSelectActivity;
import com.yanxiu.gphone.hd.student.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.hd.student.activity.StageSwitchActivity;
import com.yanxiu.gphone.hd.student.activity.UserLocationSelectActivity;
import com.yanxiu.gphone.hd.student.activity.WebViewActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/6/1.
 */
public class ActivityManager {
    private static ArrayList<Activity> list = new ArrayList<Activity>();
    public static void addActicity(Activity activity){
        if(list!=null){
            list.add(0,activity);
        }
    }
    public static boolean hasMainActivity(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                if(activity instanceof MainActivity){
                    return true;
                }
            }
        }
        return false;
    }
    public static void destoryActivity(Activity activity){
        if(list!=null){
            list.remove(activity);
        }
    }

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
                    if(activity instanceof  MainActivity){
                        LogInfo.log("lee","MainActivity destoryAllActivityOFFLogin");
                    }
                    LogInfo.log("lee","destoryAllActivityOFFLogin");
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
    public static void destoryActivityExcStageSwitchActivity(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                if(!(activity instanceof StageSwitchActivity)){
                    activity.finish();
                }
            }
        }
        if(list!=null){
            list.clear();
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
    public static void destoryAllActivityOffLogin(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                if(!(activity instanceof LoginActivity)){
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

    public static void destory(){
        if(list!=null && list.size() > 0){
            for(Activity activity : list){
                activity.finish();
            }
        }
    }
}
