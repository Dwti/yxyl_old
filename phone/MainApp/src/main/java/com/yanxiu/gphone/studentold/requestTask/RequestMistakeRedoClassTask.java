package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.SubjectExerisesItemParser;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/13 15:51.
 * Function :
 */

public class RequestMistakeRedoClassTask extends AbstractAsyncTask<SubjectExercisesItemBean> {

    private String stageId;
    private String subjectId;
    private AsyncCallBack mAsyncCallBack;
    private String pageSize="10";

    public RequestMistakeRedoClassTask(Context context, String stageId, String subjectId,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack=mAsyncCallBack;
        this.stageId=stageId;
        this.subjectId=subjectId;
    }

    @Override
    public YanxiuDataHull<SubjectExercisesItemBean> doInBackground() {
        return YanxiuHttpApi.requestMistakeRedoFrist(0,stageId,subjectId,pageSize,new SubjectExerisesItemParser());
    }

    @Override
    public void onPostExecute(int updateId, SubjectExercisesItemBean result) {
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
