package com.yanxiu.gphone.student.activity;

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
import com.yanxiu.gphone.student.task.BitmapWorkerTask;
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
    private ImageView mImageView;
    private TextView tv_crop, tv_cancel;
    private View rl_crop;
    private String imagePath;
    private Bitmap mBitmap;
    public static final int REQUEST_IMAGE_CROP = 0x04;
    private boolean isWritting = false;
    private Activity mActivity;
    public static final String IMAGE_PATH = "imagePath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        mActivity = this;
        mImageView = (ImageView) findViewById(R.id.image);
        image_over_view = (ImageCropOverView) findViewById(R.id.image_over_view);
        tv_crop = (TextView) findViewById(R.id.tv_crop);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        rl_crop = findViewById(R.id.rl_crop);
        tv_crop.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        initData();
        image_over_view.setTargetView(mImageView);
    }

    private void initData(){
        imagePath = getIntent().getStringExtra(IMAGE_PATH);
        if(TextUtils.isEmpty(imagePath)){
            mBitmap = CaptureActivity.bitmap;
            mImageView.setImageBitmap(mBitmap);
        }else {
            BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(new OnTaskCompleteListener<Bitmap>() {
                @Override
                public void onComplete(Bitmap bitmap) {
                    if(bitmap == null){
                        Toast.makeText(ImageCropActivity.this,"读取照片失败", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_CANCELED);
                        finish();
                        return;
                    }
                    mImageView.setImageBitmap(bitmap);
                    mBitmap = bitmap;
                    image_over_view.postInvalidate();
                }
            });
//            bitmapWorkerTask.execute(imagePath, String.valueOf(rl_crop.getWidth()), String.valueOf(rl_crop.getHeight()));
            bitmapWorkerTask.execute(imagePath, String.valueOf(Utils.getScreenWidth()), String.valueOf(Utils.getScreenHeight()));


//            Glide.with(mActivity).load(imagePath).asBitmap().listener(new RequestListener<String, Bitmap>() {
//                @Override
//                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//                    Toast.makeText(mActivity,"读取照片失败", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                    mBitmap = resource;
//                    return false;
//                }
//            }).into(mImageView);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_crop:
                if(isWritting || mBitmap ==null){
                    return;
                }
                isWritting = true;
                Rect rect = image_over_view.getRect();

                Bitmap sourceBmp = Bitmap.createScaledBitmap(mBitmap, mImageView.getWidth(), mImageView.getHeight(), false);

                Bitmap crop_bitmap = Bitmap.createBitmap(sourceBmp, rect.left, rect.top,
                        rect.width(), rect.height());
                File outputFile =  MediaUtils.getOutputMediaFile(true);
                isWritting = true;
                WriteBitmapToFileWorkerTask workerTask = new WriteBitmapToFileWorkerTask(this);
                workerTask.execute(outputFile,crop_bitmap);
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
    public void onComplete(File file) {
        if (file != null) {
            Intent intent = new Intent();
            intent.putExtra(IMAGE_PATH,file.getAbsolutePath());
            setResult(RESULT_OK,intent);
        } else {
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "保存图片文件失败！", Toast.LENGTH_SHORT).show();
        }
        isWritting = false;
        finish();
    }
}
