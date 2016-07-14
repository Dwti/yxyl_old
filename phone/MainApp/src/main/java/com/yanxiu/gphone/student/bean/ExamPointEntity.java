package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/11/16.
 */
public class ExamPointEntity implements YanxiuBaseBean {
    public static final int ITEM = 0;
    public static final int SECTION = 1;
    private Object originData;
    private Object sectionOrignData;
    private int type;
    public int sectionPosition;
    public int listPosition;


    private String name;
    private String value;



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSectionPosition() {
        return sectionPosition;
    }

    public void setSectionPosition(int sectionPosition) {
        this.sectionPosition = sectionPosition;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }


    public Object getOriginData() {
        return originData;
    }

    public void setOriginData(Object originData) {
        this.originData = originData;
    }


    public Object getSectionOrignData() {
        return sectionOrignData;
    }

    public void setSectionOrignData(Object sectionOrignData) {
        this.sectionOrignData = sectionOrignData;
    }
}
