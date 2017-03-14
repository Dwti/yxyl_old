package com.example.settingproblemssystem;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/10 15:15.
 * Function :
 */

public abstract class BaseActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initView();
        initData();
        initListener();
    }

    protected abstract @LayoutRes int getContentViewId();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initListener();

    @Override
    public void onClick(View v) {

    }
}
