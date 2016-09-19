package com.yanxiu.gphone.student.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import com.common.core.utils.imageloader.URLDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yanxiu.gphone.student.HtmlParser.Interface.ImageGetterListener;

public class ClassfyImageGetter implements ImageGetterListener {

    private Context context;
    private TextView view;
    private int loadedImageWidth;
    private int loadedImageheight;
    private int height;

    private URLDrawable urlDrawable;

    public ClassfyImageGetter(TextView view, Context context) {
        this.context = context;
        this.view = view;
    }

    public void setTrueHeight(int height){
        this.height=height;
    }

    @Override
    public Drawable getDrawable(String source) {
        urlDrawable = new URLDrawable(ClassfyImageGetter.this.context);
        ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);
        asyncTask.execute(source);
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {

        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable urlDrawable) {
            this.urlDrawable = urlDrawable;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)                       // 设置下载的图片是否缓存在SD卡中
                    .build();
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(source, options);
            Drawable drawable=new BitmapDrawable(bitmap);
            Rect bounds = getDefaultImageBounds(context,bitmap);
            drawable.setBounds(0, 0, bounds.width(), bounds.height());
            return drawable;
        }

        //预定图片宽高比例为 4:3
        @SuppressWarnings("deprecation")
        public Rect getDefaultImageBounds(Context context, Bitmap bitmap) {
            loadedImageWidth = Math.round(bitmap.getWidth());
            loadedImageheight = Math.round(bitmap.getHeight());

            loadedImageheight=loadedImageheight*100/loadedImageWidth;
            loadedImageWidth=100;
            Rect bounds = new Rect(0, 0, loadedImageWidth, loadedImageheight);
            return bounds;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                urlDrawable.setBounds(0, 0, loadedImageWidth, loadedImageheight);
                urlDrawable.drawable = result;
                ClassfyImageGetter.this.view.requestLayout();
                ClassfyImageGetter.this.view.invalidate();
                ClassfyImageGetter.this.view.setHeight((ClassfyImageGetter.this.view.getHeight() + result.getIntrinsicHeight()));
                ClassfyImageGetter.this.view.setEllipsize(null);

            }
        }
    }

}
