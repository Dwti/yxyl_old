package com.yanxiu.gphone.student.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.ChapterListEntity;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.parser.ChapterListParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestChapterListTask extends AbstractAsyncTask<ChapterListEntity>{
    private AsyncCallBack mAsyncCallBack;
    private int stageId;
    private String subjectId;
    private String editionId;
    private String volume;
    public RequestChapterListTask(Context context, int stageId, String subjectId, String editionId,
                                  String volume, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack = mAsyncCallBack;
        this.stageId = stageId;
        this.subjectId = subjectId;
        this.editionId = editionId;
        this.volume = volume;
    }

    @Override
    public YanxiuDataHull<ChapterListEntity> doInBackground() {
//        try {
//            Thread.sleep(6 * 1000);
//        } catch (InterruptedException e) {
//        }
        return YanxiuHttpApi.requestGetChapterList(0, stageId, subjectId, editionId, volume,
                new ChapterListParser());
    }

    @Override
    public void onPostExecute(int updateId, ChapterListEntity result) {
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
