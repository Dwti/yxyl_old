package com.yanxiu.gphone.hd.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.NetWorkTypeUtils;
import com.common.core.utils.StringUtils;
import com.common.core.view.xlistview.XListView;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.adapter.GroupHwListAdapter;
import com.yanxiu.gphone.hd.student.bean.GroupHwBean;
import com.yanxiu.gphone.hd.student.bean.GroupHwListBean;
import com.yanxiu.gphone.hd.student.bean.PageBean;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
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

/**
 * Created by Administrator on 2015/7/10.
 */
public class GroupHwActivity extends TopViewBaseActivity {

    public final static int LAUNCHER_GROUP_HW = 0x03;

    public static void launchActivity(Activity activity, int groupId, String groupName){
        Intent intent = new Intent(activity,GroupHwActivity.class);
        intent.putExtra("groupId",groupId);
        intent.putExtra("groupName",groupName);
        activity.startActivity(intent);
    }
    private TextView groupView;
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

    @Override
    protected void initLaunchIntentData () {
        Intent intent = getIntent();
        if(intent != null){
            classId = intent.getIntExtra("classId",0);
            groupId = intent.getIntExtra("groupId",0);
            groupName = intent.getStringExtra("groupName");
        }
    }

    @Override
    protected boolean isAttach () {
        return false;
    }

    @Override
    protected View getContentView () {
        mPublicLayout = PublicLoadUtils.createPage(this, R.layout.group_homework_list_layout);
        mPublicLayout.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData () {
                pageIndex = 1;
                requestHwList(false, true, false);
            }
        });
        findView();
        requestHwList(false, true, false);
        return mPublicLayout;
    }

    @Override
    protected void setContentListener () {
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                cancelTaskAndFinish();
            }
        });
    }

    @Override
    protected void destoryData () {
        cancelTaskAndFinish();
    }

    private void findView(){
        noCommentView = (RelativeLayout)mPublicLayout.findViewById(R.id.no_group_hw_list);
        listView = (XListView)mPublicLayout.findViewById(R.id.group_hw_list);
        listView.setScrollable(false);
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(ixListViewListener);
        groupHwListAdapter = new GroupHwListAdapter(this);
        listView.setAdapter(groupHwListAdapter);
        titleText.setText(groupName);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view,
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

    private XListView.IXListViewListener ixListViewListener = new XListView.IXListViewListener(){
        @Override public void onRefresh(XListView view) {
            if(!NetWorkTypeUtils.isNetAvailable()){
                pageIndex = 1;
                requestHwList(true, false, false);
            }else {
                listView.stopRefresh();
                Util.showUserToast(R.string.net_null, -1, -1);
            }
        }
        @Override public void onLoadMore(XListView view) {
            if(!NetWorkTypeUtils.isNetAvailable()){
                requestHwList(false, false, true);
            }else {
                listView.stopLoadMore();
                Util.showUserToast(R.string.net_null, -1, -1);
            }
        }
    };

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
        requestGroupHwListTask = new RequestGroupHwListTask(this,groupId, page, PAGESIZE, new AsyncCallBack() {
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

    private void forResult(){
        Intent intent = new Intent();
        boolean resultToRefresh = true;
        if(groupHwListAdapter == null || groupHwListAdapter.getCount() <= 0) {
            resultToRefresh = false;
        }
        intent.putExtra("toRefresh", resultToRefresh);
        setResult(RESULT_OK, intent);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if(resultCode == RESULT_OK && requestCode == AnswerViewActivity.LAUNCHER_FROM_GROUP){
            requestHwList(true, false, false);
        }
    }
    /**
     * 更新列表UI
     * */
    private void updateUI(){
        mPublicLayout.finish();
//        noCommentView.setVisibility(View.GONE);
        listView.setScrollable(true);
        listView.setPullRefreshEnable(true);
        if(groupHwListAdapter!=null){
            groupHwListAdapter.setList(dataList);
        }
    }

    /**
     * 请求作业群组试题列表接口
     * */
    private void requestQuestionList(String paperId,final int showana, final int status){
        if(requestQuestionListTask == null || requestQuestionListTask.isCancelled()){
            mPublicLayout.loading(true);
            if (requestQuestionListTask != null) {
                requestQuestionListTask.cancel();
                requestQuestionListTask = null;
            }
            requestQuestionListTask = new RequestQuestionListTask(this, paperId, new AsyncCallBack() {
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
                            ResolutionAnswerViewActivity.launch(GroupHwActivity.this,
                                    subjectExercisesItemBean, YanXiuConstant.HOMEWORK_REPORT);
                        }else if(status == 0){ //待完成  可以做题
                            QuestionUtils.initDataWithAnswer(subjectExercisesItemBean);
                            AnswerViewActivity.launchForResult(GroupHwActivity.this,
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
            requestGetQReportTask = new RequestGetQReportTask(this, paperId, new AsyncCallBack() {
                @Override public void update(YanxiuBaseBean result) {
                    mPublicLayout.finish();
                    SubjectExercisesItemBean subjectExercisesItemBean = (SubjectExercisesItemBean)result;
                    QuestionUtils.initDataWithAnswer(subjectExercisesItemBean);
                    if(subjectExercisesItemBean!=null && subjectExercisesItemBean.getData()!=null
                            && subjectExercisesItemBean.getData().size()>0 && subjectExercisesItemBean.getData().get(0).getPaperTest()!=null){
                        if (showana == 0){ //已完成  可以查看答题报告
                            subjectExercisesItemBean.setIsResolution(true);
                            QuestionUtils.initDataWithAnswer(subjectExercisesItemBean);
                            AnswerReportActivity.launch(GroupHwActivity.this, subjectExercisesItemBean,YanXiuConstant.HOMEWORK_REPORT,Intent.FLAG_ACTIVITY_FORWARD_RESULT, true);
                        }else{
                            String toast1 = GroupHwActivity.this.getResources().getString(R.string.group_hw_done_not_cat_ana,endTime);
                            String toast2 = GroupHwActivity.this.getResources().getString(R.string.group_hw_done_not_cat_ana_2);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cancelTaskAndFinish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    private void cancelTaskAndFinish(){
        cancelTask();
        forResult();
        GroupHwActivity.this.finish();
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
}
