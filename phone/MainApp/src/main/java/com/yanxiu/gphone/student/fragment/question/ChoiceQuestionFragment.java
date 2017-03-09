package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.MistakeRedoActivity;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.inter.AnswerCallback;
import com.yanxiu.gphone.student.inter.SetAnswerCallBack;
import com.yanxiu.gphone.student.utils.FragmentManagerFactory;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.student.view.question.choicequestion.ChoiceQuestions;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/7.
 */
public class ChoiceQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex {

    private View rootView;
    private ChoiceQuestions choiceQuestions;
    private QuestionsListener listener;
    //本地的保存数据bean
    private AnswerBean bean;

    private Button addBtn;

    private YXiuAnserTextView yXiuAnserTextView;


    public int typeId;

    private Fragment resolutionFragment;
    private AnswerCallback callback;
    private int position;
    private boolean isVisibleToUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.typeId = (getArguments() != null) ? getArguments().getInt("typeId") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (rootView==null) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_choices_question, null);
        LinearLayout ll_answer_content = (LinearLayout) rootView.findViewById(R.id.ll_answer_content);
        View view_line_ccc4a3_2 = rootView.findViewById(R.id.view_line_ccc4a3_2);
        choiceQuestions = (ChoiceQuestions) rootView.findViewById(R.id.cq_item);
        choiceQuestions.flipNextPager(listener);
        if (answerViewTypyBean==SubjectExercisesItemBean.MISTAKEREDO) {
            choiceQuestions.setCallback(callBack);
        }
        ChoiceQuestionFragment context = this;
        View top_dotted_line = rootView.findViewById(R.id.top_dotted_line);
        if (ischild)
            top_dotted_line.setVisibility(View.GONE);
        if (callback != null) {
            ll_answer_content.setVisibility(View.GONE);
            view_line_ccc4a3_2.setVisibility(View.GONE);
            choiceQuestions.setAnswerCallback(position, callback);
        }
        yXiuAnserTextView = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);
        FragmentTransaction ft = ChoiceQuestionFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();

        choiceQuestions.setChoicesType(typeId);

        selectTypeView();
        if (questionsEntity != null) {
            choiceQuestions.setAllDataSources(questionsEntity);
            if (!StringUtils.isEmpty(questionsEntity.getStem())) {
                yXiuAnserTextView.setTextHtml(questionsEntity.getStem());
            }
//            Log.d("asd",questionsEntity.getStem().toString());
        }
        LogInfo.log("geny", "---onCreateView-------pageIndex----" + pageIndex);
//        }
        return rootView;
    }

    public void setAnswerCallback(int position, AnswerCallback callback) {
        this.callback = callback;
        this.position = position;
        if (choiceQuestions != null) {
            choiceQuestions.setAnswerCallback(position, callback);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && !ischild) {
//            if (adapter!=null){
            try {
                ((QuestionsListener) getActivity()).flipNextPager(null);
            } catch (Exception e) {

            }
//            }
        }
    }

    private void selectTypeView() {
        switch (answerViewTypyBean) {
            case SubjectExercisesItemBean.RESOLUTION:
                choiceQuestions.setIsResolution(true);
                choiceQuestions.setIsClick(false);
                addAnalysisFragment();
                break;
            case SubjectExercisesItemBean.WRONG_SET:
                choiceQuestions.setIsWrongSet(true);
                choiceQuestions.setIsClick(false);
                addBtn = (Button) rootView.findViewById(R.id.add_problem_analysis);
                addBtn.setVisibility(View.VISIBLE);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBtn.setVisibility(View.GONE);
                        try {
                            ArrayList<String> answer_list = bean.getFillAnswers();
                            answer_list.clear();
                            String jsonanswer = questionsEntity.getPad().getJsonAnswer();
                            JSONArray array = new JSONArray(jsonanswer);
                            for (int i = 0; i < array.length(); i++) {
                                String answer = array.getString(i);
                                answer_list.add(answer);
                            }
                            if (answer_list.size() == 1) {
                                bean.setSelectType(answer_list.get(0));
                            }
                        } catch (Exception e) {

                        }
                        choiceQuestions.initViewWithData(bean);
                        addAnalysisFragment();
                    }
                });
                break;
            case SubjectExercisesItemBean.MISTAKEREDO:
                if (ischild){
                    if (QuestionEntity.TYPE_SUBMIT_END.equals(questionsEntity.getType())||QuestionEntity.TYPE_DELETE_END.equals(questionsEntity.getType())){
                        choiceQuestions.setIsResolution(true);
                        choiceQuestions.setIsClick(false);
                        choiceQuestions.setclearFocuse();
                        choiceQuestions.setDataSources(questionsEntity.getAnswerBean());
                        FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
                    }
                    return;
                }
                FrameLayout layout= (FrameLayout) rootView.findViewById(R.id.fra_sub_or_del);
                layout.setVisibility(View.VISIBLE);
                final SubmitOrDeleteFragment fragment = new SubmitOrDeleteFragment();
                initSubOrDel(fragment);
                fragment.setEntity(questionsEntity);
                fragment.setListener(new SubmitOrDeleteFragment.OnButtonClickListener() {
                    @Override
                    public void onClick(String type) {
                        switch (type) {
                            case SubmitOrDeleteFragment.TYPE_SUBMIT:
                                questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
                                choiceQuestions.setIsResolution(true);
                                choiceQuestions.setIsClick(false);
                                choiceQuestions.setclearFocuse();
                                choiceQuestions.setDataSources(questionsEntity.getAnswerBean());
                                checkTheAnswer();
                                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_SUBMIT);
                                FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
                                break;
                            case SubmitOrDeleteFragment.TYPE_DELETE:
                                questionsEntity.setType(QuestionEntity.TYPE_DELETE_END);
                                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_DELETE);
                                break;
                        }
                    }
                });
                FragmentManager manager = getChildFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.fra_sub_or_del, fragment,"sub_or_del");
                transaction.show(fragment);
                transaction.commit();
                break;
        }
    }

    @Override
    public void setMistakeSubmit() {
        super.setMistakeSubmit();
        choiceQuestions.setIsResolution(true);
        choiceQuestions.setIsClick(false);
        choiceQuestions.setclearFocuse();
        choiceQuestions.setDataSources(questionsEntity.getAnswerBean());
        questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
        FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
    }

    @Override
    public void setMistakeDelete() {
        super.setMistakeDelete();
        questionsEntity.setType(QuestionEntity.TYPE_DELETE_END);
    }

    private SetAnswerCallBack callBack=new SetAnswerCallBack() {
        @Override
        public void callback() {
            if (ischild){
                boolean f1=getIsHavaAnswer();
                boolean f2=getTheAnswerIsRight();
                if (redoCallback!=null){
                    redoCallback.redoCallback();
                }
                return;
            }
            Fragment fragment=getChildFragmentManager().findFragmentByTag("sub_or_del");
            if (fragment==null){
                return;
            }
            SubmitOrDeleteFragment submitOrDeleteFragment= (SubmitOrDeleteFragment) fragment;
            initSubOrDel(submitOrDeleteFragment);
        }
    };

    private boolean getIsHavaAnswer(){
        boolean flag=false;
        switch (typeId) {
            case 1:
                String answer = questionsEntity.getAnswerBean().getSelectType();
                if (!TextUtils.isEmpty(answer)) {
                    flag=true;
                }
                break;
            case 2:
                List<String> answer_list = questionsEntity.getAnswerBean().getMultiSelect();
                if (answer_list != null && answer_list.size() > 1) {
                    flag=true;
                }
                break;
        }
        questionsEntity.setIsAllBlanksFilled(flag);
        return  flag;
    }

    private void initSubOrDel(SubmitOrDeleteFragment fragment) {
        if (QuestionEntity.TYPE_SUBMIT.equals(questionsEntity.getType())) {
            if (getIsHavaAnswer()){
                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_NOT_SUBMIT_HASANSWER);
            }else {
                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_NOT_SUBMIT_NOANSWER);
            }
        }else if (QuestionEntity.TYPE_SUBMIT_END.equals(questionsEntity.getType())){
            choiceQuestions.setIsResolution(true);
            choiceQuestions.setIsClick(false);
            choiceQuestions.setclearFocuse();
            choiceQuestions.setDataSources(questionsEntity.getAnswerBean());
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_SUBMIT);
            FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
        }else if (QuestionEntity.TYPE_DELETE_END.equals(questionsEntity.getType())){
            choiceQuestions.setIsResolution(true);
            choiceQuestions.setIsClick(false);
            choiceQuestions.setclearFocuse();
            choiceQuestions.setDataSources(questionsEntity.getAnswerBean());
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_DELETE);
            FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
        }
    }

    private boolean getTheAnswerIsRight(){
        boolean flag=false;
        List<String> list = questionsEntity.getAnswer();
        switch (typeId) {
            case 1:
                String answer = questionsEntity.getAnswerBean().getSelectType();
                if (answer!=null) {
                    if (list.get(0).equals(answer)) {
                        //回答正确
                        flag = true;
                        questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_RIGHT);
                    } else {
                        //回答错误
                        flag = false;
                        questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_FAIL);
                    }
                }else {
                    questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_DEFULT);
                }
                break;
            case 2:
                List<String> answer_list = questionsEntity.getAnswerBean().getMultiSelect();
                if (answer_list != null && answer_list.size()>0) {
                    if (answer_list.size() == list.size()){
                        for (int i = 0; i < list.size(); i++) {
                            if (list.containsAll(answer_list)) {
                                //回答正确
                                flag = true;
                                questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_RIGHT);
                            } else {
                                //回答错误
                                flag = false;
                                questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_FAIL);
                            }
                        }
                    }else{
                        //回答错误
                        flag = false;
                        questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_FAIL);
                    }
                }else {
                    //未回答
                    flag=false;
                    questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_DEFULT);
                }
                break;
        }
        return flag;
    };

    private void checkTheAnswer() {
        if (getTheAnswerIsRight()){
            ((MistakeRedoActivity)getActivity()).showPopup(MistakeRedoActivity.RIGHT);
        } else {
            ((MistakeRedoActivity)getActivity()).showPopup(MistakeRedoActivity.FAIL);
        }
    }

    private void addAnalysisFragment() {
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        resolutionFragment = Fragment.instantiate(ChoiceQuestionFragment.this.getActivity(), ProblemAnalysisFragment.class.getName(), args);
        FragmentTransaction ft = ChoiceQuestionFragment.this.getChildFragmentManager().beginTransaction();
//         标准动画
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

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
        LogInfo.log("geny", "---onResume-------pageIndex----" + pageIndex);
        if (bean == null) {
            bean = questionsEntity.getAnswerBean();
        }
        if (choiceQuestions != null) {
            choiceQuestions.setDataSources(bean);
        }
        if (!ischild && isVisibleToUser) {
            ((QuestionsListener) getActivity()).flipNextPager(null);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        LogInfo.log("geny", "---onPause-------pageIndex----" + pageIndex);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogInfo.log("geny", "---onDestroy-------pageIndex----" + pageIndex);
//        choiceQuestions=null;
////        listener=null;
//        bean=null;
//        addBtn=null;
//        yXiuAnserTextView=null;
//
//        resolutionFragment=null;
//        System.gc();
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
        this.listener = listener;
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
}

