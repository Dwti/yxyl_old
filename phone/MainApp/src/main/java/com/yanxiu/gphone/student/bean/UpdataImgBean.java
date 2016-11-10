package com.yanxiu.gphone.student.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JS-00 on 2016/11/9.
 */

public class UpdataImgBean {
    private boolean ischild;
    private int father_Id;
    private int child_Id;
    private List<String> ImgUrl=new ArrayList<String>();

    public int getFather_Id() {
        return father_Id;
    }

    public void setFather_Id(int father_Id) {
        this.father_Id = father_Id;
    }

    public int getChild_Id() {
        return child_Id;
    }

    public void setChild_Id(int child_Id) {
        this.child_Id = child_Id;
    }

    public List<String> getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        ImgUrl = imgUrl;
    }

    public boolean ischild() {
        return ischild;
    }

    public void setIschild(boolean ischild) {
        this.ischild = ischild;
    }
}
