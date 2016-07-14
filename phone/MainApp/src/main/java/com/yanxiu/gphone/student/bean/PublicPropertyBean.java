package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by hai8108 on 16/4/11.
 */
public class PublicPropertyBean implements YanxiuBaseBean {
    private int paperNum;

    public int getPaperNum () {
        return paperNum;
    }

    public void setPaperNum (int paperNum) {
        this.paperNum = paperNum;
    }
}
