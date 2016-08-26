package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.adapter.TeachingMaterialAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.DataTeacherEntity;
import com.yanxiu.gphone.student.bean.EditionBean;
import com.yanxiu.gphone.student.bean.MistakeEditionBean;
import com.yanxiu.gphone.student.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.student.bean.PublicFavouriteQuestionBean;
import com.yanxiu.gphone.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.student.requestTask.RequestFavouriteEditionTask;
import com.yanxiu.gphone.student.requestTask.RequestMistakeEditionTask;
import com.yanxiu.gphone.student.requestTask.RequestPracticeEditionTask;
import com.yanxiu.gphone.student.requestTask.RequestSubjectInfoTask;
import com.yanxiu.gphone.student.utils.Configuration;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

import java.util.List;

/**
 * Created by Administrator on 2015/7/15.
 */
public class TeachingMaterialActivity extends YanxiuBaseActivity {
    public final static int TEACHING_MATERIAL_ACTIVITY = 0x100;
    public final static int PRACTICE_HISTORY_ACTIVITY = 0x101;
    public final static int PRACTICE_ERROR_COLLECTION_ACTIVITY = 0x102;
    public final static int MY_FAVOURITE_COLLECTION_ACTIVITY = 0x103;
    private ListView materialList;
    private View topView;
    private View backView;
    private TextView topTitle;

    private TeachingMaterialAdapter adapter;
    private PublicLoadLayout mRootView;
    private RequestFavouriteEditionTask mRequestFavouriteEditionTask;
    private RequestMistakeEditionTask mRequestMistakeEditionTask;
    private RequestPracticeEditionTask mRequestPracticeEditionTask;
    private RequestSubjectInfoTask mRequestSubjectInfoTask;
    private SubjectVersionBean subjectVersionBean;
    private int selectPosition = -1;
    private int type = TEACHING_MATERIAL_ACTIVITY;

    public static void launchActivity(Activity context, int type) {
        Intent intent = new Intent(context, TeachingMaterialActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = PublicLoadUtils.createPage(this, R.layout.activity_material_teaching_layout);
        if(Configuration.isAnalyLayout()){
            Util.toDispScalpelFrameLayout(this, R.layout.activity_material_teaching_layout);
        } else {
            setContentView(mRootView);
        }
        mRootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {
                if (type == PRACTICE_ERROR_COLLECTION_ACTIVITY) {
                    requestMistakeEditionTask(LoginModel.getUserinfoEntity().getStageid() + "");
                } else if (type == MY_FAVOURITE_COLLECTION_ACTIVITY) {
                    requestMyFavouriteTask(LoginModel.getUserinfoEntity().getStageid() + "");
                }else if (type == PRACTICE_HISTORY_ACTIVITY) {
                    requestPracticeEditionTask(LoginModel.getUserinfoEntity().getStageid() + "");
                } else {
                    requestSubjectInfoTask();
                }
            }
        });
        type = getIntent().getIntExtra("type", TEACHING_MATERIAL_ACTIVITY);
        findView();
    }

    private void findView() {
        topView = mRootView.findViewById(R.id.top_layout);
        backView = topView.findViewById(R.id.pub_top_left);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topTitle = (TextView) topView.findViewById(R.id.pub_top_mid);
        if (type == PRACTICE_ERROR_COLLECTION_ACTIVITY) {
            topTitle.setText(R.string.error_collection_name);
        } else if (type == MY_FAVOURITE_COLLECTION_ACTIVITY) {
            topTitle.setText(R.string.my_favourite_name);
        }else if (type == PRACTICE_HISTORY_ACTIVITY) {
            topTitle.setText(R.string.practice_history_name);
        } else {
            topTitle.setText(R.string.teaching_material_name);
        }

        materialList = (ListView) mRootView.findViewById(R.id.material_list);
        adapter = new TeachingMaterialAdapter(this, type);
        if (type == PRACTICE_ERROR_COLLECTION_ACTIVITY) {
            materialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DataTeacherEntity mDataTeacherEntity = (DataTeacherEntity) adapter.getList().get(position);
                    if(mDataTeacherEntity.getChildren() != null && !mDataTeacherEntity.getChildren().isEmpty()){
                        //MistakeSectionActivity.launch(TeachingMaterialActivity.this, mDataTeacherEntity.getName(), mDataTeacherEntity.getId() + "",
                                //mDataTeacherEntity.getData().getEditionId() + "", mDataTeacherEntity.getChildren(), mDataTeacherEntity.getData().getHas_knp());
                        MistakeAllActivity.launch(TeachingMaterialActivity.this, mDataTeacherEntity.getName(), mDataTeacherEntity.getId() + "", mDataTeacherEntity.getData().getWrongNum());

                    } else {
                        MistakeAllActivity.launch(TeachingMaterialActivity.this, mDataTeacherEntity.getName(), mDataTeacherEntity.getId() + "", mDataTeacherEntity.getData().getWrongNum());
                        //MistakeSectionActivity.launch(TeachingMaterialActivity.this, mDataTeacherEntity.getName(), mDataTeacherEntity.getId() + "",
                                //mDataTeacherEntity.getData().getEditionId() + "", null, mDataTeacherEntity.getData().getHas_knp());
                    }
                }
            });
        } else if (type == MY_FAVOURITE_COLLECTION_ACTIVITY) {
//            requestMyFavouriteTask(LoginModel.getUserinfoEntity().getStageid() + "");
            materialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    DataTeacherEntity mDataTeacherEntity = (DataTeacherEntity) adapter.getList().get(position);
                    if (mDataTeacherEntity.getChildren() != null && !mDataTeacherEntity.getChildren().isEmpty()) {
                        FavouriteSectionActivity.launch(TeachingMaterialActivity.this, mDataTeacherEntity.getName(), mDataTeacherEntity.getId() + "",
                                mDataTeacherEntity.getData().getEditionId() + "", mDataTeacherEntity.getChildren(), mDataTeacherEntity.getData().getHas_knp());
                    } else {
                        FavouriteSectionActivity.launch(TeachingMaterialActivity.this, mDataTeacherEntity.getName(), mDataTeacherEntity.getId() + "",
                                mDataTeacherEntity.getData().getEditionId() + "", null, mDataTeacherEntity.getData().getHas_knp());
                    }
                }
            });
        }else if (type == PRACTICE_HISTORY_ACTIVITY) {
            requestPracticeEditionTask(LoginModel.getUserinfoEntity().getStageid() + "");
            materialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    DataTeacherEntity mDataTeacherEntity = (DataTeacherEntity) adapter.getList().get(position);
                    PracticeHistoryActivity.launch(TeachingMaterialActivity.this, mDataTeacherEntity.getName(),
                            mDataTeacherEntity.getId(),
                            mDataTeacherEntity.getData().getEditionId(), mDataTeacherEntity.getData().getHas_knp());
                }
            });
        } else {
            requestSubjectInfoTask();
            materialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    SubjectVersionBean.DataEntity mDataEntity = (SubjectVersionBean.DataEntity) adapter.getItem(position);
                    if (mDataEntity != null) {
                        LogInfo.log("haitian", "xxx---if=" + mDataEntity.getName());
                        selectPosition = position;
                        SubjectVersionActivity.launch(TeachingMaterialActivity.this,
                                mDataEntity.getName(),
                                LoginModel.getUserinfoEntity().getStageid(),
                                mDataEntity.getId(), mDataEntity.getData() == null ? null : mDataEntity.getData().getEditionId());
                    }
                }
            });
//            subjectVersionBean = YanxiuApplication.getInstance().getSubjectVersionBean();
//            if (subjectVersionBean != null && subjectVersionBean.getData() != null && subjectVersionBean.getData().size() > 0) {
//                updateSubjectVersionView(subjectVersionBean.getData());
//            } else {
//            requestSubjectInfoTask();
//            }
        }
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
            mRootView.dataNull(true);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (type == PRACTICE_ERROR_COLLECTION_ACTIVITY) {
            requestMistakeEditionTask(LoginModel.getUserinfoEntity().getStageid() + "");
        } else if (type == MY_FAVOURITE_COLLECTION_ACTIVITY) {
            requestMyFavouriteTask(LoginModel.getUserinfoEntity().getStageid() + "");
        }
    }

    private void updateMistakeEditionView(MistakeEditionBean mMistakeEditionBean) {
        if (mMistakeEditionBean.getData() != null) {
            if (adapter.getCount() <= 0) {
                adapter.setList(mMistakeEditionBean.getData());
                materialList.setAdapter(adapter);
            } else {
                adapter.setList(mMistakeEditionBean.getData());
            }
        } else {
            mRootView.dataNull(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SubjectVersionActivity.LAUNCHER_SUBJECT_VERSION_ACTIVITY:
                    LogInfo.log("king", "SubjectVersionActivity");
                    if (data != null) {
                        Bundle bundle = data.getBundleExtra("data");
                        SubjectEditionBean.DataEntity dataBean = (SubjectEditionBean.DataEntity) bundle.getSerializable("bean");
                        if (dataBean != null) {
                            refreshData(dataBean);
                        }
                    }
                    break;
            }
        }
    }

    public void refreshData(SubjectEditionBean.DataEntity entity) {
        EditionBean editionBean = new EditionBean();
        editionBean.setEditionId(entity.getId());
        editionBean.setEditionName(entity.getName());
        SubjectVersionBean.DataEntity mDataEntity = (SubjectVersionBean.DataEntity) adapter.getList().get(selectPosition);
        mDataEntity.setData(editionBean);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        RequestSubjectInfoTask.savaCacheData(JSON.toJSONString(subjectVersionBean));
    }

    private AsyncCallBack mAsyncCallBack = new AsyncCallBack() {
        @Override
        public void update(YanxiuBaseBean result) {
            mRootView.finish();
            MistakeEditionBean mMistakeEditionBean = (MistakeEditionBean) result;
            if (mMistakeEditionBean.getData() == null || mMistakeEditionBean.getData().size() <= 0) {
                if (TextUtils.isEmpty(mMistakeEditionBean.getStatus().getDesc())) {
                    mRootView.dataNull(true);
                } else {
                    mRootView.dataNull(mMistakeEditionBean.getStatus().getDesc());
                }
            } else {
                if (type == PRACTICE_ERROR_COLLECTION_ACTIVITY || type == PRACTICE_HISTORY_ACTIVITY) {
                    updateMistakeEditionView((MistakeEditionBean) result);
                }
            }
        }

        @Override
        public void dataError(int type, String msg) {
            LogInfo.log("haitian", "type=" + type + "---msg=" + msg);
            if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                mRootView.netError(true);
            } else {
                if (TextUtils.isEmpty(msg)) {
                    mRootView.dataNull(true);
                } else {
                    mRootView.dataNull(msg);
                }
            }
        }
    };
    private AsyncLocalCallBack mAsyncLocalCallBack = new AsyncLocalCallBack() {
        @Override
        public void updateLocal(YanxiuBaseBean result) {
            MistakeEditionBean mMistakeEditionBean = (MistakeEditionBean) result;
            if (mMistakeEditionBean.getData() != null || mMistakeEditionBean.getData().size() > 0) {
                mRootView.finish();
                updateMistakeEditionView((MistakeEditionBean) result);
            }
        }

        @Override
        public void update(YanxiuBaseBean result) {
            mRootView.finish();
            MistakeEditionBean mMistakeEditionBean = (MistakeEditionBean) result;
            if (mMistakeEditionBean.getData() == null || mMistakeEditionBean.getData().size() <= 0) {
                if (TextUtils.isEmpty(mMistakeEditionBean.getStatus().getDesc())) {
                    mRootView.dataNull(true);
                } else {
                    mRootView.dataNull(mMistakeEditionBean.getStatus().getDesc());
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
            mRootView.finish();
            if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                mRootView.netError(true);
            } else {
                if (TextUtils.isEmpty(msg)) {
                    mRootView.dataNull(true);
                } else {
                    mRootView.dataNull(msg);
                }
            }
        }
    };
    //=======================================================================================
    private void requestMyFavouriteTask(final String stageId) {
        mRootView.loading(true);
        cancelMyFavouriteTask();
        if (!NetWorkTypeUtils.isNetAvailable()) {
            new YanxiuSimpleAsyncTask<MistakeEditionBean>(this) {
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
                    mRootView.finish();
                    if (result != null && result.getData() != null) {
                        updateMistakeEditionView(result);
                    } else {
                        if(adapter == null || adapter.getCount() <= 0){
                            mRootView.dataNull(true);
                        }
                    }
                }
            }.start();
        } else {
            mRequestFavouriteEditionTask = new RequestFavouriteEditionTask(TeachingMaterialActivity.this, stageId, mAsyncLocalCallBack);
            mRequestFavouriteEditionTask.start();
        }
    }
    private void cancelMyFavouriteTask() {
        if (mRequestFavouriteEditionTask != null) {
            mRequestFavouriteEditionTask.cancel();
        }
        mRequestFavouriteEditionTask = null;
    }
    //=======================================================================================
    private void requestMistakeEditionTask(final String stageId) {
        mRootView.loading(true);
        cancelMistakeEditionTask();
        if (!NetWorkTypeUtils.isNetAvailable()) {
            new YanxiuSimpleAsyncTask<MistakeEditionBean>(this) {
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
                    mRootView.finish();
                    if (result != null && result.getData() != null) {
                        updateMistakeEditionView(result);
                    } else {
                        if(adapter == null || adapter.getCount() <= 0){
                            mRootView.dataNull(true);
                        }
                    }
                }
            }.start();
        } else {
            mRequestMistakeEditionTask = new RequestMistakeEditionTask(TeachingMaterialActivity.this, stageId, mAsyncLocalCallBack);
            mRequestMistakeEditionTask.start();
        }
    }
    private void cancelMistakeEditionTask() {
        if (mRequestMistakeEditionTask != null) {
            mRequestMistakeEditionTask.cancel();
        }
        mRequestMistakeEditionTask = null;
    }

    private void requestPracticeEditionTask(String stageId) {
        cancelPracticeEditionTask();
        mRootView.loading(true);
        mRequestPracticeEditionTask = new RequestPracticeEditionTask(this, stageId, mAsyncCallBack);
        mRequestPracticeEditionTask.start();
    }

    private void cancelPracticeEditionTask() {
        if (mRequestPracticeEditionTask != null) {
            mRequestPracticeEditionTask.cancel();
        }
        mRequestPracticeEditionTask = null;
    }

    private void requestSubjectInfoTask() {
        cancelSubjectInfoTask();
        mRootView.loading(true);
        mRequestSubjectInfoTask = new RequestSubjectInfoTask(this,
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
                mRootView.finish();
                SubjectVersionBean subjectVersionBean = (SubjectVersionBean) result;
                YanxiuApplication.getInstance().setSubjectVersionBean(subjectVersionBean);
                if (subjectVersionBean.getData() != null && subjectVersionBean.getData().size() > 0) {
//                    PublicSubjectBean.saveListFromSubjectVersionBean(subjectVersionBean.getData(), LoginModel.getUserInfo().getStageid() + "");
                    updateSubjectVersionView(subjectVersionBean.getData());
                } else {
                    if (TextUtils.isEmpty(subjectVersionBean.getStatus().getDesc())) {
                        mRootView.dataError(true);
                    } else {
                        mRootView.dataNull(subjectVersionBean.getStatus().getDesc());
                    }
                }
            }

            @Override
            public void dataError(int type, String msg) {
                if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                    mRootView.netError(true);
                } else {
                    if (TextUtils.isEmpty(msg)) {
                        mRootView.dataNull(true);
                    } else {
                        mRootView.dataNull(msg);
                    }
                }
            }
        });
        mRequestSubjectInfoTask.start();

    }

    private void cancelSubjectInfoTask() {
        if (mRequestSubjectInfoTask != null) {
            mRequestSubjectInfoTask.cancel();
        }
        mRequestSubjectInfoTask = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRootView.finish();
        cancelSubjectInfoTask();
        cancelMyFavouriteTask();
        cancelMistakeEditionTask();
        cancelPracticeEditionTask();
        cancelSubjectInfoTask();
    }
}
