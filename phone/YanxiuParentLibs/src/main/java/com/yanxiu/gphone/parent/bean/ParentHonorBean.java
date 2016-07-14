package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by lee on 16-3-30.
 */
public class ParentHonorBean implements YanxiuBaseBean {
    private ParentDataStatusEntityBean status;
    private ArrayList<ParentItemHonorBean> data;

    public ParentDataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(ParentDataStatusEntityBean status) {
        this.status = status;
    }

    public ArrayList<ParentItemHonorBean> getData() {
        return data;
    }

    public void setData(ArrayList<ParentItemHonorBean> data) {
        this.data = data;
    }
}

