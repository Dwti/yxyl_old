package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.ExercisesDataEntity;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.PublicFavouriteQuestionBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.RequestBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.fragment.question.BaseQuestionFragment;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestDelFavouriteTask;
import com.yanxiu.gphone.student.requestTask.RequestSubmitFavouriteTask;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.StudentLoadingLayout;

/**
 * Created by Administrator on 2015/7/28.
 * 不可补做作业题目解析
 */
public class ResolutionAnswerViewActivity extends BaseAnswerViewActivity {
    private int pagerIndex;
    private int childIndex;
    private StudentLoadingLayout loadingLayout;


    private RequestSubmitFavouriteTask requestSubmitFavouriteTask;
    private RequestDelFavouriteTask requestDelFavouriteTask;

    private int comeFrom = 0;


    public static void launch(Activity context, SubjectExercisesItemBean bean, int comeFrom) {
        Intent intent = new Intent(context, ResolutionAnswerViewActivity.class);
        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("comeFrom", comeFrom);
        context.startActivity(intent);
    }

    public static void launch(Activity context, SubjectExercisesItemBean bean, int pagerIndex, int childIndex, int comeFrom) {
        Intent intent = new Intent(context, ResolutionAnswerViewActivity.class);
        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("pagerIndex", pagerIndex);
        intent.putExtra("childIndex", childIndex);
        intent.putExtra("comeFrom", comeFrom);
        context.startActivity(intent);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();

    }


    @Override
    public void initData(){
        super.initData();
        loadingLayout = (StudentLoadingLayout) findViewById(R.id.loading_layout);
        pagerIndex = getIntent().getIntExtra("pagerIndex", 0);
        childIndex = getIntent().getIntExtra("childIndex", 0);
        comeFrom = getIntent().getIntExtra("comeFrom", 0);
        LogInfo.log("geny-", "childIndex------" + childIndex + "----pagerIndex-----" + pagerIndex);
        if(dataSources != null && dataSources.getData() != null){
            tvPagerIndex.setText("1");
            tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(adapter.getTotalCount())));
            tvToptext.setText(this.getResources().getString(R.string.questiong_resolution));
            tvToptext.setShadowLayer(2, 0, 5, this.getResources().getColor(R.color.color_33ffff));
            tvToptext.setTextColor(this.getResources().getColor(R.color.color_006666));
            ivAnswerCard.setOnClickListener(this);
            ivAnswerCard.setBackgroundResource(R.drawable.selector_answer_uncollection);
            ivAnswerCard.setVisibility(View.GONE);
            setViewPagerPosition(pagerIndex, childIndex);

        }

        switch (comeFrom){
            case YanXiuConstant.HOMEWORK_REPORT:
                ivAnswerCard.setVisibility(View.GONE);
                break;
            case YanXiuConstant.HISTORY_REPORT:
            case YanXiuConstant.KPN_REPORT:
            case YanXiuConstant.INTELLI_REPORT:
                //ivAnswerCard.setVisibility(View.VISIBLE);
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
                        ivAnswerCard.setBackgroundResource(R.drawable.selector_answer_collection);
                        break;
                    case PaperTestEntity.UN_FAVORITE:
                        ivAnswerCard.setBackgroundResource(R.drawable.selector_answer_uncollection);
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
        vpAnswer.setCurrentItem(position);
        ((BaseQuestionFragment)adapter.getmFragments().get(position)).setChildPagerIndex(childPosition);
    }

    public void setIndexFromRead(int position){
//        tvPagerIndex.setText(position + "/" + adapter.getTotalCount());
        tvPagerIndex.setText(position + "");
    }


    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        QuestionEntity questionEntity = dataSources.getData().get(0).getPaperTest().get(position).getQuestions();
        tvPagerIndex.setText(questionEntity.getPositionForCard()+1+"");
        tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(adapter.getTotalCount())));

        switch (comeFrom){
            case YanXiuConstant.INTELLI_REPORT:
            case YanXiuConstant.KPN_REPORT:
            case YanXiuConstant.HISTORY_REPORT:
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == ivBack){
            this.finish();
        }else if(v == ivAnswerCard){

            if(AnswerReportActivity.dataSources != null && AnswerReportActivity.dataSources.getData() != null &&
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
            requestDelFavouriteTask = new RequestDelFavouriteTask(ResolutionAnswerViewActivity.this,
                                                                    String.valueOf(paperTestEntity.getQid()),

                    new AsyncCallBack(){

                        @Override
                        public void update(YanxiuBaseBean result) {
                            LogInfo.log("geny", "requestDelFavouriteTask 删除收藏成功" + ((RequestBean) result).getStatus());
                            AnswerReportActivity.dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getAnswerBean().setIsCollectionn(false);
                            AnswerReportActivity.dataSources.getData().get(0).getPaperTest().get(currentIndex).setIsfavorite(PaperTestEntity.UN_FAVORITE);
                            ivAnswerCard.setBackgroundResource(R.drawable.selector_answer_uncollection);
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
                            loadingLayout.setViewGone();
                        }

                        @Override
                        public void dataError(int type, String msg) {
                            if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                                Util.showToast(ResolutionAnswerViewActivity.this.getResources().getString(R.string.public_loading_net_null_errtxt));
                            }
                            LogInfo.log("geny", "requestDelFavouriteTask 删除收藏成功" + msg);
                            loadingLayout.setViewGone();
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

            requestSubmitFavouriteTask = new RequestSubmitFavouriteTask(ResolutionAnswerViewActivity.this,
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
                                    LogInfo.log("geny", "RequestSubmitFavouriteTask 收藏成功" + ((RequestBean) result).getStatus());
                                    AnswerReportActivity.dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getAnswerBean().setIsCollectionn(true);
                                    AnswerReportActivity.dataSources.getData().get(0).getPaperTest().get(currentIndex).setIsfavorite(PaperTestEntity.FAVORITE);
                                    LogInfo.log("geny", "paperTestEntity.getIsfavorite()------收藏成功---" + paperTestEntity.getIsfavorite() + paperTestEntity.toString());
                                    ivAnswerCard.setBackgroundResource(R.drawable.selector_answer_collection);
                                    loadingLayout.setViewGone();
                                }

                                @Override
                                public void dataError(int type, String msg) {
                                    if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                                        Util.showToast(ResolutionAnswerViewActivity.this.getResources().getString(R.string.public_loading_net_null_errtxt));
                                    }
                                    loadingLayout.setViewGone();
                                    LogInfo.log("geny", "RequestSubmitFavouriteTask 收藏成功" + msg);
                                }
                            });

        }
        requestSubmitFavouriteTask.start();
    }



}
