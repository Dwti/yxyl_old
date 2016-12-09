package com.yanxiu.gphone.student.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.activity.takephoto.CameraActivity;
import com.yanxiu.gphone.student.activity.takephoto.CorpActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *  多媒体相关工具  (相机 录音 图像 视频 音频 ) 相关处理工具
 */
public class MediaUtils {
    private static  final String TAG=MediaUtils.class.getSimpleName();
    //打开系统相册的requestCode
    public final static int OPEN_SYSTEM_PIC_BUILD=0x201;

    public final static int OPEN_SYSTEM_PIC_BUILD_KITKAT = 0x200;

    public final static int OPEN_SYSTEM_PIC_BUILD_CAMERA=0x209;



    //打开自定义相册的requestCode
    public final static int OPEN_DEFINE_PIC_BUILD=0x202;
    //打开系统相机requestCode
    public final static int OPEN_SYSTEM_CAMERA = 0x203;
    //打开系统裁剪的requestCode
    public final static int IMAGE_CROP = 0x204;

    private static final String RETURN_DATA="return-data";

    public static Uri currentCroppedImageUri;

    public static boolean IsCallBack=false;

    public static final int FROM_CAMERA = 0;
    public static final int FROM_PICTURE = 1;

    public static String getPic_select_string() {
        return pic_select_string;
    }

    public static void setPic_select_string(String pic_select_path) {
        pic_select_string = pic_select_path;
    }

    private static String pic_select_string = "";



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

    public static  void openLocalCamera(Activity activity, String str,int open_camera_request){

        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(str));
        intent.putExtra(RETURN_DATA, true);
        activity.startActivityForResult(intent,open_camera_request);
    }


    public static File currentFile;
    public static String saveCroppedImage(Bitmap bmp){
        if(bmp == null)
            return null;
        File croppedImageFile;
        File mediaStorageDir = null;
        String filePath;
        FileOutputStream fos = null;
        try{
            mediaStorageDir = new File(Environment
                    .getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES),TEMP_UPLOAD_PIC_DIR);
        }catch (Exception e){
            mediaStorageDir = new File(YanXiuConstant.SDCARD_ROOT_PATH, TEMP_UPLOAD_PIC_DIR);
            e.printStackTrace();
        }
        try {
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }
            filePath = mediaStorageDir.getPath() + File.separator
                    + System.currentTimeMillis()+"_cropped.png";
            croppedImageFile = new File(filePath);
            croppedImageFile.createNewFile();
            fos = new FileOutputStream(croppedImageFile);
            bmp.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.close();
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return filePath;
    }
    public static Uri createCroppedImageUri(){
        File croppedImageFile;
        File mediaStorageDir = null;
        String filePath;
        FileOutputStream fos = null;
        try{
            mediaStorageDir = new File(Environment
                    .getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES),TEMP_UPLOAD_PIC_DIR);
        }catch (Exception e){
            mediaStorageDir = new File(YanXiuConstant.SDCARD_ROOT_PATH, TEMP_UPLOAD_PIC_DIR);
            e.printStackTrace();
        }
        try {
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }
            filePath = mediaStorageDir.getPath() + File.separator
                    + System.currentTimeMillis()+"_cropped.png";
            croppedImageFile = new File(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return Uri.fromFile(croppedImageFile);
    }
    public static File getCameraOutputMediaFile(boolean isNewFile) {
        File mediaStorageDir = null;
        try {
            mediaStorageDir = new File(Environment
                    .getExternalStorageDirectory().getAbsolutePath()+"/"+TEMP_UPLOAD_PIC_DIR);
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

    public static File getOutputMediaFile(boolean isNewFile) {
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
    public static void cropImage(Activity activity, Uri uri, int requestCode, int from) {
         currentCroppedImageUri= createCroppedImageUri();
        //Intent intent = new Intent("com.android.camera.action.CROP");
        Intent intent = new Intent(activity, CorpActivity.class);
//        if(intent.resolveActivity(getPackageManager())==null){
//            ToastMaster.showToast("该手机不支持裁剪");
//        }
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 600);
//        intent.putExtra("outputY", 600);
        intent.putExtra("return-data", false);
        intent.putExtra("from", from);
        Bundle bundle=new Bundle();
        bundle.putString("uri",uri.toString());
        intent.putExtra("bundle",bundle);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,currentCroppedImageUri.toString());
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Compress image by size, this will modify image width/height.
     * Used to get thumbnail
     *
     * @param image
     * @param pixelW target pixel of width
     * @param pixelH target pixel of height
     * @return
     */
    public static Bitmap ratio(Bitmap image, float pixelW, float pixelH, int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if( os.toByteArray().length / 1024>quality) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩
        //   return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }


}
