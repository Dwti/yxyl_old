package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.MistakeEditionBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.MistakeEditionBeanParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestPracticeEditionTask extends AbstractAsyncTask<MistakeEditionBean>{
    private AsyncCallBack mAsyncCallBack;
    private String stageId;
    public RequestPracticeEditionTask(Context context, String stageId, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.stageId = stageId;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<MistakeEditionBean> doInBackground() {
        return YanxiuHttpApi.requestPracticeEdition(0, stageId, new MistakeEditionBeanParser());
    }

    @Override
    public void onPostExecute(int updateId, MistakeEditionBean result) {
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
