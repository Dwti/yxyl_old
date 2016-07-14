package com.yanxiu.gphone.hd.student.inter;

/**
 * Created by Administrator on 2016/3/2.
 */
public interface ResetInter {

    //重启通知，需要处理类中的静态对象或者释放资源
    void onReset();
}
