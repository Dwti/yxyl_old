package com.yanxiu.gphone.hd.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.hd.student.bean.UploadImageBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class UploadImageParser extends YanxiuMobileParser<UploadImageBean> {
    @Override
    public UploadImageBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), UploadImageBean.class);
        }
        return null;
    }
}
