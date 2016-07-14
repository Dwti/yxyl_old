package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.adapter.SubjectVersionMyModuleAdapter;
import com.yanxiu.gphone.hd.student.bean.DataStatusEntityBean;
import com.yanxiu.gphone.hd.student.bean.PublicEditionBean;
import com.yanxiu.gphone.hd.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.hd.student.eventbusbean.SubjectEditionEventBusBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestEditionInfoTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestSaveEditionInfoTask;
import com.yanxiu.gphone.hd.student.utils.Configuration;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/2/23.
 */
public class SubjectVersionFragment extends TopBaseFragment implements View.OnClickListener  {
    private SetContainerFragment mFg;

    private ListView mListView;
    private SubjectVersionMyModuleAdapter mAdapter;
//    private TextView ivBack;
//    private TextView tvSave;
    private String title;
    private int stageId;
    private String subjectId;
    private String editionId;
    private ArrayList<SubjectEditionBean.DataEntity> dataEntity;
    private SubjectEditionBean.DataEntity selectedEntity;
    private RequestEditionInfoTask requestEditionInfoTask;
    private RequestSaveEditionInfoTask requestSaveEditionInfoTask;


    public static Fragment newInstance(String title, int stageId, String subjectId, String editionId){
        SubjectVersionFragment subjectVersionFragment = new SubjectVersionFragment();
        Bundle bundle=new Bundle();
        bundle.putString("title", title);
        bundle.putInt("stageId", stageId);
        bundle.putString("subjectId", subjectId);
        bundle.putString("editionId", editionId);
        subjectVersionFragment.setArguments(bundle);
        return subjectVersionFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        getArgumentsData();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mFg= (SetContainerFragment) getParentFragment();
        mPublicLayout = PublicLoadUtils.createPage(getActivity(), R.layout.activity_subject_version);
        mPublicLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mPublicLayout.setContentBackground(android.R.color.transparent);
        if(Configuration.isAnalyLayout()){
            Util.toDispScalpelFrameLayout(getActivity(), R.layout.activity_material_teaching_layout);
        }
        findView();
        return mPublicLayout;
    }

    private void findView() {
        mListView = (ListView) mPublicLayout.findViewById(R.id.lv_subject_version);
//        ivBack = (TextView) mPublicLayout.findViewById(R.id.pub_top_left);
//        tvSave = (TextView) mPublicLayout.findViewById(R.id.pub_top_right);
//        tvSave.setBackgroundResource(R.drawable.edit_save_selector);
//        tvSave.setTextColor(CommonCoreUtil.createColorStateList(getResources().getColor(R.color.color_006666),
//                getResources().getColor(R.color.color_232c98dc)));
//        tvSave.setShadowLayer(2, 0, 2, getResources().getColor(R.color.color_33ffff));
//        tvSave.setText(R.string.subject_save);
//        int paddingOffset = Util.dipToPx(3);
//        tvSave.setPadding(paddingOffset * 2, paddingOffset, paddingOffset * 2, paddingOffset);
//        ivBack.setOnClickListener(this);
//        tvSave.setOnClickListener(this);
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        titleText.setText(R.string.teaching_material_name);
        rightText.setVisibility(View.VISIBLE);
        rightText.setText(R.string.user_name_edit_save);
        RelativeLayout.LayoutParams saveViewParams= (RelativeLayout.LayoutParams) rightText.getLayoutParams();
        saveViewParams.width=getResources().getDimensionPixelOffset(R.dimen.dimen_47);
        saveViewParams.height=getResources().getDimensionPixelOffset(R.dimen.dimen_31);
        rightText.setLayoutParams(saveViewParams);
        rightText.setGravity(Gravity.CENTER);
        rightText.setTextColor(getResources().getColor(R.color.color_006666));
        rightText.setBackgroundResource(R.drawable.edit_save_selector);

    }


    private void saveData () {
        mPublicLayout.loading(true);
        cancelRequest();
        requestSaveEditionInfoTask = new RequestSaveEditionInfoTask(this.getActivity(), stageId, subjectId, selectedEntity.getId(), new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
                mPublicLayout.finish();
                DataStatusEntityBean bean = (DataStatusEntityBean) result;
                if (bean.getCode() == 0) {
                    EventBus.getDefault().post(new SubjectEditionEventBusBean(selectedEntity));
                    mFg.mIFgManager.popStack();
//                    forResult(selectedEntity);
                } else {
                    Util.showToast(R.string.save_fail_try_again);
                }
            }

            @Override
            public void dataError (int type, String msg) {
                mPublicLayout.finish();
                Util.showToast(R.string.save_fail_try_again);
            }
        });
        requestSaveEditionInfoTask.start();
    }

    private void cancelRequest () {
        if (requestSaveEditionInfoTask != null) {
            requestSaveEditionInfoTask.cancel();
        }
        requestSaveEditionInfoTask = null;
    }


    @Override
    protected void initLoadData() {
        mAdapter = new SubjectVersionMyModuleAdapter(this.getActivity());
        mAdapter.setmListener(new SubjectVersionMyModuleAdapter.SelectPositionEntityListener() {
            @Override
            public void onSelectionPosition(SubjectEditionBean.DataEntity entity) {
                selectedEntity = entity;
//                Util.showToast("保存" + entity.getName() + "--" + entity.getData());
            }
        });
        mListView.setAdapter(mAdapter);
        mAdapter.setListView(mListView);
        mAdapter.setEditionId(editionId);
        if (!TextUtils.isEmpty(title)) {
            titleText.setText(title);
        }
        titleText.setText(String.format(this.getResources().getString(R.string.select_subject), title));
        requestData();

    }

    private void getArgumentsData(){
        title = getArguments().getString("title");
        stageId = getArguments().getInt("stageId", 0);
        subjectId = getArguments().getString("subjectId");
        editionId = getArguments().getString("editionId");
    }


    @Override
    protected void setContentListener() {
        rightText.setOnClickListener(this);
        mPublicLayout.setmRefreshData(
                new PublicLoadLayout.RefreshData() {
                    @Override
                    public void refreshData() {
                        requestData();
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.pub_top_right:
                if (selectedEntity == null && mAdapter != null) {
                    selectedEntity = mAdapter.getSelectedEntity();
                }
                if (selectedEntity != null) {
                    if (editionId != null && selectedEntity.getId().equals(editionId)) {
                        this.finish();
                    } else {
                        saveData();
                    }
                } else {
                    Util.showToast(R.string.no_data_save);
                }
                break;
        }
    }

    private void requestData () {
        mPublicLayout.loading(true);
        cancelRequestEdition();
        requestEditionInfoTask = new RequestEditionInfoTask(this.getActivity(), stageId + "",
                subjectId, new AsyncLocalCallBack() {
            @Override
            public void updateLocal (YanxiuBaseBean result) {
                LogInfo.log("geny", "updateLocal");
                SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
                dataEntity = (ArrayList) subjectEditionBean.getData();
                if (dataEntity == null || dataEntity.size() <= 0) {

                } else {
                    mPublicLayout.finish();
                    PublicEditionBean.saveListFromSubjectEditionBean(dataEntity, stageId + "", subjectId + "");
                    mAdapter.setList(dataEntity);
                }
            }

            @Override
            public void update (YanxiuBaseBean result) {
                LogInfo.log("geny", "update");
                SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
                dataEntity = (ArrayList) subjectEditionBean.getData();
                if (dataEntity == null || dataEntity.size() <= 0) {
                    if (TextUtils.isEmpty(subjectEditionBean.getStatus().getDesc())) {
                        mPublicLayout.dataNull(true);
                    } else {
                        mPublicLayout.dataNull(subjectEditionBean.getStatus().getDesc());
                    }
                } else {
                    mPublicLayout.finish();
                    PublicEditionBean.saveListFromSubjectEditionBean(dataEntity, stageId + "", subjectId + "");
                    mAdapter.setList(dataEntity);
                }
            }

            @Override
            public void dataError (int type, String msg) {
                LogInfo.log("geny", "dataError type=" + type + " msg=" + msg);
                if (mAdapter == null || mAdapter.getCount() <= 0) {
                    if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                        mPublicLayout.netError(true);
                    } else {
                        if (TextUtils.isEmpty(msg)) {
                            mPublicLayout.dataNull(true);
                        } else {
                            mPublicLayout.dataNull(msg);
                        }
                    }
                } else {
                    mPublicLayout.finish();
                    if (TextUtils.isEmpty(msg)) {
                        Util.showToast(R.string.public_loading_data_null);
                    } else {
                        Util.showToast(msg);
                    }
                }
            }
        });
        requestEditionInfoTask.start();
    }

    private void cancelRequestEdition () {
        if (requestEditionInfoTask != null) {
            requestEditionInfoTask.cancel();
        }
        requestEditionInfoTask = null;
    }


    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.wood_bg);
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
    protected void destoryData() {
        cancelRequest();
        cancelRequestEdition();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFg=null;

        mListView=null;

        mAdapter=null;

        title=null;

        subjectId=null;

        editionId=null;

        dataEntity=null;

        selectedEntity=null;

    }

    private void finish() {
        if(mFg!=null&&mFg.mIFgManager!=null){
            mFg.mIFgManager.popStack();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            executeFinish();
            return true;
        }
        return false;
    }

    @Override
    public void onReset() {
        destoryData();
    }
}
