package com.test.yanxiu.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sp on 17-3-31.
 */

public class UploadManager {
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static UploadManager mInstance;

    public static UploadManager getInstance(){
        if(mInstance == null){
            synchronized (UploadManager.class){
                if(mInstance == null){
                    mInstance = new UploadManager();
                }
            }
        }
        return mInstance;
    }

    public  <T> void uploadSingleFile(String url, String params, File file, final HttpCallback<T> httpCallback, final Class<T> clazz) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody formBody = builder.setType(MultipartBody.FORM)
                .addFormDataPart(params, file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (call.isCanceled()) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        httpCallback.onFail(null, new Error(e.getLocalizedMessage()));
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (call.isCanceled()) {
                    return;
                }
                final String retStr = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!response.isSuccessful()) {
                            httpCallback.onFail(null, new Error(response.code() + " : " + response.message()));
                            return;
                        }
                        T ret;
                        try {
                            ret = RequestBase.gson.fromJson(retStr, clazz);
                        } catch (Exception e) {
                            e.printStackTrace();
                            httpCallback.onFail(null, new Error(e.getLocalizedMessage()));
                            return;
                        }
                        httpCallback.onSuccess(null, ret);

                    }
                });
            }
        });
    }
}