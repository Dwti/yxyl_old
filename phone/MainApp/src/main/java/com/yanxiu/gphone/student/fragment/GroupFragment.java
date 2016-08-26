package com.yanxiu.gphone.student.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.core.utils.StringUtils;
import com.common.core.view.xlistview.XListView;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.AddGroupActivity;
import com.yanxiu.gphone.student.activity.GroupHwActivity;
import com.yanxiu.gphone.student.activity.GroupInfoActivity;
import com.yanxiu.gphone.student.adapter.GroupListAdapter;
import com.yanxiu.gphone.student.bean.DataStatusEntityBean;
import com.yanxiu.gphone.student.bean.GroupBean;
import com.yanxiu.gphone.student.bean.GroupEventRefresh;
import com.yanxiu.gphone.student.bean.GroupHwwaitFinishBean;
import com.yanxiu.gphone.student.bean.GroupListBean;
import com.yanxiu.gphone.student.bean.PropertyBean;
import com.yanxiu.gphone.student.bean.statistics.StatisticHashMap;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.student.requestTask.RequestGroupListTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.utils.statistics.DataStatisticsUploadManager;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 *
 * Created by Administrator on 2015/7/7.
 */
public class GroupFragment extends Fragment {
    private final static String TAG=GroupFragment.class.getSimpleName();
    private PublicLoadLayout rootView;

    private TextView groupTitle;

    private ImageView addGroupView;

//    private RelativeLayout unFinishedView;
//    private View unFinishedBottomView;

//    private TextView unFinishedTextView;

    private XListView groupList;

    private RelativeLayout noGroupView;
    private View noGroupTopView;

    private TextView noGroupTextView,noGroupAddTips;
    private TextView noGroupAddView;

    private List<GroupBean> datalist = new ArrayList<>();

    private RequestGroupListTask requestGroupListTask;

    private GroupListAdapter mGroupListAdapter;

    private boolean isNoAddClass = false;

    private String className;

    private int classId;

    private final int RIGHT_ADD = 1;
    private final int RIGHT_REFRESH = 2;
    private final int RIGHT_INFO = 3;
    private final int RIGHT_GONE = 4;

    private final int NO_CLASS_FLAG=71;
    private final int HAS_CLASS_FLAG=72;
    private RelativeLayout no_class;

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        rootView = PublicLoadUtils.createPage(getActivity(), R.layout.group_fragment);
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData () {
                requestGroupData(false, true);
                EventBus.getDefault().post(new GroupHwwaitFinishBean());//刷新重试更新小红点
            }
        });
        findView();
        EventBus.getDefault().register(this);
        requestGroupData(false, true);
        return rootView;
    }
    public void onEventMainThread(GroupEventRefresh event) {
        EventBus.getDefault().post(new GroupHwwaitFinishBean());
        requestGroupData(false, true);
    }
    private void findView () {
        groupTitle = (TextView) rootView.findViewById(R.id.main_public_top_group).findViewById(R.id
                .public_layout_top_tv);
        groupTitle.setText(R.string.navi_tbm_group);

        no_class= (RelativeLayout) rootView.findViewById(R.id.no_class);
        no_class.setVisibility(View.GONE);
        TextView TextViewInfo= (TextView) rootView.findViewById(R.id.TextViewInfo);
        TextViewInfo.setText(R.string.class_no_work);

        addGroupView = (ImageView) rootView.findViewById(R.id.main_public_top_group).findViewById(R.id
                .public_layout_top_iv);
        RelativeLayout.LayoutParams addGroupViewParams= (RelativeLayout.LayoutParams) addGroupView.getLayoutParams();
        addGroupViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addGroupViewParams.setMargins(
                0,0,getResources().getDimensionPixelOffset(R.dimen.dimen_10),0);
        addGroupView.setLayoutParams(addGroupViewParams);
//        unFinishedView = (RelativeLayout) rootView.findViewById(R.id.group_unfinish_tip_layout);
//        unFinishedBottomView = rootView.findViewById(R.id.group_unfinish_tip_bottom);
//
//        unFinishedTextView = (TextView) rootView.findViewById(R.id.group_unfinish_tip_text);
        groupList = (XListView) rootView.findViewById(R.id.group_list);

        initNoGroupView();

        groupList.setScrollable(false);
        groupList.setPullLoadEnable(false);
        groupList.setXListViewListener(ixListViewListener);
        mGroupListAdapter = new GroupListAdapter(getActivity());
        groupList.setAdapter(mGroupListAdapter);
        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view,
                                     int position, long id) {
                if (datalist != null && datalist.size() > 0) {
                    final GroupBean entity = datalist
                            .get(position - groupList.getHeaderViewsCount());
                    if (entity != null) {
                        GroupHwActivity.launchActivity(getActivity(), classId,
                                entity.getId(), entity.getName(),
                                GroupHwActivity.LAUNCHER_GROUP_HW);
                    }
                }
            }
        });
//        unFinishedView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View v) {
//                GroupHwUndoActivity.launchActivity(getActivity(),
//                        GroupHwUndoActivity.LAUNCHER_GROUP_HW_UNDO);
//            }
//        });


    }

    /**
     * 初始化无班级提示框相关View
     */
    private void initNoGroupView() {
        noGroupTopView = rootView.findViewById(R.id.no_group_top_view);
        noGroupView = (RelativeLayout) rootView.findViewById(R.id.no_group);
        noGroupTextView = (TextView) noGroupView.findViewById(R.id.top_tip_tx);
        noGroupTextView.setText(R.string.no_class_tip);
        noGroupAddView = (TextView) noGroupView.findViewById(R.id.group_bottom_submit);
        noGroupAddView.setText(R.string.class_add);
        noGroupAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (isNoAddClass) {
                    AddGroupActivity.launchActivity(getActivity(), AddGroupActivity.LAUNCHER_ADDGROUPACTIVITY);
                } else {
                    GroupInfoActivity.launchActivity(getActivity(), GroupInfoActivity.CANCEL_CLASS, classId);
                }
            }
        });
        noGroupAddTips=(TextView)noGroupView.findViewById(R.id.group_add_tips);
        noGroupAddTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityJumpUtils.jumpToNoGroupAddTipsActivity(GroupFragment.this.getActivity());
            }
        });
    }

    //加入班级成功
    private void addGroupStatistics() {
        StatisticHashMap statisticHashMap = new StatisticHashMap();
        statisticHashMap.put(YanXiuConstant.eventID, "20:event_9");//9:加入班级
        ArrayList<StatisticHashMap> arrayList = new ArrayList<StatisticHashMap>();
        arrayList.add(statisticHashMap);
        HashMap<String, String> addGroupHashMap = new HashMap<>();
        addGroupHashMap.put(YanXiuConstant.content, Util.listToJson(arrayList));
        DataStatisticsUploadManager.getInstance().NormalUpLoadData(getActivity(), addGroupHashMap);
    }

    private XListView.IXListViewListener ixListViewListener = new XListView.IXListViewListener() {

        @Override
        public void onRefresh (XListView view) {
            if (NetWorkTypeUtils.isNetAvailable()) {
                LogInfo.log("king", "onRefresh");
                requestGroupData(true, false);
                EventBus.getDefault().post(new GroupHwwaitFinishBean());//刷新重试更新小红点
            } else {
                groupList.stopRefresh();
                Util.showUserToast(R.string.net_null, -1, -1);
            }
        }

        @Override
        public void onLoadMore (XListView view) {

        }
    };

    private void setUnFinishedViewDisp (boolean toDisp) {
//        if (toDisp) {
//            unFinishedView.setVisibility(View.VISIBLE);
//            unFinishedBottomView.setVisibility(View.VISIBLE);
//        } else {
//            unFinishedView.setVisibility(View.GONE);
//            unFinishedBottomView.setVisibility(View.GONE);
//        }
    }

    public void requestGroupData (final boolean isRefresh, final boolean showLoading) {
        if (rootView == null) {
            return;
        }
        if (showLoading) {
            rootView.loading(true);

        }
        requestGroupListTask = new RequestGroupListTask(getActivity(), new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
                no_class.setVisibility(View.GONE);
                rootView.finish();
                groupList.stopLoadMore();
                groupList.stopRefresh();
                GroupListBean groupListBean = (GroupListBean) result;
                PropertyBean propertyBean = groupListBean.getProperty();
                if (propertyBean != null) {
//                    if (propertyBean.getTotalUnfinish() <= 0) {
//                        setUnFinishedViewDisp(false);
//                    } else {
//                        setUnFinishedViewDisp(true);
//                        unFinishedTextView.setText(Html.fromHtml(getActivity().getResources().getString(R.string
//                                .group_top_tip_start_text) + " <font color='#ffdb4d'>" +
//                                propertyBean
//                                        .getTotalUnfinish() +
//                                "</font> " + getActivity().getResources().getString(R.string
//                                .group_top_tip_end_text)));
//                    }
                    className = propertyBean.getClassName();
                    classId = propertyBean.getClassId();
                    //加入班级埋点
                    addGroupStatistics();
                } else {
                    className = "";
                    classId = 0;
//                    setUnFinishedViewDisp(false);
                }
                ArrayList<GroupBean> dataEntity = groupListBean.getData();
                if (dataEntity != null && dataEntity.size() > 0) {
                    datalist.clear();
                    datalist.addAll(dataEntity);
                    updateUI();
                    ArrayList<StatisticHashMap> arrayList = new ArrayList<StatisticHashMap>();
                    for (int i=0; i<dataEntity.size(); i++) {
                        StatisticHashMap statisticHashMap = new StatisticHashMap();
                        statisticHashMap.put(YanXiuConstant.eventID, "20:event_4");//4:收到作业
                        HashMap reserveHashMap = new HashMap();
                        reserveHashMap.put(YanXiuConstant.classID, String.valueOf(classId));
                        reserveHashMap.put(YanXiuConstant.quesNum, String.valueOf(dataEntity.get(i).getWaitFinishNum()));
                        statisticHashMap.put(YanXiuConstant.reserved, Util.hashMapToJsonTwo(reserveHashMap));
                        arrayList.add(statisticHashMap);
                    }
                    if (arrayList.size() > 0) {
                        receiveWorkStatistic(arrayList);
                    }
                } else {
                    //setNoGroupViewDisp(true);
                    //noGroupTopView.setVisibility(View.VISIBLE);
                    DataStatusEntityBean statue = groupListBean.getStatus();
                    if (statue != null) {
                        datalist.clear();
                        updateUI();
                        if (statue.getCode() == NO_CLASS_FLAG) {
                            LogInfo.log(TAG,"NO_CLASS_FLAG");
                            groupTitle.setText(R.string.group_top_title);
                            isNoAddClass = true;
                            noGroupTextView.setText(R.string.no_class_tip);
                            noGroupAddView.setText(R.string.class_add);
                            noGroupAddTips.setVisibility(View.VISIBLE);
                            updateRightUI(RIGHT_ADD);
                        } else if (statue.getCode() == HAS_CLASS_FLAG) {
                            LogInfo.log(TAG,"HAS_CLASS_FLAG");
                            isNoAddClass = false;
                            noGroupTextView.setText(R.string.class_shenhe);
                            noGroupAddView.setText(R.string.class_see);
                            noGroupAddTips.setVisibility(View.GONE);
                            updateRightUI(RIGHT_REFRESH);
                        } else {
                            //还未布置作业
//                            Util.showCustomToast(R.string.class_no_work);
                            no_class.setVisibility(View.VISIBLE);
                            setNoGroupViewDisp(false);
                        }
                    }
                }
            }

            //收到作业统计
            private void receiveWorkStatistic(ArrayList<StatisticHashMap> arrayList) {
                HashMap<String, String> receiveWorkHashMap = new HashMap<>();
                receiveWorkHashMap.put(YanXiuConstant.content, Util.listToJson(arrayList));
                DataStatisticsUploadManager.getInstance().NormalUpLoadData(getActivity(), receiveWorkHashMap);

            }

            @Override
            public void dataError (int type, String msg) {
                rootView.finish();
                groupList.stopLoadMore();
                groupList.stopRefresh();
                if (isRefresh) {
                    if (!StringUtils.isEmpty(msg)) {
                        Util.showUserToast(msg, null, null);
                    } else {
                        Util.showUserToast(R.string.net_null_one, -1, -1);
                    }
                } else if (showLoading) {
                    updateRightUI(RIGHT_GONE);
                    setNoGroupViewDisp(false);
                    rootView.netError(true);
                }
            }
        });
        requestGroupListTask.start();
    }


    private void updateUI () {
        rootView.finish();
        updateRightUI(RIGHT_INFO);
        if (datalist.size() > 0) {
            setNoGroupViewDisp(false);
            groupList.setScrollable(true);
            groupList.setPullRefreshEnable(true);
        } else {
            setNoGroupViewDisp(true);
            groupList.setScrollable(false);
        }
        if (mGroupListAdapter != null) {
            mGroupListAdapter.setList(datalist);
        }
        if (!StringUtils.isEmpty(className)) {
            groupTitle.setText(className);
        }
    }

    private void setNoGroupViewDisp (boolean toDisp) {
        if (toDisp) {
            noGroupTopView.setVisibility(View.VISIBLE);
            noGroupView.setVisibility(View.VISIBLE);

        } else {
            noGroupView.setVisibility(View.GONE);
            noGroupTopView.setVisibility(View.GONE);

        }
    }

    private void updateRightUI (int type) {
        addGroupView.setVisibility(View.VISIBLE);
        if (type == RIGHT_ADD) {
            addGroupView.setBackgroundResource(R.drawable.group_top_add);
            addGroupView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    AddGroupActivity.launchActivity(getActivity(), AddGroupActivity.LAUNCHER_ADDGROUPACTIVITY);
                }
            });

        } else if (type == RIGHT_REFRESH) {
            addGroupView.setBackgroundResource(R.drawable.class_refresh);
            addGroupView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    requestGroupData(false, true);
                }
            });
        } else if (type == RIGHT_INFO) {
            addGroupView.setBackgroundResource(R.drawable.group_list_person);
            addGroupView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    GroupInfoActivity.launchActivity(getActivity(),
                            GroupInfoActivity.EXIT_CLASS, classId);
                }
            });
        } else if (type == RIGHT_GONE) {
            addGroupView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (requestGroupListTask != null) {
            requestGroupListTask.cancel();
        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode,
                                  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogInfo.log(TAG, "groupFragment onActivityResult requestGroupData");
        boolean toRefresh = data.getBooleanExtra("toRefresh", true);
        if(toRefresh){
            requestGroupData(true, false);
        }
    }
}
