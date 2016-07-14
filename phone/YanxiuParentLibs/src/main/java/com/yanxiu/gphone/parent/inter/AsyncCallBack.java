package com.yanxiu.gphone.parent.inter;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/6/1.
 */
public interface AsyncCallBack {

        void update(YanxiuBaseBean result);

        void dataError(int type, String msg);
}
