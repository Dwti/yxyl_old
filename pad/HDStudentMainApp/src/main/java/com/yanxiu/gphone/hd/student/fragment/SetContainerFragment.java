package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.view.KeyEvent;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;

/**设置
 * Created by Administrator on 2016/1/21.
 */
public class SetContainerFragment extends BaseFgContainerFragment {
    private static final String TAG=SetContainerFragment.class.getSimpleName();
    private BaseFragment baseFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogInfo.log(TAG, "onCreate");
        baseFragment= (BaseFragment) SetFragment.newInstance();
        mIFgManager.addFragment(baseFragment,ft,true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogInfo.log(TAG, "backstackcount: " + mIFgManager.getBackStackCount());
        return mIFgManager.getBackStackCount() >= 1 && baseFragment.onKeyDown(keyCode, event);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(baseFragment!=null){
            baseFragment.onHiddenChanged(hidden);
        }
    }

    @Override
    protected int setFgContainerBg() {
        return R.drawable.wood_bg;
    }

    @Override
    public void onReset() {

    }
}
