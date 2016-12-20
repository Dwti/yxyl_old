package com.yanxiu.gphone.student.activity.takephoto;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Matrix;

import com.common.core.utils.BitmapUtil;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.PictureHelper;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.ImageBucketActivity;
import com.yanxiu.gphone.student.activity.ImagePicSelActivity;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.CorpBean;
import com.yanxiu.gphone.student.inter.CorpFinishListener;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.student.utils.CorpUtils;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.utils.ScreenSwitchUtils;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.StudentLoadingLayout;
import com.yanxiu.gphone.student.view.picsel.utils.AlbumHelper;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;
import com.yanxiu.gphone.student.view.takephoto.CameraPreview;
import com.yanxiu.gphone.student.view.takephoto.FocusView;
import com.yanxiu.gphone.student.view.takephoto.RecordVideoStatueCircle;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import de.greenrobot.event.EventBus;
import permissions.dispatcher.RuntimePermissions;

public class CameraActivity extends YanxiuBaseActivity implements View.OnClickListener, CorpFinishListener {
    private CameraPreview cameraPreview;
    private SeekBar sb1;
    private int flashMode = -1;  //-1 auto  0 on  1off
    private RelativeLayout fl_preview;
    private FocusView mFocusView;
    private ScreenSwitchUtils mInstance;
    private boolean portrait;
    public static final int REQUEST_CODE = 0x519;
    private StudentLoadingLayout loadingLayout;
    private android.os.Handler mHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==REQUEST_CODE){
                Uri uri=Uri.parse(MediaUtils.fileUrl);
                MediaUtils.cropImage(CameraActivity.this,uri,MediaUtils.IMAGE_CROP,MediaUtils.FROM_CAMERA);
            }else {
                //iv_CropImage.setVisibility(View.VISIBLE);
                //iv_CropImage.setImageBitmap(bitmap);
            }
        }
    };

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
        CorpUtils.getInstence().AddFinishListener(this);
        mFocusView = (FocusView) findViewById(R.id.view_focus);

        loadingLayout = (StudentLoadingLayout) findViewById(R.id.loading_layout);

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

        EventBus.getDefault().register(this);
    }

    public boolean checkCameraHardware() {
        return getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    public void onEventMainThread(CorpBean corpBean){
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode()==KeyEvent.KEYCODE_BACK){
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        loadingLayout.setViewGone();
        if (bitmap!=null) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    @Override
    protected void onResume() {
        //iv_CropImage.setVisibility(View.GONE);
        mInstance.start(this);
        portrait = mInstance.isPortrait();
        sb1.setProgress(0);
        cameraPreview = new CameraPreview(this);
        cameraPreview.setFocusView(mFocusView);
        //cameraPreview.setFocus();

        fl_preview.addView(cameraPreview);
        Camera.Parameters params = cameraPreview.getCameraParams();
        if (params == null) {
            finish();
            Toast.makeText(this, "相机权限被禁用，请在权限设置中开启", Toast.LENGTH_SHORT).show();
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

    private Bitmap bitmap;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {// 没有sd卡
                return;
            }
            try {
                //pictureFile.createNewFile();

               Thread thread = new Thread() {
                   @Override
                   public void run() {

                       File pictureFile = MediaUtils.getCameraOutputMediaFile(true);
                       LogInfo.log("path", "111path" + pictureFile.getPath());
                       BufferedOutputStream fos = null;
                       try {
                           fos = new BufferedOutputStream(new FileOutputStream(pictureFile));
                       } catch (FileNotFoundException e) {
                           e.printStackTrace();
                       }
                       final Bitmap bm = CommonCoreUtil.getImage(data);
                       Matrix matrix = new Matrix();
                       matrix.setRotate(90);
                       Bitmap saveBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                       if (saveBitmap.getByteCount() > 1024 * 1024) {
                           saveBitmap = MediaUtils.ratio(saveBitmap, bm.getWidth() / 2, bm.getHeight() / 2, 800);
                       }
                       saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                       try {
                           fos.flush();
                           fos.close();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                       mHandler.sendEmptyMessage(REQUEST_CODE);
                   }
               };
                thread.start();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };

    @Override
    public void onClick(View v) {
        portrait = mInstance.isPortrait();
        Log.i("拍照时屏幕方向状态", portrait + "");
        Camera.Parameters params = cameraPreview.getCameraParams();
        //params.setRotation(90);
        Camera camera = cameraPreview.getCameraInstance();
        //int screenWidth = screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        //int screenHeight = screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        List<Camera.Size> list= params.getSupportedPictureSizes();
        int position=list.size()/3;
        Camera.Size size=list.get(position);
        params.setPictureSize(size.width, size.height);
        camera.setParameters(params);
        //camera.setDisplayOrientation(90);
        switch (v.getId()) {
            case R.id.iv_capture:
                // 照相
                camera.autoFocus(null);
                //params.setPictureSize(cameraPreview.getWidth(), cameraPreview.getHeight());
                //camera.setParameters(params);
//                camera.takePicture(null, null, mPicture);
                camera.takePicture(null, null, mPicture);
                loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
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
                if (CommonCoreUtil.sdCardMounted()) {
                    //ActivityJumpUtils.jumpToImageBucketActivityForResult(CameraActivity.this, MediaUtils.OPEN_SYSTEM_PIC_BUILD_CAMERA);
                    try {
                        initData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    /*if (ShareBitmapUtils.getInstance().getDataList() != null && ShareBitmapUtils.getInstance().getDataList().size() > 0) {
                        ActivityJumpUtils.jumpToImagePicSelActivityForResult(CameraActivity.this, 0, ImagePicSelActivity.REQUEST_CODE);
                    }*/
                    Intent intent = new Intent(this, ImagePicSelActivity.class);
                    startActivity(intent);
                    //this.finish();
                }
                //CameraActivityPermissionsDispatcher.pickImageWithCheck(this);
                break;
        }
        camera.setParameters(params);
    }

    private void initData() throws ParseException {
        AlbumHelper helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        if (ShareBitmapUtils.getInstance().getDataList().size() > 0) {
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Camera Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onfinish() {
        //iv_CropImage.setImageBitmap(null);
        if (bitmap!=null) {
            bitmap.recycle();
            bitmap = null;
        }
        this.finish();
    }

}
