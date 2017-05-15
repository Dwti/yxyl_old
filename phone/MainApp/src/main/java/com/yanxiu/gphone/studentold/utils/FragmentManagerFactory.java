package com.yanxiu.gphone.studentold.utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.yanxiu.gphone.studentold.bean.QuestionEntity;
import com.yanxiu.gphone.studentold.fragment.question.MistakeRedoFragment;

/**
 * Created by Canghaixiao.
 * Time : 2017/2/15 17:18.
 * Function :
 */

public class FragmentManagerFactory {

    public static void addMistakeRedoFragment(Activity activity, FragmentTransaction ft, QuestionEntity questionsEntity, int containerViewId) {
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        Fragment fragment = Fragment.instantiate(activity, MistakeRedoFragment.class.getName(), args);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.replace(containerViewId, fragment).commitAllowingStateLoss();
    }

}
