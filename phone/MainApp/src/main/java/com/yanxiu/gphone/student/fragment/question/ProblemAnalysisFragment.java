package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.view.flowview.FlowLayout;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.student.activity.BaseAnswerViewActivity;
import com.yanxiu.gphone.student.bean.ExercisesDataEntity;
import com.yanxiu.gphone.student.bean.ExtendEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.feedBack.AbstractFeedBack;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.student.requestTask.RequestKnpointQBlockTask;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.student.view.question.classfy.ClassfyQuestions;
import com.yanxiu.gphone.student.view.question.subjective.SubjectiveStarLayout;

import java.util.List;
import java.util.Map;

import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_COMPUTE;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SOLVE_COMPLEX;

/**
 * Created by Administrator on 2015/7/7.
 * 题目分析界面
 */
public class ProblemAnalysisFragment extends Fragment implements View.OnClickListener{
    protected int stageId;
    protected String subjectId;
    protected String subjectName;
    protected String editionId;
    protected String editionName;
    protected String volume;
    protected String volumeName;
    protected String chapterId;
    protected String chapterName;
    protected String sectionId;
    protected String sectionName;
    protected String cellid = "0";
    protected String cellName = "";
    protected boolean isTestCenterOnclick = false;


    private YXiuAnserTextView tvKnowledgePoint;
    private YXiuAnserTextView tvReportParseText;

    private YXiuAnserTextView tvReportParseStatueText;
    private YXiuAnserTextView tvReportParseStatisticsText;

    private View rootView;
    private QuestionEntity questionsEntity;

    private YXiuAnserTextView tvDifficulltyText;

    private YXiuAnserTextView tvAnswerText;

    private SubjectiveStarLayout difficultyStart;

    private TextView tvReportQuestionError;

    private FlowLayout flowLayout;
//    protected StudentLoadingLayout loadingLayout;

    private String qid;
    private SubjectExercisesItemBean subjectExercisesItemBean;

    private LinearLayout llReportParseStatue;
    private LinearLayout llReportParseStatistics;
    private LinearLayout llParseKnowledge;
    private LinearLayout llReportParse;
    private LinearLayout llDifficullty;
    private LinearLayout llAnswer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.questionsEntity = (getArguments() != null) ? (QuestionEntity) getArguments().getSerializable("questions") : null;
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.hw_report_parse_bottom,null);
        initView();
        initData();
        return rootView;
    }

    private void initView(){
        tvKnowledgePoint = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_knowledge_point);
        tvReportParseText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_text);
        tvReportParseStatueText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_statue_text);
        tvReportParseStatisticsText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_statistics_text);
        tvDifficulltyText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_difficullty_text);
        tvAnswerText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_answer_text);

        tvReportQuestionError = (TextView) rootView.findViewById(R.id.tv_report_question_error);

        difficultyStart = (SubjectiveStarLayout) rootView.findViewById(R.id.view_sub_difficulty_star);
        llDifficullty = (LinearLayout) rootView.findViewById(R.id.hw_report_difficullty_layout);
        llAnswer = (LinearLayout) rootView.findViewById(R.id.hw_report_answer_layout);

        llReportParseStatue = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_statue_layout);
        llReportParseStatistics = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_statistics_layout);
        llParseKnowledge = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_knowledge_layout);
        llReportParse = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_layout);

        flowLayout = (FlowLayout) rootView.findViewById(R.id.knowledge_flow_layout);


        tvReportQuestionError.setOnClickListener(this);

    }


    private void initData(){
        SubjectExercisesItemBean dataSources = ((BaseAnswerViewActivity)this.getActivity()).getDataSources();
        if (dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()) {
            stageId = LoginModel.getUserinfoEntity().getStageid();
            subjectId = dataSources.getData().get(0).getSubjectid();
            subjectName = dataSources.getData().get(0).getSubjectName();
            editionId = dataSources.getData().get(0).getBedition();
            editionName = dataSources.getData().get(0).getEditionName();
            volume = dataSources.getData().get(0).getVolume();
            volumeName = dataSources.getData().get(0).getVolumeName();
            chapterId = dataSources.getData().get(0).getChapterid();
            chapterName = dataSources.getData().get(0).getChapterName();
            sectionId = dataSources.getData().get(0).getSectionid();
            sectionName = dataSources.getData().get(0).getSectionName();
            cellid = dataSources.getData().get(0).getCellid();
            cellName = dataSources.getData().get(0).getCellName();
            isTestCenterOnclick = dataSources.getData().get(0).isTestCenterOnclick();

        }

        if(dataSources != null && dataSources.getData() != null &&
                                dataSources.getData().get(0) != null &&
                                dataSources.getData().get(0).getPaperTest() != null &&
                                !dataSources.getData().get(0).getPaperTest().isEmpty()){
            qid = dataSources.getData().get(0).getPaperTest().get(((BaseAnswerViewActivity)this.getActivity()).getCurrentIndex()).getQid() + "";

        }

        if(questionsEntity != null){
           if(questionsEntity.getPoint() != null && !questionsEntity.getPoint().isEmpty()){
                List<QuestionEntity.PointEntity> pointList = questionsEntity.getPoint();
                int count = pointList.size();
                for(int i = 0; i < count; i++){
                    addPointBtn(pointList.get(i));
                }
           }else{
               llParseKnowledge.setVisibility(View.GONE);
           }

            if(questionsEntity != null){
                difficultyStart.selectStarCount(questionsEntity.getDifficulty());
                tvDifficulltyText.setTextHtml(getTypeKey(String.valueOf(questionsEntity.getDifficulty())));
                tvDifficulltyText.setVisibility(View.GONE);
            }
            if (questionsEntity.getTemplate().equals(YanXiuConstant.ANSWER_QUESTION) || questionsEntity.getType_id() == QUESTION_COMPUTE.type) {
                if (questionsEntity.getAnswer() != null && questionsEntity.getAnswer().size() > 0) {
                    StringBuffer answerString = new StringBuffer();
                    for (String str : questionsEntity.getAnswer()) {
                        answerString.append(str);
                    }
                    if (!TextUtils.isEmpty(answerString.toString())) {
                        tvAnswerText.setTextHtml(answerString.toString());
                        llAnswer.setVisibility(View.VISIBLE);
                    }
                }
            }
            if(questionsEntity.getAnalysis() != null && !TextUtils.isEmpty(questionsEntity.getAnalysis())){
                tvReportParseText.setTextHtml(questionsEntity.getAnalysis());
            }else{
                llReportParse.setVisibility(View.GONE);
            }
            if(questionsEntity.getExtend() != null && questionsEntity.getExtend().getData() != null){
                ExtendEntity.DataEntity dataEntity = questionsEntity.getExtend().getData();
                if(!TextUtils.isEmpty(dataEntity.getAnswerCompare())){
                    if (questionsEntity.getTemplate().equals(YanXiuConstant.CLASSIFY_QUESTION)) {
                        tvReportParseStatueText.setClasfyFlag(false);
                        tvReportParseStatueText.setTextHtml(dataEntity.getAnswerCompare().replaceAll("<img","<imgFy"));
                    } else {
                        tvReportParseStatueText.setTextHtml(dataEntity.getAnswerCompare());
                    }
                }else{
                    llReportParseStatue.setVisibility(View.GONE);
                }
                if(!TextUtils.isEmpty(dataEntity.getGlobalStatis())){

                    tvReportParseStatisticsText.setTextHtml(dataEntity.getGlobalStatis());
                }else{
                    llReportParseStatistics.setVisibility(View.GONE);
                }

            }else{
                llReportParseStatue.setVisibility(View.GONE);
                llReportParseStatistics.setVisibility(View.GONE);
            }
        }


    }
    private String getTypeKey(String key){
        Map<String, String> relation = CommonCoreUtil.getDataRelationMap(this.getActivity(), R.array.analysis_list);
        String value = relation.get(key);
        if(TextUtils.isEmpty(value)){
            value = "";
            return value;
        }
        return value;
    }


    private void addPointBtn(final QuestionEntity.PointEntity pointEntity){
        View knowledgeView = LayoutInflater.from(this.getActivity()).inflate(R.layout.item_knowledge, null);
        knowledgeView.setFocusable(isTestCenterOnclick);
        if(isTestCenterOnclick){
            tvKnowledgePoint.setVisibility(View.VISIBLE);
        }
        TextView tvKnowlegdeName = (TextView) knowledgeView.findViewById(R.id.tv_knowledge_name);
        if(pointEntity != null && !TextUtils.isEmpty(pointEntity.getName())){
            tvKnowlegdeName.setText(pointEntity.getName());
            if (knowledgeView != null && isTestCenterOnclick) {
                knowledgeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestSubjectKnpExercises(pointEntity.getId());
                    }
                });
            }
        }

        flowLayout.addView(knowledgeView);
    }

    protected void requestSubjectKnpExercises(String pointId) {
        LogInfo.log("geny", "requestSubjectExercises");
        ((BaseAnswerViewActivity)this.getActivity()).showDialog();
        new RequestKnpointQBlockTask(this.getActivity(), stageId, subjectId, pointId, "", "", RequestKnpointQBlockTask.ANA_QUSETION, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                subjectExercisesItemBean = (SubjectExercisesItemBean) result;
                if (subjectExercisesItemBean.getData() != null && subjectExercisesItemBean.getData().get(0).getPaperTest() != null && !subjectExercisesItemBean.getData().get(0).getPaperTest().isEmpty()) {
                    if (subjectExercisesItemBean.getData() != null && !subjectExercisesItemBean.getData().isEmpty()) {
                        if (subjectExercisesItemBean.getData() != null && !subjectExercisesItemBean.getData().isEmpty()) {
                            subjectExercisesItemBean.getData().get(0).setStageid(stageId + "");
                            subjectExercisesItemBean.getData().get(0).setSubjectid(subjectId);
                            subjectExercisesItemBean.getData().get(0).setSubjectName(subjectName);

                            subjectExercisesItemBean.getData().get(0).setBedition(editionId);
                            subjectExercisesItemBean.getData().get(0).setEditionName(editionName);

                            subjectExercisesItemBean.getData().get(0).setVolume(volume);
                            subjectExercisesItemBean.getData().get(0).setVolumeName(volumeName);

                            subjectExercisesItemBean.getData().get(0).setChapterid(chapterId);
                            subjectExercisesItemBean.getData().get(0).setChapterName(chapterName);

                            subjectExercisesItemBean.getData().get(0).setSectionid(sectionId);
                            subjectExercisesItemBean.getData().get(0).setSectionName(sectionName);

                            subjectExercisesItemBean.getData().get(0).setCellid(cellid);
                            subjectExercisesItemBean.getData().get(0).setCellName(cellName);

                            subjectExercisesItemBean.getData().get(0).setIsChapterSection(ExercisesDataEntity.CHAPTER);
                            subjectExercisesItemBean.getData().get(0).setIsTestCenterOnclick(isTestCenterOnclick);


                            AnswerViewActivity.launch(ProblemAnalysisFragment.this.getActivity(), subjectExercisesItemBean, YanXiuConstant.KPN_REPORT);
                        }
                    }
                } else {
                    if (subjectExercisesItemBean != null && subjectExercisesItemBean.getStatus() != null && subjectExercisesItemBean.getStatus().getDesc() != null) {
                       Util.showToast(subjectExercisesItemBean.getStatus().getDesc());
                    } else {
                        Util.showToast(R.string.server_connection_erro);
                    }
                }
                ((BaseAnswerViewActivity)ProblemAnalysisFragment.this.getActivity()).hideDialog();
            }

            @Override
            public void dataError(int type, String msg) {
                LogInfo.log("geny", "dataError type =====" + type);
                if (TextUtils.isEmpty(msg)) {
                    Util.showToast(R.string.server_connection_erro);
                } else {
                    Util.showToast(msg);
                }
                ((BaseAnswerViewActivity)ProblemAnalysisFragment.this.getActivity()).hideDialog();
            }
        }).start();

    }



    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        if(tvReportQuestionError == v){
            ActivityJumpUtils.jumpToFeedBackActivity(this.getActivity(), qid, AbstractFeedBack.ERROR_FEED_BACK);
            (this.getActivity()).overridePendingTransition(R.anim.fade, R.anim.hold);
        }
    }
}
