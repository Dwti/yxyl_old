package com.yanxiu.gphone.studentold.fragment.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.activity.AnswerViewActivity;
import com.yanxiu.gphone.studentold.activity.BaseAnswerViewActivity;
import com.yanxiu.gphone.studentold.activity.MistakeRedoActivity;
import com.yanxiu.gphone.studentold.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.studentold.activity.WrongAnswerViewActivity;
import com.yanxiu.gphone.studentold.adapter.AnswerAdapter;
import com.yanxiu.gphone.studentold.bean.AnswerBean;
import com.yanxiu.gphone.studentold.bean.PaperTestEntity;
import com.yanxiu.gphone.studentold.bean.QuestionEntity;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.inter.AnswerCallback;
import com.yanxiu.gphone.studentold.inter.OnPushPullTouchListener;
import com.yanxiu.gphone.studentold.view.ClozzTextview;
import com.yanxiu.gphone.studentold.view.ExpandableRelativeLayoutlayout;
import com.yanxiu.gphone.studentold.view.question.QuestionsListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public class ClozzQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex, ViewPager.OnPageChangeListener, AnswerCallback, ClozzTextview.onDrawSucessListener {

    private int pageCountIndex;
    private List<PaperTestEntity> children;
    private AnswerBean bean;
    private ClozzTextview fill_blanks_button;
    private ScrollView sv_content_top;
    private LinearLayout ll_bottom_view;
    private FrameLayout fl_content_top;

    private OnPushPullTouchListener mOnPushPullTouchListener;
    private ImageView ivBottomCtrl;
    private ViewPager vpAnswer;
    private AnswerAdapter adapter;
    private int pageCount = 1;
    private boolean isVisibleToUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pageCountIndex = this.pageIndex;
        if (questionsEntity != null && questionsEntity.getChildren() != null) {
            children = questionsEntity.getChildren();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_clozz, null);
        initview();
        listener();
        selectTypeView();
        return rootView;
    }

    private void selectTypeView(){
        switch (answerViewTypyBean){
            case SubjectExercisesItemBean.RESOLUTION:
            case SubjectExercisesItemBean.WRONG_SET:
                fill_blanks_button.setTextColor();
                break;
            case SubjectExercisesItemBean.MISTAKEREDO:
                FrameLayout layout= (FrameLayout) rootView.findViewById(R.id.fra_sub_or_del);
                layout.setVisibility(View.VISIBLE);
                FragmentManager manager = getChildFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment1=manager.findFragmentByTag("sub_or_del");
                if (fragment1==null) {
                    final SubmitOrDeleteFragment fragment = new SubmitOrDeleteFragment();
                    fragment.setEntity(questionsEntity);
                    initSubOrDel(fragment);
                    fragment.setListener(new ClozzQuestionFragment.listener(fragment));
                    transaction.add(R.id.fra_sub_or_del, fragment,"sub_or_del");
                    transaction.show(fragment);
                    transaction.commit();
                }else {
                    final SubmitOrDeleteFragment fragment= (SubmitOrDeleteFragment) fragment1;
                    fragment.setEntity(questionsEntity);
                    initSubOrDel(fragment);
                    fragment.setListener(new ClozzQuestionFragment.listener(fragment));
                }
                break;
        }
    }

    private class listener implements SubmitOrDeleteFragment.OnButtonClickListener{

        private SubmitOrDeleteFragment fragment;

        public listener(SubmitOrDeleteFragment fragment){
            this.fragment=fragment;
        }

        @Override
        public void onClick(String type) {
            switch (type) {
                case SubmitOrDeleteFragment.TYPE_SUBMIT:
                    questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
                    fill_blanks_button.setTextColor();
                    checkTheAnswer();
                    fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_SUBMIT);
                    setMistakeSubmit();
                    break;
                case SubmitOrDeleteFragment.TYPE_DELETE:
                    questionsEntity.setType(QuestionEntity.TYPE_DELETE_END);
                    fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_DELETE);
                    setMistakeDelete();
                    break;
            }
        }
    }

    @Override
    public ViewPager getViewPager() {
        return vpAnswer;
    }

    @Override
    public void setMistakeDelete() {
        super.setMistakeDelete();
        ArrayList<Fragment> list=adapter.getmFragments();
        if (list==null){
            return;
        }
        for (int i=0;i<list.size();i++){
            QuestionEntity entity=children.get(i).getQuestions();
            BaseQuestionFragment fragment= (BaseQuestionFragment) list.get(i);
            try {
                entity.setType(QuestionEntity.TYPE_DELETE_END);
                fragment.setMistakeDelete();
            }catch (Exception e){}
        }
    }

    @Override
    public void setMistakeSubmit() {
        super.setMistakeSubmit();
        ArrayList<Fragment> list=adapter.getmFragments();
        if (list==null){
            return;
        }
        for (int i=0;i<list.size();i++){
            QuestionEntity entity=children.get(i).getQuestions();
            BaseQuestionFragment fragment= (BaseQuestionFragment) list.get(i);
            try {
                entity.setType(QuestionEntity.TYPE_SUBMIT_END);
                fragment.setMistakeSubmit();
            }catch (Exception e){}
        }
    }

    private void checkTheAnswer(){
        if (children!=null&&children.size()>0){
            for (int i=0;i<children.size();i++){
                QuestionEntity entity=children.get(i).getQuestions();
                if (entity.getAnswerIsRight()!=1){
                    ((MistakeRedoActivity)getActivity()).showPopup(MistakeRedoActivity.FAIL);
                    questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_FAIL);
                    return;
                }
            }
        }
        ((MistakeRedoActivity)getActivity()).showPopup(MistakeRedoActivity.RIGHT);
        questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_RIGHT);
    }

    @Override
    public void redoCallback() {
        super.redoCallback();
        Fragment fragment1=getChildFragmentManager().findFragmentByTag("sub_or_del");
        if (fragment1!=null){
            SubmitOrDeleteFragment fragment = (SubmitOrDeleteFragment)fragment1;
            initSubOrDel(fragment);
        }
    }

    private void initSubOrDel(SubmitOrDeleteFragment fragment) {
        if (QuestionEntity.TYPE_SUBMIT.equals(questionsEntity.getType())) {
            if (children!=null&&children.size()>0){
                for (int i=0;i<children.size();i++){
                    QuestionEntity entity=children.get(i).getQuestions();
                    if (!entity.isHaveAnser()){
                        questionsEntity.setIsAllBlanksFilled(false);
                        fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_NOT_SUBMIT_NOANSWER);
                        return;
                    }
                }
            }
            questionsEntity.setIsAllBlanksFilled(true);
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_NOT_SUBMIT_HASANSWER);
        }else if (QuestionEntity.TYPE_SUBMIT_END.equals(questionsEntity.getType())){
            fill_blanks_button.setTextColor();
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_SUBMIT);
//            FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
        }else if (QuestionEntity.TYPE_DELETE_END.equals(questionsEntity.getType())){
            fill_blanks_button.setTextColor();
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_DELETE);
//            FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
        }
    }

    @Override
    public void setChildPagerIndex(int childPagerIndex) {
        super.setChildPagerIndex(childPagerIndex);
        if (vpAnswer != null) {
            vpAnswer.setCurrentItem(childPagerIndex);
        }
    }

    private void listener() {
        fill_blanks_button.setListener(new ClozzTextview.QuestionPositionSelectListener() {
            @Override
            public void QuestionPosition(ClozzTextview.Buttonbean buttonbean) {
                if (vpAnswer != null) {
                    int count = adapter.getCount();
                    if (buttonbean.getId() < count) {
                        vpAnswer.setCurrentItem(buttonbean.getId());
                    }
                }
            }
        });
    }

    @Override
    public int getChildCount() {
        if (adapter != null) {
            return adapter.getCount();
        } else {
            return super.getChildCount();
        }
    }

    private void initview() {
        ExpandableRelativeLayoutlayout rl_top_view = (ExpandableRelativeLayoutlayout) rootView.findViewById(R.id.rl_top_view);
        fill_blanks_button = (ClozzTextview) rootView.findViewById(R.id.fill_blanks_button);
        fill_blanks_button.setOnDrawSucessListener(this);
        fl_content_top = (FrameLayout) rootView.findViewById(R.id.fl_content_top);
        sv_content_top = (ScrollView) rootView.findViewById(R.id.sv_content_top);
        View top_dotted_line = rootView.findViewById(R.id.top_dotted_line);
        if(ischild)
            top_dotted_line.setVisibility(View.GONE);
        if (questionsEntity != null && questionsEntity.getStem() != null) {
            int position_index;
            if (getActivity() instanceof WrongAnswerViewActivity) {
                position_index = 0;
            } else if (getActivity() instanceof MistakeRedoActivity){
                position_index = 0;
            }else {
                position_index = -1;
            }
            fill_blanks_button.setQuestionsEntity(questionsEntity, position_index);
            fill_blanks_button.setDataSources(questionsEntity.getAnswerBean());
            fill_blanks_button.setAnswerViewTypyBean(answerViewTypyBean);
            fill_blanks_button.setData(questionsEntity.getStem());
            fill_blanks_button.setAnswers(questionsEntity.getAnswer());
        }
        ll_bottom_view = (LinearLayout) rootView.findViewById(R.id.ll_bottom_view);
        mOnPushPullTouchListener = new OnPushPullTouchListener(ll_bottom_view, rl_top_view, getActivity());
//        ivBottomCtrl = (ImageView) rootView.findViewById(R.id.iv_bottom_ctrl);
        LinearLayout ll_bottom_ctrl = (LinearLayout) rootView.findViewById(R.id.ll_bottom_ctrl);
        ll_bottom_ctrl.setOnTouchListener(mOnPushPullTouchListener);

        vpAnswer = (ViewPager) rootView.findViewById(R.id.answer_viewpager);
        //=============================================
        //反射viewPager里面的mScroller
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
        } catch (Exception e) {

        }
        vpAnswer.setOnPageChangeListener(this);
        adapter = new AnswerAdapter(this.getChildFragmentManager());
        adapter.setAnswerCallback(this);
        adapter.setAnswerViewTypyBean(answerViewTypyBean);
        adapter.addDataSourcesForReadingQuestion(children, questionsEntity.getTemplate(), questionsEntity.getType_id(), getTotalCount(),this);
        int count = adapter.getCount();
        onPageCount(count);
        vpAnswer.setAdapter(adapter);
        adapter.setViewPager(vpAnswer);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (bean == null) {
            bean = questionsEntity.getAnswerBean();
        }
//        setDataSources(bean);
        if (fill_blanks_button != null) {
            fill_blanks_button.setDataSources(bean);
        }
        LogInfo.log("geny", "onResume");
        if (getActivity() instanceof MistakeRedoActivity){

        }else {
            if (vpAnswer != null) {
                if (!is_reduction) {
                    vpAnswer.setCurrentItem(childPagerIndex);
                } else {
                    vpAnswer.setCurrentItem(adapter.getCount() - 1);
                }
            }
        }

        if (!ischild && isVisibleToUser) {
            if (!ischild) {
                if (adapter != null) {
                    try {
                        ((QuestionsListener) getActivity()).flipNextPager(adapter);
                    } catch (Exception e) {
                    }
                }
            }
        }
        setCurrent(vpAnswer);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (!isVisibleToUser) {
            if (fill_blanks_button != null) {
                if (bean != null) {
                    fill_blanks_button.saveAnswers();
                }
            }
            if (vpAnswer != null) {
                vpAnswer.setCurrentItem(0);
            }

        } else {

            if (vpAnswer != null) {
                if (!is_reduction) {
                    vpAnswer.setCurrentItem(0);
                } else {
                    vpAnswer.setCurrentItem(adapter.getCount() - 1);
                }
            }

            if (!ischild) {
                if (adapter != null) {
                    ((QuestionsListener) getActivity()).flipNextPager(adapter);
                }
            }

        }
    }

    @Override
    public void setRefresh() {
        super.setRefresh();
        if (vpAnswer != null) {
            if (!is_reduction) {
                vpAnswer.setCurrentItem(childPagerIndex);
            } else {
                vpAnswer.setCurrentItem(adapter.getCount() - 1);
            }
        }
    }

    public void onPageCount(int count) {
        pageCount = count;
    }

    @Override
    public void answercallback(int position, String answer) {
        if (fill_blanks_button != null) {
            fill_blanks_button.setAnswersToPosition(position, answer);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private int move_height = 0;
    private int position = -1;
    private boolean Isscroll = false;

    @Override
    public void onPageSelected(int position) {
        if (answerViewTypyBean == SubjectExercisesItemBean.ANSWER_QUESTION) {
            int costtime = AnswerViewActivity.totalTime - AnswerViewActivity.lastTime;
            AnswerViewActivity.lastTime = AnswerViewActivity.totalTime;
            adapter.setCostTime(costtime, questionsEntity.getPageIndex(), childPagerIndex);
            childPagerIndex = position;
            AnswerViewActivity.childIndex = position;
        }else if (answerViewTypyBean == SubjectExercisesItemBean.WRONG_SET||answerViewTypyBean == SubjectExercisesItemBean.RESOLUTION){
            this.childPagerIndex=position;
        }
        if (questionsEntity != null) {
            pageCountIndex = pageIndex + position;
            if (this.getActivity() instanceof AnswerViewActivity && isVisibleToUser) {
                ((AnswerViewActivity) this.getActivity()).setIndexFromRead(pageCountIndex);
//                ((AnswerViewActivity) this.getActivity()).setIndexNext(pageCountIndex+getChildCount());
            } else if (this.getActivity() instanceof ResolutionAnswerViewActivity && isVisibleToUser) {
                ((ResolutionAnswerViewActivity) this.getActivity()).setIndexFromRead(pageCountIndex);
            } else if (this.getActivity() instanceof WrongAnswerViewActivity && isVisibleToUser) {
                ((WrongAnswerViewActivity) this.getActivity()).setIndexFromRead(pageCountIndex);
            }
            if (fill_blanks_button != null) {
                fill_blanks_button.setTextViewSelect(position);
                if (fill_blanks_button.getList().get(position).getY() > 0) {
                    setScroll(position);
                } else {
                    this.position = position;
                    Isscroll = true;
                }
            }

        }
        ((BaseAnswerViewActivity) getActivity()).setPagerSelect(adapter.getCount(), position);
    }

    private void setScroll(int position) {
        if (move_height == 0) {
            move_height = fl_content_top.getHeight() * 8 / 26;
        }
        if (fill_blanks_button.getList().get(position).getY() - sv_content_top.getScrollY() < 0) {
            sv_content_top.scrollTo(0, fill_blanks_button.getList().get(position).getY());
        }
        if (fill_blanks_button.getList().get(position).getY() - sv_content_top.getScrollY() > fl_content_top.getBottom() - fl_content_top.getTop() - move_height) {

            sv_content_top.scrollTo(0, fill_blanks_button.getList().get(position).getY() - fl_content_top.getHeight() + move_height);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
    public void flipNextPager(QuestionsListener listener) {
        this.listener=listener;
    }

    @Override
    public void setDataSources(AnswerBean bean) {

    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        this.bean = bean;
    }

    @Override
    public void answerViewClick() {
        if (fill_blanks_button != null) {
            if (bean != null) {
                fill_blanks_button.saveAnswers();
            }
        }
    }

    @Override
    public void onsucess() {
        if (Isscroll && position != -1) {
            setScroll(position);
            this.position = -1;
            this.Isscroll = false;
        }
    }
}
