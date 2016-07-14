package com.yanxiu.gphone.student.utils.statistics.requestAsycn;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.statistics.UploadInstantPointDataBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.utils.statistics.requestAsycn.parser.UploadInstantPointDataParser;

import java.util.HashMap;
import java.util.List;


/**
 * Created by frc on 16-6-2.
 */
public class UploadInstantPointDataTask extends AbstractAsyncTask<UploadInstantPointDataBean> {

    private HashMap params;
    private AsyncCallBack mAsyncCallBack;

    public UploadInstantPointDataTask(Context context, HashMap<String, String> params, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.params = params;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<UploadInstantPointDataBean> doInBackground() {
        return YanxiuHttpApi.instantUploadPointData(0, params, new UploadInstantPointDataParser());
    }

    @Override
    public void onPostExecute(int updateId, UploadInstantPointDataBean result) {
        if (result != null) {
            if(mAsyncCallBack != null) {
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

    @Override
    public void tokenInvalidate(String msg) {

    }
}
