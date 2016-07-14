package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/7/16.
 */
public class DataWrongNumEntity implements YanxiuBaseBean {
    /**
     * wrongNum : 10
     */
    private String wrongNum;
    private String favoriteNum;
    private String editionName;
    private String editionId;
    private int has_knp = 1;

    public String getFavoriteNum() {
        return favoriteNum;
    }

    public void setFavoriteNum(String favoriteNum) {
        this.favoriteNum = favoriteNum;
    }

    public void setWrongNum(String wrongNum) {
        this.wrongNum = wrongNum;
    }

    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }

    public void setEditionId(String editionId) {
        this.editionId = editionId;
    }

    public String getWrongNum() {
        return wrongNum;
    }

    public String getEditionName() {
        return editionName;
    }

    public String getEditionId() {
        return editionId;
    }

    public int getHas_knp() {
        return has_knp;
    }

    public void setHas_knp(int has_knp) {
        this.has_knp = has_knp;
    }
}
