package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/1/22.
 */
public abstract class TopBaseFragment extends BaseWithManagerFragment implements View.OnClickListener {
    private final static String TAG=TopBaseFragment.class.getSimpleName();
    protected LinearLayout contentContainer;
    protected RelativeLayout topRootView;
    protected RelativeLayout rightView;
    protected RelativeLayout rootView;
    private View inflateView;
    protected TextView titleText;
    protected ImageView leftView;
    protected TextView rightText;
    protected ImageView ivRight;
    protected PublicLoadLayout mPublicLayout;
    protected final int INDEX_ZERO=0;
    protected final int INDEX_FIRST=1;
    protected final int INDEX_SECOND=2;
    protected final int INDEX_THRIRD=3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogInfo.log(TAG,"onCreate");
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        inflateView=inflater.inflate(R.layout.top_view_fragment_layout,null);
        initTopView();
        setListener();
        initLoadData();
    }


    private void setListener() {
        rightView.setOnClickListener(this);
        leftView.setOnClickListener(this);
        rightText.setOnClickListener(this);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //解决当前Framgemt被下一个Fragment覆盖后还能响应事件的Bug
                return true;
            }
        });
        setContentListener();
    }

    private void initTopView() {
        rootView= (RelativeLayout) inflateView.findViewById(R.id.root_view);
        topRootView=(RelativeLayout)inflateView.findViewById(R.id.top_layout);
        rightView=(RelativeLayout)inflateView.findViewById(R.id.pub_right_layout);
        rightText=(TextView)inflateView.findViewById(R.id.pub_top_right);
        titleText=(TextView)inflateView.findViewById(R.id.pub_top_mid);
        leftView=(ImageView)inflateView.findViewById(R.id.pub_top_left);
        ivRight=(ImageView)inflateView.findViewById(R.id.public_top_iv_right);
        contentContainer=(LinearLayout)inflateView.findViewById(R.id.content_linear);
        setRootView();
        setTopView();
        setContentContainerView();
        View view=getContentView();
        if(!isAttach()){
            if(view==null){
                return;
            }
            ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(params);
            contentContainer.addView(view);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogInfo.log(TAG,"onCreateView");
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogInfo.log(TAG, "onActivityCreated");

    }

    protected abstract boolean isAttach();

    /**
     * 得到MergeView   isAttach为true调用此方法
     * @param layoutId
     * @return
     */
    protected View getAttachView(int layoutId){
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        return inflater.inflate(layoutId,contentContainer,true);
    }

    protected void setTopView() {

    }

    protected void setRootView(){
        rootView.setBackgroundColor(getResources().getColor(R.color.color_white ));
    }

    protected void setContentContainerView(){
        contentContainer.setPadding(0, 0, 0, 0);
    }
    /**
     * 初始化child Activity 布局View 除去 TopView
     * @return
     */
    protected abstract View getContentView();

    /**
     * 初始化数据加载
     */
    protected abstract void initLoadData();

    /**
     *  设置所有view的监听
     */
    protected abstract void setContentListener();

    /**
     *  销毁相关数据
     */
    protected abstract void destoryData();

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pub_top_left:
                executeFinish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            executeFinish();
            return true;
        }
        return false;
    }


    protected void executeFinish(){
        destoryData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected void finishPublicLayout(){
        if(mPublicLayout!=null){
            mPublicLayout.finish();
        }

    }
    protected void showDataError(boolean isFirstLoad){
        if(isFirstLoad){
            if(mPublicLayout!=null){
                mPublicLayout.dataError(getResources().getString(R.string.data_request_fail),false);
            }
        }else{
            Util.showToast(getResources().getString(R.string.data_request_fail));
        }
    }

    protected void showDataErrorWithFlag(boolean isFirstLoad){
        if(isFirstLoad){
            if(mPublicLayout!=null){
                mPublicLayout.dataError(getResources().getString(R.string.data_request_fail),false);
            }
        }else{
            Util.showToast(getResources().getString(R.string.data_request_fail));
        }
    }


    protected void showNetError(boolean isFristLoad){
        if(isFristLoad){
            if(mPublicLayout!=null){
                mPublicLayout.netError(false);
            }
        }else{
            Util.showToast(getResources().getString(R.string.public_loading_net_null_errtxt));
        }
    }


    protected void showNetErrorWithFlag(boolean isShowContent){
        if(isShowContent){
            if(mPublicLayout!=null){
                mPublicLayout.netError(isShowContent);
            }
        }else{
            Util.showToast(getResources().getString(R.string.public_loading_net_null_errtxt));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contentContainer=null;
        topRootView=null;
        rightView=null;
        rootView=null;
        inflateView=null;
        titleText=null;
        leftView=null;
        rightText=null;
        ivRight=null;
        mPublicLayout=null;
        LogInfo.log("lee","onDestory");
    }
}
