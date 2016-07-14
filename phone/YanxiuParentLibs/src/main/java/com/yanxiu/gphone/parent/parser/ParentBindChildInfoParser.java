package com.yanxiu.gphone.parent.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.parent.bean.ParentBindChildBean;

import org.json.JSONObject;

/**
 * Created by lee on 16-3-29.
 */
public class ParentBindChildInfoParser extends YanxiuMobileParser<ParentBindChildBean> {
    @Override
    public ParentBindChildBean parse(JSONObject data) throws Exception {
        if(data!=null){
            return JSON.parseObject(data.toString(), ParentBindChildBean.class);
        }
        return null;
    }
}
