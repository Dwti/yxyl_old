package com.yanxiu.gphone.hd.student.bean;

import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;

/**
 * Created by Administrator on 2016/1/20.
 */
public class TitleBean {
    private String name;
    private int drawableId;
    private YanXiuConstant.TITLE_ENUM title_enum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public YanXiuConstant.TITLE_ENUM getTitle_enum() {
        return title_enum;
    }

    public void setTitle_enum(YanXiuConstant.TITLE_ENUM title_enum) {
        this.title_enum = title_enum;
    }
}
