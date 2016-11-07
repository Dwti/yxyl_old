package com.yanxiu.gphone.student.activity.takephoto;

import android.app.Fragment;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.fragment.CorpFragment;
import com.yanxiu.gphone.student.fragment.GuideCorpFragment;
import com.yanxiu.gphone.student.preference.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JS-00 on 2016/11/4.
 */

public class CorpActivity extends YanxiuBaseActivity {
    private static final String TAG = CorpActivity.class.getSimpleName();
    private Uri mCorpUri;
    /** Standard activity result: operation succeeded. */
    public static final int RESULT_OK           = -1;
    private FrameLayout gif_framelayout;
    private GuideCorpFragment fragment;

    // Lifecycle Method ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corp);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.corp_container, CorpFragment.getInstance()).commit();
        }
        gif_framelayout = (FrameLayout)findViewById(R.id.gif_corp_framelayout);

//        if (PreferencesManager.getInstance().getFirstMultiQuestion()) {
        final FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        fragment = new GuideCorpFragment();
            fragment.setListener(new GuideCorpFragment.DestoryListener() {
                @Override
                public void DestoryListener() {
                    gif_framelayout.setVisibility(View.GONE);
                    ft.remove(fragment);
                    fragment = null;
                }
            });
            ft.replace(R.id.gif_corp_framelayout, fragment);
            ft.commit();
            gif_framelayout.setVisibility(View.VISIBLE);
            PreferencesManager.getInstance().setFirstMultiQuestion();
//        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void startResultActivity(Uri uri) {
        if (isFinishing()) return;
        // Start ResultActivity
        this.finish();
        //startActivity(ResultActivity.createIntent(this, uri));
    }

}
