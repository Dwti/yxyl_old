package com.example.settingproblemssystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.settingproblemssystem.bean.SettingProbBean;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/21 17:15.
 * Function :
 */

public class WebViewActivity extends BaseActivity {

    public static void lunch(Activity activity, SettingProbBean bean){
        Intent intent=new Intent(activity,WebViewActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("bean",bean);
        intent.putExtra("bundle",bundle);
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
