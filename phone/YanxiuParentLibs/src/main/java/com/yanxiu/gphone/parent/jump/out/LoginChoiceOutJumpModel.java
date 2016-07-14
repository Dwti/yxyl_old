package com.yanxiu.gphone.parent.jump.out;

import android.content.ComponentName;
import android.content.Intent;

import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;

/**
 * Created by lee on 16-3-30.
 */
public class LoginChoiceOutJumpModel extends BaseJumpOutModel {

    @Override
    public Intent getLaunchINtent() {
        Intent intent= new Intent();
        ComponentName componentName=new ComponentName(YanxiuParentConstants.MAIN_APP_PACKAGE_NAME,YanxiuParentConstants.MAIN_APP_CLASSNAME);
        intent.setComponent(componentName);
        return intent;
    }
}
