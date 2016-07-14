package com.yanxiu.gphone.student.requestTask;

import android.content.Context;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.RequestBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.parser.RequestParser;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestDelFavouriteListTask extends AbstractAsyncTask<RequestBean>{
    private AsyncCallBack mAsyncCallBack;
    private ArrayList<String> questionIdList;

    public static void requestTask(Context mContext, ArrayList<String> questionIdList,
                                   AsyncCallBack mAsyncCallBack){
        if(questionIdList == null || questionIdList.size() <= 0){
            return;
        }
        RequestDelFavouriteListTask mRequestDelMistakeTask = new RequestDelFavouriteListTask(mContext, questionIdList, mAsyncCallBack);
        mRequestDelMistakeTask.start();
    }
    private RequestDelFavouriteListTask(Context context, ArrayList<String> questionIdList,
                                       AsyncCallBack mAsyncCallBack) {
        super(context);
        this.questionIdList = questionIdList;
        this.mAsyncCallBack = mAsyncCallBack;
        LogInfo.log("haitian", "RequestDelFavouriteListTask ");
    }

    @Override
    public YanxiuDataHull<RequestBean> doInBackground() {
        StringBuilder questionIds = new StringBuilder();
        questionIds.append("[");
        int count = questionIdList.size();
        for (int i =0; i< count; i++){
            if(i == (count-1)){
                questionIds.append(questionIdList.get(i)+"]");
            } else {
                questionIds.append(questionIdList.get(i)+",");
            }
        }
        LogInfo.log("haitian", "questionIds = "+questionIds.toString());
        return YanxiuHttpApi.requestDelFavouriteList(0, questionIds.toString(), new RequestParser());
    }
    @Override
    public void onPostExecute(int updateId, RequestBean result) {
        if(result != null){
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
