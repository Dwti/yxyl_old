package com.yanxiu.gphone.hd.student.fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;

/**
 * Created by Administrator on 2016/1/22.
 */
public class FragmentTest1 extends TopBaseFragment {

    private final static String TAG=FragmentTest1.class.getSimpleName();
    private HomePageFragment fg;
    private Button testBtn1;
    private FragmentTest2   test2;
    View view;

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        fg= (HomePageFragment) getParentFragment();
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        view=inflater.inflate(R.layout.layout_test1,null);
        testBtn1=(Button)view.findViewById(
                R.id.testBtn1
        );
        test2=new FragmentTest2();
        testBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    fg.mIFgManager.addFragment(test2, true);


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
        titleText.setText("test1");
    }

    @Override
    protected void destoryData() {
        fg.mIFgManager.popStack();
    }

    @Override
    protected IFgManager getFragmentManagerFromSubClass() {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass() {
        return 0;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onReset() {

    }
}
