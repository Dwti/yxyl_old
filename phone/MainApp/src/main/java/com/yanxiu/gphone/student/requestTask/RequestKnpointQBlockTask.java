package com.yanxiu.gphone.student.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.DataStatusEntityBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.parser.SubjectExerisesItemParser;
import com.common.core.utils.StringUtils;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestKnpointQBlockTask extends AbstractAsyncTask<SubjectExercisesItemBean>{
//    fromType: 0 出题出处（从哪个模块出题 0 表示知识点列表出题， 1表示从解析处出题  2表示从考点掌握出题）
    public static final String KNP_QUSETION = "0";
    public static final String ANA_QUSETION = "1";
    public static final String EXAM_QUSETION = "2";



    private AsyncCallBack mAsyncCallBack;
    private int stageId;
    private String subjectId;
    private String knpId1;
    private String knpId2;
    private String knpId3;
    private String fromType;

    public RequestKnpointQBlockTask(Context context, int stageId, String subjectId, String knpId1, String knpId2, String knpId3,
                                    String fromType, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mAsyncCallBack = mAsyncCallBack;
        this.stageId = stageId;
        this.subjectId = subjectId;
        this.knpId1 = knpId1;
        this.knpId2 = knpId2;
        this.knpId3 = knpId3;
        this.fromType = fromType;
    }

    @Override
    public YanxiuDataHull<SubjectExercisesItemBean> doInBackground() {
        return YanxiuHttpApi.requestGetKnpointQBlock(0, stageId, subjectId, knpId1, knpId2, knpId3,
                fromType, new SubjectExerisesItemParser());
    }

    @Override
    public void onPostExecute(int updateId, SubjectExercisesItemBean result) {
        if(result != null){
            if(mAsyncCallBack != null){
                if(result.getStatus()!=null){
                    if(result.getStatus().getCode()==DataStatusEntityBean.REQUEST_SUCCESS){
                        mAsyncCallBack.update(result);
                    }else{
                        if(StringUtils.isEmpty(result.getStatus().getDesc())){
                            mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);
                        }else{
                            mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL,result.getStatus().getDesc());
                        }

                    }
                }else{

                        mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);

                }


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
