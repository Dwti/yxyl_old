package com.common.login.parser;

import com.alibaba.fastjson.JSON;
import com.common.login.model.ParentInfoBean;
import com.yanxiu.basecore.parse.YanxiuMobileParser;

import org.json.JSONObject;

/**
 * Created by lee on 16-3-28.
 */
public class ParentLoginParser extends YanxiuMobileParser<ParentInfoBean> {

    @Override
    public ParentInfoBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), ParentInfoBean.class);
        }
        return null;
    }
}
