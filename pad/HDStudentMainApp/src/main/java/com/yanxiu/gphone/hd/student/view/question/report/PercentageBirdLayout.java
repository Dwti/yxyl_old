package com.yanxiu.gphone.hd.student.view.question.report;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.yanxiu.gphone.hd.student.R;

import java.util.List;

/**
 * Created by lidm on 2015/9/24.
 *  答题报告 小鸟百分比
 */
public class PercentageBirdLayout extends LinearLayout{

    public static final int MAX_COUNT_BIRD_DEFAULT = 20;

//    public static final int SELECT_CUONT_START_DEFAULT = 0;

    private Context mContext;

    private List<String> dataList;

    private LayoutInflater inflater;

//    private int maxCountStar;
//
//    private int selectCountStar = 55;

    public PercentageBirdLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public PercentageBirdLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public PercentageBirdLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs){
//        if(attrs != null){
//            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.subjective_question);
//            maxCountStar = typedArray.getInt(R.styleable.subjective_question_maxNumStar, MAX_COUNT_START_DEFAULT);
//            selectCountStar = typedArray.getInt(R.styleable.subjective_question_selectNumStar, SELECT_CUONT_START_DEFAULT);
//            LogInfo.log("geny", "---selectCountStar----" + selectCountStar);
//            LogInfo.log("geny", "--maxCountStar----" + maxCountStar);
//            typedArray.recycle();
//        }

        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.setOrientation(LinearLayout.HORIZONTAL);
        for(int i = 0; i < MAX_COUNT_BIRD_DEFAULT; i++){
            View birdView = inflater.inflate(R.layout.percentage_bird, null);
            this.addView(birdView);
            LayoutParams lp = (LayoutParams) birdView.getLayoutParams();
            lp.weight = 1;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            birdView.setLayoutParams(lp);
        }
//        selectAccuracyCount(selectCountStar);
//        initData();

    }


    public void setDataList(List<String> dataList){
        this.dataList = dataList;
    }


    public void setAccuracyCount(int accuracy){
        if(accuracy >100 && accuracy < 0){
            return;
        }
        int tensPlace =  accuracy / 10;
        int theUnit = accuracy % 10;
        if(tensPlace >= 1){
            for(int i = 0; i < tensPlace; i++) {
                FrameLayout contentView1 = (FrameLayout) this.getChildAt(i*2);
                FrameLayout contentView2 = (FrameLayout) this.getChildAt(i*2 + 1);
                ImageView starView1 = (ImageView) contentView1.findViewById(R.id.img_bird);
                ImageView starView2 = (ImageView) contentView2.findViewById(R.id.img_bird);
                starView1.setImageResource(R.drawable.woodpecker_prog);
                starView2.setImageResource(R.drawable.woodpecker_prog);
            }

            if(theUnit < 10 && theUnit > 5){
                FrameLayout contentView1 = (FrameLayout) this.getChildAt(tensPlace*2);
                FrameLayout contentView2 = (FrameLayout) this.getChildAt(tensPlace*2 + 1);
                ImageView starView1 = (ImageView) contentView1.findViewById(R.id.img_bird);
                ImageView starView2 = (ImageView) contentView2.findViewById(R.id.img_bird);
                starView1.setImageResource(R.drawable.woodpecker_prog);
                starView2.setImageResource(R.drawable.woodpecker_prog_clip);
                ClipDrawable clipDrawable = (ClipDrawable) starView2.getDrawable();
                clipDrawable.setLevel(Math.round((theUnit - 5) * 2 * 1000));
            }else if(theUnit > 0 && theUnit <= 5){
                FrameLayout contentView = (FrameLayout) this.getChildAt(tensPlace*2);
                ImageView starView = (ImageView) contentView.findViewById(R.id.img_bird);
                starView.setImageResource(R.drawable.woodpecker_prog_clip);
                ClipDrawable clipDrawable = (ClipDrawable) starView.getDrawable();
                clipDrawable.setLevel(Math.round(theUnit * 2 * 1000));
            }
        }
//        int childCount = this.getChildCount();
//        if(count > childCount){
//            return;
//        }else{
//            for(int i = 0; i < count; i++){
//                FrameLayout contentView = (FrameLayout) this.getChildAt(i);
//                ImageView starView = (ImageView) contentView.findViewById(R.id.img_star);
//                starView.setImageResource(R.drawable.star_light);
//            }
//        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
