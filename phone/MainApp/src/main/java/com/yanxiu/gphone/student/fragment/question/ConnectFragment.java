package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.MistakeRedoActivity;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.inter.SetAnswerCallBack;
import com.yanxiu.gphone.student.utils.FragmentManagerFactory;
import com.yanxiu.gphone.student.view.ConnectLinesLinearLayout;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/7.
 */
public class ConnectFragment extends BaseQuestionFragment implements PageIndex {

    private QuestionsListener listener;
    public int typeId;
    //本地的保存数据bean
    private AnswerBean bean;
    private boolean isVisibleToUser;
    private ConnectLinesLinearLayout connect_lineslinearlayout;
    private Fragment resolutionFragment;
    private Button addBtn;
    private FrameLayout ll_answer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.typeId = (getArguments() != null) ? getArguments().getInt("typeId") : null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_connect, null);
        initview(rootView);
        FragmentTransaction ft = ConnectFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
        selectTypeView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ischild) {
            if (questionsEntity != null && TextUtils.isEmpty(questionsEntity.getStem())) {
                ll_answer.setVisibility(View.GONE);
            } else {
                ll_answer.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initview(View rootView) {
        ll_answer = (FrameLayout) rootView.findViewById(R.id.ll_answer);
        YXiuAnserTextView connect_question = (YXiuAnserTextView) rootView.findViewById(R.id.connect_question);
        connect_lineslinearlayout = (ConnectLinesLinearLayout) rootView.findViewById(R.id.connect_lineslinearlayout);
        if (answerViewTypyBean==SubjectExercisesItemBean.MISTAKEREDO) {
            connect_lineslinearlayout.setCallBack(callBack);
        }
        View top_dotted_line = rootView.findViewById(R.id.top_dotted_line);
        if(ischild)
            top_dotted_line.setVisibility(View.GONE);
        if (questionsEntity != null) {
            connect_question.setTextHtml(questionsEntity.getStem());
            connect_lineslinearlayout.setAnswers(questionsEntity.getAnswer());
            if (bean != null) {
                connect_lineslinearlayout.setAnswerBean(bean);
            }
            connect_lineslinearlayout.setDatas(questionsEntity.getContent().getChoices());
//            connect_lineslinearlayout.setDefault();
        }
    }

    private void selectTypeView() {
        switch (answerViewTypyBean) {
            case SubjectExercisesItemBean.RESOLUTION:
                connect_lineslinearlayout.setIsResolution(true);
                connect_lineslinearlayout.setIsClick(false);
                addAnalysisFragment();
                break;
            case SubjectExercisesItemBean.WRONG_SET:
                connect_lineslinearlayout.setIsWrongSet(true);
                connect_lineslinearlayout.setIsClick(false);
                addBtn = (Button) rootView.findViewById(R.id.add_problem_analysis);
                addBtn.setVisibility(View.VISIBLE);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBtn.setVisibility(View.GONE);
//                        try {
//                            ArrayList<String> answer_list=bean.getFillAnswers();
//                            answer_list.clear();
//                            String jsonanswer=questionsEntity.getPad().getJsonAnswer();
//                            JSONArray array=new JSONArray(jsonanswer);
//                            for (int i=0;i<array.length();i++){
//                                String answer=array.getString(i);
//                                answer_list.add(answer);
//                            }
//                            if (answer_list.size()==1){
//                                bean.setSelectType(answer_list.get(0));
//                            }
//                        }catch (Exception e){
//
//                        }
//                        choiceQuestions.initViewWithData(bean);
                        addAnalysisFragment();
                    }
                });
                break;
            case SubjectExercisesItemBean.MISTAKEREDO:
                if (ischild){
                    if (QuestionEntity.TYPE_SUBMIT_END.equals(questionsEntity.getType())||QuestionEntity.TYPE_DELETE_END.equals(questionsEntity.getType())){
                        connect_lineslinearlayout.setIsResolution(true);
                        connect_lineslinearlayout.setIsClick(false);
                        connect_lineslinearlayout.setclearFocuse();
                        connect_lineslinearlayout.setDefault();
                        questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
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
                        switch  (type) {
                            case SubmitOrDeleteFragment.TYPE_SUBMIT:
                                questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
                                connect_lineslinearlayout.setIsResolution(true);
                                connect_lineslinearlayout.setIsClick(false);
                                connect_lineslinearlayout.setclearFocuse();
                                connect_lineslinearlayout.setDefault();
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
        connect_lineslinearlayout.setIsResolution(true);
        connect_lineslinearlayout.setIsClick(false);
        connect_lineslinearlayout.setclearFocuse();
        connect_lineslinearlayout.setDefault();
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
        connect_lineslinearlayout.saveAnswers();
        ArrayList<ArrayList<String>> list=questionsEntity.getAnswerBean().getConnect_classfy_answer();
        if (list!=null&&list.size()>0&&list.size()==questionsEntity.getAnswer().size()) {
            flag=true;
        }
        questionsEntity.setHaveAnser(flag);
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
            questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
            connect_lineslinearlayout.setIsResolution(true);
            connect_lineslinearlayout.setIsClick(false);
            connect_lineslinearlayout.setclearFocuse();
            connect_lineslinearlayout.setDefault();
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_SUBMIT);
            FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
        }else if (QuestionEntity.TYPE_DELETE_END.equals(questionsEntity.getType())){
            questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
            connect_lineslinearlayout.setIsResolution(true);
            connect_lineslinearlayout.setIsClick(false);
            connect_lineslinearlayout.setclearFocuse();
            connect_lineslinearlayout.setDefault();
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_DELETE);
            FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
        }
    }

    private boolean getTheAnswerIsRight(){
        boolean flag=true;
        if (questionsEntity.getAnswer().size()!=questionsEntity.getAnswerBean().getConnect_classfy_answer().size()){
            flag=false;
        }else {
            for (String s : questionsEntity.getAnswer()) {
                try {
                    JSONObject object = new JSONObject(s);
                    String string = object.getString("answer");
                    String ss[] = string.split(",");
                    for (ArrayList<String> list : questionsEntity.getAnswerBean().getConnect_classfy_answer()) {
                        if (list.get(0).equals(ss[0])) {
                            if (!list.get(1).equals(ss[1])) {
                                flag = false;
                            }
                        } else if (list.get(0).equals(ss[1])) {
                            if (!list.get(1).equals(ss[0])) {
                                flag = false;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (flag){
            questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_RIGHT);
        }else {
            questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_FAIL);
        }
        return flag;
    }

    private void checkTheAnswer() {
        if (getTheAnswerIsRight()){
            //回答正确
            ((MistakeRedoActivity)getActivity()).showPopup(MistakeRedoActivity.RIGHT);
        }else {
            //回答错误
            ((MistakeRedoActivity)getActivity()).showPopup(MistakeRedoActivity.FAIL);
        }
    }

    private void addAnalysisFragment() {
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);

        resolutionFragment = Fragment.instantiate(ConnectFragment.this.getActivity(), ProblemAnalysisFragment.class.getName(), args);
        FragmentTransaction ft = ConnectFragment.this.getChildFragmentManager().beginTransaction();
//         标准动画
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

        ft.replace(R.id.content_problem_analysis, resolutionFragment).commitAllowingStateLoss();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (!isVisibleToUser && connect_lineslinearlayout != null) {
            if (bean != null) {
                connect_lineslinearlayout.saveAnswers();
            }
        }
        if (isVisibleToUser) {
            if (!ischild) {
                try {
                    ((QuestionsListener) getActivity()).flipNextPager(null);
                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    public int getPageIndex() {
        return this.pageIndex;
    }

    @Override
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
        this.listener = listener;
    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
        if (connect_lineslinearlayout != null) {
            connect_lineslinearlayout.setAnswerBean(bean);
        }
    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        this.bean = bean;
        if (connect_lineslinearlayout != null) {
            connect_lineslinearlayout.setAnswerBean(bean);
        }
    }

    @Override
    public void answerViewClick() {
        super.answerViewClick();
        if (connect_lineslinearlayout != null) {
            if (bean != null) {
                connect_lineslinearlayout.saveAnswers();
            }
        }
    }

    @Override
    public void saveAnwser() {
        super.saveAnwser();
        answerViewClick();
    }
}
