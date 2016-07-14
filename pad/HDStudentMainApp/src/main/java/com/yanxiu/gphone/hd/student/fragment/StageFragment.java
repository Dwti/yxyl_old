package com.yanxiu.gphone.hd.student.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.YanxiuApplication;
import com.yanxiu.gphone.hd.student.activity.StageSwitchActivity;
import com.yanxiu.gphone.hd.student.eventbusbean.StageBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.manager.ActivityManager;
import com.yanxiu.gphone.hd.student.preference.PreferencesManager;
import com.yanxiu.gphone.hd.student.receiver.ResetMessageReceiver;
import com.yanxiu.gphone.hd.student.requestTask.RequestUpdateUserInfoTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.view.StageSelectDialog;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/2/3.
 */
public class StageFragment extends TopBaseFragment {
    private static final String STAGE_ID_KEY = "stage_id_key";
    public final static int STAGE_TYPE_PRIM = YanXiuConstant.STAGE.PRIM;
    public final static int STAGE_TYPE_JUIN = YanXiuConstant.STAGE.JUIN;
    public final static int STAGE_TYPE_HIGH = YanXiuConstant.STAGE.HIGH;
    private final static int STAGE_CONSTANT_NUM = 3;
    private View[] stageLayout = new View[STAGE_CONSTANT_NUM];
    private TextView[] stageTxt = new TextView[STAGE_CONSTANT_NUM];
    private ImageView[] stageTv = new ImageView[STAGE_CONSTANT_NUM];
    private int[] stageTxtId = {R.string.primary_txt, R.string.juinor_txt, R.string.high_txt};
    private int type;
    private SetContainerFragment mFg;
    private RequestUpdateUserInfoTask mTask;
    private static StageFragment mStageFg;
    private ResetMessageReceiver receiver = new ResetMessageReceiver();

    private StageSelectDialog mStageSelectDialog;

    public static Fragment newInstance (int stageId) {
        if (mStageFg == null) {
            mStageFg = new StageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(STAGE_ID_KEY, stageId);
            mStageFg.setArguments(bundle);
        }
        return mStageFg;
    }

    @Override
    protected void setTopView () {
        super.setTopView();
        titleText.setText(R.string.select_subject_txt);
    }


    @Override
    protected void setRootView () {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.wood_bg);
    }

    @Override
    protected boolean isAttach () {
        return false;
    }

    @Override
    protected View getContentView () {
        mFg = (SetContainerFragment) getParentFragment();
        mPublicLayout = PublicLoadUtils.createPage(getActivity(), R.layout.select_stage_my_layout);
        mPublicLayout.setContentBackground(android.R.color.transparent);
        mPublicLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        type = getArguments().getInt(STAGE_ID_KEY, STAGE_TYPE_JUIN);
        findView();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ResetMessageReceiver.INTERFILTER_ACTION);
        getActivity().registerReceiver(receiver, filter);
        return mPublicLayout;
    }


    @Override
    protected void initLoadData () {

    }


    private void findView () {
        stageLayout[0] = mPublicLayout.findViewById(R.id.primary_stage_layout);
        stageLayout[1] = mPublicLayout.findViewById(R.id.juinor_stage_layout);
        stageLayout[2] = mPublicLayout.findViewById(R.id.high_stage_layout);

        for (int i = 0; i < STAGE_CONSTANT_NUM; i++) {
            stageLayout[i].setOnClickListener(this);
            stageTxt[i] = (TextView) stageLayout[i].findViewById(R.id.name);
            stageTxt[i].setText(stageTxtId[i]);
            stageTv[i] = (ImageView) stageLayout[i].findViewById(R.id.right_arrow);
            stageTv[i].setVisibility(View.GONE);
            stageLayout[i].findViewById(R.id.left_icon).setVisibility(View.GONE);
        }
        setSelectStageDisp(type);
    }

    private void setSelectStageDisp (int type) {
        int index = 0;
        if (type == STAGE_TYPE_PRIM) {
            index = 0;
        } else if (type == STAGE_TYPE_JUIN) {
            index = 1;
        } else if (type == STAGE_TYPE_HIGH) {
            index = 2;
        }
        for (int i = 0; i < STAGE_CONSTANT_NUM; i++) {
            if (i == index) {
                stageTv[i].setVisibility(View.VISIBLE);
            } else {
                stageTv[i].setVisibility(View.GONE);
            }
        }
    }

    private void setSelectedView (final int type) {
        if (mStageSelectDialog == null) {
            mStageSelectDialog = new StageSelectDialog(getActivity(), new
                    StageSelectDialog.StageDialogCallBack() {
                        @Override
                        public void stage () {
                            setSelectStageDisp(type);
                            upLoadStageId(type);
                        }
                        @Override
                        public void cancel () {

                        }
                    });
        } else {
            mStageSelectDialog.setStageDialogCallBack(new StageSelectDialog
                    .StageDialogCallBack() {
                @Override
                public void stage () {
                    setSelectStageDisp(type);
                    upLoadStageId(type);
                }
                @Override
                public void cancel () {

                }
            });
        }
        mStageSelectDialog.show();

    }

    private void upLoadStageId (final int stageId) {
        if (LoginModel.getUserinfoEntity() == null) {
            return;
        }
        if (LoginModel.getUserinfoEntity().getStageid() == stageId) {
            finish();
            return;
        }
        mPublicLayout.loading(true);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("stageid", stageId + "");
        cancelTask();
        mTask = new RequestUpdateUserInfoTask(getActivity(), hashMap, new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
                mPublicLayout.finish();
                LogInfo.log("haitian", "type update");
                if (LoginModel.getUserinfoEntity() != null) {
                    LoginModel.getUserinfoEntity().setStageid(stageId);
                }
                String myStage = null;
                if (stageId == STAGE_TYPE_PRIM) {
                    myStage = getResources().getString(R.string.primary_txt);
                } else if (stageId == STAGE_TYPE_JUIN) {
                    myStage = getResources().getString(R.string.juinor_txt);
                } else if (stageId == STAGE_TYPE_HIGH) {
                    myStage = getResources().getString(R.string.high_txt);
                }
                if (LoginModel.getUserinfoEntity() != null) {
                    LoginModel.getUserinfoEntity().setStageName(myStage);
                }
                LoginModel.savaCacheData();
                PreferencesManager.getInstance().clearSubjectSection();
                EventBus.getDefault().post(new StageBean());
                getActivity().sendBroadcast(new Intent(ResetMessageReceiver.INTERFILTER_ACTION));

            }

            @Override
            public void dataError (int type, String msg) {
                mPublicLayout.finish();
                LogInfo.log("haitian", "type =" + type + " msg=" + msg);
                if (!TextUtils.isEmpty(msg)) {
                    Util.showToast(msg);
                } else {
                    Util.showToast(R.string.data_uploader_failed);
                }
            }
        });
        mTask.start();
    }

    private void cancelTask () {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }


    @Override
    protected void setContentListener () {
//        high_stage_layout.setOnClickListener(this);
//        juinor_stage_layout.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        super.onClick(v);
        if (v == stageLayout[0]) {
            setSelectedView(STAGE_TYPE_PRIM);
        } else if (v == stageLayout[1]) {
            setSelectedView(STAGE_TYPE_JUIN);
        } else if (v == stageLayout[2]) {
            setSelectedView(STAGE_TYPE_HIGH);
        }
    }

    private void finish () {
        if (mFg != null && mFg.mIFgManager != null) {
            mFg.mIFgManager.popStack();
        }
        mStageFg = null;
    }


    @Override
    protected void destoryData () {
        cancelTask();
        finish();
    }


    @Override
    public void onDestroy () {
        super.onDestroy();
        try {
            if (getActivity() != null) {
                getActivity().unregisterReceiver(receiver);
            }
        } catch (Exception e) {

        }
        if(mStageSelectDialog != null) {
            mStageSelectDialog.dismiss();
            mStageSelectDialog = null;
        }
        stageLayout = null;
        stageTxt = null;
        stageTv = null;
        mFg = null;
        receiver = null;

    }

    @Override
    protected IFgManager getFragmentManagerFromSubClass () {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass () {
        return 0;
    }


    @Override
    public void onReset () {
        YanxiuApplication.getInstance().setSubjectVersionBean(null);
        destoryData();
        StageSwitchActivity.launchActivity(getActivity());
        ActivityManager.destoryActivityExcStageSwitchActivity();
    }

}
