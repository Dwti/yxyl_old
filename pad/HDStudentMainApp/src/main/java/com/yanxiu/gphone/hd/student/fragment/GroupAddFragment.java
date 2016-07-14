package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.GroupInfoActivity;
import com.yanxiu.gphone.hd.student.bean.ClassInfoBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestClassInfoTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestUpdateUserInfoTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.view.passwordview.GridPasswordView;

/**
 * Created by Administrator on 2016/2/1.
 */
public class GroupAddFragment extends TopBaseFragment {

    private static final String TAG = GroupAddFragment.class.getSimpleName();

    private GroupInfoContainerFragment fg;

    private RequestUpdateUserInfoTask mTask;

    private RelativeLayout groupAddLayout;
    private GridPasswordView groupNumView;

    private TextView groupAddView;

    private RequestClassInfoTask requestClassInfoTask;

    private ClassInfoBean classInfoBean;

    public static Fragment newInstance () {
        GroupAddFragment groupAddFragment = new GroupAddFragment();
        return groupAddFragment;
    }

    @Override
    protected void setTopView () {
        super.setTopView();
        titleText.setText(R.string.group_add_title);
    }

    @Override
    protected boolean isAttach () {
        return false;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected View getContentView () {
        fg = (GroupInfoContainerFragment) getParentFragment();
        mPublicLayout = PublicLoadUtils.createPage(getActivity(), R.layout.add_group_layout);
        findView();
        return mPublicLayout;
    }

    @Override
    protected void initLoadData () {

    }

    private void findView () {
        groupAddLayout = (RelativeLayout) mPublicLayout.findViewById(R.id.no_group);
        groupAddLayout.setVisibility(View.VISIBLE);
        ((TextView) groupAddLayout.findViewById(R.id.top_tip_tx)).setText(R.string.group_add_tip);
        groupNumView = (GridPasswordView) groupAddLayout.findViewById(R.id.group_edit_number);
        groupNumView.setVisibility(View.VISIBLE);
        groupNumView.setBackgroundResource(R.drawable.group_input_bg);
        groupNumView.setPasswordVisibility(true);
        groupNumView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onChanged (String groupId) {
                LogInfo.log(TAG, "groupId = " + groupId);
            }

            @Override
            public void onMaxLength (String groupId) {
                LogInfo.log(TAG, "onMaxLength groupId = " + groupId);
            }
        });
        groupAddView = (TextView) groupAddLayout.findViewById(R.id.group_bottom_submit);
        groupAddView.setText(R.string.group_add_ok);
        groupAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (StringUtils.isEmpty(groupNumView.getPassWord())) {
                    Util.showUserToast(R.string.group_num_null, -1, -1);
                    return;
                } else {
                    requestGroupInfo();
                }

            }
        });
    }

    private void requestGroupInfo () {
        String groupNum = groupNumView.getPassWord();
        if (groupNum.length() < 6 || groupNum.length() > 8) {
            Util.showUserToast(R.string.group_num_no_right, -1, -1);
            return;
        }
        mPublicLayout.loading(true);
        cancelTask();
        try {
            requestClassInfoTask = new RequestClassInfoTask(getActivity(), groupNum, new AsyncCallBack() {
                @Override
                public void update (YanxiuBaseBean result) {
                    mPublicLayout.finish();
                    if (result != null) {
                        classInfoBean = (ClassInfoBean) result;
                        if (classInfoBean.getStatus().getCode() == YanXiuConstant.SERVER_0) {
                            if (classInfoBean.getData().size() > 0 && classInfoBean.getData().get(0).getStatus() == YanXiuConstant.SERVER_2) {
                                //Util.showUserToast(R.string.group_add_no_right, -1, -1);
                                //该班级不允许加入
                                Util.showCustomTipToast(R.string.group_add_no_right);
                            } else {
                                CommonCoreUtil.hideSoftInput(groupNumView);
                                fg.mIFgManager.addFragment(GroupDetailsFragment.newInstance
                                                (GroupInfoActivity.ADD_CLASS, classInfoBean), true,
                                        GroupDetailsFragment.class.getName());
                            }
                        } else {
                            Util.showUserToast(classInfoBean.getStatus().getDesc(), null, null);
                        }
                    }
                }

                @Override
                public void dataError (int type, String msg) {
                    mPublicLayout.finish();
                    if (!StringUtils.isEmpty(msg)) {
                        Util.showUserToast(msg, null, null);
                    } else {
                        Util.showUserToast(R.string.class_number_error, -1, -1);
                    }
                }
            });
            requestClassInfoTask.start();
        } catch (Exception e) {
            mPublicLayout.finish();
            LogInfo.log("填写的群组号码为非数字");
        }
    }

    private void cancelTask () {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }

    @Override
    public void onClick (View view) {
        super.onClick(view);
    }

    @Override
    protected void setContentListener () {

    }

    @Override
    protected void destoryData () {
        CommonCoreUtil.hideSoftInput(groupNumView);
        cancelTask();
        finish();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        fg = null;
        classInfoBean = null;
        groupAddLayout = null;
        groupNumView = null;
        classInfoBean = null;
    }

    private void finish () {
        if(fg!=null&&fg.mIFgManager!=null){
            fg.mIFgManager.popStack();
        }

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
        destoryData();
    }
}
