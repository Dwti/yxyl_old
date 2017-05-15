package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.RequestBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.RequestParser;

/**
 * Created by Administrator on 2015/11/19.
 */
public class RequestErrorFeedBackTask extends  AbstractAsyncTask<RequestBean> {
    private AsyncCallBack mAsyncCallBack;
    private String questionId;
    private String content;
    public RequestErrorFeedBackTask(Context context,String questionId,String content,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.questionId=questionId;
        this.content=content;
        this.mAsyncCallBack=mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<RequestBean> doInBackground() {
        return YanxiuHttpApi.requestErrorFeedBack(questionId, content, new RequestParser());
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
