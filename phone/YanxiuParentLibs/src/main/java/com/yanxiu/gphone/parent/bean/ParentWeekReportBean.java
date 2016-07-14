package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by hai8108 on 16/3/25.
 */
public class ParentWeekReportBean implements YanxiuBaseBean{
    private ParentDataStatusEntityBean status;
    private ParentPublicPropertyBean property;
    private ArrayList<ParentWeekReportDataBean> data;

    public ParentDataStatusEntityBean getStatus () {
        return status;
    }

    public void setStatus (ParentDataStatusEntityBean status) {
        this.status = status;
    }

    public ParentPublicPropertyBean getProperty () {
        return property;
    }

    public void setProperty (ParentPublicPropertyBean property) {
        this.property = property;
    }

    public ArrayList<ParentWeekReportDataBean> getData () {
        return data;
    }

    public void setData (ArrayList<ParentWeekReportDataBean> data) {
        this.data = data;
    }
}
