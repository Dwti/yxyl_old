package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by sunpeng on 2017/3/30.
 */

public class APPCommonInfo implements YanxiuBaseBean {
    protected String eventID;
    protected String appkey ;
    protected String resID;
    protected String uid;
    protected String clientType ;  //0,IOS;1,Android
    protected String url ;
    protected String timestamp = String.valueOf(System.currentTimeMillis());
    protected String source;   //来源：0，移动端；1，页面
    protected String ip;


    public APPCommonInfo() {
        appkey = "20001";
        resID = "";
        clientType = "1";
        url = "www.yanxiu.com";
        timestamp = String.valueOf(System.currentTimeMillis());
        source="0";
        ip="";
    }

    public String getEventID() {
        return eventID;
    }

    public String getAppkey() {
        return appkey;
    }

    public String getResID() {
        return resID;
    }

    public String getUid() {
        return uid;
    }

    public String getClientType() {
        return clientType;
    }

    public String getUrl() {
        return url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public String getIp() {
        return ip;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
