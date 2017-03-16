package com.yanxiu.gphone.student.bean.statistics;

import android.os.Build;

import com.common.core.utils.NetWorkTypeUtils;
import com.common.login.LoginModel;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

import java.util.HashMap;

/**
 * Created by JS-00 on 2016/6/16.
 */
public class StatisticHashMap extends HashMap<String, String> {
    public StatisticHashMap() {
        put(YanXiuConstant.uid, String.valueOf(LoginModel.getUid()));
        put(YanXiuConstant.appkey, "20001");
        put(YanXiuConstant.timestamp, String.valueOf(System.currentTimeMillis()));
        put(YanXiuConstant.source, String.valueOf(0));// 来源：source（0，移动端，1，页面）
        put(YanXiuConstant.clientType, String.valueOf(1));// 客户端类型：client（0，iOS，1，android）
        put(YanXiuConstant.ip, "");
        put(YanXiuConstant.url, "www.yanxiu.com");
        put(YanXiuConstant.resID, "");

        put(YanXiuConstant.mobileModel, Build.MODEL);   //手机型号
        put(YanXiuConstant.brand,Build.MANUFACTURER);         //手机品牌
        put(YanXiuConstant.system,Build.VERSION.RELEASE);        //手机系统
        put(YanXiuConstant.resolution,YanXiuConstant.displayMetrics.heightPixels + " * " +YanXiuConstant.displayMetrics.widthPixels);    //屏幕分辨率
        put(YanXiuConstant.netModel, NetWorkTypeUtils.getNetType());      //连网类型

    }
}
