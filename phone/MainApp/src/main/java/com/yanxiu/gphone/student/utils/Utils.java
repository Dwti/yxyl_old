package com.yanxiu.gphone.student.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.common.core.utils.NetWorkTypeUtils;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.student.view.CommonDialog;
import com.yanxiu.gphone.student.view.DelDialog;

import java.io.File;
import java.util.List;

/**
 * @Class:
 * @Description:
 * @author: lling(www.cnblogs.com/liuling)
 * @Date: 2015/10/20
 */
public class Utils {

    public static DisplayMetrics getScreenWH(Context context) {
        DisplayMetrics dMetrics = new DisplayMetrics();
        dMetrics = context.getResources().getDisplayMetrics();
        return dMetrics;
    }

    /**
     * ���㽹�㼰�������
     *
     * @param focusWidth
     * @param focusHeight
     * @param areaMultiple
     * @param x
     * @param y
     * @param previewleft
     * @param previewRight
     * @param previewTop
     * @param previewBottom
     * @return Rect(left,top,right,bottom) : left��top��right��bottom������ʾ��������Ϊԭ�������
     */
    public static Rect calculateTapArea(int focusWidth, int focusHeight,
                                        float areaMultiple, float x, float y, int previewleft,
                                        int previewRight, int previewTop, int previewBottom) {
        int areaWidth = (int) (focusWidth * areaMultiple);
        int areaHeight = (int) (focusHeight * areaMultiple);
        int centerX = (previewleft + previewRight) / 2;
        int centerY = (previewTop + previewBottom) / 2;
        double unitx = ((double) previewRight - (double) previewleft) / 2000;
        double unity = ((double) previewBottom - (double) previewTop) / 2000;
        int left = clamp((int) (((x - areaWidth / 2) - centerX) / unitx),
                -1000, 1000);
        int top = clamp((int) (((y - areaHeight / 2) - centerY) / unity),
                -1000, 1000);
        int right = clamp((int) (left + areaWidth / unitx), -1000, 1000);
        int bottom = clamp((int) (top + areaHeight / unity), -1000, 1000);

        return new Rect(left, top, right, bottom);
    }

    public static int clamp(int x, int min, int max) {
        if (x > max)
            return max;
        if (x < min)
            return min;
        return x;
    }

    /**
     * �������ͷ�豸�Ƿ����
     * Check if this device has a camera
     * @param context
     * @return
     */
    public static boolean checkCameraHardware(Context context) {
        if (context != null && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * @param context
     * @return app_cache_path/dirName
     */
    public static String getDBDir(Context context) {
        String path = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "bbk" + File.separator + "cloudteacher" + File.separator + "db";
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                path = externalCacheDir.getPath();
            }
        }
        if (path == null) {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.exists()) {
                path = cacheDir.getPath();
            }
        }
        return path;
    }

    /**
     * bitmap��ת
     * @param b
     * @param degrees
     * @return
     */
    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(
                        b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();  //Android�������ٴ���ʾBitmap������Ӧ����ʾ���ͷ�
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // Android123��������γ������ڴ治���쳣�����return ԭʼ��bitmap����.
            }
        }
        return b;
    }

    public static final int getHeightInPx(Context context) {
        final int height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    public static final int getWidthInPx(Context context) {
        final int width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    public static int getWindowWidth(){
        WindowManager wm = (WindowManager) YanxiuApplication.getInstance()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public static int getWindowHeight(){
        WindowManager wm = (WindowManager) YanxiuApplication.getInstance()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    private static CommonDialog errorDialog;
    public static boolean networkJudge(final Activity activity) {
        if (!NetWorkTypeUtils.isNetAvailable()) {
            errorDialog = new CommonDialog(activity, activity.getResources().getString(R.string.question_network_error),
                    activity.getResources().getString(R.string.question_continue_finish),
                    activity.getResources().getString(R.string.question_cancel),
                    new DelDialog.DelCallBack() {
                        @Override
                        public void del() {
                            //2
                            Intent intent = new Intent();
                            activity.setResult(activity.RESULT_OK, intent);
                            activity.finish();
                        }

                        @Override
                        public void sure() {
                            //1
                        }

                        @Override
                        public void cancel() {
                            errorDialog.dismiss();
                        }
                    });
            errorDialog.show();
            return true;
        } else {
            return false;
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Camera.Size getCloselyPreSize(int reqWidth, int reqHeight,
                                                List<Camera.Size> preSizeList) {

        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (YanxiuApplication.getContext().getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
            ReqTmpWidth = reqHeight;
            ReqTmpHeight = reqWidth;
        } else {
            ReqTmpWidth = reqWidth;
            ReqTmpHeight = reqHeight;
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for(Camera.Size size : preSizeList){
            if((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight)){
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }

    public static int getScreenWidth() {
        return YanxiuApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return YanxiuApplication.getContext().getResources().getDisplayMetrics().heightPixels;
    }

}
