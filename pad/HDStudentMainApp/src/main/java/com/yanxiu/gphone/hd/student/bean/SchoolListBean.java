package com.yanxiu.gphone.hd.student.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/6/12.
 */
public class SchoolListBean extends SrtBaseBean{

    private ArrayList<School> data;
    /**
     * status : {"code":0,"desc":"get schools success"}
     */
    private DataStatusEntityBean status;

    public ArrayList<School> getData() {
        return data;
    }

    public void setData(ArrayList<School> data) {
        this.data = data;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public DataStatusEntityBean getStatus() {
        return status;
    }


}
