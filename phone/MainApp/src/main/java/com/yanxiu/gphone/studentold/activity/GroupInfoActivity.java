package com.yanxiu.gphone.studentold.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.StringUtils;
import com.common.core.view.roundview.RoundedImageView;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.base.YanxiuBaseActivity;
import com.yanxiu.gphone.studentold.bean.ClassDetailBean;
import com.yanxiu.gphone.studentold.bean.ClassInfoBean;
import com.yanxiu.gphone.studentold.bean.RequestBean;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.manager.ActivityManager;
import com.yanxiu.gphone.studentold.requestTask.RequestCancelClassTask;
import com.yanxiu.gphone.studentold.requestTask.RequestClassInfoTask;
import com.yanxiu.gphone.studentold.requestTask.RequestExitClassTask;
import com.yanxiu.gphone.studentold.utils.PublicLoadUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;
import com.yanxiu.gphone.studentold.view.DelDialog;
import com.yanxiu.gphone.studentold.view.PublicLoadLayout;

/**
 * Created by Administrator on 2015/7/9.
 */
public class GroupInfoActivity extends YanxiuBaseActivity{

    public static void launchActivity(Activity activity,int type,int classId){
        Intent intent = new Intent(activity,GroupInfoActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("classId",classId);
        if(type == CANCEL_CLASS || type == EXIT_CLASS){
            activity.startActivityForResult(intent,CANCEL_OR_EXIT_CLASSS_FOR_ACTIVITY);
        }else{
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            activity.startActivity(intent);
        }
        if(type == ADD_CLASS){
            activity.finish();
        }
    }

    public static void launchActivity(Activity activity,int type,ClassInfoBean classInfoBean){
        Intent intent = new Intent(activity,GroupInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("classInfoBean",classInfoBean);
        intent.putExtra("data", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        activity.startActivity(intent);
        if(type == ADD_CLASS){
            activity.finish();
        }
    }
    public final static int CANCEL_OR_EXIT_CLASSS_FOR_ACTIVITY = 0x12;
    private PublicLoadLayout rootView;
    private TextView celView;
    private TextView titleView;
    private TextView classNameView;
    private RelativeLayout classInfoLayout;
    private TextView classNumView;
    private TextView banzhurenView;
    private TextView peopleView;
    private TextView clickView;
    private RoundedImageView groupInfoIcon;

    private int type;
    public final static int ADD_CLASS = 1;
    public final static int EXIT_CLASS = 2;
    public final static int CANCEL_CLASS = 3;

    private ClassInfoBean mClassInfoBean;

    private String classId;

    private DelDialog delDialog;

    private DelDialog cancelDialog;

    private RequestExitClassTask requestExitClassTask;

    private RequestCancelClassTask requestCancelClassTask;

    private RequestClassInfoTask requestGroupInfoTask;

    private RequestBean requestBean;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = PublicLoadUtils.createPage(this, R.layout.group_info_layout);
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override public void refreshData() {
                requestGroupInfo();
            }
        });
        setContentView(rootView);
        Intent intent = getIntent();
        if(intent!=null){
            classId = intent.getIntExtra("classId", 0)+"";
            type = intent.getIntExtra("type", 1);
            Bundle bundle = intent.getBundleExtra("data");
            if(bundle!=null){
                mClassInfoBean = (ClassInfoBean)bundle.getSerializable("classInfoBean");
            }
        }
        findView();
    }

    private void findView(){
        titleView = (TextView)findViewById(R.id.pub_top_mid);
        titleView.setText(R.string.group_info_title);
        celView = (TextView)findViewById(R.id.pub_top_left);
        groupInfoIcon = (RoundedImageView)findViewById(R.id.group_info_icon);
        groupInfoIcon.setCornerRadius(getResources().getDimensionPixelOffset(R.dimen.dimen_12));
        celView.setBackgroundResource(R.drawable.group_cancel_selector);
        classNameView = (TextView)findViewById(R.id.group_info_name);
        classInfoLayout = (RelativeLayout)findViewById(R.id.group_layout);
        classNumView = (TextView)findViewById(R.id.group_info_num);
        banzhurenView = (TextView)findViewById(R.id.group_info_kemu_text);
        peopleView = (TextView)findViewById(R.id.group_info_people_text);
        clickView = (TextView)findViewById(R.id.group_info_click);

        celView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                GroupInfoActivity.this.finish();
            }
        });

        if(type == ADD_CLASS){
            clickView.setText(R.string.group_info_add);
//            clickView.setTextColor(getResources().getColor(R.color.color_white));
//            clickView.setBackgroundResource(R.drawable.group_add_ok_border);
        }else if(type == EXIT_CLASS){
            clickView.setText(R.string.group_info_del);
//            clickView.setTextColor(getResources().getColor(R.color.color_ff40c0fd));
//            clickView.setBackgroundResource(R.drawable.group_add_edit_border);
        }else if(type == CANCEL_CLASS){
            clickView.setText(R.string.class_info_cel);
//            clickView.setTextColor(getResources().getColor(R.color.color_ff40c0fd));
//            clickView.setBackgroundResource(R.drawable.group_add_edit_border);
        }
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (type == ADD_CLASS) {
                    EditPersonNameActivity.launchActivity(
                            GroupInfoActivity.this, mClassInfoBean);
                } else if((type == EXIT_CLASS)){
                    if (delDialog == null) {
                        String title = getResources()
                                .getString(R.string.class_del_title);
                        String sure = getResources()
                                .getString(R.string.class_cel_sure);
                        String cel = getResources()
                                .getString(R.string.class_del_cel);
                        delDialog = new DelDialog(GroupInfoActivity.this, title,
                                sure, cel, new DelDialog.DelCallBack() {
                            @Override public void del() {
                                exitClass();
                            }

                            @Override public void sure() {

                            }

                            @Override public void cancel() {

                            }
                        });
                    }
                    delDialog.show();
                }else if((type == CANCEL_CLASS)){
                    if (cancelDialog == null) {
                        String title = getResources()
                                .getString(R.string.class_cel_title);
                        String sure = getResources()
                                .getString(R.string.class_cel_sure);
                        String cel = getResources()
                                .getString(R.string.class_del_cel);
                        cancelDialog = new DelDialog(GroupInfoActivity.this, title,
                                sure, cel, new DelDialog.DelCallBack() {
                            @Override public void del() {
                                cancelClass();
                            }

                            @Override public void sure() {
                            }
                            @Override public void cancel() {

                            }
                        });
                    }
                    cancelDialog.show();
                }
            }
        });
        if(mClassInfoBean == null){
            requestGroupInfo();
        }else{
            setData();
        }
    }

    private void requestGroupInfo(){
        rootView.loading(true);
        requestGroupInfoTask = new RequestClassInfoTask(this, classId, new AsyncCallBack() {
            @Override public void update(YanxiuBaseBean result) {
                rootView.finish();
                if(result != null){
                    mClassInfoBean = (ClassInfoBean)result;
                    if(mClassInfoBean.getStatus().getCode() == YanXiuConstant.SERVER_0){
                        classInfoLayout.setVisibility(View.VISIBLE);
                        setData();
                    }else{
                        classInfoLayout.setVisibility(View.GONE);
                        rootView.netError(true);
                    }
                }
            }

            @Override public void dataError(int type, String msg) {
                classInfoLayout.setVisibility(View.GONE);
                rootView.netError(true);
            }
        });
        requestGroupInfoTask.start();
    }

    private void setData(){
        if(mClassInfoBean!=null && mClassInfoBean.getData().size()>0){
            classInfoLayout.setVisibility(View.VISIBLE);
            ClassDetailBean dataEntity = mClassInfoBean.getData().get(0);
            classNameView.setText(dataEntity.getName());
            classNumView.setText(getResources()
                    .getString(R.string.group_info_num, dataEntity.getId()));
            banzhurenView.setText(dataEntity.getAdminName());
            peopleView.setText(dataEntity.getStdnum()+"");
        }
    }

    private void exitClass(){
        if(mClassInfoBean!=null && mClassInfoBean.getData().size()>0){
            ClassDetailBean dataEntity = mClassInfoBean.getData().get(0);
            requestExitClassTask = new RequestExitClassTask(this,dataEntity.getId(), new AsyncCallBack() {
                @Override public void update(YanxiuBaseBean result) {
                    if(result!=null){
                        requestBean = (RequestBean)result;
                        if(requestBean.getStatus().getCode() == YanXiuConstant.SERVER_0){
                            Util.showUserToast(R.string.group_item_del_success, -1, -1);
                            setResult(RESULT_OK,new Intent());
                            GroupInfoActivity.this.finish();
                            ActivityManager.destoryHwActivity();
                        }else{
                            Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                        }
                    }
                }

                @Override public void dataError(int type, String msg) {
                    if(!StringUtils.isEmpty(msg)){
                        Util.showUserToast(msg, null, null);
                    } else{
                        Util.showUserToast(R.string.net_null_one, -1, -1);
                    }
                }
            });
            requestExitClassTask.start();
        }
    }

    private void cancelClass(){
        if(mClassInfoBean!=null && mClassInfoBean.getData().size()>0){
            ClassDetailBean dataEntity = mClassInfoBean.getData().get(0);
            requestCancelClassTask = new RequestCancelClassTask(this,dataEntity.getId(), new AsyncCallBack() {
                @Override public void update(YanxiuBaseBean result) {
                    if(result!=null){
                        requestBean = (RequestBean)result;
                        if(requestBean.getStatus().getCode() == YanXiuConstant.SERVER_0){
                            Util.showUserToast(R.string.group_item_cancel_success, -1, -1);
                            setResult(RESULT_OK,new Intent());
                            GroupInfoActivity.this.finish();
                            ActivityManager.destoryHwActivity();
                        }else if(requestBean.getStatus().getCode() == 75){
                            Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                            setResult(RESULT_OK,new Intent());
                            GroupInfoActivity.this.finish();
                            ActivityManager.destoryHwActivity();
                        }else{
                            Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                        }
                    }
                }

                @Override public void dataError(int type, String msg) {
                    if(!StringUtils.isEmpty(msg)){
                        Util.showUserToast(msg, null, null);
                    } else{
                        Util.showUserToast(R.string.net_null_one, -1, -1);
                    }
                }
            });
            requestCancelClassTask.start();
        }
    }

}
