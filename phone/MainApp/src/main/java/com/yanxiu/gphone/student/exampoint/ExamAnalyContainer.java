package com.yanxiu.gphone.student.exampoint;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.ChartBarEntity;
import com.yanxiu.gphone.student.bean.DataElement;
import com.yanxiu.gphone.student.bean.ExamInfoBean;
import com.yanxiu.gphone.student.bean.ExamListInfo;
import com.yanxiu.gphone.student.exampoint.view.BarChartPanel;
import com.yanxiu.gphone.student.inter.GetDataCommonInter;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2015/11/16.
 */
public class ExamAnalyContainer extends LinearLayout implements Observer {
    private TextView examChildText;
    private BarChartPanel chartPanel;
    private LinearLayout leftValueContainer,rightValueContainer;
    private final int divide=6;
    private LayoutInflater mLayinflater;
    private int chartTotalValue;
    private GetDataCommonInter getDataCommonInter;
    private HorizontalScrollView scrollView;
    private   int rightItemHeight;
    private final int TOTAL_VALUE=100;
    private final int AVG_VALUE=20;
    private Context mContext;
    private static final String TAG=ExamAnalyContainer.class.getSimpleName();
    public ExamAnalyContainer(Context context) {
        super(context);
        initView(context);
    }

    public ExamAnalyContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
        setWillNotDraw(false);
    }

    public ExamAnalyContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        setWillNotDraw(false);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ExamAnalyContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
        setWillNotDraw(false);
    }

    private void initView(Context context){
        mContext=context;
        setOrientation(VERTICAL);
        mLayinflater=LayoutInflater.from(context);
        mLayinflater.inflate(R.layout.exam_analy_layout,this);
        examChildText=(TextView)findViewById(R.id.exam_childTitle);
        examChildText.setText(getResources().getString(R.string.exam_grasp_title));
        leftValueContainer=(LinearLayout)findViewById(R.id.leftValueContainer);
        rightValueContainer=(LinearLayout)findViewById(R.id.rightValueContainer);
        chartPanel=(BarChartPanel)findViewById(R.id.chart);
        scrollView=(HorizontalScrollView)findViewById(R.id.horizontalView);
        View examTopLine=findViewById(R.id.examTopLine);
        examTopLine.setLayerType(View.LAYER_TYPE_SOFTWARE,null);

        View view=new View(context);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        view.setBackgroundResource(R.drawable.zigzag_line);
        addView(view);

    }

    public void initData(){
        CommonCoreUtil.measureView(examChildText);
        CommonCoreUtil.measureView(rightValueContainer);
        CommonCoreUtil.measureView(leftValueContainer);
        if(getDataCommonInter!=null&&getDataCommonInter.getData()!=null){
            PullDownTitleView titleView = (PullDownTitleView) getDataCommonInter.getData();
            CommonCoreUtil.measureView(titleView);
        }
        createValueContainer();
    }

    private void createValueContainer() {
        createLeftValueContainer();
        createRightValueContainer();
    }

    private void createRightValueContainer() {
        rightItemHeight =rightValueContainer.getMeasuredHeight()/divide;
        for(int i=0;i<divide-1;i++){
            @SuppressLint("InflateParams") RelativeLayout rightItem= (RelativeLayout) mLayinflater.inflate(R.layout.right_value_item, null);
            RelativeLayout.LayoutParams viewParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rightItemHeight);
            rightItem.setLayoutParams(viewParams);
            rightValueContainer.addView(rightItem);
        }
        chartTotalValue=rightValueContainer.getMeasuredHeight();
    }

    private void createLeftValueContainer() {
        int totalValue=TOTAL_VALUE;
        int itemHeight=leftValueContainer.getMeasuredHeight()/divide;

        for(int i=0;i<divide-1;i++){
            if(totalValue>0){
                TextView text= (TextView) mLayinflater.inflate(R.layout.left_value_item, null);
                LinearLayout.LayoutParams textParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
                text.setPadding(0, 0, CommonCoreUtil.dipToPx(mContext, 5), 0);
                if(totalValue==TOTAL_VALUE){
                    text.setText(totalValue+"%");
                    itemHeight-= CommonCoreUtil.dipToPx(mContext, 5);
                }else{
                    text.setText(totalValue+"");
                }
                textParams.topMargin= CommonCoreUtil.dipToPx(mContext, 5);
                text.setLayoutParams(textParams);
                LogInfo.log(TAG, totalValue + "%");
                leftValueContainer.addView(text);
            }
            totalValue-=AVG_VALUE;

        }
    }


    @Override
    public void update(Observable observable, Object data) {
        if(data==null){
            return;
        }
        if(data instanceof ExamListInfo) {
            scrollView.scrollTo(0,0);
            CommonCoreUtil.measureView(chartPanel);
            chartPanel.setParams(convertDataToChart((ExamListInfo) data),rightItemHeight,chartTotalValue);
            chartPanel.startCanvas();
        }

    }

    private List<ChartBarEntity> convertDataToChart(ExamListInfo info) {
        List<ChartBarEntity> charList=new ArrayList<ChartBarEntity>();
        List<ExamInfoBean> infolist= info.getData();

        for(ExamInfoBean bean:infolist){

            ChartBarEntity entity=new ChartBarEntity();
            entity.setTitle(bean.getName());
            if(bean.getData()==null|| StringUtils.isEmpty(bean.getData().getAvgMasterRate())){
                entity.setDataElement(new DataElement(0.0f,BarChartPanel.platterTable[0]));
            }else{
                entity.setDataElement(new DataElement(BarChartPanel.convertValueToPercent(Float.valueOf(bean.getData().getAvgMasterRate())), BarChartPanel.platterTable[0]));
            }
            charList.add(entity);
        }
// TODO  this  is  the test data
//        ChartBarEntity entity1=new ChartBarEntity();
//        entity1.setTitle("我");
//        entity1.setDataElement(new DataElement(10.0f, BarChartPanel.platterTable[0]));
//
//        ChartBarEntity entity2=new ChartBarEntity();
//        entity2.setTitle("我我");
//        entity2.setDataElement(new DataElement(15.0f, BarChartPanel.platterTable[0]));
//
//        ChartBarEntity entity3=new ChartBarEntity();
//        entity3.setTitle("我我我");
//        entity3.setDataElement(new DataElement(23.0f, BarChartPanel.platterTable[0]));
//
//        ChartBarEntity entity4=new ChartBarEntity();
//        entity4.setTitle("我我我我");
//        entity4.setDataElement(new DataElement(44.0f, BarChartPanel.platterTable[0]));
//
//        ChartBarEntity entity5=new ChartBarEntity();
//        entity5.setTitle("我我我我我");
//        entity5.setDataElement(new DataElement(100.0f, BarChartPanel.platterTable[0]));
//
//        ChartBarEntity entity6=new ChartBarEntity();
//
//        entity6.setTitle("我我我我我我我我我我我我我我我我我我");
//        entity6.setDataElement(new DataElement(87.0f, BarChartPanel.platterTable[0]));
//
//        charList.add(entity1);
//        charList.add(entity2);
//        charList.add(entity3);
//        charList.add(entity4);
//        charList.add(entity5);
//        charList.add(entity6);


        return charList;
    }

    public void setGetDataCommonInter(GetDataCommonInter getDataCommonInter) {
        this.getDataCommonInter = getDataCommonInter;
    }
}
