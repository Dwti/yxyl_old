package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.core.utils.TimeUtils;
import com.common.core.view.UnMoveGridView;
import com.common.login.LoginModel;
import com.common.share.ShareEnums;
import com.common.share.ShareExceptionEnums;
import com.common.share.ShareManager;
import com.common.share.constants.ShareConstants;
import com.common.share.inter.ShareResultCallBackListener;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.ExercisesDataEntity;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestIntelliExeTask;
import com.yanxiu.gphone.student.requestTask.RequestKnpointQBlockTask;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.ShareDialog;
import com.yanxiu.gphone.student.view.StudentLoadingLayout;
import com.yanxiu.gphone.student.view.TitleView;
import com.yanxiu.gphone.student.view.question.report.PercentageBirdLayout;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2015/7/6.
 */
public class AnswerReportActivity extends YanxiuBaseActivity implements View.OnClickListener {

    private Context mContext;
    private ArrayList<PaperTestEntity> dataList;
    private ScrollView scrollView;
    private GridView gridView_old;
    private int gridViewWidth;
    private int gridCount = 5;
    public static SubjectExercisesItemBean dataSources;
    private TextView tvReport;
    private TextView tvReportNumTitle;
    private TextView tvReportNumText;
    private TextView tvReportSccuracy;
    private TextView tvReportTimeText;
    private TextView tvReportToptitle;

    private List<QuestionEntity> questionWrongList;

    private int questionCount = 0;    //题目总数

    private LinearLayout ll_grid;
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

    //时候有客观题 默认是的时候有客观题
    private boolean isObjectiveQuestion = true;


    ///////////////////////////

    private ImageView ivBack;
    private ImageView shareView;

    private List<Integer> rightQuestionNum;
    private StudentLoadingLayout loadingLayout;

    protected TextView tvQuestionTitle;

    private int comeFrom = -1;

    private int costTime;

    private AnswerCardAdapter adapter;


    private boolean isAllRight = false;

    private ImageView ivReportStamp;

    private boolean isGonePracticeAgain;

    private PercentageBirdLayout pbNumText;
    private PercentageBirdLayout pbAccuracyText;

    private ShareDialog shareDialog;

    private int sccuracy;  //正确率



    private boolean isFinishLayout = false;

    private TextView tvObjectiveLine;


    private RelativeLayout rlObjectContainer;
    private int rightCount;


    public static void launch(Activity context, SubjectExercisesItemBean dataSources, int comeFrom, int flags, boolean isGonePracticeAgain) {
        Intent intent = new Intent(context, AnswerReportActivity.class);
        intent.putExtra("subjectExercisesItemBean", dataSources);
        intent.putExtra("comeFrom", comeFrom);
        intent.putExtra("isGonePracticeAgain", isGonePracticeAgain);
        intent.setFlags(flags);
        context.startActivity(intent);
    }


    public static void launch(Activity context, SubjectExercisesItemBean dataSources) {
        Intent intent = new Intent(context, AnswerReportActivity.class);
        intent.putExtra("subjectExercisesItemBean", dataSources);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_report);
        mContext=this;
        initView();
        initData();
    }


    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_top_back);
        shareView = (ImageView) findViewById(R.id.report_share);
        gridView_old = (GridView) this.findViewById(R.id.answer_report_grid);
        gridView_old.setFocusable(false);
        tvReport = (TextView) this.findViewById(R.id.tv_report_question);
        tvReportNumTitle = (TextView) this.findViewById(R.id.report_num_title_01);
        tvReportNumText = (TextView) this.findViewById(R.id.report_num_text);
        tvReportSccuracy = (TextView) this.findViewById(R.id.report_accuracy_text);
//        tvReportTimeTitle = (TextView) this.findViewById(R.id.report_time_title);
        tvReportTimeText = (TextView) this.findViewById(R.id.report_time_text);
        ivReportStamp = (ImageView) this.findViewById(R.id.iv_report_stamp);
        scrollView= (ScrollView) findViewById(R.id.report_scrollview);
        scrollView.setFocusable(true);
        ll_grid = (LinearLayout) findViewById(R.id.ll_grid);
        btnViewResolution = (Button) this.findViewById(R.id.btn_view_resolution);
        loadingLayout = (StudentLoadingLayout) findViewById(R.id.loading_layout);
        btnPracticeAgain = (Button) this.findViewById(R.id.practice_again);
        btnErrorResolution = (Button) this.findViewById(R.id.error_resolution);
        tvQuestionTitle = (TextView) this.findViewById(R.id.tv_anwser_tips);
        tvReportToptitle = (TextView) this.findViewById(R.id.tv_top_title);

//        reportLine = this.findViewById(R.id.report_line);
        pbNumText = (PercentageBirdLayout) this.findViewById(R.id.pb_report_num_text);
        pbAccuracyText = (PercentageBirdLayout) this.findViewById(R.id.pb_accuracy_text);


        rlObjectContainer = (RelativeLayout) this.findViewById(R.id.rl_object_tab);

        tvObjectiveLine = (TextView) this.findViewById(R.id.tv_objective_line);

        ivBack.setOnClickListener(this);
        btnViewResolution.setOnClickListener(this);
        btnPracticeAgain.setOnClickListener(this);
        btnErrorResolution.setOnClickListener(this);
        shareView.setOnClickListener(this);
    }

    private void initData() {
        rightQuestionNum = new ArrayList<Integer>();
        dataSources = (SubjectExercisesItemBean) getIntent().getSerializableExtra("subjectExercisesItemBean");
        comeFrom = getIntent().getIntExtra("comeFrom", -1);
        isGonePracticeAgain = getIntent().getBooleanExtra("isGonePracticeAgain", false);
        if (dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()) {


            dataList = new ArrayList<PaperTestEntity>();
            dataList.addAll(dataSources.getData().get(0).getPaperTest());



            if (!TextUtils.isEmpty(dataSources.getData().get(0).getName())) {
                tvQuestionTitle.setText(dataSources.getData().get(0).getName());
            }

            if (dataList != null) {

                questionList = QuestionUtils.addChildQuestionToParent(dataList);
                String objectTitile;
                if (questionList != null && !questionList.isEmpty()) {

                    addGridView(QuestionUtils.classifyQuestionByType(questionList));
                    //TODO 完成度
//                    int reportSccuracy = (finishCount * 100) / questionCount;
                    int reportSccuracy = 20;

                    tvReportNumText.setText(reportSccuracy + "%");
                    pbNumText.setAccuracyCount(reportSccuracy);
                    sccuracy = 0;

                    questionCount = questionList.size();
                    rightCount = QuestionUtils.calculateRightCount(questionList);
                    sccuracy = (int) (QuestionUtils.calculateRightRate(questionList)*100);
                    pbAccuracyText.setAccuracyCount(sccuracy);

                    tvReportSccuracy.setText(sccuracy + "%");

                    if (questionList.size() <= rightCount) {
                        isAllRight = true;
                    }

                    objectTitile = String.format(this.getResources().getString(R.string.objective_title), sccuracy);
                    tvObjectiveLine.setText(objectTitile);

                    adapter = new AnswerCardAdapter(questionList);
                    gridView_old.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            gridView_old.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            gridViewWidth = gridView_old.getWidth();
                            gridView_old.setAdapter(adapter);
                            layoutFinishData();
                        }
                    });

                    isObjectiveQuestion = true;

                } else {
                    gridView_old.setVisibility(View.GONE);
                    objectTitile = String.format(this.getResources().getString(R.string.report_no_data), 0);
                    tvObjectiveLine.setText(objectTitile);
                    isObjectiveQuestion = false;
                }

            }

        }
    }

    private void addGridView(Map<String,List<QuestionEntity>> map) {
        if(map == null || map.size()==0)
            return;
        Set<Map.Entry<String,List<QuestionEntity>>> set  = map.entrySet();
        Iterator<Map.Entry<String,List<QuestionEntity>>> iterator = set.iterator();
        while (iterator.hasNext()){
            Map.Entry<String,List<QuestionEntity>> entry = iterator.next();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TitleView titleView = new TitleView(mContext);
            titleView.setLayoutParams(layoutParams);
            titleView.setTitle(entry.getKey().toString());
            UnMoveGridView gridView = new UnMoveGridView(mContext);
            gridView.setLayoutParams(layoutParams);
            gridView.setAdapter(new AnswerCardAdapter(entry.getValue()));
            gridView.setNumColumns(5);
            gridView.setHorizontalSpacing(20);
//            gridView_old.setSelector();
            gridView.setVerticalSpacing(20);

            ll_grid.addView(titleView);
            ll_grid.addView(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    QuestionEntity questionEntity = (QuestionEntity) parent.getAdapter().getItem(position);
                    jumpType(questionEntity, dataList);
                }
            });
        }
    }

    private void layoutFinishData() {
        if (dataList != null && !isFinishLayout) {
            isFinishLayout = true;
            tvReportNumTitle.setText(this.getResources().getString(R.string.hw_question_finish_sccuracy));
        }
        if (dataSources.getData().get(0) != null && dataSources.getData().get(0).getPaperStatus() != null) {
            tvReport.setText(this.getResources().getString(R.string.answer_report_time) + TimeUtils.getTimeLongYMD(dataSources.getData().get(0).getPaperStatus().getEndtime()));
        }
        tvReportTimeText.setText(this.getResources().getString(R.string.answer_cost_time) + formatTime(costTime));


        if (isGonePracticeAgain) {
            btnPracticeAgain.setVisibility(View.GONE);
        }
        switch (comeFrom) {
            case YanXiuConstant.HOMEWORK_REPORT:
                tvReportToptitle.setText(getResources().getString(R.string.answer_report));
                shareView.setVisibility(View.GONE);
                ivReportStamp.setImageResource(R.drawable.gruop_report_stamp);
                break;
            case YanXiuConstant.INTELLI_REPORT:
            case YanXiuConstant.KPN_REPORT:
            case YanXiuConstant.HISTORY_REPORT:
                tvReportToptitle.setText(getResources().getString(R.string.exercises_report));
                ivReportStamp.setImageResource(R.drawable.answer_report_stamp);
                shareView.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void jumpType(QuestionEntity questionEntity, ArrayList<PaperTestEntity> dataList) {


        if (dataList != null && !dataList.isEmpty()) {
            if (dataList.get(questionEntity.getPageIndex()).getQuestions() != null) {
                dataList.get(questionEntity.getPageIndex()).getQuestions().setChildPageIndex(questionEntity.getChildPageIndex());
                dataSources.setIsResolution(true);
                if (dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()) {
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

    private void calculationSubQuestionTime(ArrayList<PaperTestEntity> dataList) {
        int count = dataList.size();
        LogInfo.log("geny", "calculationRightQuestion count =====" + count);
        for (int i = 0; i < count; i++) {
            if (dataList.get(i) != null && dataList.get(i).getQuestions() != null) {
                costTime += dataList.get(i).getQuestions().getAnswerBean().getConsumeTime();
            }
        }
    }




    private void forResult() {
        LogInfo.log("king", "AnswerReportActivity forResult");
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            forResult();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack) {
            forResult();
            finish();
        } else if (v == shareView) {
            if (NetWorkTypeUtils.isNetAvailable()) {
                Util.showToast(R.string.net_null);
                return;
            }
            int count = 0;
            if (dataList != null) {
                count = dataList.size();
            }

            String dis = "";
            if (sccuracy <= 20) {
                dis = getResources().getString(R.string.share_discription_one);
            } else if (sccuracy <= 60) {
                dis = getResources().getString(R.string.share_discription_two);
            } else if (sccuracy <= 80) {
                dis = getResources().getString(R.string.share_discription_three);
            } else if (sccuracy <= 100) {
                dis = getResources().getString(R.string.share_discription_four);
            }
            final String discription = dis;
            if (dataSources != null && dataSources.getData() != null && dataSources.getData().get(0) != null) {
                StringBuilder sb = new StringBuilder("http://wx.hwk.yanxiu.com/sharePage.do?");
                sb.append("uid=" + LoginModel.getUid());
                sb.append("&ppid=" + dataSources.getData().get(0).getId());
                sb.append("&buildtime=" + dataSources.getData().get(0).getBuildtime());
                sb.append("&ostype=gphone");
                final String shareUrl = sb.toString();
                if (shareDialog == null) {
                    shareDialog = new ShareDialog(this, new ShareDialog.ShareCallBack() {
                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void wechatShare() {

                            Bundle bundle = createWeiShareParams(ShareConstants.WEIXIN_SHARE_TYPE_TALK, ShareConstants.WEIXIN_SHARE_WAY_WEBPAGE, discription, shareUrl);
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

                        @Override
                        public void wechatFridsShare() {

                            Bundle bundle = createWeiShareParams(ShareConstants.WEIXIN_SHARE_TYPE_FRENDS, ShareConstants.WEIXIN_SHARE_WAY_WEBPAGE, discription, shareUrl);
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

                        @Override
                        public void qqShare() {
                            Bundle bundle = new Bundle();
                            bundle.putString(ShareConstants.QQ_APPID_KEY, ShareConstants.STUDENT_QQ_AppID);
                            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                            //这条分享消息被好友点击后的跳转URL。
                            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
                            //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能t全为空，最少必须有一个是有值的。
                            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, getResources().getString(
                                    R.string.share_title));
                            //分享的图片URL
                            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, YanXiuConstant.SHARE_ICON_PATH + YanXiuConstant.SHARE_LOGO_NAME);
                            //分享的消息摘要，最长50个字
                            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, discription);
                            //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
                            bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getResources().getString(R.string.app_name));
                            //标识该消息的来源应用，值为应用名称+AppId。
                            bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);

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

                        @Override
                        public void qzoneShare() {
                            Bundle bundle = new Bundle();
                            bundle.putString(ShareConstants.QQ_APPID_KEY, ShareConstants.STUDENT_QQ_AppID);
                            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                                    QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                            //这条分享消息被好友点击后的跳转URL。
                            bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
                            //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
                            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, getResources().getString(R.string.share_title));
                            //分享的图片URL
                            ArrayList<String> icon = new ArrayList<String>();
//        icon.add("http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
                            icon.add(YanXiuConstant.SHARE_ICON_PATH + YanXiuConstant.SHARE_LOGO_NAME);
                            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, icon);
                            //分享的消息摘要，最长50个字
                            bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, discription);

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
        } else if (v == btnViewResolution) {
            dataSources.setIsResolution(true);
            if (dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()) {

//                dataSources.getData().get(0).setPaperTest(allDataList);

//                LogInfo.log("geny", "-------------------->dataSources===" + dataList.get(0).toString());
            }
            setIsTestCenterOnclick();
            ResolutionAnswerViewActivity.launch(AnswerReportActivity.this, dataSources, comeFrom);
        } else if (v == btnPracticeAgain) {

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
                switch (comeFrom) {
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

        } else if (v == btnErrorResolution) {
            //产品定义
            if (isAllRight) {
                Util.showToast(R.string.paper_test_all_right);
            } else {
                dataSources.setIsResolution(true);
                setIsTestCenterOnclick();
                ResolutionAnswerViewActivity.launch(AnswerReportActivity.this, dataSources, comeFrom);
            }
        }
    }

    private Bundle createWeiShareParams(int shareType, int shareWayType, String discription, String shareUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(ShareConstants.WX_APPID_KEY, ShareConstants.STUDENT_WX_AppID);
        bundle.putInt(ShareConstants.WEIXIN_SHARE_TYPE, shareType);
        bundle.putInt(ShareConstants.WEIXIN_SHARE_WAY_TYPE, shareWayType);
        bundle.putString(ShareConstants.WEIXIN_SHARE_TITLE_KEY, getResources().getString(R.string.share_title));
        bundle.putString(ShareConstants.WEIXIN_SHARE_DES_KEY, discription);
        bundle.putString(ShareConstants.WEIXIN_SHARE_IMG_URL, shareUrl);
        bundle.putInt(ShareConstants.WEIXIN_SHARE_IMG_ICON, R.mipmap.share_app_icon);
        return bundle;
    }


    private void setIsTestCenterOnclick() {
        if (dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()) {
            if (comeFrom == YanXiuConstant.KPN_REPORT || comeFrom == YanXiuConstant.INTELLI_REPORT) {
                dataSources.getData().get(0).setIsTestCenterOnclick(true);
            } else {
                dataSources.getData().get(0).setIsTestCenterOnclick(false);
            }
        }
    }


    private void requestSubjectExercises() {
        LogInfo.log("geny", "requestSubjectExercises");
        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_INTELLI_EXE);
        new RequestIntelliExeTask(this, stageId, subjectId, editionId, chapterId, sectionId, SubjectSectionActivity.QUESTON_COUNT, volume, cellid, new AsyncCallBack() {
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

                        if (!TextUtils.isEmpty(volume)) {
                            subjectExercisesItemBean.getData().get(0).setVolume(volume);
                        }
                        if (!TextUtils.isEmpty(volumeName)) {
                            subjectExercisesItemBean.getData().get(0).setVolumeName(volumeName);
                        }


                        subjectExercisesItemBean.getData().get(0).setChapterid(chapterId);
                        subjectExercisesItemBean.getData().get(0).setChapterName(chapterName);

                        subjectExercisesItemBean.getData().get(0).setSectionid(sectionId);
                        subjectExercisesItemBean.getData().get(0).setSectionName(sectionName);
                        AnswerViewActivity.launch(AnswerReportActivity.this, subjectExercisesItemBean, comeFrom);
//                        AnswerViewActivity.launch(AnswerReportActivity.this, subjectExercisesItemBean);
                        AnswerReportActivity.this.finish();
                    }
                } else {
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

                        if (!TextUtils.isEmpty(volume)) {
                            subjectExercisesItemBean.getData().get(0).setVolume(volume);
                        }
                        if (!TextUtils.isEmpty(volumeName)) {
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


    private class AnswerCardAdapter extends BaseAdapter {

        private ViewHolder holder;
        private List<QuestionEntity> mList = new ArrayList<>();

        public AnswerCardAdapter(List<QuestionEntity> mList) {
            this.mList=mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            QuestionEntity data = mList.get(position);
            if (data == null || data.getAnswerBean() == null)
                return null;
            AnswerBean answerBean = data.getAnswerBean();
            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                row = inflater.inflate(R.layout.item_report_card,null);
                holder = new ViewHolder();
                holder.flContent = (RelativeLayout) row.findViewById(R.id.rl_report_content);
                holder.ivSign = (TextView) row.findViewById(R.id.answer_report_icon);
                holder.tvIndex = (TextView) row.findViewById(R.id.answer_report_text);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            if (answerBean.isSubjective()) {                //如果是主观题
                if (answerBean.getRealStatus() == AnswerBean.ANSER_READED) {                                   //如果已批改
                    if (answerBean.isHalfRight()) {
                        holder.ivSign.setBackgroundResource(R.drawable.answer_report_half_correct);
                    } else if (answerBean.isRight()) {
                        holder.ivSign.setBackgroundResource(R.drawable.answer_report_correct);
                    } else if (!answerBean.isRight()) {
                        holder.ivSign.setBackgroundResource(R.drawable.answer_report_wrong);
                    }
                }
                //如果未批改，状态不做处理
            } else {          //如果是客观题
                if (answerBean.isFinish()) {
                    if (answerBean.isRight()) {
                        if (!rightQuestionNum.contains(position)) {
                            rightQuestionNum.add(position);
                        }
                        holder.ivSign.setBackgroundResource(R.drawable.answer_report_correct);
                    } else {
                        holder.ivSign.setBackgroundResource(R.drawable.answer_report_wrong);
                    }
                } else {
                    //客观题没做视为 错题
                    holder.ivSign.setBackgroundResource(R.drawable.answer_report_wrong);
                }
            }
            if (data.getChildPositionForCard() == -1) {             //等于-1表示不是复合题类型的解答题(只有是复合题且是解答题的时候才需要显示小题号)
                holder.tvIndex.setText((data.getPositionForCard() + 1) + "");
            } else {
                holder.tvIndex.setText((data.getPositionForCard() + 1) + "-" + (data.getChildPositionForCard() + 1));
            }
            return row;
        }


    }

    private class ViewHolder {
        TextView ivSign;
        TextView tvIndex;
        RelativeLayout flContent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogInfo.log("king", "AnswerReportActivity onActivityResult");
        if (requestCode == 10103) {//QQ登陆界面返回码
            if (resultCode == RESULT_CANCELED) {
                LogInfo.log("king", "AnswerReportActivity onActivityResult 10103");
            }
        }
        if (requestCode == 10104) {//qq空间登陆界面返回码
            if (resultCode == RESULT_CANCELED) {
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
        ShareManager.getInstance().clearInstance();
    }




}
