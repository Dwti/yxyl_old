package com.yanxiu.gphone.studentold.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.TextView;

import com.common.core.utils.imageloader.URLDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yanxiu.gphone.studentold.HtmlParser.Interface.ImageGetterListener;

public class ConnectImageGetter implements ImageGetterListener {

    private Context context;
    private TextView view;
    private int loadedImageWidth;
    private int loadedImageheight;
    private int px=Util.dipToPx(70);
    private URLDrawable urlDrawable;

    public ConnectImageGetter(TextView view, Context context) {
        this.context = context;
        this.view = view;
    }

    @Override
    public Drawable getDrawable(String source) {
        urlDrawable = new URLDrawable(ConnectImageGetter.this.context);
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
            Drawable drawable=null;
            if (bitmap!=null){
                drawable=new BitmapDrawable(bitmap);
                Rect bounds = getDefaultImageBounds(context,bitmap);
                drawable.setBounds(0, 0, bounds.width(), bounds.height());
            }
            return drawable;
        }

        //预定图片宽高比例为 4:3
        @SuppressWarnings("deprecation")
        public Rect getDefaultImageBounds(Context context, Bitmap bitmap) {
            loadedImageWidth = Math.round(bitmap.getWidth());
            loadedImageheight = Math.round(bitmap.getHeight());
            if (loadedImageWidth!=px) {
                loadedImageheight = loadedImageheight * px / loadedImageWidth;
                loadedImageWidth = px;
            }
            Rect bounds = new Rect(0, 0, loadedImageWidth, loadedImageheight);
            return bounds;
        }

        @Override
        protected void onPostExecute(final Drawable result) {
            if (result != null) {
                ConnectImageGetter.this.view.post(new Runnable() {
                    @Override
                    public void run() {
                        urlDrawable.setBounds(0, 0, loadedImageWidth, loadedImageheight);
                        urlDrawable.drawable = result;
                        ConnectImageGetter.this.view.setHeight((ConnectImageGetter.this.view.getHeight() + loadedImageheight));
                        ConnectImageGetter.this.view.setEllipsize(null);
                        ConnectImageGetter.this.view.requestLayout();
                        ConnectImageGetter.this.view.invalidate();
                    }
                });
            }
        }
    }

}
