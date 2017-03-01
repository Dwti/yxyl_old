package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.MistakeRedoAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.fragment.question.BaseQuestionFragment;
import com.yanxiu.gphone.student.fragment.question.SubjectiveQuestionFragment;
import com.yanxiu.gphone.student.utils.Content;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.MistakeRedoDialog;
import java.lang.ref.WeakReference;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Canghaixiao.
 * Time : 2017/2/10 11:59.
 * Function :
 */

public class MistakeRedoActivity extends BaseAnswerViewActivity implements MistakeRedoAdapter.OnShouldDownLoadListener {

    private static final int TO_CARD = 0X00;
    public static final int RIGHT = R.string.submit_right;
    public static final int FAIL = R.string.submit_fail;

    private MistakeRedoAdapter mistakeRedoAdapter;
    private int pageIndex = 0;
    private String stageId;
    private String subjectId;
    private String editionId;
    private String volumeId;
    private String chapterId;
    private String sectionId;
    private String questionId;
    private int isChapterSection = 0;
    private String uniteId;
    private int wrongCounts;
    private boolean isNetData = true;

    private int comeFrom = 0;
    private int position = 0;

    private MyThread thread;
    private MyHandle handle;
    private Timer timer = new Timer();
    private RelativeLayout rel_popup;
    private TextView TextViewInfo;
    private MistakeRedoDialog dialog;
    private ImageView iv_top_back;

    public static void launch(Activity context, SubjectExercisesItemBean bean, String subjectId, int pagerIndex, int childIndex, int comeFrom, String wrongCount, int position) {
        Intent intent = new Intent(context, MistakeRedoActivity.class);
        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("pagerIndex", pagerIndex);
        intent.putExtra("childIndex", childIndex);
        intent.putExtra("comeFrom", comeFrom);
        intent.putExtra("position", position);
        intent.putExtra("wrongCount", wrongCount);
        context.startActivityForResult(intent, YanXiuConstant.LAUNCHER_FROM_MISTAKE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        stageId = LoginModel.getUserinfoEntity().getStageid() + "";
//        subjectId = getIntent().getStringExtra("subjectId");
//        editionId = getIntent().getStringExtra("editionId");
//        volumeId = getIntent().getStringExtra("volumeId");
//        chapterId = getIntent().getStringExtra("chapterId");
//        sectionId = getIntent().getStringExtra("sectionId");
//        uniteId = getIntent().getStringExtra("uniteId");
//        isChapterSection = getIntent().getIntExtra("isChapterSection", 0);
//        isNetData = getIntent().getBooleanExtra("isNetData", true);
//        comeFrom = getIntent().getIntExtra("comeFrom", 0);
//        position = getIntent().getIntExtra("position", 0);
        String wrongCount = getIntent().getStringExtra("wrongCount");
        try {
            wrongCounts = Integer.parseInt(wrongCount);
        } catch (Exception e) {

        }
        initView();
        initData();
    }

    @Override
    protected void initView() {
        super.initView();
        rel_popup = (RelativeLayout) findViewById(R.id.rel_popup);
        TextViewInfo = (TextView) findViewById(R.id.TextViewInfo);
        iv_top_back= (ImageView) findViewById(R.id.iv_top_back);
        iv_top_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_answer_card:
                Intent intent = new Intent(this, MistakeRedoCardActivity.class);
                startActivityForResult(intent, TO_CARD);
                break;
            case R.id.iv_top_back:
                if (dialog==null){
                    initPopup();
                }else {
                    dialog.setStageDialogCallBack(back);
                    List<PaperTestEntity> list=dataSources.getData().get(0).getPaperTest();
                    dialog.setQuestionNumber(getTatleNumber(list),getRightNumber(list),getFailNumber(list));
                    dialog.show();
                }
                break;
        }
    }

    @Override
    protected void initData() {

//        if (getIntent() != null) {
//            if (getIntent().getSerializableExtra("subjectExercisesItemBean") != null) {
//                dataSources = (SubjectExercisesItemBean) getIntent().getSerializableExtra("subjectExercisesItemBean");
//            }
//        }

        dataSources = JSON.parseObject(ss, SubjectExercisesItemBean.class);

        if (dataSources == null) {
            return;
        }

        if (dataSources == null || dataSources.getData() == null || dataSources.getData().isEmpty()) {
            this.finish();
        } else {
            List<PaperTestEntity> dataList = dataSources.getData().get(0).getPaperTest();
            if (dataList != null && !dataList.isEmpty()) {
                QuestionUtils.addChildQuestionToParent(dataList);     //对题目的pageIndex childPageIndex,positionForCard,childPositionForCard进行赋值

                mistakeRedoAdapter = new MistakeRedoAdapter(getSupportFragmentManager());
                handle = new MyHandle(mistakeRedoAdapter);
                mistakeRedoAdapter.setDataSourcesFirst(dataSources, wrongCounts, 0, dataSources.getData().get(0).getPaperTest().size());
                vpAnswer.setAdapter(mistakeRedoAdapter);
                mistakeRedoAdapter.setViewPager(vpAnswer);
                mistakeRedoAdapter.setLoadListener(this);
                pageCount = mistakeRedoAdapter.getCount();

                ivBack.setOnClickListener(this);
                ivAnswerCard.setOnClickListener(this);
                ivFavCard.setOnClickListener(this);
                if (!TextUtils.isEmpty(dataSources.getData().get(0).getName())) {
                    tvQuestionTitle.setText(dataSources.getData().get(0).getName());
                }
            }
        }
        setReportError();

        pageIndex = position;
        vpAnswer.setCurrentItem(position);
        tvPagerIndex.setText(String.valueOf(position + 1));
        //tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(adapter.getTotalCount())));
        tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), (wrongCounts) + ""));
        tvToptext.setText(this.getResources().getString(R.string.questiong_resolution));
        tvToptext.setCompoundDrawables(null, null, null, null);
//            tvAnswerCard.setVisibility(View.GONE);
        ivAnswerCard.setBackgroundResource(R.drawable.selector_mistake_question_card);

    }

    @Override
    public boolean getCurrent(int fragment_ID) {
        return super.getCurrent(fragment_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == TO_CARD) {
                    //答题卡返回
                    int index = data.getIntExtra("index", -1);
                    if (index != -1) {
                        vpAnswer.setCurrentItem(index);
                    }
                } else {
                    BaseQuestionFragment currentFragment = (BaseQuestionFragment) mistakeRedoAdapter.getItem(currentIndex);
                    if (dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getChildren() != null && !dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getChildren().isEmpty())
                        if (currentFragment != null && currentFragment.getChildFragment() != null) {
                            currentFragment = (BaseQuestionFragment) currentFragment.getChildFragment();
                        }
                    if (currentFragment != null && currentFragment instanceof SubjectiveQuestionFragment) {
                        currentFragment.onActivityResult(requestCode, resultCode, data);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onLoadListener(int position, int page_start, int page_end) {
        if (thread != null) {
            thread.setClear();
            thread = null;
        }
        handle.setPage_end(page_end);
        handle.setPage_start(page_start);
        thread = new MyThread(handle);
        thread.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (dialog==null){
            initPopup();
        }else {
            dialog.show();
            dialog.setStageDialogCallBack(back);
            List<PaperTestEntity> list=dataSources.getData().get(0).getPaperTest();
            dialog.setQuestionNumber(getTatleNumber(list),getRightNumber(list),getFailNumber(list));
        }
    }

    private MistakeRedoDialog.StageDialogCallBack back=new MistakeRedoDialog.StageDialogCallBack() {
        @Override
        public void stage() {

        }

        @Override
        public void cancel() {
            dialog.dismiss();
            dialog=null;
            MistakeRedoActivity.this.finish();
        }
    };

    private void initPopup(){
        dialog=new MistakeRedoDialog(this, back);
        dialog.show();
        List<PaperTestEntity> list=dataSources.getData().get(0).getPaperTest();
        dialog.setQuestionNumber(getTatleNumber(list),getRightNumber(list),getFailNumber(list));
    }

    private String getTatleNumber(List<PaperTestEntity> list){
        int t=0;
        for (PaperTestEntity entity:list){
             if (entity.getQuestions().isHaveAnser()){
                t++;
            }
        }
        return t+"";
    }

    private String getRightNumber(List<PaperTestEntity> list){
        int r=0;
        for (PaperTestEntity entity:list){
            if (entity.getQuestions().getAnswerIsRight()== QuestionEntity.ANSWER_RIGHT){
                r++;
            }
        }
        return r+"";
    }

    private String getFailNumber(List<PaperTestEntity> list){
        int f=0;
        for (PaperTestEntity entity:list){
            if (entity.getQuestions().getAnswerIsRight()==QuestionEntity.ANSWER_FAIL){
                f++;
            }
        }
        return f+"";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread != null) {
            thread.setClear();
            thread = null;
        }
        if (dialog!=null){
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            dialog=null;
        }
    }

    private class MyThread extends Thread {

        private WeakReference<MyHandle> weak;

        MyThread(MyHandle handle) {
            weak = new WeakReference<>(handle);
        }

        void setClear() {
            if (weak != null) {
                weak.clear();
                weak = null;
            }
        }

        @Override
        public void run() {
            super.run();
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SubjectExercisesItemBean dataSources = JSON.parseObject(ss, SubjectExercisesItemBean.class);
            if (weak != null && weak.get() != null) {
                MyHandle handle = weak.get();
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = dataSources;
                handle.sendMessage(msg);
            }
        }
    }

    private static class MyHandle extends Handler {

        private WeakReference<MistakeRedoAdapter> weak;
        private WeakReference<RelativeLayout> weak_pop;
        private int page_start;
        private int page_end;

        MyHandle(MistakeRedoAdapter mistakeRedoAdapter) {
            weak = new WeakReference<>(mistakeRedoAdapter);
        }

        public void setPage_start(int page_start) {
            this.page_start = page_start;
        }

        public void setPage_end(int page_end) {
            this.page_end = page_end;
        }

        public void setPopupWindow(RelativeLayout window) {
            weak_pop = new WeakReference<>(window);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    SubjectExercisesItemBean dataSources = (SubjectExercisesItemBean) msg.obj;
                    if (weak != null && weak.get() != null) {
                        MistakeRedoAdapter adapter = weak.get();
                        adapter.addDataSources(dataSources, page_start, page_end);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 1:
                    if (weak_pop != null && weak_pop.get() != null) {
                        RelativeLayout window = weak_pop.get();
                        window.setVisibility(View.GONE);
                        weak_pop.clear();
                        weak_pop = null;
                    }
                    break;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        super.onPageScrollStateChanged(state);
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
    }

    public void showPopup(int message) {
        TextViewInfo.setText(message);
        rel_popup.setVisibility(View.VISIBLE);
        MyTimerTask task = new MyTimerTask(handle, rel_popup);
        timer.schedule(task, 1000);
    }

    private class MyTimerTask extends TimerTask {

        private WeakReference<MyHandle> weak_hand;
        private WeakReference<RelativeLayout> weak_pop;

        public MyTimerTask(MyHandle handle, RelativeLayout window) {
            weak_hand = new WeakReference<>(handle);
            weak_pop = new WeakReference<>(window);
        }

        @Override
        public void run() {
            if (weak_hand == null || weak_hand.get() == null || weak_pop == null || weak_pop.get()==null) {
                return;
            }
            MyHandle handle = weak_hand.get();
            RelativeLayout window = weak_pop.get();
            Message msg = Message.obtain();
            msg.what = 1;
            handle.setPopupWindow(window);
            handle.sendMessage(msg);
        }
    }

    String ss = Content.bb;
}
