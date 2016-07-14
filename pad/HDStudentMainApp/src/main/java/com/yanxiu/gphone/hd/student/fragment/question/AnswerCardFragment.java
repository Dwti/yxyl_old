package com.yanxiu.gphone.hd.student.fragment.question;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.YanxiuApplication;
import com.yanxiu.gphone.hd.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.hd.student.adapter.YXiuCustomerBaseAdapter;
import com.yanxiu.gphone.hd.student.bean.DataStatusEntityBean;
import com.yanxiu.gphone.hd.student.bean.GroupEventHWRefresh;
import com.yanxiu.gphone.hd.student.bean.PaperTestEntity;
import com.yanxiu.gphone.hd.student.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.hd.student.bean.QuestionEntity;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.bean.ThridExamiEvent;
import com.yanxiu.gphone.hd.student.bean.UploadImageBean;
import com.yanxiu.gphone.hd.student.bean.statistics.StatisticHashMap;
import com.yanxiu.gphone.hd.student.eventbusbean.ExHistoryEventBus;
import com.yanxiu.gphone.hd.student.fragment.GroupHwFragment;
import com.yanxiu.gphone.hd.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestGetQReportTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestSubmitQuesitonTask;
import com.yanxiu.gphone.hd.student.utils.QuestionUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.utils.statistics.DataStatisticsUploadManager;
import com.yanxiu.gphone.hd.student.view.CommonDialog;
import com.yanxiu.gphone.hd.student.view.DelDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/7/7.
 */
public class AnswerCardFragment extends Fragment implements View.OnClickListener {

    public static final int GROUP = 0x01;

    private SubjectExercisesItemBean dataSources;
    private View rootView;
    private GridView gridView;
    private List<PaperTestEntity> dataList;
    private List<QuestionEntity> questionList;
    private AnswerCardAdapter answerCardAdapter;
    private Button btnQuestionSubmit;
    private TextView tvQuestionTitle;
    private LinearLayout llAnswerCard;
    private RelativeLayout rlAnswerCardMark;
    //    private StudentLoadingLayout loadingLayout;
    private RequestSubmitQuesitonTask requestSubmitQuesitonTask;
    private RequestGetQReportTask requestGetQReportTask;
    private CommonDialog dialog;
    //主观题的list
    private List<QuestionEntity> subjectiveList;
    private int subjectiveQIndex = 0;


    private int comeFrom;
    private String questionTitle;


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        this.comeFrom = (arguments == null ? 0 : arguments.getInt("comeFrom"));
        LogInfo.log("geny", "AnswerCardFragment comeFrom------" + comeFrom);

        SubjectExercisesItemBean dataSources = (arguments == null ? null : (SubjectExercisesItemBean) arguments.getSerializable("subjectExercisesItemBean"));
        if (dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()) {
            dataList = dataSources.getData().get(0).getPaperTest();
            questionTitle = dataSources.getData().get(0).getName();
            this.dataSources = dataSources;
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Bundle arguments = getArguments();
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_answer_card, null);
        initView();
        initData();
        return rootView;
    }

    @Override
    public void onResume () {
        super.onResume();

    }

    public void setUserVisibleHint (boolean isVisibleToUser) {
        LogInfo.log("geny", "setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            LogInfo.log("geny", "getUserVisibleHint");
            if (answerCardAdapter != null)
                answerCardAdapter.notifyDataSetChanged();
        }
    }


    private void initView () {
        gridView = (GridView) rootView.findViewById(R.id.answer_card_grid);
        tvQuestionTitle = (TextView) rootView.findViewById(R.id.tv_question_title);
        llAnswerCard = (LinearLayout) rootView.findViewById(R.id.ll_answer_card);
        llAnswerCard.setOnClickListener(null);
        rlAnswerCardMark = (RelativeLayout) rootView.findViewById(R.id.rl_answer_card_mark);
        rlAnswerCardMark.setOnClickListener(this);
//        loadingLayout = (StudentLoadingLayout) rootView.findViewById(R.id.loading_layout);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

                if (AnswerCardFragment.this.getActivity() instanceof AnswerViewActivity) {
                    QuestionEntity questionEntity = answerCardAdapter.getItem(position);
                    ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).setViewPagerPosition(questionEntity.getPageIndex(), questionEntity.getChildPageIndex());
                }
//                else{
//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("position", position);
//                    intent.putExtra("data", bundle);
//                    AnswerCardFragment.this.getActivity().setResult(Activity.RESULT_OK, intent);
//                    AnswerCardFragment.this.getActivity().finish();
//
//                    if(dataList != null && !dataList.isEmpty()){
//                        AnswerBean bean = dataList.get(position).getQuestions().getAnswerBean();
//                        int time = bean.getConsumeTime();
//                        Util.showToast("此题花费-----" + time + "是否正确" + bean.isRight());
//                    }
//                }
            }
        });
        btnQuestionSubmit = (Button) rootView.findViewById(R.id.btn_question_submit);
        btnQuestionSubmit.setOnClickListener(this);

        setAnswerCardPopup();

    }


    public void setAnswerCardPopup () {
        Animation ani = AnimationUtils.loadAnimation(this.getActivity(), R.anim.answer_card_bottom_in);
        ani.setFillAfter(true);
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });
        llAnswerCard.startAnimation(ani);
    }


    private void initData () {

        if (dataList != null) {
            questionList = QuestionUtils.addChildQuestionToParent(dataList);
//                    new ArrayList<QuestionEntity>();
//            int count = dataList.size();
//            for(int i = 0; i < count; i++){
//                if(dataList.get(i) != null && dataList.get(i).getQuestions() != null){
//                    int typeId = dataList.get(i).getQuestions().getType_id();
//                    if(typeId == QUESTION_READING.type){
//                        QuestionEntity questionEntity = dataList.get(i).getQuestions();
//                        if(questionEntity != null){
//                            List<QuestionEntity> childQuestion = questionEntity.getChildren();
//                            if(childQuestion != null){
//                                questionList.addAll(childQuestion);
//                            }
//                            int childCount = childQuestion.size();
//                            for(int j = 0; j < childCount; j++){
//                                childQuestion.get(j).setPageIndex(i);
//                                childQuestion.get(j).setChildPageIndex(j);
//                            }
//                        }
//                    }else{
//                        QuestionEntity questionEntity = dataList.get(i).getQuestions();
//                        questionEntity.setPageIndex(i);
//                        questionList.add(questionEntity);
//                    }
//                }
//            }
        }
        if (!TextUtils.isEmpty(questionTitle)) {
            tvQuestionTitle.setText(questionTitle);
        }
        answerCardAdapter = new AnswerCardAdapter(this.getActivity());
        gridView.setAdapter(answerCardAdapter);
        answerCardAdapter.setList(questionList);


    }


    @Override
    public boolean getUserVisibleHint () {
        this.setUserVisibleHint(true);
        return super.getUserVisibleHint();
    }

    public void setDataSources (SubjectExercisesItemBean dataSources) {
        this.dataSources = dataSources;
        if (this.dataSources != null && this.dataSources.getData() != null && !dataSources.getData().isEmpty()) {
            dataList = this.dataSources.getData().get(0).getPaperTest();
        } else {
            LogInfo.log("geny", "dataSources = null");
        }
    }

    public void refreshAnswerCard () {
        if (answerCardAdapter != null) {
            answerCardAdapter.notifyDataSetChanged();
        }
    }


    private void quitSubmmitDialog () {
        dialog = new CommonDialog(this.getActivity(), this.getActivity().getResources().getString(R.string.question_no_finish),
                this.getActivity().getResources().getString(R.string.question_submit),
                this.getActivity().getResources().getString(R.string.question_cancel),
                new DelDialog.DelCallBack() {
                    @Override
                    public void del () {
                        //2
                        handleUploadSubjectiveImage();

                    }

                    @Override
                    public void sure () {
                        //1
//                        AnswerViewActivity.this.finish();
                    }

                    @Override
                    public void cancel () {
                        //3
                    }
                });
        dialog.show();
    }

    @Override
    public void onClick (final View v) {

        Animation ani = AnimationUtils.loadAnimation(this.getActivity(), R.anim.answer_card_bottom_out);
        ani.setFillAfter(true);
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart (Animation animation) {
            }

            @Override
            public void onAnimationEnd (Animation animation) {
                if (v == btnQuestionSubmit) {
                    ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).hideFragment();
                    if (dataList != null && !dataList.isEmpty()) {
                        int unFinishCount = QuestionUtils.calculationUnFinishQuestion(dataList);
                        if (unFinishCount > 0) {
                            quitSubmmitDialog();
                        } else {
                            handleUploadSubjectiveImage();
                        }
                    }
                } else {
                    ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).removeFragment();
                }
            }

            @Override
            public void onAnimationRepeat (Animation animation) {
            }

        });
        llAnswerCard.startAnimation(ani);
    }


    private void handleUploadSubjectiveImage () {
        subjectiveList = QuestionUtils.findSubjectiveQuesition(dataSources);
        if (!subjectiveList.isEmpty()) {
            LogInfo.log("geny", "subjectiveList===" + subjectiveList.size());

            if (subjectiveQIndex < subjectiveList.size()) {
                uploadSubjectiveImage(subjectiveList.get(subjectiveQIndex));
            } else {
                requestSubmmit();
            }

        } else {
            requestSubmmit();
        }

    }


    /**
     * 上传主观题图片
     */
    private void uploadSubjectiveImage (final QuestionEntity entity) {
        Map<String, File> fileMap = new LinkedHashMap<String, File>();
        List<String> photoUri = entity.getPhotoUri();
        if (photoUri != null && !photoUri.isEmpty()) {
            for (String uri : photoUri) {
                LogInfo.log("geny", "uri===" + uri);
                fileMap.put(String.valueOf(uri.hashCode()), new File(uri));
            }
        } else {
            subjectiveQIndex++;
            handleUploadSubjectiveImage();
            return;
        }

        ((AnswerViewActivity) this.getActivity()).showCommonDialog();
//        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        YanxiuHttpApi.requestUploadImage(fileMap, new YanxiuHttpApi.UploadFileListener() {

            @Override
            public void onFail(final YanxiuBaseBean bean) {
                rootView.post(new Runnable() {
                    @Override
                    public void run() {
                        subjectiveQIndex = 0;
                        ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).hideDialog();
//                        loadingLayout.setViewGone();
                        if (bean != null && ((UploadImageBean) bean).getStatus() != null && ((UploadImageBean) bean).getStatus().getDesc() != null) {
                            Util.showToast(((UploadImageBean) bean).getStatus().getDesc());
                        } else {
                            Util.showToast(R.string.server_connection_erro);
                        }
                    }
                });
                LogInfo.log("geny", "requestUploadImage s =onFail");
            }

            @Override
            public void onSuccess(YanxiuBaseBean bean) {

                UploadImageBean uploadImageBean = (UploadImageBean) bean;
                if (uploadImageBean.getData() != null) {
                    subjectiveQIndex++;
                    entity.getAnswerBean().setSubjectivImageUri((ArrayList<String>) uploadImageBean.getData());
                }
                handleUploadSubjectiveImage();
                LogInfo.log("geny", "requestUploadImage s =onSuccess");
            }

            @Override
            public void onProgress(int progress) {
                if (progress % 10 == 9) {
                    LogInfo.log("geny", "requestUploadImage s =onProgress-----------------" + progress);
                }
            }
        });
    }


    private void requestSubmmit () {
        ((AnswerViewActivity) this.getActivity()).showCommonDialog();
        if (requestSubmitQuesitonTask != null && requestSubmitQuesitonTask.isCancelled()) {
            requestSubmitQuesitonTask.cancel();
        }
        if (AnswerCardFragment.this.getActivity() instanceof AnswerViewActivity) {
            ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).calculationTime();
        }
        final long endtime = System.currentTimeMillis();
        final long groupStartTime = dataSources.getData().get(0).getBegintime();
        final long groupEndtime = dataSources.getData().get(0).getEndtime();//作业练习截止时间
        LogInfo.log("haitian", "groupStartTime="+groupStartTime+"---groupEndtime="+groupEndtime
                +"---endtime="+endtime);
        dataSources.setEndtime(endtime);
        requestSubmitQuesitonTask = new RequestSubmitQuesitonTask(YanxiuApplication.getContext(), dataSources, RequestSubmitQuesitonTask.SUBMIT_CODE, new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
                ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).hideDialog();
                DataStatusEntityBean bean = (DataStatusEntityBean) result;
                if (bean.getCode() == 0) {
                    LogInfo.log("haitian", "comeFrom="+comeFrom);
                    int showana = dataSources.getShowana();
                    if (showana == GroupHwFragment.NOT_FINISH_STATUS){
                        LogInfo.log("haitian", "comeFrom=" + comeFrom);
                        if (comeFrom == AnswerViewActivity.GROUP && groupEndtime > groupStartTime &&
                                ((groupEndtime - System.currentTimeMillis()) >= 3 * 60 * 1000)) {//作业截止时间判断，还未到截止时间不产生作业报告
                            Util.showToast(R.string.update_sucess);
                            EventBus.getDefault().post(new ThridExamiEvent(true));
                            EventBus.getDefault().post(new GroupEventHWRefresh());
                            ((AnswerViewActivity)AnswerCardFragment.this.getActivity()).addFinishFragment(dataSources, YanXiuConstant.END_TIME);
                        }else{
                            jumpReport();
                        }

                    }else if(showana == GroupHwFragment.HAS_FINISH_CHECK_REPORT){
                        jumpReport();
                    }else{
                        Util.showToast(R.string.update_sucess);
                        EventBus.getDefault().post(new ThridExamiEvent(true));
                        EventBus.getDefault().post(new GroupEventHWRefresh());
                        getActivity().finish();
                    }

                    if(comeFrom == YanXiuConstant.HISTORY_REPORT){
                        EventBus.getDefault().post(new ExHistoryEventBus());
                    }
                    ArrayList<StatisticHashMap> arrayList = new ArrayList<StatisticHashMap>();
                    for (int i=0; i<dataSources.getData().size(); i++) {
                        for (int j=0; j<dataSources.getData().get(i).getPaperTest().size(); j++) {
                            StatisticHashMap statisticHashMap = new StatisticHashMap();
                            statisticHashMap.put(YanXiuConstant.eventID, "20:event_3");//3:提交练习/作业
                            HashMap reserveHashMap = new HashMap();
                            reserveHashMap.put(YanXiuConstant.editionID, dataSources.getData().get(i).getBedition());
                            reserveHashMap.put(YanXiuConstant.gradeID, String.valueOf(dataSources.getData().get(i).getGradeid()));
                            reserveHashMap.put(YanXiuConstant.subjectID, String.valueOf(dataSources.getData().get(i).getSubjectid()));
                            reserveHashMap.put(YanXiuConstant.paperType, String.valueOf(dataSources.getData().get(i).getPtype()));
                            reserveHashMap.put(YanXiuConstant.quesNum, String.valueOf(dataSources.getData().get(i).getQuesnum()));
                            reserveHashMap.put(YanXiuConstant.qID, String.valueOf(dataSources.getData().get(i).getPaperTest().get(j).getQid()));
                            statisticHashMap.put(YanXiuConstant.reserved, Util.hashMapToJsonTwo(reserveHashMap));
                            arrayList.add(statisticHashMap);
                        }
                    }
                    submitQuestionStatistic(arrayList);
                } else {
                    if (bean != null && bean.getDesc() != null) {
                        Util.showToast(bean.getDesc());
                    } else {
                        Util.showToast(R.string.server_connection_erro);
                    }
                }
            }

            @Override
            public void dataError (int type, String msg) {
                if (TextUtils.isEmpty(msg)) {
                    Util.showToast(R.string.server_connection_erro);
                } else {
                    Util.showToast(msg);
                }
                ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).hideDialog();
            }
        });
        requestSubmitQuesitonTask.start();

        if (dataSources != null && dataSources.getData() != null && dataSources.getData().size() > 0) {
            PublicErrorQuestionCollectionBean.saveExercisesDataEntity(dataSources.getData().get(0));
        }
    }


    //提交练习/作业
    private void submitQuestionStatistic(ArrayList<StatisticHashMap> arrayList) {
        HashMap<String, String> submitQuestionHashMap = new HashMap<>();
        submitQuestionHashMap.put(YanXiuConstant.content, Util.listToJson(arrayList));
        DataStatisticsUploadManager.getInstance().NormalUpLoadData(getActivity(), submitQuestionHashMap);
    }

    private void jumpReport(){
        requestReport();
        EventBus.getDefault().post(new ThridExamiEvent(true));
        EventBus.getDefault().post(new GroupEventHWRefresh());
    }


    private void requestReport () {
        ((AnswerViewActivity) this.getActivity()).showCommonDialog();
        if (requestGetQReportTask != null && requestGetQReportTask.isCancelled()) {
            requestGetQReportTask.cancel();
        }
        LogInfo.log("geny", "requestReport ppid = " + String.valueOf(dataSources.getData().get(0).getId()));
        requestGetQReportTask = new RequestGetQReportTask(YanxiuApplication.getContext(), String.valueOf(dataSources.getData().get(0).getId()), new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
                ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).hideDialog();
                SubjectExercisesItemBean bean = (SubjectExercisesItemBean) result;
                if (bean.getData() != null && bean.getStatus() != null && bean.getStatus().getCode() == 0) {
                    LogInfo.log("geny", "-------------------->RequestGetQReportTask-------dataSources===" + bean.getData().toString());

                    QuestionUtils.initDataWithAnswer(bean);
                    long endtime = System.currentTimeMillis();
                    bean.setEndtime(endtime);
                    bean.setBegintime(dataSources.getBegintime());

//                    ------------------------------
                    bean.getData().get(0).setStageid(LoginModel.getUserinfoEntity().getStageid() + "");

                    if (dataSources != null && dataSources.getData() != null && dataSources
                            .getData().get(0) != null) {
                        if(TextUtils.isEmpty(bean.getData().get(0).getChapterid())) {
                            bean.getData().get(0).setChapterid(dataSources.getData().get(0).getChapterid());
                        }
                        if(TextUtils.isEmpty(bean.getData().get(0).getChapterName())) {
                            bean.getData().get(0).setChapterName(dataSources.getData().get(0).getChapterName());
                        }
                        if(TextUtils.isEmpty(bean.getData().get(0).getSectionid())) {
                            bean.getData().get(0).setSectionid(dataSources.getData().get(0).getSectionid());
                        }
                        if(TextUtils.isEmpty(bean.getData().get(0).getSectionName())) {
                            bean.getData().get(0).setSectionName(dataSources.getData().get(0).getSectionName());
                        }
                        if(TextUtils.isEmpty(bean.getData().get(0).getCellid())) {
                            bean.getData().get(0).setCellid(dataSources.getData().get(0).getCellid());
                        }
                        if(TextUtils.isEmpty(bean.getData().get(0).getCellName())) {
                            bean.getData().get(0).setCellName(dataSources.getData().get(0).getCellName());
                        }
                        bean.getData().get(0).setIsChapterSection(dataSources.getData().get(0).getIsChapterSection());
                    }
//                    -------------------------------
//                    LogInfo.log("geny", "RequestGetQReportTask comeFrome------" + comeFrom);
//
//                    if (comeFrom == GROUP) {
//                        AnswerReportActivity.launch(AnswerCardFragment.this.getActivity(), bean, YanXiuConstant.HOMEWORK_REPORT, Intent.FLAG_ACTIVITY_FORWARD_RESULT, true);
//                    } else if (comeFrom == YanXiuConstant.HISTORY_REPORT) {
//                        AnswerReportActivity.launch(AnswerCardFragment.this.getActivity(), bean, YanXiuConstant.HISTORY_REPORT, Intent.FLAG_ACTIVITY_FORWARD_RESULT, true);
//                    } else if (comeFrom == YanXiuConstant.THIRD_EXAMPOINT) {
//                        AnswerReportActivity.launch(AnswerCardFragment.this.getActivity(), bean, YanXiuConstant.INTELLI_REPORT, Intent.FLAG_ACTIVITY_FORWARD_RESULT, false);
//                    } else if (comeFrom == YanXiuConstant.KPN_REPORT) {
//                        AnswerReportActivity.launch(AnswerCardFragment.this.getActivity(), bean, YanXiuConstant.KPN_REPORT, Intent.FLAG_ACTIVITY_FORWARD_RESULT, false);
//                    } else {
//                        AnswerReportActivity.launch(AnswerCardFragment.this.getActivity(), bean, YanXiuConstant.INTELLI_REPORT, Intent.FLAG_ACTIVITY_FORWARD_RESULT, false);
//                    }
                    LogInfo.log("geny", "RequestGetQReportTask comeFrome------" + comeFrom);
                    ((AnswerViewActivity)AnswerCardFragment.this.getActivity()).addFinishFragment(bean, comeFrom);
                } else {
                    if (bean != null && bean.getStatus() != null && bean.getStatus().getDesc() != null) {
                        Util.showToast(bean.getStatus().getDesc());
                    } else {
                        Util.showToast(R.string.server_connection_erro);
                    }
                }
            }

            @Override
            public void dataError (int type, String msg) {
                if (TextUtils.isEmpty(msg)) {
                    Util.showToast(R.string.server_connection_erro);
                } else {
                    Util.showToast(msg);
                }
                ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).hideDialog();
            }
        });
        requestGetQReportTask.start();
    }


    @Override
    public void onDestroy () {
        super.onDestroy();
        if (requestSubmitQuesitonTask != null && requestSubmitQuesitonTask.isCancelled()) {
            requestSubmitQuesitonTask.cancel();
        }
    }


    private class AnswerCardAdapter extends YXiuCustomerBaseAdapter<QuestionEntity> {
        private ViewHolder holder;

        public AnswerCardAdapter (Activity context) {
            super(context);
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = mContext.getLayoutInflater();
                row = inflater.inflate(R.layout.item_answer_card, null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) row.findViewById(R.id.answer_card_icon);
                holder.tvIndex = (TextView) row.findViewById(R.id.answer_card_text);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            QuestionEntity data = mList.get(position);
            LogInfo.log("geny", "----------------------->" + data.getAnswerBean().isFinish());
            if (data.getAnswerBean().isFinish()) {
                holder.ivIcon.setBackgroundResource(R.drawable.answer_card_done);
            } else {
                holder.ivIcon.setBackgroundResource(R.drawable.answer_card_undone);
            }
            holder.tvIndex.setText(String.valueOf(position + 1));
            return row;
        }

        private class ViewHolder {
            ImageView ivIcon;
            TextView tvIndex;
        }
    }


}
