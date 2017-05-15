package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.MistakeFragmentBeanParser;

/**
 * Created by Canghaixiao.
 * Time : 2017/4/10 10:43.
 * Function :
 */

public class RequestWrongChapterask extends AbstractAsyncTask<YanxiuBaseBean> {
    private Context context;
    private AsyncCallBack mAsyncCallBack;
    private String stageId;
    private String subjectId;
    private String editionId;
    public RequestWrongChapterask(Context context ,String stageId, String subjectId ,String editionId ,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.context=context;
        this.stageId=stageId;
        this.subjectId=subjectId;
        this.editionId=editionId;
        this.mAsyncCallBack=mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<YanxiuBaseBean> doInBackground() {
        return YanxiuHttpApi.requestMistakeChapter(0,stageId,subjectId,editionId,new MistakeFragmentBeanParser());
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
