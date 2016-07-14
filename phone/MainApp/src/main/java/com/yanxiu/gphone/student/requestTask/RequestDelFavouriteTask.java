package com.yanxiu.gphone.student.requestTask;

import android.content.Context;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.RequestBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.parser.RequestParser;


/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestDelFavouriteTask extends AbstractAsyncTask<RequestBean>{
    private AsyncCallBack mAsyncCallBack;
    private String questionId;

    public static void requestTask(Context mContext, String questionId,
                                   AsyncCallBack mAsyncCallBack){
        RequestDelFavouriteTask mRequestDelMistakeTask = new RequestDelFavouriteTask(mContext, questionId, mAsyncCallBack);
        mRequestDelMistakeTask.start();
    }
    public RequestDelFavouriteTask(Context context, String questionId,
                                   AsyncCallBack mAsyncCallBack) {
        super(context);
        this.questionId = questionId;
        this.mAsyncCallBack = mAsyncCallBack;
        LogInfo.log("haitian", "RequestDelMistakeTask ");
    }

    @Override
    public YanxiuDataHull<RequestBean> doInBackground() {
        LogInfo.log("haitian", "RequestDelMistakeTask  doInBackground");
        return YanxiuHttpApi.requestDelFavourite(0, questionId, new RequestParser());
    }
    @Override
    public void onPostExecute(int updateId, RequestBean result) {
        if(result != null){
            LogInfo.log("haitian", "RequestDelMistakeTask  onPostExecute ="+result.getStatus().toString());
            if(result.getStatus() != null && result.getStatus().getCode() == 0){
                if(mAsyncCallBack != null){
                    mAsyncCallBack.update(result);
                }
            } else {
                if(mAsyncCallBack != null){
                    mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);
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
