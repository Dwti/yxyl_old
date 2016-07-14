package com.yanxiu.gphone.parent.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.parent.bean.ParentWeekReportBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class ParentWeekReportBeanParser extends YanxiuMobileParser<ParentWeekReportBean> {
    @Override
    public ParentWeekReportBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), ParentWeekReportBean.class);
        }
        return null;
    }
}
