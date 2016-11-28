package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.question.choicequestion.ChoiceQuestions;

import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_COMPUTE;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SOLVE_COMPLEX;

/**
 * Created by Administrator on 2015/7/17.
 */
public class QuestionFragmentFactory {
    private static final String TAG=QuestionFragmentFactory.class.getSimpleName();
    private static volatile QuestionFragmentFactory instance = null;
    // private constructor suppresses
    private QuestionFragmentFactory(){
    }

    public static QuestionFragmentFactory getInstance() {
        // if already inited, no need to get lock everytime
        if (instance == null) {
            synchronized (QuestionFragmentFactory.class) {
                if (instance == null) {
                    instance = new QuestionFragmentFactory();
                }
            }
        }
        return instance;
    }

    public Fragment createAnswerCardFragment(SubjectExercisesItemBean subjectExercisesItemBean, int comeFrom){
        Fragment fragment = new AnswerCardFragment();
        Bundle args = new Bundle();
        args.putSerializable("subjectExercisesItemBean", subjectExercisesItemBean);
        LogInfo.log("geny", "createAnswerCardFragment comeFrome------" + comeFrom);
        args.putInt("comeFrom", comeFrom);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment createQuestionFragment(YanXiuConstant.QUESTION_TYP questionType, QuestionEntity questionsEntity, int answerViewTypyBean, int pageIndex,int wrongID,int wrongCount) {
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (questionType) {
            case QUESTION_SINGLE_CHOICES:
                fragment = new ChoiceQuestionFragment();
                args.putSerializable("typeId", ChoiceQuestions.CHOICE_SINGLE_TYPE);
//                ((ChoiceQuestionFragment)fragment).setTypeId(ChoiceQuestions.ChoiceType.CHOICE_SINGLE_TYPE);
                break;
            case QUESTION_MULTI_CHOICES:
                fragment = new ChoiceQuestionFragment();
                args.putSerializable("typeId", ChoiceQuestions.CHOICE_MULTI_TYPE);
//                ((ChoiceQuestionFragment)fragment).setTypeId(ChoiceQuestions.ChoiceType.CHOICE_MULTI_TYPE);
                break;
            case QUESTION_JUDGE:
                fragment = new JudgeQuestionFragment();
                break;
            case QUESTION_FILL_BLANKS:
//                if (questionsEntity.getTemplate().equals(YanXiuConstant.MULTI_QUESTION) && (questionsEntity.getType_id() == QUESTION_SOLVE_COMPLEX.type
//                        || questionsEntity.getType_id() == QUESTION_COMPUTE.type)) {
//                    fragment=new NewFillBlanksFragment();
//                } else {
                    fragment = new FillBlanksFragment();
//                }
                //fragment=new NewFillBlanksFragment();
                break;
            case QUESTION_CLASSFY:
                fragment = new ClassfyQuestionFragment();
                break;
            case QUESTION_READING:
                //fragment = new ReadingQuestionsFragment();
                fragment = new ReadComplexQuestionFragment();
                break;
            case QUESTION_SUBJECTIVE:
                fragment = new SubjectiveQuestionFragment();
                break;
            case QUESTION_LISTEN_COMPLEX:
                fragment = new ListenComplexQuestionFragment();
                break;
            case QUESTION_READ_COMPLEX:
                fragment = new ReadComplexQuestionFragment();
                break;
            case QUESTION_CLOZE_COMPLEX:
//                fragment = new GestaltFillBlanksQuestionFragment();
                fragment = new ClozzQuestionFragment();
                break;
            case QUESTION_SOLVE_COMPLEX:
                fragment = new SolveComplexQuestionFragment();
                break;
            case QUESTION_CONNECT:
                fragment = new ConnectFragment();
                break;

        }
        LogInfo.log(TAG,"fragment setArguments -questionsEntity "+questionsEntity);
        args.putInt("answerViewTypyBean", answerViewTypyBean);
        args.putInt("pageIndex", pageIndex);
        args.putSerializable("questions", questionsEntity);
        args.putInt("wrong",wrongID);
        args.putInt("wrongCount",wrongCount);
//        args.putSerializable("answerViewTypyBean", answerViewTypyBean);
        fragment.setArguments(args);
        return fragment;
    }

}
