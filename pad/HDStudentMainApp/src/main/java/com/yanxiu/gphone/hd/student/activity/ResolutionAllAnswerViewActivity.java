package com.yanxiu.gphone.hd.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.ExercisesDataEntity;
import com.yanxiu.gphone.hd.student.bean.PaperTestEntity;
import com.yanxiu.gphone.hd.student.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.hd.student.bean.PublicFavouriteQuestionBean;
import com.yanxiu.gphone.hd.student.bean.RequestBean;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestDelFavouriteTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestDelMistakeTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestSubmitFavouriteTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestWrongAllQuestionTask;
import com.yanxiu.gphone.hd.student.utils.QuestionUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.view.StudentLoadingLayout;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/28.
 */
public class ResolutionAllAnswerViewActivity extends BaseAnswerViewActivity {
    private int pagerIndex;
    private int childIndex;
    private StudentLoadingLayout loadingLayout;
    private PaperTestEntity mPaperTestEntity;

    private RequestSubmitFavouriteTask requestSubmitFavouriteTask;
    private RequestDelFavouriteTask requestDelFavouriteTask;

    private int comeFrom = 0;
    private int position;
    private String wrongCount;

    private int currentPageIndex = 1;

    private RequestWrongAllQuestionTask mRequestWrongQuestionTask;
    private boolean isNetData = true;
    private String stageId;
    private String subjectId;
    private int pageIndex = 0;
    private String questionId;
    private boolean deleteAction = false;
    private int delQueNum = 0;
    private ArrayList<String> delQuestionTmpList = new ArrayList<String>();

    public static void launch(Activity context, SubjectExercisesItemBean bean, int comeFrom) {
        Intent intent = new Intent(context, ResolutionAllAnswerViewActivity.class);
        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("comeFrom", comeFrom);
        context.startActivity(intent);
    }

    public static void launch(Activity context, SubjectExercisesItemBean bean, int pagerIndex, int childIndex, int comeFrom) {
        Intent intent = new Intent(context, ResolutionAllAnswerViewActivity.class);
        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("pagerIndex", pagerIndex);
        intent.putExtra("childIndex", childIndex);
        intent.putExtra("comeFrom", comeFrom);
        context.startActivity(intent);
    }

    public static void launch(Activity context, SubjectExercisesItemBean bean, String subjectId, int pagerIndex, int childIndex, int comeFrom, String wrongCount, int position) {
        Intent intent = new Intent(context, ResolutionAllAnswerViewActivity.class);
        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("pagerIndex", pagerIndex);
        intent.putExtra("childIndex", childIndex);
        intent.putExtra("comeFrom", comeFrom);
        intent.putExtra("position", position);
        intent.putExtra("wrongCount", wrongCount);
        context.startActivityForResult(intent, YanXiuConstant.LAUNCHER_FROM_MISTAKE);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        vpAnswer.setCurrentItem(position-1);
        onPageSelected(position-1);
    }

    public int getComeFrom() {
        return comeFrom;
    }

    @Override
    public void initData(){
        super.initData();
        stageId = LoginModel.getUserinfoEntity().getStageid() + "";
        subjectId = getIntent().getStringExtra("subjectId");
        loadingLayout = (StudentLoadingLayout) findViewById(R.id.loading_layout);
        pagerIndex = getIntent().getIntExtra("pagerIndex", 0);
        childIndex = getIntent().getIntExtra("childIndex", 0);
        comeFrom = getIntent().getIntExtra("comeFrom", 0);
        position = getIntent().getIntExtra("position", 0);
        wrongCount = getIntent().getStringExtra("wrongCount");

        LogInfo.log("geny-", "childIndex------" + childIndex + "----pagerIndex-----" + pagerIndex);
        if(dataSources != null && dataSources.getData() != null){
//            tvPagerIndex.setText(1 + "/" + adapter.getTotalCount());
            pageCount = Integer.valueOf(wrongCount);
            tvPagerIndex.setText("1");
            currentPageIndex = pagerIndex;
            //tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(adapter.getTotalCount())));
            tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), pageCount-delQueNum));
            tvToptext.setText(this.getResources().getString(R.string.questiong_resolution));
            tvToptext.setShadowLayer(2, 0, 5, this.getResources().getColor(R.color.color_33ffff));
            tvToptext.setTextColor(this.getResources().getColor(R.color.color_006666));
//            tvToptext.setCompoundDrawables(null, null, null, null);
//            tvAnswerCard.setVisibility(View.GONE);
            ivAnswerCard.setOnClickListener(this);
            ivAnswerCard.setBackgroundResource(R.drawable.selector_answer_delete);
            setViewPagerPosition(pagerIndex, childIndex);
//            vpAnswer.setCurrentItem(pagerIndex);

        }

        switch (comeFrom){
            case YanXiuConstant.HOMEWORK_REPORT:
                ivAnswerCard.setVisibility(View.GONE);
                break;
            case YanXiuConstant.HISTORY_REPORT:
            case YanXiuConstant.KPN_REPORT:
            case YanXiuConstant.INTELLI_REPORT:
                ivAnswerCard.setVisibility(View.VISIBLE);
                //setIsFavorite();
                break;
        }


    }


    private void setIsFavorite(){
        if(AnswerReportActivity.dataSources != null && AnswerReportActivity.dataSources.getData() != null &&
                                                AnswerReportActivity.dataSources.getData().get(0) != null &&
                                                AnswerReportActivity.dataSources.getData().get(0).getPaperTest() != null &&
                                                !AnswerReportActivity.dataSources.getData().get(0).getPaperTest().isEmpty()){
            PaperTestEntity paperTestEntity = AnswerReportActivity.dataSources.getData().get(0).getPaperTest().get(currentIndex);
            if(paperTestEntity != null){
                LogInfo.log("geny", "paperTestEntity.getIsfavorite()------" + paperTestEntity.getIsfavorite());
                switch (paperTestEntity.getIsfavorite()){
                    case PaperTestEntity.FAVORITE:
                        ivAnswerCard.setBackgroundResource(R.drawable.selector_answer_delete);
                        break;
                    case PaperTestEntity.UN_FAVORITE:
                        ivAnswerCard.setBackgroundResource(R.drawable.selector_answer_delete);
                        break;
                }
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(AnswerReportActivity.dataSources != null && AnswerReportActivity.dataSources.getData() != null &&
                                                AnswerReportActivity.dataSources.getData().get(0) != null &&
                                                AnswerReportActivity.dataSources.getData().get(0).getPaperTest() != null &&
                                                !AnswerReportActivity.dataSources.getData().get(0).getPaperTest().isEmpty()) {
//            String stageId, String subjectId, String beditionId, String volumeId, String chapterId, String sectionId, String questionId
//            RequestFeedBackTask.startFeedBack(BaseAnswerViewActivity.this, dataSources.getData().get(0).getPaperTest().get(currentIndex).getQid() + "");
            ExercisesDataEntity exercisesDataEntity = AnswerReportActivity.dataSources.getData().get(0);

            int ptype = AnswerReportActivity.dataSources.getData().get(0).getPtype();

            switch (ptype){
                case YanXiuConstant.INTELLI_REPORT:
                    AnswerReportActivity.dataSources.getData().get(0).setIsChapterSection(0);
                    break;
                case YanXiuConstant.KPN_REPORT:
                    AnswerReportActivity.dataSources.getData().get(0).setIsChapterSection(1);
                    break;
            }

            PublicFavouriteQuestionBean.saveFavouriteExercisesDataEntity(exercisesDataEntity);

        }

    }

    public void setViewPagerPosition(int position, int childPosition){
//        LogInfo.log("geny-", "position" + position + "----childPosition" + childPosition + "----childPosition" + childPosition);
        vpAnswer.setCurrentItem(position);

//        if(childPosition != -1){
//            Fragment fragment = adapter.getItem(position);
//            if(fragment instanceof ReadingQuestionsFragment){
//
//                //发送消息通知子viewpager滑到指定的位置
//                LogInfo.log("geny-", "position" + position + "----childPosition" + childPosition + "----childPosition" + childPosition);
//                EventBus.getDefault().post(new ChildIndexEvent(childPosition));
//            }
//        }
    }

    public void setIndexFromRead(int position){
//        LogInfo.log("geny-", "setIndexFromRead   position" + position);
        tvPagerIndex.setText(position + "/" + adapter.getTotalCount());
//        tvPagerIndex.setText((adapter.getIndexPage(currentIndex) + position) + "/" + adapter.getTotalCount());
    }


    @Override
    public void onPageSelected(int position) {
//        LogInfo.log("geny-", "onPageSelected   position" + position);
        super.onPageSelected(position);
        if (position+1 == pageCount-delQueNum) {
            btnNextQuestion.setVisibility(View.GONE);
        } else {
            btnNextQuestion.setVisibility(View.VISIBLE);
        }
        pageIndex = position;

//        Fragment fragment = adapter.getItem(position);
//        if(((PageIndex) fragment).getPageIndex() == 0){
//            if(fragment instanceof ReadingQuestionsFragment){
//                tvPagerIndex.setText((adapter.getIndexPage(pagerIndex) + childIndex) + "/" + adapter.getTotalCount());
//            }else{
//                tvPagerIndex.setText(adapter.getIndexPage(pagerIndex) + "/" + adapter.getTotalCount());
//            }
//        }else{
//            tvPagerIndex.setText(((PageIndex) fragment).getPageIndex() + "/"  + adapter.getTotalCount());
//        }
        tvPagerIndex.setText(String.valueOf(position + 1));
        //tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(adapter.getTotalCount())));
        tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), pageCount-delQueNum));

        switch (comeFrom){
            case YanXiuConstant.INTELLI_REPORT:
            case YanXiuConstant.KPN_REPORT:
            case YanXiuConstant.HISTORY_REPORT:
                //setIsFavorite();
                break;
        }

//        LogInfo.log("geny-", "((PageIndex)fragment).getPageIndex()   position" + ((PageIndex)fragment).getPageIndex());
        int currentTotal = currentPageIndex * YanXiuConstant.YX_PAGESIZE_CONSTANT;
        if (pageCount > currentTotal && (adapter.getCount() - position - 1) == 3) {
            String currentId = null;
            try{
                int size = dataSources.getData().get(0).getPaperTest().size();
                PaperTestEntity mPaperTestEntity = dataSources.getData().get(0).getPaperTest().get(size - 1);
                currentId = mPaperTestEntity.getId()+"";
            } catch (Exception e){
            }
            LogInfo.log("haitian", "onPageSelected currentId ="+currentId);
            requestWrongAllQuestion(subjectId, currentPageIndex + 1, currentId);
        }
    }

    private void requestWrongAllQuestion(final String subjectId, final int currentPage, final String currentId) {
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
                    if (result != null && result.getData() != null) {
                        QuestionUtils.initDataWithAnswer(result);
                        currentPageIndex++;
                        dataSources.getData().get(0).getPaperTest().addAll(result.getData().get(0).getPaperTest());
                        adapter.addDataSourcesMore(result.getData().get(0).getPaperTest());
                    } else {
                        if (NetWorkTypeUtils.isNetAvailable()) {
                            Util.showToast(R.string.server_connection_erro);
                        }
                    }
                }
            }.start();
        } else {
            mRequestWrongQuestionTask = new RequestWrongAllQuestionTask(ResolutionAllAnswerViewActivity.this, stageId,
                    subjectId, currentPage, currentId, 2, mWrongQuesAsyncCallBack);
            mRequestWrongQuestionTask.start();
            /*mRequestWrongQuestionTask = new RequestWrongQuestionTask(WrongAnswerViewActivity.this, stageId,
                    subjectId, editionId, chapterId, sectionId, volumeId, currentPage, currentId, uniteId, isChapterSection, mWrongQuesAsyncCallBack);
            mRequestWrongQuestionTask.start();*/
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
            SubjectExercisesItemBean subjectExercisesItemBean = (SubjectExercisesItemBean) result;
            if (subjectExercisesItemBean.getData() != null && subjectExercisesItemBean.getData().size() >= 1) {
                QuestionUtils.initDataWithAnswer(subjectExercisesItemBean);
                currentPageIndex++;
                dataSources.getData().get(0).getPaperTest().addAll(subjectExercisesItemBean.getData().get(0).getPaperTest());
                adapter.addDataSourcesMore(subjectExercisesItemBean.getData().get(0).getPaperTest());
            } else {
                Util.showToast(R.string.server_connection_erro);
            }
        }
        @Override
        public void dataError(int type, String msg) {
            if (TextUtils.isEmpty(msg)) {
                Util.showToast(R.string.server_connection_erro);
            } else {
                Util.showToast(msg);
            }
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        LogInfo.log("geny", "position" + position + "----positionOffset" + positionOffset + "----positionOffsetPixels" + positionOffsetPixels);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == ivBack){
            Intent intent = new Intent();
            intent.putExtra("wrongNum", pageCount-delQueNum);
            setResult(RESULT_OK, intent);
            this.finish();
        }else if(v == ivAnswerCard){

            /*if(AnswerReportActivity.dataSources != null && AnswerReportActivity.dataSources.getData() != null &&
                    AnswerReportActivity.dataSources.getData().get(0) != null &&
                    AnswerReportActivity.dataSources.getData().get(0).getPaperTest() != null &&
                    !AnswerReportActivity.dataSources.getData().get(0).getPaperTest().isEmpty()){
                PaperTestEntity paperTestEntity = AnswerReportActivity.dataSources.getData().get(0).getPaperTest().get(currentIndex);
                if(paperTestEntity != null){
                    switch (paperTestEntity.getIsfavorite()){
                        case PaperTestEntity.FAVORITE:
                            requestDelFavourite();
                            break;
                        case PaperTestEntity.UN_FAVORITE:
                            requestSubFavourite();
                            break;
                    }
                }

            }*/
            getDelTaskInfo(pageIndex);
            LogInfo.log("haitian", "questionId =" + questionId);
            if (TextUtils.isEmpty(questionId)) {
                Util.showToast(R.string.select_location_data_error);
            } else {
                if (NetWorkTypeUtils.isNetAvailable()) {
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

                        deleteProcess(pageIndex);
                        afterDeleteRequest();
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

    private void deleteProcess(int index) {
        delQueNum++;
        int currentPageCount = pageCount-delQueNum;
        if (dataSources.getData().get(0).getPaperTest() != null && dataSources.getData().get(0).getPaperTest().size() > index) {
            dataSources.getData().get(0).getPaperTest().remove(index);
        }

        if(currentPageCount == 1){
            btnNextQuestion.setVisibility(View.GONE);
            btnLastQuestion.setVisibility(View.GONE);
        }

        if(vpAnswer.getCurrentItem() == pageCount - delQueNum - 1){
            btnNextQuestion.setVisibility(View.GONE);
        }else{
            btnNextQuestion.setVisibility(View.VISIBLE);
        }

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
                if (pageCount >= (currentTotal-YanXiuConstant.YX_PAGESIZE_CONSTANT+1)) {
                    String currentId = null;
                    try{
                        int size = dataSources.getData().get(0).getPaperTest().size();
                        PaperTestEntity mPaperTestEntity = dataSources.getData().get(0).getPaperTest().get(size - 1);
                        currentId = mPaperTestEntity.getId()+"";
                    } catch (Exception e){

                    }
                    LogInfo.log("haitian", "deleteProcess currentId ="+currentId);
                    requestWrongAllQuestion(subjectId, currentPageIndex + 1, currentId);
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
            } else {
                finishResult(true);
            }
        }
    }

    private void requestDelFavourite(){

        if(requestDelFavouriteTask != null){
            requestDelFavouriteTask.cancel();
        }
        if(requestSubmitFavouriteTask != null){
            requestSubmitFavouriteTask.cancel();
        }

        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        if(dataSources != null && dataSources.getData() != null &&
                dataSources.getData().get(0) != null &&
                dataSources.getData().get(0).getPaperTest() != null &&
                !dataSources.getData().get(0).getPaperTest().isEmpty()){
//            String stageId, String subjectId, String beditionId, String volumeId, String chapterId, String sectionId, String questionId
//            RequestFeedBackTask.startFeedBack(BaseAnswerViewActivity.this, dataSources.getData().get(0).getPaperTest().get(currentIndex).getQid() + "");
            final ExercisesDataEntity exercisesDataEntity = dataSources.getData().get(0);
            final PaperTestEntity paperTestEntity = dataSources.getData().get(0).getPaperTest().get(currentIndex);
            requestDelFavouriteTask = new RequestDelFavouriteTask(ResolutionAllAnswerViewActivity.this,
                                                                    String.valueOf(paperTestEntity.getQid()),

                    new AsyncCallBack(){

                        @Override
                        public void update(YanxiuBaseBean result) {
                            if(((RequestBean)result).getStatus() != null && ((RequestBean)result).getStatus().getCode() == 0){
                                LogInfo.log("geny", "requestDelFavouriteTask 删除收藏成功" + ((RequestBean) result).getStatus());
                                AnswerReportActivity.dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getAnswerBean().setIsCollectionn(false);
                                AnswerReportActivity.dataSources.getData().get(0).getPaperTest().get(currentIndex).setIsfavorite(PaperTestEntity.UN_FAVORITE);
                                ivAnswerCard.setBackgroundResource(R.drawable.selector_answer_delete);
//                            (String stageId, String subjectId, String editionId, String volumeId, String chapterId, String sectionId,
//                                    String qid)


                                PublicFavouriteQuestionBean.deleteFavQuestion(exercisesDataEntity.getStageid(),
                                        exercisesDataEntity.getSubjectid(),
                                        exercisesDataEntity.getBedition(),
                                        exercisesDataEntity.getVolume(),
                                        exercisesDataEntity.getChapterid(),
                                        exercisesDataEntity.getSectionid(),
                                        exercisesDataEntity.getCellid(),
                                        String.valueOf(paperTestEntity.getQid()));
                            }else{
                                Util.showToast(ResolutionAllAnswerViewActivity.this.getResources().getString(R.string.fav_question_del_failed));
                            }

                            loadingLayout.setViewGone();
                        }

                        @Override
                        public void dataError(int type, String msg) {
                            loadingLayout.setViewGone();
                            if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                                Util.showToast(ResolutionAllAnswerViewActivity.this.getResources().getString(R.string.public_loading_net_null_errtxt));
                            }else{
                                Util.showToast(ResolutionAllAnswerViewActivity.this.getResources().getString(R.string.fav_question_del_failed));
                            }
                            LogInfo.log("geny", "requestDelFavouriteTask 删除收藏成功" + msg);
                        }
                    });
        }

        requestDelFavouriteTask.start();
    }

    private void requestSubFavourite(){
        if(requestDelFavouriteTask != null){
            requestDelFavouriteTask.cancel();
        }
        if(requestSubmitFavouriteTask != null){
            requestSubmitFavouriteTask.cancel();
        }

        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        if(dataSources != null && dataSources.getData() != null &&
                dataSources.getData().get(0) != null &&
                dataSources.getData().get(0).getPaperTest() != null &&
                !dataSources.getData().get(0).getPaperTest().isEmpty()){
//            String stageId, String subjectId, String beditionId, String volumeId, String chapterId, String sectionId, String questionId
//            RequestFeedBackTask.startFeedBack(BaseAnswerViewActivity.this, dataSources.getData().get(0).getPaperTest().get(currentIndex).getQid() + "");
            final ExercisesDataEntity exercisesDataEntity = dataSources.getData().get(0);
            final PaperTestEntity paperTestEntity = dataSources.getData().get(0).getPaperTest().get(currentIndex);

            requestSubmitFavouriteTask = new RequestSubmitFavouriteTask(ResolutionAllAnswerViewActivity.this,
                                                                        exercisesDataEntity.getStageid(),
                                                                        exercisesDataEntity.getSubjectid(),
                                                                        exercisesDataEntity.getBedition(),
                                                                        exercisesDataEntity.getVolume(),
                                                                        exercisesDataEntity.getChapterid(),
                                                                        exercisesDataEntity.getSectionid(),
                                                                        String.valueOf(paperTestEntity.getQid()),
                                                                        exercisesDataEntity.getCellid(),
                                                                        dataSources.getData().get(0).getPtype(),

                            new AsyncCallBack(){

                                @Override
                                public void update(YanxiuBaseBean result) {
                                    if(((RequestBean)result).getStatus() != null && ((RequestBean)result).getStatus().getCode() == 0){
                                        LogInfo.log("geny", "RequestSubmitFavouriteTask 收藏成功" + ((RequestBean) result).getStatus());
                                        AnswerReportActivity.dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getAnswerBean().setIsCollectionn(true);
                                        AnswerReportActivity.dataSources.getData().get(0).getPaperTest().get(currentIndex).setIsfavorite(PaperTestEntity.FAVORITE);
                                        LogInfo.log("geny", "paperTestEntity.getIsfavorite()------收藏成功---" + paperTestEntity.getIsfavorite() + paperTestEntity.toString());
                                        ivAnswerCard.setBackgroundResource(R.drawable.selector_answer_delete);
                                    }else{
                                        Util.showToast(ResolutionAllAnswerViewActivity.this.getResources().getString(R.string.fav_question_add_failed));
                                    }

                                    loadingLayout.setViewGone();
                                }

                                @Override
                                public void dataError(int type, String msg) {
                                    loadingLayout.setViewGone();
                                    if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                                        Util.showToast(ResolutionAllAnswerViewActivity.this.getResources().getString(R.string.public_loading_net_null_errtxt));
                                    }else{
                                        Util.showToast(ResolutionAllAnswerViewActivity.this.getResources().getString(R.string.fav_question_add_failed));
                                    }
                                    LogInfo.log("geny", "RequestSubmitFavouriteTask 收藏成功" + msg);
                                }
                            });

        }
        requestSubmitFavouriteTask.start();
    }

    private void finishResult(boolean isDelAll){
        if(delQuestionTmpList != null && delQuestionTmpList.size() > 0){
            PublicErrorQuestionCollectionBean.deleteItemList("0", delQuestionTmpList);
        }
        Intent intent = new Intent();
        intent.putExtra("deleteAction", deleteAction);
        intent.putExtra("isDelAll", isDelAll);
        intent.putExtra("wrongNum", pageCount-delQueNum);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("wrongNum", pageCount - delQueNum);
            setResult(-1, intent);
            return super.onKeyDown(keyCode, event);
        } else
            return super.onKeyDown(keyCode, event);
    }

    private void afterDeleteRequest () {
        int currentTotal = currentPageIndex  * YanXiuConstant.YX_PAGESIZE_CONSTANT;
        if ((pageCount > currentTotal && (adapter.getCount() - position - 1) == 3)) {
            String currentId = null;
            try {
                int size = dataSources.getData().get(0).getPaperTest().size();
                PaperTestEntity mPaperTestEntity = dataSources.getData().get(0).getPaperTest().get(size - 1);
                currentId = mPaperTestEntity.getId() + "";
            } catch (Exception e) {
            }
            LogInfo.log("haitian", "onPageSelected currentId =" + currentId);
            requestWrongAllQuestion(subjectId, currentPageIndex + 1, currentId);
        }
    }
}
