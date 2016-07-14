package com.yanxiu.gphone.hd.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.hd.student.bean.ExamListInfo;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/17.
 */
public class RequestExamInfoParser extends YanxiuMobileParser<ExamListInfo> {
    @Override
    public ExamListInfo parse(JSONObject data) throws Exception {
        if(data!=null){
            return JSON.parseObject(data.toString(), ExamListInfo.class);
        }
        return null;
    }
}
