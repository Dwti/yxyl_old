package com.yanxiu.gphone.student.fragment;

import android.text.TextUtils;

import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.PracticeHistoryBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestPracticeHistoryByKnowTask;
import com.yanxiu.gphone.student.utils.Util;

/**
 * Created by Administrator on 2015/11/3.
 */
public class TestCenterHistoryFragment extends AbsHistoryFragment{

    private int pageIndex = 1;
    private boolean isLoadMore = false;

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
        rootView.loading(true);
        requestPracticeHistoryByKnowTask(true, stageId, subjectId, pageIndex);
//        List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans = PublicVolumeBean.findDataSortList(stageId, subjectId, editionId);
//        if (mSortBeans != null) {
//            filterSort(mSortBeans);
//        } else {
//            topRightView.setVisibility(View.INVISIBLE);
//            rootView.loading(true);
//            cancelEditionInfoTask();
//            requestEditionInfoTask = new RequestEditionInfoTask(getActivity(), stageId,
//                    subjectId, new AsyncLocalCallBack() {
//                @Override
//                public void updateLocal(YanxiuBaseBean result) {
//                    SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
//                    if (subjectEditionBean != null && subjectEditionBean.getData() != null) {
//                        if(NetWorkTypeUtils.isNetAvailable()){
//                            rootView.netError(true);
//                        } else {
//                            filterData(subjectEditionBean);
//                        }
//                        PublicEditionBean.saveListFromSubjectEditionBean(subjectEditionBean.getData(), stageId + "", subjectId + "");
//                    }
//                }
//                @Override
//                public void update(YanxiuBaseBean result) {
//                    SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
//                    if (subjectEditionBean != null && subjectEditionBean.getData() != null) {
//                        filterData(subjectEditionBean);
//                        PublicEditionBean.saveListFromSubjectEditionBean(subjectEditionBean.getData(), stageId + "", subjectId + "");
//                    }else {
//                        String errMsg = subjectEditionBean.getStatus().getDesc();
//                        if(TextUtils.isEmpty(errMsg)) {
//                            rootView.dataError(true);
//                        } else {
//                            rootView.dataNull(errMsg);
//                        }
//                    }
//                }
//                @Override
//                public void dataError(int type, String errMsg) {
//                    if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
//                        rootView.netError(true);
//                    } else {
//                        if (TextUtils.isEmpty(errMsg)) {
//                            rootView.dataNull(true);
//                        } else {
//                            rootView.dataNull(errMsg);
//                        }
//                    }
//                }
//            });
//            requestEditionInfoTask.start();
//        }
    }

//    private RequestEditionInfoTask requestEditionInfoTask;
//
//    private void cancelEditionInfoTask() {
//        if (requestEditionInfoTask != null) {
//            requestEditionInfoTask.cancel();
//        }
//        requestEditionInfoTask = null;
//    }

//    private void filterData(SubjectEditionBean subjectEditionBean) {
//        int count = subjectEditionBean.getData().size();
//        SubjectEditionBean.DataEntity sortEntity = null;
//        boolean hasEditionId = false;
//        for (int i = 0; i < count; i++) {
//            sortEntity = subjectEditionBean.getData().get(i);
//            if (sortEntity.getId().equals(editionId)) {
//                LogInfo.log("geny", "ok-------" + sortEntity.getChildren().size());
//                hasEditionId = true;
//                break;
//            }
//        }
//        pageIndex = 1;
//
//        if (sortEntity != null && sortEntity.getChildren() != null && sortEntity.getChildren().size() != 0) {
//            if(hasEditionId) {
//                filterSort(sortEntity.getChildren());
//            } else {
//                volumeId = sortEntity.getChildren().get(0).getId();
//                volumeName = sortEntity.getChildren().get(0).getName();
//                topRightView.setVisibility(View.GONE);
//            }
//            LogInfo.log("geny", "ok-------volume" + volumeId + "----volumeName=" + volumeName);
//        }
//        if(!hasEditionId){
//            requestPracticeHistoryByKnowTask(true, stageId, subjectId, pageIndex);
//        }
//    }

//    private void filterSort(List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans) {
//        if (TextUtils.isEmpty(volumeId)) {
//            volumeId = mSortBeans.get(0).getId();
//            volumeName = mSortBeans.get(0).getName();
//        } else {
//            boolean hasVolumeId = false;
//            for (SubjectEditionBean.DataEntity.ChildrenEntity mBean : mSortBeans) {
//                if (volumeId.equals(mBean.getId())) {
//                    hasVolumeId = true;
//                    volumeId = mBean.getId();
//                    volumeName = mBean.getName();
//                    break;
//                }
//            }
//            if(!hasVolumeId){
//                volumeId = mSortBeans.get(0).getId();
//                volumeName = mSortBeans.get(0).getName();
//            }
//        }
//        pageIndex = 1;
//        requestPracticeHistoryByKnowTask(true, stageId, subjectId, pageIndex);
//    }

    private RequestPracticeHistoryByKnowTask mRequestPracticeHistoryByKnowTask;

    private void requestPracticeHistoryByKnowTask(boolean isLoading,String stageId, String subjectId,int nextPage) {
        if(isLoading){
            rootView.loading(true);
        }
        cancelPracticeHistoryByKnowTask();
        mRequestPracticeHistoryByKnowTask = new RequestPracticeHistoryByKnowTask(getActivity(), stageId, subjectId, nextPage, mAsyncCallBack);
        mRequestPracticeHistoryByKnowTask.start();
    }

    private void cancelPracticeHistoryByKnowTask() {
        if (mRequestPracticeHistoryByKnowTask != null) {
            mRequestPracticeHistoryByKnowTask.cancel();
        }
        mRequestPracticeHistoryByKnowTask = null;
    }

    private AsyncCallBack mAsyncCallBack = new AsyncCallBack() {
        @Override
        public void update(YanxiuBaseBean result) {
            rootView.finish();
            cacheTime = System.currentTimeMillis();
            xListView.setSuccRefreshTime(cacheTime);
            xListView.stopRefresh();
            xListView.stopLoadMore();
            if(isLoadMore){
                toLoadMore((PracticeHistoryBean)result);
            }else{
                updateView((PracticeHistoryBean) result);
            }
            isLoadMore = false;
        }
        @Override
        public void dataError(int type, String errMsg) {
            xListView.stopRefresh();
            xListView.stopLoadMore();
            if( adapter == null ) {
                if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                    rootView.netError(true);
                } else {
                    rootView.dataNull(1, R.drawable.public_no_qus_bg);
//                    if (TextUtils.isEmpty(errMsg)) {
//                        rootView.dataNull(true);
//                    } else {
//                        rootView.dataNull(errMsg);
//                    }
                }
            } else {
                if(TextUtils.isEmpty(errMsg)){
                    Util.showToast(R.string.no_net_tag);
                } else {
                   Util.showToast(errMsg);
                }
                rootView.finish();
            }
            isLoadMore = false;
        }
    };

    @Override
    protected void requestData() {
        pageIndex = 1;
        requestPracticeHistoryByKnowTask(true, stageId, subjectId, pageIndex);
    }

    @Override
    public void refreshData() {
        pageIndex = 1;
        requestPracticeHistoryByKnowTask(false, stageId, subjectId, pageIndex);
    }

    @Override
    protected void loadMoreData() {
        isLoadMore = true;
        requestPracticeHistoryByKnowTask(false, stageId, subjectId, pageIndex+1);
    }

    @Override
    protected void refreshOver(boolean success) {
        if(success){

        }else{

        }
    }

    @Override
    protected void loadMoreOver(boolean success) {
        if(success){
            pageIndex+=1;
        }else{

        }
    }
}
