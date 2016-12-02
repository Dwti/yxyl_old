package com.yanxiu.gphone.student.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import com.yanxiu.gphone.student.utils.Utils;

/**
 * Created by Administrator on 2016/10/25.
 */

public class GuideCorpFragment extends Fragment{
    private DestoryListener listener;
    private ImageView iv;
    private View view;

    public interface DestoryListener{
        void DestoryListener();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.layout_guide_question,null);
        iv = (ImageView)view.findViewById(R.id.iv_guide_first_gesture);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        ObjectAnimator y = ObjectAnimator.ofFloat(iv, "y", (Utils.getWindowHeight())*3/4, (Utils.getWindowHeight())*1/3);
        ObjectAnimator x = ObjectAnimator.ofFloat(iv, "x", Utils.getWindowWidth(), (Utils.getWindowWidth())/2);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(x, y);
        animatorSet.setDuration(2000);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener!=null){
                    listener.DestoryListener();
                }
                iv.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.DestoryListener();
            }
        });
    }

    public void setListener(DestoryListener listener){
        this.listener=listener;
    }
}
