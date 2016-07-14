package com.yanxiu.gphone.student.push;


import com.tencent.android.tpush.XGPushNotificationBuilder;

/**
 * Created by Administrator on 2016/1/19.
 */
public interface PushManagerInter {
     void initXGPush();
     void setPushNotifyStyle(XGPushNotificationBuilder builder);
}
