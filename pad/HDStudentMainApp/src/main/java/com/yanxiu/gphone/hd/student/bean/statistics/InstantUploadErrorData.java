package com.yanxiu.gphone.hd.student.bean.statistics;

import java.util.Date;

/**
 * powered by yanxiu.com
 * 存储及时上传失败数据的表
 */
public class InstantUploadErrorData extends YanXiuDataBase {
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
