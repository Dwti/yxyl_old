package com.yanxiu.gphone.parent.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.parent.bean.ParentChildHonorsBean;

import org.json.JSONObject;

/**
 * Created by lidongming on 16/3/24.
 */
public class ParentChildHonorsParser extends YanxiuMobileParser<ParentChildHonorsBean> {
    @Override
    public ParentChildHonorsBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), ParentChildHonorsBean.class);
        }
        return null;
    }
}
