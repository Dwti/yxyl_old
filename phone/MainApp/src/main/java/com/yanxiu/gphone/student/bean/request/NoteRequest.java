package com.yanxiu.gphone.student.bean.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.bean.NoteBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;

/**
 * Created by sunpeng on 2017/3/13.
 */

public class NoteRequest extends RequestBase {
    private String token;
    private int wqid;
    private NoteBean note;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getWqid() {
        return wqid;
    }

    public void setWqid(int wqid) {
        this.wqid = wqid;
    }

    public NoteBean getNote() {
        return note;
    }

    public void setNote(NoteBean note) {
        this.note = note;
    }

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return YanxiuHttpApi.getPublicUrl();
    }

    @Override
    protected String urlPath() {
        return "q/editUserWrongQNote.do";
    }

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }
}
