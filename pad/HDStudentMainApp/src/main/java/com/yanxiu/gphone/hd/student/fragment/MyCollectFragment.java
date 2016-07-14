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
import com.yanxiu.gphone.hd.student.adapter.MyCollectAdapter;
import com.yanxiu.gphone.hd.student.bean.DataTeacherEntity;
import com.yanxiu.gphone.hd.student.bean.MistakeEditionBean;
import com.yanxiu.gphone.hd.student.bean.PublicFavouriteQuestionBean;
import com.yanxiu.gphone.hd.student.eventbusbean.ExerciseEventBusBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestFavouriteEditionTask;
import com.yanxiu.gphone.hd.student.utils.Configuration;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

import de.greenrobot.event.EventBus;

/**我的收藏
 * Created by Administrator on 2016/1/21.
 */
public class MyCollectFragment extends TopBaseFragment {
    private MyCollectContainerFragment fg;
    private static final String TAG=MyCollectFragment.class.getSimpleName();
    private ListView collectLv;
    private MyCollectAdapter adapter;
    private RequestFavouriteEditionTask mRequestFavouriteEditionTask;

    private BaseFragment mCurFg;


    public static Fragment newInstance(){

        return new MyCollectFragment();
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
        titleText.setText(R.string.my_favourite_name);
        leftView.setVisibility(View.GONE);
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.blue_bg);
    }


    private void findView() {
        collectLv = (ListView) mPublicLayout.findViewById(R.id.material_list);
        adapter=new MyCollectAdapter(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        LogInfo.log(TAG, "refresh");
        if(LoginModel.getUserinfoEntity()!=null){
            requestMyFavouriteTask(LoginModel.getUserinfoEntity().getStageid() + "");
        }

    }

    private void requestMyFavouriteTask(final String stageId) {
        mPublicLayout.loading(true);
        cancelMyFavouriteTask();
        if (NetWorkTypeUtils.isNetAvailable()) {
            new YanxiuSimpleAsyncTask<MistakeEditionBean>(getActivity()) {
                @Override
                public MistakeEditionBean doInBackground() {
                    MistakeEditionBean mBean = null;
                    try {
                        mBean = new MistakeEditionBean();
                        mBean.setData(PublicFavouriteQuestionBean.findDataListToSubjectEntity(stageId));
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
            mRequestFavouriteEditionTask = new RequestFavouriteEditionTask(getActivity(), stageId, mAsyncLocalCallBack);
            mRequestFavouriteEditionTask.start();
        }
    }
    private void cancelMyFavouriteTask() {
        if (mRequestFavouriteEditionTask != null) {
            mRequestFavouriteEditionTask.cancel();
        }
        mRequestFavouriteEditionTask = null;
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
                collectLv.setAdapter(adapter);
            } else {
                adapter.setList(mMistakeEditionBean.getData());
            }
        } else {
            mPublicLayout.dataNull(true);
        }
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
                            requestMyFavouriteTask(LoginModel.getUserinfoEntity().getStageid() + "");
                        }

                    }
                }
        );

        collectLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                fg = ((MyCollectContainerFragment)MyCollectFragment.this.getParentFragment());
                DataTeacherEntity mDataTeacherEntity = (DataTeacherEntity) adapter.getList().get(position);
                Fragment favouriteSectionFragment;
                if (mDataTeacherEntity.getChildren() != null && !mDataTeacherEntity.getChildren().isEmpty()) {
                    favouriteSectionFragment = FavouriteSectionFragment.newInstance(mDataTeacherEntity.getName(), mDataTeacherEntity.getId() + "",
                            mDataTeacherEntity.getData().getEditionId() + "", mDataTeacherEntity.getChildren(), mDataTeacherEntity.getData().getHas_knp());

                } else {
                    favouriteSectionFragment = FavouriteSectionFragment.newInstance(mDataTeacherEntity.getName(), mDataTeacherEntity.getId() + "",
                            mDataTeacherEntity.getData().getEditionId() + "", null, mDataTeacherEntity.getData().getHas_knp());
                }

                mCurFg = (BaseFragment) favouriteSectionFragment;
                fg.mIFgManager.addFragment(favouriteSectionFragment, true, FavouriteSectionFragment.class.getName());
            }
        });
    }
    public void onEventMainThread(ExerciseEventBusBean mExerciseEventBusBean){
        fg.mIFgManager.popBackStackImmediate(FavouriteSectionFragment.class.getName(),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if(LoginModel.getUserinfoEntity()!=null){
            requestMyFavouriteTask(LoginModel.getUserinfoEntity().getStageid() + "");
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogInfo.log(TAG, "hidden: "+hidden);
        if(!hidden){
            onResume();
        }else{
            cancelMyFavouriteTask();
        }
    }

    @Override
    public void onDestroy() {
        cancelMyFavouriteTask();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        fg=null;
        collectLv=null;
        adapter=null;
        mCurFg=null;
    }

    @Override
    protected void destoryData() {

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
       onDestroy();
    }
}
