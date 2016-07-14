package com.yanxiu.gphone.hd.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/9.
 */
public class ClassInfoBean implements YanxiuBaseBean {

    private DataStatusEntityBean status;

    private ArrayList<ClassDetailBean> data;

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public ArrayList<ClassDetailBean> getData() {
        return data;
    }

    public void setData(ArrayList<ClassDetailBean> data) {
        this.data = data;
    }
}
