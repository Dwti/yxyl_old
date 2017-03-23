package com.example.settingproblemssystem.activity;

import android.app.Activity;
import android.content.Intent;

import com.example.settingproblemssystem.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/21 15:56.
 * Function :
 */

public class QuestionListActivity extends BaseActivity {

    public static void lunch(Activity activity){
        Intent intent=new Intent(activity,QuestionListActivity.class);
        activity.startActivityForResult(intent,RESULT_OK);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_questionlist;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
