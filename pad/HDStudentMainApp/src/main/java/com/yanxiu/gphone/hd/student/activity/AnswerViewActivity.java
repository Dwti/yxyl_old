package com.yanxiu.gphone.hd.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.AnswerBean;
import com.yanxiu.gphone.hd.student.bean.ChildIndexEvent;
import com.yanxiu.gphone.hd.student.bean.QuestionEntity;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.fragment.question.AnswerCardFragment;
import com.yanxiu.gphone.hd.student.fragment.question.AnswerFinishFragment;
import com.yanxiu.gphone.hd.student.fragment.question.PageIndex;
import com.yanxiu.gphone.hd.student.fragment.question.ReadingQuestionsFragment;
import com.yanxiu.gphone.hd.student.fragment.question.SubjectiveQuestionFragment;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.manager.ActivityManager;
import com.yanxiu.gphone.hd.student.preference.PreferencesManager;
import com.yanxiu.gphone.hd.student.requestTask.RequestSubmitQuesitonTask;
import com.yanxiu.gphone.hd.student.utils.Configuration;
import com.yanxiu.gphone.hd.student.utils.QuestionUtils;
import com.yanxiu.gphone.hd.student.view.CommonDialog;
import com.yanxiu.gphone.hd.student.view.DelDialog;
import com.yanxiu.gphone.hd.student.view.picsel.PicSelView;
import com.yanxiu.gphone.hd.student.view.question.GuideQuestionView;
import com.yanxiu.gphone.hd.student.view.question.QuestionsListener;

import java.util.Formatter;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/7/6.
 */
public class AnswerViewActivity extends BaseAnswerViewActivity{

    private static final String TAG=AnswerViewActivity.class.getSimpleName();

    private int comeFrom;
    public static final int GROUP = 0x01;

    private RelativeLayout decorView;

    private int lastTime;
    private int currentTime;

    public int currentIndex = 0;
    private int totalTime;

    private CommonDialog dialog;

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

    private boolean isShowAnswerCard = false;

    private boolean isSubmitFinish = false;

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
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogInfo.log(TAG, "onCreate");
        LogInfo.log("lee","onCreate");
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
//        totalTime += 600;
        tvToptext.setText(stringForTimeNoHour(++totalTime));
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData(){
        super.initData();
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
        if(dataSources != null && dataSources.getData() != null){
            long begintime = System.currentTimeMillis();
            dataSources.setBegintime(begintime);
            LogInfo.log("geny", "1.0 / adapter.getTotalCount()" + 1.0 / adapter.getTotalCount() + "---- adapter.getTotalCount(" + adapter.getTotalCount());
            tvToptext.setText(stringForTimeNoHour(0));
            tvPagerIndex.setText("1");
            tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(adapter.getTotalCount())));
            if(PreferencesManager.getInstance().getFirstQuestion()){
                decorView = (RelativeLayout) this.findViewById(R.id.decor_view);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                mGuideQuestionView = new GuideQuestionView(this);
                decorView.addView(mGuideQuestionView, params);
                PreferencesManager.getInstance().setFirstQuestion();
            }
        }
        setReportError();

//        mGestureDetector = new GestureDetector(
//                new GestureDetector.SimpleOnGestureListener() {
//
//                    @Override
//                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//
//                        float y_dis = Math.abs(e2.getY() - e1.getY());
//                        float x_dis = Math.abs(e2.getX() - e1.getX());
//
//                        if (x_dis > FLING_MIN_DISTANCE
//                                                    && Math.abs(velocityX) > FLING_MIN_VELOCITY
//                                                    && y_dis < x_dis) {
//                            if (e1.getX() - e2.getX() > 0) {
//                                LogInfo.log("geny-", "----------------------forward-------------");
//                                if(currentIndex == adapter.getCount() - 1 && !isShowAnswerCard){
//                                    LogInfo.log("geny-", "position == adapter.getCount() - 1 && !isShowAnswerCard     ----------------------forward-------------");
//                                    adapter.answerViewClick();
//                                    addFragment();
//                                    return true;
//                                }else{
//                                    return false;
//                                }
//
//                            }
//                        }
//                        return false;
//                    }
//                });
    }


    protected void upAnswerCard(){
        adapter.answerViewClick();
        addFragment();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (mGestureDetector.onTouchEvent(ev)) {
//            return true;
//        }
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

    private void quitSubmmitDialog(){
        if(comeFrom == GROUP){
            submitAnswer();
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            this.finish();
            return;
        }
        if(dataSources != null && dataSources.getData() != null){
            if(!dataSources.getData().isEmpty() && dataSources.getData().get(0) != null && dataSources.getData().get(0).getPaperTest() != null && !dataSources.getData().get(0).getPaperTest().isEmpty()){
                int unFinishCount = QuestionUtils.calculationUnFinishAndWrongQuestion(dataSources.getData().get(0).getPaperTest());
                if(unFinishCount == dataSources.getData().get(0).getPaperTest().size()){
                    this.finish();
                    return;
                }
            }
        }
        dialog = new CommonDialog(this,this.getResources().getString(R.string.question_no_finish_live),
                this.getResources().getString(R.string.question_live),
                this.getResources().getString(R.string.question_cancel),
                new DelDialog.DelCallBack(){
                    @Override
                    public void del() {
                        //2
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        AnswerViewActivity.this.finish();
                        submitAnswer();
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


    private void submitAnswer(){
        if(dataSources==null){
            return;
        }
        long endtime = System.currentTimeMillis();
        dataSources.setEndtime(endtime);
        QuestionUtils.clearSubjectiveQuesition(dataSources);
        RequestSubmitQuesitonTask requestSubmitQuesitonTask = new RequestSubmitQuesitonTask(this, dataSources, RequestSubmitQuesitonTask.LIVE_CODE, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
            }


            @Override
            public void dataError(int type, String msg) {
            }
        });
        requestSubmitQuesitonTask.start();
    }

    private void addTimeHandler(){
        if(handler != null){
            handler.removeMessages(HANDLER_TIME);
            handler.sendEmptyMessageDelayed(HANDLER_TIME, HANDLER_TIME_DELAYED);
        }
    }

    private void removeTimeHandler(){
        if(handler != null){
            handler.removeMessages(HANDLER_TIME);
        }
    }


    @Override
    public void onBackPressed() {
        if(mGuideQuestionView!=null && mGuideQuestionView.isShown()){
            mGuideQuestionView.setVisibility(View.GONE);
        }else if(isShowAnswerCard){
            ivAnswerCard.setVisibility(View.VISIBLE);
            removeFragment();
        }else if(isSubmitFinish){
            super.onBackPressed();
        }else{
            quitSubmmitDialog();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == ivBack){
            if(isShowAnswerCard){
//                tvAnswerCard.setVisibility(View.VISIBLE);
                removeFragment();
            }else{
                quitSubmmitDialog();
            }
        }else if (v == ivAnswerCard) {
            if(!isShowAnswerCard){
                adapter.answerViewClick();
                addFragment();
            }
//            tvAnswerCard.setTag();
//            AnswerCardActivity.launch(this, dataSources, totalTime);
        }
    }

    AnswerCardFragment answerCardFragment;
    private void addFragment(){
        if(isShowAnswerCard){
            return;
        }
        if(Configuration.isDebug() && btnWrongError != null){
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

    public void addFinishFragment(SubjectExercisesItemBean bean, int comeFrom){
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



    public void hideFragment(){
        if(Configuration.isDebug() && btnWrongError != null){
            btnWrongError.setVisibility(View.VISIBLE);
        }
        isShowAnswerCard = false;


        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        if(answerCardFragment != null){
            ft.hide(answerCardFragment);
        }
        ft.commit();
//        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.content_answer_card, new Fragment()).commitAllowingStateLoss();
    }


    public void removeFragment(){
        if(Configuration.isDebug() && btnWrongError != null){
            btnWrongError.setVisibility(View.VISIBLE);
        }
        isShowAnswerCard = false;


        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        if(answerCardFragment != null){
            ft.remove(answerCardFragment);
        }
        ft.commit();
//        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.content_answer_card, new Fragment()).commitAllowingStateLoss();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        //判断是否是最后一页，偏移量都为零  弹出答题卡
        if(position == adapter.getCount() - 1 && !isSubmitFinish){
            if (positionOffset == 0 && positionOffsetPixels ==0 && vpState == ViewPager.SCROLL_STATE_DRAGGING){
                upAnswerCard();
            }
        }
        LogInfo.log("geny", "position" + position + "----positionOffset" + positionOffset + "----positionOffsetPixels" + positionOffsetPixels);
//        LogInfo.log("geny", "position" + position + "----positionOffset" + positionOffset + "----positionOffsetPixels" + positionOffsetPixels);
    }

    public void setViewPagerPosition(int position, int childPosition){
        vpAnswer.setCurrentItem(position);
//        LogInfo.log("geny", "position" + position + "----childPosition" + childPosition + "----childPosition" + childPosition);
        if(childPosition != -1){
            Fragment fragment = adapter.getItem(position);
            if(fragment instanceof ReadingQuestionsFragment){
                //发送消息通知子viewpager滑到指定的位置
//                LogInfo.log("geny-", "position" + position + "----childPosition" + childPosition + "----childPosition" + childPosition);
                EventBus.getDefault().post(new ChildIndexEvent(childPosition));
            }
        }
        removeFragment();
    }

    public void setIndexFromRead(int position){
        tvPagerIndex.setText(position + "/" + adapter.getTotalCount());
    }


    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        LogInfo.log(TAG,"onPageSelected position: "+position);
        currentIndex = position;
        currentTime = totalTime;
        int costTime = currentTime - lastTime;
        lastTime = currentTime;
        LogInfo.log("geny", costTime + "---costTime-------viewPagerLastPosition----" + viewPagerLastPosition);
        adapter.setCostTime(costTime, viewPagerLastPosition);

        tvPagerIndex.setVisibility(View.VISIBLE);
        ivAnswerCard.setVisibility(View.VISIBLE);
        if(Configuration.isDebug() && btnWrongError != null){
            btnWrongError.setVisibility(View.VISIBLE);
        }
        Fragment fragment = adapter.getItem(position);
//        tvPagerIndex.setText(((PageIndex) fragment).getPageIndex() + "/" + adapter.getTotalCount());
        tvPagerIndex.setText(String.valueOf(position + 1));
        tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(adapter.getTotalCount())));
        viewPagerLastPosition = position;
        LogInfo.log("geny", costTime + "---mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm---" + ((PageIndex) fragment).getPageIndex() + "/" + adapter.getTotalCount());
        changeCurrentSelData();

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        super.onPageScrollStateChanged(state);
    }


    public void selectViewPager(){
        LogInfo.log("geny","selectViewPager " + (vpAnswer.getCurrentItem() + 1));
        vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() + 1));
    }

    public void calculationTime() {
        int costTime = totalTime - lastTime;
        lastTime = totalTime;
        LogInfo.log("geny", costTime + "---costTime-------viewPagerLastPosition----" + viewPagerLastPosition);
        adapter.setCostTime(costTime, viewPagerLastPosition);
    }


    /**
     * 切换ViewPager
     * 切换当前选择设置数据
     */
    private void changeCurrentSelData(){
        Fragment currentFragment  = adapter.getItem(currentIndex);
        if(currentFragment != null && currentFragment instanceof SubjectiveQuestionFragment){
            QuestionEntity questionsEntity = null;
            if(dataSources != null && dataSources.getData() != null && dataSources.getData().get(0) != null){
                if(dataSources.getData().get(0).getPaperTest() != null && dataSources.getData().get(0).getPaperTest().get(currentIndex) != null){
                    questionsEntity = dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions();
                }
            }
            ((SubjectiveQuestionFragment)currentFragment).changeCurrentSelData(questionsEntity);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogInfo.log(TAG, "onDestroy");
        PicSelView.resetAllData();
        if(decorView!=null){
            decorView.removeAllViews();
        }
        decorView=null;
        dialog=null;
        if(mGuideQuestionView!=null){
            mGuideQuestionView.removeAllViews();
        }
        mGuideQuestionView=null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        LogInfo.log(TAG, "resultCode = " + resultCode + " ,requestCode = " + requestCode);

        if (resultCode == RESULT_OK) {
            LogInfo.log(TAG,"CURRENTINDEX: "+currentIndex);
            Fragment currentFragment  = adapter.getItem(currentIndex);
            if(currentFragment != null && currentFragment instanceof SubjectiveQuestionFragment){
                currentFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


//    public void showLoading(){
//        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
//    }
//
//
//    public void hideLoading(){
//        loadingLayout.setViewGone();
//    }


}
