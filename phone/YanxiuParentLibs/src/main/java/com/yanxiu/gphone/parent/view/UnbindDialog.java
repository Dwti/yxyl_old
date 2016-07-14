package com.yanxiu.gphone.parent.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.common.core.utils.imageloader.RotateImageViewAware;
import com.common.core.utils.imageloader.UniversalImageLoadTool;
import com.common.core.view.roundview.RoundedImageView;
import com.common.login.LoginModel;
import com.common.login.model.ParentInfo;
import com.common.login.model.ParentInfoBean;
import com.yanxiu.gphone.parent.R;

/**
 * Created by lidongming on 16/3/29.
 */
public class UnbindDialog extends Dialog{
    private Context mContext;
    private String headUrl;
    private UnbindCallBack unbindCallBack;
    private RoundedImageView childHeader;
    private TextView tvChildName, tvChildClass;
    private Button btnSure, btnCancel;

    public UnbindDialog(Context context, UnbindCallBack unbindCallBack) {
        super(context, R.style.unbind_dialog_style);
        setOwnerActivity((Activity) context);
        mContext = context;
        this.unbindCallBack = unbindCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initConfiger();
        setContentView(R.layout.parent_dialog_unbind);
        initView();
        initData();
    }


    private void initConfiger(){
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
    }

    private void initView(){
        childHeader = (RoundedImageView) this.findViewById(R.id.unbind_child_header);
        tvChildName = (TextView) this.findViewById(R.id.tv_unbind_dialog_child_name);
        tvChildClass = (TextView) this.findViewById(R.id.tv_unbind_dialog_child_class);
        btnSure = (Button) this.findViewById(R.id.btn_unbind_sure);
        btnCancel = (Button) this.findViewById(R.id.btn_unbind_cancel);

    }

    private void initData(){


        if(LoginModel.getRoleUserInfoEntity() != null && ((ParentInfo)LoginModel.getRoleUserInfoEntity()).getChild() != null){
            String childName = ((ParentInfo)LoginModel.getRoleUserInfoEntity()).getChild().getRealname();

            headUrl = ((ParentInfo)LoginModel.getRoleUserInfoEntity()).getChild().getHead();

            if(!TextUtils.isEmpty(childName)){
                tvChildName.setText(childName);
            }else{
                tvChildName.setText(mContext.getResources().getString(R.string.user_name_txt));
            }


        }else{
            tvChildName.setText(mContext.getResources().getString(R.string.user_name_txt));
        }

        if(LoginModel.getRoleLoginBean() != null
                && ((ParentInfoBean)LoginModel.getRoleLoginBean()).getProperty() != null
                && ((ParentInfoBean)LoginModel.getRoleLoginBean()).getProperty().getClassinfo() != null){

            String classInfo = ((ParentInfoBean)LoginModel.getRoleLoginBean()).getProperty().getClassinfo().getName();


            if(!TextUtils.isEmpty(classInfo)){
                tvChildClass.setText(classInfo);
            }else{
                tvChildClass.setText(mContext.getResources().getString(R.string.user_unknow_str));
            }

        }else{
            tvChildClass.setText(mContext.getResources().getString(R.string.user_unknow_str));
        }


        if(!TextUtils.isEmpty(headUrl)){
            UniversalImageLoadTool.disPlay(headUrl,
                    new RotateImageViewAware(childHeader, headUrl), R.drawable.parent_hearder_default);
        }else{
            UniversalImageLoadTool.disPlay("", new RotateImageViewAware(childHeader, ""), R.drawable.default_student_icon);
        }

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(unbindCallBack != null){
                    unbindCallBack.sureButton();
                }
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (unbindCallBack != null) {
                    unbindCallBack.cancelButton();
                }
            }
        });

    }


    public interface UnbindCallBack{
        void cancelButton();
        void sureButton();
    }


}
