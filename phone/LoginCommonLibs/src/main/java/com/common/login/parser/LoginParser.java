package com.common.login.parser;

import com.alibaba.fastjson.JSON;
import com.common.login.model.UserInfoBean;
import com.yanxiu.basecore.parse.YanxiuMobileParser;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class LoginParser extends YanxiuMobileParser<UserInfoBean> {
    @Override
    public UserInfoBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), UserInfoBean.class);
        }
        return null;
    }
}
