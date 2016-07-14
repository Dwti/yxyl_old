package com.yanxiu.gphone.parent.jump.out.utils;

import android.content.Context;
import android.content.Intent;

import com.common.core.utils.ContextProvider;
import com.yanxiu.gphone.parent.jump.out.BaseJumpOutModel;
import com.yanxiu.gphone.parent.jump.out.LoginChoiceOutJumpModel;

/**
 * Created by lee on 16-3-30.
 */
public class ParentOutActivityJumpUtils {

    private static void jumpOutPageCommonMethod(Context context,BaseJumpOutModel model){
        Context contexts=(context!=null)?context: ContextProvider.getApplicationContext();
        Intent intent=model.getLaunchINtent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        contexts.startActivity(intent);
    }
    /********************************* ***************************************/
    public static void jumpOutToLoginChoiceActivity(Context context){
        jumpOutPageCommonMethod(context,new LoginChoiceOutJumpModel());
    }

}
