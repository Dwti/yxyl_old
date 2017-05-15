package com.yanxiu.gphone.studentold.httpApi;

import com.test.yanxiu.network.RequestBase;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Iterator;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by sunpeng on 2017/3/15.
 */

public abstract class ExerciseRequestBase extends RequestBase {

    protected String fullUrl() throws NullPointerException, IllegalAccessException, IllegalArgumentException {
        String server = urlServer();
        String path = urlPath();

        if (server == null) {
            throw new NullPointerException();
        }

        server = omitSlash(server);
        path = omitSlash(path);

        if (!urlServer().substring(0, 4).equals("http")) {
            server = "http://" + urlServer();
        }

        String fullUrl = server;
        if (path != null) {
            fullUrl = fullUrl + "/" + path;
        }

        return fullUrl;
    }

    protected Request generateRequest(UUID uuid) throws IllegalAccessException {
        Request request ;
        Request.Builder builder = new Request.Builder()
                .tag(uuid);
        if(httpType() == HttpType.GET){
            HttpUrl.Builder urlBuilder = createHttpUrlBuilder();
            request = builder.url(urlBuilder.build()).get().build();
        }else if(httpType() == HttpType.POST){
            RequestBody requestBody = createRequestBody();
            request = builder.url(fullUrl()).post(requestBody).build();
        }else {
            request = null;
        }
        return request;
    }

    private HttpUrl.Builder createHttpUrlBuilder() throws IllegalAccessException {
        HttpUrl.Builder builder = HttpUrl.parse(fullUrl()).newBuilder();
        String strRequest = gson.toJson(this);
        JSONTokener jsonTokener = new JSONTokener(strRequest);
        JSONObject jsonObject ;
        try {
            jsonObject = (JSONObject) jsonTokener.nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.addEncodedQueryParameter(key, jsonObject.optString(key));
        }
        return builder;
    }

    protected RequestBody createRequestBody() {
        RequestBody requestBody;
        String strRequest = gson.toJson(this);
        FormBody.Builder formBody = new FormBody.Builder();
        JSONTokener jsonTokener = new JSONTokener(strRequest);
        JSONObject jsonObject ;
        try {
            jsonObject = (JSONObject) jsonTokener.nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            formBody.add(key, jsonObject.optString(key));
        }
        requestBody = formBody.build();
        return requestBody;
    }
}
