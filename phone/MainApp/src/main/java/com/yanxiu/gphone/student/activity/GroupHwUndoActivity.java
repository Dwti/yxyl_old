package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.core.utils.StringUtils;
import com.common.core.view.xlistview.XListView;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.GroupHwUndoListAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.GroupEventHWRefresh;
import com.yanxiu.gphone.student.bean.GroupEventRefresh;
import com.yanxiu.gphone.student.bean.GroupHwUnBean;
import com.yanxiu.gphone.student.bean.GroupHwUndoListBean;
import com.yanxiu.gphone.student.bean.PageBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestGroupHwUndoListTask;
import com.yanxiu.gphone.student.requestTask.RequestQuestionListTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/7/10.
 */
public class GroupHwUndoActivity extends YanxiuBaseActivity {

    public final static int LAUNCHER_GROUP_HW_UNDO = 0x02;

    public static void launchActivity (Activity activity, int requestCode) {
        Intent intent = new Intent(activity, GroupHwUndoActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    private PublicLoadLayout rootView;
    private TextView backView;
    private TextView titleView;
    private XListView listView;

    private RequestGroupHwUndoListTask requestGroupHwUndoListTask;
    private RequestQuestionListTask requestQuestionListTask;
    private int pageIndex = 1;
    private final static int PAGESIZE = 20;

    private GroupHwUndoListAdapter groupHwUndoListAdapter;
    private List<GroupHwUnBean> dataList = new ArrayList<GroupHwUnBean>();

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = PublicLoadUtils.createPage(this, R.layout.group_undo_list_layout);
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData () {
                pageIndex = 1;
                requestHwUndoList(true, false, false);
            }
        });
        setContentView(rootView);
        findView();
        requestHwUndoList(false, true, false);
    }

    private void findView () {
        backView = (TextView) findViewById(R.id.pub_top_left);
        titleView = (TextView) findViewById(R.id.pub_top_mid);
        titleView.setText(R.string.hw_undo_title);
        listView = (XListView) findViewById(R.id.group_hw_undo_list);
        listView.setScrollable(false);
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(ixListViewListener);
        groupHwUndoListAdapter = new GroupHwUndoListAdapter(this);
        listView.setAdapter(groupHwUndoListAdapter);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (requestGroupHwUndoListTask != null) {
                    requestGroupHwUndoListTask.cancel();
                    requestGroupHwUndoListTask = null;
                }
                if (requestQuestionListTask != null) {
                    requestQuestionListTask.cancel();
                    requestQuestionListTask = null;
                }
                forResult();
                GroupHwUndoActivity.this.finish();
            }
        });
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
            if (NetWorkTypeUtils.isNetAvailable()) {
                pageIndex = 1;
                requestHwUndoList(true, false, false);
            } else {
                listView.stopRefresh();
                Util.showUserToast(R.string.net_null, -1, -1);
            }
        }

        @Override
        public void onLoadMore (XListView view) {
            if (NetWorkTypeUtils.isNetAvailable()) {
                requestHwUndoList(false, false, true);
            } else {
                listView.stopLoadMore();
                Util.showUserToast(R.string.net_null, -1, -1);
            }
        }
    };

    private void requestHwUndoList (final boolean isRefresh, final boolean showLoading,
                                    final boolean isLoaderMore) {
        if (showLoading) {
            rootView.loading(true);
        }
        int oage = pageIndex;
        if (isLoaderMore) {
            oage += 1;
        }
        requestGroupHwUndoListTask = new RequestGroupHwUndoListTask(this, oage, PAGESIZE, new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
                try {
                    rootView.finish();
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
                        dataList.clear();
                        updateUI();
                        rootView.dataNull(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void dataError (int type, String msg) {
                rootView.finish();
                listView.stopRefresh();
                listView.stopLoadMore();
                if (isRefresh || isLoaderMore) {
                    if (!StringUtils.isEmpty(msg)) {
                        Util.showUserToast(msg, null, null);
                    } else {
                        Util.showUserToast(R.string.net_null_one, -1, -1);
                    }
                } else if (showLoading) {
                    rootView.netError(true);
                }
            }
        });
        requestGroupHwUndoListTask.start();
    }

    /**
     * 更新列表UI
     */
    private void updateUI () {
        rootView.finish();
        listView.setScrollable(true);
        listView.setPullRefreshEnable(true);
        if (groupHwUndoListAdapter != null) {
            groupHwUndoListAdapter.setList(dataList);
        }
    }

    private void forResult () {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            forResult();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode,
                                     Intent data) {
        LogInfo.log("king",
                "GroupHwUndoActivity onActivityResult requestGroupData");
        requestHwUndoList(true, false, false);
//        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 请求作业群组试题列表接口
     */
    private void requestQuestionList (String paperId) {
        if (requestQuestionListTask == null || requestQuestionListTask.isCancelled()) {
            rootView.loading(true);
            requestQuestionListTask = new RequestQuestionListTask(this, paperId,
                    new AsyncCallBack() {
                        @Override
                        public void update (YanxiuBaseBean result) {
                            rootView.finish();
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
                                        GroupHwUndoActivity.this,
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
                            rootView.finish();
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

    @Override
    protected void onDestroy () {
        super.onDestroy();
        if (requestGroupHwUndoListTask != null) {
            requestGroupHwUndoListTask.cancel();
            requestGroupHwUndoListTask = null;
        }
        if (requestQuestionListTask != null) {
            requestQuestionListTask.cancel();
            requestQuestionListTask = null;
        }
    }
}
