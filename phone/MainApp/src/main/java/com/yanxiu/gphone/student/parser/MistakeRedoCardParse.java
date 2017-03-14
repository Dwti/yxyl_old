package com.yanxiu.gphone.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.student.bean.MistakeRedoCardBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;

import org.json.JSONObject;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/14 10:50.
 * Function :
 */

public class MistakeRedoCardParse extends YanxiuMobileParser<MistakeRedoCardBean> {
    @Override
    public MistakeRedoCardBean parse(JSONObject data) throws Exception {
        if (data!=null){
            return JSON.parseObject(data.toString(),MistakeRedoCardBean.class);
        }
        return null;
    }
}
