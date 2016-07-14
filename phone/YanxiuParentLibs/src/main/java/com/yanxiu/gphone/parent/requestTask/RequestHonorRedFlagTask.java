package com.yanxiu.gphone.parent.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.bean.ParentRemindBean;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.parser.ParentRemindBeanParser;

/**
 * “荣誉”是否显示红色标记接口
 * Created by Administrator on 2015/6/1.
 */
public class RequestHonorRedFlagTask extends AbstractAsyncTask<ParentRemindBean>{
    private AsyncCallBack mAsyncCallBack;

    public RequestHonorRedFlagTask (Context context, AsyncCallBack
            mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<ParentRemindBean> doInBackground() {
        return YanxiuParentHttpApi.requestHonorRedFlag(new ParentRemindBeanParser());
    }

    @Override
    public void onPostExecute(int updateId, ParentRemindBean result) {
        if(result != null){
            if(result.getProperty() != null ) {
                mAsyncCallBack.update(result);
            } else {
                mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);
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
