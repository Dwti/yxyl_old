package com.yanxiu.gphone.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.student.bean.ChapterListEntity;
import com.yanxiu.gphone.student.bean.SubjectVersionBean;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class ChapterListParser extends YanxiuMobileParser<ChapterListEntity> {
    @Override
    public ChapterListEntity parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), ChapterListEntity.class);
        }
        return null;
    }
}
