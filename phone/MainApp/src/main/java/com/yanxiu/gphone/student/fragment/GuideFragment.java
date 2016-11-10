package com.yanxiu.gphone.student.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yanxiu.gphone.student.R;

/**
 * Created by Administrator on 2016/10/25.
 */

public class GuideFragment extends Fragment{
    private DestoryListener listener;

    public interface DestoryListener{
        void DestoryListener();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_guide_question,null);
        ImageView iv_guide_first_gesture = (ImageView)view.findViewById(R.id.iv_guide_first_gesture);
        Glide.with(GuideFragment.this)
                .load(R.drawable.first_use_question)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv_guide_first_gesture);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.DestoryListener();
            }
        });
        return view;
    }

    public void setListener(DestoryListener listener){
        this.listener=listener;
    }
}