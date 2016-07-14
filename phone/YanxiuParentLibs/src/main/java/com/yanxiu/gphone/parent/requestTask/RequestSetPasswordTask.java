package com.yanxiu.gphone.parent.requestTask;

import android.content.Context;

import com.common.core.utils.StrMD5;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.bean.ParentSetPasswordBean;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.parser.ParentSetPasswordParser;

/**
 * Created by lee on 16-3-30.
 */
public class RequestSetPasswordTask extends AbstractAsyncTask<ParentSetPasswordBean> {
    private AsyncCallBack mAsyncCallBack;
    private String mobile,password;
    public RequestSetPasswordTask(Context context,String mobile,String password,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack=mAsyncCallBack;
        this.mobile=mobile;
        this.password=password;
    }

    @Override
    public YanxiuDataHull<ParentSetPasswordBean> doInBackground() {
        return YanxiuParentHttpApi.requestSetPassword(mobile, StrMD5.getStringMD5(password),new ParentSetPasswordParser());
    }

    @Override
    public void onPostExecute(int updateId, ParentSetPasswordBean result) {
        if(result != null){
            if(result != null) {
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
