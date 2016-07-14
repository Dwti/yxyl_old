package com.yanxiu.gphone.hd.student.fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.ClassDetailBean;
import com.yanxiu.gphone.hd.student.bean.ClassInfoBean;
import com.yanxiu.gphone.hd.student.bean.GroupEventRefresh;
import com.yanxiu.gphone.hd.student.bean.RequestBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestAddClassTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestUpdateUserInfoTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/2/1.
 */
public class GroupEditPersonNameFragment extends TopBaseFragment {

    private static final String TAG = GroupEditPersonNameFragment.class.getSimpleName();

    private GroupInfoContainerFragment fg;
    private EditText nameView;

    private RelativeLayout editNameLayout;
    private TextView clickView;
    private TextView nameTips;

    private ClassInfoBean mClassInfoBean;

    private RequestAddClassTask requestAddGroupTask;

    private RequestBean requestBean;

    public static Fragment newInstance (ClassInfoBean classInfoBean) {
        GroupEditPersonNameFragment groupEditPersonNameFragment = new GroupEditPersonNameFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("classInfoBean", classInfoBean);
        groupEditPersonNameFragment.setArguments(bundle);
        return groupEditPersonNameFragment;
    }

    @Override
    protected void setTopView () {
        super.setTopView();
        titleText.setText(R.string.edit_person_name_title);
        leftView.setImageResource(R.drawable.group_cancel_selector);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                CommonCoreUtil.hideSoftInput(nameView);
                finish();
            }
        });
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
        mClassInfoBean = (ClassInfoBean) getArguments().getSerializable("classInfoBean");
        mPublicLayout = PublicLoadUtils.createPage(getActivity(), R.layout.edit_person_name_layout);
        findView();
        return mPublicLayout;
    }

    @Override
    protected void initLoadData() {

    }

    private void findView () {
        editNameLayout = (RelativeLayout) mPublicLayout.findViewById(R.id.group_edit_name);
        editNameLayout.setVisibility(View.VISIBLE);
        nameView = (EditText) editNameLayout.findViewById(R.id.group_edit_txt);
        nameView.setText(LoginModel.getUserinfoEntity().getRealname());
        nameView.setSelection(LoginModel.getUserinfoEntity().getRealname().length());
        editNameLayout.findViewById(R.id.group_edit_number).setVisibility(View.INVISIBLE);
        nameView.setVisibility(View.VISIBLE);
        clickView = (TextView) editNameLayout.findViewById(R.id.group_bottom_submit);
        nameTips = (TextView) editNameLayout.findViewById(R.id.top_tip_tx);
        nameTips.setText(R.string.edit_person_name_tip);
        clickView.setText(R.string.edit_person_name_ok);
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (StringUtils.isEmpty(nameView.getText().toString())) {
                    Util.showUserToast(R.string.group_add_name_null, -1, -1);
                    return;
                }
                //更改用户名称
                updateUserNameInfo(nameView.getText().toString());
                addClass();
            }
        });
    }

    private final static int USER_EDIT_NAME_TYPE = 0x101;
    private void updateUserNameInfo(final String nameInfo){
        //rootView.loading(true);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("realname", nameInfo);
        new RequestUpdateUserInfoTask(getActivity(), hashMap, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                //rootView.finish();
                LoginModel.getUserinfoEntity().setRealname(nameInfo);
                LoginModel.savaCacheData();
                Intent intent = new Intent();
                intent.putExtra("type", USER_EDIT_NAME_TYPE);
                intent.putExtra("editMsg", nameInfo);
                //setResult(RESULT_OK, intent);
            }
            @Override
            public void dataError(int type, String msg) {
                //rootView.finish();
                if (!TextUtils.isEmpty(msg)) {
                    Util.showToast(msg);
                } else {
                    Util.showToast(R.string.data_uploader_failed);
                }
            }
        }).start();
    }

    private void addClass () {
        if (mClassInfoBean != null && mClassInfoBean.getData().size() > 0) {
            ClassDetailBean dataEntity = mClassInfoBean.getData().get(0);
            String name = nameView.getText().toString();
            mPublicLayout.loading(true);
            requestAddGroupTask = new RequestAddClassTask(getActivity(), dataEntity.getId(), name, new
                    AsyncCallBack() {
                        @Override
                        public void update (YanxiuBaseBean result) {
                            mPublicLayout.finish();
                            if (result != null) {
                                CommonCoreUtil.hideSoftInput(nameView);
                                requestBean = (RequestBean) result;
                                if (requestBean.getStatus().getCode() == YanXiuConstant.SERVER_3) {
                                    Util.showUserToast(R.string.group_add_success, -1, -1);
                                } else if (requestBean.getStatus().getCode() == YanXiuConstant.SERVER_4) {
                                    Util.showUserToast(R.string.group_add_needcheck_one, R.string.group_add_needcheck_two, -1);
                                } else {
                                    Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                                }
                                finishAllBeforeFragments();
                                EventBus.getDefault().post(new GroupEventRefresh());//刷新作业首页数据
                            }
                        }

                        @Override
                        public void dataError (int type, String msg) {
                            mPublicLayout.finish();
                            if (!StringUtils.isEmpty(msg)) {
                                Util.showUserToast(msg, null, null);
                            } else {
                                Util.showUserToast(R.string.net_null_one, -1, -1);
                            }
                        }
                    });
            requestAddGroupTask.start();
        }
    }

    private void cancelTask () {
        if (requestAddGroupTask != null) {
            requestAddGroupTask.cancel();
            requestAddGroupTask = null;
        }
    }

    @Override
    protected void setContentListener () {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fg=null;
        nameView=null;

        editNameLayout=null;
        clickView=null;
        nameTips=null;
        mClassInfoBean=null;
        requestBean=null;
    }

    @Override
    protected void destoryData () {
        cancelTask();
        finish();
    }

    private void finish () {
        if(fg!=null&&fg.mIFgManager!=null){
            fg.mIFgManager.popStack();
        }

    }

    private void finishAllBeforeFragments(){
        fg.mIFgManager.popBackStackImmediate(GroupAddFragment.class.getName(),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
    public void onReset() {
            destoryData();
    }
}
