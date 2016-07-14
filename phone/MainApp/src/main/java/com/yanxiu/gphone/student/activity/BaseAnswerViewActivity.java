package com.yanxiu.gphone.student.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.utils.Configuration;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.PublicLoadLayout;
import com.yanxiu.gphone.student.view.StudentLoadingLayout;
import com.yanxiu.gphone.student.view.YanxiuTypefaceTextView;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Administrator on 2015/7/6.
 */
public class BaseAnswerViewActivity extends YanxiuBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{

    private final static String TAG=BaseAnswerViewActivity.class.getSimpleName();
    protected PublicLoadLayout mRootView;
    protected SubjectExercisesItemBean dataSources;

    protected AnswerAdapter adapter;

    protected ImageView ivBack;
    protected ViewPager vpAnswer;
    protected YanxiuTypefaceTextView tvToptext;
    protected TextView tvPagerIndex;
    protected TextView tvPagerCount;
    protected ImageView ivAnswerCard;
    protected ImageView ivFavCard;
//    private static BaseAnswerViewActivity act;

    protected int pageCount;
    protected int currentIndex;


//    protected StudentLoadingLayout loadingLayout;

    protected TextView tvQuestionTitle;
    protected Button btnWrongError;
    protected StudentLoadingLayout loadingLayout;

    protected Button btnLastQuestion, btnNextQuestion;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if(adapter != null){
//            int count = adapter.getCount();
//            for(int i = 0; i < count; i++){
//                adapter.getItem(i).onDestroy();
//            }
//        }
//        if(act != null){
//            LogInfo.log("geny", "BaseAnswerViewActivity   onCreate--------act != null");
//            if(act == this)
//                LogInfo.log("geny", "BaseAnswerViewActivity   onCreate--------act == this");
//        }
//        act = this;
        LogInfo.log("geny", "BaseAnswerViewActivity   onCreate--------");

        mRootView = PublicLoadUtils.createPage(this, R.layout.activity_answer_question);
        loadingLayout = (StudentLoadingLayout) mRootView.findViewById(R.id.loading_layout);
        setContentView(mRootView);
        if(savedInstanceState!=null){
            onRestoreInstanceState(savedInstanceState);
        }
//        initView();
//        initData();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    public void showDialog(){
        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_INTELLI_EXE);
    }


    public void showCommonDialog(){
        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
    }


    public void hideDialog(){
        loadingLayout.setViewGone();
    }



    protected void initView(){
        btnLastQuestion = (Button) this.findViewById(R.id.btn_last_question);
        btnLastQuestion.setVisibility(View.GONE);
        btnNextQuestion = (Button) this.findViewById(R.id.btn_next_question);
        btnNextQuestion.setVisibility(View.VISIBLE);

        btnNextQuestion.setOnClickListener(this);
        btnLastQuestion.setOnClickListener(this);

        ivBack = (ImageView) findViewById(R.id.iv_top_back);
        tvToptext = (YanxiuTypefaceTextView) findViewById(R.id.tv_top_title);
        tvToptext.setTypefaceName(YanxiuTypefaceTextView.TypefaceType.METRO_BOLD);
        ivAnswerCard = (ImageView) findViewById(R.id.iv_answer_card);

        loadingLayout = (StudentLoadingLayout) findViewById(R.id.loading_layout);
        ivFavCard = (ImageView) findViewById(R.id.iv_fav_card);
        vpAnswer = (ViewPager) findViewById(R.id.answer_viewpager);
        tvPagerIndex = (TextView) findViewById(R.id.tv_pager_index);
        tvPagerCount = (TextView) this.findViewById(R.id.tv_pager_count);
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

    protected void initData(){
        dataSources =  (SubjectExercisesItemBean) getIntent().getSerializableExtra("subjectExercisesItemBean");

        if(dataSources==null){
            LogInfo.log("geny","dataSources==null");
            return;
        }

        if(dataSources == null || dataSources.getData() == null || dataSources.getData().isEmpty()){
            LogInfo.log(TAG,"dataSources == null || dataSources.getData() == null || dataSources.getData().isEmpty()");
            this.finish();
        }else{
            LogInfo.log(TAG,"dataSources： "+dataSources);
            List<PaperTestEntity> dataList = dataSources.getData().get(0).getPaperTest();
            if(dataList != null && !dataList.isEmpty()){
                adapter.addDataSources(dataSources);
//                JudgeQuestionFragment fragment = (JudgeQuestionFragment) adapter.getItem(0);
//                if(fragment.bean != null)
//                    LogInfo.log("geny", "BaseAnswerViewActivity   initData--------" + fragment.bean.toString());
//                else
//                    LogInfo.log("geny", "BaseAnswerViewActivity   initData-------- null");
                vpAnswer.setAdapter(adapter);
                adapter.setViewPager(vpAnswer);
                LogInfo.log(TAG, "Adapter Refresh ");
                pageCount = adapter.getCount();

                if(1 == adapter.getTotalCount()){
                    btnNextQuestion.setVisibility(View.GONE);
                }

                ivBack.setOnClickListener(this);
                ivAnswerCard.setOnClickListener(this);
                ivFavCard.setOnClickListener(this);
                if(!TextUtils.isEmpty(dataSources.getData().get(0).getName())){
                    tvQuestionTitle.setText(dataSources.getData().get(0).getName());
                }
            }
        }
        setReportError();
    }

    protected void setReportError(){
        if(Configuration.isDebug()){
            btnWrongError = (Button) this.findViewById(R.id.btn_report_error);
            btnWrongError.setVisibility(View.VISIBLE);
            btnWrongError.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataSources != null && dataSources.getData() != null &&
                            dataSources.getData().get(0) != null &&
                            dataSources.getData().get(0).getPaperTest() != null &&
                            !dataSources.getData().get(0).getPaperTest().isEmpty()) {
                        StringBuffer sb = new StringBuffer();
                        for (String srt : dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getAnswer()) {
                            sb.append("-" + srt + "-");
                        }
                        Util.showToast(sb.toString());

//                        RequestFeedBackTask.startFeedBack(BaseAnswerViewActivity.this, dataSources.getData().get(0).getPaperTest().get(currentIndex).getQid() + "");

                    }
                }
            });
        }
    }



    protected int vpState;


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        currentIndex = position;

        btnLastQuestion.setVisibility(View.VISIBLE);
        btnNextQuestion.setVisibility(View.VISIBLE);
        if(vpAnswer.getCurrentItem() == 0){
            btnLastQuestion.setVisibility(View.GONE);
        }else if(vpAnswer.getCurrentItem() == adapter.getTotalCount() - 1){
            btnNextQuestion.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        vpState = state;
    }

    @Override
    public void onClick(View v) {
        if(v == btnLastQuestion){
            if(vpAnswer.getCurrentItem() != 0){
                vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() - 1));
            }


        }else if(v == btnNextQuestion){
            if(vpAnswer.getCurrentItem() != adapter.getTotalCount() - 1 || vpAnswer.getCurrentItem() != adapter.getListCount() - 1){
                LogInfo.log(vpAnswer.getCurrentItem()+"");
                LogInfo.log(adapter.getTotalCount()+"");
                vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() + 1));
            }
        }
    }

    public SubjectExercisesItemBean getDataSources() {
        return dataSources;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public class FixedSpeedScroller extends Scroller {
        protected int mDuration = 200;

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

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
