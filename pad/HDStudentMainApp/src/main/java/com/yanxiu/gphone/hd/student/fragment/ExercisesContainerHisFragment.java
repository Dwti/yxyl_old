package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.view.KeyEvent;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;

/**
 * Created by Administrator on 2016/2/15.
 */
public class ExercisesContainerHisFragment extends BaseFgContainerFragment {

    private BaseFragment mBaseFg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseFg= (BaseFragment) ExercisesHisFragment.newInstance();
        mIFgManager.addFragment(mBaseFg,ft,true);
    }

    @Override
    protected int setFgContainerBg() {
        return R.drawable.blue_bg;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogInfo.log("haitian", "mIFgManager.getBackStackCount()=" + mIFgManager.getBackStackCount());
        return mIFgManager.getBackStackCount() >= 1 && mBaseFg.onKeyDown(keyCode, event);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mBaseFg.onHiddenChanged(hidden);
    }

    @Override
    public void onReset() {

    }
}
