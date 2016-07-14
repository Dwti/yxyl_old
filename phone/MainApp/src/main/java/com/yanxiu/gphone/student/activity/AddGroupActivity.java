package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.ClassInfoBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestClassInfoTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.PublicLoadLayout;
import com.yanxiu.gphone.student.view.passwordview.GridPasswordView;

/**
 * Created by Administrator on 2015/7/9.
 */
public class AddGroupActivity extends YanxiuBaseActivity{

    public final static int LAUNCHER_ADDGROUPACTIVITY = 0x01;

    public static void launchActivity(Activity activity,int requestCode){
        Intent intent = new Intent(activity,AddGroupActivity.class);
        activity.startActivityForResult(intent,requestCode);
    }

    private PublicLoadLayout rootView;

    private TextView backView;
    private TextView titleView;

    private RelativeLayout groupAddLayout;
    private GridPasswordView groupNumView;

    private TextView groupAddView;

    private RequestClassInfoTask requestClassInfoTask;

    private ClassInfoBean classInfoBean;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = PublicLoadUtils.createPage(this, R.layout.add_group_layout);
        setContentView(rootView);
        findView();
    }
    private void findView(){
        backView = (TextView)findViewById(R.id.pub_top_left);
        titleView = (TextView)findViewById(R.id.pub_top_mid);
        titleView.setText(R.string.group_add_title);
        groupAddLayout = (RelativeLayout)findViewById(R.id.no_group);
        groupAddLayout.setVisibility(View.VISIBLE);
        ((TextView) groupAddLayout.findViewById(R.id.top_tip_tx)).setText(R.string.group_add_tip);
        groupNumView = (GridPasswordView)groupAddLayout.findViewById(R.id.group_edit_number);
        groupNumView.setVisibility(View.VISIBLE);
        groupNumView.setBackgroundResource(R.drawable.group_input_bg);
        groupNumView.setPasswordVisibility(true);
        groupNumView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onChanged (String groupId) {
                LogInfo.log("haitian", "groupId = " + groupId);
            }

            @Override
            public void onMaxLength (String groupId) {
                LogInfo.log("haitian", "onMaxLength groupId = " + groupId);
//                Util.hideSoftInput(groupNumView);
//                requestGroupInfo();
            }
        });
        groupAddView = (TextView)groupAddLayout.findViewById(R.id.group_bottom_submit);
        groupAddView.setText(R.string.group_add_ok);

        backView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                CommonCoreUtil.hideSoftInput(groupNumView);
                AddGroupActivity.this.finish();
            }
        });

        groupAddView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (StringUtils.isEmpty(groupNumView.getPassWord())) {
                    Util.showUserToast(R.string.group_num_null, -1, -1);
                    return;
                }
                requestGroupInfo();
            }
        });
    }
    private void requestGroupInfo(){
        String groupNum = groupNumView.getPassWord();
        if(groupNum.length()<6 || groupNum.length()>8){
            Util.showUserToast(R.string.group_num_no_right, -1, -1);
            return;
        }
        rootView.loading(true);
        try {
            requestClassInfoTask = new RequestClassInfoTask(this, groupNum, new AsyncCallBack() {
                @Override public void update(YanxiuBaseBean result) {
                    rootView.finish();
                    if(result != null){
                        classInfoBean = (ClassInfoBean)result;
                        if(classInfoBean.getStatus().getCode() == YanXiuConstant.SERVER_0){
                            if (classInfoBean.getData().size() > 0 && classInfoBean.getData().get(0).getStatus() == YanXiuConstant.SERVER_2) {
                                //Util.showUserToast(R.string.group_add_no_right, -1, -1);
                                //该班级不允许加入
                                Util.showCustomTipToast(R.string.group_add_no_right);
                            } else {
                                CommonCoreUtil.hideSoftInput(groupNumView);
                                GroupInfoActivity.launchActivity(AddGroupActivity.this,
                                        GroupInfoActivity.ADD_CLASS, classInfoBean);
                            }

                        }else{
                            Util.showUserToast(classInfoBean.getStatus().getDesc(), null, null);
                        }
                    }
                }

                @Override public void dataError(int type, String msg) {
                    rootView.finish();
                    if(!StringUtils.isEmpty(msg)){
                        Util.showUserToast(msg, null, null);
                    } else{
                        Util.showUserToast(R.string.class_number_error, -1, -1);
                    }
                }
            });
            requestClassInfoTask.start();
        }catch (Exception e){
            rootView.finish();
            LogInfo.log("填写的群组号码为非数字");
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        CommonCoreUtil.hideSoftInput(groupNumView);
    }
}
