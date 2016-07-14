package com.yanxiu.gphone.parent.requestTask;

import android.content.Context;

import com.common.login.model.DataStatusEntityBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.bean.ParentHonorBean;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.parser.ParentHonorBeanParser;

/**
 * Created by lee on 16-3-30.
 */
public class RequestHonorListTask extends AbstractAsyncTask<ParentHonorBean> {
    private AsyncCallBack mAsyncCallBack;
    public RequestHonorListTask(Context context, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack=mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<ParentHonorBean> doInBackground() {
        return YanxiuParentHttpApi.requestHonorList(new ParentHonorBeanParser());
    }

    @Override
    public void onPostExecute(int updateId, ParentHonorBean result) {
        if(result!=null){
            if(result.getStatus().getCode()== DataStatusEntityBean.REQUEST_SUCCESS){
                mAsyncCallBack.update(result);
            }else{
                mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);
            }
        }else{
            mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);
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
