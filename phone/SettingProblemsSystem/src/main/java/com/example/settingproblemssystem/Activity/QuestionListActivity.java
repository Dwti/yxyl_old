package com.example.settingproblemssystem.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

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
        return 0;
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
