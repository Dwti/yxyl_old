package com.yanxiu.gphone.student.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.common.core.utils.LogInfo;

import java.io.File;

/**
 *  多媒体相关工具  (相机 录音 图像 视频 音频 ) 相关处理工具
 */
public class MediaUtils {
    private static  final String TAG=MediaUtils.class.getSimpleName();
    //打开系统相册的requestCode
    public final static int OPEN_SYSTEM_PIC_BUILD=0x201;

    public final static int OPEN_SYSTEM_PIC_BUILD_KITKAT = 0x200;



    //打开自定义相册的requestCode
    public final static int OPEN_DEFINE_PIC_BUILD=0x202;
    //打开系统相机requestCode
    public final static int OPEN_SYSTEM_CAMERA = 0x203;

    private static final String RETURN_DATA="return-data";




    //临时文件上传地址
    public static final String TEMP_UPLOAD_PIC_DIR="YanxiuCameraImg";

    // 利用系统自带的相机应用:拍照
    // YanXiuConstant.SDCARD_ROOT_PATH, YanXiuConstant.SDCARD_ROOT_PATH＋"YanxiuCameraImg"
    public static  void openSystemCamera(Activity activity, String str,int open_camera_request){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(str));
        intent.putExtra(RETURN_DATA, true);
        activity.startActivityForResult(intent,open_camera_request);
    }


    public static File currentFile;
    private static File getOutputMediaFile(boolean isNewFile) {
        File mediaStorageDir = null;
        try {
            mediaStorageDir = new File(Environment
                    .getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES),TEMP_UPLOAD_PIC_DIR);
        } catch (Exception e) {
            mediaStorageDir = new File(YanXiuConstant.SDCARD_ROOT_PATH, TEMP_UPLOAD_PIC_DIR);
            e.printStackTrace();
        } finally {
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }
            if(isNewFile){
                if(currentFile!=null){
                    currentFile=null;
                }
                File mediaFile;
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + System.currentTimeMillis()+".png");
                currentFile=mediaFile;
            }
            return currentFile;
        }
    }
    public static Uri getOutputMediaFileUri(boolean isNewFile) {
        if(getOutputMediaFile(isNewFile)!=null){
            LogInfo.log(TAG, "Uri.fromFile(getOutputMediaFile(isNewFile))");
            return Uri.fromFile(getOutputMediaFile(isNewFile));
        }else{
            LogInfo.log(TAG,"getOutputMediaFile(isNewFile) is null");
            return null;
        }

    }


    /**
     * 打开系统图库获取照片
     */
    public static  void openSystemPic(Activity context) {
        if (android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.KITKAT) {
            openSystemPicAfterBuildKitcat(context,OPEN_SYSTEM_PIC_BUILD_KITKAT);
        } else {
            openSystemPicBeforeBuildKitcat(context,OPEN_SYSTEM_PIC_BUILD);
        }
    }



    /**
     * 打开系统相册
     */

    private static final String PIC_TYPE="image/*";
    public static  void openSystemPicAfterBuildKitcat(Activity context,int requestCode){
        Intent intent;
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); //4.4推荐用此方式
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(PIC_TYPE);
        context.startActivityForResult(intent, requestCode);
    }

    public static void openSystemPicBeforeBuildKitcat(Activity context,int requestCode){
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK); //4.4以下的API需要再兼容
        intent.setType(PIC_TYPE);
        context.startActivityForResult(intent, requestCode);
    }

    //扫描单个文件
    public static void scanFile(Context context,String path){
       File file= new File(path);
        if(!file.exists()){
            return;
        }
        scanSingleFile(context,file.getAbsolutePath());
        if(file.isDirectory()){
            if(file.listFiles().length>0){
                for(File childFile:file.listFiles()){
                    if(childFile.isDirectory()){
                        scanFile(context,childFile.getAbsolutePath());
                    } else {
                        scanSingleFile(context,childFile.getAbsolutePath());
                    }
                }
            }

        }
    }
    private static void scanSingleFile(Context context,String path){
        MediaScannerConnection.scanFile(context,
                new String[]{path}, null, null);
    }

    /**
     * 裁剪图片方法
     */
    public void cropImage(Activity activity,Uri uri,int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
//        if(intent.resolveActivity(getPackageManager())==null){
//            ToastMaster.showToast("该手机不支持裁剪");
//        }
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }


}
