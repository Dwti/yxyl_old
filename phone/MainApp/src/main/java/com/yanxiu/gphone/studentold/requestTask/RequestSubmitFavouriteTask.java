package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.RequestBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.RequestParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestSubmitFavouriteTask extends AbstractAsyncTask<RequestBean> {
    private AsyncCallBack mAsyncCallBack;
    private String stageId;
    private String subjectId;
    private String beditionId;
    private String volumeId;
    private String chapterId;
    private String sectionId;
    private String questionId;
    private String cellId;
    private int ptype;

    public static void requestTask(Context mContext, String stageId, String subjectId, String beditionId, String volumeId, String chapterId, String sectionId, String questionId, String cellId,  int ptype, AsyncCallBack mAsyncCallBack){
        RequestSubmitFavouriteTask mRequestSubmitFavouriteTask = new RequestSubmitFavouriteTask(mContext, stageId, subjectId, beditionId, volumeId, chapterId, sectionId, questionId, cellId, ptype, mAsyncCallBack);
        mRequestSubmitFavouriteTask.start();
    }
    public RequestSubmitFavouriteTask(Context context, String stageId, String subjectId, String beditionId, String volumeId, String chapterId, String sectionId, String questionId, String cellId, int ptype, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.stageId = stageId;
        this.subjectId = subjectId;
        this.beditionId = beditionId;
        this.volumeId = volumeId;
        this.chapterId = chapterId;
        this.sectionId = sectionId;
        this.questionId = questionId;
        this.cellId = cellId;
        this.ptype = ptype;
        this.mAsyncCallBack = mAsyncCallBack;
    }


    @Override
    public YanxiuDataHull<RequestBean> doInBackground() {
        return YanxiuHttpApi.requestSubmitFavourite(0, stageId, subjectId, beditionId, volumeId, chapterId, sectionId, questionId, cellId, ptype, new RequestParser());
    }
    @Override
    public void onPostExecute(int updateId, RequestBean result) {
        if (result != null) {
            if (mAsyncCallBack != null && result.getStatus().getCode() == 0) {
                mAsyncCallBack.update(result);
            } else {
                if (mAsyncCallBack != null) {
                    mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, result.getStatus().getDesc());
                }
            }
        } else {
            if (mAsyncCallBack != null) {
                mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);
            }
        }
    }

    @Override
    public void netNull() {
        if (mAsyncCallBack != null) {
            mAsyncCallBack.dataError(ErrorCode.NETWORK_NOT_AVAILABLE, null);
        }
    }

    @Override
    public void netErr(int updateId, String errMsg) {
        if (mAsyncCallBack != null) {
            mAsyncCallBack.dataError(ErrorCode.NETWORK_REQUEST_ERROR, errMsg);
        }
    }

    @Override
    public void dataNull(int updateId, String errMsg) {
        if (mAsyncCallBack != null) {
            mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, errMsg);
        }
    }
}
