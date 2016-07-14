package com.yanxiu.gphone.student.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.GroupHwUndoListBean;
import com.yanxiu.gphone.student.bean.RequestBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.parser.GroupHwUndoListParser;
import com.yanxiu.gphone.student.parser.RequestParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestGroupHwUndoListTask extends AbstractAsyncTask<GroupHwUndoListBean>{
    private AsyncCallBack mAsyncCallBack;
    private int page;
    private int pagesize;
    public RequestGroupHwUndoListTask(Context context,int page,
            int pagesize, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.page = page;
        this.pagesize = pagesize;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<GroupHwUndoListBean> doInBackground() {
        return YanxiuHttpApi.requestGroupHwUndoList(0,  page, pagesize,new GroupHwUndoListParser());
    }

    @Override
    public void onPostExecute(int updateId, GroupHwUndoListBean result) {
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
