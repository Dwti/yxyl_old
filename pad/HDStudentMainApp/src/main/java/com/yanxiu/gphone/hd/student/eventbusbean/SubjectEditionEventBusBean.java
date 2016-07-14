package com.yanxiu.gphone.hd.student.eventbusbean;

import com.yanxiu.gphone.hd.student.bean.SubjectEditionBean;

/**
 * Created by Administrator on 2016/2/23.
 */
public class SubjectEditionEventBusBean {

    private SubjectEditionBean.DataEntity selectedEntity;

    public SubjectEditionEventBusBean(SubjectEditionBean.DataEntity selectedEntity){
        this.selectedEntity = selectedEntity;
    }

    public SubjectEditionBean.DataEntity getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(SubjectEditionBean.DataEntity selectedEntity) {
        this.selectedEntity = selectedEntity;
    }
}
