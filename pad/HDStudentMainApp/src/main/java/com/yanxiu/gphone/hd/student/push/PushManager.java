package com.yanxiu.gphone.hd.student.push;

import android.app.Notification;
import android.media.RingtoneManager;
import android.util.Log;

import com.common.login.LoginModel;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotificationBuilder;
import com.tencent.android.tpush.common.Constants;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.YanxiuApplication;

/**
 * Created by Administrator on 2016/1/19.
 */
public class PushManager implements  PushManagerInter {
    @Override
    public void initXGPush() {
        XGPushConfig.enableDebug(YanxiuApplication.getInstance().getApplicationContext(), true);
        // 0.注册数据更新监听器
//        updatePushReceiver = new YanxiuPushUpdateReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(YanxiuPushUpdateReceiver.PUSH_RECEIVER_INTENT_ACTION);
//        registerReceiver(updatePushReceiver, intentFilter);
        // 1.获取设备Token
        // 注册接口
        XGPushManager.registerPush(YanxiuApplication.getInstance(),
                LoginModel.getUid() + "",
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        Log.w(Constants.LogTag,
                                "+++ register push sucess. token:" + data);
                    }

                    @Override
                    public void onFail(Object data, int errCode,
                                       String msg) {
                        Log.w(Constants.LogTag,
                                "+++ register push fail. token:" + data
                                        + ", errCode:" + errCode + ",msg:"
                                        + msg);
                    }
                });

    }

    /**
     * 设置通知自定义View，这样在下发通知时可以指定build_id。编号由开发者自己维护,build_id=0为默认设置
     */
    @Override
    public void setPushNotifyStyle(XGPushNotificationBuilder sBuilder) {
        XGCustomPushNotificationBuilder builder;
        if(sBuilder==null){
            builder = new XGCustomPushNotificationBuilder();
            builder.setSound(
                    RingtoneManager.getActualDefaultRingtoneUri(
                            YanxiuApplication.getInstance().getApplicationContext(), RingtoneManager.TYPE_ALARM)) // 设置声音
                    // setSound(
                    // Uri.parse("android.resource://" + getPackageName()
                    // + "/" + R.raw.wind)) 设定Raw下指定声音文件
                    .setDefaults(Notification.DEFAULT_VIBRATE) // 振动
                    .setFlags(Notification.FLAG_NO_CLEAR); // 是否可清除
            // 设置自定义通知layout,通知背景等可以在layout里设置
            builder.setLayoutId(R.layout.yanxiu_notification_layout);
            // 设置自定义通知内容id
            builder.setLayoutTextId(R.id.notifi_content);
            // 设置自定义通知标题id
            builder.setLayoutTitleId(R.id.notifi_title);
            // 设置自定义通知图片id
            builder.setLayoutIconId(R.id.notifi_icon);
            // 设置自定义通知图片资源
            builder.setLayoutIconDrawableId(R.drawable.logo);
            // 设置状态栏的通知小图标
            builder.setIcon(R.drawable.logo);

            // 设置时间id
            builder.setLayoutTimeId(R.id.notifi_time);
        }else{
            builder= (XGCustomPushNotificationBuilder) sBuilder;
        }
        XGPushManager.setDefaultNotificationBuilder(YanxiuApplication.getInstance().getApplicationContext(), builder);
    }


}
