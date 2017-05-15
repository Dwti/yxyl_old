package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/17.
 */
public class ChapterListEntity implements YanxiuBaseBean {
    private DataStatusEntityBean status;
    private ArrayList<ChapterListDataEntity> data;
//    private int parentPosition;
//    private int groupPosition;
//    private int childPosition;

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public ArrayList<ChapterListDataEntity> getData() {
        return data;
    }

    public void setData(ArrayList<ChapterListDataEntity> data) {
        this.data = data;
    }


}
