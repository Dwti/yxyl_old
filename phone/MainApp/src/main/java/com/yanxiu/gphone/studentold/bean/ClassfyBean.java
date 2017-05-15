package com.yanxiu.gphone.studentold.bean;

/**
 * Created by Administrator on 2016/9/7.
 */
public class ClassfyBean {
    private int id;

    private String name;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    private boolean select;



    public ClassfyBean (int id, String name) {
        this.id = id;
        this.name = name;
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
}
