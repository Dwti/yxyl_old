package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;

/**
 * 所有主界面一级Fragment继承BaseFgContainerFragment
 * Created by Administrator on 2016/1/28.
 */
public abstract class BaseFgContainerFragment extends BaseWithManagerFragment {
    private View view;
    protected FragmentTransaction ft;
    private FrameLayout rootContainer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        ft =mIFgManager.beginTransaction();
//      标准动画
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        view = inflater.inflate(R.layout.fragment_home_page_layout,null);
        rootContainer=(FrameLayout)view.findViewById(R.id.fgContainer);
        rootContainer.setBackgroundResource(setFgContainerBg());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }


    @Override
    protected IFgManager getFragmentManagerFromSubClass() {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass() {
        return R.id.fgContainer;
    }

    protected abstract int setFgContainerBg();

    @Override
    public void onDestroy() {
        super.onDestroy();
        view=null;
        ft=null;
        rootContainer=null;
    }
}
