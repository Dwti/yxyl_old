package com.yanxiu.gphone.student.manager;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.common.core.utils.FileUtils;
import com.common.login.LoginModel;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.test.yanxiu.network.UploadManager;
import com.yanxiu.gphone.student.bean.AppStartInfo;
import com.yanxiu.gphone.student.bean.AppStartInfoResponse;
import com.yanxiu.gphone.student.bean.AppStartPointFileUploadResponse;
import com.yanxiu.gphone.student.bean.request.AppStartInfoUploadRequest;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/3/30.
 */

public class AppStartPointManager {
    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + YanXiuConstant.ROOT_DIR + "/temp/";
    private static AppStartPointManager mInstance;
    private File mTempFile;

    public static AppStartPointManager getInstance() {
        if (mInstance == null) {
            synchronized (AppStartPointManager.class) {
                if (mInstance == null) {
                    mInstance = new AppStartPointManager();
                }
            }
        }
        return mInstance;
    }

    public void uploadStartInfo() {
        queryAndUploadData();

        AppStartInfoUploadRequest request = new AppStartInfoUploadRequest();
        List<AppStartInfo> list = new ArrayList<>();
        AppStartInfo startInfo = new AppStartInfo();
        startInfo.setEventID("20:event_7");  //首次启动
        startInfo.setUid(String.valueOf(LoginModel.getUid()));
        list.add(startInfo);
        request.setContent(list);
        request.startRequest(AppStartInfoResponse.class, new AppStartPointCallBack());

        startInfo.save();
    }

    public void uploadStartInfoFirstInstall() {
        AppStartInfoUploadRequest request = new AppStartInfoUploadRequest();
        List<AppStartInfo> list = new ArrayList<>();
        AppStartInfo startInfo = new AppStartInfo();
        startInfo.setEventID("20:event_10");  //首次安装之后启动
        startInfo.setUid(String.valueOf(LoginModel.getUid()));
        list.add(startInfo);
        request.setContent(list);
        request.startRequest(AppStartInfoResponse.class, new AppStartPointCallBack());

        startInfo.save();
    }

    public <T> void uploadStartInfoFile(String url, String params, File file, HttpCallback<T> httpCallback, Class<T> clazz) {
        UploadManager.getInstance().uploadSingleFile(url, params, file, httpCallback, clazz);
    }

    public void queryAndUploadData() {
        List<AppStartInfo> datas = DataSupport.findAll(AppStartInfo.class);
        StringBuilder sb = new StringBuilder();
        for (AppStartInfo info : datas) {
            sb.append(info.toString());
            sb.append("\r\n");
        }
        String content = sb.toString();
        if (TextUtils.isEmpty(content))
            return;
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = PATH + System.currentTimeMillis() + ".txt";
        File file = FileUtils.writeStringToFile(filePath, content);
        mTempFile = file;
        if (file == null)
            return;
        String url = "http://boss.shangruitong.com/upfile";
        uploadStartInfoFile(url, "appStartPointFile", file, new AppStartPointFileUploadCallBack(), AppStartPointFileUploadResponse.class);
    }

    private class AppStartPointCallBack implements HttpCallback<AppStartInfoResponse> {

        @Override
        public void onSuccess(RequestBase request, AppStartInfoResponse response) {
        }

        @Override
        public void onFail(RequestBase request, Error error) {
        }
    }

    private class AppStartPointFileUploadCallBack implements HttpCallback<AppStartPointFileUploadResponse> {

        @Override
        public void onSuccess(RequestBase request, AppStartPointFileUploadResponse response) {
            if (mTempFile != null && mTempFile.exists())
                mTempFile.delete();
            if (response != null && "ok".equals(response.getResult()))
                DataSupport.deleteAll(AppStartInfo.class);
        }

        @Override
        public void onFail(RequestBase request, Error error) {
        }
    }
}
