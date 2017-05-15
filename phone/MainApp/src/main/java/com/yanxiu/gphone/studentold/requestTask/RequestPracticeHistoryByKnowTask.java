package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.PracticeHistoryBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.PracticeHistoryBeanParser;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestPracticeHistoryByKnowTask extends AbstractAsyncTask<PracticeHistoryBean>{
    private final int PAGESIZE = YanXiuConstant.YX_PAGESIZE_CONSTANT;
    private AsyncCallBack mAsyncCallBack;
    private String stageId;
    private String subjectId;
    private int  nextPage;
    public RequestPracticeHistoryByKnowTask(Context context, String stageId, String subjectId, int nextPage, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.stageId = stageId;
        this.subjectId = subjectId;
        this.nextPage = nextPage;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<PracticeHistoryBean> doInBackground() {
        return YanxiuHttpApi.requestPracticeHistoryByKnow(0, stageId, subjectId, nextPage, PAGESIZE, new PracticeHistoryBeanParser());
    }

    @Override
    public void onPostExecute(int updateId, PracticeHistoryBean result) {
        if(result != null){
            if(mAsyncCallBack != null && result.getStatus().getCode() == 0){
                if(result.getData() != null && result.getData().size() > 0){
                    mAsyncCallBack.update(result);
                } else {
                    mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, result.getStatus().getDesc());
                }
            } else {
                if(mAsyncCallBack != null){
                    mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, result.getStatus().getDesc());
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
