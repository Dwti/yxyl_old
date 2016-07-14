package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.common.core.utils.BasePopupWindow;
import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

/**
 * Created by Administrator on 2015/12/17.
 */
public class TypePop<T> extends BasePopupWindow {
    private LinearLayout stageTypeContainer;


    private OnTypeItemClickListener onStageTypeItemClickListener;

    public void setOnTypeItemClickListener(OnTypeItemClickListener onStageTypeItemClickListener){
        this.onStageTypeItemClickListener=onStageTypeItemClickListener;
    }

    public interface  OnTypeItemClickListener{
        void OnItemClick(View v);
    }


    public TypePop(Context mContext) {
        super(mContext);
    }

    @Override
    protected void initView(Context mContext) {
        this.pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        View view=View.inflate(mContext, R.layout.stage_type_pop_layout,null);
        this.pop.setContentView(view);
        stageTypeContainer=(LinearLayout)view.findViewById(R.id.stageTypeContainer);
        loadingData();
    }

    @Override
    public void loadingData() {
        int [] stageTypeArray=new int[]{YanXiuConstant.Gender.GENDER_TYPE_MALE,YanXiuConstant.Gender.GENDER_TYPE_FEMALE};
         int totalTypes=stageTypeArray.length;
         for(int i=0;i<totalTypes;i++){
             stageTypeContainer.addView(getTypeTextView(stageTypeArray[i]));
             if(i<totalTypes-1){
                 stageTypeContainer.addView(getDashedLine());
             }
         }
    }


    private TextView getTypeTextView(int type){
        TextView text=new TextView(mContext);
        LinearLayout.LayoutParams textParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mContext.getResources().getDimensionPixelOffset(R.dimen
        .dimen_40));
        text.setLayoutParams(textParams);
        text.setGravity(Gravity.CENTER);
        text.setTag(type);
        text.setTextSize(CommonCoreUtil.dipToPx(mContext, 10));
        text.setClickable(true);
        text.setOnClickListener(this);
        text.setText(getTypeString(type));
        text.setShadowLayer(2, 0, 5, mContext.getResources().getColor(R.color.color_ffff99));
        return text;
    }

    private View getDashedLine(){
        View dashedLine=new View(mContext);
        LinearLayout.LayoutParams dashedLineParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_2));
        dashedLine.setLayoutParams(dashedLineParams);
        dashedLine.setBackgroundResource(R.drawable.icon_sel_top_line);
        dashedLine.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        return  dashedLine;
    }



    private int getTypeString(int type){
        switch (type){
            case YanXiuConstant.Gender.GENDER_TYPE_MALE:
                return R.string.male_txt;

            case YanXiuConstant.Gender.GENDER_TYPE_FEMALE:
                return R.string.female_txt;

            default:
                return 0;
        }
    }


    @Override
    protected void destoryData() {

    }



    @Override
    public void onClick(View view) {
        int type=(int)view.getTag();
        switch (type){
            case YanXiuConstant.Gender.GENDER_TYPE_MALE:
                if (onStageTypeItemClickListener != null) {
                    onStageTypeItemClickListener.OnItemClick(view);
                }
                break;

            case YanXiuConstant.Gender.GENDER_TYPE_FEMALE:
                if (onStageTypeItemClickListener != null) {
                    onStageTypeItemClickListener.OnItemClick(view);
                }
                break;

            default:
                break;
        }
        dismiss();

    }
}
