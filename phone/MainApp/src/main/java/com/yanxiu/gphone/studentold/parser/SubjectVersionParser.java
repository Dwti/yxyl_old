package com.yanxiu.gphone.studentold.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.studentold.bean.SubjectVersionBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class SubjectVersionParser extends YanxiuMobileParser<SubjectVersionBean> {
    @Override
    public SubjectVersionBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), SubjectVersionBean.class);
        }
        return null;
    }
}
