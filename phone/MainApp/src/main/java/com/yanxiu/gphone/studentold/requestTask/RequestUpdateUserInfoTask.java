package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.RequestBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.RequestParser;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestUpdateUserInfoTask extends AbstractAsyncTask<RequestBean>{
    private AsyncCallBack mAsyncCallBack;
    private HashMap<String, String> paramsMap;
    public RequestUpdateUserInfoTask(Context context, HashMap<String, String> paramsMap,
                                     AsyncCallBack mAsyncCallBack) {
        super(context);
        this.paramsMap = paramsMap;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<RequestBean> doInBackground() {
        return YanxiuHttpApi.requestUpdateUserInfo(0, new RequestParser(), paramsMap);
    }

    @Override
    public void onPostExecute(int updateId, RequestBean result) {
        if(result != null){
            if(mAsyncCallBack != null){
                if(result.getStatus().getCode() == 0){
                    mAsyncCallBack.update(result);
                } else {
                    if(mAsyncCallBack != null){
                        mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, result.getStatus().getDesc());
                    }
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
