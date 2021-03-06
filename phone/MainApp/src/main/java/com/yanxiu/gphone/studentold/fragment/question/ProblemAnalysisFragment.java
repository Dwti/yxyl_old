package com.yanxiu.gphone.studentold.fragment.question;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.view.flowview.FlowLayout;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.activity.AnswerViewActivity;
import com.yanxiu.gphone.studentold.activity.BaseAnswerViewActivity;
import com.yanxiu.gphone.studentold.activity.ImagePreviewActivity;
import com.yanxiu.gphone.studentold.activity.NoteEditActivity;
import com.yanxiu.gphone.studentold.adapter.NoteImageGridAdapter;
import com.yanxiu.gphone.studentold.bean.ExercisesDataEntity;
import com.yanxiu.gphone.studentold.bean.ExtendEntity;
import com.yanxiu.gphone.studentold.bean.QuestionEntity;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.feedBack.AbstractFeedBack;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.studentold.requestTask.RequestKnpointQBlockTask;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;
import com.yanxiu.gphone.studentold.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.studentold.view.question.subjective.SubjectiveStarLayout;
import com.yanxiu.gphone.studentold.view.spanreplaceabletextview.HtmlImageGetter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/7.
 * 题目分析界面
 */
public class ProblemAnalysisFragment extends Fragment implements View.OnClickListener {
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

    private View rootView,ll_note,ll_note_content;
    private QuestionEntity questionsEntity;

    private YXiuAnserTextView tvDifficulltyText;

    private TextView tvAnswerText;

    private SubjectiveStarLayout difficultyStart;

    private TextView tvReportQuestionError,tv_note;

    private GridView grid_note_image;
    private NoteImageGridAdapter noteAdapter;
    private FlowLayout flowLayout;
    //    protected StudentLoadingLayout loadingLayout;
    private ImageView iv_edit_note;
    private int answerViewType;

    private String qid;
    private SubjectExercisesItemBean subjectExercisesItemBean;

    private LinearLayout llReportParseStatue;
    private LinearLayout llReportParseStatistics;
    private LinearLayout llParseKnowledge;
    private LinearLayout llReportParse;
    private LinearLayout llDifficullty;
    private LinearLayout llAnswer;
    private FrameLayout send_wrong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.questionsEntity = (getArguments() != null) ? (QuestionEntity) getArguments().getSerializable("questions") : null;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.hw_report_parse_bottom, null);
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
                NoteEditActivity.launch(ProblemAnalysisFragment.this,args);
            }
        });

        grid_note_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImagePreviewActivity.lanuch(ProblemAnalysisFragment.this,noteAdapter.getData(),position,false);
            }
        });
    }

    private void initView() {

        send_wrong=(FrameLayout) rootView.findViewById(R.id.send_wrong);

        tvKnowledgePoint = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_knowledge_point);
        tvReportParseText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_text);
        tvReportParseStatueText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_statue_text);
        tvReportParseStatisticsText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_statistics_text);
        tvDifficulltyText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_difficullty_text);
        tvAnswerText = (TextView) rootView.findViewById(R.id.hw_report_answer_text);
        iv_edit_note = (ImageView) rootView.findViewById(R.id.iv_edit_note);
        tvReportQuestionError = (TextView) rootView.findViewById(R.id.tv_report_question_error);
        ll_note_content = rootView.findViewById(R.id.ll_note_content);
        ll_note = rootView.findViewById(R.id.ll_note);
        ll_note.setVisibility(View.GONE);
        tv_note = (TextView) rootView.findViewById(R.id.tv_note);
        difficultyStart = (SubjectiveStarLayout) rootView.findViewById(R.id.view_sub_difficulty_star);
        llDifficullty = (LinearLayout) rootView.findViewById(R.id.hw_report_difficullty_layout);
        llAnswer = (LinearLayout) rootView.findViewById(R.id.hw_report_answer_layout);

        grid_note_image = (GridView) rootView.findViewById(R.id.grid_note_image);
        noteAdapter = new NoteImageGridAdapter(getActivity());
        grid_note_image.setAdapter(noteAdapter);

        llReportParseStatue = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_statue_layout);
        llReportParseStatistics = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_statistics_layout);
        llParseKnowledge = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_knowledge_layout);
        llReportParse = (LinearLayout) rootView.findViewById(R.id.hw_report_parse_layout);

        flowLayout = (FlowLayout) rootView.findViewById(R.id.knowledge_flow_layout);


        tvReportQuestionError.setOnClickListener(this);
        iv_edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteEditActivity.class);
                startActivity(intent);
            }
        });

    }


    private void initData() {
        SubjectExercisesItemBean dataSources = ((BaseAnswerViewActivity) this.getActivity()).getDataSources();
        if (dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()) {
            answerViewType = dataSources.getViewType();
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

        if (dataSources != null && dataSources.getData() != null &&
                dataSources.getData().get(0) != null &&
                dataSources.getData().get(0).getPaperTest() != null &&
                !dataSources.getData().get(0).getPaperTest().isEmpty()) {
            qid = dataSources.getData().get(0).getPaperTest().get(((BaseAnswerViewActivity) this.getActivity()).getCurrentIndex()).getQid() + "";

        }

        if (questionsEntity != null) {
            if (questionsEntity.getPoint() != null && !questionsEntity.getPoint().isEmpty()) {
                List<QuestionEntity.PointEntity> pointList = questionsEntity.getPoint();
                int count = pointList.size();
                for (int i = 0; i < count; i++) {
                    addPointBtn(pointList.get(i));
                }
            } else {
                llParseKnowledge.setVisibility(View.GONE);
            }

            if (questionsEntity != null) {
                difficultyStart.selectStarCount(questionsEntity.getDifficulty());
                tvDifficulltyText.setTextHtml(getTypeKey(String.valueOf(questionsEntity.getDifficulty())));
                tvDifficulltyText.setVisibility(View.GONE);
            }

            if (questionsEntity.getAnswer() != null && questionsEntity.getAnswer().size() > 0) {
                StringBuffer answerString = new StringBuffer();
                for (String str : questionsEntity.getAnswer()) {
                    answerString.append(str).append(" ");
                }
                if (questionsEntity.getTemplate().equals(YanXiuConstant.CONNECT_QUESTION)) {
                    answerString = new StringBuffer();
                    List<String> list = questionsEntity.getAnswer();
                    for (String str : list) {
                        try {
                            JSONObject object = new JSONObject(str);
                            String ss = object.optString("answer");
                            String[] answer = ss.split(",");
                            if (Integer.parseInt(answer[0]) < list.size()) {
                                answerString.append("左" + (Integer.parseInt(answer[0]) + 1));
                            }
                            answerString.append("连");
                            if (Integer.parseInt(answer[1]) >= list.size()) {
                                answerString.append("右" + (Integer.parseInt(answer[1]) - list.size() + 1));
                            }
                            answerString.append(" ");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (questionsEntity.getTemplate().equals(YanXiuConstant.CLASSIFY_QUESTION)) {
                    answerString = new StringBuffer();
                    List<String> list = questionsEntity.getContent().getChoices();
                    List<String> data = questionsEntity.getAnswer();
                    for (String str : data) {
                        try {
                            JSONObject object = new JSONObject(str);
                            String ss = object.optString("answer");
                            String name = object.optString("name") + ":";
                            answerString.append(name);
                            String[] answer = ss.split(",");
                            for (String s : answer) {
                                answerString.append(list.get(Integer.parseInt(s)) + " ");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!TextUtils.isEmpty(answerString.toString())) {
                    tvAnswerText.setText(Html.fromHtml(answerString.toString(),new HtmlImageGetter(getActivity(),tvAnswerText),null));
                    tvAnswerText.setVisibility(View.VISIBLE);
                    llAnswer.setVisibility(View.VISIBLE);
                    switch (questionsEntity.getTemplate()) {//以下几种题型不显示答案
                        case YanXiuConstant.SINGLE_CHOICES://单选题
                        case YanXiuConstant.MULTI_CHOICES://多选题
                        case YanXiuConstant.JUDGE_QUESTION://判断题
                        case YanXiuConstant.CLOZE_QUESTION://完形填空
                            llAnswer.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    llAnswer.setVisibility(View.GONE);
                }
            }

            if (questionsEntity.getAnalysis() != null && !TextUtils.isEmpty(questionsEntity.getAnalysis())) {
                tvReportParseText.setTextHtml(questionsEntity.getAnalysis());
            } else {
                llReportParse.setVisibility(View.GONE);
            }
            if (questionsEntity.getExtend() != null && questionsEntity.getExtend().getData() != null) {
                ExtendEntity.DataEntity dataEntity = questionsEntity.getExtend().getData();
                if (!TextUtils.isEmpty(dataEntity.getAnswerCompare())) {//填空、归类、连线，当前状态为空时显示，不为空时不显示

                    switch (questionsEntity.getTemplate()) {
                        case YanXiuConstant.CONNECT_QUESTION://连线
                            tvReportParseStatueText.setTextHtml(dataEntity.getAnswerCompare());
                            llAnswer.setVisibility(View.GONE);
                            break;
                        case YanXiuConstant.FILL_BLANK://填空
                            tvReportParseStatueText.setTextHtml(dataEntity.getAnswerCompare());
                            llAnswer.setVisibility(View.GONE);
                            break;
                        case YanXiuConstant.CLASSIFY_QUESTION://归类
                            tvReportParseStatueText.setClasfyFlag(false);
                            tvReportParseStatueText.setTextHtml(dataEntity.getAnswerCompare().replaceAll("<img", "<imgFy"));
                            llAnswer.setVisibility(View.GONE);
                            break;
                        case YanXiuConstant.ANSWER_QUESTION://问答
                            tvReportParseStatueText.setTextHtml(dataEntity.getAnswerCompare());
                            break;
                        default:
                            tvReportParseStatueText.setTextHtml(dataEntity.getAnswerCompare());

                    }
                    llReportParseStatue.setVisibility(View.VISIBLE);

//                    }
                } else {
                    llReportParseStatue.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(dataEntity.getGlobalStatis())) {

                    tvReportParseStatisticsText.setTextHtml(dataEntity.getGlobalStatis());
                } else {
                    llReportParseStatistics.setVisibility(View.GONE);
                }

            } else {
                llReportParseStatue.setVisibility(View.GONE);
                llReportParseStatistics.setVisibility(View.GONE);
            }
//            if (getActivity().getClass().getSimpleName().equals(ResolutionAnswerViewActivity.class.getSimpleName())) {
//                llAnswer.setVisibility(View.VISIBLE);
//            }
        }
        tv_note.setText(questionsEntity.getJsonNote().getText());
        noteAdapter.setData(questionsEntity.getJsonNote().getImages());
        setNoteContentVisible(tv_note.getText().toString(),noteAdapter.getData());
    }

    private void setNoteContentVisible(String note,List<String> imagePath){
        if( answerViewType ==SubjectExercisesItemBean.WRONG_SET|| answerViewType ==SubjectExercisesItemBean.MISTAKEREDO){
            ll_note.setVisibility(View.VISIBLE);
        }else {
            ll_note.setVisibility(View.GONE);
            return;
        }
        if (answerViewType == SubjectExercisesItemBean.WRONG_SET){
            send_wrong.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(note) && (imagePath == null || imagePath.size() ==0)){
            ll_note_content.setVisibility(View.GONE);
        }else{
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


    private void addPointBtn(final QuestionEntity.PointEntity pointEntity) {
        View knowledgeView = LayoutInflater.from(this.getActivity()).inflate(R.layout.item_knowledge, null);
        knowledgeView.setFocusable(isTestCenterOnclick);
        if (isTestCenterOnclick) {
            tvKnowledgePoint.setVisibility(View.VISIBLE);
        }
        TextView tvKnowlegdeName = (TextView) knowledgeView.findViewById(R.id.tv_knowledge_name);
        if (pointEntity != null && !TextUtils.isEmpty(pointEntity.getName())) {
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
        ((BaseAnswerViewActivity) this.getActivity()).showDialog();
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
                ((BaseAnswerViewActivity) ProblemAnalysisFragment.this.getActivity()).hideDialog();
            }

            @Override
            public void dataError(int type, String msg) {
                LogInfo.log("geny", "dataError type =====" + type);
                if (TextUtils.isEmpty(msg)) {
                    Util.showToast(R.string.server_connection_erro);
                } else {
                    Util.showToast(msg);
                }
                ((BaseAnswerViewActivity) ProblemAnalysisFragment.this.getActivity()).hideDialog();
            }
        }).start();

    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    public void onClick(View v) {
        if (tvReportQuestionError == v) {
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
}
