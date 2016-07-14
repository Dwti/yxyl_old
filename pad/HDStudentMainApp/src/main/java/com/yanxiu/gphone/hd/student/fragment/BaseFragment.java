package com.yanxiu.gphone.hd.student.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.core.utils.LogInfo;
import com.tendcloud.tenddata.TCAgent;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.inter.ResetInter;
import com.yanxiu.gphone.hd.student.utils.ResetManager;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

/**
 * 所有Fragment的基类
 * Created by Administrator on 2016/1/19.
 */
public abstract class BaseFragment extends Fragment implements ResetInter {
    private final static  String TAG=BaseFragment.class.getSimpleName();
    protected PublicLoadLayout mPublicLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResetManager.getInstance().addObservers(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume () {
        super.onResume();
        TCAgent.onPageStart(getActivity(), this.getClass().getName());
    }

    @Override
    public void onPause () {
        super.onPause();
        TCAgent.onPageEnd(getActivity(), this.getClass().getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ResetManager.getInstance().deleteObservers(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        return false;
    }
    /**
     * 公用的错误加载页面
     * @param type 错误码
     * @param msg 错误描述
     * @param isFristLoad 是否是第一次加载 判断是使用Toast还是背景图
     */
    protected void showError(int type,String msg,boolean isFristLoad){

        switch (type){
            case ErrorCode.NETWORK_NOT_AVAILABLE:
                showNetError(isFristLoad);
                break;
            case ErrorCode.NETWORK_REQUEST_ERROR:

            case ErrorCode.DATA_REQUEST_NULL:

            case ErrorCode.JOSN_PARSER_ERROR:
                showDataError(isFristLoad);
                break;
        }
        LogInfo.log(TAG, msg);
    }


    /**
     * 公用的错误加载页面
     * @param type 错误码
     * @param msg 错误描述
     * @param isFristLoad 是否是第一次加载 判断是使用Toast还是背景图
     */
    protected void showErrorWithFlag(int type,String msg,boolean isFristLoad){
        switch (type){
            case ErrorCode.NETWORK_NOT_AVAILABLE:
                showNetErrorWithFlag(isFristLoad);
                break;
            case ErrorCode.NETWORK_REQUEST_ERROR:

            case ErrorCode.DATA_REQUEST_NULL:

            case ErrorCode.JOSN_PARSER_ERROR:
                showDataError(isFristLoad);
                break;
        }
        LogInfo.log(TAG, msg);
    }




    protected void finishPublicLayout(){
        if(mPublicLayout!=null){
            mPublicLayout.finish();
        }

    }
    protected void showDataError(boolean isFirstLoad){
        if(isFirstLoad){
            if(mPublicLayout!=null){
                mPublicLayout.dataError(getResources().getString(R.string.data_request_fail),false);
            }
        }else{
            Util.showToast(getResources().getString(R.string.data_request_fail));
        }
    }

    protected void showDataErrorWithFlag(boolean isFirstLoad){
        if(isFirstLoad){
            if(mPublicLayout!=null){
                mPublicLayout.dataError(getResources().getString(R.string.data_request_fail),false);
            }
        }else{
            Util.showToast(getResources().getString(R.string.data_request_fail));
        }
    }


    protected void showNetError(boolean isFristLoad){
        if(isFristLoad){
            if(mPublicLayout!=null){
                mPublicLayout.netError(false);
            }
        }else{
            Util.showToast(getResources().getString(R.string.public_loading_net_null_errtxt));
        }
    }


    protected void showNetErrorWithFlag(boolean isShowContent){
        if(isShowContent){
            if(mPublicLayout!=null){
                mPublicLayout.netError(isShowContent);
            }
        }else{
            Util.showToast(getResources().getString(R.string.public_loading_net_null_errtxt));
        }
    }



}
