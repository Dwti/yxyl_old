package com.yanxiu.gphone.student.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.ReadingAnswer;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_FILL_BLANKS;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_JUDGE;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_MULTI_CHOICES;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_READING;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SINGLE_CHOICES;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SUBJECTIVE;

/**
 * Created by Administrator on 2015/8/2.
 */
public class QuestionUtils {


    public static List<QuestionEntity> addChildQuestionToParent(List<PaperTestEntity> dataList) {
        List<QuestionEntity> questionList = new ArrayList<QuestionEntity>();
        List<PaperTestEntity> nullList = new ArrayList<PaperTestEntity>();
        boolean flag;
        if (dataList != null) {
            int count = dataList.size();
            int index = 0;
            for (int i = 0; i < count; i++) {
                flag = false;
                if (dataList.get(i) != null && dataList.get(i).getQuestions() != null) {
                    QuestionEntity questionEntity = dataList.get(i).getQuestions();
                    int typeId = questionEntity.getType_id();
                    if (questionEntity.getTemplate().equals(YanXiuConstant.MULTI_QUESTION)
                            || questionEntity.getTemplate().equals(YanXiuConstant.CLOZE_QUESTION)
                            || questionEntity.getTemplate().equals(YanXiuConstant.LISTEN_QUESTION)) {
                        questionEntity.setPageIndex(index);
                        List<PaperTestEntity> childQuestion = questionEntity.getChildren();
                        if (childQuestion != null) {
                            int childCount = childQuestion.size();
                            for (int j = 0; j < childCount; j++) {
                                childQuestion.get(j).getQuestions().setPageIndex(index);
                                if (22 == typeId) {
                                    //只有是复合题且是解答题的时候，才会有childPageIndex，否则childPageIndex为-1
                                    childQuestion.get(j).getQuestions().setChildPageIndex(j);
                                } else {
                                    childQuestion.get(j).getQuestions().setChildPageIndex(-1);
                                    index++;
                                    //如果是-1，下面不能再让index++
                                    flag = true;
                                }
                                questionList.add(childQuestion.get(j).getQuestions());
                            }
                        }else{
                            questionList.add(questionEntity);
                        }
                    } else {
                        questionEntity.setPageIndex(index);
                        questionList.add(questionEntity);
                    }
                    if (!flag)
                        index++;
                } else {
                    LogInfo.log("geny-", "remove item quesition------");
                    nullList.add(dataList.get(i));
                }
            }
        }
        dataList.removeAll(nullList);
        return questionList;
    }


    public static void clearSubjectiveQuesition(SubjectExercisesItemBean subjectEditionBean) {
        if (subjectEditionBean != null && subjectEditionBean.getData() != null && !subjectEditionBean.getData().isEmpty()) {
            List<PaperTestEntity> dataList = subjectEditionBean.getData().get(0).getPaperTest();

            if (dataList == null) {
                return;
            }

            int count = dataList.size();
            for (int i = 0; i < count; i++) {
                if (dataList.get(i) != null && dataList.get(i).getQuestions() != null) {
                    int typeId = subjectEditionBean.getData().get(0).getPaperTest().get(i).getQuestions().getType_id();
                    if (QUESTION_SUBJECTIVE.type == typeId) {
                        subjectEditionBean.getData().get(0).getPaperTest().get(i).getQuestions().setAnswerBean(new AnswerBean());
                    }
                }
            }

        }
    }


    public static List<PaperTestEntity> removeSubjectiveQuesition(SubjectExercisesItemBean subjectEditionBean) {
        List<PaperTestEntity> paperTestEntites = new ArrayList<PaperTestEntity>();
        if (subjectEditionBean != null && subjectEditionBean.getData() != null && !subjectEditionBean.getData().isEmpty()) {
            List<PaperTestEntity> dataList = subjectEditionBean.getData().get(0).getPaperTest();

            if (dataList == null) {
                return null;
            }

            int count = dataList.size();
            for (int i = 0; i < count; i++) {
                if (dataList.get(i) != null && dataList.get(i).getQuestions() != null) {
                    int typeId = subjectEditionBean.getData().get(0).getPaperTest().get(i).getQuestions().getType_id();
                    if (QUESTION_SUBJECTIVE.type == typeId) {
                        paperTestEntites.add(subjectEditionBean.getData().get(0).getPaperTest().get(i));
                    }
                }
            }
            subjectEditionBean.getData().get(0).getPaperTest().removeAll(paperTestEntites);

        }
        return paperTestEntites;
    }

    public static List<QuestionEntity> findSubjectiveQuesition(SubjectExercisesItemBean subjectEditionBean) {
        List<QuestionEntity> questionEntities = new ArrayList<QuestionEntity>();
        if (subjectEditionBean != null && subjectEditionBean.getData() != null && !subjectEditionBean.getData().isEmpty()) {
            List<PaperTestEntity> dataList = subjectEditionBean.getData().get(0).getPaperTest();

            if (dataList == null) {
                return null;
            }

            int count = dataList.size();
            for (int i = 0; i < count; i++) {
                if (dataList.get(i) != null && dataList.get(i).getQuestions() != null) {
                    int typeId = subjectEditionBean.getData().get(0).getPaperTest().get(i).getQuestions().getType_id();
                    if (QUESTION_SUBJECTIVE.type == typeId) {
                        questionEntities.add(subjectEditionBean.getData().get(0).getPaperTest().get(i).getQuestions());
                    }
                }
            }
        }
        return questionEntities;
    }


    public static void initDataWithAnswer(SubjectExercisesItemBean subjectEditionBean) {
        if (subjectEditionBean != null && subjectEditionBean.getData() != null && !subjectEditionBean.getData().isEmpty()) {
            List<PaperTestEntity> dataList = subjectEditionBean.getData().get(0).getPaperTest();

            if (dataList == null) {
                return;
            }

            int count = dataList.size();
            for (int i = 0; i < count; i++) {
                if (dataList.get(i) != null && dataList.get(i).getQuestions() != null) {

                    PaperTestEntity paperTestEntity = dataList.get(i);
                    QuestionEntity questionEntity = paperTestEntity.getQuestions();

                    questionEntity.setExtend(paperTestEntity.getExtend());
                    questionEntity.setPadBean(paperTestEntity.getPad());
                    if (paperTestEntity.getPad() != null) {
                        LogInfo.log("geny", "----initDataWithAnswer getPad ----" + questionEntity.getPad().toString());
                    } else {
                        LogInfo.log("geny", "----initDataWithAnswer getPad ---- null");
                    }
                    int typeId = questionEntity.getType_id();
                    if (paperTestEntity.getPad() == null) {
                        continue;
                    }
                    String jsonAnswer = questionEntity.getPad().getJsonAnswer();

                    //如果是复合类的题
                    if (questionEntity.getTemplate().equals(YanXiuConstant.MULTI_QUESTION)
                            || questionEntity.getTemplate().equals(YanXiuConstant.CLOZE_QUESTION)
                            || questionEntity.getTemplate().equals(YanXiuConstant.LISTEN_QUESTION)) {

                        List<ReadingAnswer> answerList = JSON.parseArray(jsonAnswer, ReadingAnswer.class);
                        if (answerList != null && !answerList.isEmpty()) {
                            List<PaperTestEntity> paperList = questionEntity.getChildren();
                            if (paperList == null) {
                                continue;
                            }
                            int childrenCount = paperList.size();
                            for (int j = 0; j < childrenCount; j++) {
                                List<String> answerChildList;
                                if (j >= answerList.size()) {
                                    answerChildList = null;
                                } else {
                                    answerChildList = answerList.get(j).getAnswer();
                                }
                                if (paperList.get(j) == null) {
                                    continue;
                                }
                                List<String> rightAnswer = paperList.get(j).getQuestions().getAnswer();
                                if (answerChildList != null && !answerChildList.isEmpty()) {
                                    AnswerBean answerChildBean = paperList.get(j).getQuestions().getAnswerBean();
                                    typeId = questionEntity.getChildren().get(j).getQuestions().getType_id();
                                    if (typeId == QUESTION_SINGLE_CHOICES.type || typeId == QUESTION_JUDGE.type) {
                                        answerChildBean.setSelectType(answerChildList.get(0));
                                        if (rightAnswer != null && !rightAnswer.isEmpty()) {
                                            if (!TextUtils.isEmpty(answerChildList.get(0)) && answerChildList.get(0).equals(rightAnswer.get(0))) {
                                                answerChildBean.setIsFinish(true);
                                                answerChildBean.setIsRight(true);
                                            } else {
                                                answerChildBean.setIsFinish(true);
                                                answerChildBean.setIsRight(false);
                                            }
                                        } else {
                                            answerChildBean.setIsFinish(false);
                                        }
                                    } else if (typeId == QUESTION_MULTI_CHOICES.type || typeId == QUESTION_FILL_BLANKS.type || typeId == QUESTION_SUBJECTIVE.type) {
                                        if (typeId == QUESTION_MULTI_CHOICES.type) {
                                            answerChildBean.setMultiSelect((ArrayList<String>) answerChildList);
                                        } else if (typeId == QUESTION_FILL_BLANKS.type) {
                                            answerChildBean.setFillAnswers((ArrayList<String>) answerChildList);
                                        }else if (typeId == QUESTION_SUBJECTIVE.type){
                                            answerChildBean.setSubjectivImageUri((ArrayList<String>) answerChildList);
                                        }
                                        if (rightAnswer == null || rightAnswer.isEmpty()) {
                                            answerChildBean.setIsFinish(false);
                                        } else {
                                            if(typeId != QUESTION_SUBJECTIVE.type){
                                                //如果不是问答题（即主观题）
                                                if (compare(answerChildList, rightAnswer)) {
                                                    answerChildBean.setIsFinish(true);
                                                    answerChildBean.setIsRight(true);
                                                } else {
                                                    answerChildBean.setIsFinish(true);
                                                    answerChildBean.setIsRight(false);
                                                }
                                            }else{
                                                //如果是问答题
                                                int score = paperList.get(j).getQuestions().getPad().getTeachercheck().getScore();  //老师打的分数，数值范围0-5，0:错；0~5：半对； 5：对
                                                if(score==0){
                                                    answerChildBean.setIsRight(false);
                                                }else if(score==5){
                                                    answerChildBean.setIsRight(true);
                                                }else if(score>0 && score<5){
                                                    //TODO 此时设置半对状态  以前的复合题里面是不是没有问答题，如果没有的话，需要在下面把answerChildBean设置为问答题
                                                }
                                                answerChildBean.setIsFinish(true);
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        int status = questionEntity.getPad().getStatus();
                        int costTime = questionEntity.getPad().getCosttime();
                        int score = questionEntity.getPad().getTeachercheck().getScore();
                        AnswerBean answerBean = questionEntity.getAnswerBean();
                        answerBean.setConsumeTime(costTime);
                        List<String> answerList = JSON.parseArray(jsonAnswer, String.class);

                        if (typeId != QUESTION_SUBJECTIVE.type) {
                            switch (status) {
                                case AnswerBean.ANSER_RIGHT:
                                    answerBean.setIsFinish(true);
                                    answerBean.setIsRight(true);
                                    break;
                                case AnswerBean.ANSER_WRONG:
                                    answerBean.setIsFinish(true);
                                    answerBean.setIsRight(false);
                                    break;
                                case AnswerBean.ANSER_UNFINISH:
                                    answerBean.setIsFinish(false);
                                    break;
                            }
                        } else {
                            switch (status) {
                                case AnswerBean.ANSER_UNFINISH:
                                    answerBean.setIsFinish(false);
                                    break;
                                case AnswerBean.ANSER_FINISH:
                                    answerBean.setIsFinish(true);
                                    break;
                                case AnswerBean.ANSER_READED:
                                    if(score==0){
                                        answerBean.setIsRight(false);
                                    }else if(score==5){
                                        answerBean.setIsRight(true);
                                    }else if(score>0 && score<5){
                                        //TODO 此时设置半对状态  此处为什么只有已批改状态才会设置为问答题？是否因为批改了才能跳转什么的？
                                    }
                                    answerBean.setIsFinish(true);
                                    answerBean.setIsSubjective(true);
                                    break;
                            }
                        }

                        if (typeId == QUESTION_SUBJECTIVE.type) {
                            if (answerList == null || answerList.isEmpty()) {
                                answerBean.setIsFinish(false);
                            } else {
                                answerBean.setIsFinish(true);
                            }
                        }

                        if (answerList != null && !answerList.isEmpty()) {
                            if (typeId == QUESTION_SINGLE_CHOICES.type) {
                                answerBean.setSelectType(answerList.get(0));
                            } else if (typeId == QUESTION_MULTI_CHOICES.type) {
                                answerBean.setMultiSelect((ArrayList<String>) answerList);
                            } else if (typeId == QUESTION_JUDGE.type) {
                                answerBean.setSelectType(answerList.get(0));
                            } else if (typeId == QUESTION_FILL_BLANKS.type) {
                                answerBean.setFillAnswers((ArrayList<String>) answerList);
                            } else if (typeId == QUESTION_SUBJECTIVE.type) {
                                answerBean.setSubjectivImageUri((ArrayList<String>) answerList);
                            }
                        }
                    }

                }
            }
        }
    }

    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.size() != b.size()) {
            return false;
        }
        return a.containsAll(b) && b.containsAll(a);
    }

    public static int calculationUnFinishQuestion(List<PaperTestEntity> dataList) {
        int unfinishCount = 0;
        int count = dataList.size();
        if (dataList == null) {
            return 0;
        }
        for (int i = 0; i < count; i++) {

            //if(dataList.get(i).getQuestions() != null && dataList.get(i).getQuestions().getChildren() != null && dataList.get(i).getQuestions().getType_id() == QUESTION_READING.type){
            if (dataList.get(i).getQuestions().getTemplate().equals(YanXiuConstant.MULTI_QUESTION)
                    || dataList.get(i).getQuestions().getTemplate().equals(YanXiuConstant.CLOZE_QUESTION)
                    || dataList.get(i).getQuestions().getTemplate().equals(YanXiuConstant.LISTEN_QUESTION)) {
                List<PaperTestEntity> questionList = dataList.get(i).getQuestions().getChildren();
                int childrenCount = questionList.size();
                boolean isFalse = false;
                for (int j = 0; j < childrenCount; j++) {
                    if (!questionList.get(j).getQuestions().getAnswerBean().isFinish()) {
                        unfinishCount++;
                    } else {
                        //阅读题中 有一道题做过 就设置成完成
                        dataList.get(i).getQuestions().getAnswerBean().setIsFinish(true);
                    }
                    //阅读题中 有一道题错误 就设置成错误
                    if (!questionList.get(j).getQuestions().getAnswerBean().isRight()) {
                        isFalse = isFalse || true;
                        LogInfo.log("geny", "阅读题是错误的" + i);
                    } else {
                        LogInfo.log("geny", "阅读题是对的" + i);
                    }
                }
                if (isFalse) {
                    LogInfo.log("geny", "阅读题是错误的");
                    dataList.get(i).getQuestions().getAnswerBean().setIsRight(false);
                } else {
                    LogInfo.log("geny", "阅读题是对的");
                    dataList.get(i).getQuestions().getAnswerBean().setIsRight(true);
                }

            } else {
                if (dataList.get(i).getQuestions() != null && !dataList.get(i).getQuestions().getAnswerBean().isFinish()) {
                    unfinishCount++;
                    LogInfo.log("geny", "没做完的数量的" + i);
                }
            }

        }
        LogInfo.log("geny", "calculationFinishQuestion-----" + unfinishCount);
        return unfinishCount;
    }

    public static int calculationUnFinishAndWrongQuestion(List<PaperTestEntity> dataList) {
        int unfinishCount = 0;
        int count = dataList.size();
        for (int i = 0; i < count; i++) {

            if (dataList.get(i) != null && dataList.get(i).getQuestions() != null) {
                //if (dataList.get(i).getQuestions().getChildren() != null &&
                //dataList.get(i).getQuestions().getType_id() == QUESTION_READING.type) {
                if (dataList.get(i).getQuestions().getTemplate().equals(YanXiuConstant.MULTI_QUESTION)
                        || dataList.get(i).getQuestions().getTemplate().equals(YanXiuConstant.CLOZE_QUESTION)
                        || dataList.get(i).getQuestions().getTemplate().equals(YanXiuConstant.LISTEN_QUESTION)) {
                    List<PaperTestEntity> questionList = dataList.get(i).getQuestions().getChildren();
                    int childrenCount = questionList.size();
                    boolean isFalse = false;
                    boolean isFinish = false;
                    for (int j = 0; j < childrenCount; j++) {
                        if (!questionList.get(j).getQuestions().getAnswerBean().isFinish()) {
//                        unfinishCount++;
                            isFinish = false;
                        } else {
                            //阅读题中 有一道题做过 就设置成完成
                            dataList.get(i).getQuestions().getAnswerBean().setIsFinish(true);
                            isFinish = true;
                        }
                        //阅读题中 有一道题错误 就设置成错误
                        if (!questionList.get(j).getQuestions().getAnswerBean().isRight()) {
                            isFalse = true;
                        }
                    }
                    if (isFalse) {
                        dataList.get(i).getQuestions().getAnswerBean().setIsRight(false);
                    } else {
                        dataList.get(i).getQuestions().getAnswerBean().setIsRight(true);
                    }
                    if (!isFinish) {
                        unfinishCount++;
                    }

                } else {
                    if (!dataList.get(i).getQuestions().getAnswerBean().isFinish()) {
                        unfinishCount++;
                    }
                }
            }

        }
        LogInfo.log("geny", "calculationFinishQuestion-----" + unfinishCount);
        return unfinishCount;
    }


    public static String calculationAverageSubejectScore(ArrayList<PaperTestEntity> subDataList) {
        int totalScore = 0;
        if (subDataList != null && !subDataList.isEmpty()) {
            for (PaperTestEntity paperTestEntity : subDataList) {
                QuestionEntity questionsEntity = paperTestEntity.getQuestions();
                if (questionsEntity != null && questionsEntity.getPadBean() != null && questionsEntity.getPadBean().getTeachercheck() != null && questionsEntity.getPadBean().getStatus() == AnswerBean.ANSER_READED) {
                    totalScore += questionsEntity.getPadBean().getTeachercheck().getScore();
                }
            }
            float num = (float) totalScore / subDataList.size();
            DecimalFormat df = new DecimalFormat("0.0");//格式化小数
            String str = df.format(num);//返回的是String类型
            return str;
        }
        return "0";
    }


    public static void setQuestionIndex(List<PaperTestEntity> dataList) {
        if (dataList != null) {
            int count = dataList.size();
            int index = 0;
            for (int i = 0; i < count; i++) {
                if (dataList.get(i) != null && dataList.get(i).getQuestions() != null) {
                    int typeId = dataList.get(i).getQuestions().getType_id();
                    //if (typeId == QUESTION_READING.type) {
                    if (dataList.get(i).getQuestions().getTemplate().equals(YanXiuConstant.MULTI_QUESTION)
                            || dataList.get(i).getQuestions().getTemplate().equals(YanXiuConstant.CLOZE_QUESTION)
                            || dataList.get(i).getQuestions().getTemplate().equals(YanXiuConstant.LISTEN_QUESTION)) {
                        QuestionEntity questionEntity = dataList.get(i).getQuestions();
                        questionEntity.setQuestionIndex(index);
                        if (questionEntity != null) {
                            List<PaperTestEntity> childQuestion = questionEntity.getChildren();
                            if (childQuestion != null) {
                                int childCount = childQuestion.size();
                                for (int j = 0; j < childCount; j++) {
                                    childQuestion.get(j).getQuestions().setQuestionIndex(index);
                                }
                            }
                        }
                    } else {
                        QuestionEntity questionEntity = dataList.get(i).getQuestions();
                        questionEntity.setQuestionIndex(index);
                    }
                    index++;
                }
            }
        }
    }

}
