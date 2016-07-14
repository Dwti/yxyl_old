package com.yanxiu.gphone.parent.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.parent.bean.ParentRequestBean;

import org.json.JSONObject;

/**
 * Created by lidongming on 16/3/24.
 */
public class ParentRequestParser extends YanxiuMobileParser<ParentRequestBean> {
    @Override
    public ParentRequestBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), ParentRequestBean.class);
        }
        return null;
    }
}
