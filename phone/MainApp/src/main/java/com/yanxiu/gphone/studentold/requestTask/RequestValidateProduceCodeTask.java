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
public class RequestValidateProduceCodeTask extends AbstractAsyncTask<RequestBean>{
    private AsyncCallBack mAsyncCallBack;
    private String mobile;
    private String code;
    private int type;
    public RequestValidateProduceCodeTask(Context context, String mobile,String code,
            int type, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mobile = mobile;
        this.code = code;
        this.type = type;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<RequestBean> doInBackground() {
        return YanxiuHttpApi.requestValidateProduceCode(0,mobile,code,type,new RequestParser());
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
