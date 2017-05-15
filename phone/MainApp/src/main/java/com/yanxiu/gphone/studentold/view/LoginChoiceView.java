package com.yanxiu.gphone.studentold.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

//import com.yanxiu.gphone.parent.jump.utils.ParentActivityJumpUtils;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.activity.LoginActivity;

/**
 * Created by libo on 16/3/17.
 */
public class LoginChoiceView extends RelativeLayout implements View.OnClickListener {
    private ImageView studentBtn,parentBtn;
    private Context mContext;
    public LoginChoiceView(Context context) {
        super(context);
        initView(context);
    }

    public LoginChoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoginChoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoginChoiceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }
    private void initView(Context context){
        mContext=context;
        LayoutInflater.from(context).inflate(R.layout.login_choice_view_layout,this,true);
        studentBtn=(ImageView)findViewById(R.id.studentImg);
        parentBtn=(ImageView)findViewById(R.id.parentImg);
        studentBtn.setOnClickListener(this);
        parentBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.studentImg:
                LoginActivity.launcherActivity(mContext,0);
                break;
            case R.id.parentImg:
                //ParentActivityJumpUtils.jumpToParentLoginActivity(mContext);
                break;
        }
    }
}
