package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/7/10.
 */
public class RequestBean implements YanxiuBaseBean{
    /**
     * status : {"code":3,"desc":"加入成功"}
     */
    private DataStatusEntityBean status;

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public DataStatusEntityBean getStatus() {
        return status;
    }


}
