package com.yanxiu.gphone.hd.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.hd.student.bean.SubjectVersionBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class IntelliExeParser extends YanxiuMobileParser<SubjectVersionBean> {
    @Override
    public SubjectVersionBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), SubjectVersionBean.class);
        }
        return null;
    }
}
