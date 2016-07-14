package com.yanxiu.gphone.parent.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.parent.bean.ParentSetPasswordBean;

import org.json.JSONObject;

/**
 * Created by lee on 16-3-30.
 */
public class ParentSetPasswordParser extends YanxiuMobileParser<ParentSetPasswordBean> {
    @Override
    public ParentSetPasswordBean parse(JSONObject data) throws Exception {
        if(data!=null){
            return JSON.parseObject(data.toString(), ParentSetPasswordBean.class);
        }
        return null;
    }
}
