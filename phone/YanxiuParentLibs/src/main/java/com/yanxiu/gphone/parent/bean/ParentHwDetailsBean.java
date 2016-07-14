package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by hai8108 on 16/3/25.
 */
public class ParentHwDetailsBean implements YanxiuBaseBean{
    private ParentDataStatusEntityBean status;
    private ArrayList<ParentHwDetailBean> data;

    public ParentDataStatusEntityBean getStatus () {
        return status;
    }

    public void setStatus (ParentDataStatusEntityBean status) {
        this.status = status;
    }

    public ArrayList<ParentHwDetailBean> getData () {
        return data;
    }

    public void setData (ArrayList<ParentHwDetailBean> data) {
        this.data = data;
    }
}
