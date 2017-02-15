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
import com.yanxiu.gphone.student.activity.CameraActivity;
import com.yanxiu.gphone.student.activity.ImageCropActivity;
import com.yanxiu.gphone.student.activity.LocalPhotoViewActivity;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.DeleteImageBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.inter.CorpListener;
import com.yanxiu.gphone.student.utils.CorpUtils;
import com.yanxiu.gphone.student.utils.FragmentManagerFactory;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.picsel.PicSelView;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

/**
 * Created by lidm on 2015/9/25.
 */
public class SubjectiveQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex, CorpListener {
    public static final String TYPE = "image_delete";
    private static final String TAG = SubjectiveQuestionFragment.class.getSimpleName();
    private View rootView;
    //本地的保存数据bean
    private AnswerBean bean;

    private Button addBtn;

    private YXiuAnserTextView yXiuAnserTextView;
    private Fragment resolutionFragment;
    private PicSelView mPicSelView;
    private Activity mActivity;

    private boolean isFirstSub;//是否是首个主观题Frgment用于初始化全局主观题Id

    private boolean IsCreat = false;
    private boolean IsVisible = false;
    private boolean IsFirst = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        this.isFirstSub = (getArguments() != null) ? getArguments().getBoolean("isFirstSub", false) : false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_subjective_question, null);
        yXiuAnserTextView = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);
        View top_dotted_line = rootView.findViewById(R.id.top_dotted_line);
        if(ischild)
            top_dotted_line.setVisibility(View.GONE);
        FragmentTransaction ft = SubjectiveQuestionFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
        if (questionsEntity != null && questionsEntity.getStem() != null) {
            yXiuAnserTextView.setTextHtml(questionsEntity.getStem().replaceAll("\\(_\\)", "____"));
        }
        IsCreat = true;
        setPicSelViewId();
        changeCurrentSelData(questionsEntity);
        selectTypeView();
        return rootView;
    }


    public void onEventMainThread(DeleteImageBean bean) {
        if (bean.getType().equals(TYPE)) {
            String url = bean.getImage_url();
            if (url.startsWith("http")) {
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
        if (isVisibleToUser && !ischild) {
            try {
                ((QuestionsListener) getActivity()).flipNextPager(null);
            } catch (Exception e) {
            }
        }
        if (isVisibleToUser) {
            if (ischild) {
                YanXiuConstant.catch_position = pageIndex;
            }
            IsVisible = true;
        } else {
            IsVisible = false;
        }
    }

    /**
     * 设置当前主观题Id给图片选择View
     */
    private void setPicSelViewId() {
        LogInfo.log(TAG, "setPicSelViewId: " + this.questionsEntity.getId());
        mPicSelView = (PicSelView) rootView.findViewById(R.id.picSelView);
        mPicSelView.setFragment(this);
        if (this.questionsEntity != null && !StringUtils.isEmpty(this.questionsEntity.getId())) {
            mPicSelView.setSubjectiveId(this.questionsEntity.getId(), questionsEntity.getAnswerBean().getSubjectivImageUri());
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
            case SubjectExercisesItemBean.MISTAKEREDO:
                FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
                break;
            default:
                break;
        }
    }

    private void addAnalysisFragment() {
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        resolutionFragment = Fragment.instantiate(SubjectiveQuestionFragment.this.getActivity(), SubjectiveProblemAnalysisFragment.class.getName(), args);
        ((SubjectiveProblemAnalysisFragment) resolutionFragment).setIndexposition(this);
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
        if (entity == null || TextUtils.isEmpty(entity.getId()))
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
            mPicSelView.upDate(getActivity(), type, questionsEntity.getId());
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
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LocalPhotoViewActivity.REQUEST_CODE:
                updataPhotoView(LocalPhotoViewActivity.REQUEST_CODE);
                break;
            case MediaUtils.CAPATURE_AND_CROP:
                if (resultCode == mActivity.RESULT_OK) {
                    String imagePath = data.getStringExtra(ImageCropActivity.IMAGE_PATH);
                    ShareBitmapUtils.getInstance().addPath(ShareBitmapUtils.getInstance().getCurrentSbId(), imagePath);
                    updataPhotoView(MediaUtils.OPEN_DEFINE_PIC_BUILD);
                }
                CameraActivity.bitmap = null;
                break;
            default:
                break;
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogInfo.log(TAG, "---onDestroyView-------pageIndex----" + pageIndex);
        IsCreat = false;
        IsFirst = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogInfo.log(TAG, "---onDestroy-------pageIndex----" + pageIndex);
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
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
    }

    @Override
    public void ondelete(DeleteImageBean bean) {
        String url = bean.getImage_url();
        if (url.startsWith("http")) {
            questionsEntity.getAnswerBean().getSubjectivImageUri().remove(url);
        }
    }
}
