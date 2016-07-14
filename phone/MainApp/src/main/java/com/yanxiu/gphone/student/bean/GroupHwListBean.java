package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/10.
 */
public class GroupHwListBean implements YanxiuBaseBean{

    private DataStatusEntityBean status;

    private PageBean page;

    private ArrayList<GroupHwBean> data;

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public ArrayList<GroupHwBean> getData() {
        return data;
    }

    public void setData(ArrayList<GroupHwBean> data) {
        this.data = data;
    }
}
