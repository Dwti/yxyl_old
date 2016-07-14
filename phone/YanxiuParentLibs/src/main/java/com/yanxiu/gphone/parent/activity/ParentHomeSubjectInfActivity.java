package com.yanxiu.gphone.parent.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;
import com.yanxiu.gphone.parent.bean.ParentHomeSubjectInfoBean;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.requestTask.RequestHomeSubjectInfoTask;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.utils.PublicLoadUtils;
import com.yanxiu.gphone.parent.view.ColorArcProgressBar;
import com.yanxiu.gphone.parent.view.PublicLoadLayout;

/**
 * Created by lidongming on 16/3/25.
 * 家长段首页详情界面
 */
public class ParentHomeSubjectInfActivity extends TopViewBaseActivity {

//    private View mView;

    private ColorArcProgressBar leftRateProgressBar, middleRateProgressBar, rightRateProgressBar;
    private ColorArcProgressBar leftTimeProgressBar, middleTimeProgressBar, rightTimeProgressBar;

    //提交时间排名
    private TextView tvSubmitRank;
    //老师的评语
    private TextView tvTeacherComment;
    //学科的图片
    private ImageView ivSubjectInfoIcon;
    //学科的名称
    private TextView tvSubjectInfoName;
    //pid
    private int pid;
    //学科名称
    private String subjectName;
    //数据源
    private ParentHomeSubjectInfoBean dataSources;

    private TextView tvSubjectInfoTime;

    private RelativeLayout contentLayout;

    private RequestHomeSubjectInfoTask requestHomeSubjectInfoTask;

    private long subjectInfoTime;

    public static void launch(Activity context, int pid, String subjectName, long subjectInfoTime) {
        Intent intent = new Intent(context, ParentHomeSubjectInfActivity.class);
        intent.putExtra("pid", pid);
        intent.putExtra("subjectName", subjectName);
        intent.putExtra("subjectInfoTime", subjectInfoTime);
        context.startActivity(intent);
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mPublicLayout = PublicLoadUtils.createPage(this, R.layout.parent_subject_info_layout);
        mPublicLayout.finish();
        mPublicLayout.setmRefreshData(new PublicLoadLayout.RefreshData() {

            @Override
            public void refreshData() {
                requestData();
            }

        });
        initView(mPublicLayout);
        initData();
        return mPublicLayout;
    }


    private void initData() {
        pid = getIntent().getIntExtra("pid", 0);
        subjectName = getIntent().getStringExtra("subjectName");
        subjectInfoTime = getIntent().getLongExtra("subjectInfoTime", 0);
        if(!TextUtils.isEmpty(subjectName)){
            titleText.setText(subjectName);
        }
        String strTime;
        tvSubjectInfoTime.setVisibility(View.GONE);
        try {
            strTime = ParentUtils.longToFormateTime(subjectInfoTime);
            tvSubjectInfoTime.setText(strTime);
            tvSubjectInfoTime.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        requestData();
    }

    private void initView(View rootView) {
        View ratePercentView = rootView.findViewById(R.id.view_home_percent_rate_layout);
        leftRateProgressBar = (ColorArcProgressBar) ratePercentView.findViewById(R.id.left_class_precision_pb);
        middleRateProgressBar = (ColorArcProgressBar) ratePercentView.findViewById(R.id.center_class_precision_pb);
        rightRateProgressBar = (ColorArcProgressBar) ratePercentView.findViewById(R.id.right_class_precision_pb);

        View timePercentView = rootView.findViewById(R.id.view_home_percent_time_layout);
        leftTimeProgressBar = (ColorArcProgressBar) timePercentView.findViewById(R.id.left_class_precision_pb);
        middleTimeProgressBar = (ColorArcProgressBar) timePercentView.findViewById(R.id.center_class_precision_pb);
        rightTimeProgressBar = (ColorArcProgressBar) timePercentView.findViewById(R.id.right_class_precision_pb);

        tvSubmitRank = (TextView) rootView.findViewById(R.id.parent_submit_rank_num);

        tvTeacherComment = (TextView) rootView.findViewById(R.id.tv_sub_info_techer_comment);

        ivSubjectInfoIcon = (ImageView) rootView.findViewById(R.id.iv_subject_info_icon);
        tvSubjectInfoName = (TextView) rootView.findViewById(R.id.tv_subjcet_info_name);

        contentLayout = (RelativeLayout) rootView.findViewById(R.id.rl_subject_layout);
        tvSubjectInfoTime = (TextView) rootView.findViewById(R.id.tv_home_subject_info_time);
    }
    //请求数据
    private void requestData(){
        cancelTask();
        mPublicLayout.loading(true);
        contentLayout.setVisibility(View.INVISIBLE);
        requestHomeSubjectInfoTask = new RequestHomeSubjectInfoTask(this, pid, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
//                finishPublicLayout();
                mPublicLayout.finish();
                dataSources = (ParentHomeSubjectInfoBean) result;
                if(dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()){
                    contentLayout.setVisibility(View.VISIBLE);
                    updateData(dataSources.getData().get(0));
                    updateRateProgressBar(dataSources.getData().get(0));
                    updateTimeProgressBar(dataSources.getData().get(0));
                }else{
                    mPublicLayout.dataNull(true);
                }
            }

            @Override
            public void dataError(int type, String msg) {
                if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                    mPublicLayout.netError(true);
                } else {
                    if (TextUtils.isEmpty(msg)) {
                        mPublicLayout.dataNull(true);
                    } else {
                        mPublicLayout.dataNull(msg);
                    }
                }
            }
        });

        requestHomeSubjectInfoTask.start();

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    private void cancelTask(){
        if(requestHomeSubjectInfoTask != null){
            requestHomeSubjectInfoTask.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTask();
        requestHomeSubjectInfoTask = null;
    }

    /**
     * 平均时间的progessbar
     * @param dataBean
     */
    private void updateTimeProgressBar(ParentHomeSubjectInfoBean.DataBean dataBean){

        if(dataBean != null){
            ParentHomeSubjectInfoBean.DataBean.DetailJsonBean detailJsonBean = dataBean.getDetailJson();
            if(detailJsonBean != null){
                //用时超过的学生
                int beatStudentNum = detailJsonBean.getCostTimeBeatNum();
                String beatStudentNumHint = this.getResources().getString(R.string.parent_beat_student_num);
                String beatTimeNum = String.format(beatStudentNumHint, beatStudentNum);
                middleTimeProgressBar.setHintString(beatTimeNum);
                //平均用时
                int classAvgCostTime = detailJsonBean.getClassAvgCostTime();
                String classAvgCostTimeStr = ParentUtils.formatTime(classAvgCostTime);
                leftTimeProgressBar.setCurrentString(classAvgCostTimeStr);
                //用时最短超过人数
                int paperCostTime = detailJsonBean.getPaperCostTime();
                String paperCostTimeStr = ParentUtils.formatTime(paperCostTime);
                middleTimeProgressBar.setCurrentString(paperCostTimeStr);
                //班级最短用时
                int classMinCostTime = detailJsonBean.getClassMinCostTime();
                String classMinCostTimeStr = ParentUtils.formatTime(classMinCostTime);
                rightTimeProgressBar.setCurrentString(classMinCostTimeStr);

            }
        }

    }

    /**
     * 正确率的progressbar
     * @param dataBean
     */
    private void updateRateProgressBar(ParentHomeSubjectInfoBean.DataBean dataBean){

        if(dataBean != null){
            ParentHomeSubjectInfoBean.DataBean.DetailJsonBean detailJsonBean = dataBean.getDetailJson();
            if(detailJsonBean != null){
                //用时超过的学生
                int beatStudentRateNum = detailJsonBean.getRateBeatNum();
                String beatStudentNumHint = this.getResources().getString(R.string.parent_beat_student_num);
                String beateRateNum = String.format(beatStudentNumHint, beatStudentRateNum);
                middleRateProgressBar.setHintString(beateRateNum);

                //设置学科的字体颜色和图标
                int subjectId = detailJsonBean.getSubjectId();
                int fontColor = ParentUtils.getSubjectTextColor(subjectId);
                middleRateProgressBar.setFontColor(fontColor);
                //平均正确率
                int classAvgRate = (int)(detailJsonBean.getClassAvgRate() * 100);
                leftRateProgressBar.setIsAnim(true);
                leftRateProgressBar.setCurrentValues(classAvgRate);
                //自己的正确率
                int paperRate = (int)(detailJsonBean.getPaperRate() * 100);
                middleRateProgressBar.setIsAnim(true);
                middleRateProgressBar.setCurrentValues(paperRate);
                //班级最短用时
                int classMaxRate = (int)(detailJsonBean.getClassMaxRate() * 100);
                rightRateProgressBar.setIsAnim(true);
                rightRateProgressBar.setCurrentValues(classMaxRate);

            }
        }
    }

    private void updateData(ParentHomeSubjectInfoBean.DataBean dataBean){

        if(dataBean != null){
            ParentHomeSubjectInfoBean.DataBean.DetailJsonBean detailJsonBean = dataBean.getDetailJson();
            if(detailJsonBean != null){
                //提交排名
                int submitRank = detailJsonBean.getSubmitRank();
                tvSubmitRank.setText(String.valueOf(submitRank));
                //学科名称
                String subjectName = detailJsonBean.getSubjectName();
                if(!TextUtils.isEmpty(subjectName)){
                    tvSubjectInfoName.setText(subjectName);
                }
                //设置学科的字体颜色和图标
                int subjectId = detailJsonBean.getSubjectId();
                ParentUtils.setSubjectTextColor(subjectId, tvSubjectInfoName);
                ParentUtils.setIcon(subjectId, ivSubjectInfoIcon);
                //老师的评语
                if(!TextUtils.isEmpty(detailJsonBean.getTeacherComment())){
                    String teacherComment = this.getResources().getString(R.string.parent_home_teacher_comment);
                    teacherComment = String.format(teacherComment, detailJsonBean.getTeacherComment());
                    tvTeacherComment.setText(teacherComment);
                }else{
                    tvTeacherComment.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    protected void setContentListener() {

    }

    @Override
    protected void destoryData() {

    }

    @Override
    protected void initLaunchIntentData() {

    }

}
