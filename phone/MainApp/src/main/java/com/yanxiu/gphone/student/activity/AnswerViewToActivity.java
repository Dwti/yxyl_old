package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.preference.PreferencesManager;
import com.yanxiu.gphone.student.view.CommonDialog;
import com.yanxiu.gphone.student.view.DelDialog;
import com.yanxiu.gphone.student.view.question.GuideQuestionView;

import java.lang.reflect.Field;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2015/7/6.
 */
public class AnswerViewToActivity extends YanxiuBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{


    private SubjectExercisesItemBean dataSources;

    private AnswerAdapter adapter;

    private ImageView ivBack;
    private ViewPager vpAnswer;
    private TextView tvToptext;
    private TextView tvPagerIndex;
    private TextView tvAnswerCard;

    private boolean isResolution;



    private FrameLayout decorView;

    private int lastTime;
    private int currentTime;

    private int pageCount;

    private int totalTime;

    private CommonDialog dialog;

    private TextView tvQuestionTitle;

    private int viewPagerLastPosition;
    /**
     * 刷新进度
     */
    private final int HANDLER_TIME = 0x100;
    private final int HANDLER_TIME_GESTURE = 0x101;
    /**
     * 一秒
     */
    private final int HANDLER_TIME_DELAYED = 1000;


    private GuideQuestionView mGuideQuestionView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_TIME:
                    handler.sendEmptyMessageDelayed(HANDLER_TIME, HANDLER_TIME_DELAYED);
                    updateProgress();
                    break;
                case HANDLER_TIME_GESTURE:
                    break;
                case 1:
                    break;
            }
        }
    };

    public static void launch(Activity context, SubjectExercisesItemBean bean) {
        Intent intent = new Intent(context, AnswerViewToActivity.class);
        intent.putExtra("subjectExercisesItemBean", bean);
        context.startActivity(intent);
    }

//    public static void launch(Activity context, SubjectExercisesItemBean bean, boolean isResolution) {
//        Intent intent = new Intent(context, AnswerViewActivity.class);
//        intent.putExtra("subjectExercisesItemBean", bean);
//        intent.putExtra("isResolution", isResolution);
//        context.startActivity(intent);
//    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
        initView();
        initData();
    }

    /**
     * 时间转换分：秒
     */
    public String stringForTimeNoHour(int timeMs) {

        StringBuilder formatBuilder = new StringBuilder();
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());

        try {
            int totalSeconds = timeMs;

            int seconds = totalSeconds % 60;
            int minutes = totalSeconds / 60;

            formatBuilder.setLength(0);

            return formatter.format("%02d:%02d", minutes, seconds).toString();
        } finally {
            formatter.close();
        }
    }

    public void updateProgress() {
        tvToptext.setText(stringForTimeNoHour(++totalTime));
    }


    private void initView(){
        ivBack = (ImageView) findViewById(R.id.iv_top_back);
        tvToptext = (TextView) findViewById(R.id.tv_top_title);
        tvAnswerCard = (TextView) findViewById(R.id.tv_answer_card);
        vpAnswer = (ViewPager) findViewById(R.id.answer_viewpager);
        tvPagerIndex = (TextView) findViewById(R.id.tv_pager_index);
        //=============================================
        //反射viewPager里面的mScroller
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            FixedSpeedScroller mScroller = new FixedSpeedScroller(vpAnswer.getContext(),new AccelerateInterpolator());
            mField.set(vpAnswer, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //=============================================
        vpAnswer.setOnPageChangeListener(this);
        tvQuestionTitle = (TextView) this.findViewById(R.id.tv_anwser_tips);
        adapter = new AnswerAdapter(getSupportFragmentManager());

    }

    private void initData(){
        dataSources =  (SubjectExercisesItemBean) getIntent().getSerializableExtra("subjectExercisesItemBean");
        if(dataSources == null || dataSources.getData() == null || dataSources.getData().get(0) == null){
            this.finish();
        }else{
            long begintime = System.currentTimeMillis();
            dataSources.setBegintime(begintime);
            List<PaperTestEntity> dataList = dataSources.getData().get(0).getPaperTest();
            if(dataList != null && !dataList.isEmpty()){
                adapter.addDataSources(dataSources);
                vpAnswer.setAdapter(adapter);
                adapter.setViewPager(vpAnswer);
                pageCount = adapter.getCount();

                ivBack.setOnClickListener(this);
                tvAnswerCard.setOnClickListener(this);

                tvQuestionTitle.setText(dataSources.getData().get(0).getName());


                if(isResolution){
                    tvPagerIndex.setText("1/" + pageCount);
                    tvToptext.setText(this.getResources().getString(R.string.questiong_resolution));
                    tvToptext.setCompoundDrawables(null, null, null, null);
                    tvAnswerCard.setVisibility(View.GONE);
                }else{
                    tvToptext.setText(stringForTimeNoHour(0));
                    addTimeHandler();
                    tvPagerIndex.setText("1/" + (pageCount - 1));
                    if(PreferencesManager.getInstance().getFirstQuestion()){
                        decorView = (FrameLayout) this.findViewById(R.id.fl_decor_view);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                        mGuideQuestionView = new GuideQuestionView(this);
                        decorView.addView(mGuideQuestionView, params);
                        PreferencesManager.getInstance().setFirstQuestion();
                    }
                }

            }
        }
    }


    private void quitSubmmitDialog(){
        dialog = new CommonDialog(this,this.getResources().getString(R.string.question_no_finish),
                this.getResources().getString(R.string.question_submit),
                this.getResources().getString(R.string.question_cancel),
                new DelDialog.DelCallBack(){
                    @Override
                    public void del() {
                        //2
                        AnswerViewToActivity.this.finish();
                    }

                    @Override
                    public void sure() {
                        //1
//                        AnswerViewActivity.this.finish();
                    }

                    @Override
                    public void cancel() {
                        //3
                    }
                });
        dialog.show();
    }

    private void addTimeHandler(){
        handler.removeMessages(HANDLER_TIME);
        handler.sendEmptyMessageDelayed(HANDLER_TIME, HANDLER_TIME_DELAYED);
    }


    @Override
    public void onBackPressed() {
        if(mGuideQuestionView!=null && mGuideQuestionView.isShown()){
            mGuideQuestionView.setVisibility(View.GONE);
        }else{
            quitSubmmitDialog();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == ivBack){
            quitSubmmitDialog();
        }else if (v == tvAnswerCard) {
            AnswerCardActivity.launch(this, dataSources, totalTime);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LogInfo.log("geny", "position" + position + "----positionOffset" + positionOffset + "----positionOffsetPixels" + positionOffsetPixels);
    }

    public void setViewPagerPosition(int position){
        vpAnswer.setCurrentItem(position);
    }


    @Override
    public void onPageSelected(int position) {
        if(position == pageCount - 1){
            tvPagerIndex.setVisibility(View.GONE);
        }else{
            tvPagerIndex.setVisibility(View.VISIBLE);
            if(isResolution){
                tvPagerIndex.setText((position + 1) + "/" + pageCount);
            }else{
                tvPagerIndex.setText((position + 1) + "/" + (pageCount - 1));
            }

            currentTime = totalTime;
            int costTime = currentTime - lastTime;
            lastTime = currentTime;
            adapter.setCostTime(costTime, viewPagerLastPosition);
            viewPagerLastPosition = position;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 200;

        public FixedSpeedScroller(Context context) {
            super(context);
        }
        public FixedSpeedScroller(Context context,Interpolator interpolator) {
            super(context,interpolator);
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

    }

    public void selectViewPager(){
        vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() + 1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        LogInfo.log("king", "resultCode = " + resultCode + " ,requestCode = " + requestCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AnswerCardActivity.LAUNCHER_CARD_INDEX_ACTIVITY:
                    Bundle bundle = data.getBundleExtra("data");
                    int position = bundle.getInt("position", 0);
                    setViewPagerPosition(position);
                    break;
            }
        }
    }
}
