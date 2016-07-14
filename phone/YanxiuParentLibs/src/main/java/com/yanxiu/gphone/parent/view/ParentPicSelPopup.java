package com.yanxiu.gphone.parent.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.BasePopupWindow;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.utils.ParentMediaUtils;

/**
 * 用于照相和自定义相册选择
 * Created by Administrator on 2015/9/28.
 */
public class ParentPicSelPopup extends BasePopupWindow {

    public ParentPicSelPopup(Context mContext) {
        super(mContext);
    }

    @Override
    protected void initView(Context mContext) {
        View view=View.inflate(mContext, R.layout.parent_pic_sel_pop_view,null);
        this.pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.pop.setContentView(view);


        LinearLayout secLinear=(LinearLayout)view.findViewById(R.id.secLinear);


        LinearLayout thrLinear=(LinearLayout)view.findViewById(R.id.thrLinear);


        //取消
        TextView fourText=(TextView)view.findViewById(R.id.fourText);

        secLinear.setOnClickListener(this);
        thrLinear.setOnClickListener(this);
        fourText.setOnClickListener(this);
    }

    @Override
    public void loadingData() {

    }

    private  RelativeLayout.LayoutParams getLayoutParams(LinearLayout linear,int height){
        RelativeLayout.LayoutParams secParams= (RelativeLayout.LayoutParams) linear.getLayoutParams();
        secParams.height=height;
        return secParams;
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.secLinear) {
            if (CommonCoreUtil.sdCardMounted()) {
                String path = ParentMediaUtils.getOutputMediaFileUri(true).toString();
                if (StringUtils.isEmpty(path)) {
                    return;
                }
                ParentMediaUtils.openSystemCamera(((Activity) mContext), path, ParentMediaUtils.OPEN_SYSTEM_CAMERA);
            }
            dismiss();

        } else if (i == R.id.thrLinear) {
            if (CommonCoreUtil.sdCardMounted()) {
//                ActivityJumpUtils.jumpToImageBucketActivityForResult(((Activity) mContext), ParentMediaUtils.OPEN_DEFINE_PIC_BUILD);
            }
            dismiss();

        } else if (i == R.id.fourText) {
            dismiss();

        }
    }

    @Override
    protected void destoryData() {

    }


}
