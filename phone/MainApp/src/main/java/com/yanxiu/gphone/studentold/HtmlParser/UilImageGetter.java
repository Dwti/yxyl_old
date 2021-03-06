package com.yanxiu.gphone.studentold.HtmlParser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yanxiu.gphone.studentold.HtmlParser.Interface.ImageGetterListener;

public class UilImageGetter implements ImageGetterListener {

    private Context context;
    private TextView view;
    private int loadedImageWidth;
    private int loadedImageheight;

    private URLDrawable urlDrawable;

    public UilImageGetter(TextView view, Context context) {
        this.context = context;
        this.view = view;
    }

    @Override
    public Drawable getDrawable(String source) {
        urlDrawable = new URLDrawable(UilImageGetter.this.context);
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
            final float density = context.getResources().getDisplayMetrics().density;
            float sizeScale;
            //按照240dip,对px和dp做处理
            if(density == 3){
                sizeScale = (float) 2.5;
            }else if(density == 2.5){
                sizeScale = (float) 2.0;
            }else{
                sizeScale = density;
            }
            loadedImageWidth = Math.round(bitmap.getWidth() * sizeScale);
            loadedImageheight = Math.round(bitmap.getHeight() * sizeScale);
            Rect bounds = new Rect(0, 0, loadedImageWidth, loadedImageheight);
            return bounds;
        }

        @Override
        protected void onPostExecute(final Drawable result) {
            if (result != null) {
                urlDrawable.setBounds(0, 0, loadedImageWidth, loadedImageheight);
                urlDrawable.drawable = result;
                UilImageGetter.this.view.post(new Runnable() {
                    @Override
                    public void run() {
                        UilImageGetter.this.view.setHeight((UilImageGetter.this.view.getHeight() + loadedImageheight));
//                        UilImageGetter.this.view.setHeight(Integer.MAX_VALUE);
                        UilImageGetter.this.view.setEllipsize(null);
                        UilImageGetter.this.view.requestLayout();
                        UilImageGetter.this.view.invalidate();
                    }
                });
            }
        }
    }
}
