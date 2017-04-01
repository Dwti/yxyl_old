package com.yanxiu.gphone.student.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.imageloader.URLDrawable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yanxiu.gphone.student.HtmlParser.Interface.ImageGetterListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

public class ClassfyImageGetter2 implements ImageGetterListener {

    private Context context;
    private TextView view;
    private int loadedImageWidth;
    private int loadedImageheight;
    private int height;
    int rel_width=Util.dipToPx(32);

    private URLDrawable urlDrawable;

    private int afterLine = -1;
    private int beforeLine = -1;
    private int viewWillResetHeight;

    public ClassfyImageGetter2(TextView view, Context context) {
        this.context = context;
        this.view = view;
    }

    public void setTrueHeight(int height){
        this.height=height;
    }

    @Override
    public Drawable getDrawable(String source) {
        urlDrawable = new URLDrawable(ClassfyImageGetter2.this.context);
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
            if (bitmap != null) {
                loadedImageWidth = Math.round(bitmap.getWidth());
                loadedImageheight = Math.round(bitmap.getHeight());
            }
            int ww=ClassfyImageGetter2.this.view.getWidth()*4/5;
            if (loadedImageWidth!=rel_width){
                loadedImageheight = rel_width;
                loadedImageWidth = rel_width;
            }
            Rect bounds = new Rect(0, 0, loadedImageWidth, loadedImageheight);
            return bounds;
        }

        class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

            @Override
            public void onGlobalLayout() {
                ClassfyImageGetter2.this.view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                afterLine = ClassfyImageGetter2.this.view.getLineCount();
                LogInfo.log("geny", "afterLine change--" + afterLine);
                if (afterLine != beforeLine) {
                    ClassfyImageGetter2.this.viewWillResetHeight += (ClassfyImageGetter2.this.view.getTextSize()+10) * (afterLine - beforeLine);
                    ClassfyImageGetter2.this.view.setHeight(ClassfyImageGetter2.this.viewWillResetHeight);
//                FillBlankImageGetterTrick.this.view.invalidate();
                    beforeLine = afterLine;
                }
                LogInfo.log("geny", "onGlobalLayout.this.viewWillResetHeight--" + ClassfyImageGetter2.this.viewWillResetHeight);
            }
        }

        @Override
        protected void onPostExecute(final Drawable result) {
            if (result != null) {
                ClassfyImageGetter2.this.view.post(new Runnable() {
                    @Override
                    public void run() {
                        urlDrawable.setBounds(0, 0, loadedImageWidth, loadedImageheight);
                        urlDrawable.drawable = result;
                        //beforeLine = ClassfyImageGetter.this.view.getLineCount();
                        //ViewTreeObserver.OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener();
                        //ClassfyImageGetter.this.view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
                        int height=((YXiuAnserTextView)ClassfyImageGetter2.this.view).getClassfyHeight();
                        if (height==0){
                            height=ClassfyImageGetter2.this.view.getHeight();
                        }

                        int setheight=height + result.getIntrinsicHeight();
                        ((YXiuAnserTextView)ClassfyImageGetter2.this.view).setClassfyheight(setheight);
                        ClassfyImageGetter2.this.view.requestLayout();
                        ClassfyImageGetter2.this.view.invalidate();
                        ClassfyImageGetter2.this.view.setHeight(setheight);
                        ClassfyImageGetter2.this.view.setEllipsize(null);

                    }
                });

            }
        }
    }

}
