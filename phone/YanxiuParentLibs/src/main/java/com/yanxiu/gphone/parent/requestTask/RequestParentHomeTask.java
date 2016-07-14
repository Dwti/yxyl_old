package com.yanxiu.gphone.parent.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.bean.ParentHomeDetailBean;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.parser.ParentHomeParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestParentHomeTask extends AbstractAsyncTask<ParentHomeDetailBean>{
    private AsyncCallBack mAsyncCallBack;
    private int currentPage;
    private int pageSize;
    public RequestParentHomeTask(Context context, int currentPage, int pageSize, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<ParentHomeDetailBean> doInBackground() {
        return YanxiuParentHttpApi.requestGetHomeInfo(currentPage, pageSize, new ParentHomeParser());
    }

    @Override
    public void onPostExecute(int updateId, ParentHomeDetailBean result) {
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
