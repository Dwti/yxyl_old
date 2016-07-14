package com.yanxiu.gphone.hd.student.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;

/**
 * Created by Administrator on 2016/5/4.
 */
public class NoGroupAddTipsFragment extends TopBaseFragment {
    private static final String TAG=NoGroupAddTipsFragment.class.getSimpleName();
    private GroupInfoContainerFragment fg;
    public static Fragment newInstance(){
          NoGroupAddTipsFragment noGroupAddTipsFragment=new NoGroupAddTipsFragment();
           return noGroupAddTipsFragment;
    }

    @Override
    protected boolean isAttach() {
        return true;
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        titleText.setText(getResources().getString(
                R.string.group_add_tips_title
        ));
    }

    @Override
    protected void setContentContainerView() {
        super.setContentContainerView();
        contentContainer.setPadding(getResources().getDimensionPixelOffset(R.dimen.dimen_39),getResources().getDimensionPixelOffset(R.dimen.dimen_35),getResources().getDimensionPixelOffset(R.dimen.dimen_39),0);
        contentContainer.setBackgroundColor(getResources().getColor(R.color.color_fff0b2));
    }

    @Override
    protected View getContentView() {
        fg = (GroupInfoContainerFragment) getParentFragment();
        return getAttachView(R.layout.no_group_add_tips_activity);
    }

    @Override
    protected void initLoadData() {

    }

    @Override
    protected void setContentListener() {

    }
    private void finish () {
        if(fg!=null&&fg.mIFgManager!=null){
            fg.mIFgManager.popStack();
        }

    }
    @Override
    protected void destoryData() {
        LogInfo.log(TAG,"destoryData");
        finish();
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
    public void onReset() {

    }
}
