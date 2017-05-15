package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.MistakeRedoNumberBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.MisRedoNumParser;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/13 12:28.
 * Function :
 */

public class MisRedoNumQuestionTask extends AbstractAsyncTask<MistakeRedoNumberBean> {

    private String stageId;
    private String subjectId;
    private AsyncCallBack mAsyncCallBack;

    public MisRedoNumQuestionTask(Context context, String stageId, String subjectId,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.stageId=stageId;
        this.subjectId=subjectId;
        this.mAsyncCallBack=mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<MistakeRedoNumberBean> doInBackground() {
        return YanxiuHttpApi.requestMisRedoNum(0,stageId,subjectId,new MisRedoNumParser());
    }

    @Override
    public void onPostExecute(int updateId, MistakeRedoNumberBean result) {
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
