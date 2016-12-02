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
import com.yanxiu.gphone.student.view.picsel.utils.AlbumHelper;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;
import com.yanxiu.gphone.student.view.takephoto.CameraPreview;
import com.yanxiu.gphone.student.view.takephoto.FocusView;
import com.yanxiu.gphone.student.view.takephoto.RecordVideoStatueCircle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode()==KeyEvent.KEYCODE_BACK){
//            Intent intent=new Intent();
//            intent.putExtra("asd","asd");
//            setResult(100001,intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        mInstance.stop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
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
                LogInfo.log("path", "111path" + pictureFile.getPath());
                FileOutputStream fos = new FileOutputStream(pictureFile);

                final Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
                        data.length);
                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                /**
                 * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
                 */
                /*int degree = readPictureDegree(pictureFile.getAbsolutePath());

                BitmapFactory.Options opts=new BitmapFactory.Options();//获取缩略图显示到屏幕上
                opts.inSampleSize=2;
                Bitmap cbitmap=BitmapFactory.decodeFile(pictureFile.getAbsolutePath(),opts);*/

                /**
                 * 把图片旋转为正的方向
                 */
                /*Bitmap newbitmap = rotaingImageView(degree, cbitmap);
                newbitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);*/
                //fos.write(data);
                fos.flush();
                fos.close();

                Uri uri=MediaUtils.getOutputMediaFileUri(false);
                MediaUtils.cropImage(CameraActivity.this,uri,MediaUtils.IMAGE_CROP,MediaUtils.FROM_CAMERA);
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

    /*private void handlerCameraBit(Activity activity,String id ) {
        if(ShareBitmapUtils.getInstance().getDrrMaps().get(id)!=null){
            Uri uri=MediaUtils.getOutputMediaFileUri(false);
            String path = null;
            if(uri!=null){
                path= PictureHelper.getPath(mContext,
                        uri);
                LogInfo.log(TAG, "111path"+path);
            }
            if(path==null){
//                YanXiuConstant.index_position=0;
//                EventBus.getDefault().unregister(fragment);
                return;
            }

            Bitmap bitmap = null;
            try {
                bitmap = BitmapUtil.revitionImageSize(path);
                BitmapUtil.reviewPicRotate(bitmap, path, true);
                if(bitmap != null && !bitmap.isRecycled()){
                    bitmap.recycle();
                }
                //在此处进行裁剪
                YanXiuConstant.index_position=position;
                MediaUtils.cropImage(activity,uri,MediaUtils.IMAGE_CROP,MediaUtils.FROM_CAMERA);
//                ShareBitmapUtils.getInstance().addPath(id, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
//            YanXiuConstant.index_position=0;
        }
    }*/

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

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
        Camera.Size size=list.get(1);
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
        this.finish();
    }
}
