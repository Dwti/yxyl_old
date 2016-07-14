package com.yanxiu.gphone.hd.student.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.yanxiu.gphone.hd.student.R;

/**个人资料
 * Created by Administrator on 2016/1/21.
 */
public class MyUserInfoContainerFragment extends BaseFgContainerFragment {
    private BaseFragment myUserInfoFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myUserInfoFragment= (BaseFragment) MyUserInfoFragment.newInstance();
        mIFgManager.addFragment(myUserInfoFragment,ft,true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mIFgManager.getBackStackCount() >= 1 && myUserInfoFragment.onKeyDown(keyCode, event);
    }

    @Override
    protected int setFgContainerBg() {
        return R.drawable.blue_bg;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myUserInfoFragment.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(myUserInfoFragment!=null){
            myUserInfoFragment.onHiddenChanged(hidden);
        }

    }

    @Override
    public void onReset() {

    }
}
