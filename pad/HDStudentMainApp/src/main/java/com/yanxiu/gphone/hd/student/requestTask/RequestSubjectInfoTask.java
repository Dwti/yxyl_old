package com.yanxiu.gphone.hd.student.requestTask;

import android.content.Context;

import com.common.core.utils.LogInfo;
import com.common.login.model.LocalCacheBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.hd.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.hd.student.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.hd.student.parser.SubjectVersionParser;


/**
 * Created by Administrator on 2015/7/17.
 */
public class RequestSubjectInfoTask
        extends AbstractAsyncTask<SubjectVersionBean> {

    private static final String CACHE_ID = "subjectversionbean";
    private int stageId;
    private AsyncLocalCallBack asyncLocalCallBack;

    public RequestSubjectInfoTask(Context context, int stageId, AsyncLocalCallBack callBack) {
        super(context);
        this.stageId = stageId;
        this.asyncLocalCallBack = callBack;
    }

//    @Override
//    public SubjectVersionBean loadLocalData() {
//        String cacheData = getCacheData();
//        SubjectVersionBean subjectVersionBean = YanxiuApplication.getInstance().getSubjectVersionBean();
//        if (cacheData != null) {
//            try {
//                subjectVersionBean = new SubjectVersionParser().initialParse(cacheData);
//                if(subjectVersionBean != null && subjectVersionBean.getData() != null && subjectVersionBean.getData().size() > 0){
//                    return subjectVersionBean;
//                }
//            } catch (Exception e) {
//                return null;
//            }
//        }
//        if(subjectVersionBean != null && subjectVersionBean.getData() != null && subjectVersionBean.getData().size() > 0){
//            return subjectVersionBean;
//        }
//        return null;
//    }
//
//    @Override
//    public boolean loadLocalDataComplete(
//            SubjectVersionBean subjectVersionBean) {
//        if (subjectVersionBean != null) {
//            if (asyncLocalCallBack != null) {
//                asyncLocalCallBack.updateLocal(subjectVersionBean);
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }

    @Override
    public YanxiuDataHull<SubjectVersionBean> doInBackground() {
        LogInfo.log("king", "doInBackground ");
        YanxiuDataHull<SubjectVersionBean> dataHull = YanxiuHttpApi
                .requestSubjectInfo(0, stageId, new SubjectVersionParser());
        if (dataHull != null && dataHull.getDataType() == YanxiuDataHull.DataType.DATA_IS_INTEGRITY) {
            SubjectVersionBean subjectVersionBean = dataHull.getDataEntity();
            if(subjectVersionBean!=null && subjectVersionBean.getStatus()!=null && subjectVersionBean.getStatus().getCode() == 0){
                savaCacheData(dataHull.getSourceData());
            }
            return dataHull;
        }
        return null;
    }

    @Override public void onPostExecute(int updateId,
            SubjectVersionBean result) {
        if(result != null && result.getStatus() != null && result.getStatus().getCode() == 0){
            if(asyncLocalCallBack != null){
                asyncLocalCallBack.update(result);
            }
        } else {
            if (asyncLocalCallBack != null && !isLocalSucceed()) {
                asyncLocalCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);
            }
        }
    }

    @Override
    public void dataNull(int dataTypeId, String errMsg) {
        if (asyncLocalCallBack != null && !isLocalSucceed()) {
            asyncLocalCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, errMsg);
        }
    }

    @Override
    public void netErr(int dataTypeId, String errMsg) {
        if (asyncLocalCallBack != null && !isLocalSucceed()) {
            asyncLocalCallBack.dataError(ErrorCode.NETWORK_REQUEST_ERROR, errMsg);
        }
    }

    @Override
    public void netNull() {
        if (asyncLocalCallBack != null && !isLocalSucceed()) {
            asyncLocalCallBack.dataError(ErrorCode.NETWORK_NOT_AVAILABLE, null);
        }
    }

    private String getCacheData() {
        LocalCacheBean localCacheBean = LocalCacheBean.findDataById(CACHE_ID);
        if (localCacheBean != null) {
            LogInfo.log("king", "getCacheSuccess");
            return localCacheBean.getCacheData();
        }
        return null;
    }

    public static void savaCacheData(String data) {
        LocalCacheBean localCacheBean = new LocalCacheBean();
        localCacheBean.setCacheId(CACHE_ID);
        localCacheBean.setCacheData(data);
        localCacheBean.setCacheTime(System.currentTimeMillis());
        boolean savaSuccess = LocalCacheBean.saveData(localCacheBean);
        LogInfo.log("king", "savaSuccess = " + savaSuccess);
    }

}
