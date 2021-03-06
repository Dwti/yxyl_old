package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.ChapterListEntity;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.ChapterListParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestKnpTask extends AbstractAsyncTask<ChapterListEntity>{
    private AsyncCallBack mAsyncCallBack;
    private int stageId;
    private String subjectId;

    public RequestKnpTask(Context context, int stageId, String subjectId, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack = mAsyncCallBack;
        this.stageId = stageId;
        this.subjectId = subjectId;
    }

    @Override
    public YanxiuDataHull<ChapterListEntity> doInBackground() {
        return YanxiuHttpApi.requestExamInfo(subjectId, new ChapterListParser());
//        return YanxiuHttpApi.requestGetKnpList(0, stageId, subjectId, new ChapterListParser());
    }

    @Override
    public void onPostExecute(int updateId, ChapterListEntity result) {
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
