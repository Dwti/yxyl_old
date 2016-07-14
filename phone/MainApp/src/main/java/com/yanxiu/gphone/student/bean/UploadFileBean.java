package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/17.
 */
public class UploadFileBean implements YanxiuBaseBean {
    private DataStatusEntityBean status;
    private ArrayList<UploadDataBean> data;

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public ArrayList<UploadDataBean> getData() {
        return data;
    }

    public void setData(ArrayList<UploadDataBean> data) {
        this.data = data;
    }
}
