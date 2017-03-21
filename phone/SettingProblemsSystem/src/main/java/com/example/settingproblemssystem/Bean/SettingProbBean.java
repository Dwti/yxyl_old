package com.example.settingproblemssystem.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/21 12:02.
 * Function :
 */

public class SettingProbBean extends BaseBean{

    private String Qname;
    private int QType;
    private List<SettingProbBean> childs=new ArrayList<>();

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

    public List<SettingProbBean> getChilds() {
        return childs;
    }

    public void setChilds(List<SettingProbBean> childs) {
        this.childs = childs;
    }
}
