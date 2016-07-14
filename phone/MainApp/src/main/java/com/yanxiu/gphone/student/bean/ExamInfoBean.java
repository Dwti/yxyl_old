package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * Created by Administrator on 2015/11/17.
 */
public class ExamInfoBean implements YanxiuBaseBean{
    private String id;
    private String name;

    private ExamPropertyData data;
    private List<ExamInfoBean> children;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExamInfoBean> getChildren() {
        return children;
    }

    public void setChildren(List<ExamInfoBean> children) {
        this.children = children;
    }

    public ExamPropertyData getData() {
        return data;
    }

    public void setData(ExamPropertyData data) {
        this.data = data;
    }



}
