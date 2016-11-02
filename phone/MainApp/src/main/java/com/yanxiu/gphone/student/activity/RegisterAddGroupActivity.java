package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.ClassDetailBean;
import com.yanxiu.gphone.student.bean.ClassInfoBean;
import com.yanxiu.gphone.student.bean.RequestBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestAddClassTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/11/1.
 */

public class RegisterAddGroupActivity extends YanxiuBaseActivity implements View.OnClickListener {

    private PublicLoadLayout rootView;
    private Object messageFromIntent;
    private ClassDetailBean detailBean;
    private EditText name;
    private RequestBean requestBean;

    public static void launchActivity(Activity context,int type,ClassInfoBean classInfoBean){
        Intent intent = new Intent(context,RegisterAddGroupActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("classinfo",classInfoBean);
        intent.putExtra("bundle",bundle);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = PublicLoadUtils.createPage(this, R.layout.activity_registeraddgroup);
        setContentView(rootView);
        getMessageFromIntent();
        initview();
    }

    private void initview() {
        TextView school_name= (TextView) findViewById(R.id.school_name);
        school_name.setText("学校："+detailBean.getSchoolname());
        TextView class_name= (TextView) findViewById(R.id.class_name);
        class_name.setText("班级："+detailBean.getName());
        TextView teacher_name= (TextView) findViewById(R.id.teacher_name);
        teacher_name.setText("老师："+detailBean.getAdminName());
        name= (EditText) findViewById(R.id.name);
        TextView group_info_click= (TextView) findViewById(R.id.group_info_click);
        group_info_click.setOnClickListener(this);
    }

    public void getMessageFromIntent() {
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("bundle");
        ClassInfoBean classInfoBean= (ClassInfoBean) bundle.getSerializable("classinfo");
        detailBean=classInfoBean.getData().get(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_info_click:
                String user_name=name.getText().toString().trim();
                if (TextUtils.isEmpty(user_name)){
                    Util.showToast("请输入真实姓名");
                    return;
                }
                AddGroup(user_name);
                break;
        }
    }

    private void AddGroup(String user_name) {
        rootView.loading(true);
        RequestAddClassTask requestAddGroupTask = new RequestAddClassTask(this, detailBean.getId(), user_name, new AsyncCallBack() {
            @Override public void update(YanxiuBaseBean result) {
                rootView.finish();
                if(result != null){
                    CommonCoreUtil.hideSoftInput(name);
                    requestBean = (RequestBean)result;
                    if(requestBean.getStatus().getCode() == YanXiuConstant.SERVER_3){
                        Util.showUserToast(R.string.group_add_success, -1, -1);
                    }else if(requestBean.getStatus().getCode() == YanXiuConstant.SERVER_4){
                        Util.showUserToast(R.string.group_add_needcheck_one, R.string.group_add_needcheck_two, -1);
                       }else{
                        Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                    }
//                    Intent intent = new Intent();
//                    setResult(RESULT_OK,intent);
//                    RegisterAddGroupActivity.this.finish();
                }
            }

            @Override public void dataError(int type, String msg) {
                rootView.finish();
                if(!StringUtils.isEmpty(msg)){
                    Util.showUserToast(msg, null, null);
                } else{
                    Util.showUserToast(R.string.net_null_one, -1, -1);
                }
            }
        });
        requestAddGroupTask.start();
    }
}
