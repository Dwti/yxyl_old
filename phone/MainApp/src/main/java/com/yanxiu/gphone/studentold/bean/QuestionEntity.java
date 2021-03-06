package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/24.
 */
public class QuestionEntity implements YanxiuBaseBean {
    //父题第几页
    private int parentIndex = -1;

    public int getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(int parentIndex) {
        this.parentIndex = parentIndex;
    }

    //智能答题第几页 真实的位置，如果有children 就是父题的位置，没有的话就是子题的位置
    private int pageIndex = -1;

    private int questionIndex;

    private int difficulty;

    //child 第几页
    private int childPageIndex = -1;

    private int childPageNumber = -1;

    private int positionForCard = -1;   //此位置用于答题卡题号显示
    private int childPositionForCard = -1; //此位置用于答题卡题号显示

    private String template;
    private List<String> answer;
    private int parent_type_id=-1;  //父题的typeid，如果当前题目没有子题的话，parent_type_id等于type_id (此字段主要提供给答题报告按题型分类的时候用)
    private int type_id;
    private String id;
    private String qid;       //PaperTest的qid
    private String wqid;     //PaperTest的wqid(如果有子题的话，是父题的wqid)

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
    private NoteBean jsonNote = new NoteBean();

    private ExtendEntity extend;

    public static final String TYPE_SUBMIT="0";
    public static final String TYPE_SUBMIT_END="1";
    public static final String TYPE_DELETE_END="2";

    /**0、未提交1、已提交2、已删除*/
    private String Type="0";

    public static final int ANSWER_DEFULT=0;
    public static final int ANSWER_RIGHT=1;
    public static final int ANSWER_FAIL=2;

    /**0默认1正确2错误*/
    private int AnswerIsRight=0;

    /**true 有答案 false 没有答案*/
    private boolean isHaveAnser=false;

    public boolean isHaveAnser() {
        return isHaveAnser;
    }

    public void setIsAllBlanksFilled(boolean haveAnser) {
        isHaveAnser = haveAnser;
    }

    public int getAnswerIsRight() {
        return AnswerIsRight;
    }

    public void setAnswerIsRight(int answerIsRight) {
        AnswerIsRight = answerIsRight;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

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

    public int getChildPageNumber() {
        return childPageNumber;
    }

    public void setChildPageNumber(int childPageNumber) {
        this.childPageNumber = childPageNumber;
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

    public int getPositionForCard() {
        return positionForCard;
    }

    public void setPositionForCard(int positionForCard) {
        this.positionForCard = positionForCard;
    }

    public int getChildPositionForCard() {
        return childPositionForCard;
    }

    public void setChildPositionForCard(int childPositionForCard) {
        this.childPositionForCard = childPositionForCard;
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

//        public List<String> getAnswerList() {
//            return answer;
//        }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getParent_type_id() {
        return parent_type_id;
    }

    public void setParent_type_id(int parent_type_id) {
        this.parent_type_id = parent_type_id;
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

    public NoteBean getJsonNote() {
        return jsonNote;
    }

    public void setJsonNote(NoteBean jsonNote) {
        this.jsonNote = jsonNote;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getWqid() {
        return wqid;
    }

    public void setWqid(String wqid) {
        this.wqid = wqid;
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
