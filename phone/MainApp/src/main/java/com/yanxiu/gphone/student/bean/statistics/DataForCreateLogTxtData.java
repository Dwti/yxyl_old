package com.yanxiu.gphone.student.bean.statistics;

import java.util.Date;

/**
 * Created by FengRongCheng on 2016/6/4 22:47.
 * powered by yanxiu.com
 * 存储用来生成Log.txt文件的数据的表
 */
public class DataForCreateLogTxtData extends YanXiuDataBase {
    private Date uploadTime;
    private String dataName;
    private String dataContent;

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }
}
