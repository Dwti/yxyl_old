package com.yanxiu.gphone.student.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.PracticeHistoryBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.parser.PracticeHistoryBeanParser;
import com.yanxiu.gphone.student.parser.SubjectExerisesItemParser;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestGetQReportTask extends AbstractAsyncTask<SubjectExercisesItemBean>{
    private AsyncCallBack mAsyncCallBack;
    private String paperId;
    public RequestGetQReportTask(Context context, String paperId, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack = mAsyncCallBack;
        this.paperId = paperId;
    }

    @Override
    public YanxiuDataHull<SubjectExercisesItemBean> doInBackground() {
        YanxiuDataHull<SubjectExercisesItemBean> dataHull = YanxiuHttpApi.requestGetQReport(0, paperId, new SubjectExerisesItemParser());
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
