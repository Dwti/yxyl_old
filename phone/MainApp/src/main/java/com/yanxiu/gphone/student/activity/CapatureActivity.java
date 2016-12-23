package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.utils.Utils;
import com.yanxiu.gphone.student.view.CameraPreview;
import com.yanxiu.gphone.student.view.picsel.utils.AlbumHelper;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;
import com.yanxiu.gphone.student.view.takephoto.RecordVideoStatueCircle;

import java.text.ParseException;

import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

/**
 * Created by sp on 16-12-19.
 */

public class CapatureActivity extends Activity implements View.OnClickListener {

    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private RecordVideoStatueCircle btn_capture;
    public static Bitmap bitmap;
    private OrientationEventListener orientationEventListener;
    private int backCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private ImageView iv_close;
    private TextView tv_photos;
    private boolean isTakingPhoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        tv_photos = (TextView) findViewById(R.id.tv_photos);
        mCameraPreview = (CameraPreview) findViewById(R.id.cameraPreview);
        btn_capture = (RecordVideoStatueCircle) findViewById(R.id.btn_capture);
        btn_capture.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        tv_photos.setOnClickListener(this);
        Log.i("lifeCircle:","onCreate");
        backCameraId = getBackCameraId();
        orientationEventListener = new OrientationEventListener(this,SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {

                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN || mCamera == null) {
                    return;  //手机平放时，检测不到有效的角度
                }
                android.hardware.Camera.CameraInfo info =
                        new android.hardware.Camera.CameraInfo();
                android.hardware.Camera.getCameraInfo(backCameraId, info);
                orientation = (orientation + 45) / 90 * 90;
                int rotation = 0;
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    rotation = (info.orientation - orientation + 360) % 360;
                } else {  // back-facing camera
                    rotation = (info.orientation + orientation) % 360;
                }
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setRotation(rotation);
                mCamera.setParameters(parameters);
                Log.i("rotation ", "rotation:" + rotation);
                Log.i("cameraOritation ", "cameraOritation:" + info.orientation);
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lifeCircle:","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (safeCameraOpen()) {
            mCameraPreview.setCamera(mCamera);
        }
        orientationEventListener.enable();
        Log.i("lifeCircle:","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationEventListener.disable();
        releaseCameraAndPreview();
        Log.i("lifeCircle:","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lifeCircle:","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lifeCircle:","onDestroy");
    }

    private boolean safeCameraOpen() {
        boolean qOpened = false;
        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(backCameraId);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"打开摄像头失败", Toast.LENGTH_SHORT).show();
            finish();
        }
        return qOpened;
    }

    private void releaseCameraAndPreview() {
        mCameraPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_capture:
                if (isTakingPhoto || mCamera ==null)
                    return;
                isTakingPhoto = true;
                mCamera.takePicture(null,null,new MyPictureCallBack());
                break;
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_photos:
                if (CommonCoreUtil.sdCardMounted()) {
                    try {
                        initImageData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(this, ImagePicSelActivity.class);
                    startActivityForResult(intent,ImagePicSelActivity.IMAGE_SELECT);
                }
                break;
            default:
                break;
        }
    }

    private void initImageData() throws ParseException {
        AlbumHelper helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        if (ShareBitmapUtils.getInstance().getDataList().size() > 0) {
            ShareBitmapUtils.getInstance().getDataList().clear();
        }
        helper.resetParmas();
        ShareBitmapUtils.getInstance().setDataList(helper.getImagesBucketList(false));
    }

    private Bitmap decodeBitmap(byte[] data, int width, int height) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, bmOptions);
        int scaleFactor = Utils.calculateInSampleSize(bmOptions, width, height);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, bmOptions);
        return bitmap;
    }


    class MyPictureCallBack implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                bitmap = decodeBitmap(data,Utils.getScreenWidth(), Utils.getScreenHeight());
                if(bitmap != null){
                    Intent intent = new Intent(CapatureActivity.this,ImageCropActivity.class);
                    startActivityForResult(intent,ImageCropActivity.REQUEST_IMAGE_CROP);
                }else {
                    Toast.makeText(CapatureActivity.this,"保存照片失败！",Toast.LENGTH_SHORT).show();
                    finish();
                }
                isTakingPhoto = false;
            }
        }
    }

    private int getBackCameraId(){
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ImageCropActivity.REQUEST_IMAGE_CROP:
                if(resultCode == RESULT_OK){
                    setResult(RESULT_OK,data);
                    finish();
                }
                break;
            case ImagePicSelActivity.IMAGE_SELECT:
                if(resultCode == RESULT_OK){
                    setResult(RESULT_OK,data);
                }
                finish();
                break;
            default:
                break;
        }

    }
}
