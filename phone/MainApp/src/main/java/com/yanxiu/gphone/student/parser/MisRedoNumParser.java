package com.yanxiu.gphone.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.student.bean.MistakeRedoNumberBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;

import org.json.JSONObject;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/13 14:45.
 * Function :
 */

public class MisRedoNumParser extends YanxiuMobileParser<MistakeRedoNumberBean> {
    @Override
    public MistakeRedoNumberBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), MistakeRedoNumberBean.class);
        }
        return null;
    }
}
