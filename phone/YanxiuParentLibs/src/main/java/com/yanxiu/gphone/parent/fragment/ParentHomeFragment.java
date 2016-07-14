package com.yanxiu.gphone.parent.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.view.xlistview.XListView;
import com.common.login.LoginModel;
import com.common.login.model.ParentInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.adapter.ParentHomeAdapter;
import com.yanxiu.gphone.parent.bean.ParentHomeDetailBean;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.requestTask.RequestParentHomeTask;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.utils.PublicLoadUtils;
import com.yanxiu.gphone.parent.view.PublicLoadLayout;

/**
 * Created by hai8108 on 16/3/17.
 */
public class ParentHomeFragment extends Fragment {

    protected final int pageSize = 10;
    protected int pageIndex = 1;

    protected boolean isFirstLoading = true;

    private boolean isMoreData = true;


    private PublicLoadLayout publicLoadLayout;

    private TextView tvTitle;
    private TextView tvLeft;

    private RequestParentHomeTask requestParentHomeTask;


    private XListView xListView;

    private ParentHomeAdapter homeAdapter;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        publicLoadLayout = PublicLoadUtils.createPage(this.getActivity(), R.layout.parent_home_fg_layout);
        publicLoadLayout.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {
                sendRequest2RefreshList();
            }
        });
//        contentView = inflater.inflate(R.layout.parent_home_fg_layout, null);
        initView(publicLoadLayout);
        initData();
        return publicLoadLayout;
    }



    private void initView(View rootView) {

        xListView = (XListView) rootView.findViewById(R.id.home_xlistview);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(false);
        xListView.setScrollable(true);
        xListView.setXListViewListener(new XListViewRefreshListener());

        tvTitle = (TextView) rootView.findViewById(R.id.pub_top_mid);
        tvLeft = (TextView) rootView.findViewById(R.id.pub_top_left);
        tvLeft.setVisibility(View.GONE);
    }


    private void initData(){
        tvTitle.setText(this.getResources().getString(R.string.user_name_txt));
        if(LoginModel.getRoleUserInfoEntity() != null){

            if(((ParentInfo)LoginModel.getRoleUserInfoEntity()).getChild() != null){
                String studentName = ((ParentInfo)LoginModel.getRoleUserInfoEntity()).getChild().getRealname();
                if(!TextUtils.isEmpty(studentName)){
                    tvTitle.setText(studentName);
                }
            }
        }
        homeAdapter = new ParentHomeAdapter(this.getActivity());
        xListView.setAdapter(homeAdapter);

        sendRequest2RefreshList();


//        RequestParentHomeTask requestParentHomeTask = new RequestParentHomeTask(this.getActivity(), 1, 10, new AsyncCallBack() {
//            @Override
//            public void update(YanxiuBaseBean result) {
//                ParentHomeDetailBean parentHomeDetailBean = (ParentHomeDetailBean) result;
//                homeAdapter.setList(parentHomeDetailBean.getData());
//            }
//
//            @Override
//            public void dataError(int type, String msg) {
//
//            }
//        });
//        requestParentHomeTask.start();
    }


    /**
     * 下拉刷新控件的 动作监听 刷新、加载更多、更新当前页面
     *
     * @author lidm
     */
    private class XListViewRefreshListener implements XListView.IXListViewListener {

        @Override
        public void onRefresh(XListView view) {//刷新
            sendRequest2RefreshList();

        }



        @Override
        public void onLoadMore(XListView view) {//加载更多
            sendRequest2LoadMore();
        }

    }

    /**
     * 加载更多
     */
    private void sendRequest2LoadMore() {
        cancelTask();

        LogInfo.log("geny", "sendRequest2RefreshList --------------------------");
        LogInfo.log("geny", "sendRequest2RefreshList" + "---" + pageIndex + "----" + pageSize);

        requestParentHomeTask = new RequestParentHomeTask(this.getActivity(), pageIndex, pageSize, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                updateView(result);

            }

            @Override
            public void dataError(int type, String msg) {
                handerLoadMore(type, msg);
            }


        });
        requestParentHomeTask.start();

    }


    protected void updateView(YanxiuBaseBean result) {

        xListView.setPullRefreshEnable(true);
        long cacheTime = System.currentTimeMillis();
        xListView.setSuccRefreshTime(cacheTime);
        xListView.stopLoadMore();
        if (isDataEmpty((ParentHomeDetailBean) result)) {
            xListView.setVisibility(View.GONE);
            xListView.setPullRefreshEnable(false);
            isMoreData = false;
            return;
        } else {
            isMoreData = pageIndex * pageSize < ((ParentHomeDetailBean) result).getPage().getTotalCou();
            LogInfo.log("geny", (pageIndex * pageSize) + "-----" + ((ParentHomeDetailBean) result).getPage().getTotalCou());
            pageIndex++;
        }
        LogInfo.log("geny", "是否可以刷新" + isMoreData);
        xListView.setPullLoadEnable(isMoreData);
        homeAdapter.filterData(((ParentHomeDetailBean) result).getData());
        homeAdapter.addMoreData(((ParentHomeDetailBean) result).getData());
        xListView.setVisibility(View.VISIBLE);
    }

    /**
     * 处理加载更多错误处理
     * @param type
     */
    protected void handerLoadMore(int type, String msg){
        LogInfo.log("geny", "handerLoadMore---------");
//        xListView.setPullLoadEnable(false);
        xListView.stopLoadMore();
        if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
            if (isFirstLoading) {
                publicLoadLayout.netError(true);
            } else {
                ParentUtils.showToast(R.string.public_loading_net_errtxt_p);
            }
            return;
        }

        if (isFirstLoading) {
            xListView.setVisibility(View.GONE);
            publicLoadLayout.dataError(true);
        } else {
            if(TextUtils.isEmpty(msg)){
                ParentUtils.showToast(R.string.public_loading_net_null_errtxt_p);
            }else{
                ParentUtils.showToast(msg);
            }
        }
    }


    /**
     * 下拉刷新
     */
    private void sendRequest2RefreshList() {

        if(isFirstLoading){
            publicLoadLayout.loading(true);
        }

        cancelTask();

        pageIndex = 1;

        LogInfo.log("geny", "sendRequest2RefreshList --------------------------");
        LogInfo.log("geny", "sendRequest2RefreshList" + "---" + pageIndex + "----" + pageSize);

        requestParentHomeTask = new RequestParentHomeTask(this.getActivity(), pageIndex, pageSize, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {

                refreshView(result);
            }

            @Override
            public void dataError(int type, String msg) {
                handlerReFreshError(type, msg);
            }


        });
        requestParentHomeTask.start();

    }


    protected void refreshView(YanxiuBaseBean result) {

        if(isFirstLoading){
            publicLoadLayout.finish();
        }

        xListView.setPullRefreshEnable(true);
        if (homeAdapter != null){
            homeAdapter.clearDataSrouces();
        }

        long cacheTime = System.currentTimeMillis();
        xListView.setSuccRefreshTime(cacheTime);
        xListView.stopRefresh();

        if (isDataEmpty((ParentHomeDetailBean) result)) {
            publicLoadLayout.dataNull(true);

            xListView.setVisibility(View.GONE);
            xListView.setPullRefreshEnable(false);
            isMoreData = false;
            LogInfo.log("geny", "资料数据为空！！！");
            return;
        } else {
            LogInfo.log("geny", "资料数据不为空！！！");

            refreshSueccessView(result, true);
        }

        xListView.setPullLoadEnable(isMoreData);
        homeAdapter.filterData(((ParentHomeDetailBean) result).getData());
        homeAdapter.setList(((ParentHomeDetailBean) result).getData());
        xListView.setVisibility(View.VISIBLE);
    }

    /**
     * 处理下拉刷新错误处理
     */
    protected  void handlerReFreshError(int type, String msg){
        if(xListView!=null){
            xListView.stopRefresh();
        }
        if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
            if (isFirstLoading) {
                LogInfo.log("geny", "handlerReFreshError --------------------------message_no_network");
                publicLoadLayout.netError(true);
            } else {
                ParentUtils.showToast(R.string.public_loading_net_errtxt_p);
            }
            return;
        }
        if (isFirstLoading) {
            if(xListView!=null){
                xListView.setVisibility(View.GONE);
            }
            publicLoadLayout.dataError(true);
        } else {
            if(TextUtils.isEmpty(msg)){
                ParentUtils.showToast(R.string.public_loading_net_null_errtxt_p);
            }else{
                ParentUtils.showToast(msg);
            }
        }
    }


    /**
     * 是否加载的数据中的list为空
     * @param parentHomeDetailBean
     * @return
     */
    private boolean isDataEmpty(ParentHomeDetailBean parentHomeDetailBean){
        if (parentHomeDetailBean == null || parentHomeDetailBean.getPage() == null || parentHomeDetailBean.getPage().getTotalCou() == 0) {
            return true;
        }
        return false;
    }

    private void refreshSueccessView(YanxiuBaseBean result, boolean isCacheSuccess){
        isMoreData = pageIndex * pageSize < ((ParentHomeDetailBean) result).getPage().getTotalCou();
//        if (!isCacheSuccess) {
//        }
        pageIndex++;
        isFirstLoading = false;
    }

    private void cancelTask() {

        if (requestParentHomeTask != null) {
            requestParentHomeTask.cancel();
        }

    }



    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }




}
