package com.yanxiu.gphone.student.manager;

import android.util.Log;

import com.common.login.LoginModel;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.bean.AppStartInfo;
import com.yanxiu.gphone.student.bean.AppStartInfoResponse;
import com.yanxiu.gphone.student.bean.request.AppStartInfoUploadRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/3/30.
 */

public class AppStartPointManager {

    private static AppStartPointManager mInstance;
    public static AppStartPointManager getInstance(){
        if(mInstance ==null){
            synchronized (AppStartPointManager.class){
                if(mInstance == null){
                    mInstance = new AppStartPointManager();
                }
            }
        }
        return mInstance;
    }
    public void uploadStartInfo(){
        AppStartInfoUploadRequest request = new AppStartInfoUploadRequest();
        List<AppStartInfo> list = new ArrayList<>();
        AppStartInfo startInfo = new AppStartInfo();
        startInfo.setEventID("20:event_7");  //首次启动
        startInfo.setUid(String.valueOf(LoginModel.getUid()));
        list.add(startInfo);
        request.setContent(list);
        request.startRequest(AppStartInfoResponse.class,new AppStartPointCallBack());
    }

    public void uploadStartInfoFirstInstall(){
        AppStartInfoUploadRequest request = new AppStartInfoUploadRequest();
        List<AppStartInfo> list = new ArrayList<>();
        AppStartInfo startInfo = new AppStartInfo();
        startInfo.setEventID("20:event_10");  //首次安装之后启动
        startInfo.setUid(String.valueOf(LoginModel.getUid()));
        list.add(startInfo);
        request.setContent(list);
        request.startRequest(AppStartInfoResponse.class,new AppStartPointCallBack());
    }

    public void uploadStartInfoFile(){

    }

    private class AppStartPointCallBack implements HttpCallback<AppStartInfoResponse>{

        @Override
        public void onSuccess(RequestBase request, AppStartInfoResponse response) {
            Log.i("start",response.toString());
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            Log.i("start","failed");
        }
    }
}
