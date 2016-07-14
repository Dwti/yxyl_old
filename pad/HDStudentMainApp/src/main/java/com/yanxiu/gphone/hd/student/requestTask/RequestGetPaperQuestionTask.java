package com.yanxiu.gphone.hd.student.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.parser.SubjectExerisesItemParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestGetPaperQuestionTask extends AbstractAsyncTask<SubjectExercisesItemBean>{
    private AsyncCallBack mAsyncCallBack;
    private String ppid;
    public RequestGetPaperQuestionTask(Context context, String ppid, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack = mAsyncCallBack;
        this.ppid = ppid;
    }

    @Override
    public YanxiuDataHull<SubjectExercisesItemBean> doInBackground() {
        YanxiuDataHull<SubjectExercisesItemBean> dataHull = YanxiuHttpApi.requestGetQuestionList(0, ppid, new SubjectExerisesItemParser());
        return dataHull;
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
