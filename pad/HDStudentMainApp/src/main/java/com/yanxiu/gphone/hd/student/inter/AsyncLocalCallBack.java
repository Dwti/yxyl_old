package com.yanxiu.gphone.hd.student.inter;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/6/1.
 */
public interface AsyncLocalCallBack {

        void updateLocal(YanxiuBaseBean result);

        void update(YanxiuBaseBean result);

        void dataError(int type, String msg);
}
