package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;

import com.yanxiu.gphone.hd.student.fragment.manager.CommonFgManager;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;

/**
 * 带统一处理的FragmentManager的Fgment 所有需要此功能的Fragment继承此类
 * Created by Administrator on 2016/1/22.
 */
public abstract class BaseWithManagerFragment extends BaseFragment{
    protected IFgManager mIFgManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getFragmentManagerFromSubClass()!=null){
            mIFgManager=getFragmentManagerFromSubClass();
        }else{
            mIFgManager=new CommonFgManager();
            mIFgManager.setFragmentManager(getChildFragmentManager());
            mIFgManager.setFgContainerID(getFgContainerIDFromSubClass());
        }
    }

    protected abstract IFgManager getFragmentManagerFromSubClass();

    protected abstract int getFgContainerIDFromSubClass();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIFgManager=null;
    }
}
