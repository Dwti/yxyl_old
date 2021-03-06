package com.yanxiu.gphone.hd.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.bean.QuestionEntity;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.view.question.choicequestion.ChoiceQuestions;

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

    public Fragment createQuestionFragment(YanXiuConstant.QUESTION_TYP questionType, QuestionEntity questionsEntity, int answerViewTypyBean, int pageIndex) {
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
                fragment = new FillBlanksFragment();
                break;
            case QUESTION_READING:
                fragment = new ReadingQuestionsFragment();
                break;
            case QUESTION_SUBJECTIVE:
                fragment = new SubjectiveQuestionFragment();
                break;

        }
        LogInfo.log(TAG,"fragment setArguments -questionsEntity "+questionsEntity);
        args.putInt("answerViewTypyBean", answerViewTypyBean);
        args.putInt("pageIndex", pageIndex);
        args.putSerializable("questions", questionsEntity);
//        args.putSerializable("answerViewTypyBean", answerViewTypyBean);
        fragment.setArguments(args);
        return fragment;
    }

}
