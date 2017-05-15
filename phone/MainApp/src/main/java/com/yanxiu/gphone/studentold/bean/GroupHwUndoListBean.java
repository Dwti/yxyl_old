package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/10.
 */
public class GroupHwUndoListBean implements YanxiuBaseBean{
    private DataStatusEntityBean status;
    private PageBean page;

    private ArrayList<GroupHwUnBean> data;

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

    public ArrayList<GroupHwUnBean> getData() {
        return data;
    }

    public void setData(ArrayList<GroupHwUnBean> data) {
        this.data = data;
    }
}
