package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.fragment.question.AnswerCardFragment;
import com.common.core.utils.TimeUtils;

/**
 * Created by Administrator on 2015/7/6.
 * 答题卡界面
 */
public class
AnswerCardActivity extends YanxiuBaseActivity implements View.OnClickListener{
    public final static int LAUNCHER_CARD_INDEX_ACTIVITY = 0x11;
    private ImageView ivBack;
    private TextView tvToptext;
    private int totalTime;
    private SubjectExercisesItemBean dataSources;

    public static void launch(Activity context, SubjectExercisesItemBean bean, int totalTime) {
        Intent intent = new Intent(context, AnswerCardActivity.class);
        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("totalTime", totalTime);
        context.startActivityForResult(intent, LAUNCHER_CARD_INDEX_ACTIVITY);

    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_card);
        initView();
        initData();
    }

    private void initView(){
        ivBack = (ImageView) findViewById(R.id.iv_top_back);
        tvToptext = (TextView) findViewById(R.id.tv_top_title);


        ivBack.setOnClickListener(this);
    }

    private void initData(){
        dataSources =  (SubjectExercisesItemBean) getIntent().getSerializableExtra("subjectExercisesItemBean");
        totalTime = getIntent().getIntExtra("totalTime", 0);
        tvToptext.setText(TimeUtils.stringForTimeNoHour(totalTime));

        Bundle args = new Bundle();
        args.putSerializable("subjectExercisesItemBean", dataSources);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_answer_card, Fragment.instantiate(getBaseContext(), AnswerCardFragment.class.getName(), args)).commitAllowingStateLoss();

    }


    @Override
    public void onClick(View v) {
        if(v == ivBack){
            this.finish();
        }
    }

}
