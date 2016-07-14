package com.yanxiu.gphone.parent.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.bean.ParentHwDetailsBean;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.parser.ParentHwDetailsParser;

/**
 * 作业数据获取各学科统计详情数据接口
 * Created by Administrator on 2015/6/1.
 */
public class RequestHwDetailsTask extends AbstractAsyncTask<ParentHwDetailsBean>{
    private AsyncCallBack mAsyncCallBack;
    private String classId;
    private int week;
    private String year;

    public RequestHwDetailsTask (Context context, String classId, int week, String year,
                                 AsyncCallBack
            mAsyncCallBack) {
        super(context);
        this.classId = classId;
        this.week = week;
        this.year = year;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<ParentHwDetailsBean> doInBackground() {
        return YanxiuParentHttpApi.requestHwListDetailsData(classId, week, year, new
                ParentHwDetailsParser());
    }

    @Override
    public void onPostExecute(int updateId, ParentHwDetailsBean result) {
        if(result != null){
            if(result.getData() != null && result.getData().size() >0 ) {
                mAsyncCallBack.update(result);
            } else {
                mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);
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
