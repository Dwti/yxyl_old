package com.common.core.utils.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.common.core.CommonCoreApplication;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;

import java.io.File;

/**
 * @author gao yong (gaoyong06[at]qq[dot]com)
 * inspire from: https://github.com/nostra13/Android-Universal-Image-Loader/issues/451
 * description:
 *          使用 Android-Universal-Image-Loader 实现 android Html.fromHtml (String source, Html.ImageGetter imageGetter, Html.TagHandler tagHandler)中的imageGetter方法.
 * sample:
 *          String htmlSnippetCode = "如图，已知抛物线 y=ax<sup>2</sup>+bx+c（a≠0）的顶点坐标为（4，<img src=\"http://p2.qingguo.com/G1/M00/9F/B2/rBACE1PfJ7iQLN4bAAABOwdsNv0023.png\" height=\"42\" width=\"7\">），且与y轴交于点C，与x轴交于A，B两点（点A在点B的左边），且A点坐标为（2，0）。在抛物线的对称轴 <img src=\"http://p2.qingguo.com/G1/M00/9F/B2/rBACE1PfJ7iTXZhbAAABESQziLA683.png\" height=\"21\" width=\"6\">上是否存在一点P，使AP+CP的值最小？若存在，则AP+CP的最小值为（&nbsp;&nbsp;&nbsp;&nbsp;）<img src=\"http://p1.qingguo.com/G1/M00/62/2A/rBACFFPfJ7iiO8gTAAALHRPDATw26.jpeg\" height=\"165\" width=\"193\">";
 *          TextView TVMain = (TextView) this.findViewById(R.id.TVMain);
 *          UilImageGetter imageGetter = new UilImageGetter(TVMain,this);
 *          Spanned spanned = Html.fromHtml(htmlSnippetCode, imageGetter, null);
 *          TVMain.setText(spanned);
 */
public class UilImageGetter implements Html.ImageGetter {

    private Context context;
    private TextView view;
    private int viewWillResetHeight;
//    private CustomImageLoadingListener listener;
    private CommonCoreApplication application;
    private boolean first = true;
    int height=-1;

    private int afterLine = -1;
    private int beforeLine = -1;

    public UilImageGetter(TextView view,Context context,CommonCoreApplication application) {
        this.application = application;
        this.context = context;
        this.view = view;
    }

    public void setTrueHeight(int height){
        this.height=height;
    }

    @Override
    public Drawable getDrawable(String source) {
        LogInfo.log("geny", "source -------" + source);
        URLDrawable urlDrawable = new URLDrawable(UilImageGetter.this.context);
//        urlDrawable.setBounds(0, 0, urlDrawable.getIntrinsicWidth(), urlDrawable.getIntrinsicHeight());
        //实现方法一
//        listener = new CustomImageLoadingListener(urlDrawable);
//        ImageLoader.getInstance().loadImage(source,listener);
        //实现方法二
        // get the actual source
        ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);
        asyncTask.execute(source);

        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Bitmap> {

        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable urlDrawable) {
            this.urlDrawable = urlDrawable;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String source = params[0];
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                                                        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                                                        .cacheOnDisk(true)                       // 设置下载的图片是否缓存在SD卡中
                                                        .build();

//            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(uri, ivPhotoView, options);
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(source, options);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            UilImageGetter.this.replaceImage(result,urlDrawable);
        }
    }

    private class CustomImageLoadingListener extends SimpleImageLoadingListener {

        private URLDrawable urlDrawable;

        CustomImageLoadingListener(URLDrawable urlDrawable){

            this.urlDrawable = urlDrawable;
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            super.onLoadingStarted(imageUri,view);
            File discCacheImg = DiskCacheUtils.findInCache(imageUri,null);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            //TODO:
            //do something
            super.onLoadingFailed(imageUri,view,failReason);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            super.onLoadingComplete(imageUri, view, loadedImage);
            UilImageGetter.this.replaceImage(loadedImage,this.urlDrawable);
//            File discCacheDir = UilImageGetter.this.application.discCache.getDirectory();
//            File discCacheImg = UilImageGetter.this.application.discCache.get(imageUri);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

            super.onLoadingCancelled(imageUri, view);
            //TODO:
                //do something
        }
    }
    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            UilImageGetter.this.view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            afterLine = UilImageGetter.this.view.getLineCount();
            LogInfo.log("geny", "afterLine change--" + afterLine);
            if (afterLine != beforeLine) {
                UilImageGetter.this.viewWillResetHeight += (UilImageGetter.this.view.getTextSize()+10) * (afterLine - beforeLine);
                UilImageGetter.this.view.setHeight(UilImageGetter.this.viewWillResetHeight);
//                UilImageGetter.this.view.invalidate();
                beforeLine = afterLine;
            }
            LogInfo.log("geny", "onGlobalLayout.this.viewWillResetHeight--" + UilImageGetter.this.viewWillResetHeight);
        }
    }
    int loadedImageWidth;
    int loadedImageheight;
    public void replaceImage(final Bitmap loadedImage,final URLDrawable urlDrawable) {

        //http://stackoverflow.com/questions/18359806/linearlayouts-width-and-height-are-zero
        //备注:
        //  当图片是从网络获取时:UilImageGetter.this.view.getHeight() 大于0
        //  当图片是从文件缓存目录获取时:UilImageGetter.this.view.getHeight() 等于0 [等于0就出问题了]
        //  等于0的原因是replaceImage执行时UilImageGetter.this.view还未完成布局尺寸的计算,解决办法时把replaceImage的业务放到UI Thread的message Queue里面.
        //  待能处理这个Runnable对象时,UilImageGetter.this.view 应该已经完成布局尺寸计算了.
        this.view.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (null != loadedImage) {
                    final float density = context.getResources().getDisplayMetrics().density;
                    float sizeScale;
                    LogInfo.log("density", "getDisplayMetrics-- " + density);
                    //按照240dip,对px和dp做处理
                    if(density == 3){
                        sizeScale = (float) 2.5;
                    }else if(density == 2.5){
                        sizeScale = (float) 2.0;
                    }else{
                        sizeScale = density;
                    }
//
                    LogInfo.log("geny", "replaceImage--" + UilImageGetter.this.viewWillResetHeight);
                    loadedImageWidth = Math.round(loadedImage.getWidth() * sizeScale);
                    loadedImageheight = Math.round(loadedImage.getHeight() * sizeScale);
                    if(loadedImageWidth > CommonCoreUtil.getScreenWidth() * 0.55){
                        int oLoadedImageWidth = loadedImageWidth;
                        loadedImageWidth = (int) (CommonCoreUtil.getScreenWidth() * 0.55);
                        double scale = (double)loadedImageWidth / (double)oLoadedImageWidth;
                        loadedImageheight = (int) (scale * loadedImageheight);
                    }
                    Drawable result = new BitmapDrawable(context.getResources(), loadedImage);
                    result.setBounds(0, 0, loadedImageWidth, loadedImageheight);
                    urlDrawable.setBounds(0, 0, loadedImageWidth, loadedImageheight);
                    urlDrawable.drawable  = result;

                    UilImageGetter.this.view.invalidate();
                    LogInfo.log("geny", "afterLine change--" + UilImageGetter.this.view.getTextSize());
                    LogInfo.log("geny", "loadedImageheight change--" + loadedImageheight);
                    if(0 != UilImageGetter.this.viewWillResetHeight) {
                        if(loadedImageheight > UilImageGetter.this.view.getTextSize() && loadedImageWidth > CommonCoreUtil.getScreenWidth() * 0.55){
                            UilImageGetter.this.viewWillResetHeight += loadedImageheight;
                        }else if(loadedImageheight > UilImageGetter.this.view.getTextSize()){
                            UilImageGetter.this.viewWillResetHeight += loadedImageheight - UilImageGetter.this.view.getTextSize();
                        }
                    }else {
                        if(loadedImageheight > UilImageGetter.this.view.getTextSize() && loadedImageWidth > CommonCoreUtil.getScreenWidth() * 0.55){
                            if (height!=-1){
                                UilImageGetter.this.viewWillResetHeight = height + loadedImageheight;
                            }else {
                                UilImageGetter.this.viewWillResetHeight = UilImageGetter.this.view.getHeight() + loadedImageheight;
                            }
                        }else if(loadedImageheight > UilImageGetter.this.view.getTextSize()){
                            if (height!=-1){
                                UilImageGetter.this.viewWillResetHeight = (int) (height + loadedImageheight - UilImageGetter.this.view.getTextSize());
                            }else {
                                UilImageGetter.this.viewWillResetHeight = (int) (UilImageGetter.this.view.getHeight() + loadedImageheight - UilImageGetter.this.view.getTextSize());
                            }
                        }else{
                            if (height!=-1){
                                UilImageGetter.this.viewWillResetHeight = height;
                            }else {
                                UilImageGetter.this.viewWillResetHeight = UilImageGetter.this.view.getHeight();
                            }
                        }
                    }

                    //http://stackoverflow.com/questions/7870312/android-imagegetter-images-overlapping-text
                    // For ICS
                    UilImageGetter.this.view.setHeight(UilImageGetter.this.viewWillResetHeight);
//                    LogInfo.log("geny", "change viewWillResetHeight-- " + viewWillResetHeight);
                    beforeLine = UilImageGetter.this.view.getLineCount();
//                    LogInfo.log("geny", "change beforeLine-- " + beforeLine);
                    ViewTreeObserver.OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener();
                    UilImageGetter.this.view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
                    UilImageGetter.this.view.requestLayout();
                    // Pre ICS
                    UilImageGetter.this.view.setEllipsize(null);
                    LogInfo.log("geny", "viewWillResetHeight--" + UilImageGetter.this.viewWillResetHeight);

//                    UilImageGetter.this.view.invalidate();

                }
            }
        } );
    }
}
