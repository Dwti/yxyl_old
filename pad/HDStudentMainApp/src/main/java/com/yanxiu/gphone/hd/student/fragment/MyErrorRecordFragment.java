package com.yanxiu.gphone.hd.student.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.MistakeAllActivity;
import com.yanxiu.gphone.hd.student.adapter.MyErrorRecordAdapter;
import com.yanxiu.gphone.hd.student.bean.DataTeacherEntity;
import com.yanxiu.gphone.hd.student.bean.MistakeEditionBean;
import com.yanxiu.gphone.hd.student.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.hd.student.eventbusbean.ExerciseEventBusBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestMistakeEditionTask;
import com.yanxiu.gphone.hd.student.utils.Configuration;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

import de.greenrobot.event.EventBus;

/**
 * 我的错题
 * Created by Administrator on 2016/1/21.
 */
public class MyErrorRecordFragment extends TopBaseFragment{
    private MyErrorRecordContainerFragment fg;

    private MyErrorRecordAdapter adapter;
    private ListView mErrorRecordLv;
    private RequestMistakeEditionTask mRequestMistakeEditionTask;

    private BaseFragment mCurFg;



    public static Fragment newInstance(){

        return new MyErrorRecordFragment();
    }


    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        EventBus.getDefault().register(this);
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
    protected void setTopView() {
        super.setTopView();
        titleText.setText(R.string.error_collection_name);
        leftView.setVisibility(View.GONE);
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.blue_bg);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(LoginModel.getUserinfoEntity()!=null){
            requestMistakeEditionTask(LoginModel.getUserinfoEntity().getStageid() + "");
        }


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            onResume();
        }else{
            cancelMistakeEditionTask();
        }
    }

    private void requestMistakeEditionTask(final String stageId) {
        mPublicLayout.loading(true);
        cancelMistakeEditionTask();
        if (NetWorkTypeUtils.isNetAvailable()) {
            new YanxiuSimpleAsyncTask<MistakeEditionBean>(getActivity()) {
                @Override
                public MistakeEditionBean doInBackground() {
                    MistakeEditionBean mBean = null;
                    try {
                        mBean = new MistakeEditionBean();
                        mBean.setData(PublicErrorQuestionCollectionBean.findDataListToSubjectEntity(stageId));
                    } catch (Exception e) {
                    }
                    return mBean;
                }
                @Override
                public void onPostExecute(MistakeEditionBean result) {
                    mPublicLayout.finish();
                    if (result != null && result.getData() != null) {
                        updateMistakeEditionView(result);
                    } else {
                        if(adapter == null || adapter.getCount() <= 0){
                            mPublicLayout.dataNull(true);
                        }
                    }
                }
            }.start();
        } else {
            mRequestMistakeEditionTask = new RequestMistakeEditionTask(getActivity(),stageId, mAsyncLocalCallBack);
            mRequestMistakeEditionTask.start();
        }
    }

    private AsyncLocalCallBack mAsyncLocalCallBack = new AsyncLocalCallBack() {
        @Override
        public void updateLocal(YanxiuBaseBean result) {
            MistakeEditionBean mMistakeEditionBean = (MistakeEditionBean) result;
            if (mMistakeEditionBean.getData() != null && mMistakeEditionBean.getData().size() > 0) {
                mPublicLayout.finish();
                updateMistakeEditionView((MistakeEditionBean) result);
            }
        }

        @Override
        public void update(YanxiuBaseBean result) {
            mPublicLayout.finish();
            MistakeEditionBean mMistakeEditionBean = (MistakeEditionBean) result;
            if (mMistakeEditionBean.getData() == null || mMistakeEditionBean.getData().size() <= 0) {
                if (TextUtils.isEmpty(mMistakeEditionBean.getStatus().getDesc())) {
                    mPublicLayout.dataNull(true);
                } else {
                    mPublicLayout.dataNull(mMistakeEditionBean.getStatus().getDesc());
                }
                adapter.getList().clear();
                adapter.notifyDataSetChanged();
            } else {
                updateMistakeEditionView((MistakeEditionBean) result);
            }
        }

        @Override
        public void dataError(int type, String msg) {
            LogInfo.log("haitian", "type=" + type + "---msg=" + msg);
            if(adapter != null){
                if(adapter.getList() != null){
                    adapter.getList().clear();
                }
                adapter.notifyDataSetChanged();
            }
            mPublicLayout.finish();
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
    };

    private void updateMistakeEditionView(MistakeEditionBean mMistakeEditionBean) {
        if (mMistakeEditionBean.getData() != null) {
            if (adapter.getCount() <= 0) {
                adapter.setList(mMistakeEditionBean.getData());
                mErrorRecordLv.setAdapter(adapter);
            } else {
                adapter.setList(mMistakeEditionBean.getData());
            }
        } else {
            mPublicLayout.dataNull(true);
        }
    }

    private void cancelMistakeEditionTask() {
        if (mRequestMistakeEditionTask != null) {
            mRequestMistakeEditionTask.cancel();
        }
        mRequestMistakeEditionTask = null;
    }


    private void findView() {
        mErrorRecordLv = (ListView) mPublicLayout.findViewById(R.id.material_list);
        adapter=new MyErrorRecordAdapter(getActivity());
    }

    @Override
    protected void initLoadData() {

    }

    @Override
    protected void setContentListener() {
        mPublicLayout.setmRefreshData(
                new PublicLoadLayout.RefreshData() {
                    @Override
                    public void refreshData() {
                        if(LoginModel.getUserinfoEntity()==null){
                            return;
                        }
                        requestMistakeEditionTask(LoginModel.getUserinfoEntity().getStageid() + "");
                    }
                }
        );
        mErrorRecordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fg = ((MyErrorRecordContainerFragment)MyErrorRecordFragment.this.getParentFragment());
                DataTeacherEntity mDataTeacherEntity = (DataTeacherEntity) adapter.getList().get(position);
                //Fragment mistakeSectionFragment;
                Fragment mistakeAllFragment;
                if (mDataTeacherEntity.getChildren() != null && !mDataTeacherEntity.getChildren().isEmpty()) {
                    //mistakeSectionFragment = MistakeSectionFragment.newInstance(mDataTeacherEntity.getName(), mDataTeacherEntity.getId() + "",
                            //mDataTeacherEntity.getData().getEditionId() + "", mDataTeacherEntity.getChildren(), mDataTeacherEntity.getData().getHas_knp());
                    MistakeAllActivity.launch(getActivity(), mDataTeacherEntity.getName(), mDataTeacherEntity.getId() + "", mDataTeacherEntity.getData().getWrongNum()
                           );
                } else {
                    //mistakeSectionFragment = MistakeSectionFragment.newInstance(mDataTeacherEntity.getName(), mDataTeacherEntity.getId() + "",
                            //mDataTeacherEntity.getData().getEditionId() + "", null, mDataTeacherEntity.getData().getHas_knp());
                    MistakeAllActivity.launch(getActivity(), mDataTeacherEntity.getName(), mDataTeacherEntity.getId() + "", mDataTeacherEntity.getData().getWrongNum()
                    );
                }
                //mCurFg = (BaseFragment) mistakeSectionFragment;
                //fg.mIFgManager.addFragment(mistakeSectionFragment, true, MistakeSectionFragment.class.getName());
                //mCurFg = (BaseFragment) mistakeAllFragment;
                //fg.mIFgManager.addFragment(mistakeAllFragment, true, MistakeAllFragment.class.getName());
            }
        });
    }

    @Override
    protected void destoryData() {
        cancelMistakeEditionTask();
    }
    public void onEventMainThread(ExerciseEventBusBean mExerciseEventBusBean){
        fg.mIFgManager.popBackStackImmediate(MistakeSectionFragment.class.getName(),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if(LoginModel.getUserinfoEntity()==null){
            return;
        }
        requestMistakeEditionTask(LoginModel.getUserinfoEntity().getStageid() + "");
    }
    @Override
    public void onDestroy() {
        cancelMistakeEditionTask();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        fg=null;
        mCurFg=null;
        adapter=null;
        mErrorRecordLv=null;
        mRequestMistakeEditionTask=null;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogInfo.log("geny", "ExerciseHomeworkFragment-----onKeyDown");
        if(mCurFg!=null){
            return mCurFg.onKeyDown(keyCode,event);
        }else{
            return super.onKeyDown(keyCode,event);
        }
    }

    @Override
    public void onReset() {
        destoryData();
    }
}
