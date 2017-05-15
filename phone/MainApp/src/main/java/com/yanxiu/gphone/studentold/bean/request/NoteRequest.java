package com.yanxiu.gphone.studentold.bean.request;

import com.yanxiu.gphone.studentold.httpApi.ExerciseRequestBase;
import com.yanxiu.gphone.studentold.bean.NoteBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;

/**
 * Created by sunpeng on 2017/3/13.
 */

public class NoteRequest extends ExerciseRequestBase {
    private String token;
    private String wqid;
    private NoteBean note;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWqid() {
        return wqid;
    }

    public void setWqid(String wqid) {
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
