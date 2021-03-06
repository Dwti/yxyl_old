package com.yanxiu.gphone.studentold.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.studentold.bean.WXLoginBean;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/9/21.
 */
public class WXLoginParse extends YanxiuMobileParser<WXLoginBean> {
    @Override
    public WXLoginBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), WXLoginBean.class);
        }
        return null;
    }
}

