package com.yanxiu.gphone.hd.student.fragment.manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Administrator on 2016/1/22.
 */
public interface IFgManager {

    void setFragmentManager(FragmentManager fgManager);

    Fragment findFragmentById(int layoutId);

    Fragment findFragmentByTag(String tag);

    void setFgContainerID(int fgContainerId );

    FragmentTransaction beginTransaction();

    void addFragment(Fragment fg,FragmentTransaction ft,boolean isAllowingStateLoss);

    void addFragment(Fragment fg,FragmentTransaction ft,boolean isAllowingStateLoss,String name);

    void addFragment(Fragment fg,boolean isAllowingStateLoss);

    void addFragment(Fragment fg,boolean isAllowingStateLoss,String name);

    void showFragment(Fragment fg,FragmentTransaction ft,boolean isAllowingStateLoss);

    void showFragment(Fragment fg,boolean isAllowingStateLoss);

    void hideFragment(Fragment fg,FragmentTransaction ft,boolean isAllowingStateLoss);

    void hideFragment(Fragment fg,boolean isAllowingStateLoss);

    void replaceFragment(Fragment fg,FragmentTransaction ft,boolean isAllowingStateLoss);

    void replaceFragment(Fragment fg,boolean isAllowingStateLoss);

    void removeFragment(Fragment fg,FragmentTransaction ft,boolean isAllowingStateLoss);

    void removeFragment(Fragment fg,boolean isAllowingStateLoss);

    void popStack();

    void popBackStackImmediate(String name,int flag);

    int getBackStackCount();

}
