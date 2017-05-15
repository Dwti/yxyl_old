package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by sunpeng on 2017/3/13.
 */

public class NoteBean implements YanxiuBaseBean {
    private String qid;
    private String text;
    private ArrayList<String> images = new ArrayList<>();

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
