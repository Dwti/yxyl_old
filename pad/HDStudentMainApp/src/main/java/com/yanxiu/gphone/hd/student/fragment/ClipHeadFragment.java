package com.yanxiu.gphone.hd.student.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.core.view.clipview.ClipImageLayout;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.eventbusbean.ClipHeadBean;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/1/29.
 */
public class ClipHeadFragment extends BaseFragment implements View.OnClickListener{
    private ClipImageLayout mClipImageLayout;
    private TextView cancleBtn, surebtn;
    private View view;
    private MyUserInfoContainerFragment fg;
    private static final String URI_KEY="uri_key";
    private static final String RESULT_FLAG_CODE="result_code";
    private int resultCode;
    public static Fragment newInstance(String arg,int flag){
        ClipHeadFragment fragment = new ClipHeadFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URI_KEY, arg);
        bundle.putInt(RESULT_FLAG_CODE,flag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fg= (MyUserInfoContainerFragment) getParentFragment();
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        view=inflater.inflate(R.layout.show_head_layout,null);
        String imgPath=getArguments().getString(URI_KEY);
        resultCode=getArguments().getInt(RESULT_FLAG_CODE);
        if (TextUtils.isEmpty(imgPath)) {
            Util.showToast(R.string.select_location_data_error);
            fg.mIFgManager.popStack();
        }
        cancleBtn = (TextView) view.findViewById(R.id.cancle);
        cancleBtn.setOnClickListener(this);
        surebtn = (TextView) view.findViewById(R.id.sure);
        surebtn.setOnClickListener(this);
        mClipImageLayout = (ClipImageLayout) view.findViewById(R.id.id_clipImageLayout);
        mClipImageLayout.setImagePath(imgPath);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public String getClipBitmapPath() {
        Bitmap bitmap = mClipImageLayout.clip();
        if(bitmap!=null){
            return saveFile(bitmap, YanXiuConstant.HEAD_IMAGE_FILENAME);
        }else{
            return "";
        }

    }
    /**
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public String saveFile(Bitmap bm, String fileName){
        File dirFile = new File(YanXiuConstant.HEAD_IMAGE_SAVE_DIR);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = null;
        BufferedOutputStream bos = null;
        try{
            myCaptureFile = new File(YanXiuConstant.HEAD_IMAGE_SAVE_DIR + fileName);
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            return myCaptureFile.getAbsolutePath().toString();
        } catch (Exception e){
            if(bos != null ){
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
        return null;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancle:
                finish();
                break;
            case R.id.sure:
                ClipHeadBean clipHeadBean=new ClipHeadBean();
                clipHeadBean.setClipPicPath(getClipBitmapPath());
                EventBus.getDefault().post(clipHeadBean);
                finish();
                break;
        }
    }

    private void finish(){
        if(fg!=null&&fg.mIFgManager!=null){
            fg.mIFgManager.popStack();
        }

    }

    @Override
    public void onReset() {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mClipImageLayout=null;
        cancleBtn =null;
        surebtn=null;
        view=null;
        fg=null;
    }
}
