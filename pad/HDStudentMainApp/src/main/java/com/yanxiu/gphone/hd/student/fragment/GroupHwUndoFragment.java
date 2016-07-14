package com.yanxiu.gphone.hd.student.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.common.core.utils.NetWorkTypeUtils;
import com.common.core.utils.StringUtils;
import com.common.core.view.xlistview.XListView;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.hd.student.adapter.GroupHwUndoListAdapter;
import com.yanxiu.gphone.hd.student.bean.GroupEventHWUndoRefresh;
import com.yanxiu.gphone.hd.student.bean.GroupHwUnBean;
import com.yanxiu.gphone.hd.student.bean.GroupHwUndoListBean;
import com.yanxiu.gphone.hd.student.bean.PageBean;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestGroupHwUndoListTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestQuestionListTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.QuestionUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作业
 * Created by Administrator on 2015/7/7.
 */
public class GroupHwUndoFragment extends TopBaseFragment {
    private XListView listView;

    private RequestGroupHwUndoListTask requestGroupHwUndoListTask;
    private RequestQuestionListTask requestQuestionListTask;

    private int pageIndex = 1;
    private final static int PAGESIZE = 20;

    private GroupHwUndoListAdapter groupHwUndoListAdapter;
    private ArrayList<GroupHwUnBean> dataList = new ArrayList<GroupHwUnBean>();

    private GroupInfoContainerFragment fg;

    public static Fragment newInstance () {
        GroupHwUndoFragment mGroupHwUndoFragment = new GroupHwUndoFragment();
        return mGroupHwUndoFragment;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requestHwUndoList(false, true, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected boolean isAttach () {
        return false;
    }

    @Override
    protected View getContentView () {
        fg = (GroupInfoContainerFragment) getParentFragment();
        mPublicLayout = PublicLoadUtils.createPage(getActivity(), R.layout.group_undo_list_layout);
        mPublicLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.trans));
        mPublicLayout.setContentBackground(android.R.color.transparent);
        mPublicLayout.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData () {
                pageIndex = 1;
                requestHwUndoList(false, true, false);
            }
        });
        findView();
        EventBus.getDefault().register(this);
        return mPublicLayout;
    }

    public void onEventMainThread (GroupEventHWUndoRefresh event) {
        pageIndex = 1;
        requestHwUndoList(false, true, false);
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        listView = null;
        groupHwUndoListAdapter = null;
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
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initLoadData () {

    }

    @Override
    protected void setContentListener () {

    }

    @Override
    protected void destoryData () {
        finish();
    }

    private void cancelTask () {
        if (requestGroupHwUndoListTask != null) {
            requestGroupHwUndoListTask.cancel();
            requestGroupHwUndoListTask = null;
        }
        if (requestQuestionListTask != null) {
            requestQuestionListTask.cancel();
            requestQuestionListTask = null;
        }
    }

    private void finish () {
        cancelTask();
        if(fg!=null&&fg.mIFgManager!=null){
            fg.mIFgManager.popStack();
        }

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
        titleText.setText(R.string.hw_undo_title);
        listView = (XListView) mPublicLayout.findViewById(R.id.group_hw_undo_list);
        listView.setScrollable(false);
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(ixListViewListener);
        groupHwUndoListAdapter = new GroupHwUndoListAdapter(getActivity());
        listView.setAdapter(groupHwUndoListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view,
                                     int position, long id) {
                if (groupHwUndoListAdapter != null) {
                    List<GroupHwUnBean> list = groupHwUndoListAdapter.getList();
                    if (list != null && list.size() > 0) {
                        GroupHwUnBean entity = list
                                .get(position - listView.getHeaderViewsCount());
                        if (entity != null) {
                            requestQuestionList(entity.getId() + "");
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
                pageIndex = 1;
                requestHwUndoList(true, false, false);
            } else {
                listView.stopRefresh();
                Util.showUserToast(R.string.net_null, -1, -1);
            }
        }

        @Override
        public void onLoadMore (XListView view) {
            if (!NetWorkTypeUtils.isNetAvailable()) {
                requestHwUndoList(false, false, true);
            } else {
                listView.stopLoadMore();
                Util.showUserToast(R.string.net_null, -1, -1);
            }
        }
    };

    /**
     * 请求作业群组试题列表接口
     */
    private void requestQuestionList (String paperId) {
        if (requestQuestionListTask == null || requestQuestionListTask.isCancelled()) {
            mPublicLayout.loading(true);
            requestQuestionListTask = new RequestQuestionListTask(getActivity(), paperId,
                    new AsyncCallBack() {
                        @Override
                        public void update (YanxiuBaseBean result) {
                            mPublicLayout.finish();
                            SubjectExercisesItemBean subjectExercisesItemBean = (SubjectExercisesItemBean) result;
                            if (subjectExercisesItemBean != null && subjectExercisesItemBean.getData()
                                    != null
                                    && subjectExercisesItemBean.getData().size()
                                    > 0 &&
                                    subjectExercisesItemBean.getData().get(0)
                                            .getPaperTest() != null) {
                                QuestionUtils.initDataWithAnswer(
                                        subjectExercisesItemBean);
                                AnswerViewActivity.launchForResult(
                                        getActivity(),
                                        subjectExercisesItemBean, AnswerViewActivity.GROUP);
                            } else {
                                if (subjectExercisesItemBean.getStatus()
                                        == null) {
                                    Util.showUserToast(R.string.net_null, -1, -1);
                                } else {
                                    Util.showUserToast(subjectExercisesItemBean.getStatus()
                                            .getDesc(), null, null);
                                }
                            }
                        }

                        @Override
                        public void dataError (int type, String msg) {
                            mPublicLayout.finish();
                            if (!StringUtils.isEmpty(msg)) {
                                Util.showUserToast(msg, null, null);
                            } else {
                                Util.showUserToast(R.string.net_null, -1, -1);
                            }
                        }
                    });
            requestQuestionListTask.start();
        }
    }

    private void requestHwUndoList (final boolean isRefresh, final boolean showLoading,
                                    final boolean isLoaderMore) {
        if (showLoading) {
            mPublicLayout.loading(true);
        }
        int oage = pageIndex;
        if (isLoaderMore) {
            oage += 1;
        }
        requestGroupHwUndoListTask = new RequestGroupHwUndoListTask(getActivity(), oage, PAGESIZE, new
                AsyncCallBack() {
                    @Override
                    public void update (YanxiuBaseBean result) {
                        mPublicLayout.finish();
                        listView.stopRefresh();
                        listView.stopLoadMore();
                        GroupHwUndoListBean groupHwUndoListBean = (GroupHwUndoListBean) result;
                        ArrayList<GroupHwUnBean> list = groupHwUndoListBean.getData();
                        if (list != null && list.size() > 0) {
                            if (isLoaderMore) {
                                pageIndex += 1;
                            } else if (isRefresh) {
                                pageIndex = 1;
                                dataList.clear();
                            }
                            dataList.addAll(list);
                            PageBean pageBean = groupHwUndoListBean.getPage();
                            if (pageBean != null) {
                                if (pageIndex == pageBean.getTotalPage()) {
                                    listView.setPullLoadEnable(false);
                                } else {
                                    listView.setPullLoadEnable(true);
                                }
                            } else {
                                listView.setPullLoadEnable(false);
                            }
                            updateUI();
                        } else {
                            mPublicLayout.netError(true);
                        }
                    }

                    @Override
                    public void dataError (int type, String msg) {
                        mPublicLayout.finish();
                        listView.stopRefresh();
                        listView.stopLoadMore();
                        if (isRefresh || isLoaderMore) {
                            if (!StringUtils.isEmpty(msg)) {
                                Util.showUserToast(msg, null, null);
                            } else {
                                Util.showUserToast(R.string.net_null_one, -1, -1);
                            }
                        } else if (showLoading) {
                            mPublicLayout.netError(true);
                        }
                    }
                });
        requestGroupHwUndoListTask.start();
    }

    /**
     * 更新列表UI
     */
    private void updateUI () {
        mPublicLayout.finish();
        listView.setScrollable(true);
        listView.setPullRefreshEnable(true);
        if (groupHwUndoListAdapter != null) {
            groupHwUndoListAdapter.setList(dataList);
        }
    }

    @Override
    public void onReset () {
        destoryData();
    }
}
