package com.yanxiu.gphone.studentold.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.base.YanxiuBaseActivity;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.requestTask.RequestUpdateUserInfoTask;
import com.yanxiu.gphone.studentold.utils.PublicLoadUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.view.PublicLoadLayout;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/10.
 */
public class UserNameEditActivity extends YanxiuBaseActivity implements View.OnClickListener{
    public final static int USER_EDIT_REQUEST_CODE = 0x100;
    public final static int USER_EDIT_NAME_TYPE = 0x101;
    public final static int USER_EDIT_NICK_NAME_TYPE = 0x102;
    private PublicLoadLayout rootView;
    private View backView;
    private View topView;
    private TextView titleView, saveView;
    private EditText editText;
    private int type;
    private String editMsg;
    private ImageView clearIcon;
    public static void launch(Activity activity, int type, String editMsg){
        Intent intent = new Intent(activity, UserNameEditActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("editMsg", editMsg);
        activity.startActivityForResult(intent, USER_EDIT_REQUEST_CODE);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = PublicLoadUtils.createPage(this, R.layout.activity_edit_name_user_layout);
        setContentView(rootView);
        rootView.setBackgroundResource(R.color.trans);

        type = getIntent().getIntExtra("type", USER_EDIT_NAME_TYPE);
        editMsg = getIntent().getStringExtra("editMsg");
        if(!TextUtils.isEmpty(editMsg) && editMsg.length() > 25){
            editMsg = editMsg.substring(0, 24);
        }
        findView();
    }
    private void findView(){
        topView = rootView.findViewById(R.id.top_layout);
        backView = topView.findViewById(R.id.pub_top_left);
        titleView = (TextView) topView.findViewById(R.id.pub_top_mid);
        saveView = (TextView) topView.findViewById(R.id.pub_top_right);
        clearIcon=(ImageView)rootView.findViewById(R.id.clearContentIcon);
        RelativeLayout.LayoutParams saveViewParams= (RelativeLayout.LayoutParams) saveView.getLayoutParams();
        saveViewParams.width=getResources().getDimensionPixelOffset(R.dimen.dimen_47);
        saveViewParams.height=getResources().getDimensionPixelOffset(R.dimen.dimen_31);
        saveView.setLayoutParams(saveViewParams);
        saveView.setGravity(Gravity.CENTER);
        saveView.setTextColor(getResources().getColor(R.color.color_006666));
        saveView.setBackgroundResource(R.drawable.edit_save_selector);
        
        editText = (EditText) rootView.findViewById(R.id.user_name_edit);
        if(!TextUtils.isEmpty(editMsg)){
            editText.setText(editMsg);
            editText.setSelection(editMsg.length());
        }
        backView.setOnClickListener(this);
        saveView.setOnClickListener(this);
        clearIcon.setOnClickListener(this);
        if(type == USER_EDIT_NICK_NAME_TYPE){
            titleView.setText(R.string.user_nick_name);
        } else {
            titleView.setText(R.string.user_name_str);
        }
        saveView.setText(R.string.user_name_edit_save);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.pub_top_left:
                finishActivity();
                break;
            case R.id.pub_top_right:
                String msg = editText.getEditableText().toString();
                if(TextUtils.isEmpty(msg)){
                    Util.showToast(getString(R.string.user_edit_info_is_null, titleView.getText()));
                    return;
                } else {
                    updateInfo(msg);
                }
                break;
            case R.id.clearContentIcon:
                editText.setText("");
                break;
        }


    }
    private void updateInfo(final String nameInfo){
        rootView.loading(true);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if(type == USER_EDIT_NICK_NAME_TYPE){
            hashMap.put("nickname", nameInfo);
        } else {
            hashMap.put("realname", nameInfo);
        }
        new RequestUpdateUserInfoTask(this, hashMap, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                rootView.finish();
                if(type == USER_EDIT_NICK_NAME_TYPE){
                    LoginModel.getUserinfoEntity().setNickname(nameInfo);
                } else {
                    LoginModel.getUserinfoEntity().setRealname(nameInfo);
                }
                LoginModel.savaCacheData();
                Intent intent = new Intent();
                intent.putExtra("type", type);
                intent.putExtra("editMsg", nameInfo);
                setResult(RESULT_OK, intent);
                finishActivity();
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
    @Override
    protected void onPause() {
        super.onPause();
        CommonCoreUtil.hideSoftInput(editText);
    }

    private void finishActivity() {
        CommonCoreUtil.hideSoftInput(editText);
        finish();
    }
}
