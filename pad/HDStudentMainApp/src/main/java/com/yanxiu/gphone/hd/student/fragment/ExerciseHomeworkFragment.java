package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.YanxiuApplication;
import com.yanxiu.gphone.hd.student.activity.SubjectSectionActivity;
import com.yanxiu.gphone.hd.student.adapter.IntelliExeAdapter;
import com.yanxiu.gphone.hd.student.bean.EditionBean;
import com.yanxiu.gphone.hd.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.hd.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.hd.student.eventbusbean.SubjectEditionEventBusBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestEditionInfoTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestSubjectInfoTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;
import com.yanxiu.gphone.hd.student.view.stickhome.StickHomeLayout;

import de.greenrobot.event.EventBus;

/**
 * 首页（练习）
 * Created by Administrator on 2015/7/7.
 */
public class ExerciseHomeworkFragment extends TopBaseFragment{
    private static final String TAG=ExerciseHomeworkFragment.class.getSimpleName();
//    private ListView mGridView;
    private RelativeLayout noSubjectView;
    private TextView noSubjectTextView;
    private IntelliExeAdapter adapter;
//    private ImageView awardIcon;
    private int selectPosition = -1;

    private StickHomeLayout stickHomeLayout;
    private SubjectVersionBean.DataEntity dataEntity;
    private String name;
    private RequestSubjectInfoTask requestSubjectInfoTask;

    private HomePageFragment fg;
    private BaseFragment mCurFg;
    private RequestEditionInfoTask requestEditionInfoTask;
//    private TextView titleView;

    @Override
    protected IFgManager getFragmentManagerFromSubClass() {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass() {
        return 0;
    }


    @Override
    protected boolean isAttach() {
        return false;
    }


    protected void setRootView(){
        rootView.setBackgroundResource(R.drawable.exercise_bg);
    }



    @Override
    protected View getContentView() {
        EventBus.getDefault().register(this);
        mPublicLayout = PublicLoadUtils.createPage(getActivity(), R.layout.fragment_intelliexe);
        mPublicLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mPublicLayout.setContentBackground(android.R.color.transparent);
        initView();
        requestData();
        LogInfo.log("geny", "getContentView");
        mPublicLayout.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {
                requestData();
            }
        });
        return mPublicLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogInfo.log("geny", "onCreate");
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogInfo.log("geny", "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onEventMainThread(SubjectEditionEventBusBean subjectEditionEventBusBean){
        requestData();
    }


    @Override
    protected void initLoadData() {

    }

    @Override
    protected void setContentListener() {

    }

    @Override
    protected void destoryData() {
        cancelEditionInfoTask();
        cancelRequestSubjectInfoTask();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        noSubjectView=null;
        noSubjectTextView=null;
        adapter=null;
        if(stickHomeLayout!=null){
            stickHomeLayout.recycleView();
            stickHomeLayout.removeAllViews();
            stickHomeLayout=null;
        }
        if(mPublicLayout!=null){
            mPublicLayout.removeAllViews();
            mPublicLayout=null;
        }
        dataEntity=null;
        fg=null;
        mCurFg=null;
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        titleText.setText(getActivity().getResources().getString(R.string.exe));
        leftView.setVisibility(View.GONE);
    }

    private void initView() {
        noSubjectView = (RelativeLayout)mPublicLayout.findViewById(R.id.no_subject);
        noSubjectTextView = (TextView)mPublicLayout.findViewById(R.id.no_subject_text);

        stickHomeLayout = (StickHomeLayout) mPublicLayout.findViewById(R.id.stick_home_layout);
        adapter = new IntelliExeAdapter(this.getActivity());
        stickHomeLayout.setAdapter(adapter);
        stickHomeLayout.setOnItemClickListener(new StickHomeLayout.OnItemClickListener() {
            @Override
            public void onItemChangeListener(SubjectEditionBean.DataEntity selectedEntity) {
                LogInfo.log("geny", "HomeWorkFragment------onItemChangeListener");
                refreshData(selectedEntity);
            }

            @Override
            public void loading() {
                mPublicLayout.loading(true);
            }

            @Override
            public void finish() {
                mPublicLayout.finish();
            }

            @Override
            public void netError() {
                mPublicLayout.netError(true);
            }

            @Override
            public void dataNull(String str) {
                mPublicLayout.dataNull(str);
            }

            @Override
            public void dataNull(boolean isDataNull) {
                mPublicLayout.dataNull(isDataNull);
            }

            @Override
            public void selectionEntity(String name, SubjectVersionBean.DataEntity entity) {
                ExerciseHomeworkFragment.this.name = name;
                dataEntity = entity;
                requestSortData();
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        LogInfo.log("geny", "onStart");
    }

    @Override
    public void onResume() {
        LogInfo.log("geny", "onResume");
        if (adapter != null) {
            //我的里面公共资源
            if(YanxiuApplication.getInstance().getSubjectVersionBean() != null && !YanxiuApplication.getInstance().getSubjectVersionBean().getData().isEmpty()){
                adapter.setList(YanxiuApplication.getInstance().getSubjectVersionBean().getData());
            }
        }
        super.onResume();
    }

    private void requestData() {
        LogInfo.log("geny", " HomeWorkFragment  requestData");
        mPublicLayout.loading(true);
        cancelRequestSubjectInfoTask();
        requestSubjectInfoTask = new RequestSubjectInfoTask(getActivity(),
                LoginModel.getUserinfoEntity().getStageid(), new AsyncLocalCallBack() {
            @Override
            public void updateLocal(YanxiuBaseBean result) {
                mPublicLayout.finish();
                LogInfo.log("geny", " HomeWorkFragment  updateLocal");
                noSubjectView.setVisibility(View.GONE);
                SubjectVersionBean subjectVersionBean = (SubjectVersionBean) result;
                YanxiuApplication.getInstance().setSubjectVersionBean(subjectVersionBean);
                adapter.setList(subjectVersionBean.getData());
            }

            @Override
            public void update(YanxiuBaseBean result) {
                mPublicLayout.finish();
                LogInfo.log("geny", " HomeWorkFragment  update");
                SubjectVersionBean subjectVersionBean = (SubjectVersionBean) result;
                if(subjectVersionBean.getData()!=null && subjectVersionBean.getData().size()>0){
                    noSubjectView.setVisibility(View.GONE);
                    YanxiuApplication.getInstance().setSubjectVersionBean(subjectVersionBean);
                    adapter.setList(subjectVersionBean.getData());
                }else{
                    noSubjectView.setVisibility(View.VISIBLE);
                    noSubjectTextView.setText(subjectVersionBean.getStatus().getDesc());
                }
            }

            @Override
            public void dataError(int type, String msg) {
                LogInfo.log("geny", " HomeWorkFragment  dataError");
                mPublicLayout.finish();
                mPublicLayout.netError(true);
            }
        });
        requestSubjectInfoTask.start();
    }



    private void cancelRequestSubjectInfoTask(){
        if(requestSubjectInfoTask!=null){
            requestSubjectInfoTask.cancel();
        }
    }





    public void myRefreshData(SubjectEditionBean.DataEntity entity) {
        EditionBean editionBean = new EditionBean();
        editionBean.setEditionId(entity.getId());
        editionBean.setEditionName(entity.getName());
        adapter.getList().get(selectPosition).setData(editionBean);
        adapter.notifyDataSetChanged();

        SubjectSectionActivity.launch(ExerciseHomeworkFragment.this.getActivity(),
                ((SubjectVersionBean.DataEntity) adapter.getItem(selectPosition)).getName(),
                ((SubjectVersionBean.DataEntity) adapter.getItem(selectPosition)));
        LogInfo.log("geny", "refreshData entity" + selectPosition);
    }


    public void refreshData(SubjectEditionBean.DataEntity entity) {
        EditionBean editionBean = new EditionBean();
        editionBean.setEditionId(entity.getId());
        editionBean.setEditionName(entity.getName());
        selectPosition = stickHomeLayout.getSelectPosition();
        adapter.getList().get(selectPosition).setData(editionBean);
        adapter.notifyDataSetChanged();
        name = adapter.getItem(selectPosition).getName();
        dataEntity = adapter.getItem(selectPosition);
        requestSortData();

//        SubjectSectionActivity.launch(ExerciseHomeworkFragment.this.getActivity(),
//                adapter.getItem(selectPosition).getName(),
//                adapter.getItem(selectPosition));
        LogInfo.log("geny", "refreshData entity" + selectPosition);
    }


    private void requestSortData() {
        mPublicLayout.loading(true);
        cancelEditionInfoTask();
        requestEditionInfoTask = new RequestEditionInfoTask(this.getActivity(), LoginModel.getUserinfoEntity().getStageid() + "",
                dataEntity.getId(), new AsyncLocalCallBack() {
            @Override
            public void updateLocal(YanxiuBaseBean result) {
                mPublicLayout.finish();
                SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
                if (subjectEditionBean != null && subjectEditionBean.getData() != null) {
                    if (NetWorkTypeUtils.isNetAvailable()) {
                        Util.showToast(R.string.server_connection_erro);
                    } else {
                        LogInfo.log("geny", "updateLocal");
                        jumpSubjectSection(subjectEditionBean);
                    }
//                    PublicEditionBean.saveListFromSubjectEditionBean(subjectEditionBean.getData(), stageId + "", subjectId + "");
                }
            }

            @Override
            public void update(YanxiuBaseBean result) {
                mPublicLayout.finish();
                LogInfo.log("geny", "update");
                SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
                if (subjectEditionBean != null && subjectEditionBean.getData() != null) {
                    jumpSubjectSection(subjectEditionBean);
//                    PublicEditionBean.saveListFromSubjectEditionBean(subjectEditionBean.getData(), stageId + "", subjectId + "");
                } else {
                    Util.showToast(R.string.server_connection_erro);
                }
            }

            @Override
            public void dataError(int type, String msg) {
                mPublicLayout.finish();
                if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                    Util.showToast(R.string.public_loading_net_errtxt);
                } else {
                    Util.showToast(R.string.server_connection_erro);
                }
            }
        });
        requestEditionInfoTask.start();
    }


    private void cancelEditionInfoTask() {
        if (requestEditionInfoTask != null) {
            requestEditionInfoTask.cancel();
        }
        requestEditionInfoTask = null;
    }

    private void jumpSubjectSection(SubjectEditionBean subjectEditionBean){
        fg = ((HomePageFragment)this.getParentFragment());
        Bundle args = new Bundle();
        args.putString("title", name);
        args.putSerializable("entity", dataEntity);
        args.putSerializable("subjectEditionBean", subjectEditionBean);
        SubjectSectionFragment subjectSectionFragment = (SubjectSectionFragment) SubjectSectionFragment.newInstance();
        mCurFg= subjectSectionFragment;
        subjectSectionFragment.setArguments(args);
        fg.mIFgManager.addFragment(subjectSectionFragment, true, SubjectSectionFragment.class.getName());
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
        LogInfo.log("lee","onReset");
        destoryData();
    }
}
