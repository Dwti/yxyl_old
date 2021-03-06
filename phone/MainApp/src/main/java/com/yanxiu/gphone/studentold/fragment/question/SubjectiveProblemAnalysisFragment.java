package com.yanxiu.gphone.studentold.fragment.question;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
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
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.activity.BaseAnswerViewActivity;
import com.yanxiu.gphone.studentold.activity.ImagePreviewActivity;
import com.yanxiu.gphone.studentold.activity.NoteEditActivity;
import com.yanxiu.gphone.studentold.activity.PhotoViewActivity;
import com.yanxiu.gphone.studentold.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.studentold.adapter.NoteImageGridAdapter;
import com.yanxiu.gphone.studentold.adapter.SubjectiveImageAdapter;
import com.yanxiu.gphone.studentold.adapter.AudioCommentAdapter;
import com.yanxiu.gphone.studentold.bean.AnswerBean;
import com.yanxiu.gphone.studentold.bean.AudioCommentBean;
import com.yanxiu.gphone.studentold.bean.QuestionEntity;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.feedBack.AbstractFeedBack;
import com.yanxiu.gphone.studentold.inter.CorpListener;
import com.yanxiu.gphone.studentold.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.studentold.utils.CorpUtils;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;
import com.yanxiu.gphone.studentold.view.AudioCommentPlayer;
import com.yanxiu.gphone.studentold.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.studentold.view.question.subjective.SubjectiveHeartLayout;
import com.yanxiu.gphone.studentold.view.question.subjective.SubjectiveStarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_COMPUTE;

/**
 * Created by lidm on 2015/9/25.
 * 主观题的题目分析界面
 */
public class SubjectiveProblemAnalysisFragment extends Fragment implements View.OnClickListener, SubjectiveQuestionFragment.OnUserVisibleHintListener {

    private YXiuAnserTextView tvKnowledgePoint;
    private YXiuAnserTextView tvReportParseText;
    private YXiuAnserTextView tvCorrectionResultText;
    private TextView tv_result,tv_note;
    private ImageView iv_edit_note;
//    private YXiuAnserTextView tvMyAnswer;
//    private ReadingQuestionsFragment rlAnswerPen;
    private int answerViewTypeBean;

    private SubjectiveHeartLayout subjectiveStarLayout;
    //
    private YXiuAnserTextView tvDifficulltyText;

    private YXiuAnserTextView tvAnswerText;

    private SubjectiveStarLayout difficultyStart;

    private View rootView,ll_note_content,ll_note;
    private QuestionEntity questionsEntity;

    private FlowLayout flowLayout;

    private LinearLayout llParseKnowledge;
    private LinearLayout llReportParse;
    private LinearLayout llDifficullty;
    private LinearLayout llAnswer, ll_voice_comment;

    private TextView tvReportQuestionError;

    private RelativeLayout rlSubjectNoanswer;

    private RelativeLayout correctResultContent;

    private String qid;

    private GridView subjectiveGrid,grid_note_image;
    private ListView lv_voice_comment;
    private AudioCommentPlayer currentVoicePlayer;
    private AudioCommentAdapter audioCommentAdapter;
    private SubjectiveImageAdapter adapter;
    private NoteImageGridAdapter noteAdapter ;
    private List<String> photosList;
    private CorpListener listener;
    private List<AudioCommentBean> audioComments = new ArrayList<>();
    private TelephonyManager telephonyManager;

    //add
    private ImageView ivIcon, iv_result,iv_result1;
    private FrameLayout flCorrectionContent;
    /**
     * 批改结果模块
     **/
    private LinearLayout mLlReportParse;

    private FrameLayout send_wrong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.questionsEntity = (getArguments() != null) ? (QuestionEntity) getArguments().getSerializable("questions") : null;
        this.answerViewTypeBean = (getArguments() != null) ? getArguments().getInt("answerViewType"):0;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.hw_subjective_report_parse_bottom, null);
        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneCallListener(), PhoneStateListener.LISTEN_CALL_STATE);
        initView();
        initData();
        initListener();
        return rootView;
    }

    private void initListener() {
        iv_edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(NoteEditActivity.NOTE_CONTENT,tv_note.getText().toString());
                args.putStringArrayList(NoteEditActivity.PHOTO_PATH,noteAdapter.getData());
                args.putString(NoteEditActivity.WQID,questionsEntity.getWqid());
                args.putString(NoteEditActivity.QID,questionsEntity.getQid());
                NoteEditActivity.launch(SubjectiveProblemAnalysisFragment.this,args);
            }
        });

        grid_note_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImagePreviewActivity.lanuch(SubjectiveProblemAnalysisFragment.this,noteAdapter.getData(),position,false);
            }
        });
    }

    private void initView() {

        send_wrong=(FrameLayout) rootView.findViewById(R.id.send_wrong);

        tvKnowledgePoint = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_knowledge_point);
        tvReportParseText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_text);
        tvCorrectionResultText = (YXiuAnserTextView) rootView.findViewById(R.id.correction_result_text);

        rlSubjectNoanswer = (RelativeLayout) rootView.findViewById(R.id.rl_subject_noanswer);

//        tvMyAnswer = (YXiuAnserTextView) rootView.findViewById(R.id.my_anwsers_text);
        iv_result = (ImageView) rootView.findViewById(R.id.iv_result);
        iv_result1 = (ImageView) rootView.findViewById(R.id.iv_result1);
        tv_result = (TextView) rootView.findViewById(R.id.tv_result);
        tvReportQuestionError = (TextView) rootView.findViewById(R.id.tv_report_question_error);
        ll_note_content= rootView.findViewById(R.id.ll_note_content);
        ll_note = rootView.findViewById(R.id.ll_note);
        ll_note.setVisibility(View.GONE);
        tv_note = (TextView) rootView.findViewById(R.id.tv_note);
        tvDifficulltyText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_difficullty_text);
        tvAnswerText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_answer_text);

        subjectiveStarLayout = (SubjectiveHeartLayout) rootView.findViewById(R.id.view_correction_result);

        difficultyStart = (SubjectiveStarLayout) rootView.findViewById(R.id.view_sub_difficulty_star);
        llDifficullty = (LinearLayout) rootView.findViewById(R.id.hw_report_difficullty_layout);
        llAnswer = (LinearLayout) rootView.findViewById(R.id.hw_report_answer_layout);

        llReportParse = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_layout);

        llParseKnowledge = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_knowledge_layout);

        correctResultContent = (RelativeLayout) rootView.findViewById(R.id.correcting_result_content);
        iv_edit_note = (ImageView) rootView.findViewById(R.id.iv_edit_note);
        subjectiveGrid = (GridView) rootView.findViewById(R.id.subjective_questions_grid);
        ll_voice_comment = (LinearLayout) rootView.findViewById(R.id.ll_voice_comment);
        lv_voice_comment = (ListView) rootView.findViewById(R.id.lv_voice_comment);
        adapter = new SubjectiveImageAdapter(this.getActivity());
        subjectiveGrid.setAdapter(adapter);

        grid_note_image = (GridView) rootView.findViewById(R.id.grid_note_image);
        noteAdapter = new NoteImageGridAdapter(getActivity());
        grid_note_image.setAdapter(noteAdapter);

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
        if (questionsEntity != null && questionsEntity.getPad() != null && questionsEntity.getPad().getJsonAudioComment() != null && questionsEntity.getPad().getJsonAudioComment().size() > 0) {
            audioComments = questionsEntity.getPad().getJsonAudioComment();
            audioCommentAdapter = new AudioCommentAdapter(getActivity(), audioComments);
            lv_voice_comment.setAdapter(audioCommentAdapter);

            lv_voice_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    AudioCommentPlayer audioCommentPlayer = (AudioCommentPlayer) view.findViewById(R.id.simple_voice_player);
                    if (audioCommentPlayer.isPlaying)
                        audioCommentPlayer.stopAndRelease();
                    else audioCommentPlayer.start();
                    if (currentVoicePlayer == audioCommentPlayer)
                        return;
                    if (currentVoicePlayer != null && currentVoicePlayer.isPlaying) {
                        currentVoicePlayer.stopAndRelease();
                    }
                    currentVoicePlayer = audioCommentPlayer;
                    if (position >= lv_voice_comment.getCount() - 1)
                        return;
                    for (int i = position; i < lv_voice_comment.getCount() - 1; i++) {
                        View itemView = lv_voice_comment.getChildAt(i - lv_voice_comment.getFirstVisiblePosition());
                        View nextItemView = lv_voice_comment.getChildAt(i + 1 - lv_voice_comment.getFirstVisiblePosition());
                        AudioCommentPlayer voicePlayer = (AudioCommentPlayer) itemView.findViewById(R.id.simple_voice_player);
                        final AudioCommentPlayer nextVociePlayer = (AudioCommentPlayer) nextItemView.findViewById(R.id.simple_voice_player);
                        voicePlayer.setOnPalyCompleteListener(new AudioCommentPlayer.OnPalyCompleteListener() {
                            @Override
                            public void onComplete(AudioCommentPlayer audioCommentPlayer) {
                                currentVoicePlayer = null;
                                nextVociePlayer.start();
                                currentVoicePlayer = nextVociePlayer;
                            }
                        });
                    }
                }
            });
        } else {
            ll_voice_comment.setVisibility(View.GONE);
        }

        if (questionsEntity != null) {
            //不可补做的题 隐藏批改结果
            if (getActivity().getClass().getSimpleName().equals(ResolutionAnswerViewActivity.class.getSimpleName()) && ((ResolutionAnswerViewActivity) getActivity()).isNotFinished) {
                mLlReportParse.setVisibility(View.GONE);
            } else {
                mLlReportParse.setVisibility(View.VISIBLE);
            }
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
                //如果是主观类型的填空题，只显示正确、错误或未批改，不显示得分
                if (questionsEntity.getType_id() == YanXiuConstant.QUESTION_TYP.QUESTION_FILL_BLANKS.type||questionsEntity.getType_id() == 16||questionsEntity.getType_id() == 17) {
                    subjectiveStarLayout.setVisibility(View.GONE);
                    tv_result.setVisibility(View.VISIBLE);
                    if (questionsEntity.getPad().getTeachercheck().getScore() == 5) {
                        tv_result.setText(this.getActivity().getResources().getString(R.string.correct));
                    } else {
                        tv_result.setText(this.getActivity().getResources().getString(R.string.wrong));
                    }
                } else {
                    subjectiveStarLayout.selectStarCount(questionsEntity.getPad().getTeachercheck().getScore());
                }

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

            if (questionsEntity.getType_id() == YanXiuConstant.QUESTION_TYP.QUESTION_FILL_BLANKS.type||questionsEntity.getType_id() == 16||questionsEntity.getType_id() == 17){
                iv_result.setVisibility(View.GONE);
                iv_result1.setVisibility(View.VISIBLE);
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
        tv_note.setText(questionsEntity.getJsonNote().getText());
        noteAdapter.setData(questionsEntity.getJsonNote().getImages());
        setNoteContentVisible(tv_note.getText().toString(),noteAdapter.getData());
    }

    private void setNoteContentVisible(String note,List<String> imagePath){
        if(answerViewTypeBean==SubjectExercisesItemBean.WRONG_SET||answerViewTypeBean==SubjectExercisesItemBean.MISTAKEREDO){
            ll_note.setVisibility(View.VISIBLE);
        }else {
            ll_note.setVisibility(View.GONE);
            return;
        }
        if (answerViewTypeBean == SubjectExercisesItemBean.WRONG_SET){
            send_wrong.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(note) && (imagePath == null || imagePath.size() ==0)){
            ll_note_content.setVisibility(View.GONE);
        }else {
            ll_note_content.setVisibility(View.VISIBLE);
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
        if (currentVoicePlayer != null)
            currentVoicePlayer.stopAndRelease();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (currentVoicePlayer != null)
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case NoteEditActivity.REQUEST_NOTE_EDIT:
                if(resultCode == getActivity().RESULT_OK){
                    String text = data.getStringExtra(NoteEditActivity.NOTE_CONTENT);
                    ArrayList<String> images = data.getStringArrayListExtra(NoteEditActivity.PHOTO_PATH);
                    tv_note.setText(text);
                    noteAdapter.setData(images);
                    questionsEntity.getJsonNote().setText(text);
                    questionsEntity.getJsonNote().setImages(images);
                    setNoteContentVisible(tv_note.getText().toString(),noteAdapter.getData());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onUserVisibleHint(boolean isVisibleToUser) {
        if (!isVisibleToUser && currentVoicePlayer != null) {
            currentVoicePlayer.stopAndRelease();
        }
    }

    private class PhoneCallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING && currentVoicePlayer != null) {
                currentVoicePlayer.stopAndRelease();
            }
        }
    }
}
