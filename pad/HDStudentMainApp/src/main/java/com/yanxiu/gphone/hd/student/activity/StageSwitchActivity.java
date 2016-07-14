package com.yanxiu.gphone.hd.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.base.YanxiuBaseActivity;

/**
 * Created by hai8108 on 16/2/26.
 */
public class StageSwitchActivity extends YanxiuBaseActivity {
    private Handler mHander = new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    MainActivity.launchActivity(StageSwitchActivity.this);
                    StageSwitchActivity.this.finish();
                    LogInfo.log("haitian", "----StageSwitchActivity finish----");
                    break;
            }
        }
    };
    private ImageView pbLoading;
    private Animation operatingAnim;
    public static void launchActivity(Activity activity) {
        Intent intent = new Intent(activity, StageSwitchActivity.class);
        activity.startActivity(intent);
    }
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_layout);
        pbLoading = (ImageView) findViewById(R.id.pb_loaing);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim
                .xlistview_header_progress);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            pbLoading.startAnimation(operatingAnim);
        }
        mHander.sendEmptyMessageDelayed(0,1500);
    }
}
