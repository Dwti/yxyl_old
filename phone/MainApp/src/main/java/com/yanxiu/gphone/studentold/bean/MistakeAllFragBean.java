package com.yanxiu.gphone.studentold.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Canghaixiao.
 * Time : 2017/4/1 17:16.
 * Function :
 */

public class MistakeAllFragBean implements Serializable{
    private int id;
    private String name;
    private int question_num;
    private ArrayList<Integer> qids;
    private ArrayList<MistakeAllFragBean> children;
    private String lft;
    private String lvl;
    private String rgt;
    private String root;
    private String sep;

    private boolean isHaveChildren=false;
    private int Hierarchy=-1;
    private boolean isExPand=false;

    public String getLft() {
        return lft;
    }

    public void setLft(String lft) {
        this.lft = lft;
    }

    public String getLvl() {
        return lvl;
    }

    public void setLvl(String lvl) {
        this.lvl = lvl;
    }

    public String getRgt() {
        return rgt;
    }

    public void setRgt(String rgt) {
        this.rgt = rgt;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getSep() {
        return sep;
    }

    public void setSep(String sep) {
        this.sep = sep;
    }

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
