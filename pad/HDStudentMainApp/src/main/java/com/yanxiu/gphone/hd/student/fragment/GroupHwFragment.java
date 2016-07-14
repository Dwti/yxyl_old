package com.yanxiu.gphone.hd.student.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.core.utils.StringUtils;
import com.common.core.view.xlistview.XListView;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.AnswerReportActivity;
import com.yanxiu.gphone.hd.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.hd.student.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.hd.student.adapter.GroupHwListAdapter;
import com.yanxiu.gphone.hd.student.bean.GroupEventHWRefresh;
import com.yanxiu.gphone.hd.student.bean.GroupEventRefresh;
import com.yanxiu.gphone.hd.student.bean.GroupHwBean;
import com.yanxiu.gphone.hd.student.bean.GroupHwListBean;
import com.yanxiu.gphone.hd.student.bean.PageBean;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestGetQReportTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestGroupHwListTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestQuestionListTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.QuestionUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作业
 * Created by Administrator on 2015/7/7.
 */
public class GroupHwFragment extends TopBaseFragment {

    public final static int NOT_FINISH_STATUS=1;//未完成 不可补做  查看解析报告
    public final static int HAS_FINISH_CHECK_REPORT=0;   //已完成  可以查看答题报告

    private XListView listView;
    private RelativeLayout noCommentView;

    private RequestGroupHwListTask requestGroupHwListTask;
    private RequestQuestionListTask requestQuestionListTask;
    private RequestGetQReportTask requestGetQReportTask;
    private int pageIndex = 1;
    private final static int PAGESIZE = 20;
    private int classId;
    private int groupId;
    private String groupName;

    private GroupHwListAdapter groupHwListAdapter;
    private List<GroupHwBean> dataList = new ArrayList<GroupHwBean>();
    private GroupInfoContainerFragment fg;

    public static Fragment newInstance (int classId,int groupId,String groupName) {
        GroupHwFragment mGroupHwFragment = new GroupHwFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("classId", classId);
        bundle.putInt("groupId", groupId);
        bundle.putString("groupName", groupName);
        mGroupHwFragment.setArguments(bundle);
        return mGroupHwFragment;
    }
    public static Fragment newInstance (int groupId,String groupName) {
        GroupHwFragment mGroupHwFragment = new GroupHwFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("groupId", groupId);
        bundle.putString("groupName", groupName);
        mGroupHwFragment.setArguments(bundle);
        return mGroupHwFragment;
    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requestHwList(false, true, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected boolean isAttach () {
        return false;
    }

    @Override
    protected View getContentView () {
        EventBus.getDefault().register(this);
        fg = (GroupInfoContainerFragment) getParentFragment();
        classId = getArguments().getInt("classId", 0);
        groupId = getArguments().getInt("groupId", 0);
        groupName = getArguments().getString("groupName");
        mPublicLayout = PublicLoadUtils.createPage(getActivity(), R.layout.group_homework_list_layout);
        mPublicLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.trans));
        mPublicLayout.setContentBackground(android.R.color.transparent);
        mPublicLayout.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData () {
                pageIndex = 1;
                requestHwList(false, true, false);
            }
        });
        findView();
        return mPublicLayout;
    }
    public void onEventMainThread(GroupEventHWRefresh event) {
        pageIndex = 1;
        EventBus.getDefault().post(new GroupEventRefresh());
        requestHwList(true, false, false);
    }
    @Override
    public void onDestroy () {
        super.onDestroy();
        listView = null;
        noCommentView = null;
        groupHwListAdapter = null;
        if(dataList != null){
            dataList.clear();
        }
        dataList = null;
        fg = null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void setTopView () {
        super.setTopView();
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finish();
            }
        });
    }

    @Override
    protected void initLoadData() {

    }

    @Override
    protected void setContentListener () {

    }

    @Override
    protected void destoryData () {
        cancelTask();
        finish();
    }
    private void cancelTask(){
        if (requestGroupHwListTask != null) {
            requestGroupHwListTask.cancel();
            requestGroupHwListTask = null;
        }
        if (requestQuestionListTask != null) {
            requestQuestionListTask.cancel();
            requestQuestionListTask = null;
        }
        if (requestGetQReportTask != null) {
            requestGetQReportTask.cancel();
            requestGetQReportTask = null;
        }
    }
    private void finish () {
        cancelTask();
        fg.mIFgManager.popStack();
    }

    @Override
    protected IFgManager getFragmentManagerFromSubClass () {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass () {
        return 0;
    }

    private void findView () {
        titleText.setText(groupName);
        noCommentView = (RelativeLayout)mPublicLayout.findViewById(R.id.no_group_hw_list);
        listView = (XListView)mPublicLayout.findViewById(R.id.group_hw_list);
        listView.setScrollable(false);
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(ixListViewListener);
        groupHwListAdapter = new GroupHwListAdapter(getActivity());
        listView.setAdapter(groupHwListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view,
                                     int position, long id) {
                if (groupHwListAdapter != null) {
                    List<GroupHwBean> list = groupHwListAdapter.getList();
                    if (list != null && list.size() > 0) {
                        GroupHwBean entity = list
                                .get(position - listView.getHeaderViewsCount());
                        if (entity != null && entity.getPaperStatus() != null) {
                            if (entity.getPaperStatus().getStatus()
                                    == 2) {  //已完成
                                requestQReportList(entity.getId() + "",
                                        entity.getShowana(),
                                        entity.getOverTime());
                            } else {  //未完成  待完成
                                requestQuestionList(entity.getId() + "",
                                        entity.getShowana(),
                                        entity.getPaperStatus().getStatus());
                            }
                        }
                    }
                }
            }
        });
    }

    private XListView.IXListViewListener ixListViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh (XListView view) {
            if (!NetWorkTypeUtils.isNetAvailable()) {
                LogInfo.log("king", "onRefresh");
                pageIndex = 1;
                requestHwList(true, false, false);
            } else {
                listView.stopRefresh();
                Util.showUserToast(R.string.net_null, -1, -1);
            }
        }

        @Override
        public void onLoadMore (XListView view) {
            if(!NetWorkTypeUtils.isNetAvailable()){
                requestHwList(false, false, true);
            }else {
                listView.stopLoadMore();
                Util.showUserToast(R.string.net_null, -1, -1);
            }
        }
    };
    /**
     * 更新列表UI
     * */
    private void updateUI(){
        mPublicLayout.finish();
        listView.setScrollable(true);
        listView.setPullRefreshEnable(true);
        if(groupHwListAdapter!=null){
            groupHwListAdapter.setList(dataList);
        }
    }
    private void requestHwList(final boolean isRefresh,final boolean showLoading,
                               final boolean isLoaderMore){
        if(showLoading){
            mPublicLayout.loading(true);
        }
        int page = pageIndex;
        if(isLoaderMore){
            page +=1;
        }
        if (requestGroupHwListTask != null) {
            requestGroupHwListTask.cancel();
            requestGroupHwListTask = null;
        }
        requestGroupHwListTask = new RequestGroupHwListTask(getActivity(),groupId, page, PAGESIZE, new
                AsyncCallBack() {
            @Override public void update(YanxiuBaseBean result) {
                mPublicLayout.finish();
                listView.stopRefresh();
                listView.stopLoadMore();
                GroupHwListBean groupHwListBean = (GroupHwListBean)result;
                ArrayList<GroupHwBean> data = groupHwListBean.getData();
                if(data!=null && data.size()>0) {
                    if(isLoaderMore){
                        pageIndex +=1;
                    }else if(isRefresh){
                        pageIndex = 1;
                        dataList.clear();
                    }
                    dataList.addAll(data);
                    PageBean pageBean = groupHwListBean.getPage();
                    if(pageBean!=null){
                        if(pageIndex == pageBean.getTotalPage()){
                            listView.setPullLoadEnable(false);
                        }else{
                            listView.setPullLoadEnable(true);
                        }
                    }else{
                        listView.setPullLoadEnable(false);
                    }
                    updateUI();
                }else{
                    mPublicLayout.dataNull(getResources().getString(R.string.no_group_hw_list_tip));
                }
            }

            @Override public void dataError(int type, String msg) {
                mPublicLayout.finish();
                listView.stopRefresh();
                listView.stopLoadMore();
                if(isRefresh || isLoaderMore){
                    if(!StringUtils.isEmpty(msg)){
                        Util.showUserToast(msg, null, null);
                    } else{
                        Util.showUserToast(R.string.net_null_one,-1,-1);
                    }
                }else if(showLoading){
                    mPublicLayout.netError(true);
                }
            }
        });
        requestGroupHwListTask.start();
    }
    /**
     * 请求作业群组试题列表接口
     * */
    private void requestQuestionList(String paperId,final  int showana,final int status){
        if(requestQuestionListTask == null || requestQuestionListTask.isCancelled()){
            mPublicLayout.loading(true);
            if (requestQuestionListTask != null) {
                requestQuestionListTask.cancel();
                requestQuestionListTask = null;
            }
            requestQuestionListTask = new RequestQuestionListTask(getActivity(), paperId, new AsyncCallBack
                    () {
                @Override public void update(YanxiuBaseBean result) {
                    mPublicLayout.finish();
                    SubjectExercisesItemBean subjectExercisesItemBean = (SubjectExercisesItemBean)result;
                    if(subjectExercisesItemBean!=null && subjectExercisesItemBean.getData()!=null
                            && subjectExercisesItemBean.getData().size()>0 && subjectExercisesItemBean.getData().get(0).getPaperTest()!=null){
                        subjectExercisesItemBean.setShowana(showana);
                        if(status == 1){ //未完成 不可补做  查看解析报告
                            subjectExercisesItemBean.setIsResolution(true);
                            QuestionUtils.initDataWithAnswer(
                                    subjectExercisesItemBean);
                            ResolutionAnswerViewActivity.launch(getActivity(),
                                    subjectExercisesItemBean, YanXiuConstant.HOMEWORK_REPORT);
                        }else if(status == 0){ //待完成  可以做题
                            QuestionUtils.initDataWithAnswer(subjectExercisesItemBean);
                            AnswerViewActivity.launchForResult(getActivity(),
                                    subjectExercisesItemBean, AnswerViewActivity.GROUP);
                        }
                    }else{
                        if(subjectExercisesItemBean.getStatus() == null){
                            Util.showUserToast(R.string.net_null, -1, -1);
                        }else{
                            Util.showUserToast(subjectExercisesItemBean.getStatus().getDesc(),null,null);
                        }
                    }
                }

                @Override public void dataError(int type, String msg) {
                    mPublicLayout.finish();
                    if(!StringUtils.isEmpty(msg)){
                        Util.showUserToast(msg,null,null);
                    }else{
                        Util.showUserToast(R.string.net_null,-1,-1);
                    }
                }
            });
            requestQuestionListTask.start();
        }
    }
    /**
     * 请求作业群组试题列表接口
     * */
    private void requestQReportList(String paperId,final  int showana,final String endTime){
        if(requestGetQReportTask == null || requestGetQReportTask.isCancelled()){
            mPublicLayout.loading(true);
            if (requestGetQReportTask != null) {
                requestGetQReportTask.cancel();
                requestGetQReportTask = null;
            }
            requestGetQReportTask = new RequestGetQReportTask(getActivity(), paperId, new AsyncCallBack() {
                @Override public void update(YanxiuBaseBean result) {
                    mPublicLayout.finish();
                    SubjectExercisesItemBean subjectExercisesItemBean = (SubjectExercisesItemBean)result;
                    QuestionUtils.initDataWithAnswer(subjectExercisesItemBean);
                    if(subjectExercisesItemBean!=null && subjectExercisesItemBean.getData()!=null
                            && subjectExercisesItemBean.getData().size()>0 && subjectExercisesItemBean.getData().get(0).getPaperTest()!=null){
                        subjectExercisesItemBean.setShowana(showana);
                        if (showana == 0){ //已完成  可以查看答题报告
                            subjectExercisesItemBean.setIsResolution(true);
                            QuestionUtils.initDataWithAnswer(subjectExercisesItemBean);
                            AnswerReportActivity.launch(getActivity(), subjectExercisesItemBean,
                                    YanXiuConstant.HOMEWORK_REPORT, Intent.FLAG_ACTIVITY_FORWARD_RESULT, true);
                        }else{
                            String toast1 = getActivity().getResources().getString(R.string
                                    .group_hw_done_not_cat_ana,endTime);
                            String toast2 = getActivity().getResources().getString(R.string
                                    .group_hw_done_not_cat_ana_2);
                            Util.showUserToast(toast1,toast2,null);
                        }
                    }else{
                        if(subjectExercisesItemBean.getStatus() == null){
                            Util.showUserToast(R.string.net_null, -1, -1);
                        }else{
                            Util.showUserToast(subjectExercisesItemBean.getStatus().getDesc(),null,null);
                        }
                    }
                }

                @Override public void dataError(int type, String msg) {
                    mPublicLayout.finish();
                    if(!StringUtils.isEmpty(msg)){
                        Util.showUserToast(msg,null,null);
                    }else{
                        Util.showUserToast(R.string.net_null,-1,-1);
                    }
                }
            });
            requestGetQReportTask.start();
        }
    }

    @Override
    public void onReset() {
        destoryData();
    }
}
