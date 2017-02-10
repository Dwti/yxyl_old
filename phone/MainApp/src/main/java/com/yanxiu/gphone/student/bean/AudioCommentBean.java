package com.yanxiu.gphone.student.bean;

import java.io.Serializable;

/**
 * Created by sp on 17-2-10.
 */

public class AudioCommentBean implements Serializable {
    private String url;
    private int length;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
