package com.yanxiu.gphone.student.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/4/1 17:16.
 * Function :
 */

public class MistakeAllFragBean implements Serializable{
    int id;
    String name;
    int question_num;
    ArrayList<Integer> qids;
    ArrayList<MistakeAllFragBean> children;

    private boolean isHaveChildren=true;
    private int Hierarchy=-1;
    private boolean isExPand=false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuestion_num() {
        return question_num;
    }

    public void setQuestion_num(int question_num) {
        this.question_num = question_num;
    }

    public ArrayList<Integer> getQids() {
        return qids;
    }

    public void setQids(ArrayList<Integer> qids) {
        this.qids = qids;
    }

    public ArrayList<MistakeAllFragBean> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<MistakeAllFragBean> children) {
        this.children = children;
    }

    public boolean isHaveChildren() {
        return isHaveChildren;
    }

    public void setHaveChildren(boolean haveChildren) {
        isHaveChildren = haveChildren;
    }

    public int getHierarchy() {
        return Hierarchy;
    }

    public void setHierarchy(int hierarchy) {
        Hierarchy = hierarchy;
    }

    public boolean isExPand() {
        return isExPand;
    }

    public void setExPand(boolean exPand) {
        isExPand = exPand;
    }

}
