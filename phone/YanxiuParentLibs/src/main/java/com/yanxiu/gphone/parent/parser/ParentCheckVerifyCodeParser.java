package com.yanxiu.gphone.parent.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.parent.bean.ParentVerifyBean;

import org.json.JSONObject;

/**
 * Created by lee on 16-3-28.
 */
public class ParentCheckVerifyCodeParser extends YanxiuMobileParser<ParentVerifyBean> {
    @Override
    public ParentVerifyBean parse(JSONObject data) throws Exception {
        if(data!=null){
            return JSON.parseObject(data.toString(), ParentVerifyBean.class);
        }
        return null;
    }
}
