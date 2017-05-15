package com.yanxiu.gphone.studentold.view.picsel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yanxiu.gphone.studentold.R;
import com.common.core.utils.BasePopupWindow;

/**
 * Created by Administrator on 2015/10/12.
 */
public class PicDelPopup extends BasePopupWindow {
    public PicDelPopup(Context mContext) {
        super(mContext);
    }

    @Override
    protected void initView(Context mContext) {
        View view=View.inflate(mContext, R.layout.pic_sel_pop_view,null);
        this.pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.pop.setContentView(view);
        TextView firText=(TextView)view.findViewById(R.id.firText);
        firText.setText(mContext.getResources().getString(R.string.is_del_pic_tips));

        LinearLayout secLinear=(LinearLayout)view.findViewById(R.id.secLinear);
        TextView secText=(TextView)view.findViewById(R.id.secText);
        secText.setText(mContext.getResources().getString(R.string.del_tip));

        LinearLayout thrLinear=(LinearLayout)view.findViewById(R.id.thrLinear);
        TextView thrText=(TextView)view.findViewById(R.id.thrText);
        thrText.setText(mContext.getResources().getString(R.string.cancel_txt));
        TextView fourText=(TextView)view.findViewById(R.id.fourText);
        fourText.setVisibility(View.GONE);

        secLinear.setOnClickListener(this);
        secLinear.setTag(ItemClickListener.TWO);
        thrLinear.setOnClickListener(this);
        thrLinear.setTag(ItemClickListener.THR);
    }

    @Override
    public void loadingData() {

    }

    @Override
    protected void destoryData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.secLinear:
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick((Integer) view.getTag());
                }
                dismiss();
                break;
            case R.id.thrLinear:
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick((Integer) view.getTag());
                }
                dismiss();
                break;
        }
    }
}
