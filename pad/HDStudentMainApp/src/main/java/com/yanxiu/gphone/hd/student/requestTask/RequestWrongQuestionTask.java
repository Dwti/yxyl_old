package com.yanxiu.gphone.hd.student.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.parser.SubjectExerisesItemParser;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestWrongQuestionTask extends AbstractAsyncTask<SubjectExercisesItemBean>{
    private AsyncCallBack mAsyncCallBack;
    private String stageId;
    private String subjectId;
    private String editionId;
    private String chapterId;
    private String sectionId;
    private String volumeId;
    private String currentId;
    private String cellId;
    private int ptype;
    private int currentPage = 1;
    private final int pageSize = YanXiuConstant.YX_PAGESIZE_CONSTANT;
    public RequestWrongQuestionTask(Context context, String stageId, String subjectId, String editionId,
                                    String chapterId, String sectionId, String volumeId, int currentPage, String currentId, String cellId, int ptype,
                                    AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack = mAsyncCallBack;
        this.stageId = stageId;
        this.subjectId = subjectId;
        this.editionId = editionId;
        this.chapterId = chapterId;
        this.sectionId = sectionId;
        this.volumeId = volumeId;
        this.currentId = currentId;
        this.cellId = cellId;
        this.ptype = (ptype == 1 ? 2 : ptype);
        this.currentPage = currentPage;
    }

    @Override
    public YanxiuDataHull<SubjectExercisesItemBean> doInBackground() {
        return YanxiuHttpApi.requestWrongQuestion(0, stageId, subjectId, editionId, volumeId, chapterId, sectionId, pageSize, currentPage,
                currentId, cellId, ptype, new
                SubjectExerisesItemParser());
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
