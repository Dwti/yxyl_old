package com.example.settingproblemssystem.activity;

import android.app.Activity;
import android.content.Intent;

import com.example.settingproblemssystem.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/21 17:27.
 * Function :
 */

public class CompQuestionActivity extends BaseActivity {

    public static void lunch(Activity activity){
        Intent intent=new Intent(activity,CompQuestionActivity.class);
        activity.startActivityForResult(intent,RESULT_OK);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_compquestion;
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
