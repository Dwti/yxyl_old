package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.SubjectExerisesItemParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestIntelliExeTask extends AbstractAsyncTask<SubjectExercisesItemBean>{
    private AsyncCallBack mAsyncCallBack;
    private int stageId;
    private String subjectId;
    private String editionId;
    private String chapterId;
    private String sectionId;
    private String cellId;
    private int questNum;
    private String volumeId;
    public RequestIntelliExeTask(Context context, int stageId, String subjectId, String editionId,
                                 String chapterId, String sectionId, int questNum,
                                 String volumeId, String cellId, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack = mAsyncCallBack;
        this.stageId = stageId;
        this.subjectId = subjectId;
        this.editionId = editionId;
        this.chapterId = chapterId;
        this.sectionId = sectionId;
        this.questNum = questNum;
        this.volumeId = volumeId;
        this.cellId = cellId;
    }

    @Override
    public YanxiuDataHull<SubjectExercisesItemBean> doInBackground() {
        YanxiuDataHull<SubjectExercisesItemBean> dataHull = YanxiuHttpApi.requestIntelliExe(0, stageId, subjectId, editionId,chapterId, sectionId, questNum,volumeId,
                cellId, new SubjectExerisesItemParser());
       LogInfo.log("geny", "RequestIntelliExeTask---dataHull----" + dataHull.getSourceData());
        return dataHull;
    }

    @Override
    public void onPostExecute(int updateId, SubjectExercisesItemBean result) {
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
