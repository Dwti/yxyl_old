package com.yanxiu.gphone.student.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.ExamListInfo;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.parser.RequestExamInfoParser;

/**
 * Created by Administrator on 2015/11/17.
 */
public class RequestExamInfoTask extends  AbstractAsyncTask<ExamListInfo> {

    private AsyncCallBack mAsyncCallBack;
    private String subjectId;
    public RequestExamInfoTask(Context context,String subjectId,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack=mAsyncCallBack;
        this.subjectId=subjectId;
    }

    @Override
    public YanxiuDataHull<ExamListInfo> doInBackground() {
        return YanxiuHttpApi.requestExamInfo(subjectId,new RequestExamInfoParser());
    }

    @Override
    public void onPostExecute(int updateId, ExamListInfo result) {
        if(result != null){
            if(result.getStatus().getCode()==0){
                if(mAsyncCallBack != null){
                    mAsyncCallBack.update(result);
                }
            }else{
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
