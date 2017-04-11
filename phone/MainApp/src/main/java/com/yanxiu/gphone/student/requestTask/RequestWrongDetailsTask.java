package com.yanxiu.gphone.student.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.parser.SubjectExerisesItemParser;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/4/10 17:24.
 * Function :
 */

public class RequestWrongDetailsTask extends AbstractAsyncTask<SubjectExercisesItemBean> {
    private AsyncCallBack mAsyncCallBack;
    private String subjectId;
    private int currentPage = 1;
    private ArrayList<Integer> qids;
    private final int pageSize = YanXiuConstant.YX_PAGESIZE_CONSTANT;

    public RequestWrongDetailsTask(Context context, String subjectId , ArrayList<Integer> qids, int currentPage , AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack = mAsyncCallBack;
        this.subjectId = subjectId;
        this.qids=qids;
        this.currentPage = currentPage;
    }

    @Override
    public YanxiuDataHull<SubjectExercisesItemBean> doInBackground() {
        return YanxiuHttpApi.requestWrongDetailsQuestion(0,qids,subjectId, pageSize, currentPage ,new SubjectExerisesItemParser());
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
