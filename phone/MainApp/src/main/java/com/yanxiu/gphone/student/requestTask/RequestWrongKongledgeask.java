package com.yanxiu.gphone.student.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.parser.MistakeFragmentBeanParser;

/**
 * Created by Canghaixiao.
 * Time : 2017/4/10 11:50.
 * Function :
 */

public class RequestWrongKongledgeask extends AbstractAsyncTask<YanxiuBaseBean>  {
    private Context context;
    private String stageId;
    private String subjectId;
    private AsyncCallBack mAsyncCallBack;

    public RequestWrongKongledgeask(Context context ,String stageId, String subjectId ,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.context=context;
        this.stageId=stageId;
        this.subjectId=subjectId;
        this.mAsyncCallBack=mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<YanxiuBaseBean> doInBackground() {
        return YanxiuHttpApi.requestMistakeKongledge(0,stageId,subjectId,new MistakeFragmentBeanParser());
    }

    @Override
    public void onPostExecute(int updateId, YanxiuBaseBean result) {
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
