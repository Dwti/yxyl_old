package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.DataStatusEntityBean;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.DataStatusEntityBeanParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestSubmitQuesitonTask extends AbstractAsyncTask<DataStatusEntityBean>{
//    "ppstatus" : 0         状态     0 未作答， 1 未完成， 2 已完成
    public static final int SUBMIT_CODE = 2;
    public static final int LIVE_CODE = 1;
    private AsyncCallBack mAsyncCallBack;
    private SubjectExercisesItemBean bean;
    private int ppstatus;
    public RequestSubmitQuesitonTask(Context context, SubjectExercisesItemBean bean,int ppstatus, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack = mAsyncCallBack;
        this.bean = bean;
        this.ppstatus = ppstatus;
    }

    @Override
    public YanxiuDataHull<DataStatusEntityBean> doInBackground() {
        return YanxiuHttpApi.requestsubmitQ(0, bean, ppstatus,
                new DataStatusEntityBeanParser());
    }

    @Override
    public void onPostExecute(int updateId, DataStatusEntityBean result) {
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
