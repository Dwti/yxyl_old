package com.yanxiu.gphone.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.student.bean.QQUserInfoBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class QQUserInfoBeanParser extends YanxiuMobileParser<QQUserInfoBean> {
    @Override
    public QQUserInfoBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), QQUserInfoBean.class);
        }
        return null;
    }
}
