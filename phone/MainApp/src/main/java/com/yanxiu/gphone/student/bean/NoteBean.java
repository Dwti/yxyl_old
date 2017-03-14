package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/3/13.
 */

public class NoteBean implements YanxiuBaseBean {
    private int qid;
    private String text;
    private ArrayList<String> images;

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
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
