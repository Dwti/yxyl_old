package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/9.
 */
public class GroupInfoBean implements YanxiuBaseBean{

    private DataStatusEntityBean status;

    private ArrayList<GroupDetailBean> data;

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public ArrayList<GroupDetailBean> getData() {
        return data;
    }

    public void setData(ArrayList<GroupDetailBean> data) {
        this.data = data;
    }
}
