package com.yanxiu.gphone.studentold.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.studentold.bean.MistakeAllFragmentBean;

import org.json.JSONObject;

/**
 * Created by Canghaixiao.
 * Time : 2017/4/10 14:32.
 * Function :
 */

public class MistakeFragmentBeanParser extends YanxiuMobileParser<YanxiuBaseBean> {
    @Override
    public YanxiuBaseBean parse(JSONObject data) throws Exception {
        if (data!=null){
            return JSON.parseObject(data.toString(), MistakeAllFragmentBean.class);
        }
        return null;
    }
}
