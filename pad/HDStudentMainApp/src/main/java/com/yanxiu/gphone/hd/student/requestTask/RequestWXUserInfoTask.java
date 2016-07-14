package com.yanxiu.gphone.hd.student.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.bean.WXUserInfoBean;
import com.yanxiu.gphone.hd.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.parser.WXUserBeanParser;

/**
 * Created by Administrator on 2015/6/1.
 */
    public class RequestWXUserInfoTask extends AbstractAsyncTask<WXUserInfoBean>{
    private AsyncCallBack mAsyncCallBack;
    private String access_token;
    private String openid;
    public RequestWXUserInfoTask(Context context, String access_token, String openid,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.access_token = access_token;
        this.openid = openid;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<WXUserInfoBean> doInBackground() {
        return YanxiuHttpApi.requestWXUserInfo(access_token, openid,new WXUserBeanParser());
    }

    @Override
    public void onPostExecute(int updateId, WXUserInfoBean result) {
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
}
