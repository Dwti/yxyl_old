package com.yanxiu.gphone.parent.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.parent.bean.ParentHwDetailsBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class ParentHwDetailsParser extends YanxiuMobileParser<ParentHwDetailsBean> {
    @Override
    public ParentHwDetailsBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), ParentHwDetailsBean.class);
        }
        return null;
    }
}
