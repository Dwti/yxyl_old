package com.yanxiu.gphone.hd.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/11/17.
 */
public class ExamPropertyData implements YanxiuBaseBean {
    private String avgMasterRate;
    private String masterNum;
    private String totalNum;
    private String masterLevel;

    public String getAvgMasterRate() {
        return avgMasterRate;
    }

    public void setAvgMasterRate(String avgMasterRate) {
        this.avgMasterRate = avgMasterRate;
    }

    public String getMasterNum() {
        return masterNum;
    }

    public void setMasterNum(String masterNum) {
        this.masterNum = masterNum;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getMasterLevel() {
        return masterLevel;
    }

    public void setMasterLevel(String masterLevel) {
        this.masterLevel = masterLevel;
    }
}
