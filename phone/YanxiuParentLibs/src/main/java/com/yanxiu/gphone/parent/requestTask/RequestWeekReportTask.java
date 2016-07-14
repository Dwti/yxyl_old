package com.yanxiu.gphone.parent.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.bean.ParentWeekReportBean;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.parser.ParentWeekReportBeanParser;

/**
 * 获取上一周下一周周报数据
 * Created by Administrator on 2015/6/1.
 */
public class RequestWeekReportTask extends AbstractAsyncTask<ParentWeekReportBean>{
    private AsyncCallBack mAsyncCallBack;
    private int type = 0;
    private int week;
    private String year;

    public RequestWeekReportTask (Context context, int type, int week, String year, AsyncCallBack
            mAsyncCallBack) {
        super(context);
        this.type = type;
        this.week = week;
        this.year = year;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<ParentWeekReportBean> doInBackground() {
        return YanxiuParentHttpApi.requestWeekReportData(type, week, year, new
                ParentWeekReportBeanParser());
    }

    @Override
    public void onPostExecute(int updateId, ParentWeekReportBean result) {
        if(result != null){
            if(result.getProperty() != null) {
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
