package com.yanxiu.gphone.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.student.bean.MistakeDoWorkBean;
import com.yanxiu.gphone.student.bean.MistakeRedoCardBean;

import org.json.JSONObject;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/14 16:01.
 * Function :
 */

public class MisReDoworkParser extends YanxiuMobileParser<MistakeDoWorkBean> {
    @Override
    public MistakeDoWorkBean parse(JSONObject data) throws Exception {
        if (data!=null){
            return JSON.parseObject(data.toString(),MistakeDoWorkBean.class);
        }
        return null;
    }
}
