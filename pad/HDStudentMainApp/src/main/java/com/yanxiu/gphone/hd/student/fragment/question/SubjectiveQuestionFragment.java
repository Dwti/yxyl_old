package com.yanxiu.gphone.hd.student.fragment.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.LocalPhotoViewActivity;
import com.yanxiu.gphone.hd.student.bean.AnswerBean;
import com.yanxiu.gphone.hd.student.bean.QuestionEntity;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.utils.MediaUtils;
import com.yanxiu.gphone.hd.student.view.picsel.PicSelView;
import com.yanxiu.gphone.hd.student.view.picsel.utils.ShareBitmapUtils;
import com.yanxiu.gphone.hd.student.view.question.QuestionsListener;
import com.yanxiu.gphone.hd.student.view.question.YXiuAnserTextView;


/**
 * Created by lidm on 2015/9/25.
 */
public class SubjectiveQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex  {
    private static final String TAG=SubjectiveQuestionFragment.class.getSimpleName();
    private View rootView;
    //本地的保存数据bean
    private AnswerBean bean;

    private Button addBtn;

    private YXiuAnserTextView yXiuAnserTextView;

    private QuestionEntity questionsEntity;

    private Fragment resolutionFragment;
    private int answerViewTypyBean;

    private int pageIndex;
    private PicSelView mPicSelView;

    private boolean isFirstSub;//是否是首个主观题Frgment用于初始化全局主观题Id

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogInfo.log(TAG, "SubjectiveQuestionFragment on Create： ");
            this.questionsEntity = (getArguments() != null) ? (QuestionEntity) getArguments().getSerializable("questions") : null;
            if(questionsEntity==null){
                LogInfo.log(TAG, "questionsEntity==null");
            }

            this.answerViewTypyBean = (getArguments() != null) ? getArguments().getInt("answerViewTypyBean") : null;
            this.pageIndex = (getArguments() != null) ? getArguments().getInt("pageIndex") : 0;
            this.isFirstSub=(getArguments()!=null)?getArguments().getBoolean("isFirstSub", false):false;
            if(isFirstSub){
                if(!ShareBitmapUtils.getInstance().isInitCurrentId()){
                    LogInfo.log("geny","come from onCreate");
                    changeCurrentSelData(questionsEntity);
                    ShareBitmapUtils.getInstance().setIsInitCurrentId(true);
                }
            }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_subjective_question, null);
        yXiuAnserTextView = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);

        FragmentTransaction ft = SubjectiveQuestionFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
        if(questionsEntity != null && questionsEntity.getStem() != null){
            yXiuAnserTextView.setTextHtml(questionsEntity.getStem());
        }

        setPicSelViewId();
        selectTypeView();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogInfo.log("lee", "onActivityCreated");

    }

    /**
     * 设置当前主观题Id给图片选择View
     */
    private void setPicSelViewId(){
        LogInfo.log(TAG, "setPicSelViewId: " + this.questionsEntity.getId());
        mPicSelView=(PicSelView)rootView.findViewById(R.id.picSelView);
        if(this.questionsEntity!=null&& !StringUtils.isEmpty(this.questionsEntity.getId())){
            mPicSelView.setSubjectiveId(this.questionsEntity.getId());
        }
    }
    private void selectTypeView(){
        switch (answerViewTypyBean){
            case SubjectExercisesItemBean.RESOLUTION:
                mPicSelView.setVisibility(View.GONE);
                addAnalysisFragment();
                break;
            case SubjectExercisesItemBean.WRONG_SET:
                mPicSelView.setVisibility(View.GONE);
                addBtn = (Button) rootView.findViewById(R.id.add_problem_analysis);
                addBtn.setVisibility(View.VISIBLE);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBtn.setVisibility(View.GONE);
                        addAnalysisFragment();
                    }
                });
                break;
            default:
//                addAnalysisFragment();
                break;
        }
    }

    private void addAnalysisFragment(){
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        resolutionFragment = Fragment.instantiate(SubjectiveQuestionFragment.this.getActivity(), SubjectiveProblemAnalysisFragment.class.getName(), args);
        FragmentTransaction ft = SubjectiveQuestionFragment.this.getChildFragmentManager().beginTransaction();
//         标准动画
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        ft.replace(R.id.content_problem_analysis, resolutionFragment).commitAllowingStateLoss();
    }

    @Override
    public void onStart() {
        super.onStart();
        LogInfo.log("geny", "---onStart-------pageIndex----" + pageIndex);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogInfo.log(TAG, "---onResume-------pageIndex----" + pageIndex);
        if(bean == null){
            bean = questionsEntity.getAnswerBean();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        LogInfo.log("geny", "---onPause-------pageIndex----" + pageIndex);
//        LogInfo.log("geny", "onPause");
//        if (resolutionFragment != null) {
//            ft = ChoiceQuestionFragment.this.getChildFragmentManager().beginTransaction();
//            ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
//        }
    }

    /**
     * 切换当前的全局变量数据
     *  1. 首个主观题执行 2 . onPageSelected 执行
     */
    public void changeCurrentSelData(QuestionEntity entity){
        LogInfo.log(TAG, "changeCurrentSelData");
        if(this.questionsEntity==null && entity != null){
            this.questionsEntity = entity;
            LogInfo.log(TAG,"entity != null");
        }

        if(this.questionsEntity==null){
            return;
        }

        if(StringUtils.isEmpty(questionsEntity.getId())){
            LogInfo.log(TAG,"StringUtils.isEmpty(questionsEntity.getId())");
            return;
        }
        LogInfo.log(TAG, "Change Id is : " + this.questionsEntity.getId());
        String id=this.questionsEntity.getId();
        if(StringUtils.isEmpty(id)){
            return;
        }
        ShareBitmapUtils.getInstance().setCurrentSbId(id);
        if(mPicSelView!=null){
            mPicSelView.changeData();
        }

    }


    public void updataPhotoView(int type)  {
        LogInfo.log(TAG, "updataPhotoView");
        if(questionsEntity==null||StringUtils.isEmpty(questionsEntity.getId())){
            LogInfo.log(TAG,"questionsEntity==null||StringUtils.isEmpty(questionsEntity.getId())");
            questionsEntity = (getArguments() != null) ? (QuestionEntity) getArguments().getSerializable("questions") : null;
            if(questionsEntity==null||StringUtils.isEmpty(questionsEntity.getId())){
                LogInfo.log(TAG,"getArguments(): questionsEntity==null||StringUtils.isEmpty(questionsEntity.getId())");
                return;
            }
        }

            ShareBitmapUtils.getInstance().setCurrentSbId(questionsEntity.getId());

//            PicSelViewEvent event =new PicSelViewEvent();
//            event.setType(type);
//            event.setCurrentIndex(((AnswerViewActivity) getActivity()).currentIndex);
//            event.setPicIndex(pageIndex - 1);
//            event.setId(questionsEntity.getId());
//            EventBus.getDefault().post(event);
        LogInfo.log(TAG, "mPicSelView: " + mPicSelView);
            if(mPicSelView!=null){
                mPicSelView.upDate(type,questionsEntity.getId());
            }

            if(!ShareBitmapUtils.getInstance().isCurrentListIsEmpty(questionsEntity.getId())){
                LogInfo.log(TAG,"setPhotoUri");
                questionsEntity.setPhotoUri(ShareBitmapUtils.getInstance().getPathList(questionsEntity.getId()));
            }
            questionsEntity.getAnswerBean().setIsSubjective(true);
            //add by lidm 判断是否完成主观题
            if(!ShareBitmapUtils.getInstance().isCurrentListIsEmpty(questionsEntity.getId())){
                LogInfo.log(TAG,"questionsEntity.getAnswerBean().setIsFinish(true)");
                questionsEntity.getAnswerBean().setIsFinish(true);
            }else{
                LogInfo.log(TAG, "questionsEntity.getAnswerBean().setIsFinish(false)");
                questionsEntity.getAnswerBean().setIsFinish(false);
            } 

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case MediaUtils.OPEN_SYSTEM_CAMERA:
                updataPhotoView(MediaUtils.OPEN_SYSTEM_CAMERA);
                break;
            case MediaUtils.OPEN_DEFINE_PIC_BUILD:
                updataPhotoView(MediaUtils.OPEN_DEFINE_PIC_BUILD);
                break;
            case LocalPhotoViewActivity.REQUEST_CODE:
                updataPhotoView(LocalPhotoViewActivity.REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
        resolutionFragment = null;
        LogInfo.log(TAG, "---onDestroyView-------pageIndex----" + pageIndex);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogInfo.log(TAG, "---onDestroy-------pageIndex----" + pageIndex);
        rootView=null;
        bean=null;
        addBtn=null;
        yXiuAnserTextView=null;
        questionsEntity=null;
        resolutionFragment=null;
        mPicSelView=null;

    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
//        this.listener = listener;
    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        this.bean = bean;
    }

    @Override public void answerViewClick() {

    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

}
