package com.yanxiu.gphone.parent.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.adapter.HonorAdapter;
import com.yanxiu.gphone.parent.bean.ParentHonorBean;
import com.yanxiu.gphone.parent.bean.ParentItemHonorBean;
import com.yanxiu.gphone.parent.eventbus.bean.HonorRedFlagEventBean;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.requestTask.RequestHonorListTask;
import com.yanxiu.gphone.parent.utils.PublicLoadUtils;
import com.yanxiu.gphone.parent.view.PublicLoadLayout;
import com.yanxiu.gphone.parent.view.RecycleItemDecoration;

import de.greenrobot.event.EventBus;

/**
 * Created by hai8108 on 16/3/17.
 */
public class ParentHonorFragment extends Fragment {
    private RecyclerView mRecycleView;
    private HonorAdapter mAdapter;
    private RequestHonorListTask mTask;
    private PublicLoadLayout mPublicLy;
    private boolean isFirseLoad=true;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPublicLy= PublicLoadUtils.createPage(getActivity(), R.layout.parent_honor_fg_layout);
        initView();
    }

    private void initView() {
        View leftView=mPublicLy.findViewById(R.id.pub_top_left);
        leftView.setVisibility(View.GONE);
        TextView titText= (TextView) mPublicLy.findViewById(R.id.pub_top_mid);
        titText.setText(getResources().getString(R.string.honor_title));
        mRecycleView=(RecyclerView)mPublicLy.findViewById(R.id.honorList);
        mAdapter=new HonorAdapter(getActivity());
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_p_20);
        mRecycleView.addItemDecoration(new RecycleItemDecoration(spacingInPixels));
        mPublicLy.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {
                requestToServerForHonorList();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            requestToServerForHonorList();
        }else{
            mPublicLy.finish();
            cancelTask();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestToServerForHonorList();
    }

    private void requestToServerForHonorList(){
        cancelTask();
        mPublicLy.loading(true);
        mTask=new RequestHonorListTask(getActivity(), new AsyncCallBack() {

            @Override
            public void update(YanxiuBaseBean result) {
                    mPublicLy.finish();
                    updateUI(result);
                    isFirseLoad=false;
            }

            @Override
            public void dataError(int type, String msg) {
                     mPublicLy.finish();
                     mPublicLy.showErrorWithFlag(type,msg,isFirseLoad);
            }
        });
        mTask.start();
    }

    private void updateUI(YanxiuBaseBean result) {
        ParentHonorBean bean= (ParentHonorBean) result;
        if(bean.getData()!=null&&bean.getData().size()>0){
            mAdapter.setList(bean.getData());
            StringBuilder sb = new StringBuilder();
            for (ParentItemHonorBean mDataBean : bean.getData()){
                if(!TextUtils.isEmpty(mDataBean.getId())){
                    sb.append(mDataBean.getId()+",");
                }
            }
            HonorRedFlagEventBean mHonorRedFlagEventBean = new HonorRedFlagEventBean();
            String ids = sb.toString();
            if(!TextUtils.isEmpty(ids) && ids.length() > 1) {
                mHonorRedFlagEventBean.setRedIds(ids.substring(0, ids.length() - 1));
                LogInfo.log("haitian", "ids=" + mHonorRedFlagEventBean.getRedIds());
                EventBus.getDefault().post(mHonorRedFlagEventBean);
            }
        }

    }

    private void cancelTask(){
        if(mTask!=null){
            mTask.cancel();
            mTask=null;
        }
    }
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mPublicLy;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }




}
