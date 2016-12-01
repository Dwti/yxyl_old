package com.yanxiu.gphone.student.jump.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.activity.ExamPointActivity;
import com.yanxiu.gphone.student.activity.FeedBackActivity;
import com.yanxiu.gphone.student.activity.ImageBucketActivity;
import com.yanxiu.gphone.student.activity.ImagePicSelActivity;
import com.yanxiu.gphone.student.activity.LocalPhotoViewActivity;
import com.yanxiu.gphone.student.activity.LoginChoiceActivity;
import com.yanxiu.gphone.student.activity.NoGroupAddTipsActivity;
import com.yanxiu.gphone.student.activity.RankingListActivity;
import com.yanxiu.gphone.student.activity.ThirdExamSiteActivity;
import com.yanxiu.gphone.student.bean.ExamInfoBean;
import com.yanxiu.gphone.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.student.jump.BaseJumModelForResult;
import com.yanxiu.gphone.student.jump.BaseJumpModel;
import com.yanxiu.gphone.student.jump.ExamPointJumpModel;
import com.yanxiu.gphone.student.jump.FeedbackJumpModel;
import com.yanxiu.gphone.student.jump.ImageBucketJumpBackModel;
import com.yanxiu.gphone.student.jump.ImageBucketJumpBean;
import com.yanxiu.gphone.student.jump.ImagePicSelJumpBackModel;
import com.yanxiu.gphone.student.jump.ImagePicSelJumpModel;
import com.yanxiu.gphone.student.jump.LocalPhotoViewJumpBackModel;
import com.yanxiu.gphone.student.jump.LocalPhotoViewJumpModel;
import com.yanxiu.gphone.student.jump.LoginChoiceJumpModel;
import com.yanxiu.gphone.student.jump.NoGroupAddTipsJumpModel;
import com.yanxiu.gphone.student.jump.RankingListJumpModel;
import com.yanxiu.gphone.student.jump.ThirdExamSiteJumpBackModel;
import com.yanxiu.gphone.student.jump.ThirdExamSiteJumpModel;
import com.yanxiu.gphone.student.jump.constants.JumpModeConstants;


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
public class ActivityJumpUtils {

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
        Context contexts = (context != null) ? context : YanxiuApplication.getInstance();
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

    public static void jumpToRankingListActivity(Context context){
        RankingListJumpModel jumpModel=new RankingListJumpModel();
        jumpModel.setTargetActivity(RankingListActivity.class);
        jumpToPageCommonMethod(jumpModel, context);
    }

    /**
     * 跳转到相册目录
     * @param context
     * @param requestCode
     */
    public static void jumpToImageBucketActivityForResult(Activity context,int requestCode){
        ImageBucketJumpBean jumpModel=new ImageBucketJumpBean();
        jumpModel.setTargetActivity(ImageBucketActivity.class);
        jumpModel.setRequestCode(requestCode);
        jumpToPageForResultCommonMethod(jumpModel, context);

    }

    public static void jumpBackFromImageBucketActivity(Activity context,int resultCode){
        ImageBucketJumpBackModel jumpBackModel=new ImageBucketJumpBackModel();
        jumpBackCommonMethod(jumpBackModel, context, resultCode);
    }


    /**
     * 跳转到相册目录下的相册
     * @param context
     * @param pos
     */
    public static void jumpToImagePicSelActivityForResult(Activity context,int pos,int requestCode){
        ImagePicSelJumpModel jumpModel=new ImagePicSelJumpModel();
        jumpModel.setSelPos(pos);
        jumpModel.setRequestCode(requestCode);
        jumpModel.setTargetActivity(ImagePicSelActivity.class);
        jumpToPageForResultCommonMethod(jumpModel, context);
    }

    public static void jumpBackFromImagePicSelActivity(Activity context,boolean isAddList,int resultCode){
        ImagePicSelJumpBackModel jumpBackModel=new ImagePicSelJumpBackModel();
        jumpBackModel.setIsAddList(isAddList);
        jumpBackCommonMethod(jumpBackModel, context, resultCode);
    }



    /**
     * 跳转到本地图片查看
     */

    public static void jumpToLocalPhotoViewActivityForResult(Activity context,int pos,int requestCode){
        LocalPhotoViewJumpModel jumpModel=new LocalPhotoViewJumpModel();
        jumpModel.setTargetActivity(LocalPhotoViewActivity.class);
        jumpModel.setRequestCode(requestCode);
        jumpModel.setPos(pos);
        jumpToPageForResultCommonMethod(jumpModel, context);
    }

    public static void jumpBackFromLocalPhotoViewActivity(Activity context,int resultCode){
        LocalPhotoViewJumpBackModel jumpBackModel=new LocalPhotoViewJumpBackModel();
        jumpBackCommonMethod(jumpBackModel, context, resultCode);
    }

    /**
     * 跳转到反馈
     * @param context
     * @param typeCode  ADVICE_FEED_BAck ： 跳转到意见反馈
     *                  ERROR_FEED_BACK  ：跳转到题目报错
     */
    public static void jumpToFeedBackActivity(Context context,int typeCode){
        FeedbackJumpModel jumpModel=new FeedbackJumpModel();
        jumpModel.setTargetActivity(FeedBackActivity.class);
        jumpModel.setTypeCode(typeCode);
        jumpToPageCommonMethod(jumpModel, context);
    }

    /**
     * 跳转到反馈
     * @param context
     * @param typeCode  ADVICE_FEED_BAck ： 跳转到意见反馈
     *                  ERROR_FEED_BACK  ：跳转到题目报错
     */
    public static void jumpToFeedBackActivity(Context context,String questionId,int typeCode){
        FeedbackJumpModel jumpModel=new FeedbackJumpModel();
        jumpModel.setTargetActivity(FeedBackActivity.class);
        jumpModel.setQuestionId(questionId);
        jumpModel.setTypeCode(typeCode);
        jumpToPageCommonMethod(jumpModel, context);
    }



    /**
     * 跳转到考点分析
     */

    public static void jumpToExamPointActivity(Context context,SubjectVersionBean.DataEntity dataEntity){
        ExamPointJumpModel jumpModel=new ExamPointJumpModel();
        jumpModel.setTargetActivity(ExamPointActivity.class);
        jumpModel.setDataEntity(dataEntity);
        jumpToPageCommonMethod(jumpModel,context);
    }

    /**
     * 跳转到三级考点列表
     */
    public static void jumpToThirdExamSiteActivity(Context context,SubjectVersionBean.DataEntity mDataEntity,ExamInfoBean... examPointBean){
        ThirdExamSiteJumpModel jumpModel=new ThirdExamSiteJumpModel();
        jumpModel.setTargetActivity(ThirdExamSiteActivity.class);
        jumpModel.setmDataEntity(mDataEntity);
        jumpModel.setFirstExamBean(examPointBean[0]);
        jumpModel.setSecondExamBean(examPointBean[1]);
        jumpToPageCommonMethod(jumpModel,context);
    }

    /**
     * 跳转到三级考点列表
     */
    public static void jumpToThirdExamSiteActivityForResult(Activity context,int requestCode,SubjectVersionBean.DataEntity mDataEntity,ExamInfoBean... examPointBean){
        ThirdExamSiteJumpModel jumpModel=new ThirdExamSiteJumpModel();
        jumpModel.setTargetActivity(ThirdExamSiteActivity.class);
        jumpModel.setmDataEntity(mDataEntity);
        jumpModel.setFirstExamBean(examPointBean[0]);
        jumpModel.setSecondExamBean(examPointBean[1]);
        jumpModel.setRequestCode(requestCode);
        jumpToPageForResultCommonMethod(jumpModel, context);
    }



    /**
     * 从三级考点返回
     *
     */
    public static void jumpBackFromThirdExamSiteActivity(Activity context,ExamInfoBean chapterExam,int resultCode){
        ThirdExamSiteJumpBackModel jumpBackModel=new ThirdExamSiteJumpBackModel();
        jumpBackModel.setTargetActivity(ExamPointActivity.class);
        jumpBackModel.setChapterExam(chapterExam);
        jumpBackCommonMethod(jumpBackModel, context, resultCode);
    }

    /**
     * 跳转到登录选择页面
     *
     */
    public static void jumpToLoginChoiceActivity(Context context){
        LoginChoiceJumpModel jumpModel=new LoginChoiceJumpModel();
        jumpModel.setTargetActivity(LoginChoiceActivity.class);
        jumpToPageCommonMethod(jumpModel, context);
    }

    /**
     * 跳转到加入班级提示页面
     */
    public static void jumpToNoGroupAddTipsActivity(Context context){
        NoGroupAddTipsJumpModel jumpModel=new NoGroupAddTipsJumpModel();
        jumpModel.setTargetActivity(NoGroupAddTipsActivity.class);
        jumpToPageCommonMethod(jumpModel,context);
    }


}
