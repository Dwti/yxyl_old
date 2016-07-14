package com.yanxiu.gphone.student.view.picsel.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * 图片对象
 * Created by Administrator on 2015/9/28.
 */
public class ImageItem implements YanxiuBaseBean {
    private String imageId;
    private String thumbnailPath;
    private String imagePath;
    private String dateToken;
    private boolean isSelected = false;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getDateToken() {
        return dateToken;
    }

    public void setDateToken(String dateToken) {
        this.dateToken = dateToken;
    }
}
