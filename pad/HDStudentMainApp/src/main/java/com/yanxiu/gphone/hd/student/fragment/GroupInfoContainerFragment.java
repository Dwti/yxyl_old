package com.yanxiu.gphone.hd.student.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;

/**
 * Created by Administrator on 2016/1/21.
 */
public class GroupInfoContainerFragment extends BaseFgContainerFragment {
    private BaseFragment groupFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupFragment = (BaseFragment)GroupFragment.newInstance();
        mIFgManager.addFragment(groupFragment,ft,true);
    }
    @Override
    protected int setFgContainerBg() {
        return R.drawable.yanxiu_group_bg;
    }
    public void requestGroupData (final boolean isRefresh, final boolean showLoading){
        ((GroupFragment)groupFragment).requestGroupData(isRefresh, showLoading);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //groupFragment.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        groupFragment.onHiddenChanged(hidden);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogInfo.log("haitian", "HomePageFragment.getBackStackCount()="+mIFgManager.getBackStackCount());
        if(mIFgManager.getBackStackCount() >= 1){
            return groupFragment.onKeyDown(keyCode,event);
        }else{
            return false;
        }
    }

    @Override
    public void onReset() {

    }
}
