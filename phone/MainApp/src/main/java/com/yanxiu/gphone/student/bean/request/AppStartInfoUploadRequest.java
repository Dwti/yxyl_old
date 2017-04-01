package com.yanxiu.gphone.student.bean.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.bean.AppStartInfo;
import com.yanxiu.gphone.student.httpApi.ExerciseRequestBase;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by sunpeng on 2017/3/30.
 */

public class AppStartInfoUploadRequest extends ExerciseRequestBase {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private List<AppStartInfo> content = new ArrayList<>();

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }

    @Override
    protected String urlServer() {
        return "http://boss.shangruitong.com";
    }

    @Override
    protected String urlPath() {
        return "/logup";
    }

    @Override
    protected RequestBody createRequestBody() {
        RequestBody requestBody;
        String strRequest = gson.toJson(this);
        requestBody = RequestBody.create(JSON,strRequest);
        return requestBody;
    }

    public List<AppStartInfo> getContent() {
        return content;
    }

    public void setContent(List<AppStartInfo> content) {
        this.content = content;
    }
}
