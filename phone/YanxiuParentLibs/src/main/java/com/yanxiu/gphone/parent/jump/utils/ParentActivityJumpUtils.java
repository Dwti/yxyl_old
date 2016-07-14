package com.yanxiu.gphone.parent.jump.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.common.core.utils.ContextProvider;
import com.yanxiu.gphone.parent.activity.ParentBindAccountActivity;
import com.yanxiu.gphone.parent.activity.ParentBindDataShowActivity;
import com.yanxiu.gphone.parent.activity.ParentLoginActivity;
import com.yanxiu.gphone.parent.activity.ParentSetPsdActivity;
import com.yanxiu.gphone.parent.activity.ParentVerifyActivity;
import com.yanxiu.gphone.parent.jump.BaseJumModelForResult;
import com.yanxiu.gphone.parent.jump.BaseJumpModel;
import com.yanxiu.gphone.parent.jump.ParentBindJumpModel;
import com.yanxiu.gphone.parent.jump.ParentBindShowJumpModel;
import com.yanxiu.gphone.parent.jump.ParentLoginJumpModel;
import com.yanxiu.gphone.parent.jump.ParentSetPsdJumpModel;
import com.yanxiu.gphone.parent.jump.ParentVerifyJumpModel;
import com.yanxiu.gphone.parent.jump.constants.JumpModeConstants;


/**
 * Activity 跳转工具类
 *
 * 命名规范：
 *
 * 1. 以 startActivity方式启动方法命名： jumpTo+xxxActivity  如 jumpToWelcomeActivity
 * 2. 以 startActivityForResult方式启动方法命名： jumpTo+xxxActivity+ForResult如 jumpToSearchActivityForResult
 * 2. 从Activity setResult 返回：jumpBackFrom+xxxActivity 如 jumpBackFromSearchActivity
 *
 * startActivity调用统一入口：  jumpToPageCommonMethod
 * startActivityForResult调用统一入口： jumpToPageForResultCommonMethod
 * setResult调用统一入口：  jumpBackCommonMethod
 */
public class ParentActivityJumpUtils {

    /******************************* 统一内跳方式*********************************/


    /**
     * 统一的SetResult方法
     *
     * @param model
     * @param activity
     * @param result
     */
    private static void jumpBackCommonMethod(BaseJumpModel model, Activity activity, int result) {
        Intent intent = new Intent();
        intent.putExtra(JumpModeConstants.JUMP_MODEL_KEY, model);
        activity.setResult(result, intent);
    }


    /**
     * 统一startActivityForResult入口
     *
     * @param model    BaseJumpModelForResult startActivityForResult跳转数据存储基类
     * @param activity 上下文
     */
    private static void jumpToPageForResultCommonMethod(BaseJumModelForResult model,
                                                        Activity activity) {
        Intent intent = new Intent(activity, model.getTargetActivity());
        intent.putExtra(JumpModeConstants.JUMP_MODEL_KEY, model);
        activity.startActivityForResult(intent, model.getRequestCode());

    }


    /**
     * 统一跳转逻辑
     *
     * @param model ：BaseJumpModel跳转数据存储基类
     */
    private static void jumpToPageCommonMethod(BaseJumpModel model,
                                               Context context) {
        // 引用applicationContext，为了不持有ExternalJumpActivity的context对象，导致无法释放问题
        Context contexts = (context != null) ? context : ContextProvider.getApplicationContext();
        Intent intent = new Intent(contexts, model.getTargetActivity());
        Bundle bundle = new Bundle();
        bundle.putSerializable(JumpModeConstants.JUMP_MODEL_KEY, model);
        intent.putExtras(bundle);
        if (contexts instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        contexts.startActivity(intent);

    }


    /****************************** 此处以下编写具体的跳转方法  ************************************************/
    public static void jumpToParentLoginActivity(Context context){
        ParentLoginJumpModel jumpModel=new ParentLoginJumpModel();
        jumpModel.setTargetActivity(ParentLoginActivity.class);
        jumpToPageCommonMethod(jumpModel, context);
    }

    public static void jumpToParentBindAccountActivity(Context context,int from){
        ParentBindJumpModel jumpModel=new ParentBindJumpModel();
        jumpModel.setTargetActivity(ParentBindAccountActivity.class);
        jumpModel.setForm(from);
        jumpToPageCommonMethod(jumpModel,context);
    }

    public static void jumpToParentBindShowActivity(Context context,String stdUid,String schoolName,String head,String realName){
        ParentBindShowJumpModel jumpModel=new ParentBindShowJumpModel();
        jumpModel.setTargetActivity(ParentBindDataShowActivity.class);
        jumpModel.setSchoolName(schoolName);
        jumpModel.setHead(head);
        jumpModel.setRealName(realName);
        jumpModel.setStdUid(stdUid);
        jumpToPageCommonMethod(jumpModel,context);
    }

    public static void jumpToParentVerifyActivity(Context context,int from){
        ParentVerifyJumpModel jumpModel=new ParentVerifyJumpModel();
        jumpModel.setFrom(from);
        jumpModel.setTargetActivity(ParentVerifyActivity.class);
        jumpToPageCommonMethod(jumpModel,context);
    }

    public static void jumpToParentSetPsdActivity(Context context,int from,String mobile,String verifyCode){
        ParentSetPsdJumpModel jumpModel=new ParentSetPsdJumpModel();
        jumpModel.setFrom(from);
        jumpModel.setMobile(mobile);
        jumpModel.setVerifyCode(verifyCode);
        jumpModel.setTargetActivity(ParentSetPsdActivity.class);
        jumpToPageCommonMethod(jumpModel,context);
    }



}
