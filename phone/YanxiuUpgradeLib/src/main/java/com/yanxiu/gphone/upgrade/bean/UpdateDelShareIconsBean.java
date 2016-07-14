package com.yanxiu.gphone.upgrade.bean;

/**
 * Created by hai8108 on 16/4/5.
 */
public class UpdateDelShareIconsBean {
    public final static int INSTALL_APK_CONSTANT = 1;
    public final static int FORCE_UPGRADE_CONSTANT = 2;
    private String shareIconId;

    private int todoType;

    public String getShareIconId () {
        return shareIconId;
    }

    public void setShareIconId (String shareIconId) {
        this.shareIconId = shareIconId;
    }

    public int getTodoType () {
        return todoType;
    }

    public void setTodoType (int todoType) {
        this.todoType = todoType;
    }
}
