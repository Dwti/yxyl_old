package com.yanxiu.gphone.hd.student.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.core.utils.StringUtils;
import com.common.core.view.xlistview.XListView;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.adapter.GroupListAdapter;
import com.yanxiu.gphone.hd.student.bean.DataStatusEntityBean;
import com.yanxiu.gphone.hd.student.bean.GroupBean;
import com.yanxiu.gphone.hd.student.bean.GroupEventRefresh;
import com.yanxiu.gphone.hd.student.bean.GroupHwwaitFinishBean;
import com.yanxiu.gphone.hd.student.bean.GroupListBean;
import com.yanxiu.gphone.hd.student.bean.PropertyBean;
import com.yanxiu.gphone.hd.student.bean.statistics.StatisticHashMap;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestGroupListTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.utils.statistics.DataStatisticsUploadManager;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作业
 * Created by Administrator on 2015/7/7.
 */
public class GroupFragment extends TopBaseFragment {
    private BaseFragment mBaseFragment;
//    private RelativeLayout unFinishedView;
//    private View unFinishedBottomView;

//    private TextView unFinishedTextView;

    private XListView groupList;

    private RelativeLayout noGroupView;
    private View noGroupTopView;

    private TextView noGroupTextView;

    private TextView noGroupAddView;


    private TextView noGroupAddTips;

    private List<GroupBean> datalist = new ArrayList<GroupBean>();

    private RequestGroupListTask requestGroupListTask;

    private GroupListAdapter mGroupListAdapter;

    private boolean isNoAddClass = false;

    private String className;

    private int classId;

    private final int RIGHT_ADD = 1;
    private final int RIGHT_REFRESH = 2;
    private final int RIGHT_INFO = 3;
    private final int RIGHT_GONE = 4;

    private boolean isFirst = true;

    private GroupInfoContainerFragment fg;

    public static Fragment newInstance () {
        GroupFragment groupFragment = new GroupFragment();
        return groupFragment;
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (mBaseFragment != null) {
            return mBaseFragment.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    protected boolean isAttach () {
        return false;
    }

    @Override
    protected View getContentView () {
        fg = (GroupInfoContainerFragment) getParentFragment();
        mPublicLayout = PublicLoadUtils.createPage(getActivity(), R.layout.group_fragment);
        mPublicLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.trans));
        mPublicLayout.setContentBackground(android.R.color.transparent);
        mPublicLayout.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData () {
                requestGroupData(false, true);
                EventBus.getDefault().post(new GroupHwwaitFinishBean());
            }
        });
        findView();
        EventBus.getDefault().register(this);
        return mPublicLayout;
    }
    public void onEventMainThread(GroupEventRefresh event) {
        EventBus.getDefault().post(new GroupHwwaitFinishBean());
        requestGroupData(false, true);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            requestGroupData(false, true);
        }
    }
    @Override
    public void onDestroy () {
        super.onDestroy();
        mBaseFragment=null;
//        unFinishedView=null;
//        unFinishedBottomView=null;
//        unFinishedTextView=null;
        groupList=null;
        noGroupView=null;
        noGroupTopView=null;
        noGroupTextView=null;
        noGroupAddView=null;
        datalist=null;
        requestGroupListTask=null;
        mGroupListAdapter=null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initLoadData() {
        requestGroupData(false, true);
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
        if (requestGroupListTask != null) {
            requestGroupListTask.cancel();
            requestGroupListTask=null;
        }
    }

    private void finish () {
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
        leftView.setVisibility(View.GONE);
        titleText.setText(R.string.navi_tbm_group);

//        unFinishedView = (RelativeLayout) mPublicLayout.findViewById(R.id.group_unfinish_tip_layout);
//        unFinishedBottomView = mPublicLayout.findViewById(R.id.group_unfinish_tip_bottom);
//
//        unFinishedTextView = (TextView) mPublicLayout.findViewById(R.id.group_unfinish_tip_text);
        groupList = (XListView) mPublicLayout.findViewById(R.id.group_list);
        noGroupTopView = mPublicLayout.findViewById(R.id.no_group_top_view);

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
                        mBaseFragment = (BaseFragment) GroupHwFragment.newInstance(classId,
                                entity.getId(), entity.getName());
                        fg.mIFgManager.addFragment(mBaseFragment,true, GroupHwFragment.class.getName());
                    }
                }
            }
        });
//        unFinishedView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View v) {
//                mBaseFragment = (BaseFragment) GroupHwUndoFragment.newInstance();
//                fg.mIFgManager.addFragment(mBaseFragment,true, GroupHwUndoFragment.class.getName());
//            }
//        });
        LogInfo.log("king", "findView");
    }

    private void initNoGroupView() {
        noGroupView = (RelativeLayout) mPublicLayout.findViewById(R.id.no_group);

        noGroupTextView = (TextView) noGroupView.findViewById(R.id.top_tip_tx);
        noGroupTextView.setText(R.string.no_class_tip);
        noGroupAddView = (TextView) noGroupView.findViewById(R.id.group_bottom_submit);
        noGroupAddView.setText(R.string.class_add);
        noGroupAddTips=(TextView)noGroupView.findViewById(R.id.group_add_tips);
        noGroupAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (isNoAddClass) {
                    mBaseFragment = (BaseFragment) GroupAddFragment.newInstance();
                    fg.mIFgManager.addFragment(mBaseFragment, true,
                            GroupAddFragment.class.getName());
                } else {
                    mBaseFragment = (BaseFragment) GroupDetailsFragment.newInstance
                            (GroupDetailsFragment.CANCEL_CLASS, classId);
                    fg.mIFgManager.addFragment(mBaseFragment,true, GroupDetailsFragment.class.getName());
                }
            }
        });
        noGroupAddTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaseFragment= (BaseFragment) NoGroupAddTipsFragment.newInstance();
                fg.mIFgManager.addFragment(mBaseFragment,true);
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
            if (!NetWorkTypeUtils.isNetAvailable()) {
                LogInfo.log("king", "onRefresh");
                requestGroupData(true, false);
                EventBus.getDefault().post(new GroupHwwaitFinishBean());
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
        if (mPublicLayout == null) {
            return;
        }
        if (showLoading) {
            mPublicLayout.loading(true);
            isFirst = false;
        }
        cancelTask();
        requestGroupListTask = new RequestGroupListTask(getActivity(), new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
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
                    DataStatusEntityBean statue = groupListBean.getStatus();
                    if (statue != null) {
                        datalist.clear();
                        updateUI();
                        if (statue.getCode() == 71) {
                            titleText.setText(R.string.group_top_title);
                            isNoAddClass = true;
                            noGroupTextView.setText(R.string.no_class_tip);
                            noGroupAddView.setText(R.string.class_add);
                            noGroupAddTips.setVisibility(View.VISIBLE);
                            updateRightUI(RIGHT_ADD);
                        } else if (statue.getCode() == 72) {
                            isNoAddClass = false;
                            noGroupTextView.setText(R.string.class_shenhe);
                            noGroupAddView.setText(R.string.class_see);
                            noGroupAddTips.setVisibility(View.GONE);
                            updateRightUI(RIGHT_REFRESH);
                        }else {
                            //还未布置作业
                            Util.showCustomToast(R.string.class_no_work);
                            setNoGroupViewDisp(false);
                        }

                    }
                }
                mPublicLayout.finish();
            }

            @Override
            public void dataError (int type, String msg) {
                mPublicLayout.finish();
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
                    mPublicLayout.netError(true);
                }
            }
        });
        requestGroupListTask.start();
    }

    //收到作业统计
    private void receiveWorkStatistic(ArrayList<StatisticHashMap> arrayList) {
        HashMap<String, String> receiveWorkHashMap = new HashMap<>();
        receiveWorkHashMap.put(YanXiuConstant.content, Util.listToJson(arrayList));
        DataStatisticsUploadManager.getInstance().NormalUpLoadData(getActivity(), receiveWorkHashMap);

    }

    private void updateUI () {
        mPublicLayout.finish();
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
            titleText.setText(className);
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
        ivRight.setVisibility(View.VISIBLE);
        if (type == RIGHT_ADD) {
            ivRight.setImageResource(R.drawable.group_top_add);
            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    mBaseFragment = (BaseFragment) GroupAddFragment.newInstance();
                    fg.mIFgManager.addFragment(mBaseFragment,true, GroupAddFragment.class.getName());
                }
            });
        } else if (type == RIGHT_REFRESH) {
            ivRight.setImageResource(R.drawable.class_refresh);
            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    requestGroupData(false, true);
                }
            });
        } else if (type == RIGHT_INFO) {
            ivRight.setImageResource(R.drawable.group_list_person);
            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    mBaseFragment = (BaseFragment) GroupDetailsFragment.newInstance
                            (GroupDetailsFragment.EXIT_CLASS, classId);
                    fg.mIFgManager.addFragment(mBaseFragment,true, GroupDetailsFragment.class.getName());
                }
            });
        } else if (type == RIGHT_GONE) {
            ivRight.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode,
                                  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean toRefresh = data.getBooleanExtra("toRefresh", true);
        if (toRefresh) {
            requestGroupData(true, false);
        }
    }

    @Override
    public void onReset() {
            destoryData();
    }


}
