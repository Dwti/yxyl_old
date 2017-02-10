package com.yanxiu.gphone.student.fragment.question;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.view.flowview.FlowLayout;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.BaseAnswerViewActivity;
import com.yanxiu.gphone.student.activity.PhotoViewActivity;
import com.yanxiu.gphone.student.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.student.adapter.SubjectiveImageAdapter;
import com.yanxiu.gphone.student.adapter.AudioCommentAdapter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.AudioCommentBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.feedBack.AbstractFeedBack;
import com.yanxiu.gphone.student.inter.CorpListener;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.student.utils.CorpUtils;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.SimpleVoicePlayer;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.student.view.question.subjective.SubjectiveHeartLayout;
import com.yanxiu.gphone.student.view.question.subjective.SubjectiveStarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_COMPUTE;

/**
 * Created by lidm on 2015/9/25.
 * 主观题的题目分析界面
 */
public class SubjectiveProblemAnalysisFragment extends Fragment implements View.OnClickListener,SubjectiveQuestionFragment.OnUserVisibleHintListener {

    private YXiuAnserTextView tvKnowledgePoint;
    private YXiuAnserTextView tvReportParseText;
    private YXiuAnserTextView tvCorrectionResultText;
//    private YXiuAnserTextView tvMyAnswer;
//    private ReadingQuestionsFragment rlAnswerPen;

    private SubjectiveHeartLayout subjectiveStarLayout;
    //
    private YXiuAnserTextView tvDifficulltyText;

    private YXiuAnserTextView tvAnswerText;

    private SubjectiveStarLayout difficultyStart;

    private View rootView;
    private QuestionEntity questionsEntity;

    private FlowLayout flowLayout;

    private LinearLayout llParseKnowledge;
    private LinearLayout llReportParse;
    private LinearLayout llDifficullty;
    private LinearLayout llAnswer;

    private TextView tvReportQuestionError;

    private RelativeLayout rlSubjectNoanswer;

    private RelativeLayout correctResultContent;

    private String qid;

    private GridView subjectiveGrid;
    private ListView lv_voice_comment;
    private SimpleVoicePlayer currentVoicePlayer;
    private AudioCommentAdapter audioCommentAdapter;
    private SubjectiveImageAdapter adapter;
    private List<String> photosList;
    private CorpListener listener;
    private List<AudioCommentBean> audioComments = new ArrayList<>();

    //add
    private ImageView ivIcon;
    private FrameLayout flCorrectionContent;
    /**
     * 批改结果模块
     **/
    private LinearLayout mLlReportParse;

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

    private void initView() {
        tvKnowledgePoint = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_knowledge_point);
        tvReportParseText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_text);
        tvCorrectionResultText = (YXiuAnserTextView) rootView.findViewById(R.id.correction_result_text);

        rlSubjectNoanswer = (RelativeLayout) rootView.findViewById(R.id.rl_subject_noanswer);

//        tvMyAnswer = (YXiuAnserTextView) rootView.findViewById(R.id.my_anwsers_text);

        tvReportQuestionError = (TextView) rootView.findViewById(R.id.tv_report_question_error);

        tvDifficulltyText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_difficullty_text);
        tvAnswerText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_answer_text);

        subjectiveStarLayout = (SubjectiveHeartLayout) rootView.findViewById(R.id.view_correction_result);

        difficultyStart = (SubjectiveStarLayout) rootView.findViewById(R.id.view_sub_difficulty_star);
        llDifficullty = (LinearLayout) rootView.findViewById(R.id.hw_report_difficullty_layout);
        llAnswer = (LinearLayout) rootView.findViewById(R.id.hw_report_answer_layout);

        llReportParse = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_layout);

        llParseKnowledge = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_knowledge_layout);

        correctResultContent = (RelativeLayout) rootView.findViewById(R.id.correcting_result_content);

        subjectiveGrid = (GridView) rootView.findViewById(R.id.subjective_questions_grid);
        lv_voice_comment = (ListView) rootView.findViewById(R.id.lv_voice_comment);
        adapter = new SubjectiveImageAdapter(this.getActivity());
        subjectiveGrid.setAdapter(adapter);

        flowLayout = (FlowLayout) rootView.findViewById(R.id.knowledge_flow_layout);

        tvReportQuestionError.setOnClickListener(this);

        ivIcon = (ImageView) rootView.findViewById(R.id.icon_correction_result);
        flCorrectionContent = (FrameLayout) rootView.findViewById(R.id.fl_correction_result_content);
        mLlReportParse = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_statistics_layout);


    }

    private void addPointBtn(final QuestionEntity.PointEntity pointEntity) {
        View knowledgeView = LayoutInflater.from(this.getActivity()).inflate(R.layout.item_knowledge, null);
        knowledgeView.setFocusable(false);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(CommonCoreUtil.dipToPx(10), CommonCoreUtil.dipToPx(10), 0, 0);
        TextView tvKnowlegdeName = (TextView) knowledgeView.findViewById(R.id.tv_knowledge_name);
        if (pointEntity != null && !TextUtils.isEmpty(pointEntity.getName())) {
            tvKnowlegdeName.setText(pointEntity.getName());
        }
        knowledgeView.setLayoutParams(params);

        flowLayout.addView(knowledgeView);
    }

    public void setIndexposition(CorpListener listener) {
        this.listener = listener;
    }


    private void initData() {
        for(int i =0;i<10;i++){
            AudioCommentBean bean = new AudioCommentBean();
//            bean.setUrl("http://scc.jsyxw.cn/audio1/2016/1028/2dc11fce06cce1a3db7553715b15d742465585.mp3");
            bean.setUrl("http://scc.jsyxw.cn/audio/2017/0204/file_58959fb430fce.mp3");
            bean.setLength((i+1) * 10);
            audioComments.add(bean);
        }
        audioCommentAdapter = new AudioCommentAdapter(getActivity(),audioComments);
        lv_voice_comment.setAdapter(audioCommentAdapter);

        lv_voice_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                SimpleVoicePlayer simpleVoicePlayer = (SimpleVoicePlayer) view.findViewById(R.id.simple_voice_player);
                if(simpleVoicePlayer.isPlaying)
                    simpleVoicePlayer.stopAndRelease();
                else simpleVoicePlayer.start();
                if(currentVoicePlayer == simpleVoicePlayer)
                    return;
                if(currentVoicePlayer != null && currentVoicePlayer.isPlaying) {
                    currentVoicePlayer.stopAndRelease();
                }
                currentVoicePlayer = simpleVoicePlayer;
                if(position >= lv_voice_comment.getCount() -1)
                    return;
                for(int i = position;i < lv_voice_comment.getCount() -1; i++){
                    View itemView = lv_voice_comment.getChildAt(i - lv_voice_comment.getFirstVisiblePosition());
                    View nextItemView = lv_voice_comment.getChildAt(i+1 - lv_voice_comment.getFirstVisiblePosition());
                    SimpleVoicePlayer voicePlayer = (SimpleVoicePlayer) itemView.findViewById(R.id.simple_voice_player);
                    final SimpleVoicePlayer nextVociePlayer = (SimpleVoicePlayer) nextItemView.findViewById(R.id.simple_voice_player);
                    voicePlayer.setOnPalyCompleteListener(new SimpleVoicePlayer.OnPalyCompleteListener() {
                        @Override
                        public void onComplete(SimpleVoicePlayer simpleVoicePlayer) {
                            currentVoicePlayer = null;
                            nextVociePlayer.start();
                            currentVoicePlayer = nextVociePlayer;
                        }
                    });
                }


//                simpleVoicePlayer.setOnPalyCompleteListener(new SimpleVoicePlayer.OnPalyCompleteListener() {
//                    @Override
//                    public void onComplete(SimpleVoicePlayer simpleVoicePlayer) {
//                        currentVoicePlayer =null;
//                        if(position >= lv_voice_comment.getCount() -1)
//                            return;
//                        View nextItemView = lv_voice_comment.getChildAt(position+1 - lv_voice_comment.getFirstVisiblePosition());
//                        SimpleVoicePlayer nextVociePlayer = (SimpleVoicePlayer) nextItemView.findViewById(R.id.simple_voice_player);
//                        nextVociePlayer.start();
//                        currentVoicePlayer = nextVociePlayer;
//                        nextVociePlayer.setOnPalyCompleteListener(new SimpleVoicePlayer.OnPalyCompleteListener() {
//                            @Override
//                            public void onComplete(SimpleVoicePlayer simpleVoicePlayer) {
//
//                            }
//                        });
//                    }
//                });
            }
        });

        if (questionsEntity != null) {
            //不可补做的题 隐藏批改结果
            if (getActivity().getClass().getSimpleName().equals(ResolutionAnswerViewActivity.class.getSimpleName()) && ((ResolutionAnswerViewActivity) getActivity()).isNotFinished) {
                mLlReportParse.setVisibility(View.GONE);
            } else {
                mLlReportParse.setVisibility(View.VISIBLE);
            }
//            if (questionsEntity.getAnswerBean().getRealStatus() == AnswerBean.ANSER_READED ) {
//                mLlReportParse.setVisibility(View.VISIBLE);
//            } else {
//                mLlReportParse.setVisibility(View.GONE);
//            }
            photosList = questionsEntity.getAnswerBean().getSubjectivImageUri();
            adapter.addMoreData(photosList);

            if (photosList == null || photosList.isEmpty()) {
                subjectiveGrid.setVisibility(View.GONE);
                rlSubjectNoanswer.setVisibility(View.VISIBLE);
            } else {
                subjectiveGrid.setVisibility(View.VISIBLE);
                rlSubjectNoanswer.setVisibility(View.GONE);
            }

            subjectiveGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CorpUtils.getInstence().AddListener(listener);
                    YanXiuConstant.index_position = YanXiuConstant.catch_position;
                    PhotoViewActivity.launch(SubjectiveProblemAnalysisFragment.this.getActivity(), (ArrayList<String>) photosList, position);
                }
            });

            if (questionsEntity.getPad() != null && questionsEntity.getPad().getTeachercheck() != null && questionsEntity.getPad().getStatus() == AnswerBean.ANSER_READED) {
                subjectiveStarLayout.selectStarCount(questionsEntity.getPad().getTeachercheck().getScore());
                if (!TextUtils.isEmpty(questionsEntity.getPad().getTeachercheck().getQcomment())) {
                    tvCorrectionResultText.setTextHtml(questionsEntity.getPad().getTeachercheck().getQcomment());
                } else {
                    ivIcon.setVisibility(View.GONE);
                    flCorrectionContent.setVisibility(View.GONE);
                }
            } else {
                subjectiveStarLayout.setVisibility(View.GONE);
                ivIcon.setVisibility(View.GONE);
                flCorrectionContent.setBackgroundColor(this.getActivity().getResources().getColor(android.R.color.transparent));
                tvCorrectionResultText.setTextHtml(this.getActivity().getResources().getString(R.string.subjective_questions_unread));
            }

            if (questionsEntity != null) {
                difficultyStart.selectStarCount(questionsEntity.getDifficulty());
                tvDifficulltyText.setTextHtml(getTypeKey(String.valueOf(questionsEntity.getDifficulty())));
            }

            if (questionsEntity.getTemplate().equals(YanXiuConstant.ANSWER_QUESTION) || questionsEntity.getType_id() == QUESTION_COMPUTE.type) {
                if (questionsEntity.getAnswer() != null && questionsEntity.getAnswer().size() > 0) {
                    StringBuffer answerString = new StringBuffer();
                    for (String str : questionsEntity.getAnswer()) {
                        answerString.append(str).append(" ");
                    }
                    if (!TextUtils.isEmpty(answerString.toString().trim())) {
                        tvAnswerText.setTextHtml(answerString.toString());
                        llAnswer.setVisibility(View.VISIBLE);
                    }
                }
            }


            if (questionsEntity.getPoint() != null && !questionsEntity.getPoint().isEmpty()) {
                List<QuestionEntity.PointEntity> pointList = questionsEntity.getPoint();
                int count = pointList.size();
                for (int i = 0; i < count; i++) {
                    addPointBtn(pointList.get(i));
                }
            } else {
                llParseKnowledge.setVisibility(View.GONE);
            }
            if (questionsEntity.getAnalysis() != null && !TextUtils.isEmpty(questionsEntity.getAnalysis())) {
                tvReportParseText.setTextHtml(questionsEntity.getAnalysis());
            } else {
                llReportParse.setVisibility(View.GONE);
            }


        }
    }

    private String getTypeKey(String key) {
        Map<String, String> relation = CommonCoreUtil.getDataRelationMap(this.getActivity(), R.array.analysis_list);
        String value = relation.get(key);
        if (TextUtils.isEmpty(value)) {
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
    public void onStop() {
        super.onStop();
        if(currentVoicePlayer != null)
        currentVoicePlayer.stopAndRelease();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(currentVoicePlayer != null)
        currentVoicePlayer.stopAndRelease();
    }


    @Override
    public void onClick(View v) {
        if (tvReportQuestionError == v) {
            SubjectExercisesItemBean dataSources = ((BaseAnswerViewActivity) this.getActivity()).getDataSources();

            if (dataSources != null && dataSources.getData() != null &&
                    dataSources.getData().get(0) != null &&
                    dataSources.getData().get(0).getPaperTest() != null &&
                    !dataSources.getData().get(0).getPaperTest().isEmpty()) {
                qid = dataSources.getData().get(0).getPaperTest().get(((BaseAnswerViewActivity) this.getActivity()).getCurrentIndex()).getQid() + "";
                LogInfo.log("geny", "subject question qid----" + qid);
            }
            ActivityJumpUtils.jumpToFeedBackActivity(this.getActivity(), qid, AbstractFeedBack.ERROR_FEED_BACK);
            (this.getActivity()).overridePendingTransition(R.anim.fade, R.anim.hold);
        }
    }


    @Override
    public void onUserVisibleHint(boolean isVisibleToUser) {
        if(!isVisibleToUser && currentVoicePlayer != null){
            currentVoicePlayer.stopAndRelease();
        }
    }
}
