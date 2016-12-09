package com.common.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 */
public class BitmapUtil {
	private final static  String TAG=BitmapUtil.class.getSimpleName();
	//服务器的上传是2m 但媒资库那边的限制1m  介于ios的图片在 600k-800k之间 Android的限制于1M
	private static long UP_LOAD_MAX_SIZE = 1024 * 1024 * 1;
	
	/**
	 * 获取图片文件的信息，是否旋转了90度，如果是则反转
	 * @param bitmap 需要旋转的图片
	 * @param path   图片的路径
	 */
	public static Bitmap reviewPicRotate(Bitmap bitmap,String path, boolean isSaveFile){
		int degree = getPicRotate(path);
		LogInfo.log("haitian","reviewPicRotate degree="+degree);
		if(degree!=0){
			LogInfo.log("lee","reviewPicRotate");
			Matrix m = new Matrix();
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			m.setRotate(degree, width, height); // 旋转angle度
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);// 从新生成图片
			if(isSaveFile){
				saveFile(bitmap, path);
			}
		}
		return bitmap;
	}

	public  static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		Bitmap bitmap;
		int i=0;
		while (true) {
			int widthParams=options.outWidth >> i;
			int heightParams=options.outHeight >> i;
			if ((widthParams <= 1000)
					&& (heightParams <= 1000)) {
				LogInfo.log(TAG,"options.outWidth >> i: "+widthParams+"options.outHeight >> i : "+heightParams);
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				LogInfo.log(TAG," options.inSampleSize: "+ options.inSampleSize);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}


	public  static Bitmap revitionImageSize(Context context,int id) throws IOException {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), id, options);
		Bitmap bitmap;
		int i=0;
		while (true) {
			int widthParams=options.outWidth >> i;
			int heightParams=options.outHeight >> i;
			if ((widthParams <= 1000)
					&& (heightParams <= 1000)) {
				LogInfo.log(TAG,"options.outWidth >> i: "+widthParams+"options.outHeight >> i : "+heightParams);
				options.inSampleSize = (int) Math.pow(2.0D, i);
				LogInfo.log(TAG," options.inSampleSize: "+ options.inSampleSize);
				options.inJustDecodeBounds = false;
				bitmap =BitmapFactory.decodeResource(context.getResources(), id, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}




	public static void releaseImageViewResouce(ImageView imageView) {
		if (imageView == null) return;
		Drawable drawable = imageView.getDrawable();
		if (drawable != null && drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			Bitmap bitmap = bitmapDrawable.getBitmap();
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
	}

	public static void releaseViewResouce(View view){
		if(view==null){
			return;
		}
		Bitmap bt=view.getDrawingCache();
		if(bt!=null&&!bt.isRecycled()){
			bt.isRecycled();
		}
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inSampleSize = 2;
		//获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

		/**
         * 保存文件
         * @param bm
         * @throws IOException
         */
	public static void saveFile(final Bitmap bm, final String filePath){
		new Thread(new Runnable() {
			@Override
			public void run() {
				File myCaptureFile = null;
				BufferedOutputStream bos = null;
				try{
					myCaptureFile = new File(filePath);
					if(myCaptureFile.exists()){
						myCaptureFile.delete();
					}
					myCaptureFile.createNewFile();
					bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
					bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
					bos.flush();
					bos.close();
				} catch (Exception e){
					if(bos != null ){
						try {
							bos.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					bos = null;
				}
			}
		}).start();

	}

	public static void saveFileMain(final Bitmap bm, final String filePath){
		File myCaptureFile = null;
		BufferedOutputStream bos = null;
		try{
			myCaptureFile = new File(filePath);
			if(myCaptureFile.exists()){
				myCaptureFile.delete();
			}
			myCaptureFile.createNewFile();
			bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		} catch (Exception e){
			if(bos != null ){
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			bos = null;
		}
	}
	/**
	 * 读取图片文件旋转的角度
	 * @param path 图片绝对路径
	 * @return 图片旋转的角度
	 */
	public static int getPicRotate(String path) {
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

	//压缩图片
	public static ByteArrayInputStream compressImage(String path) throws Exception{
//        Bitmap image=BitmapFactory.decodeFile(path);
//        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
//        bfOptions.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一
//        BitmapFactory.decodeFile(path, bfOptions);
		Bitmap image = BitmapUtil.revitionImageSize(path);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 100;
		LogInfo.log(TAG,"图片大小: "+baos.toByteArray().length+"压缩范围： "+ UP_LOAD_MAX_SIZE);
		while ( baos.toByteArray().length >(UP_LOAD_MAX_SIZE)) {
			baos.reset();
			options -= 10;
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			LogInfo.log(TAG,"执行压缩" + baos.toByteArray().length);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		return isBm;
	}


}
