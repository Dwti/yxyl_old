package com.yanxiu.gphone.hd.student.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.utils.Util;

/**
 * Created by Administrator on 2016/2/14.
 */
public class AboutFragment extends  TopBaseFragment{
    private RelativeLayout officialweixin,officialQQ,officialPhone;
    private TextView versionTextView;
    private TextView weixinText,phoneText,qqText;

    private SetContainerFragment fg;
    private static AboutFragment aboutFragment;
    public static Fragment newInstance(){
        if(aboutFragment==null){
            aboutFragment=new AboutFragment();
        }
        return aboutFragment;
    }

    @Override
    protected boolean isAttach() {
        return true;
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        titleText.setText(R.string.about_us_txt);
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.wood_bg);
    }

    @Override
    protected View getContentView() {
        fg= (SetContainerFragment) getParentFragment();
        View mView = getAttachView(R.layout.activity_about_us_layout);
        officialweixin=(RelativeLayout) mView.findViewById(R.id.official_weixin_layout);
        officialQQ=(RelativeLayout) mView.findViewById(R.id.services_qq_layout);
        officialPhone=(RelativeLayout) mView.findViewById(R.id.services_tel_num_layout);

        weixinText=(TextView) mView.findViewById(R.id.official_weixin_content);
        qqText=(TextView) mView.findViewById(R.id.services_qq_content_txt);
        phoneText=(TextView) mView.findViewById(R.id.services_tel_num_content_txt);

        versionTextView = (TextView) mView.findViewById(R.id.yanxiu_version_desc);

        return mView;
    }

    @Override
    protected void initLoadData() {
        versionTextView.setText(String.format(getResources().getString(R.string.system__version_des), CommonCoreUtil.getClientVersionName(getActivity())));
    }

    @Override
    protected void setContentListener() {
        officialQQ.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Util.copyToClipData(getActivity(), qqText.getText().toString());
                return true;
            }
        });
        officialPhone.setOnClickListener(this);
        officialweixin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Util.copyToClipData(getActivity(), weixinText.getText().toString());
                return true;
            }
        });
    }

    @Override
    protected void destoryData() {
        finish();
    }

   private void finish(){
       if(fg!=null&&fg.mIFgManager!=null){
           fg.mIFgManager.popStack();
       }
       aboutFragment=null;
   }

    @Override
    public void onDestroy() {
        super.onDestroy();
        officialweixin=null;
        officialQQ=null;
        officialPhone=null;
        versionTextView=null;
        weixinText=null;
        phoneText=null;
        qqText=null;
        fg=null;

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.services_tel_num_layout:
             //   CommonCoreUtil.callPhone(getActivity(),phoneText.getText().toString());
                break;
        }

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
        destoryData();
    }

}
