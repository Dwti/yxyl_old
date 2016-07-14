package com.yanxiu.gphone.parent.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.parent.bean.ParentHomeDetailBean;

import org.json.JSONObject;

/**
 * Created by lidongming on 16/3/24.
 */
public class ParentHomeParser extends YanxiuMobileParser<ParentHomeDetailBean> {
    @Override
    public ParentHomeDetailBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), ParentHomeDetailBean.class);
        }
        return null;
    }
}
