package com.yanxiu.gphone.parent.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.bean.ParentHomeSubjectInfoBean;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.parser.ParentHomeSubjectInfoParser;

/**
 * 获取作业详细统计信息接口数据
 * Created by Administrator on 2015/6/1.
 */
public class RequestHomeSubjectInfoTask extends AbstractAsyncTask<ParentHomeSubjectInfoBean>{
    private AsyncCallBack mAsyncCallBack;
    private int pid = 0;

    public RequestHomeSubjectInfoTask(Context context, int pid, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.pid = pid;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<ParentHomeSubjectInfoBean> doInBackground() {
        return YanxiuParentHttpApi.requestGetHwkDetail(pid, new ParentHomeSubjectInfoParser());
    }

    @Override
    public void onPostExecute(int updateId, ParentHomeSubjectInfoBean result) {
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
