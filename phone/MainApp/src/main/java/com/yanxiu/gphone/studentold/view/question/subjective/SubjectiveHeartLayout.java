package com.yanxiu.gphone.studentold.view.question.subjective;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.studentold.R;

import java.util.List;

/**
 * Created by lidm on 2015/9/24.
 *  主观题的描述中的星星
 */
public class SubjectiveHeartLayout extends LinearLayout{

    public static final int MAX_COUNT_START_DEFAULT = 5;

    public static final int SELECT_CUONT_START_DEFAULT = 0;

    private Context mContext;

    private List<String> dataList;

    private LayoutInflater inflater;

    private int maxCountHeart;

    private int selectCountHeart;

    public SubjectiveHeartLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public SubjectiveHeartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SubjectiveHeartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        if(attrs != null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.subjective_question);
            maxCountHeart = typedArray.getInt(R.styleable.subjective_question_maxNumStar, MAX_COUNT_START_DEFAULT);
            selectCountHeart = typedArray.getInt(R.styleable.subjective_question_selectNumStar, SELECT_CUONT_START_DEFAULT);
            LogInfo.log("geny", "---selectCountStar----" + selectCountHeart);
            LogInfo.log("geny", "--maxCountStar----" + maxCountHeart);
            typedArray.recycle();
        }

        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.setOrientation(LinearLayout.HORIZONTAL);
        for(int i = 0; i < maxCountHeart; i++){
            View starView = inflater.inflate(R.layout.subjective_heart, null);
            this.addView(starView);
        }
        selectStarCount(selectCountHeart);
//        initData();

    }


    public void setDataList(List<String> dataList){
        this.dataList = dataList;
    }

    private void initData(){

//        selectStarCount(selectCountStar);
    }

    public void selectStarCount(int count){
        int childCount = this.getChildCount();
        if(count > childCount){
            return;
        }else{
            for(int i = 0; i < count; i++){
                FrameLayout contentView = (FrameLayout) this.getChildAt(i);
                ImageView starView = (ImageView) contentView.findViewById(R.id.img_heart);
                starView.setImageResource(R.drawable.heart_light);
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
