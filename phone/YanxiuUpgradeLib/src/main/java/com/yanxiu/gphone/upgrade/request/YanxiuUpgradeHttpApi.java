package com.yanxiu.gphone.upgrade.request;

import android.os.Bundle;
import android.text.TextUtils;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.impl.YanxiuHttpBaseParameter;
import com.yanxiu.basecore.impl.YanxiuHttpParameter;
import com.yanxiu.basecore.impl.YanxiuHttpTool;
import com.yanxiu.basecore.parse.YanxiuMainParser;
import com.yanxiu.gphone.upgrade.utils.UpgradeConstant;

/**
 * Created by hai8108 on 16/3/28.
 */
public class YanxiuUpgradeHttpApi {
    public interface OFFICIAL_URL {
        String DYNAMIC_APP_UPLOAD = "http://mobile.yanxiu.com/api";
    }

    /**
     * 根据参数，调起请求
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> request (
            YanxiuHttpBaseParameter<T, D, ?> httpParameter) {
        YanxiuHttpTool<T> handler = new YanxiuHttpTool<T>();
        return handler.requsetData(httpParameter);
    }

    public static String getInitUrl () {
        return OFFICIAL_URL.DYNAMIC_APP_UPLOAD;
    }

    /**
     * 开机接口
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestInitialize (int
                                                                                             updataId, String content, String token,
                                                                                     String mobile,
                                                                                     YanxiuMainParser<T, D> parser) {

        String baseUrl = getInitUrl() + "/initialize";

        Bundle params = new Bundle();

        params.putString("did", UpgradeConstant.getDEVICEID());
        params.putString("brand", UpgradeConstant.BRAND);
        params.putString("nettype", NetWorkTypeUtils.getNetWorkType());
        params.putString("osType", UpgradeConstant.OS_TYPE);
        params.putString("os", UpgradeConstant.OS);
        params.putString("token", token);
        params.putString("appVersion", UpgradeConstant.VERSION);
        params.putString("content", content);
        params.putString("mode", UpgradeConstant.isForTest() ? "test" : "release");
        params.putString("operType", UpgradeConstant.OPERTYPE);
        params.putString("phone", TextUtils.isEmpty(mobile) ? "-" : mobile);
        params.putString("remoteIp", "");
        params.putString("productLine", UpgradeConstant.PRODUCTLINE + "");
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl,
                params, YanxiuHttpBaseParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "开机接口 = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }
}
