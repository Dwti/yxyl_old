package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.MistakeDoWorkBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.MisReDoworkParser;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/14 15:39.
 * Function :
 */

public class MistakeDoWorkTask extends AbstractAsyncTask<MistakeDoWorkBean> {

    private String stageId;
    private String subjectId;
    private String lastWqid;
    private String lastWqnumber;
    private String deleteWqidList;
    private AsyncCallBack mAsyncCallBack;

    public MistakeDoWorkTask(Context context, String stageId, String subjectId, String lastWqid, String lastWqnumber, String deleteWqidList, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.stageId=stageId;
        this.subjectId=subjectId;
        this.lastWqid=lastWqid;
        this.lastWqnumber=lastWqnumber;
        this.deleteWqidList=deleteWqidList;
        this.mAsyncCallBack=mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<MistakeDoWorkBean> doInBackground() {
        return YanxiuHttpApi.requestMisRedoWork(0,stageId,subjectId,lastWqid,lastWqnumber,deleteWqidList,new MisReDoworkParser());
    }

    @Override
    public void onPostExecute(int updateId, MistakeDoWorkBean result) {
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
