package com.yanxiu.gphone.hd.student.bean;

/**
 * Created by Administrator on 2015/7/16.
 */
public class EditionBean extends SrtBaseBean{
    /**
     * editionName : 人教版
     * editionId : 1234
     */
    private String editionName;
    private String editionId;

    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }

    public void setEditionId(String editionId) {
        this.editionId = editionId;
    }

    public String getEditionName() {
        return editionName;
    }

    public String getEditionId() {
        return editionId;
    }
}
