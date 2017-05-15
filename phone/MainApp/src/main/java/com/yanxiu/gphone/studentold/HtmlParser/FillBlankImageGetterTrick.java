package com.yanxiu.gphone.studentold.HtmlParser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yanxiu.gphone.studentold.HtmlParser.Interface.ImageGetterListener;

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
                    .cacheInMemory(false)                        // 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(false)                       // 设置下载的图片是否缓存在SD卡中
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
            if (density == 3) {
                sizeScale = (float) 2.5;
            } else if (density == 2.5) {
                sizeScale = (float) 2.0;
            } else {
                sizeScale = density;
            }
            loadedImageWidth = Math.round(bitmap.getWidth() * sizeScale);
            loadedImageheight = Math.round(bitmap.getHeight() * sizeScale);

            Rect bounds = new Rect(0, 0, loadedImageWidth, loadedImageheight);
            return bounds;
        }

        @Override
        protected void onPostExecute(final Bitmap result) {
            if (result == null) {
                if (callback != null) {
                    callback.onFinish();
                    return;
                }
            }

            if (result != null) {
                FillBlankImageGetterTrick.this.view.post(new Runnable() {
                    @Override
                    public void run() {
                        final Drawable drawable = new BitmapDrawable(result);
                        Rect bounds = getDefaultImageBounds(context, result);
//                        drawable.setBounds(0, 0, bounds.width(), bounds.height());
                        // 这里貌似有多线程问题？？？由于所有图片都用一个ImageGetter，所以factor会算错？
                        final float factor = Math.min(1.0f, (float) (view.getWidth() / (float) loadedImageWidth));
                        final int width = Math.round(loadedImageWidth * factor);
                        final int height = Math.round(loadedImageheight * factor);
//                        drawable.setBounds(0, 0, width, height);

                        drawable.setBounds(0, 0, width, height);
                        urlDrawable.setBounds(0, 0, width, height);
                        urlDrawable.drawable = drawable;

                        // 这里非常trick，但实在不知道为什么在两种手机上算出来的高度会有误差，只能暂时如此了
                        /**
                         * windowmanger获得Display,然后getsize这个方法本身就有问题不够精确，后期处理，改为getrealsize或其他方式
                         * cwq
                         * */
                        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                        Display display = wm.getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int screenWidth = size.x;
                        if (screenWidth <= 480) {
                            FillBlankImageGetterTrick.this.view.setHeight((FillBlankImageGetterTrick.this.view.getHeight() + (int) (loadedImageheight * factor)));
                        } else {
                            FillBlankImageGetterTrick.this.view.setHeight((FillBlankImageGetterTrick.this.view.getHeight() + (int) (drawable.getIntrinsicHeight())));
                        }

                        FillBlankImageGetterTrick.this.view.setHeight(FillBlankImageGetterTrick.this.view.getHeight() + drawable.getIntrinsicHeight());

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