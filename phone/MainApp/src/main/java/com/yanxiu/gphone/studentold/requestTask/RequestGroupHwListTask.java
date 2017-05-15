package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.GroupHwListBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.GroupHwListParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestGroupHwListTask extends AbstractAsyncTask<GroupHwListBean>{
    private AsyncCallBack mAsyncCallBack;
    private int groupId;
    private int oage;
    private int pagesize;
    public RequestGroupHwListTask(Context context, int groupId, int oage,
            int pagesize, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.groupId = groupId;
        this.oage = oage;
        this.pagesize = pagesize;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<GroupHwListBean> doInBackground() {
        return YanxiuHttpApi.requestGroupHwList(0, groupId, oage, pagesize,
                new GroupHwListParser());
    }

    @Override
    public void onPostExecute(int updateId, GroupHwListBean result) {
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
