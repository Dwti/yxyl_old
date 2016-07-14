package com.yanxiu.gphone.hd.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.hd.student.bean.MistakeEditionBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class MistakeEditionBeanParser extends YanxiuMobileParser<MistakeEditionBean> {
    @Override
    public MistakeEditionBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), MistakeEditionBean.class);
        }
        return null;
    }
}
