package com.yanxiu.gphone.hd.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/17.
 */
public class PracticeHistoryBean extends SrtBaseBean implements YanxiuBaseBean {
    private DataStatusEntityBean status;
    private YanxiuPageInfoBean page;
    private ArrayList<PracticeHistoryChildBean> data;

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public YanxiuPageInfoBean getPage() {
        return page;
    }

    public void setPage(YanxiuPageInfoBean page) {
        this.page = page;
    }

    public ArrayList<PracticeHistoryChildBean> getData() {
        return data;
    }

    public void setData(ArrayList<PracticeHistoryChildBean> data) {
        this.data = data;
    }
}
