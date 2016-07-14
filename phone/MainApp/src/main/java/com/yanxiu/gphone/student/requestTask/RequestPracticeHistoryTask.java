package com.yanxiu.gphone.student.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.PracticeHistoryBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.parser.PracticeHistoryBeanParser;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestPracticeHistoryTask extends AbstractAsyncTask<PracticeHistoryBean>{
    private final int PAGESIZE = YanXiuConstant.YX_PAGESIZE_CONSTANT;
    private AsyncCallBack mAsyncCallBack;
    private String stageId;
    private String subjectId;
    private String beditionId;
    private String volume;
    private int  nextPage;
    public RequestPracticeHistoryTask(Context context, String stageId, String subjectId, String beditionId, String volume, int nextPage, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.stageId = stageId;
        this.subjectId = subjectId;
        this.beditionId = beditionId;
        this.volume = volume;
        this.nextPage = nextPage;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<PracticeHistoryBean> doInBackground() {
        return YanxiuHttpApi.requestPracticeHistory(0, stageId, subjectId, beditionId, volume, nextPage, PAGESIZE, new PracticeHistoryBeanParser());
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
