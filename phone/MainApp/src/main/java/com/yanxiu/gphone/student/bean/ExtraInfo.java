package com.yanxiu.gphone.student.bean;

import android.os.Build;

import com.common.core.utils.NetWorkTypeUtils;
import com.google.gson.Gson;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

/**
 * Created by sunpeng on 2017/3/30.
 */

public class ExtraInfo implements YanxiuBaseBean {
    protected String mobileModel;
    protected String brand;
    protected String system;
    protected String resolution;
    protected String netModel;

    public ExtraInfo() {
        mobileModel = Build.MODEL;
        brand = Build.MANUFACTURER;
        system = Build.VERSION.RELEASE;
        resolution = YanXiuConstant.displayMetrics.heightPixels + " * " +YanXiuConstant.displayMetrics.widthPixels;
        netModel = NetWorkTypeUtils.getNetType();
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
