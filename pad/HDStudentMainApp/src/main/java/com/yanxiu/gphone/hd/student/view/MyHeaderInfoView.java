package com.yanxiu.gphone.hd.student.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.StringUtils;
import com.common.core.utils.imageloader.RotateImageViewAware;
import com.common.core.utils.imageloader.UniversalImageLoadTool;
import com.common.core.view.roundview.RoundedImageView;
import com.common.login.LoginModel;
import com.common.login.model.UserInfo;
import com.yanxiu.gphone.hd.student.R;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2016/1/27.
 */
public class MyHeaderInfoView extends RelativeLayout implements Observer {

    private TextView mUserTitle,accountTv;
    private RoundedImageView mImageView;

    public MyHeaderInfoView(Context context) {
        super(context);
        initView(context);
    }

    public MyHeaderInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyHeaderInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyHeaderInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }
    private void initView(Context context){

        setBackgroundResource(R.drawable.my_header_view_selector);
        LayoutInflater inflater=LayoutInflater.from(context);
        inflater.inflate(R.layout.my_header_fg_layout, this, true);
        AbsListView.LayoutParams params=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelOffset(R.dimen.dimen_83));
        setLayoutParams(params);
        accountTv =(TextView)findViewById(R.id.accountTv);
        mUserTitle=(TextView)findViewById(R.id.userTitle);
        mImageView=(RoundedImageView)findViewById(R.id.user_icon);
        mImageView.setCornerRadius(20);
        setHeadInfo();

    }

    private void setHeadInfo() {
        UserInfo userInfo= (UserInfo) LoginModel.getRoleUserInfoEntity();
        if(userInfo!=null&&!StringUtils.isEmpty(userInfo.getMobile())){
            accountTv.setText(String.format(getResources().getString(R.string.account_name),userInfo.getMobile()));
        }else{
            accountTv.setText(String.format(getResources().getString(R.string.account_name),""));
        }
        mUserTitle.setText(LoginModel.getUserinfoEntity().getNickname());

        UniversalImageLoadTool.disPlay(LoginModel.getUserinfoEntity().getHead(), new RotateImageViewAware(mImageView, LoginModel.getUserinfoEntity().getHead()), R.drawable.user_info_default_bg);
    }

    @Override
    public void update(Observable observable, Object o) {
        setHeadInfo();
    }
}
