package com.yanxiu.gphone.hd.student.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;


public class HomePageFragment extends BaseFgContainerFragment{
    private final BaseFragment baseFragment = new ExerciseHomeworkFragment();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIFgManager.addFragment(baseFragment, ft, true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogInfo.log("geny", "HomePageFragment-----onKeyDown");
        LogInfo.log("haitian", "HomePageFragment.getBackStackCount()=" + mIFgManager.getBackStackCount());
        return mIFgManager.getBackStackCount() >= 1 && baseFragment.onKeyDown(keyCode, event);
    }
    @Override
    protected int setFgContainerBg() {
        return R.drawable.blue_bg;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onReset() {

    }
}
