package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.utils.FragmentManagerFactory;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.fillblanks.FillBlanksFramelayout;

/**
 * Created by Administrator on 2015/7/7.
 */
public class FillBlanksFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex {

    private FillBlanksFramelayout fillBlanksFramelayout;

    private AnswerBean bean;

    //    private FragmentTransaction ft;
    private Fragment resolutionFragment;
    private Button addBtn;
    private boolean isVisibleToUser = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (rootView == null) {
        rootView = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_fill_blanks, null);
        fillBlanksFramelayout = (FillBlanksFramelayout) rootView.findViewById(R.id.fb_item);
        View top_dotted_line = rootView.findViewById(R.id.top_dotted_line);
        if (ischild)
            top_dotted_line.setVisibility(View.GONE);
        if (questionsEntity != null && questionsEntity.getStem() != null) {
            fillBlanksFramelayout.setAnswers(questionsEntity.getAnswer());
            fillBlanksFramelayout.setData(questionsEntity.getStem());
//            Log.d("asd", "Stem+++++" + questionsEntity.getStem());
        }

        FragmentTransaction ft = FillBlanksFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
        selectTypeView();
//        }
        return rootView;
    }

    private void addAnalysisFragment() {
        rootView.setClickable(false);
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        resolutionFragment = Fragment.instantiate(FillBlanksFragment.this.getActivity(), ProblemAnalysisFragment.class.getName(), args);
        FragmentTransaction ft = FillBlanksFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, resolutionFragment).commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bean == null) {
            bean = questionsEntity.getAnswerBean();
        }
        setDataSources(bean);
        LogInfo.log("geny", "onResume");
        if (!ischild && isVisibleToUser) {
            try {
                ((QuestionsListener) getActivity()).flipNextPager(null);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fillBlanksFramelayout != null) {
            fillBlanksFramelayout.hideSoftInput();
//            answerViewClick();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (!isVisibleToUser && fillBlanksFramelayout != null) {
            fillBlanksFramelayout.hideSoftInput();
            if (bean != null) {
                LogInfo.log("king", "saveAnswers");
                fillBlanksFramelayout.saveAnswers();
            }
        }
        if (isVisibleToUser && !ischild) {
//            if (adapter!=null){
            try {
                ((QuestionsListener) getActivity()).flipNextPager(null);
            } catch (Exception e) {
            }
//            }
        }
    }

    @Override
    public void flipNextPager(QuestionsListener flip) {
    }

    @Override
    public void saveAnwser() {
        super.saveAnwser();
        answerViewClick();
    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
        if (fillBlanksFramelayout != null) {
            fillBlanksFramelayout.setDataSources(bean);
        }
    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        this.bean = bean;
    }

    @Override
    public void answerViewClick() {
        if (fillBlanksFramelayout != null) {
            fillBlanksFramelayout.hideSoftInput();
            if (bean != null) {
                LogInfo.log("king", "answerViewClick saveAnswers");
                fillBlanksFramelayout.saveAnswers();
            }
        }
    }

    private void selectTypeView() {
        fillBlanksFramelayout.setAnswerViewTypyBean(answerViewTypyBean);
        switch (answerViewTypyBean) {
            case SubjectExercisesItemBean.RESOLUTION:
                addAnalysisFragment();
                break;
            case SubjectExercisesItemBean.WRONG_SET:
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

        }
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
    public void onDestroy() {
        super.onDestroy();
//        rootView = null;
//        fillBlanksFramelayout = null;
//        bean = null;
//        resolutionFragment = null;
//        addBtn = null;
//        rootView = null;
//        fillBlanksFramelayout = null;
//        bean = null;
//        resolutionFragment = null;
//        addBtn = null;

        System.gc();
    }
}
