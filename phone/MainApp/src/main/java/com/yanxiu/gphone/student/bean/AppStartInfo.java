package com.yanxiu.gphone.student.bean;

import com.google.gson.Gson;
import com.yanxiu.basecore.bean.YanxiuBaseBean;

import org.litepal.crud.DataSupport;

/**
 * Created by sunpeng on 2017/3/30.
 */

public class AppStartInfo extends DataSupport {
    private String eventID;
    private String appkey ;
    private String resID;
    private String uid;
    private String clientType ;  //0,IOS;1,Android
    private String url ;
    private String timestamp;
    private String source;   //来源：0，移动端；1，页面
    private String ip;
    private ExtraInfo reserved;

    public AppStartInfo() {
        appkey = "20001";
        resID = "";
        clientType = "1";
        url = "www.yanxiu.com";
        timestamp = String.valueOf(System.currentTimeMillis());
        source="0";
        ip="";
        reserved = new ExtraInfo();
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getResID() {
        return resID;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ExtraInfo getReserved() {
        return reserved;
    }

    public void setReserved(ExtraInfo reserved) {
        this.reserved = reserved;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return  gson.toJson(this);
    }
}
