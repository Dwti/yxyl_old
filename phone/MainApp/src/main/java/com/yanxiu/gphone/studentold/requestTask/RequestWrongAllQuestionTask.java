package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.SubjectExerisesItemParser;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestWrongAllQuestionTask extends AbstractAsyncTask<SubjectExercisesItemBean>{
    private AsyncCallBack mAsyncCallBack;
    private String stageId;
    private String subjectId;
    private int ptype;
    private int currentPage = 1;
    private String currentId = "0";
    private final int pageSize = YanXiuConstant.YX_PAGESIZE_CONSTANT;
    public RequestWrongAllQuestionTask(Context context, String stageId, String subjectId, int currentPage, String currentId, int ptype,
                                       AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack = mAsyncCallBack;
        this.stageId = stageId;
        this.subjectId = subjectId;
        //this.ptype = (ptype == 1 ? 2 : ptype);
        this.ptype = 2;
        this.currentPage = currentPage;
        this.currentId = currentId;
    }

    @Override
    public YanxiuDataHull<SubjectExercisesItemBean> doInBackground() {
        return YanxiuHttpApi.requestWrongAllQuestion(0, stageId, subjectId, pageSize, currentPage, currentId,
                ptype, new
                SubjectExerisesItemParser());
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
