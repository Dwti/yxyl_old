package com.yanxiu.gphone.student.parser;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.bean.SubjectVersionBean;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class SubjectExerisesItemParser extends YanxiuMobileParser<SubjectExercisesItemBean> {
    @Override
    public SubjectExercisesItemBean parse(JSONObject data) throws Exception {
        if(data != null ){
            Log.d("asd",data.toString());
            return JSON.parseObject(data.toString(), SubjectExercisesItemBean.class);
        }
        return null;
    }
}
