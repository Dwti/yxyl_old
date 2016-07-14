package com.yanxiu.gphone.hd.student.fragment;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.YanxiuApplication;
import com.yanxiu.gphone.hd.student.adapter.TeachingMaterialAdapter;
import com.yanxiu.gphone.hd.student.bean.EditionBean;
import com.yanxiu.gphone.hd.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.hd.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.hd.student.eventbusbean.SubjectEditionEventBusBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestSubjectInfoTask;
import com.yanxiu.gphone.hd.student.utils.Configuration;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 教材版本
 * Created by Administrator on 2016/2/14.
 */
public class TeachingMaterialFragment extends TopBaseFragment {
    private static final String TAG=TeachingMaterialFragment.class.getSimpleName();
    private RequestSubjectInfoTask mRequestSubjectInfoTask;
    private TeachingMaterialAdapter adapter;
    private int selectPosition = -1;
    private ListView materialList;
    private Handler mHandler=new Handler();
    private SetContainerFragment mFg;
    private static TeachingMaterialFragment teachMaterFg;

    public static Fragment newInstance(){
        if(teachMaterFg==null){
            teachMaterFg=new TeachingMaterialFragment();
        }
        return teachMaterFg;
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        EventBus.getDefault().register(this);
        mFg= (SetContainerFragment) getParentFragment();
        mPublicLayout = PublicLoadUtils.createPage(getActivity(), R.layout.activity_material_teaching_layout);
        mPublicLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mPublicLayout.setContentBackground(android.R.color.transparent);
        if(Configuration.isAnalyLayout()){
            Util.toDispScalpelFrameLayout(getActivity(), R.layout.activity_material_teaching_layout);
        }
        findView();
        return mPublicLayout;
    }

    @Override
    protected void initLoadData() {
        requestSubjectInfoTask();
    }

    private void findView() {
        materialList = (ListView) mPublicLayout.findViewById(R.id.material_list);
        adapter = new TeachingMaterialAdapter(getActivity());
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        titleText.setText(R.string.teaching_material_name);
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.wood_bg);
    }

    @Override
    protected void setContentListener() {
        mPublicLayout.setmRefreshData(
                new PublicLoadLayout.RefreshData() {
                    @Override
                    public void refreshData() {
                        requestSubjectInfoTask();
                    }
                }
        );
        materialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!CommonCoreUtil.checkClickEvent(800)) {
                    return;
                }
                SubjectVersionBean.DataEntity mDataEntity = (SubjectVersionBean.DataEntity) adapter.getItem(position);
                if (mDataEntity != null) {
                    LogInfo.log(TAG, "xxx---if=" + mDataEntity.getName());
                    selectPosition = position;
                    if (LoginModel.getUserinfoEntity() != null) {
                        mFg.mIFgManager.addFragment(SubjectVersionFragment.newInstance(mDataEntity.getName(), LoginModel.getUserinfoEntity().getStageid(), mDataEntity.getId(), mDataEntity.getData() == null ? null : mDataEntity.getData().getEditionId()), true);
                    }

                }
            }
        });
    }

    private void requestSubjectInfoTask() {
        cancelSubjectInfoTask();
        mPublicLayout.loading(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LoginModel.getUserinfoEntity() == null) {
                    finish();
                    return;
                }
                mRequestSubjectInfoTask = new RequestSubjectInfoTask(getActivity(),
                        LoginModel.getUserinfoEntity().getStageid(), new AsyncLocalCallBack() {
                    @Override
                    public void updateLocal(YanxiuBaseBean result) {
//                mRootView.finish();
//                SubjectVersionBean subjectVersionBean = (SubjectVersionBean) result;
//                YanxiuApplication.getInstance().setSubjectVersionBean(subjectVersionBean);
//                PublicSubjectBean.saveListFromSubjectVersionBean(subjectVersionBean.getData(), LoginModel.getUserInfo().getStageid() + "");
//                updateSubjectVersionView(subjectVersionBean.getData());
                    }

                    @Override
                    public void update(YanxiuBaseBean result) {
                        mPublicLayout.finish();
                        SubjectVersionBean subjectVersionBean = (SubjectVersionBean) result;
                        YanxiuApplication.getInstance().setSubjectVersionBean(subjectVersionBean);
                        if (subjectVersionBean.getData() != null && subjectVersionBean.getData().size() > 0) {
//                    PublicSubjectBean.saveListFromSubjectVersionBean(subjectVersionBean.getData(), LoginModel.getUserInfo().getStageid() + "");
                            updateSubjectVersionView(subjectVersionBean.getData());
                        } else {
                            if (TextUtils.isEmpty(subjectVersionBean.getStatus().getDesc())) {
                                mPublicLayout.dataError(true);
                            } else {
                                mPublicLayout.dataNull(subjectVersionBean.getStatus().getDesc());
                            }
                        }
                    }

                    @Override
                    public void dataError(int type, String msg) {
                        if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                            mPublicLayout.netError(true);
                        } else {
                            if (TextUtils.isEmpty(msg)) {
                                mPublicLayout.dataNull(true);
                            } else {
                                mPublicLayout.dataNull(msg);
                            }
                        }
                    }
                });
                mRequestSubjectInfoTask.start();
            }
        }, 1000);

    }

    public void onEventMainThread(SubjectEditionEventBusBean subjectEditionEventBusBean){
        SubjectEditionBean.DataEntity dataBean = subjectEditionEventBusBean.getSelectedEntity();
        if (dataBean != null) {
            refreshData(dataBean);
        }
    }

    private void refreshData(SubjectEditionBean.DataEntity entity) {
        EditionBean editionBean = new EditionBean();
        editionBean.setEditionId(entity.getId());
        editionBean.setEditionName(entity.getName());
        SubjectVersionBean.DataEntity mDataEntity = (SubjectVersionBean.DataEntity) adapter.getList().get(selectPosition);
        mDataEntity.setData(editionBean);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
//        RequestSubjectInfoTask.savaCacheData(JSON.toJSONString(subjectVersionBean));
    }




    private void updateSubjectVersionView(List<SubjectVersionBean.DataEntity> dataList) {
        if (dataList != null && dataList.size() > 0) {
            if (adapter.getCount() <= 0) {
                adapter.setList(dataList);
                materialList.setAdapter(adapter);
            } else {
                adapter.setList(dataList);
            }
        } else {
            mPublicLayout.dataNull(true);
        }
    }



    private void cancelSubjectInfoTask() {
        if (mRequestSubjectInfoTask != null) {
            mRequestSubjectInfoTask.cancel();
        }
        mRequestSubjectInfoTask = null;
    }


    @Override
    protected void destoryData() {
        EventBus.getDefault().unregister(this);
        if(mPublicLayout!=null){
            mPublicLayout.finish();
            mPublicLayout=null;
        }

        cancelSubjectInfoTask();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRequestSubjectInfoTask=null;
        adapter=null;
        materialList=null;
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler=null;
        }

        mFg=null;

    }

    private void finish() {
        if(mFg!=null&&mFg.mIFgManager!=null){
            mFg.mIFgManager.popStack();
        }
        teachMaterFg=null;
    }

    @Override
    protected IFgManager getFragmentManagerFromSubClass() {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass() {
        return 0;
    }


    @Override
    public void onReset() {
        destoryData();
    }
}
