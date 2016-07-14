package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/14.
 */
public class GroupListBean implements YanxiuBaseBean {

    private DataStatusEntityBean status;

    private ArrayList<GroupBean> data;

    private PropertyBean property;

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public ArrayList<GroupBean> getData() {
        return data;
    }

    public void setData(ArrayList<GroupBean> data) {
        this.data = data;
    }

    public PropertyBean getProperty() {
        return property;
    }

    public void setProperty(PropertyBean property) {
        this.property = property;
    }
}
