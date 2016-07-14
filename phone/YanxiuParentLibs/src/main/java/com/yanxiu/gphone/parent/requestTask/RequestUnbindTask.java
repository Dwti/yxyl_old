package com.yanxiu.gphone.parent.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.bean.ParentRequestBean;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.parser.ParentRequestParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestUnbindTask extends AbstractAsyncTask<ParentRequestBean>{

    private AsyncCallBack mAsyncCallBack;
    private String content;

    public RequestUnbindTask(Context context, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<ParentRequestBean> doInBackground() {
        return YanxiuParentHttpApi.requestUnBindChildAccount(new ParentRequestParser());
    }

    @Override
    public void onPostExecute(int updateId, ParentRequestBean result) {
        if(result != null){
            if(result.getStatus().getCode() == 0){
                if(mAsyncCallBack != null){
                    mAsyncCallBack.update(result);
                }
            } else {
                if(mAsyncCallBack != null){
                    mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, result.getStatus().getDesc());
                }
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
