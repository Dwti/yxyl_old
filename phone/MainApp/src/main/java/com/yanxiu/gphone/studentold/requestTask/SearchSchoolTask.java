package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.SchoolListBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.SchoolListParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class SearchSchoolTask extends AbstractAsyncTask<SchoolListBean>{
    private AsyncCallBack mAsyncCallBack;
    private String schoolName;
    private String regionId;
    public SearchSchoolTask(Context context, String schoolName, String regionId,
                            AsyncCallBack mAsyncCallBack) {
        super(context);
        this.schoolName = schoolName;
        this.regionId = regionId;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<SchoolListBean> doInBackground() {
        return YanxiuHttpApi.requestSearchSchool(0, schoolName, regionId,
                new SchoolListParser());
    }

    @Override
    public void onPostExecute(int updateId, SchoolListBean result) {
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
