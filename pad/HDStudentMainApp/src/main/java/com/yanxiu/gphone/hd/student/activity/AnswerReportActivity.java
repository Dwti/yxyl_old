package com.yanxiu.gphone.hd.student.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.login.LoginModel;
import com.common.share.ShareEnums;
import com.common.share.ShareExceptionEnums;
import com.common.share.ShareManager;
import com.common.share.constants.ShareConstants;
import com.common.share.inter.ShareResultCallBackListener;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.base.YanxiuBaseActivity;
import com.yanxiu.gphone.hd.student.adapter.YXiuCustomerBaseAdapter;
import com.yanxiu.gphone.hd.student.bean.AnswerBean;
import com.yanxiu.gphone.hd.student.bean.ExercisesDataEntity;
import com.yanxiu.gphone.hd.student.bean.PaperTestEntity;
import com.yanxiu.gphone.hd.student.bean.QuestionEntity;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestIntelliExeTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestKnpointQBlockTask;
import com.yanxiu.gphone.hd.student.utils.QuestionUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.view.ShareDialog;
import com.yanxiu.gphone.hd.student.view.StudentLoadingLayout;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;


/**
 * Created by Administrator on 2015/7/6.
 */
public class AnswerReportActivity extends YanxiuBaseActivity implements View.OnClickListener{
    private ArrayList<PaperTestEntity> dataList;
    private ArrayList<PaperTestEntity> subDataList;
    private GridView gridView;
    private int gridViewWitdth;
    private GridView subjectiveGrid;
    private int gridCount = 5;
    private int subjectiveGridWitdth;
    public static SubjectExercisesItemBean dataSources;

    //    private TextView tvReportTimeTitle;
    private TextView tvReportTimeText;
    private TextView tvReportToptitle;

    private ArrayList<PaperTestEntity> wrongList;
    private List<QuestionEntity> questionWrongList;

    private int objectiveCount = 0;


    private Button btnViewResolution;
    private Button btnPracticeAgain;
    private Button btnErrorResolution;

    /////////////////////////////////
    private SubjectExercisesItemBean subjectExercisesItemBean;
    private int stageId;
    private String subjectId;
    private String subjectName;
    private String editionId;
    private String editionName;
    private String volume;
    private String volumeName;
    private String chapterId;
    private String chapterName;
    private String sectionId;
    private String sectionName;
    private String cellid = "0";
    private String cellName = "";

    private List<QuestionEntity> questionList;
    //主观题的list
    private List<QuestionEntity> subjectiveList;
    //所有的题目list
    private List<QuestionEntity> allList;

    ///////////////////////////

    private ImageView ivBack;
    private ImageView shareView;

    private List<Integer> rightQuestionNum;
    private StudentLoadingLayout loadingLayout;

    protected TextView tvQuestionTitle;

    private int comeFrom = -1;

    private int costTime;

    private AnswerCardAdapter adapter;

    private AnswerCardSubAdapter subjectAdapter;

    private boolean isAllRight = false;

    private ImageView ivReportStamp;

    private boolean isGonePracticeAgain;

    private ArrayList<PaperTestEntity> allDataList;


    private ShareDialog shareDialog;

    private int sccuracy;
    private ScrollView reportScrollview;

    private int questionCount;

    private int finishCount;


    private TextView tvObjectTab, tvSubjectTab;

    private TextView tvObjectiveLine;
    private SwitchCompat btnWrongQuestion;
    private TextView tvSubjectiveLine;

    private LinearLayout llReportTab;
    private RelativeLayout rlObjectContainer, rlSubjectContainer;

    private int rightCount;

    //时候有客观题 默认是的时候有客观题
    private boolean isObjectiveQuestion = true;

//    private int ptype;

    public static void launch(Activity context, SubjectExercisesItemBean dataSources, int comeFrom, int flags, boolean isGonePracticeAgain) {
        Intent intent = new Intent(context, AnswerReportActivity.class);
        intent.putExtra("subjectExercisesItemBean", dataSources);
        intent.putExtra("comeFrom", comeFrom);
        intent.putExtra("isGonePracticeAgain", isGonePracticeAgain);
        intent.setFlags(flags);
//        intent.putParcelableArrayListExtra("paperTestEntityList", (ArrayList<? extends Parcelable>) dataList);
        context.startActivity(intent);
    }


    public static void launch(Activity context, SubjectExercisesItemBean dataSources) {
        Intent intent = new Intent(context, AnswerReportActivity.class);
        intent.putExtra("subjectExercisesItemBean", dataSources);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
//        intent.putParcelableArrayListExtra("paperTestEntityList", (ArrayList<? extends Parcelable>) dataList);
        context.startActivity(intent);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_report);
        initView();
        initData();
    }

    private void initView(){
        ivBack = (ImageView) findViewById(R.id.iv_top_back);
        shareView = (ImageView) findViewById(R.id.report_share);
        gridView = (GridView) this.findViewById(R.id.answer_report_grid);
        gridView.setFocusable(false);
        subjectiveGrid = (GridView) this.findViewById(R.id.subjective_questions_grid);
        subjectiveGrid.setFocusable(false);
        tvReportTimeText = (TextView) this.findViewById(R.id.report_time_text);
        ivReportStamp = (ImageView) this.findViewById(R.id.iv_report_stamp);
        reportScrollview = (ScrollView) this.findViewById(R.id.report_scrollview);
        reportScrollview.requestLayout();

        btnViewResolution = (Button) this.findViewById(R.id.btn_view_resolution);
        loadingLayout = (StudentLoadingLayout) findViewById(R.id.loading_layout);
        btnPracticeAgain = (Button) this.findViewById(R.id.practice_again);
        btnErrorResolution = (Button) this.findViewById(R.id.error_resolution);
        tvQuestionTitle = (TextView) this.findViewById(R.id.tv_anwser_tips);
        tvReportToptitle = (TextView) this.findViewById(R.id.tv_top_title);


        btnWrongQuestion = (SwitchCompat) this.findViewById(R.id.btn_subjective_wrong);
        tvSubjectTab = (TextView) this.findViewById(R.id.tv_subjcetive_tab);
        tvObjectTab = (TextView) this.findViewById(R.id.tv_objective_tab);
        llReportTab = (LinearLayout) this.findViewById(R.id.ll_report_tab);
        rlObjectContainer = (RelativeLayout) this.findViewById(R.id.rl_object_tab);
        rlSubjectContainer = (RelativeLayout) this.findViewById(R.id.rl_subject_tab);

        tvObjectiveLine = (TextView) this.findViewById(R.id.tv_objective_line);
        tvSubjectiveLine = (TextView) this.findViewById(R.id.tv_subjective_line);

        ivBack.setOnClickListener(this);
        btnViewResolution.setOnClickListener(this);
        btnPracticeAgain.setOnClickListener(this);
        btnErrorResolution.setOnClickListener(this);
        shareView.setOnClickListener(this);
        tvSubjectTab.setOnClickListener(this);
        tvObjectTab.setOnClickListener(this);
    }

    private void initData(){
        rightQuestionNum = new ArrayList<Integer>();
        dataSources =  (SubjectExercisesItemBean) getIntent().getSerializableExtra("subjectExercisesItemBean");
//        ptype = dataSources.getData().get(0).getPtype();
        comeFrom = getIntent().getIntExtra("comeFrom", -1);
        isGonePracticeAgain = getIntent().getBooleanExtra("isGonePracticeAgain", false);
        if(dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()){

            if(dataSources.getData().get(0).getPaperTest() != null){
                questionCount = dataSources.getData().get(0).getPaperTest().size();
            } else {
                questionCount = 0;
            }
            LogInfo.log("geny", "----questionCount ----" + questionCount);
            // 主观题和客观题的完成度添加
            finishCount = questionCount - QuestionUtils.calculationUnFinishQuestion(dataSources.getData().get(0).getPaperTest());
            LogInfo.log("geny", "----finishCount ----" + finishCount);
            //移除主观题
            subDataList = (ArrayList<PaperTestEntity>) QuestionUtils.removeSubjectiveQuesition(dataSources);
            LogInfo.log("geny", "----sub data ----" + subDataList.size());

            //所有非主观题的集合
            dataList = new ArrayList<>();
            if(dataSources.getData().get(0).getPaperTest() != null){
                dataList.addAll(dataSources.getData().get(0).getPaperTest());
                LogInfo.log("geny", "----sub data ----" + dataList.size());
            }

            //错题集合
            wrongList= new ArrayList<>();
            if(dataSources.getData().get(0).getPaperTest() != null){
                wrongList.addAll(dataSources.getData().get(0).getPaperTest());
            }

            allDataList = new ArrayList<>();
            allDataList.addAll(dataList);
            allDataList.addAll(subDataList);

            QuestionUtils.setQuestionIndex(allDataList);

            allList = QuestionUtils.addChildQuestionToParent(allDataList);

            //初始化主观题集合 ps： 用之前的客观题分页判断
            subjectiveList = QuestionUtils.addChildQuestionToParent(subDataList);


//            long costTime;

            if(!TextUtils.isEmpty(dataSources.getData().get(0).getName())){
                tvQuestionTitle.setText(dataSources.getData().get(0).getName());
            }

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    allList = QuestionUtils.addChildQuestionToParent(allDataList);
                    jumpType(position, allDataList);
                }
            });

            subjectiveGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    QuestionUtils.addChildQuestionToParent(allDataList);
                    jumpType(dataList.size() + position, allDataList);
                }
            });

            adapter = new AnswerCardAdapter(this);
            subjectAdapter = new AnswerCardSubAdapter(this);
            if(dataList != null) {
                //初始化客观题 集合
                questionList = QuestionUtils.addChildQuestionToParent(dataList);
                objectiveCount = questionList.size();
                String objectTitile;
                LogInfo.log("geny", "----questionList data ----" + questionList.size());

                if(dataList != null) {

//                    setObjectiveSelected();

                    calculationSubQuestionTime(subDataList);
                    rightCount = calculationRightQuestion(wrongList);

                    if(questionList.size() <= rightCount){
                        isAllRight = true;
                    }

                    if(isAllRight){
                        btnWrongQuestion.setVisibility(View.GONE);
                    }

                }

                if(questionList != null && !questionList.isEmpty()){
                    objectiveCount = questionList.size();
                    sccuracy = (rightCount * 100) / objectiveCount;

                    objectTitile = String.format(this.getResources().getString(R.string.objective_title), rightCount, objectiveCount, sccuracy);
                    tvObjectiveLine.setText(objectTitile);

                    gridView.setAdapter(adapter);
                    adapter.addMoreData(questionList);

                    isObjectiveQuestion = true;

                }else{
                    btnWrongQuestion.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    objectTitile = String.format(this.getResources().getString(R.string.report_no_data), 0);
                    tvObjectiveLine.setText(objectTitile);
                    isObjectiveQuestion = false;
                }

            }

            String subjectTitile;
            if(subjectiveList != null && !subjectiveList.isEmpty()){
                int subDataCount = subDataList.size();
                subjectTitile = String.format(this.getResources().getString(R.string.subjective_title), subDataCount, QuestionUtils.calculationAverageSubejectScore(subDataList));
                tvSubjectiveLine.setText(subjectTitile);

                subjectiveGrid.setAdapter(subjectAdapter);
                subjectAdapter.addMoreData(subjectiveList);

            }else{
                subjectiveGrid.setVisibility(View.GONE);
                String objectTitile = String.format(this.getResources().getString(R.string.report_no_data), 0);
                tvSubjectiveLine.setText(objectTitile);
            }
            layoutFinishData();
        }
    }

    private void layoutFinishData(){

        tvReportTimeText.setText(this.getResources().getString(R.string.answer_cost_time) + formatTime(costTime));

        if(isObjectiveQuestion){
            setObjectiveSelected();
        }else{
            setSubjectiveSelected();
        }

        btnWrongQuestion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    compoundButton.setTextColor(AnswerReportActivity.this.getResources().getColor(R.color.color_118989));
                    questionWrongList = QuestionUtils.addChildQuestionToParent(wrongList);
                    if (questionWrongList != null && !questionWrongList.isEmpty()) {
                        adapter.setList(questionWrongList);
                    }
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            jumpType(position, wrongList);
                        }
                    });
                } else {
                    allList = QuestionUtils.addChildQuestionToParent(allDataList);
                    if(questionList != null && !questionList.isEmpty()){
                        adapter.setList(questionList);
                    }
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            jumpType(position, allDataList);
                        }
                    });
                }
            }
        });

        if(isGonePracticeAgain){
            btnPracticeAgain.setVisibility(View.GONE);
        }
        switch (comeFrom){
            case YanXiuConstant.HOMEWORK_REPORT:
//                    reportLine.setVisibility(View.GONE);
                tvReportToptitle.setText(getResources().getString(R.string.answer_report));
                shareView.setVisibility(View.GONE);
                ivReportStamp.setImageResource(R.drawable.gruop_report_stamp);
                llReportTab.setVisibility(View.VISIBLE);
                break;
            case YanXiuConstant.INTELLI_REPORT:
            case YanXiuConstant.KPN_REPORT:
            case YanXiuConstant.HISTORY_REPORT:
                subjectiveGrid.setVisibility(View.GONE);
                tvReportToptitle.setText(getResources().getString(R.string.exercises_report));
                ivReportStamp.setImageResource(R.drawable.answer_report_stamp);
                shareView.setVisibility(View.VISIBLE);
                llReportTab.setVisibility(View.GONE);
                break;
        }
    }

    public int getComeFrom() {
        return comeFrom;
    }

    /**
     * 跳转的 位置
     * @param position
     */
    private void jumpType(int position){
        if(position < allDataList.size()){

            QuestionEntity questionEntity = allList.get(position);

            if (allDataList != null && !allDataList.isEmpty()) {
                if (allDataList.get(questionEntity.getPageIndex()).getQuestions() != null) {
//                            dataSources.getData().get(0).getPaperTest().get(position).getQuestions().setPageIndex(questionEntity.getPageIndex());
                    //从答题报告和答题卡进入是判断子项是否是当前页  没有为-1  跳转后置为-1
                    //阅读题的子项fragment 中的viewpager 传入子项index
                    allDataList.get(questionEntity.getPageIndex()).getQuestions().setChildPageIndex(questionEntity.getChildPageIndex());
                    dataSources.setIsResolution(true);
                    dataSources.getData().get(0).setPaperTest(allDataList);

                    if (dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()) {
                        dataSources.getData().get(0).setPaperTest(allDataList);
                    }
                    setIsTestCenterOnclick();
                    ResolutionAnswerViewActivity.launch(AnswerReportActivity.this, dataSources, questionEntity.getPageIndex(), questionEntity.getChildPageIndex(), comeFrom);
                    allDataList.get(questionEntity.getPageIndex()).getQuestions().setChildPageIndex(-1);
                }
            }

        }

    }

    private void jumpType(int position, ArrayList<PaperTestEntity> dataList){

        QuestionEntity questionEntity = dataList.get(position).getQuestions();

        if (dataList != null && !dataList.isEmpty()) {
            if (dataList.get(questionEntity.getPageIndex()).getQuestions() != null) {
                dataList.get(questionEntity.getPageIndex()).getQuestions().setChildPageIndex(questionEntity.getChildPageIndex());
                dataSources.setIsResolution(true);
                if(dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()){
                    dataSources.getData().get(0).setPaperTest(dataList);

                }
                setIsTestCenterOnclick();
                LogInfo.log("jumpType--------", "getPageIndex--" + questionEntity.getPageIndex());
                ResolutionAnswerViewActivity.launch(AnswerReportActivity.this, dataSources, questionEntity.getPageIndex(), questionEntity.getChildPageIndex(), comeFrom);
                dataList.get(questionEntity.getPageIndex()).getQuestions().setChildPageIndex(-1);
            }
        }

    }

    /**
     * 时间转换分：秒
     */
    public String formatTime(int timeMs) {

        StringBuilder formatBuilder = new StringBuilder();
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());

        try {
//            int totalSeconds = timeMs;
            int sec = timeMs % 60;
            timeMs = timeMs / 60;
            int min = timeMs % 60;
            timeMs = timeMs / 60;

            formatBuilder.setLength(0);

            return formatter.format("%02d:%02d:%02d", timeMs, min, sec).toString();
        } finally {
            formatter.close();
        }
    }

    private void calculationSubQuestionTime(ArrayList<PaperTestEntity> dataList){
        int count = dataList.size();
        LogInfo.log("geny", "calculationRightQuestion count =====" + count);
        for(int i = 0; i < count; i++){
            if(dataList.get(i) != null && dataList.get(i).getQuestions() != null) {
                costTime += dataList.get(i).getQuestions().getAnswerBean().getConsumeTime();
            }
        }
    }



    private int calculationRightQuestion(ArrayList<PaperTestEntity> dataList){
        int rightCount = 0;
        int count = dataList.size();
        ArrayList<PaperTestEntity> dataRightList = new ArrayList<PaperTestEntity>();
        LogInfo.log("geny", "calculationRightQuestion count =====" + count);
        for(int i = 0; i < count; i++){
            if(dataList.get(i) != null && dataList.get(i).getQuestions() != null) {

                if (dataList.get(i).getQuestions().getChildren() != null &&
                        dataList.get(i).getQuestions().getType_id() == YanXiuConstant.QUESTION_TYP.QUESTION_READING.type) {

                    List<QuestionEntity> questionList = dataList.get(i).getQuestions().getChildren();
                    int childrenCount = questionList.size();
                    boolean isFalse = false;
                    for (int j = 0; j < childrenCount; j++) {
                        costTime += questionList.get(j).getAnswerBean().getConsumeTime();
                        LogInfo.log("geny", "getChildren time =====" + questionList.get(j).getAnswerBean().getConsumeTime());
                        if (questionList.get(j).getAnswerBean().isRight()) {
                            rightCount++;
                        } else {
                            isFalse = isFalse || true;
                        }
                    }
                    if (!isFalse) {
                        dataRightList.add(dataList.get(i));
                    }
                } else {
                    LogInfo.log("geny", "calculationQuestion time =====" + dataList.get(i).getQuestions().getAnswerBean().getConsumeTime());
                    costTime += dataList.get(i).getQuestions().getAnswerBean().getConsumeTime();
                    LogInfo.log("geny", "calculationRightQuestion null -----" + i);
                    if (dataList.get(i).getQuestions().getAnswerBean().isRight()) {
                        rightCount++;
                        dataRightList.add(dataList.get(i));
                    }
                }
            }
        }
        LogInfo.log("geny", "calculationRightQuestion count =====" + dataList.size());
        dataList.removeAll(dataRightList);
        LogInfo.log("geny", "dataRightList count =====" + dataRightList.size());
        LogInfo.log("geny", "dataList count =====" + dataList.size());
        return rightCount;
    }

    private void forResult(){
        LogInfo.log("king","AnswerReportActivity forResult");
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            forResult();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if(v == ivBack){
            forResult();
            finish();
        }else if(v == tvObjectTab){
            setObjectiveSelected();
        }else if(v == tvSubjectTab){
            setSubjectiveSelected();
        }else if(v == shareView){
            if(NetWorkTypeUtils.isNetAvailable()){
                Util.showToast(R.string.net_null);
                return;
            }
            int count = 0;
            if(dataList!=null){
                count = dataList.size();
            }

            String dis ="";
            if(sccuracy<=20){
                dis = getResources().getString(R.string.share_discription_one);
            }else if(sccuracy<=60){
                dis = getResources().getString(R.string.share_discription_two);
            }else if(sccuracy<=80){
                dis = getResources().getString(R.string.share_discription_three);
            }else if(sccuracy<=100){
                dis = getResources().getString(R.string.share_discription_four);
            }
            final String discription = dis;
            if(dataSources!=null && dataSources.getData()!=null && dataSources.getData().get(0)!=null){
                StringBuilder sb = new StringBuilder("http://wx.hwk.yanxiu.com/sharePage.do?");
                sb.append("uid="+ LoginModel.getUid());
                sb.append("&ppid="+dataSources.getData().get(0).getId());
                sb.append("&buildtime="+dataSources.getData().get(0).getBuildtime());
                sb.append("&ostype=gpad");
                final String shareUrl = sb.toString();
                if(shareDialog == null){
                    shareDialog = new ShareDialog(this, new ShareDialog.ShareCallBack() {
                        @Override public void cancel() {

                        }

                        @Override public void wechatShare() {
                            Bundle bundle=createWeiShareParams(ShareConstants.WEIXIN_SHARE_TYPE_TALK ,ShareConstants.WEIXIN_SHARE_WAY_WEBPAGE , discription, shareUrl);
                            ShareManager.getInstance().onShare(AnswerReportActivity.this, ShareEnums.WEIXIN, bundle, new ShareResultCallBackListener() {
                                @Override
                                public void notInstall() {
                                    Util.showToast(R.string.no_install_weixin);
                                }

                                @Override
                                public void shareException(ShareExceptionEnums exceptionEnums) {

                                }

                                @Override
                                public void shareSuccess(Object o) {

                                }

                                @Override
                                public void shareFailrue(Object o) {

                                }
                            });
                        }

                        @Override public void wechatFridsShare() {
                            Bundle bundle= createWeiShareParams(ShareConstants.WEIXIN_SHARE_TYPE_FRENDS,ShareConstants.WEIXIN_SHARE_WAY_WEBPAGE , discription, shareUrl);
                            ShareManager.getInstance().onShare(AnswerReportActivity.this, ShareEnums.WEIXIN_FRIENDS, bundle, new ShareResultCallBackListener() {
                                @Override
                                public void notInstall() {
                                    Util.showToast(R.string.no_install_weixin);
                                }

                                @Override
                                public void shareException(ShareExceptionEnums exceptionEnums) {

                                }

                                @Override
                                public void shareSuccess(Object o) {

                                }

                                @Override
                                public void shareFailrue(Object o) {

                                }

                            });
                        }

                        @Override public void qqShare() {
                            Bundle bundle = new Bundle();
                            bundle.putString(ShareConstants.QQ_APPID_KEY,ShareConstants.HD_STUDENT_QQ_AppID);
                            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                            //这条分享消息被好友点击后的跳转URL。
                            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL,shareUrl);
                            //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能t全为空，最少必须有一个是有值的。
                            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, getResources().getString(
                                    R.string.share_title));
                            //分享的图片URL
                            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, YanXiuConstant.SHARE_ICON_PATH+ YanXiuConstant.SHARE_LOGO_NAME);
                            //分享的消息摘要，最长50个字
                            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, discription);
                            //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
                            bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getResources().getString(R.string.app_name));
                            //标识该消息的来源应用，值为应用名称+AppId。
                            bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT,QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);

                            ShareManager.getInstance().onShare(AnswerReportActivity.this, ShareEnums.QQ, bundle, new ShareResultCallBackListener() {

                                @Override
                                public void notInstall() {
                                    Util.showToast(R.string.no_install_qq);
                                }

                                @Override
                                public void shareException(ShareExceptionEnums exceptionEnums) {

                                }

                                @Override
                                public void shareSuccess(Object o) {

                                }

                                @Override
                                public void shareFailrue(Object o) {

                                }
                            });

                        }

                        @Override public void qzoneShare() {
                            Bundle bundle = new Bundle();
                            bundle.putString(ShareConstants.QQ_APPID_KEY,ShareConstants.HD_STUDENT_QQ_AppID);
                            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                                    QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                            //这条分享消息被好友点击后的跳转URL。
                            bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
                            //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
                            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE,getResources().getString(R.string.share_title));
                            //分享的图片URL
                            ArrayList<String> icon = new ArrayList<>();
                            icon.add(YanXiuConstant.SHARE_ICON_PATH+YanXiuConstant.SHARE_LOGO_NAME);
                            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, icon);
                            //分享的消息摘要，最长50个字
                            bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,discription);

                            ShareManager.getInstance().onShare(AnswerReportActivity.this, ShareEnums.QQZONE, bundle, new ShareResultCallBackListener() {

                                @Override
                                public void notInstall() {
                                    Util.showToast(R.string.no_install_qq);
                                }

                                @Override
                                public void shareException(ShareExceptionEnums exceptionEnums) {

                                }

                                @Override
                                public void shareSuccess(Object o) {

                                }

                                @Override
                                public void shareFailrue(Object o) {

                                }
                            });
                        }
                    });
                }
                shareDialog.show();
            }
        } else if(v == btnViewResolution){
            dataSources.setIsResolution(true);
            if(dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()){

                dataSources.getData().get(0).setPaperTest(allDataList);

            }
            setIsTestCenterOnclick();
            ResolutionAnswerViewActivity.launch(AnswerReportActivity.this, dataSources, comeFrom);
        }else if(v == btnPracticeAgain){

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
                switch (comeFrom){
                    case YanXiuConstant.INTELLI_REPORT:
                        LogInfo.log("geny", "-------YanXiuConstant.INTELLI_REPORT" + comeFrom);
                        requestSubjectExercises();
                        break;
                    case YanXiuConstant.KPN_REPORT:
                        LogInfo.log("geny", "-------YanXiuConstant.KPN_REPORT" + comeFrom);
                        requestSubjectKnpExercises();
                        break;
                }
            }

        }else if(v == btnErrorResolution){
            //产品定义
            if(isAllRight) {
                Util.showToast(R.string.paper_test_all_right);
            } else {
                dataSources.setIsResolution(true);
                if (dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()) {
                    dataSources.getData().get(0).setPaperTest(wrongList);
                }
                setIsTestCenterOnclick();
                ResolutionAnswerViewActivity.launch(AnswerReportActivity.this, dataSources, comeFrom);
            }
        }
    }

    private Bundle createWeiShareParams(int shareType,int shareWayType,String discription,String shareUrl){
        Bundle bundle=new Bundle();
        bundle.putString(ShareConstants.WX_APPID_KEY,ShareConstants.HD_STUDENT_WX_AppID);
        bundle.putInt(ShareConstants.WEIXIN_SHARE_TYPE,shareType);
        bundle.putInt(ShareConstants.WEIXIN_SHARE_WAY_TYPE,shareWayType);
        bundle.putString(ShareConstants.WEIXIN_SHARE_TITLE_KEY,getResources().getString(R.string.share_title));
        bundle.putString(ShareConstants.WEIXIN_SHARE_DES_KEY,discription);
        bundle.putString(ShareConstants.WEIXIN_SHARE_IMG_URL, shareUrl);
        bundle.putInt(ShareConstants.WEIXIN_SHARE_IMG_ICON,R.mipmap.share_app_icon);
        return bundle;
    }

    private void setIsTestCenterOnclick(){

        if (dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()) {
            if(comeFrom == YanXiuConstant.KPN_REPORT || comeFrom == YanXiuConstant.INTELLI_REPORT){
                dataSources.getData().get(0).setIsTestCenterOnclick(true);
            }else{
                dataSources.getData().get(0).setIsTestCenterOnclick(false);
            }
        }
    }


    private void requestSubjectExercises() {
        LogInfo.log("geny", "requestSubjectExercises");
        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_INTELLI_EXE);
        //TODO
        new RequestIntelliExeTask(this, stageId, subjectId, editionId, chapterId, sectionId, 10, volume, cellid , new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                subjectExercisesItemBean = (SubjectExercisesItemBean) result;
                if(subjectExercisesItemBean.getData() != null && subjectExercisesItemBean.getData().get(0).getPaperTest() != null && !subjectExercisesItemBean.getData().get(0).getPaperTest().isEmpty()){
                    if(subjectExercisesItemBean.getData() != null && !subjectExercisesItemBean.getData().isEmpty()){
                        subjectExercisesItemBean.getData().get(0).setStageid(stageId + "");
                        subjectExercisesItemBean.getData().get(0).setSubjectid(subjectId);
                        subjectExercisesItemBean.getData().get(0).setSubjectName(subjectName);

                        subjectExercisesItemBean.getData().get(0).setBedition(editionId);
                        subjectExercisesItemBean.getData().get(0).setEditionName(editionName);

                        if(!TextUtils.isEmpty(volume)){
                            subjectExercisesItemBean.getData().get(0).setVolume(volume);
                        }
                        if(!TextUtils.isEmpty(volumeName)){
                            subjectExercisesItemBean.getData().get(0).setVolumeName(volumeName);
                        }


                        subjectExercisesItemBean.getData().get(0).setChapterid(chapterId);
                        subjectExercisesItemBean.getData().get(0).setChapterName(chapterName);

                        subjectExercisesItemBean.getData().get(0).setSectionid(sectionId);
                        subjectExercisesItemBean.getData().get(0).setSectionName(sectionName);
                        AnswerViewActivity.launch(AnswerReportActivity.this, subjectExercisesItemBean, comeFrom);
                        AnswerReportActivity.this.finish();
                    }
                }else{
                    Util.showToast(R.string.server_connection_erro);
                }

                loadingLayout.setViewGone();
            }

            @Override
            public void dataError(int type, String msg) {
                LogInfo.log("geny", "dataError type =====" + type);
                if (TextUtils.isEmpty(msg)) {
                    Util.showToast(R.string.server_connection_erro);
                } else {
                    Util.showToast(msg);
                }
                loadingLayout.setViewGone();
            }
        }).start();
    }

    protected void requestSubjectKnpExercises() {
        LogInfo.log("geny", "requestSubjectExercises");
        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_INTELLI_EXE);
        new RequestKnpointQBlockTask(this, stageId, subjectId, chapterId, sectionId, cellid, RequestKnpointQBlockTask.EXAM_QUSETION, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                subjectExercisesItemBean = (SubjectExercisesItemBean) result;
                if (subjectExercisesItemBean.getData() != null && subjectExercisesItemBean.getData().get(0).getPaperTest() != null && !subjectExercisesItemBean.getData().get(0).getPaperTest().isEmpty()) {
                    if (subjectExercisesItemBean.getData() != null && !subjectExercisesItemBean.getData().isEmpty()) {
                        subjectExercisesItemBean.getData().get(0).setStageid(stageId + "");
                        subjectExercisesItemBean.getData().get(0).setSubjectid(subjectId);
                        subjectExercisesItemBean.getData().get(0).setSubjectName(subjectName);

                        subjectExercisesItemBean.getData().get(0).setBedition(editionId);
                        subjectExercisesItemBean.getData().get(0).setEditionName(editionName);

                        if(!TextUtils.isEmpty(volume)){
                            subjectExercisesItemBean.getData().get(0).setVolume(volume);
                        }
                        if(!TextUtils.isEmpty(volumeName)){
                            subjectExercisesItemBean.getData().get(0).setVolumeName(volumeName);
                        }

                        subjectExercisesItemBean.getData().get(0).setChapterid(chapterId);
                        subjectExercisesItemBean.getData().get(0).setChapterName(chapterName);

                        subjectExercisesItemBean.getData().get(0).setSectionid(cellid);
                        subjectExercisesItemBean.getData().get(0).setSectionName(cellName);

                        subjectExercisesItemBean.getData().get(0).setIsChapterSection(ExercisesDataEntity.TEST_CENTER);

                        AnswerViewActivity.launch(AnswerReportActivity.this, subjectExercisesItemBean, comeFrom);
                    }
                } else {
                    if (subjectExercisesItemBean != null && subjectExercisesItemBean.getStatus() != null && subjectExercisesItemBean.getStatus().getDesc() != null) {
                        Util.showToast(subjectExercisesItemBean.getStatus().getDesc());
                    } else {
                        Util.showToast(R.string.server_connection_erro);
                    }
                }
                loadingLayout.setViewGone();
            }

            @Override
            public void dataError(int type, String msg) {
                LogInfo.log("geny", "dataError type =====" + type);
                if (TextUtils.isEmpty(msg)) {
                    Util.showToast(R.string.server_connection_erro);
                } else {
                    Util.showToast(msg);
                }
                loadingLayout.setViewGone();
            }
        }).start();

    }


    private class AnswerCardAdapter extends YXiuCustomerBaseAdapter<QuestionEntity> {

        private ViewHolder holder;

        public AnswerCardAdapter(Activity context) {
            super(context);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = mContext.getLayoutInflater();
                row = inflater.inflate(R.layout.item_report_card, null);
                holder  = new ViewHolder();
                holder.ivSign = (TextView) row.findViewById(R.id.answer_report_icon);
                holder.tvIndex = (TextView) row.findViewById(R.id.answer_report_text);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            QuestionEntity data = mList.get(position);
            if(data != null && data.getAnswerBean().isFinish()){
                if(data.getAnswerBean().isRight()){
                    if(!rightQuestionNum.contains(position)){
                        rightQuestionNum.add(position);
                    }
                    holder.ivSign.setBackgroundResource(R.drawable.answer_report_correct);
                }else{
                    holder.ivSign.setBackgroundResource(R.drawable.answer_report_wrong);
                }

            }else{
                //add by 1.1 答题卡没做视为 错题
                holder.ivSign.setBackgroundResource(R.drawable.answer_report_wrong);

            }
            int questionIndex = data.getQuestionIndex();
            holder.tvIndex.setText(String.valueOf(questionIndex + 1));
            return row;
        }


    }
    private class ViewHolder {
        TextView ivSign;
        TextView tvIndex;
    }


    private class AnswerCardSubAdapter extends YXiuCustomerBaseAdapter<QuestionEntity> {

        private ViewHolder holder;

        public AnswerCardSubAdapter(Activity context) {
            super(context);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = mContext.getLayoutInflater();
                row = inflater.inflate(R.layout.item_report_card, null);
                holder  = new ViewHolder();
                holder.ivSign = (TextView) row.findViewById(R.id.answer_report_icon);
                holder.tvIndex = (TextView) row.findViewById(R.id.answer_report_text);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }
            QuestionEntity data = mList.get(position);
            if(data != null && data.getPadBean() != null){
                int status = data.getPadBean().getStatus();
                LogInfo.log("geny", "status AnswerCardSubAdapter =====" + status);
                switch (status){
                    case AnswerBean.ANSER_READED:
                        holder.ivSign.setVisibility(View.VISIBLE);
                        if(data.getPadBean().getTeachercheck() != null){
                            holder.ivSign.setText(String.valueOf(data.getPadBean().getTeachercheck().getScore()));
                        }
                        holder.ivSign.setBackgroundResource(R.drawable.answer_report_read);
                        break;
                    default:
                        holder.ivSign.setVisibility(View.INVISIBLE);
                        break;
                }
            }
            int questionIndex = data.getQuestionIndex();
            holder.tvIndex.setText(String.valueOf(questionIndex + 1));
            return row;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogInfo.log("king", "AnswerReportActivity onActivityResult");
        if(requestCode == 10103){//QQ登陆界面返回码
            if (resultCode == RESULT_CANCELED){
                LogInfo.log("king", "AnswerReportActivity onActivityResult 10103");
            }
        }
        if(requestCode == 10104){//qq空间登陆界面返回码
            if (resultCode == RESULT_CANCELED){
                LogInfo.log("king", "AnswerReportActivity onActivityResult 10104");
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogInfo.log("king", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogInfo.log("king", "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dataSources!=null){
            dataSources=null;
        }
    }

    private void setSubjectiveSelected(){
        tvSubjectTab.setTextColor(getResources().getColor(R.color.color_666252));
        tvSubjectTab.setBackgroundResource(android.R.color.transparent);
        tvObjectTab.setTextColor(getResources().getColor(R.color.color_b2ab8f));
        tvObjectTab.setBackgroundResource(R.color.color_fff4d9);
        rlObjectContainer.setVisibility(View.GONE);
        rlSubjectContainer.setVisibility(View.VISIBLE);
    }

    private void setObjectiveSelected(){
        tvObjectTab.setTextColor(getResources().getColor(R.color.color_666252));
        tvObjectTab.setBackgroundResource(android.R.color.transparent);
        tvSubjectTab.setTextColor(getResources().getColor(R.color.color_b2ab8f));
        tvSubjectTab.setBackgroundResource(R.color.color_fff4d9);
        rlObjectContainer.setVisibility(View.VISIBLE);
        rlSubjectContainer.setVisibility(View.GONE);
    }



}
