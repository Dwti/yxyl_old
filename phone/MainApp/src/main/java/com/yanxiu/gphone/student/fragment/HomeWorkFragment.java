package com.yanxiu.gphone.student.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.example.settingproblemssystem.activity.SettingProbActivity;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.activity.SubjectSectionActivity;
import com.yanxiu.gphone.student.activity.TeachingMaterialActivity;
import com.yanxiu.gphone.student.adapter.IntelliExeAdapter;
import com.yanxiu.gphone.student.bean.EditionBean;
import com.yanxiu.gphone.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.student.requestTask.RequestSubjectInfoTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.UpdataUtils;
import com.yanxiu.gphone.student.view.PublicLoadLayout;
import com.yanxiu.gphone.student.view.stickhome.StickHomeLayout;

/**
 * Created by Administrator on 2015/7/7.
 */
 public class HomeWorkFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout noSubjectView;
    private TextView noSubjectTextView;
    private PublicLoadLayout rootView;
    private IntelliExeAdapter adapter;
    private ImageView awardIcon;
    private ImageView historyIcon;
    private int selectPosition = -1;

    private StickHomeLayout stickHomeLayout;

    private RequestSubjectInfoTask requestSubjectInfoTask;

    private TextView titleView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = PublicLoadUtils.createPage(getActivity(), R.layout.fragment_intelliexe);
        initView();
        requestData();
        LogInfo.log("geny", "onCreateView");
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {
                requestData();
            }
        });
        return rootView;
    }

    private void initView() {
        titleView = (TextView) rootView.findViewById(R.id.main_public_top_intelliexe).findViewById(R.id
                .public_layout_top_tv);
        titleView.setText(R.string.exe);
        awardIcon = (ImageView) rootView.findViewById(R.id.main_public_top_intelliexe).findViewById(R.id
                .public_layout_top_iv);
        awardIcon.setBackgroundResource(R.drawable.award_sel);
        awardIcon.setVisibility(View.VISIBLE);
        awardIcon.setOnClickListener(this);
        historyIcon = (ImageView) rootView.findViewById(R.id
                .main_public_top_intelliexe).findViewById(R.id
                .public_layout_history_iv);
        historyIcon.setVisibility(View.VISIBLE);
        historyIcon.setOnClickListener(this);
        noSubjectView = (RelativeLayout)rootView.findViewById(R.id.no_subject);
        noSubjectTextView = (TextView)rootView.findViewById(R.id.no_subject_text);

        stickHomeLayout = (StickHomeLayout) rootView.findViewById(R.id.stick_home_layout);
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
                rootView.loading(true);
            }

            @Override
            public void finish() {
                rootView.finish();
            }

            @Override
            public void netError() {
                rootView.netError(true);
            }

            @Override
            public void dataNull(String str) {
                rootView.dataNull(str);
            }

            @Override
            public void dataNull(boolean isDataNull) {
                rootView.dataNull(isDataNull);
            }
        });

        if (YanxiuHttpApi.mUrlBean!=null&&!YanxiuHttpApi.mUrlBean.getMode().equals("release")) {
            TextView system_redo = (TextView) rootView.findViewById(R.id.system_redo);
            system_redo.setVisibility(View.VISIBLE);
            system_redo.setOnClickListener(this);
        }

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
            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    private void requestData() {
        if (LoginModel.getUserinfoEntity()==null){
            return;
        }
        rootView.loading(true);
        requestSubjectInfoTask = new RequestSubjectInfoTask(getActivity(),
                LoginModel.getUserinfoEntity().getStageid(), new AsyncLocalCallBack() {
            @Override
            public void updateLocal(YanxiuBaseBean result) {
                rootView.finish();
                LogInfo.log("geny", " HomeWorkFragment  updateLocal");
                noSubjectView.setVisibility(View.GONE);
                SubjectVersionBean subjectVersionBean = (SubjectVersionBean) result;
                YanxiuApplication.getInstance().setSubjectVersionBean(subjectVersionBean);
//                PublicSubjectBean.saveListFromSubjectVersionBean(subjectVersionBean.getData(), LoginModel.getUserInfo().getStageid()+"");
                adapter.setList(subjectVersionBean.getData());
            }

            @Override
            public void update(YanxiuBaseBean result) {
                rootView.finish();
                LogInfo.log("geny", " HomeWorkFragment  update");
                SubjectVersionBean subjectVersionBean = (SubjectVersionBean) result;
                if(subjectVersionBean.getData()!=null && subjectVersionBean.getData().size()>0){
                    noSubjectView.setVisibility(View.GONE);
                    YanxiuApplication.getInstance().setSubjectVersionBean(subjectVersionBean);
//                    PublicSubjectBean.saveListFromSubjectVersionBean(subjectVersionBean.getData(), LoginModel.getUserInfo().getStageid() + "");
                    adapter.setList(subjectVersionBean.getData());
                }else{
                    noSubjectView.setVisibility(View.VISIBLE);
                    noSubjectTextView.setText(subjectVersionBean.getStatus().getDesc());
                }
            }

            @Override
            public void dataError(int type, String msg) {
                LogInfo.log("geny", " HomeWorkFragment  dataError");
                rootView.finish();
                rootView.netError(true);
            }
        });
        requestSubjectInfoTask.start();
    }

    public void refreshData(SubjectEditionBean.DataEntity entity) {
        EditionBean editionBean = new EditionBean();
        editionBean.setEditionId(entity.getId());
        editionBean.setEditionName(entity.getName());
        selectPosition = stickHomeLayout.getSelectPosition();
        adapter.getList().get(selectPosition).setData(editionBean);
        adapter.notifyDataSetChanged();

        SubjectSectionActivity.launch(HomeWorkFragment.this.getActivity(),
                adapter.getItem(selectPosition).getName(),
                adapter.getItem(selectPosition));
        LogInfo.log("geny", "refreshData entity" + selectPosition);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.public_layout_top_iv:
                ActivityJumpUtils.jumpToRankingListActivity(getActivity());
                break;
            case R.id.public_layout_history_iv:
                TeachingMaterialActivity.launchActivity(getActivity(), TeachingMaterialActivity.PRACTICE_HISTORY_ACTIVITY);
                break;
            case R.id.system_redo:
                SettingProbActivity.lunch(getActivity());
                break;
        }
    }

}
