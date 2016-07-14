package com.yanxiu.gphone.student.jump;

import com.yanxiu.gphone.student.bean.SubjectVersionBean;

/**
 * Created by Administrator on 2015/11/16.
 */
public class ExamPointJumpModel extends BaseJumpModel {
    private SubjectVersionBean.DataEntity dataEntity;

    public SubjectVersionBean.DataEntity getDataEntity() {
        return dataEntity;
    }

    public void setDataEntity(SubjectVersionBean.DataEntity dataEntity) {
        this.dataEntity = dataEntity;
    }
}
