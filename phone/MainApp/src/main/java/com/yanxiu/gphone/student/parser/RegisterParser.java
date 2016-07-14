package com.yanxiu.gphone.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.student.bean.RegisterBean;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class RegisterParser extends YanxiuMobileParser<RegisterBean> {
    @Override
    public RegisterBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), RegisterBean.class);
        }
        return null;
    }
}
