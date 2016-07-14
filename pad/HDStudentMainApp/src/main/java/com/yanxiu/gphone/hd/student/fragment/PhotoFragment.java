package com.yanxiu.gphone.hd.student.fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.view.picsel.bean.LocalImageView;

/**
 * Created by Administrator on 2015/7/7.
 */
public class PhotoFragment extends BaseFragment implements View.OnClickListener{

    private View rootView;

    private String uri;

    private LocalImageView ivPhotoView;


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
        ivPhotoView = (LocalImageView) rootView.findViewById(R.id.iv_photo_view);
//        ivPhotoView.setScaleType(ImageView.ScaleType.CENTER);

    }


    private void initData(){
        if(!TextUtils.isEmpty(uri) && uri.startsWith("http")){

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)                       // 设置下载的图片是否缓存在SD卡中
                    .build();
//            ivPhotoView.setImageUrl(uri, YanxiuApplication.getInstance().getImageLoader());
//            ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask();
//            asyncTask.execute(uri);
//            requestBitmap(uri);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(uri, ivPhotoView, options);


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

    @Override
    public void onReset() {

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


    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Bitmap> {


        public ImageGetterAsyncTask() {
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String source = params[0];
            return com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(source);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(ivPhotoView!=null){
                ivPhotoView.setImageBitmap(result);
                ivPhotoView.requestLayout();
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rootView=null;

        uri=null;

        ivPhotoView=null;
    }
}
