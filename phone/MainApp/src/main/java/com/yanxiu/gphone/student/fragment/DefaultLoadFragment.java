package com.yanxiu.gphone.student.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.fragment.question.BaseQuestionFragment;

/**
 * Created by Canghaixiao.
 * Time : 2017/2/20 16:07.
 * Function :
 */

public class DefaultLoadFragment extends BaseQuestionFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_defaultload,container,false);
        return view;
    }

}
