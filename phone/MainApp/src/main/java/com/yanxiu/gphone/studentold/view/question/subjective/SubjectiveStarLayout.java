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
public class SubjectiveStarLayout extends LinearLayout{

    public static final int MAX_COUNT_START_DEFAULT = 5;

    public static final int SELECT_CUONT_START_DEFAULT = 0;

    private Context mContext;

    private List<String> dataList;

    private LayoutInflater inflater;

    private int maxCountStar;

    private int selectCountStar;

    public SubjectiveStarLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public SubjectiveStarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SubjectiveStarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        if(attrs != null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.subjective_question);
            maxCountStar = typedArray.getInt(R.styleable.subjective_question_maxNumStar, MAX_COUNT_START_DEFAULT);
            selectCountStar = typedArray.getInt(R.styleable.subjective_question_selectNumStar, SELECT_CUONT_START_DEFAULT);
            LogInfo.log("geny", "---selectCountStar----" + selectCountStar);
            LogInfo.log("geny", "--maxCountStar----" + maxCountStar);
            typedArray.recycle();
        }

        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.setOrientation(LinearLayout.HORIZONTAL);
        for(int i = 0; i < maxCountStar; i++){
            View starView = inflater.inflate(R.layout.subjective_star, null);
            this.addView(starView);
        }
        selectStarCount(selectCountStar);
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
                ImageView starView = (ImageView) contentView.findViewById(R.id.img_star);
                starView.setImageResource(R.drawable.star_light);
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
