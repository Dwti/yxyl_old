package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.RequestBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.RequestParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestCancelClassTask extends AbstractAsyncTask<RequestBean>{
    private AsyncCallBack mAsyncCallBack;
    private int classId;
    public RequestCancelClassTask(Context context, int classId,
            AsyncCallBack mAsyncCallBack) {
        super(context);
        this.classId = classId;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<RequestBean> doInBackground() {
        return YanxiuHttpApi.requestCancelClass(0, classId, new RequestParser());
    }

    @Override
    public void onPostExecute(int updateId, RequestBean result) {
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
