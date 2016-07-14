package com.yanxiu.gphone.hd.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * Created by Administrator on 2015/9/24.
 */
public class RankResultBean implements YanxiuBaseBean {
    private DataStatusEntityBean status;
    private RankPropertyBean property;
    private List<RankItemBean> data;

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public RankPropertyBean getProperty() {
        return property;
    }

    public void setProperty(RankPropertyBean property) {
        this.property = property;
    }

    public List<RankItemBean> getData() {
        return data;
    }

    public void setData(List<RankItemBean> data) {
        this.data = data;
    }
}
