package com.example.settingproblemssystem.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/21 12:02.
 * Function :
 */

public class SettingProbBean {

    private String Qname;
    private int QType;
    private List<String> childs=new ArrayList<>();

    public String getQname() {
        return Qname;
    }

    public void setQname(String qname) {
        Qname = qname;
    }

    public int getQType() {
        return QType;
    }

    public void setQType(int QType) {
        this.QType = QType;
    }

    public List<String> getChilds() {
        return childs;
    }

    public void setChilds(List<String> childs) {
        this.childs = childs;
    }
}
