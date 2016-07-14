package com.yanxiu.gphone.parent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.YanxiuParentBaseActivity;
import com.yanxiu.gphone.parent.adapter.RecycleViewDivider;
import com.yanxiu.gphone.parent.adapter.WeekReportDetailsAdapter;
import com.yanxiu.gphone.parent.bean.ParentHwDetailBean;
import com.yanxiu.gphone.parent.bean.ParentHwDetailsBean;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.requestTask.RequestHwDetailsTask;
import com.yanxiu.gphone.parent.utils.PublicLoadUtils;
import com.yanxiu.gphone.parent.view.PublicLoadLayout;

import java.util.ArrayList;

/**
 * Created by hai8108 on 16/3/23.
 */
public class ParentWeekReportDetailsActivity extends YanxiuParentBaseActivity implements View.OnClickListener {
    private View topView;
    private PublicLoadLayout rootView;
    private TextView leftTitle, midTitle, rightTitle;
    private RecyclerView detailsList;
    private WeekReportDetailsAdapter adapter;
    private RequestHwDetailsTask mRequestHwDetailsTask;
    private String classId;
    private int week;
    private String year;
    private ArrayList<ParentHwDetailBean> detailBeans;

    public static void launchActivity (Activity activity, String classId, int week, String year) {
        Intent intent = new Intent(activity, ParentWeekReportDetailsActivity.class);
        intent.putExtra("classId", classId);
        intent.putExtra("week", week);
        intent.putExtra("year", year);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = PublicLoadUtils.createPage(this, R.layout
                .activity_details_weekreport_parent_layout);
        setContentView(rootView);
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData () {
                requestHwDetailsTask();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            classId = intent.getStringExtra("classId");
            week = intent.getIntExtra("week", 0);
            year = intent.getStringExtra("year");
        } else {
            finish();
        }
        findView();
    }

    private void findView () {
        topView = findViewById(R.id.top_view);
        leftTitle = (TextView) topView.findViewById(R.id.pub_top_left);
        midTitle = (TextView) topView.findViewById(R.id.pub_top_mid);
        midTitle.setText(R.string.yanxiu_parent_details_txt);
        midTitle.setTextColor(getResources().getColor(R.color.color_b28f47_p));
        leftTitle.setOnClickListener(this);
        detailsList = (RecyclerView) findViewById(R.id.detailList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        detailsList.setLayoutManager(layoutManager);
        detailsList.setHasFixedSize(true);
        detailsList.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, CommonCoreUtil.dipToPx(7), getResources()
                .getColor(R.color.color_333333_p)));
        adapter = new WeekReportDetailsAdapter(this, detailBeans);
        detailsList.setAdapter(adapter);
        requestHwDetailsTask();
    }

    @Override
    public void onClick (View v) {
        if (v == leftTitle) {
            this.finish();
        }
    }

    //数据请求
    private AsyncCallBack mCallBack = new AsyncCallBack() {
        @Override
        public void update (YanxiuBaseBean result) {
            rootView.finish();
            ParentHwDetailsBean mBean = (ParentHwDetailsBean) result;
            detailBeans = mBean.getData();
            updateList(detailBeans);
        }

        @Override
        public void dataError (int type, String msg) {
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
    private void updateList(ArrayList<ParentHwDetailBean> detailBeans){
        if(detailBeans != null && detailBeans.size() > 0) {
            if (adapter == null) {
                adapter = new WeekReportDetailsAdapter(this, detailBeans);
                detailsList.setAdapter(adapter);
            } else {
                adapter.setList(detailBeans);
            }
        }
    }
    private void requestHwDetailsTask () {
        cancelTask();
        rootView.loading(true);
        mRequestHwDetailsTask = new RequestHwDetailsTask(this, classId, week, year, mCallBack);
        mRequestHwDetailsTask.start();
    }

    private void cancelTask () {
        if (mRequestHwDetailsTask != null) {
            mRequestHwDetailsTask.cancel();
        }
        mRequestHwDetailsTask = null;
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        cancelTask();
        topView = null;
        leftTitle = null;
        midTitle = null;
        rightTitle = null;
        if (detailsList != null) {
            detailsList.removeAllViews();
        }
        detailsList = null;
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
