package com.yanxiu.gphone.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.student.bean.ExercisesBean;
import com.yanxiu.gphone.student.bean.SubjectEditionBean;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class SubjectEditionParser extends YanxiuMobileParser<SubjectEditionBean> {
    @Override
    public SubjectEditionBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), SubjectEditionBean.class);
        }
        return null;
    }
}
