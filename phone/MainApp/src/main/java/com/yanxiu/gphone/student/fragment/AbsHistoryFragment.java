package com.yanxiu.gphone.student.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.core.view.xlistview.XListView;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.AnswerReportActivity;
import com.yanxiu.gphone.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.student.adapter.PracticeHistoryAdapter;
import com.yanxiu.gphone.student.bean.PracticeHistoryBean;
import com.yanxiu.gphone.student.bean.PracticeHistoryChildBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.bean.YanxiuPageInfoBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestGetPaperQuestionTask;
import com.yanxiu.gphone.student.requestTask.RequestGetQReportTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

/**
 * Created by Administrator on 2015/11/3.
 */
public abstract class AbsHistoryFragment extends Fragment {

    protected PublicLoadLayout rootView;
//    protected StudentLoadingLayout loadingLayout;
    protected XListView xListView;
    protected PracticeHistoryAdapter adapter;
    protected View topRightView;
    protected TextView rightTxtView;
    protected ImageView rightImageView;

    protected long cacheTime = 0L;

    protected String title;
    protected String stageId;
    protected String subjectId;
    protected String editionId;
    protected String volumeId;
    protected String volumeName;

    public static final int CHAPTER_HISTORY = 0;
    public static final int TEST_CENTER_HISTORY = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.subjectId = (getArguments() != null) ? getArguments().getString("subjectId") : "";
        this.editionId = (getArguments() != null) ? getArguments().getString("editionId") : "";
        this.title = (getArguments() != null) ? getArguments().getString("title") : "";
        stageId = LoginModel.getUserinfoEntity().getStageid()+"";
//        volumeId = PreferencesManager.getInstance().getSubjectSection(stageId, subjectId, editionId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = PublicLoadUtils.createPage(this.getActivity(), R.layout.fragment_history);
        initView();
        initData();
        requestData();
        return rootView;
    }

    public void setRightView(View view,TextView rightTxtView,ImageView rightImageView){
        this.topRightView = view;
        this.rightTxtView = rightTxtView;
        this.rightImageView = rightImageView;
        topRightView.setVisibility(View.GONE);
    }

    protected void initView(){
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {
                requestData();
            }
        });
//        loadingLayout = (StudentLoadingLayout) rootView.findViewById(R.id.loading_layout);
        xListView = (XListView) rootView.findViewById(R.id.practice_history_pull_list);
        xListView.setVisibility(View.VISIBLE);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(false);
        xListView.setScrollable(true);
        xListView.setXListViewListener(XListViewRefreshListener);
        xListView.setCacheTime(cacheTime);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PracticeHistoryChildBean data = (PracticeHistoryChildBean) adapter.getItem(position - xListView.getHeaderViewsCount());
                if (data != null) {
                    if (data.getStatus() == 2) {
                        requestQReportTask(data.getPaperId() + "");
                    } else {
                        requestSubjectExercises(data.getPaperId() + "");
                    }
                }
            }
        });
    }

    /**
     * 下拉刷新控件的 动作监听 刷新、加载更多、更新当前页面
     */
    private XListView.IXListViewListener XListViewRefreshListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh(XListView view) {//刷新
            //更新当前的新闻列表
            if(NetWorkTypeUtils.isNetAvailable()){
                xListView.stopRefresh();
                xListView.stopLoadMore();
                Util.showToast(R.string.no_net_tag);
            } else {
                refreshData();
            }
        }
        @Override
        public void onLoadMore(XListView view) {//加载更多
            //加载下面20条
            if(NetWorkTypeUtils.isNetAvailable()){
                xListView.stopRefresh();
                xListView.stopLoadMore();
                Util.showToast(R.string.no_net_tag);
            } else {
                loadMoreData();
            }
        }

    };

    private void requestSubjectExercises(String paperId) {
        rootView.loading(true);
        new RequestGetPaperQuestionTask(getActivity(), paperId, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                SubjectExercisesItemBean subjectExercisesItemBean = (SubjectExercisesItemBean) result;
                if(subjectExercisesItemBean.getData() != null && subjectExercisesItemBean.getData().get(0).getPaperTest() != null && !subjectExercisesItemBean.getData().get(0).getPaperTest().isEmpty()){
                    if(subjectExercisesItemBean.getData() != null && !subjectExercisesItemBean.getData().isEmpty()){
                        subjectExercisesItemBean.getData().get(0).setStageid(stageId + "");
                        subjectExercisesItemBean.getData().get(0).setSubjectid(subjectId);
                        subjectExercisesItemBean.getData().get(0).setBedition(editionId);
//                        subjectExercisesItemBean.getData().get(0).setVolume(volumeId);
//                        subjectExercisesItemBean.getData().get(0).setVolumeName(volumeName);

                        //未完成  去答题
                        QuestionUtils.initDataWithAnswer(subjectExercisesItemBean);
                        AnswerViewActivity.launchForResult(getActivity(), subjectExercisesItemBean, YanXiuConstant.HISTORY_REPORT);
                    }
                }else{
                    if(subjectExercisesItemBean!= null && subjectExercisesItemBean.getStatus() != null){
                        if(!TextUtils.isEmpty(subjectExercisesItemBean.getStatus().getDesc())) {
                            Util.showToast(subjectExercisesItemBean.getStatus().getDesc());
                        } else {
                            Util.showToast(R.string.server_connection_erro);
                        }
                    } else {
                        Util.showToast(R.string.server_connection_erro);
                    }
                }

                rootView.finish();
            }

            @Override
            public void dataError(int type, String msg) {
                rootView.finish();
                if (TextUtils.isEmpty(msg)) {
                    Util.showToast(R.string.server_connection_erro);
                } else {
                    Util.showToast(msg);
                }
            }
        }).start();
    }

    private void requestQReportTask(String ppid) {
        rootView.loading(true);
        new RequestGetQReportTask(getActivity(), ppid, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                SubjectExercisesItemBean subjectExercisesItemBean = (SubjectExercisesItemBean) result;
                if(subjectExercisesItemBean.getData() != null && subjectExercisesItemBean.getData().get(0).getPaperTest() != null && !subjectExercisesItemBean.getData().get(0).getPaperTest().isEmpty()){
                    if(subjectExercisesItemBean.getData() != null && !subjectExercisesItemBean.getData().isEmpty()){
                        subjectExercisesItemBean.getData().get(0).setStageid(stageId
                                + "");
                        subjectExercisesItemBean.getData().get(0).setSubjectid(
                                subjectId);
                        subjectExercisesItemBean.getData().get(0).setBedition(
                                editionId);
//                        subjectExercisesItemBean.getData().get(0).setVolume(
//                                volumeId);
//                        subjectExercisesItemBean.getData().get(0).setVolumeName(
//                                volumeName);
                        QuestionUtils.initDataWithAnswer(subjectExercisesItemBean);
//                        int comeFrom = 0;
//                        if (subjectExercisesItemBean != null && subjectExercisesItemBean.getData() != null && !subjectExercisesItemBean.getData().isEmpty()) {
//                            int ptype = subjectExercisesItemBean.getData().get(0).getPtype();
//                            switch (ptype){
//                                case YanXiuConstant.INTELLI_REPORT:
//                                    comeFrom = YanXiuConstant.INTELLI_REPORT;
//                                    break;
//                                case YanXiuConstant.KPN_REPORT:
//                                    comeFrom = YanXiuConstant.KPN_REPORT;
//                                    break;
//                            }
//                        }
                        AnswerReportActivity.launch(getActivity(), subjectExercisesItemBean, YanXiuConstant.HISTORY_REPORT, Intent.FLAG_ACTIVITY_FORWARD_RESULT, true);
                    }
                }else{
                    if(subjectExercisesItemBean!= null && subjectExercisesItemBean.getStatus() != null){
                        if(!TextUtils.isEmpty(subjectExercisesItemBean.getStatus().getDesc())) {
                           Util.showToast(subjectExercisesItemBean.getStatus().getDesc());
                        } else {
                            Util.showToast(R.string.server_connection_erro);
                        }
                    } else {
                        Util.showToast(R.string.server_connection_erro);
                    }
                }

                rootView.finish();
            }

            @Override
            public void dataError(int type, String msg) {
                rootView.finish();
                if (TextUtils.isEmpty(msg)) {
                   Util.showToast(R.string.server_connection_erro);
                } else {
                   Util.showToast(msg);
                }
            }
        }).start();
    }

    protected void updateView(PracticeHistoryBean mPracticeHistoryBean){
        if(mPracticeHistoryBean != null && mPracticeHistoryBean.getData() != null){
            adapter = new PracticeHistoryAdapter(getActivity(), subjectId);
            adapter.setList(mPracticeHistoryBean.getData());
            xListView.setAdapter(adapter);
            YanxiuPageInfoBean mYanxiuPageInfoBean = mPracticeHistoryBean.getPage();
            if(mYanxiuPageInfoBean != null && adapter != null){
                if(mYanxiuPageInfoBean.getTotalCou() > adapter.getCount()){
                    xListView.setPullLoadEnable(true);
                } else {
                    xListView.setPullLoadEnable(false);
                }
            }
        }
    }

    protected void toLoadMore(PracticeHistoryBean mPracticeHistoryBean){
        if(mPracticeHistoryBean != null && mPracticeHistoryBean.getData() != null){
            if(adapter != null) {
                adapter.addMoreData(mPracticeHistoryBean.getData());
            }
            YanxiuPageInfoBean mYanxiuPageInfoBean = mPracticeHistoryBean.getPage();
            if(mYanxiuPageInfoBean != null && adapter != null){
                LogInfo.log("haitian", "pageToString =" + mYanxiuPageInfoBean.toString() + " listCount=" + adapter.getCount());
                if(mYanxiuPageInfoBean.getTotalCou() > adapter.getCount()){
                    xListView.setPullLoadEnable(true);
                    loadMoreOver(true);
                } else {
                    xListView.setPullLoadEnable(false);
                }
            }
        }
    }

    public void setRightViewVisible(boolean isVisible){
//        if(topRightView!=null){
//            if(isVisible){
//                topRightView.setVisibility(View.VISIBLE);
//            }else{
//                topRightView.setVisibility(View.GONE);
//            }
//        }
    }

    protected void initData(){

    }

    protected abstract void requestData();

    public abstract void refreshData();

    protected abstract void loadMoreData();

    protected abstract void refreshOver(boolean success);

    protected abstract void loadMoreOver(boolean success);
}
