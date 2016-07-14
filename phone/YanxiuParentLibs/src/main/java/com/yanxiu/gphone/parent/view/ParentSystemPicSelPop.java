package com.yanxiu.gphone.parent.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.utils.ParentMediaUtils;

/**
 * 专用于系统照相和相册选择
 * Created by Administrator on 2015/12/21.
 */
public class ParentSystemPicSelPop extends ParentPicSelPopup {

    public ParentSystemPicSelPop(Context mContext) {
        super(mContext);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.secLinear) {
            if (CommonCoreUtil.sdCardMounted()) {
                ParentMediaUtils.openSystemPic((Activity) mContext);
            }
            dismiss();

        } else if (i == R.id.thrLinear) {
            if (CommonCoreUtil.sdCardMounted()) {
                String path = ParentMediaUtils.getOutputMediaFileUri(true).toString();
                if (StringUtils.isEmpty(path)) {
                    return;
                }
                ParentMediaUtils.openSystemCamera(((Activity) mContext), path, ParentMediaUtils.OPEN_SYSTEM_CAMERA);
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
