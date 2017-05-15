package com.yanxiu.gphone.studentold.view.picsel.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * 相册目录
 * Created by Administrator on 2015/9/28.
 */
public class ImageBucket implements YanxiuBaseBean {
    public int count = 0;
    private String bucketName;
    private String dateToken;//取相册中最新修改的第一张图片时间
    private List<ImageItem> imageList;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public List<ImageItem> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageItem> imageList) {
        this.imageList = imageList;
    }

    public String getDateToken() {
        return dateToken;
    }

    public void setDateToken(String dateToken) {
        this.dateToken = dateToken;
    }
}
