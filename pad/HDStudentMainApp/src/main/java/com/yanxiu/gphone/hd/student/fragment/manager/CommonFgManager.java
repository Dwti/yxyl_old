package com.yanxiu.gphone.hd.student.fragment.manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.common.core.utils.LogInfo;

/**
 * Created by Administrator on 2016/1/22.
 */
public class CommonFgManager implements  IFgManager {
    private final static String TAG=CommonFgManager.class.getSimpleName();
    private FragmentManager mFgManager;
    private int mFgContainerId=-1;
    private FragmentTransaction mFt;
    @Override
    public void setFragmentManager(FragmentManager fgManager) {
        this.mFgManager=fgManager;
    }


    @Override
    public Fragment findFragmentById(int layoutId) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }
        return mFgManager.findFragmentById(layoutId);
    }

    @Override
    public Fragment findFragmentByTag(String tag) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }
        return mFgManager.findFragmentByTag(tag);
    }

    @Override
    public void setFgContainerID(int fgContainerId) {
        this.mFgContainerId=fgContainerId;
    }

    @Override
    public FragmentTransaction beginTransaction() {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }
        return mFgManager.beginTransaction();
    }

    @Override
    public void addFragment(Fragment fg, FragmentTransaction ft, boolean isAllowingStateLoss) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }

        if(ft==null){
            addFragment(fg, isAllowingStateLoss);
        }else{
            if(fg.isAdded()){
                LogInfo.log(TAG,"fg.isAdded()");
                return;
            }
            this.mFt=ft;
            this.mFt.add(mFgContainerId,fg);
            commit(mFt,isAllowingStateLoss);
        }
    }

    @Override
    public void addFragment(Fragment fg, FragmentTransaction ft, boolean isAllowingStateLoss, String name) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }

        if(ft==null){
            addFragment(fg, isAllowingStateLoss,name);
        }else{
            if(fg.isAdded()){
                LogInfo.log(TAG,"fg.isAdded()");
                return;
            }
            this.mFt=ft;
            this.mFt.addToBackStack(name);
            this.mFt.add(mFgContainerId,fg);
            commit(mFt,isAllowingStateLoss);
        }
    }


    @Override
    public void addFragment(Fragment fg, boolean isAllowingStateLoss) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }
        addFragment(fg,isAllowingStateLoss,null);
    }

    @Override
    public void addFragment(Fragment fg, boolean isAllowingStateLoss, String name) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }
        if(fg.isAdded()){
            return;
        }
        this.mFt=mFgManager.beginTransaction();
        this.mFt.setCustomAnimations(com.common.core.R.anim.slide_in_right,  com.common.core.R.anim.slide_out_right, com.common.core.R.anim.slide_in_right, com.common.core.R.anim.slide_out_right);
        this.mFt.addToBackStack(name);
        this.mFt.add(mFgContainerId,fg);
        commit(mFt,isAllowingStateLoss);
    }

    @Override
    public void showFragment(Fragment fg, FragmentTransaction ft, boolean isAllowingStateLoss) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }
        if(ft==null){
            showFragment(fg,isAllowingStateLoss);
        }else{
            this.mFt=ft;
            this.mFt.show(fg);
            commit(mFt,isAllowingStateLoss);
        }
    }

    @Override
    public void showFragment(Fragment fg,boolean isAllowingStateLoss) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }
        this.mFt=mFgManager.beginTransaction();
        this.mFt.addToBackStack(null);
        this.mFt.show(fg);
        commit(mFt,isAllowingStateLoss);
    }

    @Override
    public void hideFragment(Fragment fg, FragmentTransaction ft, boolean isAllowingStateLoss) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }

        if(ft==null){
            hideFragment(fg,isAllowingStateLoss);
        }else{

            this.mFt=ft;
            this.mFt.hide(fg);
            commit(mFt, isAllowingStateLoss);
        }
    }

    @Override
    public void hideFragment(Fragment fg,boolean isAllowingStateLoss) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }

        this.mFt=mFgManager.beginTransaction();
        this.mFt.addToBackStack(null);
        this.mFt.hide(fg);
        commit(mFt, false);

    }

    @Override
    public void replaceFragment(Fragment fg, FragmentTransaction ft, boolean isAllowingStateLoss) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }

        if(mFgContainerId==-1){
            throw  new IllegalArgumentException("mFgContainerId Parameter is not set,the default value is -1 ");
        }
        if(ft==null){
            replaceFragment(fg, isAllowingStateLoss);
        }else{

            this.mFt=ft;
            mFt.replace(mFgContainerId, fg);
            commit(mFt, isAllowingStateLoss);
        }

    }

    @Override
    public void replaceFragment(Fragment fg,boolean isAllowingStateLoss) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }

        if(mFgContainerId==-1){
            throw  new IllegalArgumentException("mFgContainerId Parameter is not set,the default value is -1 ");
        }

        this.mFt=mFgManager.beginTransaction();
        this.mFt.addToBackStack(null);
        this.mFt.replace(mFgContainerId, fg);
        commit(mFt, false);
    }

    @Override
    public void removeFragment(Fragment fg, FragmentTransaction ft, boolean isAllowingStateLoss) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }
        if(ft==null){
            removeFragment(fg, isAllowingStateLoss);
        }else{
            this.mFt=ft;
            this.mFt.remove(fg);
            commit(mFt, isAllowingStateLoss);
        }
    }

    @Override
    public void removeFragment(Fragment fg,boolean isAllowingStateLoss) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }
        this.mFt=mFgManager.beginTransaction();
        this.mFt.addToBackStack(null);
        this.mFt.remove(fg);
        commit(mFt, false);
    }



    @Override
    public void popStack() {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }
        mFgManager.popBackStack();
    }

    @Override
    public void popBackStackImmediate(String name, int flag) {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }
        mFgManager.popBackStack(name,flag);
    }

    @Override
    public int getBackStackCount() {
        if(mFgManager==null){
            throw  new NullPointerException("FragmentManager is the null object,please init it ");
        }
        return mFgManager.getBackStackEntryCount();
    }

    private void commit(FragmentTransaction ft,boolean isAllowingStateLoss){
        if(isAllowingStateLoss){
            ft.commitAllowingStateLoss();
        }else{
            ft.commit();
        }
    }

}
