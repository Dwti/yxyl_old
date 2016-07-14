package com.yanxiu.gphone.upgrade.request;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.basecore.impl.YanxiuHttpAsyncTask;
import com.yanxiu.gphone.upgrade.bean.InitializeBean;
import com.yanxiu.gphone.upgrade.parser.InitializeParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class InitializeTask extends YanxiuHttpAsyncTask<InitializeBean> {
    private AsyncCallBack mAsyncCallBack;
    private String token;
    private String mobile;
    private String content;
    public InitializeTask (Context context, String content, String token, String mobile,
                           AsyncCallBack mAsyncCallBack) {
        super(context);
        this.content = content;
        this.token = token;
        this.mobile = mobile;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<InitializeBean> doInBackground() {
        return YanxiuUpgradeHttpApi.requestInitialize(0, content, token, mobile, new
                InitializeParser());
    }

    @Override
    public void onPostExecute(int updateId, InitializeBean result) {
        if(result != null){
            if(mAsyncCallBack != null){
                mAsyncCallBack.update(result);
            }
        } else {
            if(mAsyncCallBack != null){
                mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);
            }
        }
    }
    @Override
    public void netNull() {
        if(mAsyncCallBack != null){
            mAsyncCallBack.dataError(ErrorCode.NETWORK_NOT_AVAILABLE, null);
        }
    }

    @Override
    public void netErr(int updateId, String errMsg) {
        if(mAsyncCallBack != null){
            mAsyncCallBack.dataError(ErrorCode.NETWORK_REQUEST_ERROR, errMsg);
        }
    }

    @Override
    public void dataNull(int updateId, String errMsg) {
        if(mAsyncCallBack != null){
            mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, errMsg);
        }
    }

    @Override
    public void tokenInvalidate (String msg) {

    }
}
