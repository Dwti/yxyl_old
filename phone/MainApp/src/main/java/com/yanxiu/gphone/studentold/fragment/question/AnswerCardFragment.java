package com.yanxiu.gphone.studentold.fragment.question;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.Toast;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.YanxiuApplication;
import com.yanxiu.gphone.studentold.activity.AnswerViewActivity;
import com.yanxiu.gphone.studentold.activity.GroupHwActivity;
import com.yanxiu.gphone.studentold.adapter.YXiuCustomerBaseAdapter;
import com.yanxiu.gphone.studentold.bean.DataStatusEntityBean;
import com.yanxiu.gphone.studentold.bean.ExHistoryEventBus;
import com.yanxiu.gphone.studentold.bean.GroupEventHWRefresh;
import com.yanxiu.gphone.studentold.bean.PaperTestEntity;
import com.yanxiu.gphone.studentold.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.studentold.bean.QuestionEntity;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.bean.ThridExamiEvent;
import com.yanxiu.gphone.studentold.bean.UploadImageBean;
import com.yanxiu.gphone.studentold.bean.statistics.StatisticHashMap;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.requestTask.RequestGetQReportTask;
import com.yanxiu.gphone.studentold.requestTask.RequestSubmitQuesitonTask;
import com.yanxiu.gphone.studentold.utils.QuestionUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;
import com.yanxiu.gphone.studentold.utils.statistics.DataStatisticsUploadManager;
import com.yanxiu.gphone.studentold.view.CommonDialog;
import com.yanxiu.gphone.studentold.view.DelDialog;
import com.yanxiu.gphone.studentold.view.LoadingDialog;

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
    private LinearLayout rlAnswerCard;
    private ImageView ivAnswerCardClose;
    private RelativeLayout rlAnswerCardMark;
    private LinearLayout llAnswerCardMark;
    //    private StudentLoadingLayout loadingLayout;
    private RequestSubmitQuesitonTask requestSubmitQuesitonTask;
    private RequestGetQReportTask requestGetQReportTask;
    private CommonDialog dialog;
    //主观题的list
    private List<QuestionEntity> subjectiveList;
    private int subjectiveQIndex = 0;


    private int comeFrom;
    private String questionTitle;
    private LoadingDialog mLoadingDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        this.comeFrom = (arguments == null ? 0 : arguments.getInt("comeFrom"));
        LogInfo.log("geny", "AnswerCardFragment comeFrom------" + comeFrom);

        SubjectExercisesItemBean dataSources = (arguments == null ? null : (SubjectExercisesItemBean) arguments.getSerializable("subjectExercisesItemBean"));
        //SubjectExercisesItemBean dataSources = Util.getSubjectExercisesItemBean();
        if (dataSources != null && dataSources.getData() != null && !dataSources.getData().isEmpty()) {
            dataList = dataSources.getData().get(0).getPaperTest();
            questionTitle = dataSources.getData().get(0).getName();
            this.dataSources = dataSources;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Bundle arguments = getArguments();
        rootView = inflater.inflate(R.layout.fragment_answer_card, null);
        initView();
        initData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogInfo.log("geny", "setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            LogInfo.log("geny", "getUserVisibleHint");
            if (answerCardAdapter != null)
                answerCardAdapter.notifyDataSetChanged();
        }
    }


    private void initView() {
        gridView = (GridView) rootView.findViewById(R.id.answer_card_grid);
        tvQuestionTitle = (TextView) rootView.findViewById(R.id.tv_question_title);
        rlAnswerCard = (LinearLayout) rootView.findViewById(R.id.rl_answer_card);
        rlAnswerCard.setOnClickListener(null);
        ivAnswerCardClose = (ImageView) rootView.findViewById(R.id.iv_answer_card_close);
        ivAnswerCardClose.setOnClickListener(this);
        rlAnswerCardMark = (RelativeLayout) rootView.findViewById(R.id.rl_answer_card_mark);
        rlAnswerCardMark.setOnClickListener(this);
        llAnswerCardMark = (LinearLayout) rootView.findViewById(R.id.ll_answer_card_mark);
//        loadingLayout = (StudentLoadingLayout) rootView.findViewById(R.id.loading_layout);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (AnswerCardFragment.this.getActivity() instanceof AnswerViewActivity) {
                    QuestionEntity questionEntity = answerCardAdapter.getItem(position);
                    ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).setViewPagerPosition(questionEntity.getPageIndex(), questionEntity.getChildPageIndex());
                }
            }
        });
        btnQuestionSubmit = (Button) rootView.findViewById(R.id.btn_question_submit);
        btnQuestionSubmit.setOnClickListener(this);

        setAnswerCardPopup();

    }


    public void setAnswerCardPopup() {
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
        //rlAnswerCard.startAnimation(ani);
        llAnswerCardMark.startAnimation(ani);
    }


    private void initData() {

        if (dataList != null) {
            questionList = QuestionUtils.addChildQuestionToParent(dataList);
        }
        if (!TextUtils.isEmpty(questionTitle)) {
            tvQuestionTitle.setText(questionTitle);
        }
        answerCardAdapter = new AnswerCardAdapter(this.getActivity());
        gridView.setAdapter(answerCardAdapter);
        answerCardAdapter.setList(questionList);
        mLoadingDialog = new LoadingDialog(getActivity());
        mLoadingDialog.setCanceledOnTouchOutside(false);

    }


    @Override
    public boolean getUserVisibleHint() {
        this.setUserVisibleHint(true);
        return super.getUserVisibleHint();
    }

    public void setDataSources(SubjectExercisesItemBean dataSources) {
        this.dataSources = dataSources;
        if (this.dataSources != null && this.dataSources.getData() != null && !dataSources.getData().isEmpty()) {
            dataList = this.dataSources.getData().get(0).getPaperTest();
        } else {
            LogInfo.log("geny", "dataSources = null");
        }
    }

    public void refreshAnswerCard() {
        if (answerCardAdapter != null) {
            answerCardAdapter.notifyDataSetChanged();
        }
    }


    private void quitSubmmitDialog() {
        dialog = new CommonDialog(this.getActivity(), this.getActivity().getResources().getString(R.string.question_no_finish),
                this.getActivity().getResources().getString(R.string.question_submit),
                this.getActivity().getResources().getString(R.string.question_cancel),
                new DelDialog.DelCallBack() {
                    @Override
                    public void del() {
                        //2
                        handleUploadSubjectiveImage();

                    }

                    @Override
                    public void sure() {
                        //1
//                        getActivity().finish();
                    }

                    @Override
                    public void cancel() {
                        //3
                    }
                });
        dialog.show();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.rl_answer_card_mark:
                hideCardFragment();
                break;
            case R.id.btn_question_submit:
                if (!NetWorkTypeUtils.isNetAvailable()) {
                    if (getActivity()==null){
                        return;
                    }
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.public_loading_net_null_errtxt), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (getActivity()==null){
                    return;
                }
                ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).hideFragment();
                if (dataList != null && !dataList.isEmpty()) {
                    int unFinishCount = QuestionUtils.calculationUnFinishQuestion(dataList);
                    if (unFinishCount > 0) {
                        Log.i("unfinish", unFinishCount + "");
                        quitSubmmitDialog();
                    } else {
                        handleUploadSubjectiveImage();
                    }
                }
                break;
            case R.id.iv_answer_card_close:
                hideCardFragment();
                break;
        }

    }

    private void hideCardFragment() {
        try {
            Animation ani = AnimationUtils.loadAnimation(this.getActivity(), R.anim.answer_card_bottom_out);
            ani.setFillAfter(true);
            ani.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    try {
                        ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).removeFragment();
                    } catch (Exception e) {
                        e.toString();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

            });
            //rlAnswerCard.startAnimation(ani);
            llAnswerCardMark.startAnimation(ani);
        } catch (Exception e) {
        }
    }


    private void handleUploadSubjectiveImage() {
        subjectiveList = QuestionUtils.findSubjectiveQuesition(dataSources);
        mLoadingDialog.setmCurrent(subjectiveQIndex);
        mLoadingDialog.setmNum(subjectiveList.size());
        if (subjectiveList.size() > 0) {
            mLoadingDialog.show();
            mLoadingDialog.updateUI();
            mLoadingDialog.setTipText();
        }
        if (!subjectiveList.isEmpty()) {
            LogInfo.log("geny", "subjectiveList===" + subjectiveList.size());

            if (subjectiveQIndex < subjectiveList.size()) {
                uploadSubjectiveImage(subjectiveList.get(subjectiveQIndex));
            } else {
                mLoadingDialog.dismiss();
                requestSubmmit();
            }

        } else {
            mLoadingDialog.dismiss();
            requestSubmmit();
        }

    }


    private void submit_image_fail(final YanxiuBaseBean bean){
        rootView.post(new Runnable() {
            @Override
            public void run() {
                subjectiveQIndex = 0;
                if (getActivity()==null){
                    return;
                }
                if (AnswerCardFragment.this.getActivity()!=null) {
                    ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).hideDialog();
                }
//                        loadingLayout.setViewGone();
                if (bean != null && ((UploadImageBean) bean).getStatus() != null && ((UploadImageBean) bean).getStatus().getDesc() != null) {
                    //Util.showToast(((UploadImageBean) bean).getStatus().getDesc());
                } else {
                    //Util.showToast(R.string.server_connection_erro);
                }
                saveNetErrorDialog();
            }
        });
        LogInfo.log("geny", "requestUploadImage s =onFail");
    }

    private void submit_image_sucrss(){

    }

    /**
     * 上传主观题图片
     */
    private void uploadSubjectiveImage(final QuestionEntity entity) {
        Map<String, File> fileMap = new LinkedHashMap<String, File>();
        List<String> photoUri = entity.getPhotoUri();
        final ArrayList<String> httpUrl = new ArrayList<>();
        if (photoUri != null && !photoUri.isEmpty()) {
            for (String uri : photoUri) {
                LogInfo.log("geny", "uri===" + uri);
                if (!uri.startsWith("http")) {
                    fileMap.put(String.valueOf(uri.hashCode()), new File(uri));
                } else {
                    httpUrl.add(uri);
                }
            }
        } else {
            subjectiveQIndex++;
            handleUploadSubjectiveImage();
            return;
        }

        if (fileMap == null || fileMap.size() == 0) {
            subjectiveQIndex++;
            entity.getAnswerBean().setSubjectivImageUri(httpUrl);
            handleUploadSubjectiveImage();
            return;
        }
        //((AnswerViewActivity)this.getActivity()).showCommonDialog();
//        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        YanxiuHttpApi.requestUploadImage(fileMap, new YanxiuHttpApi.UploadFileListener() {

            @Override
            public void onFail(final YanxiuBaseBean bean) {
                Message message=Message.obtain();
                message.obj=bean;
                message.what=2;
                myhandle.sendMessage(message);
            }

            @Override
            public void onSuccess(YanxiuBaseBean bean) {

                UploadImageBean uploadImageBean = (UploadImageBean) bean;
                if (uploadImageBean.getData() != null) {
                    subjectiveQIndex++;
                    ArrayList<String> uploadBean = (ArrayList<String>) uploadImageBean.getData();
                    uploadBean.addAll(httpUrl);
                    entity.getAnswerBean().setSubjectivImageUri(uploadBean);
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


    private class Myhandle extends Handler{

        public Myhandle(){

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    YanxiuBaseBean result= (YanxiuBaseBean) msg.obj;
                    submit_updata(result);
                    break;
                case 1:
                    String message= (String) msg.obj;
                    submit_dataerror(message);
                    break;
                case 2:
                    YanxiuBaseBean bean= (YanxiuBaseBean) msg.obj;
                    submit_image_fail(bean);
                    break;
            }
        }
    }

    Myhandle myhandle=new Myhandle();

    private void submit_updata(YanxiuBaseBean result){
        final long groupStartTime = dataSources.getData().get(0).getBegintime();
        final long groupEndtime = dataSources.getData().get(0).getEndtime();//作业练习截止时间

        ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).hideDialog();
        DataStatusEntityBean bean = (DataStatusEntityBean) result;
        if (bean.getCode() == 0) {

            int showana = dataSources.getShowana();
            if (showana == GroupHwActivity.NOT_FINISH_STATUS) {
                LogInfo.log("haitian", "comeFrom=" + comeFrom);
                if (comeFrom == AnswerViewActivity.GROUP && groupEndtime > groupStartTime &&
                        ((groupEndtime - System.currentTimeMillis()) >= 3 * 60 * 1000)) {//作业截止时间判断，还未到截止时间不产生作业报告
                    Util.showToast(R.string.update_sucess);
                    EventBus.getDefault().post(new ThridExamiEvent(true));
                    EventBus.getDefault().post(new GroupEventHWRefresh());
                    if (getActivity()==null){
                        return;
                    }
                    ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).addFinishFragment(dataSources, YanXiuConstant.END_TIME);
                } else {
                    jumpReport();
                }

            } else if (showana == GroupHwActivity.HAS_FINISH_CHECK_REPORT) {
                jumpReport();
            } else {
                Util.showToast(R.string.update_sucess);

                EventBus.getDefault().post(new ThridExamiEvent(true));
                EventBus.getDefault().post(new GroupEventHWRefresh());
                if (getActivity()==null){
                    return;
                }
                getActivity().finish();
            }

            if (comeFrom == YanXiuConstant.HISTORY_REPORT) {
                EventBus.getDefault().post(new ExHistoryEventBus());
            }
            ArrayList<StatisticHashMap> arrayList = new ArrayList<StatisticHashMap>();
            for (int i = 0; i < dataSources.getData().size(); i++) {
                StatisticHashMap statisticHashMap = new StatisticHashMap();
                statisticHashMap.put(YanXiuConstant.eventID, "20:event_3");//3:提交练习/作业
                HashMap reserveHashMap = new HashMap();

                reserveHashMap.put(YanXiuConstant.editionID, dataSources.getData().get(i).getBedition());
                reserveHashMap.put(YanXiuConstant.gradeID, String.valueOf(dataSources.getData().get(i).getGradeid()));
                reserveHashMap.put(YanXiuConstant.subjectID, String.valueOf(dataSources.getData().get(i).getSubjectid()));
                reserveHashMap.put(YanXiuConstant.paperType, String.valueOf(comeFrom));
                reserveHashMap.put(YanXiuConstant.quesNum, String.valueOf(dataSources.getData().get(i).getQuesnum()));
                String questionId = "[";
                for (int j = 0; j < dataSources.getData().get(i).getPaperTest().size(); j++) {
                    questionId = questionId + "\"" + dataSources.getData().get(i).getPaperTest().get(j).getQid() + "\"" + ",";
                }
                questionId = questionId.substring(0, questionId.lastIndexOf(",")) + "]";
                reserveHashMap.put(YanXiuConstant.qID, questionId);
                statisticHashMap.put(YanXiuConstant.reserved, Util.hashMapToJsonTwo(reserveHashMap));
                arrayList.add(statisticHashMap);
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

    private void submit_dataerror(String msg){
        if (TextUtils.isEmpty(msg)) {
            //Util.showToast(R.string.server_connection_erro);
        } else {
            //Util.showToast(msg);
        }
        submitNetErrorDialog();
        if (getActivity()==null){
            return;
        }
        ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).hideDialog();
    }

    private void requestSubmmit() {
        if (getActivity()==null){
            return;
        }
        ((AnswerViewActivity) this.getActivity()).showCommonDialog();
        if (requestSubmitQuesitonTask != null && requestSubmitQuesitonTask.isCancelled()) {
            requestSubmitQuesitonTask.cancel();
        }
        if (AnswerCardFragment.this.getActivity() instanceof AnswerViewActivity) {
            ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).calculateLastQuestionTime();
            ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).setQuestionCostTime();
        }
        long endtime = System.currentTimeMillis();
        dataSources.setEndtime(endtime);
        dataSources.getData().get(0).getPaperStatus().setCosttime(AnswerViewActivity.totalTime);
        requestSubmitQuesitonTask = new RequestSubmitQuesitonTask(YanxiuApplication.getContext(), dataSources, RequestSubmitQuesitonTask.SUBMIT_CODE, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                Message message=Message.obtain();
                message.what=0;
                message.obj=result;
                myhandle.sendMessage(message);
            }


            @Override
            public void dataError(int type, String msg) {
                Message message=Message.obtain();
                message.what=1;
                message.obj=msg;
                myhandle.sendMessage(message);
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
        if (getActivity()==null){
            return;
        }
        DataStatisticsUploadManager.getInstance().NormalUpLoadData(getActivity(), submitQuestionHashMap);
    }

    private void jumpReport() {
        requestReport();
        EventBus.getDefault().post(new ThridExamiEvent(true));
        EventBus.getDefault().post(new GroupEventHWRefresh());
    }


    private void requestReport() {
        if (getActivity()==null){
            return;
        }
        ((AnswerViewActivity) this.getActivity()).showCommonDialog();
        if (requestGetQReportTask != null && requestGetQReportTask.isCancelled()) {
            requestGetQReportTask.cancel();
        }
        LogInfo.log("geny", "requestReport ppid = " + String.valueOf(dataSources.getData().get(0).getId()));
        requestGetQReportTask = new RequestGetQReportTask(YanxiuApplication.getContext(), String.valueOf(dataSources.getData().get(0).getId()), new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                if (getActivity()==null){
                    return;
                }
                ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).hideDialog();
                SubjectExercisesItemBean bean = (SubjectExercisesItemBean) result;
                if (bean.getData() != null && bean.getStatus() != null && bean.getStatus().getCode() == 0) {
                    LogInfo.log("geny", "-------------------->RequestGetQReportTask-------dataSources===" + bean.getData().toString());

                    QuestionUtils.initDataWithAnswer(bean);
                    long endtime = System.currentTimeMillis();
                    bean.setEndtime(endtime);
                    bean.setBegintime(dataSources.getBegintime());

                    LogInfo.log("geny", "RequestGetQReportTask comeFrome------" + comeFrom);
                    ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).addFinishFragment(bean, comeFrom);
                } else {
                    if (bean != null && bean.getStatus() != null && bean.getStatus().getDesc() != null) {
                        Util.showToast(bean.getStatus().getDesc());
                    } else {
                        Util.showToast(R.string.server_connection_erro);
                    }
                }
            }

            @Override
            public void dataError(int type, String msg) {
                if (TextUtils.isEmpty(msg)) {
                    Util.showToast(R.string.server_connection_erro);
                } else {
                    Util.showToast(msg);
                }
                if (getActivity()==null){
                    return;
                }
                ((AnswerViewActivity) AnswerCardFragment.this.getActivity()).hideDialog();
            }
        });
        requestGetQReportTask.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (requestSubmitQuesitonTask != null && requestSubmitQuesitonTask.isCancelled()) {
            requestSubmitQuesitonTask.cancel();
        }
    }


    private class AnswerCardAdapter extends YXiuCustomerBaseAdapter<QuestionEntity> {
        private ViewHolder holder;

        public AnswerCardAdapter(Activity context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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
            if (data.getChildPositionForCard() == -1) {             //等于-1表示不是复合题类型的解答题(只有是复合题且是解答题的时候才需要显示小题号)
                holder.tvIndex.setText((data.getPositionForCard() + 1) + "");
            } else {
                holder.tvIndex.setText((data.getPositionForCard() + 1) + "-" + (data.getChildPositionForCard() + 1));
            }
            return row;
        }

        private class ViewHolder {
            ImageView ivIcon;
            TextView tvIndex;
        }
    }

    private CommonDialog saveNetErrorDialog;

    public void saveNetErrorDialog() {
        if (getActivity()==null){
            return;
        }
        saveNetErrorDialog = new CommonDialog(getActivity(), getActivity().getResources().getString(R.string.question_save_network_error),
                getActivity().getResources().getString(R.string.try_again),
                getActivity().getResources().getString(R.string.question_cancel),
                new DelDialog.DelCallBack() {
                    @Override
                    public void del() {
                        handleUploadSubjectiveImage();
                    }

                    @Override
                    public void sure() {
                        handleUploadSubjectiveImage();
                    }

                    @Override
                    public void cancel() {
                        saveNetErrorDialog.dismiss();
                    }
                });
        saveNetErrorDialog.show();
    }

    private CommonDialog submitNetErrorDialog;

    public void submitNetErrorDialog() {
        if (getActivity()==null){
            return;
        }
        submitNetErrorDialog = new CommonDialog(getActivity(), getActivity().getResources().getString(R.string.question_submit_network_error),
                getActivity().getResources().getString(R.string.try_again),
                getActivity().getResources().getString(R.string.question_cancel),
                new DelDialog.DelCallBack() {
                    @Override
                    public void del() {
                        handleUploadSubjectiveImage();
                    }

                    @Override
                    public void sure() {
                        handleUploadSubjectiveImage();
                    }

                    @Override
                    public void cancel() {
                        submitNetErrorDialog.dismiss();
                    }
                });
        submitNetErrorDialog.show();
    }


}
