package com.yanxiu.gphone.student.fragment.question;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.common.core.utils.LogInfo;
import com.common.core.utils.PictureHelper;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.CaptureActivity;
import com.yanxiu.gphone.student.activity.ImageCropActivity;
import com.yanxiu.gphone.student.activity.LocalPhotoViewActivity;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.CorpBean;
import com.yanxiu.gphone.student.bean.DeleteImageBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.inter.CorpListener;
import com.yanxiu.gphone.student.utils.CorpUtils;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.picsel.PicSelView;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import de.greenrobot.event.EventBus;

/**
 * Created by lidm on 2015/9/25.
 */
public class SubjectiveQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex, CorpListener {
    public static final String TYPE="image_delete";
    public static final String TYPE_CORP="corp";
    private static final String TAG = SubjectiveQuestionFragment.class.getSimpleName();
    private View rootView;
    //本地的保存数据bean
    private AnswerBean bean;

    private Button addBtn;

    private YXiuAnserTextView yXiuAnserTextView;

//    private QuestionEntity questionsEntity;

    private Fragment resolutionFragment;
//    private int answerViewTypyBean;

//    private int pageIndex;
    private PicSelView mPicSelView;
    private Activity mActivity;

    private boolean isFirstSub;//是否是首个主观题Frgment用于初始化全局主观题Id

    private boolean IsCreat=false;
    private boolean IsVisible=false;
    private boolean IsFirst=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
//        this.questionsEntity = (getArguments() != null) ? (QuestionEntity) getArguments().getSerializable("questions") : null;
//        this.answerViewTypyBean = (getArguments() != null) ? getArguments().getInt("answerViewTypyBean") : null;
//        this.pageIndex = (getArguments() != null) ? getArguments().getInt("pageIndex") : 0;
        this.isFirstSub = (getArguments() != null) ? getArguments().getBoolean("isFirstSub", false) : false;
//        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (rootView==null) {
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_subjective_question, null);
            yXiuAnserTextView = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);


            FragmentTransaction ft = SubjectiveQuestionFragment.this.getChildFragmentManager().beginTransaction();
            ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
            if (questionsEntity != null && questionsEntity.getStem() != null) {
//                yXiuAnserTextView.setTextHtml("v-stem : \"在2.3737<img class=\"kfformula latex3\" src=\"http://scc.jsyxw.cn/formula/f9/f96217d18c60c280226bac50e510092b.png\" data-latex=\"\\cdot \\cdot \\cdot \"/><span style=\"font-family: 宋体;\">&nbsp;</span>，4.68，，0.8686，3.14159这几个数中，有限小数有____,循环小数有____。\"");
                yXiuAnserTextView.setTextHtml(questionsEntity.getStem().replaceAll("\\(_\\)", "____"));
            }
            IsCreat = true;
            setRegister();
            setPicSelViewId();

//        for (int i=0; questionsEntity.getAnswerBean().getSubjectivImageUri()!= null && i<questionsEntity.getAnswerBean().getSubjectivImageUri().size(); i++) {
//            ShareBitmapUtils.getInstance().addPath(this.questionsEntity.getId(), questionsEntity.getAnswerBean().getSubjectivImageUri().get(i).toString());
//        }
            changeCurrentSelData(questionsEntity);
            selectTypeView();
//        }
        return rootView;
    }


    public void onEventMainThread(DeleteImageBean bean){
        if (bean.getType().equals(TYPE)){
            String url=bean.getImage_url();
            if (url.startsWith("http")){
                questionsEntity.getAnswerBean().getSubjectivImageUri().remove(url);
//                ShareBitmapUtils.getInstance().getDrrMaps().get(questionsEntity.getId()).remove(url);
//                EventBus.getDefault().unregister(this);
            }
        }
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

    private void setRegister(){
//        if (IsCreat&&IsVisible&&IsFirst){
//            if (questionsEntity.getType_id() == QUESTION_SOLVE_COMPLEX.type||questionsEntity.getType_id()==QUESTION_SUBJECTIVE.type) {
//                EventBus.getDefault().register(this);
//                IsFirst = false;
//            }
//        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&!ischild){
            try {
                ((QuestionsListener) getActivity()).flipNextPager(null);
            }catch (Exception e){}
        }
        if (isVisibleToUser){
            if (ischild) {
                YanXiuConstant.catch_position = pageIndex;
            }
            IsVisible=true;
        }else {
            IsVisible=false;
//            CorpUtils.getInstence().RemoveListener(this);
      }
//        EventBus.getDefault().unregister(this);
        setRegister();
    }

    /**
     * 设置当前主观题Id给图片选择View
     */
    private void setPicSelViewId() {
        LogInfo.log(TAG, "setPicSelViewId: " + this.questionsEntity.getId());
        mPicSelView = (PicSelView) rootView.findViewById(R.id.picSelView);
        mPicSelView.setFragment(this);
        if (this.questionsEntity != null && !StringUtils.isEmpty(this.questionsEntity.getId())) {
            mPicSelView.setSubjectiveId(this.questionsEntity.getId(),questionsEntity.getAnswerBean().getSubjectivImageUri());
        }
    }

    private void selectTypeView() {
        switch (answerViewTypyBean) {
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
                break;
        }
    }

    private void addAnalysisFragment() {
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        resolutionFragment = Fragment.instantiate(SubjectiveQuestionFragment.this.getActivity(), SubjectiveProblemAnalysisFragment.class.getName(), args);
        ((SubjectiveProblemAnalysisFragment)resolutionFragment).setIndexposition(this);
        FragmentTransaction ft = SubjectiveQuestionFragment.this.getChildFragmentManager().beginTransaction();
//         标准动画
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

        ft.replace(R.id.content_problem_analysis, resolutionFragment).commitAllowingStateLoss();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogInfo.log(TAG, "---onResume-------pageIndex----" + pageIndex);
        if (bean == null) {
            bean = questionsEntity.getAnswerBean();
        }
        if (!ischild) {
            if (CorpUtils.getInstence().getCorpListener() != null) {
                if (((CorpListener) this).hashCode() == CorpUtils.getInstence().getCorpListener().hashCode()) {
                    YanXiuConstant.index_position = 0;
                }
            }
        }
        //EventBus.getDefault().unregister(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        LogInfo.log("geny", "---onPause-------pageIndex----" + pageIndex);
    }

    /**
     * 切换当前的全局变量数据
     * 1. 首个主观题执行 2 . onPageSelected 执行
     */
    public void changeCurrentSelData(QuestionEntity entity) {
        if(entity==null || TextUtils.isEmpty(entity.getId()))
            return;
//        ShareBitmapUtils.getInstance().setCurrentSbId(entity.getId());
        if (mPicSelView != null) {
            mPicSelView.changeData();
        }

    }


    public void updataPhotoView(int type) {
        if (questionsEntity == null || StringUtils.isEmpty(questionsEntity.getId())) {
            return;
        }
        if (mPicSelView != null) {
            mPicSelView.upDate(getActivity(),type, questionsEntity.getId());
        }

        if (!ShareBitmapUtils.getInstance().isCurrentListIsEmpty(questionsEntity.getId())) {
            questionsEntity.setPhotoUri(ShareBitmapUtils.getInstance().getPathList(questionsEntity.getId()));
        }
        questionsEntity.getAnswerBean().setIsSubjective(true);
        //add by lidm 判断是否完成主观题
        if (!ShareBitmapUtils.getInstance().isCurrentListIsEmpty(questionsEntity.getId())) {
            questionsEntity.getAnswerBean().setIsFinish(true);
        } else {
            questionsEntity.getAnswerBean().setIsFinish(false);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode) {
            //点击拍照，拍照完了点击确定会走这儿（此时还没有进入裁剪页面）
            case MediaUtils.OPEN_SYSTEM_CAMERA:
                if(questionsEntity==null || StringUtils.isEmpty(questionsEntity.getId()))
                    return;
                //mPicSelView.upDate(getActivity(),MediaUtils.OPEN_SYSTEM_CAMERA, questionsEntity.getId());
                break;
            //进入照片选择页面，并且裁剪完了，会走这儿
            case MediaUtils.OPEN_DEFINE_PIC_BUILD:
//                YanXiuConstant.index_position=0;
                EventBus.getDefault().unregister(this);
                if (data != null) {
                    updataPhotoView(MediaUtils.OPEN_DEFINE_PIC_BUILD);
                } else {
                    if (resultCode==100001){

                    }else {
                        if(questionsEntity==null || StringUtils.isEmpty(questionsEntity.getId()))
                            return;
                        mPicSelView.upDate(getActivity(),MediaUtils.OPEN_SYSTEM_CAMERA, questionsEntity.getId());
                    }
                }
                break;
            //拍照时候的裁剪走这个地方，如果是从相册选择的话，走的是ImageBucketActivity的onActivityResult
            case MediaUtils.IMAGE_CROP:
                if(resultCode==mActivity.RESULT_OK){
                    if(data!=null){
                        String filePath = PictureHelper.getPath(mActivity,MediaUtils.currentCroppedImageUri);
                        ShareBitmapUtils.getInstance().addPath(ShareBitmapUtils.getInstance().getCurrentSbId(), filePath);
                        mPicSelView.updateImage(ShareBitmapUtils.getInstance().getCurrentSbId());

                        if (!ShareBitmapUtils.getInstance().isCurrentListIsEmpty(questionsEntity.getId())) {
                            questionsEntity.setPhotoUri(ShareBitmapUtils.getInstance().getPathList(questionsEntity.getId()));
                        }
                        questionsEntity.getAnswerBean().setIsSubjective(true);
                        //判断是否完成主观题
                        if (!ShareBitmapUtils.getInstance().isCurrentListIsEmpty(questionsEntity.getId())) {
                            questionsEntity.getAnswerBean().setIsFinish(true);
                        } else {
                            questionsEntity.getAnswerBean().setIsFinish(false);
                        }
                    }
                }
                break;
            case LocalPhotoViewActivity.REQUEST_CODE:
                updataPhotoView(LocalPhotoViewActivity.REQUEST_CODE);
                break;
            case MediaUtils.CAPATURE_AND_CROP:
                if(resultCode == mActivity.RESULT_OK){

                    String imagePath = data.getStringExtra(ImageCropActivity.IMAGE_PATH);
                    ShareBitmapUtils.getInstance().addPath(ShareBitmapUtils.getInstance().getCurrentSbId(), imagePath);
                    updataPhotoView(MediaUtils.OPEN_DEFINE_PIC_BUILD);
                }
                if(!CaptureActivity.bitmap.isRecycled()){
                    CaptureActivity.bitmap.recycle();
                }
                CaptureActivity.bitmap = null;
                break;
            default:
                break;
        }


    }



    public void onEventMainThread(CorpBean corpBean){
//<<<<<<< HEAD
//        if (TYPE_CORP.equals(corpBean.getType())) {
////            if (MediaUtils.IsCallBack) {
//                String filePath = PictureHelper.getPath(getActivity(), MediaUtils.currentCroppedImageUri);
//                ShareBitmapUtils.getInstance().addPath(ShareBitmapUtils.getInstance().getCurrentSbId(), filePath);
//                updataPhotoView(MediaUtils.OPEN_DEFINE_PIC_BUILD);
////                MediaUtils.currentCroppedImageUri = null;
////                MediaUtils.IsCallBack=false;
////            }
//=======
//        String filePath = PictureHelper.getPath(getActivity(),MediaUtils.currentCroppedImageUri);
//        ShareBitmapUtils.getInstance().addPath(ShareBitmapUtils.getInstance().getCurrentSbId(), filePath);
//        updataPhotoView(MediaUtils.OPEN_DEFINE_PIC_BUILD);
//
//>>>>>>> 3e356847799f860e6412c0cea4a35f6d0b81f6dc
//        YanXiuConstant.index_position=0;
//        EventBus.getDefault().unregister(this);
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogInfo.log(TAG, "---onDestroyView-------pageIndex----" + pageIndex);
//        EventBus.getDefault().unregister(this);
        IsCreat=false;
        IsFirst=true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogInfo.log(TAG, "---onDestroy-------pageIndex----" + pageIndex);
//        EventBus.getDefault().unregister(this);
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

    @Override
    public void answerViewClick() {

    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public void oncorp(Uri url) {
        String filePath = PictureHelper.getPath(getActivity(), url);
        ShareBitmapUtils.getInstance().addPath(ShareBitmapUtils.getInstance().getCurrentSbId(), filePath);
        updataPhotoView(MediaUtils.OPEN_DEFINE_PIC_BUILD);
//
//
//        String filePath1 = PictureHelper.getPath(getActivity(), MediaUtils.currentCroppedImageUri);
//        ShareBitmapUtils.getInstance().addPath(ShareBitmapUtils.getInstance().getCurrentSbId(), filePath1);
//        updataPhotoView(MediaUtils.OPEN_DEFINE_PIC_BUILD);
    }

    @Override
    public void ondelete(DeleteImageBean bean) {
        String url=bean.getImage_url();
        if (url.startsWith("http")){
            questionsEntity.getAnswerBean().getSubjectivImageUri().remove(url);
        }
    }
}
