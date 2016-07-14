package com.yanxiu.gphone.hd.student.view.picsel.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * 相册目录
 * Created by Administrator on 2015/9/28.
 */
public class ImageBucket implements YanxiuBaseBean {
    public int count = 0;
    private String bucketName;
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
}
