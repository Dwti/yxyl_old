package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.view.KeyEvent;

import com.yanxiu.gphone.hd.student.R;

/**
 * Created by Administrator on 2016/2/16.
 */
public class MyErrorRecordContainerFragment extends BaseFgContainerFragment{

    private BaseFragment mBaseFg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseFg= (BaseFragment) MyErrorRecordFragment.newInstance();
        mIFgManager.addFragment(mBaseFg,ft,true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return mIFgManager.getBackStackCount() >= 1 && mBaseFg.onKeyDown(keyCode, event);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mBaseFg.onHiddenChanged(hidden);
    }

    @Override
    protected int setFgContainerBg() {
        return R.drawable.blue_bg;
    }

    @Override
    public void onReset() {

    }
}
