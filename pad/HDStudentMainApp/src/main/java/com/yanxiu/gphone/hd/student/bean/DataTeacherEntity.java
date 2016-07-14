package com.yanxiu.gphone.hd.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/16.
 */
public class DataTeacherEntity implements YanxiuBaseBean {
    /**
     * data : {"wrongNum":5}
     * children : null
     * name : 第一节 小明被打，求面积
     * id : 23
     */
    /**
     * data : {"wrongNum":10}
     * children : [{"data":{"wrongNum":5},"children":null,"name":"第一节 小明被打，求面积","id":23},{"data":{"wrongNum":5},"children":null,"name":"第二节 统计小明被打次数","id":14}]
     * name : 第一章 微分求小明心里阴影面积
     * id : 12
     */
    private String id;
    private String name;
    private boolean isExpanded;
    private DataWrongNumEntity data;
    private ArrayList<DataTeacherEntity> children;
    private String volumeId;
    private String volumeName;

    @Override
    public String toString() {
        return "DataTeacherEntity{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setIsExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public void setData(DataWrongNumEntity data) {
        this.data = data;
    }

    public void setChildren(ArrayList<DataTeacherEntity> children) {
        this.children = children;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataWrongNumEntity getData() {
        return data;
    }

    public ArrayList<DataTeacherEntity> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
