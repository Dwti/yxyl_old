package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * Created by Administrator on 2015/11/17.
 */
public class ExamListInfo implements YanxiuBaseBean {
    private DataStatusEntityBean status;
    private List<ExamInfoBean> data;
    public List<ExamInfoBean> getData() {
        return data;
    }
    public void setData(List<ExamInfoBean> data) {
        this.data = data;
    }


    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }
}
