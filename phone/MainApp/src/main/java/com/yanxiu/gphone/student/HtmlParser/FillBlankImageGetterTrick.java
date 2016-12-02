package com.yanxiu.gphone.student.HtmlParser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yanxiu.gphone.student.HtmlParser.Interface.ImageGetterListener;

public class FillBlankImageGetterTrick implements ImageGetterListener {
    public interface Callback {
        public void onFinish();
    }
    private Callback callback;
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private Context context;
    private TextView view;
    private int loadedImageWidth;
    private int loadedImageheight;

    private URLDrawable urlDrawable;

    public FillBlankImageGetterTrick(TextView view, Context context) {
        this.context = context;
        this.view = view;
    }

    @Override
    public Drawable getDrawable(String source) {
        urlDrawable = new URLDrawable(FillBlankImageGetterTrick.this.context);
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
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(source, options);



            return bitmap;
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
        protected void onPostExecute(final Bitmap result) {
            if (result != null) {
                if (result == null) {
                    if (callback != null)
                    {
                        callback.onFinish();
                        return;
                    }
                }

                final Drawable drawable=new BitmapDrawable(result);
                Rect bounds = getDefaultImageBounds(context,result);
                drawable.setBounds(0, 0, bounds.width(), bounds.height());
                // 这里貌似有多线程问题？？？由于所有图片都用一个ImageGetter，所以factor会算错？
                final float factor = Math.min(1.0f, (float)(view.getWidth() / (float)loadedImageWidth));
                drawable.setBounds(0, 0, (int)(loadedImageWidth * factor), (int)(loadedImageheight * factor));
                urlDrawable.setBounds(0, 0, (int)(loadedImageWidth * factor), (int)(loadedImageheight * factor));
                urlDrawable.drawable = drawable;

                FillBlankImageGetterTrick.this.view.post(new Runnable() {
                    @Override
                    public void run() {
                        FillBlankImageGetterTrick.this.view.setHeight((FillBlankImageGetterTrick.this.view.getHeight() + (int)(loadedImageheight * factor)));
                        FillBlankImageGetterTrick.this.view.setEllipsize(null);
                        FillBlankImageGetterTrick.this.view.requestLayout();
                        FillBlankImageGetterTrick.this.view.invalidate();
                        if (callback != null) {
                            callback.onFinish();
                        }
                    }
                });


            }
        }
    }
}