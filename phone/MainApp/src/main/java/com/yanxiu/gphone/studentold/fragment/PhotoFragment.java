package com.yanxiu.gphone.studentold.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.view.StudentLoadingLayout;
import com.yanxiu.gphone.studentold.view.ZoomImageView;

/**
 * Created by Administrator on 2015/7/7.
 */
public class PhotoFragment extends Fragment implements View.OnClickListener{

    private View rootView;

    private String uri;

    private ZoomImageView ivPhotoView;

    private DisplayImageOptions options;                                 // 创建配置过得DisplayImageOption对象
    private StudentLoadingLayout loadingLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        this.uri = (arguments == null? null: arguments.getString("uri"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.item_photo_view, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivPhotoView = (ZoomImageView) rootView.findViewById(R.id.iv_photo_view);
        loadingLayout = (StudentLoadingLayout) rootView.findViewById(R.id.loading_layout);
        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
//        ivPhotoView.setScaleType(ImageView.ScaleType.CENTER);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loadingLayout.setViewGone();
    }

    private void initData(){
        if(!TextUtils.isEmpty(uri) && uri.startsWith("http")) {

            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)                       // 设置下载的图片是否缓存在SD卡中
                    .build();                                   // 创建配置过得DisplayImageOption对象
//            ivPhotoView.setImageUrl(uri, YanxiuApplication.getInstance().getImageLoader());
//            ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask();
//            asyncTask.execute(uri);
//            requestBitmap(uri);
//            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(uri, ivPhotoView, options);
            Glide.with(this).load(uri).override(400, 667).into(new GlideDrawableImageViewTarget(ivPhotoView) {
                @Override
                public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                    super.onResourceReady(drawable, anim);
                    //在这里添加一些图片加载完成的操作
                    loadingLayout.setViewGone();
                }
            });
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }


    @Override
    public void onClick(View v) {

    }

//    public void requestBitmap(String uri){
//        ImageRequest imageRequest = new ImageRequest(
//                                            uri,
//                                            new ResponseListener(),
//                                            0, // 图片的宽度，如果是0，就不会进行压缩，否则会根据数值进行压缩
//                                            0, // 图片的高度，如果是0，就不进行压缩，否则会压缩
//                                            Bitmap.Config.ARGB_8888, // 图片的颜色属性
//                                            new ResponseErrorListener());
//        Volley.newRequestQueue(this.getActivity()).add(imageRequest);
//    }
//
//    private class ResponseListener implements Response.Listener<Bitmap> {
//        @Override
//        public void onResponse(Bitmap response) {
//            //  Log.d("TAG", "-------------\n" + response.toString());
//            ivPhotoView.setImageBitmap(response);
//            ivPhotoView.requestLayout();
//        }
//    }
//
//    private class ResponseErrorListener implements Response.ErrorListener {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//        }
//    }


//    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Bitmap> {
//
//
//        public ImageGetterAsyncTask() {
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//
//            String source = params[0];
//            Bitmap bitmap = com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(source);
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            ivPhotoView.setImageBitmap(result);
//            ivPhotoView.requestLayout();
//        }
//    }

}
