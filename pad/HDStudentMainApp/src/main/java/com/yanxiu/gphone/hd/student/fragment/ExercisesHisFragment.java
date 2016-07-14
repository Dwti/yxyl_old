package com.yanxiu.gphone.hd.student.fragment;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.adapter.ExerciseHisAdapter;
import com.yanxiu.gphone.hd.student.bean.DataTeacherEntity;
import com.yanxiu.gphone.hd.student.bean.MistakeEditionBean;
import com.yanxiu.gphone.hd.student.eventbusbean.ExerciseEventBusBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestPracticeEditionTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

import de.greenrobot.event.EventBus;

/**
 * 练习历史
 * Created by Administrator on 2016/1/21.
 */
public class ExercisesHisFragment extends TopBaseFragment {
    private ExercisesContainerHisFragment fg;
    private ListView exiseHisLv;
    private ExerciseHisAdapter adapter;
    private RequestPracticeEditionTask mRequestPracticeEditionTask;
    private MistakeEditionBean mMistakeEditionBean;
    public static Fragment newInstance(){

        return new ExercisesHisFragment();
    }


    private BaseFragment mCurFg;

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
//        if(Configuration.isAnalyLayout()){
//            Util.toDispScalpelFrameLayout(getActivity(), R.layout.activity_material_teaching_layout);
//        }
        findView();
        return mPublicLayout;
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        titleText.setText(R.string.practice_history_name);
        leftView.setVisibility(View.GONE);
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.blue_bg);
    }


    private void findView() {
        exiseHisLv = (ListView) mPublicLayout.findViewById(R.id.material_list);
        adapter=new ExerciseHisAdapter(getActivity());
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
                        if(LoginModel.getUserinfoEntity()!=null){
                            requestPracticeEditionTask(LoginModel.getUserinfoEntity().getStageid() + "");
                        }
                    }
                }
        );
        exiseHisLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fg = ((ExercisesContainerHisFragment) ExercisesHisFragment.this.getParentFragment());
                DataTeacherEntity mDataTeacherEntity = (DataTeacherEntity) adapter.getList().get(position);
                Fragment practiceHistorySectionFragment;
                practiceHistorySectionFragment = PracticeHistorySectionFragment.newInstance(mDataTeacherEntity.getName(), mDataTeacherEntity.getId() + "",
                        mDataTeacherEntity.getData().getEditionId() + "", mDataTeacherEntity.getData().getHas_knp());
                mCurFg = (BaseFragment) practiceHistorySectionFragment;
                fg.mIFgManager.addFragment(practiceHistorySectionFragment, true, PracticeHistorySectionFragment.class.getName());

            }
        });
    }

    private void requestPracticeEditionTask(String stageId) {
        cancelPracticeEditionTask();
        mPublicLayout.loading(true);
        mRequestPracticeEditionTask = new RequestPracticeEditionTask(getActivity(), stageId, mAsyncCallBack);
        mRequestPracticeEditionTask.start();
    }

    private AsyncCallBack mAsyncCallBack = new AsyncCallBack() {
        @Override
        public void update(YanxiuBaseBean result) {
            mPublicLayout.finish();
            mMistakeEditionBean= (MistakeEditionBean) result;
            if (mMistakeEditionBean.getData() == null || mMistakeEditionBean.getData().size() <= 0) {

                if (TextUtils.isEmpty(mMistakeEditionBean.getStatus().getDesc())) {
                    mPublicLayout.dataNull(true);
                } else {
                    mPublicLayout.dataNull(mMistakeEditionBean.getStatus().getDesc());
                }
            } else {
                updateMistakeEditionView((MistakeEditionBean) result);
            }
        }

        @Override
        public void dataError(int type, String msg) {
            LogInfo.log("haitian", "type=" + type + "---msg=" + msg);

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

    private void clearData() {
        if (mMistakeEditionBean != null) {
            if(mMistakeEditionBean.getData() != null) {
                mMistakeEditionBean.getData().clear();
            }
            updateMistakeEditionView(mMistakeEditionBean);
        }
    }
    private void updateMistakeEditionView(MistakeEditionBean mMistakeEditionBean) {
        if (mMistakeEditionBean.getData() != null) {
            if (adapter.getCount() <= 0) {
                adapter.setList(mMistakeEditionBean.getData());
                exiseHisLv.setAdapter(adapter);
            } else {
                adapter.setList(mMistakeEditionBean.getData());
            }
        } else {
            mPublicLayout.dataNull(true);
        }
    }




    private void cancelPracticeEditionTask() {
        if (mRequestPracticeEditionTask != null) {
            mRequestPracticeEditionTask.cancel();
        }
        mRequestPracticeEditionTask = null;
    }

    @Override
    protected void destoryData() {
        cancelPracticeEditionTask();
    }

    @Override
    public void onDestroy() {
        cancelPracticeEditionTask();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        fg=null;
        exiseHisLv=null;
        adapter=null;
        mRequestPracticeEditionTask=null;
        mMistakeEditionBean=null;
        mCurFg=null;
    }


    public void onEventMainThread(ExerciseEventBusBean mExerciseEventBusBean){
        fg.mIFgManager.popBackStackImmediate(MistakeSectionFragment.class.getName(),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    @Override
    public void onResume() {
        super.onResume();
        clearData();
        if(LoginModel.getUserinfoEntity()!=null){
            requestPracticeEditionTask(LoginModel.getUserinfoEntity().getStageid() + "");
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            onResume();
        }else{
            cancelPracticeEditionTask();
        }
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
