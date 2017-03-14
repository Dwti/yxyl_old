package com.yanxiu.gphone.student.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.MistakeRedoCardBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.parser.MistakeRedoCardParse;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/13 18:50.
 * Function :
 */

public class RequestMisRedoCardClassTask extends AbstractAsyncTask<MistakeRedoCardBean> {

    private String stageId;
    private String subjectId;
    private AsyncCallBack mAsyncCallBack;

    public RequestMisRedoCardClassTask(Context context,String stageId,String subjectId,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.stageId=stageId;
        this.subjectId=subjectId;
        this.mAsyncCallBack=mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<MistakeRedoCardBean> doInBackground() {
        return YanxiuHttpApi.requestMisRedoCard(0,stageId,subjectId,new MistakeRedoCardParse());
    }

    @Override
    public void onPostExecute(int updateId, MistakeRedoCardBean result) {
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
