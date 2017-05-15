package com.yanxiu.gphone.studentold.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.utils.MediaUtils;
import com.common.core.utils.StringUtils;
import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.studentold.view.picsel.PicSelPopup;

/**
 * 专用于系统照相和相册选择
 * Created by Administrator on 2015/12/21.
 */
public class SystemPicSelPop extends PicSelPopup {
    public SystemPicSelPop(Context mContext) {
        super(mContext);
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
