package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/15.
 */
public class MistakeEditionBean implements YanxiuBaseBean{
    /**
     * data : [{"data":{"wrongNum":5,"editionName":"人教版","editionId":1},"children":null,"name":"语文","id":2},{"data":{"wrongNum":10,"editionName":"人教版","editionId":1},"children":null,"name":"数学","id":3}]
     * page : null
     * status : {"code":0,"desc":"user get edition mistakeNum success"}
     */
    private ArrayList<DataTeacherEntity> data;
    private ArrayList<DataTeacherEntity> children;
    private DataStatusEntityBean status;

    public void setData(ArrayList<DataTeacherEntity> data) {
        this.data = data;
    }
    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }
    public ArrayList<DataTeacherEntity> getData() {
        return data;
    }
    public DataStatusEntityBean getStatus() {
        return status;
    }

    public ArrayList<DataTeacherEntity> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<DataTeacherEntity> children) {
        this.children = children;
    }
}
