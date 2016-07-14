package com.yanxiu.gphone.student.requestTask;

import android.content.Context;

import com.common.core.utils.LogInfo;
import com.common.login.model.LocalCacheBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.student.parser.SubjectEditionParser;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestEditionInfoTask extends AbstractAsyncTask<SubjectEditionBean> {
    //    private AsyncCallBack mAsyncCallBack;
    private AsyncLocalCallBack asyncLocalCallBack;
    private String stageId;
    private String subjectId;
    public final static String CACHE_ID = "SubjectEditionBean";

    public RequestEditionInfoTask(Context context, String stageId, String subjectId,
                                  AsyncLocalCallBack asyncLocalCallBack) {
        super(context);
        this.stageId = stageId;
        this.subjectId = subjectId;
        this.asyncLocalCallBack = asyncLocalCallBack;
    }

//    @Override
//    public SubjectEditionBean loadLocalData() {
//        String cacheData = getCacheData();
//        if (cacheData != null) {
//            try {
//                SubjectEditionBean subjectEditionBean = new SubjectEditionParser().initialParse(cacheData);
//                return subjectEditionBean;
//            } catch (Exception e) {
//                return null;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public boolean loadLocalDataComplete(SubjectEditionBean subjectEditionBean) {
//        if (subjectEditionBean != null) {
//            if (asyncLocalCallBack != null) {
//                asyncLocalCallBack.updateLocal(subjectEditionBean);
//                return true;
//            }
//        }
//        return false;
//    }
    @Override
    public YanxiuDataHull<SubjectEditionBean> doInBackground() {
        YanxiuDataHull<SubjectEditionBean> dataHull = YanxiuHttpApi.requestEditionInfo(0, stageId, subjectId, new SubjectEditionParser());
        if (dataHull != null && dataHull.getDataType() == YanxiuDataHull.DataType.DATA_IS_INTEGRITY) {
            SubjectEditionBean subjectBean = dataHull.getDataEntity();
            if(subjectBean!=null && subjectBean.getStatus()!=null && subjectBean.getStatus().getCode() == 0){
                savaCacheData(dataHull.getSourceData());
            }
            return dataHull;
        }
        return null;
    }

    @Override
    public void onPostExecute(int updateId, SubjectEditionBean result) {
        if (result != null) {
            SubjectEditionBean subjectEditionBean = result;
            ArrayList<SubjectEditionBean.DataEntity> dataEntity = (ArrayList) subjectEditionBean.getData();
            if (dataEntity == null || dataEntity.size() <= 0) {
                if(!isLocalSucceed()){
                    if (asyncLocalCallBack != null) {
                        asyncLocalCallBack.update(result);
                    }
                }
            }else{
                if(!isLocalSucceed()){
                    if (asyncLocalCallBack != null) {
                        asyncLocalCallBack.update(result);
                    }
                }
            }
        } else {
            if (asyncLocalCallBack != null) {
                asyncLocalCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);
            }
        }
    }

    @Override
    public void netNull() {
        if (asyncLocalCallBack != null) {
            asyncLocalCallBack.dataError(ErrorCode.NETWORK_NOT_AVAILABLE, null);
        }
    }

    @Override
    public void netErr(int updateId, String errMsg) {
        if (asyncLocalCallBack != null) {
            asyncLocalCallBack.dataError(ErrorCode.NETWORK_REQUEST_ERROR, errMsg);
        }
    }

    @Override
    public void dataNull(int updateId, String errMsg) {
        if (asyncLocalCallBack != null) {
            asyncLocalCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, errMsg);
        }
    }

    public String getCacheData() {
        LocalCacheBean localCacheBean = LocalCacheBean.findDataById(CACHE_ID + stageId + "--" + subjectId);
        if (localCacheBean != null) {
            LogInfo.log("geny", "getCacheSuccess");
            return localCacheBean.getCacheData();
        }
        return null;
    }

    private void savaCacheData(String data) {
        LocalCacheBean localCacheBean = new LocalCacheBean();
        localCacheBean.setCacheId(CACHE_ID + stageId + "--" + subjectId);
        localCacheBean.setCacheData(data);
        localCacheBean.setCacheTime(System.currentTimeMillis());
        boolean savaSuccess = LocalCacheBean.saveData(localCacheBean);
        LogInfo.log("geny", "savaSuccess = " + savaSuccess);
    }
}
