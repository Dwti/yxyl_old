package com.yanxiu.gphone.hd.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yanxiu.gphone.hd.student.activity.base.YanxiuBaseActivity;
import com.yanxiu.gphone.hd.student.bean.ClassInfoBean;


/**
 * Created by Administrator on 2015/7/9.
 */
public class GroupInfoActivity extends YanxiuBaseActivity {

    public static void launchActivity(Activity activity,int type,int classId){
        Intent intent = new Intent(activity,GroupInfoActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("classId",classId);
        if(type == CANCEL_CLASS || type == EXIT_CLASS){
            activity.startActivityForResult(intent,CANCEL_OR_EXIT_CLASSS_FOR_ACTIVITY);
        }else{
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            activity.startActivity(intent);
        }
        if(type == ADD_CLASS){
            activity.finish();
        }
    }

    public static void launchActivity(Activity activity,int type,ClassInfoBean classInfoBean){
        Intent intent = new Intent(activity,GroupInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("classInfoBean",classInfoBean);
        intent.putExtra("data", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        activity.startActivity(intent);
        if(type == ADD_CLASS){
            activity.finish();
        }
    }
    private int type;
    public final static int ADD_CLASS = 1;
    public final static int EXIT_CLASS = 2;
    public final static int CANCEL_CLASS = 3;
    public final static int CANCEL_OR_EXIT_CLASSS_FOR_ACTIVITY = 0x12;


}
