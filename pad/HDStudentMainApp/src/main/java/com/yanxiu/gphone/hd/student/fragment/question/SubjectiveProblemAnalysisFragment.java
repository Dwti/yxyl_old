package com.yanxiu.gphone.hd.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.view.flowview.FlowLayout;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.BaseAnswerViewActivity;
import com.yanxiu.gphone.hd.student.activity.PhotoViewActivity;
import com.yanxiu.gphone.hd.student.adapter.SubjectiveImageAdapter;
import com.yanxiu.gphone.hd.student.bean.AnswerBean;
import com.yanxiu.gphone.hd.student.bean.QuestionEntity;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.feedBack.AbstractFeedBack;
import com.yanxiu.gphone.hd.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.hd.student.view.question.subjective.SubjectiveHeartLayout;
import com.yanxiu.gphone.hd.student.view.question.subjective.SubjectiveStarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lidm on 2015/9/25.
 *   主观题的题目分析界面
 */
public class SubjectiveProblemAnalysisFragment extends Fragment implements View.OnClickListener{

    private YXiuAnserTextView tvKnowledgePoint;
    private YXiuAnserTextView tvReportParseText;
    private YXiuAnserTextView tvCorrectionResultText;
//    private YXiuAnserTextView tvMyAnswer;
//    private ReadingQuestionsFragment rlAnswerPen;

    private SubjectiveHeartLayout subjectiveStarLayout;
//
    private YXiuAnserTextView tvDifficulltyText;

    private SubjectiveStarLayout difficultyStart;

    private View rootView;
    private QuestionEntity questionsEntity;

    private FlowLayout flowLayout;

    private LinearLayout llParseKnowledge;
    private LinearLayout llReportParse;
    private LinearLayout llDifficullty;

    private RelativeLayout rlSubjectNoanswer;

    private TextView tvReportQuestionError;

    private RelativeLayout correctResultTitle, correctResultContent;

    private String qid;

    private GridView subjectiveGrid;
    private SubjectiveImageAdapter adapter;
    private List<String> photosList;

    //add
    private ImageView ivIcon;
    private FrameLayout flCorrectionContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.questionsEntity = (getArguments() != null) ? (QuestionEntity) getArguments().getSerializable("questions") : null;
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.hw_subjective_report_parse_bottom, null);
        initView();
        initData();
        return rootView;
    }

    private void initView(){
        tvKnowledgePoint = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_knowledge_point);
        tvReportParseText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_text);
        tvCorrectionResultText = (YXiuAnserTextView) rootView.findViewById(R.id.correction_result_text);

        rlSubjectNoanswer = (RelativeLayout) rootView.findViewById(R.id.rl_subject_noanswer);

        tvReportQuestionError = (TextView) rootView.findViewById(R.id.tv_report_question_error);

//        tvMyAnswer = (YXiuAnserTextView) rootView.findViewById(R.id.my_anwsers_text);

        tvDifficulltyText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_difficullty_text);

        subjectiveStarLayout = (SubjectiveHeartLayout) rootView.findViewById(R.id.view_correction_result);

        difficultyStart = (SubjectiveStarLayout) rootView.findViewById(R.id.view_sub_difficulty_star);
        llDifficullty = (LinearLayout) rootView.findViewById(R.id.hw_report_difficullty_layout);

        llReportParse = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_layout);

        llParseKnowledge = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_knowledge_layout);

        correctResultTitle = (RelativeLayout) rootView.findViewById(R.id.correcting_result_title);
        correctResultContent = (RelativeLayout) rootView.findViewById(R.id.correcting_result_content);

        subjectiveGrid = (GridView) rootView.findViewById(R.id.subjective_questions_grid);
        adapter = new SubjectiveImageAdapter(this.getActivity());
        subjectiveGrid.setAdapter(adapter);

        flowLayout = (FlowLayout) rootView.findViewById(R.id.knowledge_flow_layout);

        tvReportQuestionError.setOnClickListener(this);

        ivIcon = (ImageView) rootView.findViewById(R.id.icon_correction_result);
        flCorrectionContent = (FrameLayout) rootView.findViewById(R.id.fl_correction_result_content);

    }

    private void addPointBtn(final QuestionEntity.PointEntity pointEntity){
        View knowledgeView = LayoutInflater.from(this.getActivity()).inflate(R.layout.item_knowledge, null);
        knowledgeView.setFocusable(false);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(Util.dipToPx(10), Util.dipToPx(10), 0, 0);
        TextView tvKnowlegdeName = (TextView) knowledgeView.findViewById(R.id.tv_knowledge_name);
        if(pointEntity != null && !TextUtils.isEmpty(pointEntity.getName())){
            tvKnowlegdeName.setText(pointEntity.getName());
        }
        knowledgeView.setLayoutParams(params);

        flowLayout.addView(knowledgeView);
    }


    private void initData(){
        if(questionsEntity != null){

            photosList = questionsEntity.getAnswerBean().getSubjectivImageUri();
            adapter.addMoreData(photosList);

            if(photosList == null || photosList.isEmpty()){
                subjectiveGrid.setVisibility(View.GONE);
                rlSubjectNoanswer.setVisibility(View.VISIBLE);
//                tvMyAnswer.setTextHtml(this.getActivity().getResources().getString(R.string.subjective_questions_unanwser));
            }else{
                subjectiveGrid.setVisibility(View.VISIBLE);
                rlSubjectNoanswer.setVisibility(View.GONE);
            }

            subjectiveGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PhotoViewActivity.launch(SubjectiveProblemAnalysisFragment.this.getActivity(), (ArrayList<String>) photosList, position);
                }
            });

            if(questionsEntity.getPadBean() != null && questionsEntity.getPadBean().getTeachercheck() != null && questionsEntity.getPadBean().getStatus() == AnswerBean.ANSER_READED){
                subjectiveStarLayout.selectStarCount(questionsEntity.getPadBean().getTeachercheck().getScore());
                if(!TextUtils.isEmpty(questionsEntity.getPadBean().getTeachercheck().getQcomment())){
                    tvCorrectionResultText.setTextHtml(questionsEntity.getPadBean().getTeachercheck().getQcomment());
                }else{
                    ivIcon.setVisibility(View.GONE);
                    flCorrectionContent.setVisibility(View.GONE);
                }
            }else{
                subjectiveStarLayout.setVisibility(View.GONE);
                ivIcon.setVisibility(View.GONE);
                flCorrectionContent.setBackgroundColor(this.getActivity().getResources().getColor(android.R.color.transparent));
                tvCorrectionResultText.setTextHtml(this.getActivity().getResources().getString(R.string.subjective_questions_unread));
            }

            if(questionsEntity != null){
                difficultyStart.selectStarCount(questionsEntity.getDifficulty());
                tvDifficulltyText.setTextHtml(getTypeKey(String.valueOf(questionsEntity.getDifficulty())));
//                Util.getDataRelationMap(String.valueOf(questionsEntity.getDifficulty()));
            }
//            else{
//                llDifficullty.setVisibility(View.GONE);
//            }


//            if(questionsEntity.getPadBean() != null && questionsEntity.getPadBean().ge() != null && questionsEntity.getPadBean().getStatus() == AnswerBean.ANSER_READED){
//                subjectiveStarLayout.selectStarCount(questionsEntity.getPadBean().getTeachercheck().getScore());
//                tvCorrectionResultText.setTextHtml(questionsEntity.getPadBean().getTeachercheck().getQcomment());
//            }else{
//                subjectiveStarLayout.setVisibility(View.GONE);
//                tvCorrectionResultText.setTextHtml(this.getActivity().getResources().getString(R.string.subjective_questions_unread));
//            }


           if(questionsEntity.getPoint() != null && !questionsEntity.getPoint().isEmpty()){
                List<QuestionEntity.PointEntity> pointList = questionsEntity.getPoint();
                int count = pointList.size();
//                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < count; i++){
                    addPointBtn(pointList.get(i));
//                    sb.append(pointList.get(i).getName());
//                    if(i != count - 1){
//                        sb.append(",");
//                    }
                }
//               if(TextUtils.isEmpty(sb.toString())){
//                   llParseKnowledge.setVisibility(View.GONE);
//               }else{
//                   tvKnowledgePoint.setTextHtml(sb.toString());
//               }
           }else{
               llParseKnowledge.setVisibility(View.GONE);
           }
            if(questionsEntity.getAnalysis() != null && !TextUtils.isEmpty(questionsEntity.getAnalysis())){
                tvReportParseText.setTextHtml(questionsEntity.getAnalysis());
            }else{
                llReportParse.setVisibility(View.GONE);
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


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if(tvReportQuestionError == v){
            SubjectExercisesItemBean dataSources = ((BaseAnswerViewActivity)this.getActivity()).getDataSources();

            if(dataSources != null && dataSources.getData() != null &&
                    dataSources.getData().get(0) != null &&
                    dataSources.getData().get(0).getPaperTest() != null &&
                    !dataSources.getData().get(0).getPaperTest().isEmpty()){
                qid = dataSources.getData().get(0).getPaperTest().get(((BaseAnswerViewActivity)this.getActivity()).getCurrentIndex()).getQid() + "";
                LogInfo.log("geny", "subject question qid----" + qid);
            }
            ActivityJumpUtils.jumpToFeedBackActivity(this.getActivity(), qid, AbstractFeedBack.ERROR_FEED_BACK);
            (this.getActivity()).overridePendingTransition(R.anim.fade, R.anim.hold);
        }
    }


}
