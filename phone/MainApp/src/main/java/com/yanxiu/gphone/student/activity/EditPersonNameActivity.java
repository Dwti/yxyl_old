package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.ClassDetailBean;
import com.yanxiu.gphone.student.bean.ClassInfoBean;
import com.yanxiu.gphone.student.bean.RequestBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestAddClassTask;
import com.yanxiu.gphone.student.requestTask.RequestUpdateUserInfoTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/9.
 */
public class EditPersonNameActivity extends YanxiuBaseActivity {

    public static void launchActivity(Activity activity, ClassInfoBean classInfoBean) {
        Intent intent = new Intent(activity, EditPersonNameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("classInfoBean", classInfoBean);
        intent.putExtra("data", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        activity.startActivity(intent);
        activity.finish();
    }

    private PublicLoadLayout rootView;
    private RelativeLayout editNameLayout;
    private TextView celView;
    private TextView titleView;

    private EditText nameView;
    private TextView clickView;
    private TextView nameTips;

    private ClassInfoBean mClassInfoBean;

    private RequestAddClassTask requestAddGroupTask;

    private RequestBean requestBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = PublicLoadUtils.createPage(this, R.layout.edit_person_name_layout);
        setContentView(rootView);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("data");
            mClassInfoBean = (ClassInfoBean) bundle.getSerializable("classInfoBean");
        }
        findView();
    }

    private void findView() {
        titleView = (TextView) findViewById(R.id.pub_top_mid);
        titleView.setText(R.string.edit_person_name_title);
        editNameLayout = (RelativeLayout) findViewById(R.id.group_edit_name);
        editNameLayout.setVisibility(View.VISIBLE);
        celView = (TextView) findViewById(R.id.pub_top_left);
        celView.setBackgroundResource(R.drawable.group_cancel_selector);
        nameView = (EditText) editNameLayout.findViewById(R.id.group_edit_txt);
        nameView.setText(LoginModel.getUserinfoEntity().getRealname());
        nameView.setSelection(LoginModel.getUserinfoEntity().getRealname().length());
        editNameLayout.findViewById(R.id.group_edit_number).setVisibility(View.INVISIBLE);
        nameView.setVisibility(View.VISIBLE);
        clickView = (TextView) editNameLayout.findViewById(R.id.group_bottom_submit);
        nameTips = (TextView) editNameLayout.findViewById(R.id.top_tip_tx);
        nameTips.setText(R.string.edit_person_name_tip);
        clickView.setText(R.string.edit_person_name_ok);
        celView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonCoreUtil.hideSoftInput(nameView);
                EditPersonNameActivity.this.finish();
            }
        });
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void updateUserNameInfo(final String nameInfo) {
        rootView.loading(true);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("realname", nameInfo);
        new RequestUpdateUserInfoTask(this, hashMap, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                rootView.finish();
                LoginModel.getUserinfoEntity().setRealname(nameInfo);
                LoginModel.savaCacheData();
                Intent intent = new Intent();
                intent.putExtra("type", USER_EDIT_NAME_TYPE);
                intent.putExtra("editMsg", nameInfo);
                setResult(RESULT_OK, intent);
            }

            @Override
            public void dataError(int type, String msg) {
                rootView.finish();
                if (!TextUtils.isEmpty(msg)) {
                    Util.showToast(msg);
                } else {
                    Util.showToast(R.string.data_uploader_failed);
                }
            }
        }).start();
    }

    private void addClass() {
        if (mClassInfoBean != null && mClassInfoBean.getData().size() > 0) {
            ClassDetailBean dataEntity = mClassInfoBean.getData().get(0);
            String name = nameView.getText().toString();
            rootView.loading(true);
            requestAddGroupTask = new RequestAddClassTask(this, dataEntity.getId(), name, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    rootView.finish();
                    if (result != null) {
                        CommonCoreUtil.hideSoftInput(nameView);
                        requestBean = (RequestBean) result;
                        /***
                         * modify by frc
                         * code 为0时说明能正常审核   班级数据中的status为0时说明当前班级申请需要审核  反之不需要
                         */
                        if (requestBean.getStatus().getCode() == YanXiuConstant.SERVER_0) {
                            if (mClassInfoBean.getData().get(0).getStatus() == 0) {
                                Util.showUserToast(R.string.group_add_success, -1, -1);
                            } else {
                                Util.showUserToast(R.string.group_add_needcheck_one, R.string.group_add_needcheck_two, -1);
                            }
                        } else {
                            Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                        }
//                        if(requestBean.getStatus().getCode() == YanXiuConstant.SERVER_3){
//                           Util.showUserToast(R.string.group_add_success, -1, -1);
//                        }else if(requestBean.getStatus().getCode() == YanXiuConstant.SERVER_4){
//                            Util.showUserToast(R.string.group_add_needcheck_one, R.string.group_add_needcheck_two, -1);
//                        }else {
//                            Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
//                        }
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        EditPersonNameActivity.this.finish();
                    }
                }

                @Override
                public void dataError(int type, String msg) {
                    rootView.finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonCoreUtil.hideSoftInput(nameView);
    }
}
