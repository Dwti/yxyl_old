package com.yanxiu.gphone.hd.student.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.utils.MediaUtils;
import com.yanxiu.gphone.hd.student.utils.RightContainerUtils;
import com.yanxiu.gphone.hd.student.view.picsel.PicSelPopup;

/**
 * 专用于系统照相和相册选择
 * Created by Administrator on 2015/12/21.
 */
public class SystemPicSelPop extends PicSelPopup {
    private static final String TAG=SystemPicSelPop.class.getSimpleName();
    public SystemPicSelPop(Context mContext) {
        super(mContext);
        LinearLayout linear= (LinearLayout) view;
        linear.setGravity(Gravity.CENTER);
        this.pop.setWidth(RightContainerUtils
                .getInstance().getContainerWidth());
        this.pop.setHeight(RightContainerUtils.getInstance().getContainerHeight()-mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_44
        ));
//
//        LogInfo.log(TAG,"H: "+RightContainerUtils.getInstance().getContainerHeight());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.secLinear:
                if (CommonCoreUtil.sdCardMounted()){
                    MediaUtils.openSystemPic((Activity) mContext);
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
