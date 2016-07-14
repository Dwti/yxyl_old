package com.yanxiu.gphone.hd.student.fragment;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;

/**
 * Created by Administrator on 2016/1/22.
 */
public class FragmentTest2 extends TopBaseFragment {
    private final String TAG=FragmentTest2.class.getSimpleName();
    private HomePageFragment fg;

    @Override
    protected IFgManager getFragmentManagerFromSubClass() {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass() {
        return 0;
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        fg= (HomePageFragment) getParentFragment();
        View view=View.inflate(getActivity(),R.layout.layout_test2, null);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return view;
    }

    @Override
    protected void initLoadData() {

    }

    @Override
    protected void setContentListener() {

    }

    @Override
    protected void setTopView() {
        super.setTopView();
        titleText.setText("test2");
    }

    @Override
    protected void destoryData() {
        fg.mIFgManager.popStack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogInfo.log(TAG,"onDestroy");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogInfo.log(TAG, "onDetach");
    }

    @Override
    public void onReset() {

    }
}
