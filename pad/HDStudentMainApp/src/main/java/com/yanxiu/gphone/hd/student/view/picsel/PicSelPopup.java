package com.yanxiu.gphone.hd.student.view.picsel;

import android.app.Activity;
import android.content.Context;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.BasePopupWindow;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.hd.student.utils.MediaUtils;

/**
 * 用于照相和自定义相册选择
 * Created by Administrator on 2015/9/28.
 */
public class PicSelPopup extends BasePopupWindow {
    protected View view;
    public PicSelPopup(Context mContext) {
        super(mContext);
    }

    @Override
    protected void initView(Context mContext) {
        view =View.inflate(mContext, R.layout.pic_sel_pop_view,null);
        this.pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.pop.setContentView(view);
        TextView firText=(TextView)view.findViewById(R.id.firText);
        firText.setText(mContext.getResources().getString(R.string.photo_tips));


        LinearLayout secLinear=(LinearLayout)view.findViewById(R.id.secLinear);

        secLinear.setLayoutParams(getLayoutParams(secLinear, CommonCoreUtil.dipToPx(mContext, 51)));

        TextView secText=(TextView)view.findViewById(R.id.secText);
        secText.setText(mContext.getResources().getString(R.string.pic_bucket));
        TextPaint secTextPaint=secText.getPaint();
        secTextPaint.setFakeBoldText(true);

        secTextPaint.setShadowLayer(2F,0F,4F,  mContext.getResources().getColor(R.color.color_ffff99));

        ImageView secImg=(ImageView)view.findViewById(R.id.secImg);
        secImg.setVisibility(View.VISIBLE);

        LinearLayout thrLinear=(LinearLayout)view.findViewById(R.id.thrLinear);
        thrLinear.setLayoutParams(getLayoutParams(thrLinear, CommonCoreUtil.dipToPx(mContext, 51)));

        TextView thrText=(TextView)view.findViewById(R.id.thrText);
        thrText.setText(mContext.getResources().getString(R.string.open_camera));
        TextPaint thrTextPaint=thrText.getPaint();
        thrTextPaint.setFakeBoldText(true);
        thrText.setShadowLayer(2F, 0F, 4F, mContext.getResources().getColor(R.color.color_ffff99));


        ImageView thrImg=(ImageView)view.findViewById(R.id.thrImg);
        thrImg.setVisibility(View.VISIBLE);

        //取消
        TextView fourText=(TextView)view.findViewById(R.id.fourText);
        fourText.setVisibility(View.VISIBLE);
        fourText.setText(mContext.getResources().getString(R.string.cancel_txt));
        fourText.setShadowLayer(2F, 0F, 4F, mContext.getResources().getColor(R.color.color_ffff99));

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
        switch (view.getId()){

            case R.id.secLinear:
                if (CommonCoreUtil.sdCardMounted()){
                    ActivityJumpUtils.jumpToImageBucketActivityForResult(((Activity) mContext), MediaUtils.OPEN_DEFINE_PIC_BUILD);
                }
                dismiss();
                break;
            case R.id.thrLinear:
                if (CommonCoreUtil.sdCardMounted()) {
                    String path=MediaUtils.getOutputMediaFileUri(true).toString();
                    if(StringUtils.isEmpty(path)){
                        return;
                    }
                    MediaUtils.openSystemCamera(((Activity) mContext), path, MediaUtils.OPEN_SYSTEM_CAMERA);
                }
                dismiss();
                break;
            case R.id.fourText:
                dismiss();
                break;
        }
    }

    @Override
    protected void destoryData() {

    }


}
