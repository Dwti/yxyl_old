package com.yanxiu.gphone.hd.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.hd.student.bean.ClassInfoBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class ClassInfoParser extends YanxiuMobileParser<ClassInfoBean> {
    @Override
    public ClassInfoBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), ClassInfoBean.class);
        }
        return null;
    }
}
