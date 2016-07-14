package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/15.
 */
public class SectionMistakeBean implements YanxiuBaseBean {
    /**
     * data : [{"data":{"wrongNum":10},"children":[{"data":{"wrongNum":5},"children":null,"name":"第一节 小明被打，求面积","id":23},{"data":{"wrongNum":5},"children":null,"name":"第二节 统计小明被打次数","id":14}],"name":"第一章 微分求小明心里阴影面积","id":12}]
     * page : null
     * status : {"code":0,"desc":"user get section mistakeNum success"}
     */
    private ArrayList<DataTeacherEntity> data;
    private String page;
    private DataStatusEntityBean status;

    public void setData(ArrayList<DataTeacherEntity> data) {
        this.data = data;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public ArrayList<DataTeacherEntity> getData() {
        return data;
    }

    public String getPage() {
        return page;
    }

    public DataStatusEntityBean getStatus() {
        return status;
    }
}
