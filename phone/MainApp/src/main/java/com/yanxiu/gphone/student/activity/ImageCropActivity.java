package com.yanxiu.gphone.student.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.commoninterface.OnTaskCompleteListener;
import com.yanxiu.gphone.student.preference.PreferencesManager;
import com.yanxiu.gphone.student.task.WriteBitmapToFileWorkerTask;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.utils.Utils;
import com.yanxiu.gphone.student.view.ImageCropOverView;

import java.io.File;

/**
 * Created by sp on 16-12-8.
 */

public class ImageCropActivity extends Activity implements View.OnClickListener, OnTaskCompleteListener<File> {

    private ImageCropOverView image_over_view;
    private ImageView iv_photo,iv_guide;
    private TextView tv_crop, tv_cancel;
    private View rl_crop;
    private View rl_guide_gesture_bg;
    private String imagePath;
    private Bitmap mBitmap;
    public static final int REQUEST_IMAGE_CROP = 0x04;
    private boolean isWritting = false;
    private Activity mActivity;
    public static final String IMAGE_PATH = "imagePath";
    private AnimatorSet animatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        mActivity = this;
        initView();
        initData();
        image_over_view.setTargetView(iv_photo);
    }

    private void initView() {
        rl_guide_gesture_bg = findViewById(R.id.rl_guide_gesture_bg);
        iv_guide = (ImageView) findViewById(R.id.iv_guide);
        iv_photo = (ImageView) findViewById(R.id.image);
        image_over_view = (ImageCropOverView) findViewById(R.id.image_over_view);
        tv_crop = (TextView) findViewById(R.id.tv_crop);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        rl_crop = findViewById(R.id.rl_crop);

        tv_crop.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        rl_guide_gesture_bg.setOnClickListener(this);
    }

    private void initData() {
        imagePath = getIntent().getStringExtra(IMAGE_PATH);
        if (TextUtils.isEmpty(imagePath)) {
            mBitmap = CameraActivity.bitmap;
            iv_photo.setImageBitmap(mBitmap);
        } else {
//            BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(new OnTaskCompleteListener<Bitmap>() {
//                @Override
//                public void onComplete(Bitmap bitmap) {
//                    if(bitmap == null){
//                        Toast.makeText(ImageCropActivity.this,"读取照片失败", Toast.LENGTH_SHORT).show();
//                        setResult(RESULT_CANCELED);
//                        finish();
//                        return;
//                    }
//                    iv_photo.setImageBitmap(bitmap);
//                    mBitmap = bitmap;
//                    image_over_view.postInvalidate();
//                }
//            });
////            bitmapWorkerTask.execute(imagePath, String.valueOf(rl_crop.getWidth()), String.valueOf(rl_crop.getHeight()));
//            bitmapWorkerTask.execute(imagePath, String.valueOf(Utils.getScreenWidth()), String.valueOf(Utils.getScreenHeight()));


            Glide.with(mActivity).load(imagePath).asBitmap().listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    Toast.makeText(mActivity, "读取照片失败", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                    return true;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    mBitmap = resource;
                    return false;
                }
            }).into(iv_photo);
        }
        startGuideAnimation();
    }

    private void startGuideAnimation(){
        if(PreferencesManager.getInstance().getFirstCorpQuestion()){
            rl_guide_gesture_bg.setVisibility(View.VISIBLE);
            ObjectAnimator translationX = ObjectAnimator.ofFloat(iv_guide, "x", Utils.getWindowWidth(), (Utils.getWindowWidth())/2);
            ObjectAnimator translationY = ObjectAnimator.ofFloat(iv_guide, "y", (Utils.getWindowHeight())*3/4, (Utils.getWindowHeight())/3);
            animatorSet = new AnimatorSet();
            animatorSet.playTogether(translationX, translationY);
            animatorSet.setDuration(2000);
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    rl_guide_gesture_bg.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animatorSet.start();
            PreferencesManager.getInstance().setFirstCorpQuestion();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_guide_gesture_bg:
                rl_guide_gesture_bg.setVisibility(View.GONE);
                break;
            case R.id.tv_crop:
                if (isWritting || mBitmap == null) {
                    return;
                }
                isWritting = true;
                Rect rect = image_over_view.getRect();

                Bitmap sourceBmp = Bitmap.createScaledBitmap(mBitmap, iv_photo.getWidth(), iv_photo.getHeight(), false);

                Bitmap crop_bitmap = Bitmap.createBitmap(sourceBmp, rect.left, rect.top,
                        rect.width(), rect.height());
                File outputFile = MediaUtils.getOutputMediaFile(true);
                isWritting = true;
                WriteBitmapToFileWorkerTask workerTask = new WriteBitmapToFileWorkerTask(this);
                workerTask.execute(outputFile, crop_bitmap);
                break;
            case R.id.tv_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(animatorSet!=null)
            animatorSet.end();
    }

    @Override
    public void onComplete(File file) {
        if (file != null) {
            Intent intent = new Intent();
            intent.putExtra(IMAGE_PATH, file.getAbsolutePath());
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "保存图片文件失败！", Toast.LENGTH_SHORT).show();
        }
        isWritting = false;
        finish();
    }
}
