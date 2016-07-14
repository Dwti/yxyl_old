package com.yanxiu.gphone.hd.student.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.base.YanxiuJumpBaseActivity;
import com.yanxiu.gphone.hd.student.fragment.BaseFragment;
import com.yanxiu.gphone.hd.student.fragment.FeedBackFragment;
import com.yanxiu.gphone.hd.student.jump.FeedbackJumpModel;

/**
 * Created by Administrator on 2016/2/16.
 * 反馈界面壳 套入FeedBackFragment
 */
public class FeedBackActivity extends YanxiuJumpBaseActivity {
    private String questionId;
    private int typeCode;
    private BaseFragment feedBackFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_feedback_layout);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        feedBackFragment = (BaseFragment) FeedBackFragment.newInstance(questionId, typeCode);
        ft.add(R.id.fl_feedback, feedBackFragment).commitAllowingStateLoss();
    }

    @Override
    protected void initLaunchIntentData() {
        FeedbackJumpModel jumpModel = (FeedbackJumpModel) this.getBaseJumpModel();
        if(jumpModel != null){
            questionId = jumpModel.getQuestionId();
            typeCode = jumpModel.getTypeCode();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return feedBackFragment.onKeyDown(keyCode, event);
    }
}
