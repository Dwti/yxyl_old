package com.yanxiu.gphone.hd.student.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.bean.SectionMistakeBean;
import com.yanxiu.gphone.hd.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.parser.SectionMistakeBeanParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestFavouriteSectionTask extends AbstractAsyncTask<SectionMistakeBean> {
    private AsyncCallBack mAsyncCallBack;
    private String stageId;
    private String subjectId;
    private String beditionId;
    private String volume;

    private int ptype;

    public RequestFavouriteSectionTask(Context context, String stageId, String subjectId, String beditionId, String volume, int ptype,
                                       AsyncCallBack mAsyncCallBack) {
        super(context);
        this.stageId = stageId;
        this.subjectId = subjectId;
        this.beditionId = beditionId;
        this.volume = volume;
        this.ptype = (ptype == 1 ? 2 : ptype);
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<SectionMistakeBean> doInBackground() {
        return YanxiuHttpApi.requestSectionFavourite(0, stageId, subjectId, beditionId, volume, ptype, new SectionMistakeBeanParser());
    }

    @Override
    public void onPostExecute(int updateId, SectionMistakeBean result) {
        if (result != null) {
            if (mAsyncCallBack != null) {
                mAsyncCallBack.update(result);
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
