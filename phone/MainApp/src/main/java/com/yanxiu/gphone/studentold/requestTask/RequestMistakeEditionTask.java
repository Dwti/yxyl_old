package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;

import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.DataTeacherEntity;
import com.yanxiu.gphone.studentold.bean.MistakeEditionBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.studentold.parser.MistakeEditionBeanParser;

import java.util.List;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestMistakeEditionTask extends AbstractAsyncTask<MistakeEditionBean> {
    private AsyncLocalCallBack mAsyncCallBack;
    private String stageId;
    List<DataTeacherEntity> dataList;
    private String subjectEditionCacheId;

    public RequestMistakeEditionTask(Context context, String stageId, AsyncLocalCallBack mAsyncCallBack) {
        super(context);
        this.stageId = stageId;
        subjectEditionCacheId = "subject_edition_cache_id_stageid_" + stageId + LoginModel.getToken();
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<MistakeEditionBean> doInBackground() {
        return YanxiuHttpApi.requestMistakeEdition(0, stageId, new MistakeEditionBeanParser());
    }
    @Override
    public void onPostExecute(int updateId, MistakeEditionBean result) {
        if (result != null) {
            if (mAsyncCallBack != null && result.getStatus().getCode() == 0) {
                mAsyncCallBack.update(result);
            } else {
                if (mAsyncCallBack != null) {
                    mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, result.getStatus().getDesc());
                }
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
}
