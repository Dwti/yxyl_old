package com.yanxiu.gphone.student.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.ReadingAnswer;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_FILL_BLANKS;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_JUDGE;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_MULTI_CHOICES;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SINGLE_CHOICES;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SUBJECTIVE;

/**
 * Created by Administrator on 2015/8/2.
 */
public class QuestionUtils {

    /**
     * 我的错题移除无关题目
     */
    public static void CleanData(List<PaperTestEntity> data) {
//        //需求变更
        if (true){
            return;
        }
        Iterator<PaperTestEntity> iterator = data.listIterator();
        PaperTestEntity paperTestEntity;
        while (iterator.hasNext()) {
            paperTestEntity = iterator.next();
            if (paperTestEntity.getQuestions().getTemplate().equals(YanXiuConstant.CONNECT_QUESTION) || paperTestEntity.getQuestions().getTemplate().equals(YanXiuConstant.CLASSIFY_QUESTION))
                iterator.remove();
        }
            //需求变更
//        for (int i = 0; i < data.size(); ) {
//            if (data.get(i).getQuestions().getExtend() == null) {
//                data.remove(i);
//                i = i;
//            } else {
//                i++;
//            }
//        }
    }

    public static void settingAnswer(SubjectExercisesItemBean bean) {
        /**因产品需求变更，需要暂时去掉这个功能*/
        if (true) {
            return;
        }

        List<PaperTestEntity> list = bean.getData().get(0).getPaperTest();
        for (PaperTestEntity entity : list) {
            try {
                ArrayList<String> answer_list = entity.getQuestions().getAnswerBean().getFillAnswers();
                String jsonanswer = entity.getQuestions().getPad().getJsonAnswer();
                JSONArray array = new JSONArray(jsonanswer);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    JSONArray jsonArray = object.optJSONArray("answer");
                    String answer = "";
                    if (jsonArray != null) {
                        answer = jsonArray.optString(0);
                        if (answer == null) {
                            answer = "";
                        }
                    }
                    answer_list.add(answer);
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * 移除归类跟连线题
     *
     * @param bean
     */
    public static void removeQuestions(SubjectExercisesItemBean bean) {
        if (true){
            return;
        }
        if (bean == null || bean.getData() == null || bean.getData().get(0).getPaperTest().isEmpty()) {
            return;
        }
        List<PaperTestEntity> list = bean.getData().get(0).getPaperTest();
        Iterator<PaperTestEntity> iterator = list.listIterator();
        PaperTestEntity paperTestEntity;
        while (iterator.hasNext()) {
            paperTestEntity = iterator.next();
            if (paperTestEntity.getQuestions().getTemplate().equals(YanXiuConstant.CONNECT_QUESTION) || paperTestEntity.getQuestions().getTemplate().equals(YanXiuConstant.CLASSIFY_QUESTION))
                iterator.remove();
        }
    }

    public static List<QuestionEntity> addChildQuestionToParent(List<PaperTestEntity> dataList) {
        List<QuestionEntity> questionList = new ArrayList<QuestionEntity>();
        List<PaperTestEntity> nullList = new ArrayList<PaperTestEntity>();
        boolean flag;
        if (dataList != null) {
            int count = dataList.size();
            int position = 0;    //记录答题卡的题号之用
            int index = 0;       //记录题目的真实序号之用
            for (int i = 0; i < count; i++) {
                flag = false;
                if (dataList.get(i) != null && dataList.get(i).getQuestions() != null) {
                    QuestionEntity questionEntity = dataList.get(i).getQuestions();
                    int typeId = questionEntity.getType_id();
                    questionEntity.setPositionForCard(position);
                    questionEntity.setPageIndex(index);
                    if (questionEntity.getTemplate().equals(YanXiuConstant.MULTI_QUESTION)
                            || questionEntity.getTemplate().equals(YanXiuConstant.CLOZE_QUESTION)
                            || questionEntity.getTemplate().equals(YanXiuConstant.LISTEN_QUESTION)) {
                        List<PaperTestEntity> childQuestion = questionEntity.getChildren();
                        if (childQuestion != null) {
                            int childCount = childQuestion.size();
                            for (int j = 0; j < childCount; j++) {
                                QuestionEntity question = childQuestion.get(j).getQuestions();
                                question.setPositionForCard(position);
                                question.setPageIndex(index);
                                question.setChildPageIndex(j);
                                if (22 == typeId) {
                                    //只有是复合题且是解答题的时候，才会有childPositionForCard，否则childPositionForCard为-1
                                    question.setChildPositionForCard(j);
                                    //子题是解答题的时候，添加小题号
                                    int startIndex = question.getStem().indexOf("(");
                                    if (startIndex != 0)
                                        question.setStem("(" + (j + 1) + ")" + question.getStem());
                                } else {
                                    question.setChildPositionForCard(-1);
                                    position++;
                                    //如果是-1，下面不能再让position++
                                    flag = true;
                                }
                                question.setParent_type_id(questionEntity.getType_id());
                                questionList.add(question);
                            }
                        } else {
                            questionList.add(questionEntity);
                        }
                    } else {
                        questionEntity.setParent_type_id(questionEntity.getType_id());
                        questionList.add(questionEntity);
                    }
                    index++;
                    if (!flag)
                        position++;
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
                    List<PaperTestEntity> childQuestion = subjectEditionBean.getData().get(0).getPaperTest().get(i).getQuestions().getChildren();
                    if (childQuestion == null || childQuestion.isEmpty()) {
                        String template = subjectEditionBean.getData().get(0).getPaperTest().get(i).getQuestions().getTemplate();
                        if (YanXiuConstant.ANSWER_QUESTION.equals(template)) {
                            subjectEditionBean.getData().get(0).getPaperTest().get(i).getQuestions().setAnswerBean(new AnswerBean());
                        }
                    } else {
                        int chilidCount = childQuestion.size();
                        for (int j = 0; j < chilidCount; j++) {
                            String childTemplate = childQuestion.get(j).getQuestions().getTemplate();
                            if (YanXiuConstant.ANSWER_QUESTION.equals(childTemplate)) {
                                childQuestion.get(j).getQuestions().setAnswerBean(new AnswerBean());
                            }
                        }
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
        List<QuestionEntity> list = new ArrayList<QuestionEntity>();
        if (subjectEditionBean != null && subjectEditionBean.getData() != null && !subjectEditionBean.getData().isEmpty()) {
            List<PaperTestEntity> dataList = subjectEditionBean.getData().get(0).getPaperTest();
            if (dataList == null) {
                return null;
            }
            int count = dataList.size();
            for (int i = 0; i < count; i++) {
                QuestionEntity questionEntity;
                if (dataList.get(i) != null && dataList.get(i).getQuestions() != null) {
                    questionEntity = dataList.get(i).getQuestions();
                    if (questionEntity.getChildren() != null && questionEntity.getChildren().size() != 0) {
                        List<PaperTestEntity> paperList = questionEntity.getChildren();
                        int childCount = paperList.size();
                        for (int j = 0; j < childCount; j++) {
                            QuestionEntity child = paperList.get(j).getQuestions();
                            if (child != null) {
                                if (YanXiuConstant.ANSWER_QUESTION.equals(child.getTemplate())) {
                                    list.add(child);
                                }
                            }
                        }
                    } else {
                        if (YanXiuConstant.ANSWER_QUESTION.equals(questionEntity.getTemplate())) {
                            list.add(questionEntity);
                        }
                    }

                }
            }
        }
        return list;
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
                    String template = questionEntity.getTemplate();
                    if (questionEntity.getPad() == null)
                        continue;
                    String jsonAnswer = questionEntity.getPad().getJsonAnswer();

                    //如果是复合类的题
                    if (questionEntity.getTemplate().equals(YanXiuConstant.MULTI_QUESTION)
                            || questionEntity.getTemplate().equals(YanXiuConstant.CLOZE_QUESTION)
                            || questionEntity.getTemplate().equals(YanXiuConstant.LISTEN_QUESTION)) {

                        List<PaperTestEntity> paperList = questionEntity.getChildren();
                        if (paperList != null && !paperList.isEmpty()) {
                            int childrenCount = paperList.size();
                            for (int j = 0; j < childrenCount; j++) {
                                QuestionEntity childQuestion = paperList.get(j).getQuestions();
                                if(childQuestion == null || childQuestion.getPad()==null)
                                    continue;
                                List<String> answerChildList;
                                String childJsonAnswer =childQuestion.getPad().getJsonAnswer();
                                answerChildList = JSON.parseArray(childJsonAnswer,String.class);
                                List<String> rightAnswer = childQuestion.getAnswer();
                                AnswerBean answerChildBean = childQuestion.getAnswerBean();
                                int status = childQuestion.getPad().getStatus();
                                String childTemplate = childQuestion.getTemplate();
                                answerChildBean.setStatus(status);
                                //此处分为需要老师批改跟不需要老师批改两种情况处理（即主观题与非主观题）
                                if (!YanXiuConstant.ANSWER_QUESTION.equals(childTemplate)) {
                                    //非主观题（就是有正确答案，直接能在客户端判定的题）
                                    if (answerChildList != null && !answerChildList.isEmpty()) {
                                        if (YanXiuConstant.SINGLE_CHOICES.equals(childTemplate) || YanXiuConstant.JUDGE_QUESTION.equals(childTemplate)) {
                                            answerChildBean.setSelectType(answerChildList.get(0));
                                            if (rightAnswer != null && !rightAnswer.isEmpty()) {
                                                if (!TextUtils.isEmpty(answerChildList.get(0)) && answerChildList.get(0).equals(rightAnswer.get(0))) {
                                                    answerChildBean.setIsRight(true);
                                                } else {
                                                    answerChildBean.setIsRight(false);
                                                }
                                            }
                                        } else {
                                            if (YanXiuConstant.MULTI_CHOICES.equals(childTemplate)) {
                                                answerChildBean.setMultiSelect((ArrayList<String>) answerChildList);
                                            } else if (YanXiuConstant.FILL_BLANK.equals(childTemplate) || YanXiuConstant.NEW_FILL_BLANK.equals(childTemplate)) {
                                                answerChildBean.setFillAnswers((ArrayList<String>) answerChildList);
                                            }
                                            if (compare(answerChildList, rightAnswer)) {
                                                answerChildBean.setIsRight(true);
                                            } else {
                                                answerChildBean.setIsRight(false);
                                            }
                                        }
                                        answerChildBean.setIsFinish(true);
                                        //如果是填空题，需要再次确认一下是不是所有的空全填完了（没填完的空是""）
                                        if (YanXiuConstant.FILL_BLANK.equals(childTemplate) || YanXiuConstant.NEW_FILL_BLANK.equals(childTemplate)){
                                            for(String str: answerChildList){
                                                if (TextUtils.isEmpty(str)){
                                                    answerChildBean.setIsFinish(false);
                                                    break;
                                                }
                                            }
                                        }
                                    } else {
                                        answerChildBean.setIsFinish(false);
                                    }
                                } else {
                                    //如果是主观题
                                    if (paperList.get(j).getQuestions().getPad().getTeachercheck() != null) {
                                        int score = paperList.get(j).getQuestions().getPad().getTeachercheck().getScore();  //老师打的分数，数值范围0-5，0:错；0~5：半对； 5：对
                                        if (AnswerBean.ANSER_READED == status) {
                                            if (score == 0) {
                                                answerChildBean.setIsRight(false);
                                            } else if (score == 5) {
                                                answerChildBean.setIsRight(true);
                                            } else if (score > 0 && score < 5) {
                                                answerChildBean.setIsHalfRight(true);
                                            }
                                            answerChildBean.setIsFinish(true);
                                        }
                                    }
                                    if (answerChildList != null && !answerChildList.isEmpty()) {
                                        answerChildBean.setIsFinish(true);
                                        answerChildBean.setSubjectivImageUri((ArrayList<String>) answerChildList);
                                    }
                                    answerChildBean.setIsSubjective(true);
                                }
                            }
                        }
                    }else {
                        int status = questionEntity.getPad().getStatus();
                        int costTime = questionEntity.getPad().getCosttime();
                        AnswerBean answerBean = questionEntity.getAnswerBean();
                        answerBean.setConsumeTime(costTime);
                        answerBean.setStatus(status);
                        List<String> answerList = JSON.parseArray(jsonAnswer, String.class);
                        //此处分为需要老师批改跟不需要老师批改两种情况处理（即主观题与非主观题）
                        if (!YanXiuConstant.ANSWER_QUESTION.equals(template)) {
                            //非主观题
                            if (!YanXiuConstant.CONNECT_QUESTION.equals(template)&&!YanXiuConstant.CLASSIFY_QUESTION.equals(template)){
                                if (answerList != null && !answerList.isEmpty()) {
                                    if (status == AnswerBean.ANSER_RIGHT)
                                        answerBean.setIsRight(true);
                                    answerBean.setIsFinish(true);
                                    if (YanXiuConstant.SINGLE_CHOICES.equals(template)) {
                                        answerBean.setSelectType(answerList.get(0));
                                    } else if (YanXiuConstant.MULTI_CHOICES.equals(template)) {
                                        answerBean.setMultiSelect((ArrayList<String>) answerList);
                                    } else if (YanXiuConstant.JUDGE_QUESTION.equals(template)) {
                                        answerBean.setSelectType(answerList.get(0));
                                    } else if (YanXiuConstant.FILL_BLANK.equals(template) || YanXiuConstant.NEW_FILL_BLANK.equals(template)) {
                                        answerBean.setFillAnswers((ArrayList<String>) answerList);
                                    }
                                    //如果是填空题，需要再次确认一下是不是所有的空全填完了（没填完的空是""）
                                    if (YanXiuConstant.FILL_BLANK.equals(template) || YanXiuConstant.NEW_FILL_BLANK.equals(template)){
                                        for(String str: answerList){
                                            if (TextUtils.isEmpty(str)){
                                                answerBean.setIsFinish(false);
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    answerBean.setIsFinish(false);
                                }
                            }else {
                                try {
                                    JSONArray array=new JSONArray(jsonAnswer);
                                    for (int k=0;k<array.length();k++){
                                        String arrayJSONArray[]= array.getString(k).split(",");
                                        ArrayList<String> list=new ArrayList<String>();
                                        for (int l=0;l<arrayJSONArray.length;l++){
                                            if (!TextUtils.isEmpty(arrayJSONArray[l])) {
                                                list.add(arrayJSONArray[l]);
                                            }
                                        }
                                        answerBean.getConnect_classfy_answer().add(list);
                                    }



                                    if (answerBean.getConnect_classfy_answer().size()>0 ){
                                        int num = 0;
                                        for (ArrayList<String> listStr:answerBean.getConnect_classfy_answer()) {
                                            num = num + listStr.size();
                                        }
                                        if (YanXiuConstant.CLASSIFY_QUESTION.equals(template) && num < questionEntity.getContent().getChoices().size()) {
                                            answerBean.setIsFinish(false);
                                            answerBean.setIsRight(false);
                                        }else if (YanXiuConstant.CONNECT_QUESTION.equals(template) &&answerBean.getConnect_classfy_answer().size()<questionEntity.getAnswer().size()){
                                            answerBean.setIsFinish(false);
                                            answerBean.setIsRight(false);
                                        }else {
                                            answerBean.setIsFinish(true);
                                            List<String> list = questionEntity.getAnswer();
                                            answerBean.setIsRight(CheckConnect_classfy_answer(list, answerBean.getConnect_classfy_answer(), template));
                                        }
                                    }else {
                                        answerBean.setIsFinish(false);
                                        answerBean.setIsRight(false);
                                    }
                                }catch (Exception e){

                                }
                            }

                        } else {
                            //主观题
                            if (questionEntity.getPad().getTeachercheck() != null) {
                                int score = questionEntity.getPad().getTeachercheck().getScore();
                                switch (status) {
                                    //已批改状态，未批改的话不做处理
                                    case AnswerBean.ANSER_READED:
                                        if (score == 0) {
                                            answerBean.setIsRight(false);
                                        } else if (score == 5) {
                                            answerBean.setIsRight(true);
                                        } else if (score > 0 && score < 5) {
                                            answerBean.setIsHalfRight(true);
                                        }
                                        answerBean.setIsFinish(true);
                                        break;
                                }
                            }
                            if (answerList != null && !answerList.isEmpty()) {
                                answerBean.setIsFinish(true);
                                answerBean.setSubjectivImageUri((ArrayList<String>) answerList);
                            }
                            answerBean.setIsSubjective(true);

                        }
                    }
                }
            }
        }
    }

    public static boolean CheckConnect_classfy_answer(List<String> list,ArrayList<ArrayList<String>> answer_list,String template) {
        if (list.size()!=answer_list.size()){
            return false;
        }
        boolean flag=true;
        if (YanXiuConstant.CONNECT_QUESTION.equals(template)){
            for (String s:list){
                try {
                    JSONObject object=new JSONObject(s);
                    String s1[]=object.getString("answer").split(",");
                    for (ArrayList<String> stringArrayList:answer_list){
                        if (s1[0].equals(stringArrayList.get(0))){
                            if (!s1[1].equals(stringArrayList.get(1))){
                                flag=false;
                            }
                        }else if (s1[0].equals(stringArrayList.get(1))){
                            if (!s1[1].equals(stringArrayList.get(0))){
                                flag=false;
                            }
                        }
                    }
                }catch (Exception e){
                    flag=false;
                }
            }
        }else if (YanXiuConstant.CLASSIFY_QUESTION.equals(template)){
            for (int i=0;i<list.size();i++){
                try {
                    String s=list.get(i);
                    JSONObject object=new JSONObject(s);
                    String s1[]=object.getString("answer").split(",");
                    ArrayList<String> stringArrayList=answer_list.get(i);
                    if (s1.length!=stringArrayList.size()){
                        flag=false;
                    }else {
                        for (String ss:s1){
                            boolean cla=false;
                            for (String sss:stringArrayList){
                                if (ss.equals(sss)){
                                    cla=true;
                                }
                            }
                            if (!cla){
                                flag=false;
                            }
                        }
                    }
                }catch (Exception e){
                    flag=false;
                }
            }
        }
        return flag;
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
            if (dataList.get(i).getQuestions().getTemplate().equals(YanXiuConstant.MULTI_QUESTION)
                    || dataList.get(i).getQuestions().getTemplate().equals(YanXiuConstant.CLOZE_QUESTION)
                    || dataList.get(i).getQuestions().getTemplate().equals(YanXiuConstant.LISTEN_QUESTION)) {
                List<PaperTestEntity> questionList = dataList.get(i).getQuestions().getChildren();
                if (questionList == null)
                    continue;
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
                    if (questionList == null)
                        continue;
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
                if (questionsEntity != null && questionsEntity.getPad() != null && questionsEntity.getPad().getTeachercheck() != null && questionsEntity.getPad().getStatus() == AnswerBean.ANSER_READED) {
                    totalScore += questionsEntity.getPad().getTeachercheck().getScore();
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

    /**
     * 计算正确率,计算规则：主观题只有被批改之后，才会计入在内
     *
     * @param list 拆分处理之后的单题集合
     * @return
     */
    public static float calculateRightRate(List<QuestionEntity> list) {
        if (list == null || list.size() == 0)
            return 0;
        float totalCount = 0;
        float rightCount = 0;
        float result = 0;
        for (QuestionEntity entity : list) {
            if (entity == null)
                continue;
            if (YanXiuConstant.ANSWER_QUESTION.equals(entity.getTemplate())) {
                if (AnswerBean.ANSER_READED == entity.getAnswerBean().getRealStatus()) {
                    if (entity.getAnswerBean().isRight()) {
                        rightCount += 1;
                    } else if (entity.getAnswerBean().isHalfRight()) {
                        //半对状态算50%正确率
                        rightCount += 0.5;
                    }
                    totalCount += 1;
                }
            } else {
                if (entity.getAnswerBean().isRight()) {
                    rightCount += 1;
                }
                totalCount += 1;
            }
        }
        return Float.parseFloat(String.format("%.2f", rightCount / totalCount));
    }

    /**
     * 计算做对的题目数
     *
     * @param list
     * @return
     */
    public static int calculateRightCount(List<QuestionEntity> list) {
        if (list == null || list.size() == 0)
            return 0;
        int rightCount = 0;
        for (QuestionEntity entity : list) {
            if (entity == null)
                continue;
            if (entity.getAnswerBean().isRight()) {
                rightCount += 1;
            }
        }
        return rightCount;
    }

    /**
     * 对题目按照题型进行分类
     *
     * @param list
     * @return
     */
    public static Map<String, List<QuestionEntity>> classifyQuestionByType(List<QuestionEntity> list) {
        if (list == null || list.size() == 0)
            return null;
        TreeMap<String, List<QuestionEntity>> treeMap = new TreeMap<>(new QuestionTypeComparator());
        int count = list.size();
        QuestionEntity questionEntity;
        for (int i = 0; i < count; i++) {
            questionEntity = list.get(i);
            String typeName = getQuestionTypeNameByParentTypeId(questionEntity.getParent_type_id());
            if (!treeMap.containsKey(typeName)) {
                List<QuestionEntity> tempList = new ArrayList<>();
                tempList.add(questionEntity);
                treeMap.put(typeName, tempList);
            } else {
                List<QuestionEntity> valueList = treeMap.get(typeName);
                valueList.add(questionEntity);
            }
        }
        return treeMap;
    }

    public static String getQuestionTypeNameByParentTypeId(int typeId) {
        String name = "";
        switch (typeId) {
            case 1:
                name = "单选题";
                break;
            case 2:
                name = "多选题";
                break;
            case 3:
                name = "填空题";
                break;
            case 4:
                name = "判断题";
                break;
            case 5:
                name = "材料阅读";
                break;
            case 6:
                name = "问答题";
                break;
            case 7:
                name = "连线题";
                break;
            case 8:
                name = "计算题";
                break;
            case 13:
                name = "归类题";
                break;
            case 14:
                name = "阅读理解";
                break;
            case 15:
                name = "完形填空";
                break;
            case 16:
                name = "翻译题";
                break;
            case 17:
                name = "改错题";
                break;
            case 20:
                name = "排序题";
                break;
            case 22:
                name = "解答题";
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 18:
            case 19:
            case 21:
                name = "听力题";
                break;
            default:
                break;

        }
        return name;
    }

    public static int getIntValue(String str) {
        int result = 0;
        switch (str) {
            case "听力题":
                result = 1;
                break;
            case "单选题":
                result = 2;
                break;
            case "多选题":
                result = 3;
                break;
            case "判断题":
                result = 4;
                break;
            case "连线题":
                result = 5;
                break;
            case "归类题":
                result = 6;
                break;
            case "排序题":
                result = 7;
                break;
            case "完形填空":
                result = 8;
                break;
            case "阅读理解":
                result = 9;
                break;
            case "填空题":
                result = 10;
                break;
            case "改错题":
                result = 11;
                break;
            case "翻译题":
                result = 12;
                break;
            case "计算题":
                result = 13;
                break;
            case "解答题":
                result = 14;
                break;
            case "问答题":
                result = 15;
                break;
            case "材料阅读":
                result = 16;
                break;

        }
        return result;
    }

    public static int getBmpResIdByName(String str) {
        int result = 0;
        switch (str) {
            case "听力题":
                result = R.drawable.tingliti;
                break;
            case "单选题":
                result = R.drawable.danxuanti;
                break;
            case "多选题":
                result = R.drawable.duoxuanti;
                break;
            case "判断题":
                result = R.drawable.panduanti;
                break;
            case "连线题":
                result = R.drawable.lianxianti;
                break;
            case "归类题":
                result = R.drawable.guileiti;
                break;
            case "排序题":
                result = R.drawable.paixuti;
                break;
            case "完形填空":
                result = R.drawable.wanxingtiankong;
                break;
            case "阅读理解":
                result = R.drawable.yuedulijie;
                break;
            case "填空题":
                result = R.drawable.tiankongti;
                break;
            case "改错题":
                result = R.drawable.gaicuoti;
                break;
            case "翻译题":
                result = R.drawable.fanyiti;
                break;
            case "计算题":
                result = R.drawable.jisuanti;
                break;
            case "解答题":
                result = R.drawable.jiedati;
                break;
            case "问答题":
                result = R.drawable.wendati;
                break;
            case "材料阅读":
                result = R.drawable.cailiaoyuedu;
                break;

        }
        return result;
    }

    public static class QuestionTypeComparator implements Comparator<String> {

        @Override
        public int compare(String lhs, String rhs) {
            return getIntValue(lhs) - getIntValue(rhs);
        }
    }

}
