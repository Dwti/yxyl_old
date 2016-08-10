package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/24.
 */
public class QuestionEntity implements YanxiuBaseBean {
    //智能答题第几页
    private int pageIndex = -1;

    private int questionIndex;

    private int difficulty;

    //child 第几页
    private int childPageIndex = -1;

    private String template;
    private List<String> answer;
    private int type_id;
    private String id;

    public QuestionEntity getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionEntity questions) {
        this.questions = questions;
    }

    private String analysis;
    private ContentEntity content;
    private List<PointEntity> point;
    private String stem;
    private List<PaperTestEntity> children;
    private QuestionEntity questions;
    private AnswerBean answerBean ;//= new AnswerBean();
    private String titleName;
    private String url;   //听力的url播放地址
    private List<String> photoUri;

    private ExtendEntity extend;

    public PadBean getPad() {
        return pad;
    }

    public void setPad(PadBean pad) {
        this.pad = pad;
    }

    private PadBean pad;
    private boolean readQuestion = false;
    private String readItemName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getChildPageIndex() {
        return childPageIndex;
    }

    public void setChildPageIndex(int childPageIndex) {
        this.childPageIndex = childPageIndex;
    }

    public ExtendEntity getExtend() {
        return extend;
    }

    public void setExtend(ExtendEntity extend) {
        this.extend = extend;
    }

    public List<PaperTestEntity> getChildren() {
        return children;
    }

    public void setChildren(List<PaperTestEntity> children) {
        this.children = children;
    }

    public AnswerBean getAnswerBean() {
        if(answerBean == null){
            answerBean = new AnswerBean();
        }
        return answerBean;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    public List<String> getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(List<String> photoUri) {
        this.photoUri = photoUri;
    }

    public void setAnswerBean(AnswerBean answerBean) {
        this.answerBean = answerBean;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public void setContent(ContentEntity content) {
        this.content = content;
    }

    public void setPoint(List<PointEntity> point) {
        this.point = point;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public String getTemplate() {
        return template;
    }

//        public List<String> getAnswer() {
//            return answer;
//        }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getType_id() {
        return type_id;
    }

    public String getId() {
        return id;
    }

    public String getAnalysis() {
        return analysis;
    }

    public ContentEntity getContent() {
        return content;
    }

    public List<PointEntity> getPoint() {
        return point;
    }

    public String getStem() {
        return stem;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public void setReadQuestion(boolean readQuestion) {
        this.readQuestion = readQuestion;
    }

    public boolean isReadQuestion() {
        return readQuestion;
    }

    public void setReadItemName(String readItemName) {
        this.readItemName = readItemName;
    }

    public String getReadItemName() {
        return readItemName;
    }

    public class ContentEntity  implements YanxiuBaseBean {

        private List<String> choices;

        public void setChoices(List<String> choices) {
            this.choices = choices;
        }

        public List<String> getChoices() {
            return choices;
        }
    }

    @Override
    public String toString() {
        return "QuestionEntity{" +
                "template='" + template + '\'' +
                ", type_id=" + type_id +
                ", id='" + id + '\'' +
                ", analysis='" + analysis + '\'' +
                ", stem='" + stem + '\'' +
                '}';
    }

    public class PointEntity  implements YanxiuBaseBean {
        /**
         * name : 等差数列
         * id : 1449
         */
        private String name;
        private String id;

        public void setName(String name) {
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }
}
