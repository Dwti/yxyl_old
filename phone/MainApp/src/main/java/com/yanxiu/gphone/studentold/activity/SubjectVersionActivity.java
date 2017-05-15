package com.yanxiu.gphone.studentold.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.adapter.SubjectVersionMyModuleAdapter;
import com.yanxiu.gphone.studentold.base.YanxiuBaseActivity;
import com.yanxiu.gphone.studentold.bean.DataStatusEntityBean;
import com.yanxiu.gphone.studentold.bean.PublicEditionBean;
import com.yanxiu.gphone.studentold.bean.SubjectEditionBean;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.studentold.requestTask.RequestEditionInfoTask;
import com.yanxiu.gphone.studentold.requestTask.RequestSaveEditionInfoTask;
import com.yanxiu.gphone.studentold.utils.PublicLoadUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.view.PublicLoadLayout;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/9.
 * 2.3 获得教材版本信息：
 */
public class SubjectVersionActivity extends YanxiuBaseActivity implements View.OnClickListener {

    public final static int LAUNCHER_SUBJECT_VERSION_ACTIVITY = 0x11;
    private PublicLoadLayout rootView;
    private ListView mListView;
    private SubjectVersionMyModuleAdapter mAdapter;
    private TextView ivBack;
    private TextView tvSave;
    private String title;
    private int stageId;
    private String subjectId;
    private String editionId;
    private ArrayList<SubjectEditionBean.DataEntity> dataEntity;
    private SubjectEditionBean.DataEntity selectedEntity;
    private RequestEditionInfoTask requestEditionInfoTask;
    private RequestSaveEditionInfoTask requestSaveEditionInfoTask;

    public static void launch (Activity context, String title, int stageId, String subjectId) {
        Intent intent = new Intent(context, SubjectVersionActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("stageId", stageId);
        intent.putExtra("subjectId", subjectId);
        context.startActivityForResult(intent, LAUNCHER_SUBJECT_VERSION_ACTIVITY);
    }

    public static void launch (Activity context, String title, int stageId, String subjectId, String editionId) {
        Intent intent = new Intent(context, SubjectVersionActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("stageId", stageId);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("editionId", editionId);
        context.startActivityForResult(intent, LAUNCHER_SUBJECT_VERSION_ACTIVITY);
    }

    private TextView tvTopTitle;


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = PublicLoadUtils.createPage(this, R.layout.activity_subject_version);
        setContentView(rootView);
        initView();
        initData();
        requestData();
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData () {
                requestData();
            }
        });
    }


    private void initView () {
        mListView = (ListView) findViewById(R.id.lv_subject_version);
        tvTopTitle = (TextView) findViewById(R.id.pub_top_mid);
        tvTopTitle.setText(R.string.itelliexe);
        ivBack = (TextView) findViewById(R.id.pub_top_left);
        tvSave = (TextView) findViewById(R.id.pub_top_right);
        tvSave.setBackgroundResource(R.drawable.edit_save_selector);
        tvSave.setTextColor(CommonCoreUtil.createColorStateList(getResources().getColor(R.color.color_006666),
                getResources().getColor(R.color.color_232c98dc)));
        tvSave.setShadowLayer(2, 0, 2, getResources().getColor(R.color.color_33ffff));
        tvSave.setText(R.string.subject_save);
        int paddingOffset = CommonCoreUtil.dipToPx(3);
        tvSave.setPadding(paddingOffset*2, paddingOffset, paddingOffset*2, paddingOffset);
        ivBack.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }
    private void initData () {
        mAdapter = new SubjectVersionMyModuleAdapter(this);
        mAdapter.setmListener(new SubjectVersionMyModuleAdapter.SelectPositionEntityListener() {
            @Override
            public void onSelectionPosition (SubjectEditionBean.DataEntity entity) {
                selectedEntity = entity;
//                Util.showToast("保存" + entity.getName() + "--" + entity.getData());
            }
        });
        mListView.setAdapter(mAdapter);
        mAdapter.setListView(mListView);
        title = getIntent().getStringExtra("title");
        stageId = getIntent().getIntExtra("stageId", 0);
        subjectId = getIntent().getStringExtra("subjectId");
        editionId = getIntent().getStringExtra("editionId");
        mAdapter.setEditionId(editionId);
        if (!TextUtils.isEmpty(title)) {
            tvTopTitle.setText(title);
        }
        tvTopTitle.setText(String.format(this.getResources().getString(R.string.select_subject), title));
    }


    private void requestData () {
        rootView.loading(true);
//        if (NetWorkTypeUtils.isNetAvailable()) {
//            new YanxiuSimpleAsyncTask<SubjectEditionBean>(this) {
//                @Override
//                public SubjectEditionBean doInBackground() {
//                    SubjectEditionBean mSubjectEditionBean = new SubjectEditionBean();
//                    List<SubjectEditionBean.DataEntity> dataEntity = PublicEditionBean.findDataToSubjectEditionBean(stageId + "", subjectId + "");
//                    mSubjectEditionBean.setData(dataEntity);
//                    return mSubjectEditionBean;
//                }
//                @Override
//                public void onPostExecute(SubjectEditionBean result) {
//                    rootView.finish();
//                    if (result != null && result.getData() != null) {
//                        dataEntity = (ArrayList<SubjectEditionBean.DataEntity>) result.getData();
//                        if(dataEntity.size() > 0){
//                            mAdapter.setList(result.getData());
//                        } else {
//                            rootView.dataNull(true);
//                        }
//                    } else {
//                        rootView.dataNull(true);
//                    }
//                }
//            }.start();
//        } else {
        cancelRequestEdition();
        requestEditionInfoTask = new RequestEditionInfoTask(this, stageId + "",
                subjectId, new AsyncLocalCallBack() {
            @Override
            public void updateLocal (YanxiuBaseBean result) {
                LogInfo.log("geny", "updateLocal");
                SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
                dataEntity = (ArrayList) subjectEditionBean.getData();
                if (dataEntity == null || dataEntity.size() <= 0) {

                } else {
                    rootView.finish();
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
                        rootView.dataNull(true);
                    } else {
                        rootView.dataNull(subjectEditionBean.getStatus().getDesc());
                    }
                } else {
                    rootView.finish();
                    PublicEditionBean.saveListFromSubjectEditionBean(dataEntity, stageId + "", subjectId + "");
                    mAdapter.setList(dataEntity);
                }
            }

            @Override
            public void dataError (int type, String msg) {
                LogInfo.log("geny", "dataError type=" + type + " msg=" + msg);
                if (mAdapter == null || mAdapter.getCount() <= 0) {
                    if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                        rootView.netError(true);
                    } else {
                        if (TextUtils.isEmpty(msg)) {
                            rootView.dataNull(true);
                        } else {
                            rootView.dataNull(msg);
                        }
                    }
                } else {
                    rootView.finish();
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
//    }

    private void cancelRequestEdition () {
        if (requestEditionInfoTask != null) {
            requestEditionInfoTask.cancel();
        }
        requestEditionInfoTask = null;
    }

    private void saveData () {
        rootView.loading(true);
        cancelRequest();
        requestSaveEditionInfoTask = new RequestSaveEditionInfoTask(this, stageId, subjectId, selectedEntity.getId(), new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
                rootView.finish();
                DataStatusEntityBean bean = (DataStatusEntityBean) result;
                if (bean.getCode() == 0) {
                    forResult(selectedEntity);
                } else {
                    Util.showToast(R.string.save_fail_try_again);
                }
            }

            @Override
            public void dataError (int type, String msg) {
                rootView.finish();
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

    public void forResult (SubjectEditionBean.DataEntity entity) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", entity);
        intent.putExtra("data", bundle);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        cancelRequestEdition();
        cancelRequest();
    }

    @Override
    public void onClick (View v) {
        if (v == ivBack) {
            this.finish();
        } else if (v == tvSave) {
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
        }
    }
}
