package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentController;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.ChildIndexEvent;
import com.yanxiu.gphone.student.bean.GroupEventHWRefresh;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.fragment.question.AnswerCardFragment;
import com.yanxiu.gphone.student.fragment.question.AnswerFinishFragment;
import com.yanxiu.gphone.student.fragment.question.BaseQuestionFragment;
import com.yanxiu.gphone.student.fragment.question.NewFillBlanksFragment;
import com.yanxiu.gphone.student.fragment.question.PageIndex;
import com.yanxiu.gphone.student.fragment.question.ReadingQuestionsFragment;
import com.yanxiu.gphone.student.fragment.question.SolveComplexQuestionFragment;
import com.yanxiu.gphone.student.fragment.question.SubjectiveQuestionFragment;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.manager.ActivityManager;
import com.yanxiu.gphone.student.preference.PreferencesManager;
import com.yanxiu.gphone.student.requestTask.RequestSubmitQuesitonTask;
import com.yanxiu.gphone.student.utils.Configuration;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.CommonDialog;
import com.yanxiu.gphone.student.view.DelDialog;
import com.yanxiu.gphone.student.view.picsel.PicSelView;
import com.yanxiu.gphone.student.view.question.GuideClassfyQuestionView;
import com.yanxiu.gphone.student.view.question.GuideMultiQuestionView;
import com.yanxiu.gphone.student.view.question.GuideQuestionView;
import com.yanxiu.gphone.student.view.question.QuestionsListener;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/7/6.
 */
public class AnswerViewActivity extends BaseAnswerViewActivity {

    private static final String TAG = AnswerViewActivity.class.getSimpleName();

    private int comeFrom;
    public static final int GROUP = 0x01;

    private FrameLayout decorView;

    public static int totalTime=0;
    public static int lastTime=0;
    public int currentIndex = 0;

    private CommonDialog dialog;

    public static int childIndex=0;   //当前显示的子题的位置（如果有子题的话）
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

    public final static int LAUNCHER_FROM_GROUP = 0x11;


    private GuideQuestionView mGuideQuestionView;
    private GuideMultiQuestionView mMultiGifImageView;
    private GuideClassfyQuestionView mClassfyGifImageView;

    private boolean isShowAnswerCard = false;

    private boolean isSubmitFinish = false;

    private int mNextIndex;

    private BaseQuestionFragment lastFragment;


//    private ProgressLayout progressLayout;


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
        Intent intent = new Intent(context, AnswerViewActivity.class);
        intent.putExtra("subjectExercisesItemBean", bean);
        context.startActivity(intent);
    }

    public static void launch(Activity context, SubjectExercisesItemBean bean, int comeFrom) {
        Intent intent = new Intent(context, AnswerViewActivity.class);
        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("comeFrom", comeFrom);
        context.startActivity(intent);
    }

    public static void launchForResult(Activity context, SubjectExercisesItemBean bean, int comeFrom) {
        Intent intent = new Intent(context, AnswerViewActivity.class);
        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("comeFrom", comeFrom);
        context.startActivityForResult(intent, LAUNCHER_FROM_GROUP);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogInfo.log(TAG, "onCreate");
        LogInfo.log("lee", "onCreate");
        ActivityManager.destoryAllEntelliTopActivity();
        PicSelView.resetAllData();
//        setContentView(R.layout.activity_answer_question);
        initView();
        initData();
//        testUpload();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
        LogInfo.log("geny", "onSaveInstanceState");
        outState.putSerializable("dataSources", dataSources);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
        LogInfo.log("geny", "onRestoreInstanceState");
        dataSources = (SubjectExercisesItemBean) savedInstanceState.get("dataSources");
    }


    /**
     * 时间转换分：秒
     */
    public String stringForTimeNoHour(int timeMs) {

        StringBuilder formatBuilder = new StringBuilder();
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());

        try {
//            int totalSeconds = timeMs;
            int sec = timeMs % 60;
            timeMs = timeMs / 60;
            int min = timeMs % 60;
            timeMs = timeMs / 60;

            formatBuilder.setLength(0);

            return formatter.format("%02d:%02d:%02d", timeMs, min, sec).toString();
        } finally {
            formatter.close();
        }
    }

    public void updateProgress() {
        tvToptext.setText(stringForTimeNoHour(++totalTime));
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        FrameLayout.LayoutParams allParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        childIndex=0;
        comeFrom = this.getIntent().getIntExtra("comeFrom", -1);
        adapter.setComeFrom(comeFrom);
        adapter.setFlip(new QuestionsListener() {
            @Override
            public void flipNextPager(QuestionsListener listener) {
                if (currentIndex == adapter.getCount() - 1) {
                    upAnswerCard();
                }
            }

            @Override
            public void setDataSources(AnswerBean bean) {

            }

            @Override
            public void initViewWithData(AnswerBean bean) {

            }

            @Override
            public void answerViewClick() {

            }

        });
        if (dataSources != null && dataSources.getData() != null) {
            long begintime = System.currentTimeMillis();
            dataSources.setBegintime(begintime);
//            progressLayout.setWeight(1, adapter.getTotalCount());
            LogInfo.log("geny", "1.0 / adapter.getTotalCount()" + 1.0 / adapter.getTotalCount() + "---- adapter.getTotalCount(" + adapter.getTotalCount());
            totalTime=dataSources.getData().get(0).getPaperStatus().getCosttime();
            tvToptext.setText(stringForTimeNoHour(totalTime));
            tvPagerIndex.setText("1");
            tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(adapter.getTotalCount())));

            if (PreferencesManager.getInstance().getFirstClassfyQuestion() && dataSources.getData().get(0).getPaperTest().get(0).getQuestions().getTemplate().equals(YanXiuConstant.CLASSIFY_QUESTION)) {
                decorView = (FrameLayout) this.findViewById(R.id.fl_decor_view);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                mClassfyGifImageView = new GuideClassfyQuestionView(this);
                decorView.addView(mClassfyGifImageView, params);
                PreferencesManager.getInstance().setFirstClassfyQuestion();
            }
            if (PreferencesManager.getInstance().getFirstMultiQuestion() && dataSources.getData().get(0).getPaperTest().get(0).getQuestions().getChildren() != null && dataSources.getData().get(0).getPaperTest().get(0).getQuestions().getChildren().size() > 0) {
                decorView = (FrameLayout) this.findViewById(R.id.fl_decor_view);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                mMultiGifImageView = new GuideMultiQuestionView(this);
                decorView.addView(mMultiGifImageView, params);
                PreferencesManager.getInstance().setFirstMultiQuestion();
            }


            if (PreferencesManager.getInstance().getFirstQuestion()) {
                decorView = (FrameLayout) this.findViewById(R.id.fl_decor_view);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                mGuideQuestionView = new GuideQuestionView(this);
                decorView.addView(mGuideQuestionView, params);
                PreferencesManager.getInstance().setFirstQuestion();
            }


        }
        setReportError();

    }


    protected void upAnswerCard() {
        adapter.answerViewClick();
        addFragment();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();

        addTimeHandler();
    }


    @Override
    protected void onPause() {
        super.onPause();
        removeTimeHandler();
    }

    private void quitSubmmitDialog() {
        if (comeFrom == GROUP) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            this.finish();
            submitAnswer();
            return;
        }
        if (dataSources != null && dataSources.getData() != null) {
            if (!dataSources.getData().isEmpty() && dataSources.getData().get(0) != null && dataSources.getData().get(0).getPaperTest() != null && !dataSources.getData().get(0).getPaperTest().isEmpty()) {
                int unFinishCount = QuestionUtils.calculationUnFinishAndWrongQuestion(dataSources.getData().get(0).getPaperTest());
                if (unFinishCount == dataSources.getData().get(0).getPaperTest().size()) {
                    this.finish();
                    return;
                }
            }
        }
        dialog = new CommonDialog(this, this.getResources().getString(R.string.question_no_finish_live),
                this.getResources().getString(R.string.question_live),
                this.getResources().getString(R.string.question_cancel),
                new DelDialog.DelCallBack() {
                    @Override
                    public void del() {
                        //2
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        AnswerViewActivity.this.finish();
                        submitAnswer();
                    }

                    @Override
                    public void sure() {
                        //1
                    }

                    @Override
                    public void cancel() {
                        //3
                    }
                });
        dialog.show();
    }


    public void hideFragment() {
        if (Configuration.isDebug() && btnWrongError != null) {
            btnWrongError.setVisibility(View.VISIBLE);
        }
        isShowAnswerCard = false;


        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        if (answerCardFragment != null) {
            ft.hide(answerCardFragment);
        }
        ft.commit();
//        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.content_answer_card, new Fragment()).commitAllowingStateLoss();
    }


    private void submitAnswer() {
        if (dataSources == null) {
            return;
        }
        adapter.answerViewClick();
        long endtime = System.currentTimeMillis();
        dataSources.setEndtime(endtime);
        calculateLastQuestionTime();
        QuestionUtils.clearSubjectiveQuesition(dataSources);
        dataSources.getData().get(0).getPaperStatus().setCosttime(AnswerViewActivity.totalTime);
        RequestSubmitQuesitonTask requestSubmitQuesitonTask = new RequestSubmitQuesitonTask(this, dataSources, RequestSubmitQuesitonTask.LIVE_CODE, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                EventBus.getDefault().post(new GroupEventHWRefresh());
            }
            @Override
            public void dataError(int type, String msg) {
            }
        });
        requestSubmitQuesitonTask.start();
    }

    private void addTimeHandler() {
        if (handler != null) {
            handler.removeMessages(HANDLER_TIME);
            handler.sendEmptyMessageDelayed(HANDLER_TIME, HANDLER_TIME_DELAYED);
        }
    }

    private void removeTimeHandler() {
        if (handler != null) {
            handler.removeMessages(HANDLER_TIME);
        }
    }


    @Override
    public void onBackPressed() {
        if (mMultiGifImageView != null && mMultiGifImageView.isShown()) {
            mMultiGifImageView.setVisibility(View.GONE);
        }
        if (mClassfyGifImageView != null && mClassfyGifImageView.isShown()) {
            mClassfyGifImageView.setVisibility(View.GONE);
        }
        if (mGuideQuestionView != null && mGuideQuestionView.isShown()) {
            mGuideQuestionView.setVisibility(View.GONE);
        } else if (isShowAnswerCard) {
            ivAnswerCard.setVisibility(View.VISIBLE);
            removeFragment();
        } else if (isSubmitFinish) {
            super.onBackPressed();
        } else {
            quitSubmmitDialog();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == ivBack) {
            if (isShowAnswerCard) {
//                tvAnswerCard.setVisibility(View.VISIBLE);
                removeFragment();
            } else {
                quitSubmmitDialog();
            }
        } else if (v == ivAnswerCard) {
            if (!isShowAnswerCard) {
                adapter.answerViewClick();
                addFragment();
            }
        }

    }

    AnswerCardFragment answerCardFragment;

    private void addFragment() {
        if (isShowAnswerCard) {
            return;
        }
        if (Configuration.isDebug() && btnWrongError != null) {
            btnWrongError.setVisibility(View.GONE);
        }
        isShowAnswerCard = true;
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();

        answerCardFragment = new AnswerCardFragment();
        Bundle args = new Bundle();
        args.putSerializable("subjectExercisesItemBean", dataSources);
        args.putInt("comeFrom", comeFrom);
        answerCardFragment.setArguments(args);
        ft.add(R.id.content_answer_card, answerCardFragment);
        ft.commit();

    }


    public void addFinishFragment(SubjectExercisesItemBean bean, int comeFrom) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();

        AnswerFinishFragment answerFinishFragment = new AnswerFinishFragment();
        Bundle args = new Bundle();
        args.putSerializable("subjectExercisesItemBean", bean);
        args.putInt("comeFrom", comeFrom);
        answerFinishFragment.setArguments(args);
        ft.add(R.id.content_answer_card, answerFinishFragment);
        ft.commit();

        isSubmitFinish = true;

    }


    public void removeFragment() {
        if (Configuration.isDebug() && btnWrongError != null) {
            btnWrongError.setVisibility(View.VISIBLE);
        }
        isShowAnswerCard = false;


        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        if (answerCardFragment != null) {
            ft.remove(answerCardFragment);
        }
        ft.commit();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        //判断是否是最后一页，偏移量都为零  弹出答题卡
        if (position == adapter.getCount() - 1 && !isSubmitFinish) {
            if (positionOffset == 0 && positionOffsetPixels == 0 && vpState == ViewPager.SCROLL_STATE_DRAGGING) {
                upAnswerCard();
            }
        }
        LogInfo.log("geny", "position" + position + "----positionOffset" + positionOffset + "----positionOffsetPixels" + positionOffsetPixels);
    }

    public void setViewPagerPosition(int position, int childPosition) {
        vpAnswer.setCurrentItem(position);
        ((BaseQuestionFragment) adapter.getmFragments().get(position)).setChildPagerIndex(childPosition);
//        LogInfo.log("geny", "position" + position + "----childPosition" + childPosition + "----childPosition" + childPosition);
        if (childPosition != -1) {
            Fragment fragment = adapter.getItem(position);
            if (fragment instanceof ReadingQuestionsFragment) {
                //发送消息通知子viewpager滑到指定的位置
//                LogInfo.log("geny-", "position" + position + "----childPosition" + childPosition + "----childPosition" + childPosition);
                EventBus.getDefault().post(new ChildIndexEvent(childPosition));
            }
        }
        removeFragment();
    }

    public void setIndexFromRead(int position) {
        tvPagerIndex.setText(position + "" );
    }

    public void setIndexNext(int index) {
        mNextIndex = index;
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        LogInfo.log(TAG, "onPageSelected position: " + position);
        LogInfo.log("TTTT", "onPageSelected" + position);
        currentIndex = position;
        int costTime = totalTime - lastTime;
        lastTime = totalTime;
        LogInfo.log("geny", costTime + "---costTime-------viewPagerLastPosition----" + viewPagerLastPosition);
        if(dataSources.getData().get(0).getPaperTest().get(position).getQuestions().getChildren()!=null && flag ){
            //flag表示 是不是按的上一题按钮切换的题目；此处这样处理的原因是因为，如果当前题目是复合题，然后点上一题按钮切换到上一道复合题的话，会切换到上一道复合题的最
            //后一道小题，切换的时候会先调用小题的viewPager的onPageSelected方法，然后chilidIndex被设为小题的index，此时在这儿再去保存上一道题的小题的答题时间，可能
            //会导致childIndex越界
            flag=false;
        }else{
            adapter.setCostTime(costTime, viewPagerLastPosition,childIndex);
            childIndex=0;
        }
        if (PreferencesManager.getInstance().getFirstClassfyQuestion() && dataSources.getData().get(0).getPaperTest().get(position).getQuestions().getTemplate().equals(YanXiuConstant.CLASSIFY_QUESTION)) {
            decorView = (FrameLayout) this.findViewById(R.id.fl_decor_view);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            mClassfyGifImageView = new GuideClassfyQuestionView(this);
            decorView.addView(mClassfyGifImageView, params);
            PreferencesManager.getInstance().setFirstClassfyQuestion();
        }
        tvPagerIndex.setVisibility(View.VISIBLE);
        ivAnswerCard.setVisibility(View.VISIBLE);
        if (Configuration.isDebug() && btnWrongError != null) {
            btnWrongError.setVisibility(View.VISIBLE);
        }
//        Fragment fragment = adapter.getItem(position);
        List<Fragment> list = ((AnswerAdapter) vpAnswer.getAdapter()).getmFragments();
        int sumIndex = 0;
        for (int i = 0; i < position; i++) {
            BaseQuestionFragment fragment1 = (BaseQuestionFragment) list.get(i);
            sumIndex = sumIndex + fragment1.getChildCount();
        }
        LogInfo.log("TTT", "position" + ((BaseQuestionFragment) list.get(position)).getChildCount());
        if (nextPager_onclick == 0 || ((BaseQuestionFragment) list.get(position)).getChildCount() == 1) {
            tvPagerIndex.setText(String.valueOf(dataSources.getData().get(0).getPaperTest().get(position).getQuestions().getPositionForCard() + 1));
        } else {
            tvPagerIndex.setText(String.valueOf(sumIndex + ((BaseQuestionFragment) list.get(position)).getChildCount()));
            nextPager_onclick = 0;
        }


        tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(adapter.getTotalCount())));
        viewPagerLastPosition = position;
//        changeCurrentSelData();


    }

    @Override
    public void onPageScrollStateChanged(int state) {
        super.onPageScrollStateChanged(state);
    }


    public void selectViewPager() {
        LogInfo.log("geny", "selectViewPager " + (vpAnswer.getCurrentItem() + 1));
        vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() + 1));
    }

    public void calculateLastQuestionTime() {
        int costTime = totalTime - lastTime;
        lastTime = totalTime;
        int size = dataSources.getData().get(0).getPaperTest().size();
        QuestionEntity questionEntity = dataSources.getData().get(0).getPaperTest().get(vpAnswer.getCurrentItem()).getQuestions();
        if(questionEntity.getChildren()!=null && !questionEntity.getChildren().isEmpty()){
            adapter.setCostTime(costTime,viewPagerLastPosition,childIndex);
        }else {
            adapter.setCostTime(costTime, viewPagerLastPosition,-1);
        }
    }


    /**
     * 切换ViewPager
     * 切换当前选择设置数据
     */
    private void changeCurrentSelData() {
        Fragment currentFragment = adapter.getItem(currentIndex);
        if (currentFragment != null && currentFragment instanceof SubjectiveQuestionFragment) {
            QuestionEntity questionsEntity = null;
            if (dataSources != null && dataSources.getData() != null && dataSources.getData().get(0) != null) {
                if (dataSources.getData().get(0).getPaperTest() != null && dataSources.getData().get(0).getPaperTest().get(currentIndex) != null) {
                    questionsEntity = dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions();
                }
            }
            ((SubjectiveQuestionFragment) currentFragment).changeCurrentSelData(questionsEntity);
        }
    }

    @java.lang.Override
    protected void onDestroy() {
        super.onDestroy();
        LogInfo.log(TAG, "onDestroy");
        totalTime=0;
        lastTime=0;
        PicSelView.resetAllData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        LogInfo.log(TAG, "resultCode = " + resultCode + " ,requestCode = " + requestCode);

        if (resultCode == RESULT_OK) {
            LogInfo.log(TAG, "CURRENTINDEX: " + currentIndex);
            BaseQuestionFragment currentFragment = (BaseQuestionFragment) adapter.getItem(currentIndex);
            if (dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getChildren() != null && !dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getChildren().isEmpty())
                currentFragment= (BaseQuestionFragment) currentFragment.getChildFragment();
            if (currentFragment != null && currentFragment instanceof SubjectiveQuestionFragment) {
                currentFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


}
