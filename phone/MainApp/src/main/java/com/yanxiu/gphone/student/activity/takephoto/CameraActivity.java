package com.yanxiu.gphone.student.activity.takephoto;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.PictureHelper;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.ImageBucketActivity;
import com.yanxiu.gphone.student.activity.ImagePicSelActivity;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.utils.ScreenSwitchUtils;
import com.yanxiu.gphone.student.view.picsel.utils.AlbumHelper;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;
import com.yanxiu.gphone.student.view.takephoto.CameraPreview;
import com.yanxiu.gphone.student.view.takephoto.FocusView;
import com.yanxiu.gphone.student.view.takephoto.RecordVideoStatueCircle;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;

import permissions.dispatcher.RuntimePermissions;

public class CameraActivity extends YanxiuBaseActivity implements View.OnClickListener {
    private CameraPreview cameraPreview;
    private SeekBar sb1;
    private int flashMode = -1;  //-1 auto  0 on  1off
    private RelativeLayout fl_preview;
    private FocusView mFocusView;
    private ScreenSwitchUtils mInstance;
    private  boolean portrait;
    public static final int REQUEST_CODE=0x519;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //mFilePath = getIntent().getStringExtra(MediaStore.EXTRA_OUTPUT);
        mInstance = ScreenSwitchUtils.init(this);
        if (!checkCameraHardware()) {
            Toast.makeText(this, "没有检测到相机", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mFocusView = (FocusView) findViewById(R.id.view_focus);

        fl_preview = (RelativeLayout) findViewById(R.id.fl_preview);
        RecordVideoStatueCircle btn_capture = (RecordVideoStatueCircle) findViewById(R.id.iv_capture);
        ImageView iv_flash = (ImageView) findViewById(R.id.iv_flash);
        ImageView iv_return = (ImageView) findViewById(R.id.iv_return);
        TextView iv_picture = (TextView) findViewById(R.id.iv_picture);
        iv_return.setOnClickListener(this);
        iv_flash.setOnClickListener(this);
        iv_picture.setOnClickListener(this);
        sb1 = (SeekBar) findViewById(R.id.sb1);
        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cameraPreview.setZoom(progress);
            }
        });
        btn_capture.setOnClickListener(this);

    }

    public boolean checkCameraHardware() {
        return getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
        mInstance.stop();
    }

    @Override
    protected void onResume() {
        mInstance.start(this);
        portrait = mInstance.isPortrait();
        sb1.setProgress(0);
        cameraPreview = new CameraPreview(this);
        cameraPreview.setFocusView(mFocusView);
        fl_preview.addView(cameraPreview);
        Camera.Parameters params = cameraPreview.getCameraParams();
        if (params == null) {
            finish();
            Toast.makeText(this, "打开相机失败", Toast.LENGTH_SHORT).show();
        } else {
            int maxZoom = params.getMaxZoom();
            sb1.setMax(maxZoom);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        cameraPreview.releaseCamera();
        fl_preview.removeAllViews();
        cameraPreview = null;
        super.onPause();
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {// 没有sd卡
                return;
            }

            File pictureFile = MediaUtils.getOutputMediaFile(true);
            try {
                //pictureFile.createNewFile();
                LogInfo.log("path", "111path"+pictureFile.getPath());
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                setResult(RESULT_OK);
                finish();
                //拍完预览
                /*Intent intent = new Intent(CameraActivity.this, PictureActivity.class);
                intent.putExtra("type", getIntent().getIntExtra("type", 0));
                intent.setData(Uri.fromFile(pictureFile));
                intent.putExtra("portrait",portrait);
                startActivity(intent);*/
                //ShareBitmapUtils.getInstance().addPath(ShareBitmapUtils.getInstance().getCurrentSbId(), pictureFile.getPath());
                //ActivityJumpUtils.jumpBackFromImageBucketActivity(CameraActivity.this, RESULT_OK);
                //MediaUtils.cropImage(CameraActivity.this,Uri.fromFile(pictureFile),MediaUtils.IMAGE_CROP);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };

    @Override
    public void onClick(View v) {
        portrait = mInstance.isPortrait();
        Log.i("拍照时屏幕方向状态",portrait+"");
        Camera.Parameters params = cameraPreview.getCameraParams();
//        params.setRotation(90);
        Camera camera = cameraPreview.getCameraInstance();
        switch (v.getId()) {
            case R.id.iv_capture:
                // 照相
                camera.autoFocus(null);
//                camera.takePicture(null, null, mPicture);
                camera.takePicture(null,null,mPicture);

                break;
            case R.id.iv_flash://auto  on off 切换
                if (flashMode == -1) {//auto
                    ((ImageView) v).setImageResource(R.drawable.flash_on);
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    flashMode = 0;
                } else if (flashMode == 0) {//on
                    ((ImageView) v).setImageResource(R.drawable.flash_off);
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    flashMode = 1;
                } else {
                    ((ImageView) v).setImageResource(R.drawable.flash_auto);
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                    flashMode = -1;
                }
                break;
            case R.id.iv_return:
                finish();
                break;
            case R.id.iv_picture:
                if (CommonCoreUtil.sdCardMounted()){
                    //ActivityJumpUtils.jumpToImageBucketActivityForResult(CameraActivity.this, MediaUtils.OPEN_SYSTEM_PIC_BUILD_CAMERA);
                    try {
                        initData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(ShareBitmapUtils.getInstance().getDataList()!=null&&ShareBitmapUtils.getInstance().getDataList().size()>0){
                        ActivityJumpUtils.jumpToImagePicSelActivityForResult(CameraActivity.this, 0, ImagePicSelActivity.REQUEST_CODE);
                    }
                    this.finish();
                }
                //CameraActivityPermissionsDispatcher.pickImageWithCheck(this);
                break;
        }
        camera.setParameters(params);
    }

    private void initData() throws ParseException {
        AlbumHelper helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        if(ShareBitmapUtils.getInstance().getDataList().size()>0){
            ShareBitmapUtils.getInstance().getDataList().clear();
        }
        helper.resetParmas();
        ShareBitmapUtils.getInstance().setDataList(helper.getImagesBucketList(false));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case MediaUtils.OPEN_SYSTEM_PIC_BUILD_CAMERA:
                    setResult(resultCode, data);
                    this.finish();
                    break;

            }
    }
}
