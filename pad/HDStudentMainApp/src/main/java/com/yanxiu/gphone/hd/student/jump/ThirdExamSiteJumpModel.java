
package com.yanxiu.gphone.hd.student.jump;

import com.yanxiu.gphone.hd.student.bean.ExamInfoBean;
import com.yanxiu.gphone.hd.student.bean.SubjectVersionBean;

/**
 * Created by Administrator on 2015/11/19.
 */
public class ThirdExamSiteJumpModel extends BaseJumModelForResult {
    private ExamInfoBean firstExamBean; //一级考点对象
    private ExamInfoBean secondExamBean;//二级考点对象
    private SubjectVersionBean.DataEntity mDataEntity;
    public ExamInfoBean getSecondExamBean() {
        return secondExamBean;
    }

    public void setSecondExamBean(ExamInfoBean info) {
        this.secondExamBean = info;
    }


    public ExamInfoBean getFirstExamBean() {
        return firstExamBean;
    }

    public void setFirstExamBean(ExamInfoBean firstExamBean) {
        this.firstExamBean = firstExamBean;
    }

    public SubjectVersionBean.DataEntity getmDataEntity() {
        return mDataEntity;
    }

    public void setmDataEntity(SubjectVersionBean.DataEntity mDataEntity) {
        this.mDataEntity = mDataEntity;
    }
}

