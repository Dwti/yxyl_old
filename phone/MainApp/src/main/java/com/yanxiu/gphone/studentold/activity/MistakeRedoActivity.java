package com.yanxiu.gphone.studentold.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.NetWorkTypeUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.adapter.MistakeRedoAdapter;
import com.yanxiu.gphone.studentold.bean.MistakeDoWorkBean;
import com.yanxiu.gphone.studentold.bean.MistakeRedoCardBean;
import com.yanxiu.gphone.studentold.bean.PaperTestEntity;
import com.yanxiu.gphone.studentold.bean.QuestionEntity;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.fragment.question.BaseQuestionFragment;
import com.yanxiu.gphone.studentold.fragment.question.MistakeRedoCardFragment;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.requestTask.MistakeDoWorkTask;
import com.yanxiu.gphone.studentold.requestTask.RequestMisRedoAddClassTask;
import com.yanxiu.gphone.studentold.requestTask.RequestMisRedoCardClassTask;
import com.yanxiu.gphone.studentold.utils.QuestionUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;
import com.yanxiu.gphone.studentold.view.MistakeRedoDialog;
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
    private static final String TAG="ANSWER_CARD";
    public static final int RIGHT = R.string.submit_right;
    public static final int FAIL = R.string.submit_fail;

    private int wrongCounts;
    private int position = 0;

    private MyHandle handle;
    private Timer timer = new Timer();
    private RelativeLayout rel_popup;
    private TextView TextViewInfo;
    private MistakeRedoDialog dialog;
    private ImageView iv_top_back;
    private String stageId;
    private String subjectId;
    private MistakeRedoCardBean mistakeRedoCardBean;
    private FrameLayout content_answer_card;

    private String lastWqid="";
    private String lastWqnumber="";
    private String deleteWqidList="";
    private int index=0;
    private RequestMisRedoAddClassTask addClassTask;

    public static void launch(Activity context, SubjectExercisesItemBean bean,String wrongCount,String stageId,String subjectId) {
        Intent intent = new Intent(context, MistakeRedoActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("subjectExercisesItemBean", bean);
        bundle.putString("wrongCount", wrongCount);
        bundle.putString("stageId",stageId);
        bundle.putString("subjectId",subjectId);
        intent.putExtra("bundle",bundle);
        context.startActivityForResult(intent, YanXiuConstant.LAUNCHER_FROM_MISTAKE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle=getIntent().getBundleExtra("bundle");
            String wrongCount=bundle.getString("wrongCount","0");
            wrongCounts = Integer.parseInt(wrongCount);
            stageId=bundle.getString("stageId","");
            subjectId=bundle.getString("subjectId","");
            dataSources= (SubjectExercisesItemBean) bundle.getSerializable("subjectExercisesItemBean");
        } catch (Exception e) {}
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
        content_answer_card= (FrameLayout) findViewById(R.id.content_answer_card);
        content_answer_card.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_answer_card:
                CommonCoreUtil.hideSoftInput(mRootView,mRootView);
                if (mistakeRedoCardBean==null) {
                    requestMisRedoCard();
                }else {
                    boolean flag=checkIsHaveAnswer();
                    addFragment();
                }
                break;
            case R.id.iv_top_back:
                if (dialog==null){
                    initPopup();
                }else {
                    setDialogShow();
                }
                break;
        }
    }

    private void requestMisRedoCard(){
        mRootView.loading(true);
        RequestMisRedoCardClassTask cardClassTask=new RequestMisRedoCardClassTask(this, stageId, subjectId, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                mRootView.finish();
                mistakeRedoCardBean= (MistakeRedoCardBean) result;
                if (mistakeRedoCardBean!=null&&mistakeRedoCardBean.getData()!=null&&mistakeRedoCardBean.getData().size()>0){
                    boolean flag=checkIsHaveAnswer();
                    addFragment();
                }else {
                    Util.showToast(R.string.data_erro);
                }
            }

            @Override
            public void dataError(int type, String msg) {
                mRootView.finish();
                if (!NetWorkTypeUtils.isNetAvailable()) {
                    Util.showToast(R.string.server_connection_erro);
                }else {
                    Util.showToast(R.string.data_erro);
                }
            }
        });
        cardClassTask.start();
    }

    public String getWqid(){
        int index=vpAnswer.getCurrentItem();
        PaperTestEntity entity=mistakeRedoAdapter.getDatas().get(index);
        if (entity!=null){
            return entity.getWqid();
        }
        return "";
    }

    public String getQid(){
        int index=vpAnswer.getCurrentItem();
        PaperTestEntity entity=mistakeRedoAdapter.getDatas().get(index);
        if (entity!=null){
            List<PaperTestEntity> list=entity.getQuestions().getChildren();
            if (list!=null&&list.size()>0){
                BaseQuestionFragment fragment=mistakeRedoAdapter.getFragmentAtNow();
                ViewPager pager=fragment.getViewPager();
                if (pager==null){
                    return "";
                }else {
                    int position=pager.getCurrentItem();
                    if (position>list.size()-1){
                        return "";
                    }
                    String qid=list.get(position).getQid()+"";
                    return qid;
                }
            }
            return entity.getQid()+"";
        }
        return "";
    }

    private boolean checkIsHaveAnswer(){
        List<PaperTestEntity> list=mistakeRedoAdapter.getDatas();
        int x=0;
        int y=0;
        for (int i=0;i<list.size();i++){
            PaperTestEntity entity=list.get(i);
            MistakeRedoCardBean.Mdata mdata=mistakeRedoCardBean.getData().get(x);

            if (entity!=null){
                if (entity.getQuestions().isHaveAnser()){
                    mdata.getWqtypes().set(y,MistakeRedoCardBean.TYPE_HASANSWER);
                }else {
                    mdata.getWqtypes().set(y,MistakeRedoCardBean.TYPE_NOANSWER);
                }

            }
            y++;
            if (y==mdata.getWqtypes().size()){
                x++;
                y=0;
            }
        }
        return false;
    }

    private void addFragment(){
        content_answer_card.setVisibility(View.VISIBLE);
        FragmentManager manager=getSupportFragmentManager();
        Fragment fragment=manager.findFragmentByTag(TAG);
        if (fragment==null) {
            FragmentTransaction ft = manager.beginTransaction();
            MistakeRedoCardFragment cardFragment = new MistakeRedoCardFragment();
            Bundle args = new Bundle();
            args.putSerializable("MistakeRedoCardBean", mistakeRedoCardBean);
            cardFragment.setArguments(args);
            ft.add(R.id.content_answer_card, cardFragment, TAG);
            ft.commit();
        }else {
            MistakeRedoCardFragment cardFragment= (MistakeRedoCardFragment) fragment;
            cardFragment.onRefresh();
        }
    }

    public void removeFragment(){
        content_answer_card.setVisibility(View.GONE);
    }

    public void setViewPagerCurrent(int item){
        vpAnswer.setCurrentItem(item-1,false);
        content_answer_card.setVisibility(View.GONE);
        FragmentManager manager=getSupportFragmentManager();
        Fragment fragment=manager.findFragmentByTag(TAG);
        if (fragment!=null){
            MistakeRedoCardFragment cardFragment= (MistakeRedoCardFragment) fragment;
            cardFragment.onLoadFinish();
        }
    }

    @Override
    protected void initData() {

//        dataSources = JSON.parseObject(ss, SubjectExercisesItemBean.class);

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
                handle = new MyHandle();
                mistakeRedoAdapter.setDataSourcesFirst(dataSources, wrongCounts);
                vpAnswer.setAdapter(mistakeRedoAdapter);
                mistakeRedoAdapter.setViewPager(vpAnswer);
                mistakeRedoAdapter.setLoadListener(this);
                pageCount = mistakeRedoAdapter.getCount();

                boolean flag=setIndex();
                vpAnswer.post(new Runnable() {
                    @Override
                    public void run() {
                        vpAnswer.setCurrentItem(index-1,false);
                    }
                });

                ivBack.setOnClickListener(this);
                ivAnswerCard.setOnClickListener(this);
                ivFavCard.setOnClickListener(this);
                if (!TextUtils.isEmpty(dataSources.getData().get(0).getName())) {
                    tvQuestionTitle.setText(dataSources.getData().get(0).getName());
                }
            }
        }
        setReportError();

        vpAnswer.setCurrentItem(position,false);
        tvPagerIndex.setText(String.valueOf(position + 1));
        tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), (wrongCounts) + ""));
        tvToptext.setText(this.getResources().getString(R.string.mistake_redo));
        tvToptext.setCompoundDrawables(null, null, null, null);
        ivAnswerCard.setBackgroundResource(R.drawable.selector_mistake_question_card);
        answer_view_type.setBackgroundResource(R.drawable.mistake_redo);
    }

    private boolean setIndex(){
        List<PaperTestEntity> list=dataSources.getData().get(0).getPaperTest();
        for (PaperTestEntity entity:list){
            if (entity!=null&&entity.getRedostatus()==0){
                index=entity.getWqnumber();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean getCurrent(int fragment_ID) {
        return super.getCurrent(fragment_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                /**
                 * 下面逻辑有问题，datasources这个对象不能直接使用，不过错题重做暂时不需要这个功能，留待后用
                 * */
//                if (requestCode == TO_CARD) {
//                    //答题卡返回
//                    int index = data.getIntExtra("index", -1);
//                    if (index != -1) {
//                        vpAnswer.setCurrentItem(index);
//                    }
//                } else {
//                    BaseQuestionFragment currentFragment = (BaseQuestionFragment) mistakeRedoAdapter.getItem(currentIndex);
//                    if (dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getChildren() != null && !dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getChildren().isEmpty())
//                        if (currentFragment != null && currentFragment.getChildFragment() != null) {
//                            currentFragment = (BaseQuestionFragment) currentFragment.getChildFragment();
//                        }
//                    if (currentFragment != null && currentFragment instanceof SubjectiveQuestionFragment) {
//                        currentFragment.onActivityResult(requestCode, resultCode, data);
//                    }
//                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onLoadListener(int page) {
        if (addClassTask!=null){
            try {
                addClassTask.cancel();
                addClassTask=null;
            }catch (Exception e){}
        }
        requestMistakeRedo(page);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (content_answer_card!=null&&content_answer_card.getVisibility()==View.VISIBLE){
            content_answer_card.setVisibility(View.GONE);
        }else {
            if (dialog == null) {
                initPopup();
            } else {
                setDialogShow();
            }
        }
    }

    public void setDialogShow(){
        dialog.setStageDialogCallBack(back);
        List<PaperTestEntity> list=mistakeRedoAdapter.getDatas();
        dialog.setQuestionNumber(getTatleNumber(list),getRightNumber(list),getFailNumber(list));
        dialog.show();

    }

    private MistakeRedoDialog.StageDialogCallBack back=new MistakeRedoDialog.StageDialogCallBack() {
        @Override
        public void stage() {

        }

        @Override
        public void cancel() {
            requestdoWork();
//            MistakeRedoActivity.this.finish();
        }
    };

    private void requestdoWork(){
        mRootView.loading(true);
        boolean flag=initdoworkData();
        MistakeDoWorkTask doWorkTask=new MistakeDoWorkTask(this, stageId, subjectId, lastWqid, lastWqnumber, deleteWqidList, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                mRootView.finish();
                MistakeDoWorkBean doWorkBean= (MistakeDoWorkBean) result;
                if (doWorkBean!=null&&doWorkBean.getStatus()!=null&&doWorkBean.getStatus().getCode()==0){
                    MistakeRedoActivity.this.finish();
                }else {
                    Util.showToast(R.string.data_erro);
                }
            }

            @Override
            public void dataError(int type, String msg) {
                mRootView.finish();
                if (!NetWorkTypeUtils.isNetAvailable()) {
                    Util.showToast(R.string.server_connection_erro);
                }else {
                    Util.showToast(R.string.data_erro);
                }
            }
        });
        doWorkTask.start();
    }

    private boolean initdoworkData() {
        List<PaperTestEntity> list=mistakeRedoAdapter.getDatas();
        for (int i=0;i<list.size();i++){
            PaperTestEntity entity=list.get(i);
            if (entity!=null&&entity.getQuestions().getAnswerIsRight()!=0){
                lastWqid=entity.getWqid();
                lastWqnumber=entity.getWqnumber()+"";
            }
            if (entity!=null&&entity.getQuestions().getType()==QuestionEntity.TYPE_DELETE_END){
                deleteWqidList=deleteWqidList+entity.getWqid()+",";
            }
        }
        if (deleteWqidList.length()>0) {
            deleteWqidList=deleteWqidList.substring(0, deleteWqidList.length() - 1);
        }
        return false;
    }

    private void initPopup(){
        dialog=new MistakeRedoDialog(this, back);
        dialog.show();
        List<PaperTestEntity> list=mistakeRedoAdapter.getDatas();
        dialog.setQuestionNumber(getTatleNumber(list),getRightNumber(list),getFailNumber(list));
    }

    private int getTatleNumber(List<PaperTestEntity> list){
        int t=0;
        for (PaperTestEntity entity:list){
             if (entity!=null&&entity.getQuestions().isHaveAnser()){
                t++;
            }
        }
        return t;
    }

    private int getRightNumber(List<PaperTestEntity> list){
        int r=0;
        for (PaperTestEntity entity:list){
            if (entity!=null&&entity.getQuestions().getAnswerIsRight()== QuestionEntity.ANSWER_RIGHT){
                r++;
            }
        }
        return r;
    }

    private int getFailNumber(List<PaperTestEntity> list){
        int f=0;
        for (PaperTestEntity entity:list){
            if (entity!=null&&entity.getQuestions().getAnswerIsRight()==QuestionEntity.ANSWER_FAIL){
                f++;
            }
        }
        return f;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null){
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            dialog=null;
        }
    }

    private void requestMistakeRedo(final int page){
//        mRootView.loading(true);
        addClassTask=new RequestMisRedoAddClassTask(this, page+"", stageId, subjectId, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
//                mRootView.loading(false);
                SubjectExercisesItemBean dataSources = (SubjectExercisesItemBean) result;
                if (dataSources!=null&&dataSources.getStatus().getCode()==0&&dataSources.getData()!=null&&dataSources.getData().size()>0) {
                    mistakeRedoAdapter.addDataSources(dataSources, page);
                    mistakeRedoAdapter.notifyDataSetChanged();
                }else {
                    Util.showToast(R.string.data_erro);
                }
            }

            @Override
            public void dataError(int type, String msg) {
//                mRootView.loading(false);
                if (!NetWorkTypeUtils.isNetAvailable()) {
                    Util.showToast(R.string.server_connection_erro);
                }else {
                    Util.showToast(R.string.data_erro);
                }
            }
        });
        addClassTask.start();
    }

    private static class MyHandle extends Handler {

        private WeakReference<RelativeLayout> weak_pop;

        MyHandle() {
        }

        public void setPopupWindow(RelativeLayout window) {
            weak_pop = new WeakReference<>(window);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case 0:
//                    SubjectExercisesItemBean dataSources = (SubjectExercisesItemBean) msg.obj;
//                    if (weak != null && weak.get() != null) {
//                        MistakeRedoAdapter adapter = weak.get();
//                        adapter.addDataSources(dataSources, page);
//                        adapter.notifyDataSetChanged();
//                    }
//                    break;
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
}
