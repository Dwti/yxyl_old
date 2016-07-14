package com.yanxiu.gphone.parent.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.parent.bean.ParentHomeSubjectInfoBean;

import org.json.JSONObject;

/**
 * Created by lidongming on 16/3/28.
 * 首页详情界面
 */
public class ParentHomeSubjectInfoParser extends YanxiuMobileParser<ParentHomeSubjectInfoBean> {
    @Override
    public ParentHomeSubjectInfoBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), ParentHomeSubjectInfoBean.class);
        }
        return null;
    }
}
