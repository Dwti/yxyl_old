package com.yanxiu.gphone.studentold.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.ImageView;

//import com.yanxiu.gphone.parent.eventbus.bean.ParentJumpToLoginChoiceEventBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.YanxiuApplication;
import com.yanxiu.gphone.studentold.view.LoginChoiceView;

/**
 * Created by libo on 16/3/17.
 */
public class LoginChoiceActivity extends YanxiuJumpBaseActivity {
    private LoginChoiceView mLoginChoiceView;
    private ImageView mStartTabLogo;
    private final float Y_DEL=480f;
    private final int LOGO_DUR_TIME=1000;
    private final int LOGIN_DUR_TIME=500;
    private final static String TAG=LoginChoiceActivity.class.getSimpleName();
    private Handler mHandler=new Handler();

    @Override
    protected void initLaunchIntentData() {
            
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EventBus.getDefault().register(this);
        setContentView(R.layout.login_choice_layout);
        initView();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startLogoAnim();
            }
        },200);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitProcess();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void exitProcess() {
        finish();
        YanxiuApplication.getInstance().exitProcess();
    }


    private void startLogoAnim() {
        ObjectAnimator valueAnimator=ObjectAnimator.ofFloat(mStartTabLogo,"",1.1f,0f);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                startLoginAnim();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alpa = (float) valueAnimator.getAnimatedValue();
                mStartTabLogo.setAlpha(alpa);
            }
        });

        valueAnimator.setDuration(LOGO_DUR_TIME);
        valueAnimator.start();
    }

    private void initView() {
        mStartTabLogo=(ImageView)findViewById(R.id.startTabLogo);
        mLoginChoiceView= (LoginChoiceView) findViewById(R.id.loginChoiceView);
        mLoginChoiceView.setTranslationY(Y_DEL);
    }

    private void startLoginAnim() {

        ValueAnimator valueAnimator=ValueAnimator.ofFloat(Y_DEL,0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float delY=(float)animation.getAnimatedValue();
                mLoginChoiceView.setTranslationY(delY);
            }
        });
        valueAnimator.setDuration(LOGIN_DUR_TIME);
        valueAnimator.start();
    }
    /*public void onEventMainThread(ParentJumpToLoginChoiceEventBean eventBean){
        LogInfo.log("geny", "LoginChoiceActivity-------onEventMainThread");
       if(eventBean==null){
           return;
       }
        ActivityManager.destoryExcepLoginChoiceActivity();

    }*/

}
