package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
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
import com.yanxiu.gphone.student.commoninterface.OnTaskCompleteListener;
import com.yanxiu.gphone.student.task.WriteByteToFileWorkerTask;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.utils.Utils;
import com.yanxiu.gphone.student.view.CameraPreview;
import com.yanxiu.gphone.student.view.picsel.utils.AlbumHelper;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;
import com.yanxiu.gphone.student.view.takephoto.RecordVideoStatueCircle;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
            Toast.makeText(this,"打开摄像头失败,请检查权限", Toast.LENGTH_SHORT).show();
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
                    //正常不用把Bitmap存一次文件，存文件是为了读取Exif信息，获取照片的旋转角度（针对三星系列手机，非三星系列手机，得到的bitmap就是旋转过的）
                    File temFile = MediaUtils.getOutputMediaFile(true);
                    WriteByteToFileWorkerTask writeFileTask = new WriteByteToFileWorkerTask(new FileWriteCompleteListener());
                    writeFileTask.execute(temFile,data);
                }else {
                    Toast.makeText(CapatureActivity.this,"拍照失败！",Toast.LENGTH_SHORT).show();
                    isTakingPhoto = false;
                    finish();
                }
            }
        }
    }

    class FileWriteCompleteListener implements OnTaskCompleteListener<File> {
        @Override
        public void onComplete(File file) {
            if(file == null){
                Toast.makeText(CapatureActivity.this,"保存照片失败!",Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                file.delete();
                isTakingPhoto = false;
                finish();
                return;
            }
            int orientation = ExifInterface.ORIENTATION_NORMAL;
            ExifInterface exifInterface ;
            try {
                exifInterface = new ExifInterface(file.getAbsolutePath());
                orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
                switch (orientation){
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        orientation = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        orientation = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        orientation = 270;
                        break;
                }
                Log.i("jpegorientation",orientation+"");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //一般的手机，上面在orientationEventListener中，根据屏幕旋转的角度直接设置rotation就可以直接旋转图片 ，但是对于三星系列的手机，setRotation没有效果，但是这个rotation会
            //写入到exif信息中。一般手机，不管在相机中调没调用params.setRotaion()，exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL)
            //都会返回ExifInterface.ORIENTATION_UNDEFINED；三星的手机如果调用了会返回实际设置的rotation（横屏拍照也会返回ExifInterface.ORIENTATION_NORMAL），没调用的话，会返回ExifInterface.ORIENTATION_NORMAL（非设置的默认值）
            if(orientation != ExifInterface.ORIENTATION_UNDEFINED && orientation != ExifInterface.ORIENTATION_NORMAL){
                Matrix matrix = new Matrix();
                matrix.setRotate(orientation);
                Bitmap bmpTemp = Bitmap.createBitmap(bitmap,0,0, bitmap.getWidth(), bitmap.getHeight(),matrix,true);
                bitmap = bmpTemp;
            }
            file.delete();
            Intent intent = new Intent(CapatureActivity.this,ImageCropActivity.class);
            startActivityForResult(intent,ImageCropActivity.REQUEST_IMAGE_CROP);
            isTakingPhoto = false;
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
