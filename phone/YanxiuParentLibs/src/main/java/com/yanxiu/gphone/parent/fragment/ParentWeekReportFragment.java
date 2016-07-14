package com.yanxiu.gphone.parent.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.adapter.RecycleViewDivider;
import com.yanxiu.gphone.parent.adapter.WeekReportAdapter;
import com.yanxiu.gphone.parent.bean.ParentPublicPropertyBean;
import com.yanxiu.gphone.parent.bean.ParentWeekReportBean;
import com.yanxiu.gphone.parent.bean.ParentWeekReportDataBean;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.requestTask.RequestCurrentWeekReportTask;
import com.yanxiu.gphone.parent.requestTask.RequestWeekReportTask;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.utils.PublicLoadUtils;
import com.yanxiu.gphone.parent.view.PublicLoadLayout;

import java.util.ArrayList;

/**
 * Created by hai8108 on 16/3/17.
 */
public class ParentWeekReportFragment extends Fragment implements View.OnClickListener {
    private final static int FORWARD_WEEK_TYPE = 0;//上一周
    private final static int NEXT_WEEK_TYPE = 1;//下一周
    private PublicLoadLayout rootView;
    private View topView;
    private TextView leftTitle, midTitle, rightTitle;
    private RecyclerView reportListView;
    private WeekReportAdapter mWeekReportAdapter;
    private ArrayList<ParentWeekReportDataBean> modelDatas;
    private ParentPublicPropertyBean timeDataBean;
    private RequestCurrentWeekReportTask mRequestCurrentWeekReportTask;
    private RequestWeekReportTask mRequestWeekReportTask;
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = PublicLoadUtils.createPage(getActivity(), R.layout.fragment_week_report_recycleview_layout);
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData () {
                requestCurrentWeekReportData();
            }
        });
        findView();
        return rootView;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void findView () {
        topView = rootView.findViewById(R.id.top_view);
        leftTitle = (TextView) topView.findViewById(R.id.pub_top_sub_left);
        leftTitle.setVisibility(View.VISIBLE);
        topView.findViewById(R.id.pub_top_left).setVisibility(View.GONE);
        rightTitle = (TextView) topView.findViewById(R.id.pub_top_right);
        midTitle = (TextView) topView.findViewById(R.id.pub_top_mid);
        leftTitle.setText(R.string.back_week_txt);
        rightTitle.setText(R.string.forward_week_txt);
        midTitle.setText(R.string.navi_tbm_weekworktab);
        midTitle.setTextColor(getActivity().getResources().getColor(R.color.color_b28f47_p));
        leftTitle.setEnabled(false);
        rightTitle.setEnabled(false);
        reportListView = (RecyclerView) rootView.findViewById(R.id.reportListView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        reportListView.setLayoutManager(layoutManager);
        reportListView.setHasFixedSize(true);
        reportListView.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL, CommonCoreUtil.dipToPx(14),
                getResources()
                        .getColor(R.color.color_333333_p)));
        mWeekReportAdapter = new WeekReportAdapter(getActivity(), modelDatas, timeDataBean);
        reportListView.setAdapter(mWeekReportAdapter);
        leftTitle.setOnClickListener(this);
        rightTitle.setOnClickListener(this);
        leftTitle.setVisibility(View.INVISIBLE);
        rightTitle.setVisibility(View.INVISIBLE);
        requestCurrentWeekReportData();
    }

    @Override
    public void onClick (View v) {
        int type = FORWARD_WEEK_TYPE;
        if (v == leftTitle) {
            if(timeDataBean.getCanLast() != 1){
                ParentUtils.showToast(R.string.parent_hw_report_last_null);
                return;
            } else {
                type = FORWARD_WEEK_TYPE;
            }
        } else if (v == rightTitle) {
            if(timeDataBean.getCanNext() != 1){
                ParentUtils.showToast(R.string.parent_hw_report_next_null);
                return;
            } else {
                type = NEXT_WEEK_TYPE;
            }

        }
        requestWeekReportTaskData(type, timeDataBean);

    }
    private AsyncCallBack mCurrentWeekReportCallBack = new AsyncCallBack() {
        @Override
        public void update (YanxiuBaseBean result) {
            rootView.finish();
            ParentWeekReportBean mBean = (ParentWeekReportBean) result;
            timeDataBean = mBean.getProperty();
            leftTitle.setEnabled(true);
            rightTitle.setEnabled(true);
            LogInfo.log("haitian", "timeData ="+timeDataBean.toString());
            modelDatas = mBean.getData();
            updateList(modelDatas);
        }

        @Override
        public void dataError (int type, String msg) {
            leftTitle.setEnabled(false);
            rightTitle.setEnabled(false);
            if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                rootView.netError(true);
            } else {
                if (TextUtils.isEmpty(msg)) {
                    rootView.dataNull(true);
                } else {
                    rootView.dataNull(msg);
                }
            }
        }
    };
    private void updateList(ArrayList<ParentWeekReportDataBean> modelDatas){
        if(timeDataBean != null){
            if(timeDataBean.getCanNext() == 1){
                if(rightTitle.getVisibility() == View.INVISIBLE) {
                    rightTitle.setVisibility(View.VISIBLE);
                }
            } else {
                if(rightTitle.getVisibility() == View.VISIBLE) {
                    rightTitle.setVisibility(View.INVISIBLE);
                }
            }
            if(timeDataBean.getCanLast() == 1){
                if(leftTitle.getVisibility() == View.INVISIBLE) {
                    leftTitle.setVisibility(View.VISIBLE);
                }
            } else {
                if(leftTitle.getVisibility() == View.VISIBLE) {
                    leftTitle.setVisibility(View.INVISIBLE);
                }
            }
        }
        if(modelDatas != null && modelDatas.size() > 0) {
            if (mWeekReportAdapter == null) {
                mWeekReportAdapter = new WeekReportAdapter(getActivity(), modelDatas, timeDataBean);
                reportListView.setAdapter(mWeekReportAdapter);
            } else {
                mWeekReportAdapter.setList(modelDatas, timeDataBean);
            }
        } else {
            mWeekReportAdapter.setList(modelDatas, timeDataBean);
            rootView.dataNull(true);
        }
    }
    private void requestCurrentWeekReportData(){
        cancelCurrentWeekReportTask();
        rootView.loading(true);
        mRequestCurrentWeekReportTask = new RequestCurrentWeekReportTask(getActivity(), mCurrentWeekReportCallBack);
        mRequestCurrentWeekReportTask.start();
    }
    private void cancelCurrentWeekReportTask(){
        if(mRequestCurrentWeekReportTask != null){
            mRequestCurrentWeekReportTask.cancel();
        }
        mRequestCurrentWeekReportTask = null;
    }
    //上一周下一周数据请求
    private AsyncCallBack mWeekReportCallBack = new AsyncCallBack() {
        @Override
        public void update (YanxiuBaseBean result) {
            rootView.finish();
            leftTitle.setEnabled(true);
            rightTitle.setEnabled(true);
            ParentWeekReportBean mBean = (ParentWeekReportBean) result;
            timeDataBean = mBean.getProperty();
            LogInfo.log("haitian", "timeData =" + timeDataBean.toString());
            modelDatas = mBean.getData();
            updateList(modelDatas);

        }
        @Override
        public void dataError (int type, String msg) {
            rootView.finish();
            leftTitle.setEnabled(true);
            rightTitle.setEnabled(true);
            if (TextUtils.isEmpty(msg)) {
                ParentUtils.showToast(R.string.public_loading_data_null_p);
            } else {
                ParentUtils.showToast(msg);
            }
        }
    };
    private void requestWeekReportTaskData(final int type, ParentPublicPropertyBean mBean){
        cancelWeekReportTask();
        if(mBean != null ) {
            leftTitle.setEnabled(false);
            rightTitle.setEnabled(false);
            rootView.loading(true);
            mRequestWeekReportTask = new RequestWeekReportTask(getActivity(), type, mBean.getWeek(),
                    mBean.getYear(),
                    mWeekReportCallBack);
            mRequestWeekReportTask.start();
        }
    }
    private void cancelWeekReportTask(){
        if(mRequestWeekReportTask != null){
            mRequestWeekReportTask.cancel();
        }
        mRequestWeekReportTask = null;
    }
    @Override
    public void onDestroyView () {
        super.onDestroyView();
        cancelCurrentWeekReportTask();
        cancelWeekReportTask();
    }
}
