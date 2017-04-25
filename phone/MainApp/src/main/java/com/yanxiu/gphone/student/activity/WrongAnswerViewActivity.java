package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.bean.ExercisesDataEntity;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.fragment.question.BaseQuestionFragment;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestDelMistakeTask;
import com.yanxiu.gphone.student.requestTask.RequestWrongAllQuestionTask;
import com.yanxiu.gphone.student.requestTask.RequestWrongDetailsTask;
import com.yanxiu.gphone.student.requestTask.RequestWrongQuestionTask;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/7/28.
 */
public class WrongAnswerViewActivity extends BaseAnswerViewActivity {
    public final static int WRONG_ANSWER_REQUESTCODE = 0x100;
    protected RequestWrongAllQuestionTask mRequestWrongQuestionTask;
    protected PaperTestEntity mPaperTestEntity;
    protected int pageIndex = 0;
    protected String stageId;
    protected String subjectId;
    protected String editionId;
    protected String volumeId;
    protected String chapterId;
    protected String sectionId;
    protected String questionId;
    protected int isChapterSection = 0;
    protected String uniteId;
    protected int currentPageIndex = 1;
    protected int delQueNum = 0;
    protected boolean isNetData = true;

    private ArrayList<Integer> qids=new ArrayList<>();
    private ArrayList<Integer> qids_catch=new ArrayList<>();

    protected int comeFrom = 0;
    protected int position;

    protected boolean deleteAction = false;
    protected ArrayList<String> delQuestionTmpList = new ArrayList<String>();
    protected int wrongCounts;
    protected boolean isGetDataNow=true;
    private RequestWrongDetailsTask wrongDetailsTask;

    public static void launch(Activity context, SubjectExercisesItemBean bean, String subjectId, String editionId, String volumeId, String chapterId, String sectionId, String uniteId, int isChapterSection, boolean isNetData) {
        Intent intent = new Intent(context, WrongAnswerViewActivity.class);
        QuestionUtils.initDataWithAnswer(bean);
        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("editionId", editionId);
        intent.putExtra("volumeId", volumeId);
        intent.putExtra("chapterId", chapterId);
        intent.putExtra("sectionId", sectionId);
        intent.putExtra("uniteId", uniteId);
        intent.putExtra("isChapterSection", isChapterSection);
        intent.putExtra("isNetData", isNetData);
        context.startActivityForResult(intent, WRONG_ANSWER_REQUESTCODE);
    }

    public static void launch(Activity context,String subjectId, int pagerIndex, int childIndex, int comeFrom, String wrongCount, int position) {
        Intent intent = new Intent(context, WrongAnswerViewActivity.class);
//        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("pagerIndex", pagerIndex);
        intent.putExtra("childIndex", childIndex);
        intent.putExtra("comeFrom", comeFrom);
        intent.putExtra("position", position);
        intent.putExtra("wrongCount", wrongCount);
        context.startActivityForResult(intent, YanXiuConstant.LAUNCHER_FROM_MISTAKE);
    }

    public static void launch(Activity context,ArrayList<Integer> qids,ArrayList<Integer> qids_catch,  String subjectId, int pagerIndex, int childIndex, int comeFrom, String wrongCount, int position) {
        Intent intent = new Intent(context, WrongAnswerViewActivity.class);
//        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("pagerIndex", pagerIndex);
        intent.putExtra("childIndex", childIndex);
        intent.putExtra("comeFrom", comeFrom);
        intent.putIntegerArrayListExtra("qids",qids);
        intent.putIntegerArrayListExtra("qids_catch",qids_catch);
        intent.putExtra("position", position);
        intent.putExtra("wrongCount", wrongCount);
        context.startActivityForResult(intent, YanXiuConstant.LAUNCHER_FROM_MISTAKE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreat2();
    }

    protected void onCreat2(){
        stageId = LoginModel.getUserinfoEntity().getStageid() + "";
        subjectId = getIntent().getStringExtra("subjectId");
        editionId = getIntent().getStringExtra("editionId");
        volumeId = getIntent().getStringExtra("volumeId");
        chapterId = getIntent().getStringExtra("chapterId");
        sectionId = getIntent().getStringExtra("sectionId");
        uniteId = getIntent().getStringExtra("uniteId");
        isChapterSection = getIntent().getIntExtra("isChapterSection", 0);
        isNetData = getIntent().getBooleanExtra("isNetData", true);
        comeFrom = getIntent().getIntExtra("comeFrom", 0);
        position = getIntent().getIntExtra("position", 0);
        String wrongCount = getIntent().getStringExtra("wrongCount");
        try {
            wrongCounts=Integer.parseInt(wrongCount);
            qids=getIntent().getIntegerArrayListExtra("qids");
            qids_catch=getIntent().getIntegerArrayListExtra("qids_catch");
        }catch (Exception e){

        }
        initView();
        initData();
    }

    @Override
    protected void initView() {
        super.initView();
        adapter.setWrongCount(wrongCounts);
    }

    @Override
    public void initData() {
        super.initData();
        //if (dataSources != null && dataSources.getData() != null) {
//            if (position+1 == wrongCounts-delQueNum) {
//                btnNextQuestion.setVisibility(View.GONE);
//            } else {
//                btnNextQuestion.setVisibility(View.VISIBLE);
//            }
        initData2();
        }

    protected void initData2(){
        pageIndex = position;
        vpAnswer.setCurrentItem(position);
        tvPagerIndex.setText(String.valueOf(position + 1));
        //tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(adapter.getTotalCount())));
        tvToptext.setShadowLayer(2, 0, 5, this.getResources().getColor(R.color.color_33ffff));
        tvToptext.setTextColor(this.getResources().getColor(R.color.color_006666));

        tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), (wrongCounts-delQueNum)+""));
        tvToptext.setText(this.getResources().getString(R.string.questiong_resolution));
        tvToptext.setCompoundDrawables(null, null, null, null);
//            tvAnswerCard.setVisibility(View.GONE);
        ivAnswerCard.setBackgroundResource(R.drawable.selector_answer_delete);
        answer_view_type.setBackgroundResource(R.drawable.answer_report);
        //}
        adapter.setOnMoveListener(moveListener);
    }

    @Override
    protected void initDataSource() {
        if(comeFrom == MistakeAllActivity.WRONG_LIST){
            dataSources = MistakeAllActivity.subjectExercisesItemBeanIntent;
        }else if(comeFrom == MistakeDetailsActivity.WRONG_DETAIL){
            dataSources = MistakeDetailsActivity.subjectExercisesItemBeanIntent;
        }else {
            super.initDataSource();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LogInfo.log("geny", "position" + position + "----positionOffset" + positionOffset + "----positionOffsetPixels" + positionOffsetPixels);

    }

    public void method() throws Exception {};

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
//        if(vpAnswer.getCurrentItem() == pageCount - delQueNum - 1){
//            btnNextQuestion.setVisibility(View.GONE);
//        }else{
//            btnNextQuestion.setVisibility(View.VISIBLE);
//        }

//        List<Fragment> list=((AnswerAdapter)vpAnswer.getAdapter()).getmFragments();
//        int sumIndex = 0;
//        for (int i=0;i<position;i++){
//            BaseQuestionFragment fragment1= (BaseQuestionFragment) list.get(i);
//            sumIndex = sumIndex + fragment1.getChildCount();
//        }

        /**按牛的逻辑需要去掉这个页码显示问题*/
//        if (nextPager_onclick == 0 || ((BaseQuestionFragment) list.get(position)).getChildCount() == 1) {
//            tvPagerIndex.setText(String.valueOf(sumIndex + 1));
//        } else {
//            tvPagerIndex.setText(String.valueOf(sumIndex + ((BaseQuestionFragment) list.get(position)).getChildCount()));
//            nextPager_onclick = 0;
//        }

    }

    AnswerAdapter.OnMoveListener moveListener=new AnswerAdapter.OnMoveListener() {
        @Override
        public void onMove(int position) {
            if (position!=pageIndex) {
                List<Fragment> list = ((AnswerAdapter) vpAnswer.getAdapter()).getmFragments();
                BaseQuestionFragment fragment = (BaseQuestionFragment) list.get(vpAnswer.getCurrentItem());
                fragment.setWrongQuestionTitle(position + 1 + "", String.valueOf((wrongCounts - delQueNum)));

                tvPagerIndex.setText(String.valueOf(position + 1));

                tvPagerCount.setText(" / " + String.format(WrongAnswerViewActivity.this.getResources().getString(R.string.pager_count), String.valueOf((wrongCounts - delQueNum))));
            }
            pageIndex = position;
            int currentTotal = adapter.getCount();
            LogInfo.log("haitian", "currentTotal =" + currentTotal + "   position=" + position + "   currentTotal - position - 1 - delQueNum="
                    + (currentTotal - position - 1 - delQueNum) + " adapter.getCount() - position - 1=" + (adapter.getCount() - position - 1));
            if (wrongCounts-delQueNum > currentTotal && (adapter.getCount() - position - 1) < 2&&isGetDataNow) {
                isGetDataNow=false;
                String currentId = null;
                try{
                    int size = dataSources.getData().get(0).getPaperTest().size();
                    PaperTestEntity mPaperTestEntity = dataSources.getData().get(0).getPaperTest().get(size - 1);
                    currentId = mPaperTestEntity.getWqid()+"";
                } catch (Exception e){
                }
                LogInfo.log("haitian", "onPageSelected currentId ="+currentId);
                //requestWrongQuestion(subjectId, editionId, volumeId, chapterId, sectionId, currentPageIndex + 1, currentId);
                requestWrongAllQuestion(subjectId, currentPageIndex + 1, currentId);
            }else if ((adapter.getCount() - position - 1) >2){
                isGetDataNow=true;
            }
        }
    };

    public void setIndexFromRead(int position){
        LogInfo.log("TTTT", "test"+position);
        /**按牛的逻辑需要去掉这个页码显示问题*/
//        tvPagerIndex.setText(position+"/"+String.valueOf((wrongCounts - delQueNum)));
    }

    public void selectViewPager(){
        LogInfo.log("geny","selectViewPager " + (vpAnswer.getCurrentItem() + 1));
        vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() + 1));
    }

    private void requestWrongAllQuestion(final String subjectId, final int currentPage, final String currentId) {
//        isGetDataNow=false;
        cancelWrongQuestionTask();
        if (!isNetData) {
            new YanxiuSimpleAsyncTask<SubjectExercisesItemBean>(this) {
                @Override
                public SubjectExercisesItemBean doInBackground() {
                    SubjectExercisesItemBean mBean = null;
                    try {
                        ArrayList<ExercisesDataEntity> data = null;
                        ExercisesDataEntity mExercisesDataEntity = PublicErrorQuestionCollectionBean.findExercisesDataEntityWithAll(stageId,
                                subjectId, (currentPage - 1) * YanXiuConstant
                                        .YX_PAGESIZE_CONSTANT);
                        mBean = new SubjectExercisesItemBean();
                        data = new ArrayList<ExercisesDataEntity>();
                        data.add(mExercisesDataEntity);
                        mBean.setData(data);
                    } catch (Exception e) {
                    }
                    return mBean;
                }

                @Override
                public void onPostExecute(SubjectExercisesItemBean result) {
//                    isGetDataNow=true;
                    if (result != null && result.getData() != null) {
                        QuestionUtils.initDataWithAnswer(result);
                        currentPageIndex++;
                        dataSources.getData().get(0).getPaperTest().addAll(result.getData().get(0).getPaperTest());
                        adapter.addDataSourcesMore(result.getData().get(0).getPaperTest());
                    } else {
                        if (!NetWorkTypeUtils.isNetAvailable()) {
                            Util.showToast(R.string.server_connection_erro);
                        }
                    }
                }
            }.start();
        } else {

            if (qids!=null&&qids.size()>0) {
                if (wrongDetailsTask != null) {
                    wrongDetailsTask.cancel();
                    wrongDetailsTask = null;
                }
                ArrayList<Integer> datas = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    if (qids_catch.size() > 0) {
                        datas.add(qids_catch.get(0));
                        qids_catch.remove(0);
                    }
                }

                wrongDetailsTask = new RequestWrongDetailsTask(this, subjectId, datas, pageIndex, mWrongQuesAsyncCallBack);
                wrongDetailsTask.start();
            }else {
                mRequestWrongQuestionTask = new RequestWrongAllQuestionTask(WrongAnswerViewActivity.this, stageId,
                        subjectId, currentPage, currentId, 2, mWrongQuesAsyncCallBack);
                mRequestWrongQuestionTask.start();

            }

            /*mRequestWrongQuestionTask = new RequestWrongQuestionTask(WrongAnswerViewActivity.this, stageId,
                    subjectId, editionId, chapterId, sectionId, volumeId, currentPage, currentId, uniteId, isChapterSection, mWrongQuesAsyncCallBack);
            mRequestWrongQuestionTask.start();*/
        }
    }

    private void getDelTaskInfo(int position) {
        if (dataSources != null && dataSources.getData() != null) {
            if (dataSources.getData().get(0) != null && dataSources.getData().get(0).getPaperTest() != null && dataSources.getData().get(0).getPaperTest().size() > position) {
                mPaperTestEntity = dataSources.getData().get(0).getPaperTest().get(position);
                if (mPaperTestEntity != null) {
                    questionId = mPaperTestEntity.getQid() + "";
                }
            } else {
                questionId = null;
                mPaperTestEntity = null;
            }
        } else {
            questionId = null;
            mPaperTestEntity = null;
        }
    }

    private void requestWrongQuestion(final String subjectId, final String editionId, final String volumeId, final String chapterId, final String
            sectionId, final int currentPage, final String currentId) {
        cancelWrongQuestionTask();
        if (!isNetData) {
            new YanxiuSimpleAsyncTask<SubjectExercisesItemBean>(this) {
                @Override
                public SubjectExercisesItemBean doInBackground() {
                    SubjectExercisesItemBean mBean = null;
                    try {
                        ArrayList<ExercisesDataEntity> data = null;
                        ExercisesDataEntity mExercisesDataEntity = PublicErrorQuestionCollectionBean.findExercisesDataEntityWithChapter(stageId,
                                subjectId, editionId, volumeId, chapterId, sectionId, uniteId, isChapterSection, (currentPage - 1) * YanXiuConstant
                                        .YX_PAGESIZE_CONSTANT);
                        mBean = new SubjectExercisesItemBean();
                        data = new ArrayList<ExercisesDataEntity>();
                        data.add(mExercisesDataEntity);
                        mBean.setData(data);
                    } catch (Exception e) {
                    }
                    return mBean;
                }

                @Override
                public void onPostExecute(SubjectExercisesItemBean result) {
                    if (result != null && result.getData() != null) {
                        QuestionUtils.initDataWithAnswer(result);
                        currentPageIndex++;
                        dataSources.getData().get(0).getPaperTest().addAll(result.getData().get(0).getPaperTest());
                        adapter.addDataSourcesMore(result.getData().get(0).getPaperTest());
                    } else {
                        if (!NetWorkTypeUtils.isNetAvailable()) {
                            Util.showToast(R.string.server_connection_erro);
                        }
                    }
                }
            }.start();
        } else {
            if (qids!=null&&qids.size()>0) {
                if (wrongDetailsTask != null) {
                    wrongDetailsTask.cancel();
                    wrongDetailsTask = null;
                }
                ArrayList<Integer> datas = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    if (qids_catch.size() > 0) {
                        datas.add(qids_catch.get(0));
                        qids_catch.remove(0);
                    }
                }

                wrongDetailsTask = new RequestWrongDetailsTask(this, subjectId, datas, pageIndex, mWrongQuesAsyncCallBack);
                wrongDetailsTask.start();
            }else {
                mRequestWrongQuestionTask = new RequestWrongAllQuestionTask(WrongAnswerViewActivity.this, stageId,
                        subjectId, currentPage, currentId, 2, mWrongQuesAsyncCallBack);
                mRequestWrongQuestionTask.start();

            }
        }
    }

    private void cancelWrongQuestionTask() {
        if (mRequestWrongQuestionTask != null) {
            mRequestWrongQuestionTask.cancel();
        }
        mRequestWrongQuestionTask = null;
    }

    private AsyncCallBack mWrongQuesAsyncCallBack = new AsyncCallBack() {
        @Override
        public void update(YanxiuBaseBean result) {
//            isGetDataNow=true;
            SubjectExercisesItemBean subjectExercisesItemBean = (SubjectExercisesItemBean) result;
            if (subjectExercisesItemBean.getData() != null && subjectExercisesItemBean.getData().size() >= 1) {
                currentPageIndex++;

                QuestionUtils.initDataWithAnswer(subjectExercisesItemBean);
                QuestionUtils.CleanData(subjectExercisesItemBean.getData().get(0).getPaperTest());
                QuestionUtils.settingAnswer(subjectExercisesItemBean);

                List<PaperTestEntity> paperTestEntities=subjectExercisesItemBean.getData().get(0).getPaperTest();
                dataSources.getData().get(0).getPaperTest().addAll(paperTestEntities);
                adapter.addDataSourcesMore(paperTestEntities);
                if (paperTestEntities!=null&&paperTestEntities.size()>0){
                    setNextShow();
                }
            } else {
                if (!NetWorkTypeUtils.isNetAvailable()) {
                    Util.showUserToast(getString(R.string.public_loading_net_null_errtxt), null, null);
                }else {
                    Util.showToast(R.string.public_loading_data_null);
                }
            }
        }
        @Override
        public void dataError(int type, String msg) {
//            isGetDataNow=true;
            if (TextUtils.isEmpty(msg)) {
                Util.showToast(R.string.server_connection_erro);
            } else {
                Util.showToast(msg);
            }
        }
    };

    private void finishResult(boolean isDelAll){
        if(delQuestionTmpList != null && delQuestionTmpList.size() > 0){
            PublicErrorQuestionCollectionBean.deleteItemList(volumeId, delQuestionTmpList);
        }
        Intent intent = new Intent();
        intent.putExtra("deleteAction", deleteAction);
        intent.putExtra("isDelAll", isDelAll);
        intent.putExtra("chapterId", chapterId);
        intent.putExtra("sectionId", sectionId);
        intent.putExtra("uniteId", uniteId);
        intent.putExtra("isChapterSection", isChapterSection);
        intent.putExtra("wrongNum",wrongCounts-delQueNum);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finishResult(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public class WrongAnswerDeleteBean{
        public int position;
        public int id;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == ivBack) {
            finishResult(false);
//        }else if(v == btnLastQuestion){
//            if(vpAnswer.getCurrentItem() != 0){
//                vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() - 1));
//            }
//
//        }else if(v == btnNextQuestion){
//            if(vpAnswer.getCurrentItem() != pageCount - delQueNum - 1){
//                vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() + 1));
//            }
        }else if (v == ivAnswerCard) {
            getDelTaskInfo(pageIndex);
            LogInfo.log("haitian", "questionId =" + questionId);
            if (TextUtils.isEmpty(questionId)) {
                Util.showToast(R.string.select_location_data_error);
            } else {
                if (!NetWorkTypeUtils.isNetAvailable()) {
                    Util.showToast(R.string.public_loading_net_null_errtxt);
                    return;
                }
                mRootView.loading(true);
                RequestDelMistakeTask.requestTask(this, questionId, new AsyncCallBack() {
                    @Override
                    public void update(YanxiuBaseBean result) {
                        mRootView.finish();
                        deleteAction = true;
                        Util.showToast(R.string.mistake_question_del_done);
                        PublicErrorQuestionCollectionBean.updateDelData(questionId);
                        WrongAnswerDeleteBean deleteBean=new WrongAnswerDeleteBean();
                        if (dataSources.getData().get(0).getPaperTest() != null && dataSources.getData().get(0).getPaperTest().size() > pageIndex) {
                            PaperTestEntity entity = dataSources.getData().get(0).getPaperTest().get(pageIndex);
                            deleteBean.id=entity.getId();
                        }
                        deleteBean.position=pageIndex;
                        EventBus.getDefault().post(deleteBean);
                        deleteProcess(pageIndex);
                        try{
                            int currentPageCount = wrongCounts-delQueNum;
                            int size = dataSources.getData().get(0).getPaperTest().size();
                            if (size<6&&currentPageCount>size) {
                                PaperTestEntity mPaperTestEntity = dataSources.getData().get(0).getPaperTest().get(size - 1);
                                String currentId = mPaperTestEntity.getWqid() + "";
                                requestWrongAllQuestion(subjectId, currentPageIndex + 1, currentId);
                            }
                        } catch (Exception e){
                        }
                    }

                    @Override
                    public void dataError(int type, String msg) {
                        mRootView.finish();
                        if (!TextUtils.isEmpty(msg)) {
                            Util.showToast(msg);
                        } else {
                            Util.showToast(R.string.mistake_question_del_failed);
                        }
                    }
                });
            }
        }
    }

    private void deleteProcess(int index) {
        delQueNum++;
        int currentPageCount = wrongCounts-delQueNum;
        if (dataSources.getData().get(0).getPaperTest() != null && dataSources.getData().get(0).getPaperTest().size() > index) {
            dataSources.getData().get(0).getPaperTest().remove(index);
        }
//        if(currentPageCount == 1){
//            btnNextQuestion.setVisibility(View.GONE);
//            btnLastQuestion.setVisibility(View.GONE);
//        }
//
//        if(vpAnswer.getCurrentItem() == wrongCounts - delQueNum - 1){
//            btnNextQuestion.setVisibility(View.GONE);
//        }else{
//            btnNextQuestion.setVisibility(View.VISIBLE);
//        }

        delQuestionTmpList.add(questionId);
        adapter.deleteFragment(index);
        if(currentPageCount <= 0){
            finishResult(true);
        } else {
            LogInfo.log("haitian", "adapter.getCount()%10 ="+adapter.getCount()%YanXiuConstant
                    .YX_PAGESIZE_CONSTANT+"----currentPageCount="+currentPageCount +"---index="+index);
            if(adapter.getCount()%YanXiuConstant.YX_PAGESIZE_CONSTANT == 3 && currentPageCount > 3){
                int currentTotal = (currentPageIndex + 1)* YanXiuConstant.YX_PAGESIZE_CONSTANT;
                LogInfo.log("haitian", "currentTotal ="+(currentTotal-YanXiuConstant.YX_PAGESIZE_CONSTANT+1)+"----pageCount="+pageCount);
                if (wrongCounts >= (currentTotal-YanXiuConstant.YX_PAGESIZE_CONSTANT+1)) {
                    String currentId = null;
                    try{
                        int size = dataSources.getData().get(0).getPaperTest().size();
                        PaperTestEntity mPaperTestEntity = dataSources.getData().get(0).getPaperTest().get(size - 1);
                        currentId = mPaperTestEntity.getId()+"";
                    } catch (Exception e){

                    }
                    LogInfo.log("haitian", "deleteProcess currentId ="+currentId);
                    requestWrongQuestion(subjectId, editionId, volumeId, chapterId, sectionId, currentPageIndex + 1, currentId);
                }
            }
            int currentIndex = index;
            if (index > 0 && index == currentPageCount) {//最后一个
                currentIndex = index - 1;
            }
            vpAnswer.setCurrentItem(currentIndex);
            if (currentPageCount > 0) {
                tvPagerIndex.setText(String.valueOf(currentIndex + 1));
                tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(currentPageCount)));

                List<Fragment> list=((AnswerAdapter)vpAnswer.getAdapter()).getmFragments();
                BaseQuestionFragment fragment= (BaseQuestionFragment) list.get(vpAnswer.getCurrentItem());
                fragment.setWrongQuestionTitle(currentIndex+1+"",String.valueOf((wrongCounts - delQueNum)));

            } else {
                finishResult(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mRequestWrongQuestionTask!=null){
                if (!mRequestWrongQuestionTask.isCancelled()){
                    mRequestWrongQuestionTask.cancel();
                }
                mRequestWrongQuestionTask=null;
            }
        }catch (Exception e){}
    }
}