package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yanxiu.gphone.student.fragment.PhotoFragment;

/**
 * Created by Administrator on 2015/10/08.
 */
public class PhotoFragmentFactory {

    private static volatile PhotoFragmentFactory instance = null;
    // private constructor suppresses
    private PhotoFragmentFactory(){

    }
    public static PhotoFragmentFactory getInstance() {
        // if already inited, no need to get lock everytime
        if (instance == null) {
            synchronized (PhotoFragmentFactory.class) {
                if (instance == null) {
                    instance = new PhotoFragmentFactory();
                }
            }
        }
        return instance;
    }

    public Fragment createPhotoFragment(String uri){
        Fragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
//        args.putSerializable("subjectExercisesItemBean", subjectExercisesItemBean);
//        LogInfo.log("geny", "createAnswerCardFragment comeFrome------" + comeFrom);
        args.putString("uri", uri);
        fragment.setArguments(args);
        return fragment;
    }



}
