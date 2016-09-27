package com.yanxiu.gphone.student.exampoint;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.student.R;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2015/11/16.
 */
public class PullDownTitleView extends RelativeLayout implements View.OnClickListener,Observer {
    private TextView textView;
    private  RelativeLayout titleLayout;
    private Context mContext;
    private CommunicateInter communicateInter;
    private  static final String FA="fonts/metor_bold.OTF";
    private  static final String TAG=PullDownTitleView.class.getSimpleName();
    public PullDownTitleView(Context context) {
        super(context);
        initView(context);
    }

    public PullDownTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullDownTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PullDownTitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    @Override
    public void update(Observable observable, Object o) {
        if(o==null){
            return;
        }
        View v= (View) o;
        LogInfo.log(TAG, "update");
        if(v.getVisibility()==View.GONE){
            setClampDefaultSt();
        }
    }

    public interface CommunicateInter {
        void onClick(View view);
        View getView();
    }

    public void setCommunicateInter(CommunicateInter communicateInter){
        this.communicateInter=communicateInter;
    }


    private void initView(Context context){
        mContext=context;
        LayoutInflater inflater=LayoutInflater.from(context);
        inflater.inflate(R.layout.pull_down_title_layout,this);
        titleLayout= (RelativeLayout) findViewById(R.id.titleLayout);
        textView=(TextView)findViewById(R.id.pubdown_top_mid);
        textView.setVisibility(View.VISIBLE);
        Typeface mFace= Typeface.createFromAsset(context.getAssets(),FA);
        textView.setTypeface(mFace);
        titleLayout.setOnClickListener(this);
    }

    public void setTitle(String text){
        if(StringUtils.isEmpty(text)){
            return;
        }
        textView.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleLayout:
                if(communicateInter!=null){
                    View view=communicateInter.getView();
                    if(view.getVisibility()==View.VISIBLE){
                        setClampDefaultSt();
                        view.setVisibility(View.GONE);
                    }else {
                        setClampActiveSt();
                        view.setVisibility(View.VISIBLE);
                    }
                    communicateInter.onClick(v);
                }
                break;
        }
    }

    private  void setClampActiveSt(){
        textView.setTextColor(getResources().getColor(R.color.color_006666));
        titleLayout.setBackgroundResource(R.drawable.clamp_btn_press);
    }

    private void setClampDefaultSt(){
        textView.setTextColor(getResources().getColor(R.color.color_805500));
        titleLayout.setBackgroundResource(R.drawable.clamp_btn_selector);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        titleLayout.setEnabled(enabled);
    }
}
