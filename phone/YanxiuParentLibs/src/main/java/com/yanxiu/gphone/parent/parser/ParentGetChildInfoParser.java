package com.yanxiu.gphone.parent.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.parent.bean.ParentGetChildInfoBean;

import org.json.JSONObject;

/**
 * Created by lee on 16-3-29.
 */
public class ParentGetChildInfoParser extends YanxiuMobileParser<ParentGetChildInfoBean> {
    @Override
    public ParentGetChildInfoBean parse(JSONObject data) throws Exception {
        if(data!=null){
            return JSON.parseObject(data.toString(), ParentGetChildInfoBean.class);
        }
        return null;
    }
}
