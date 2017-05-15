package com.yanxiu.gphone.studentold.activity;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.adapter.RankListAdapter;
import com.yanxiu.gphone.studentold.bean.DataStatusEntityBean;
import com.yanxiu.gphone.studentold.bean.RankResultBean;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.requestTask.RequestRankListTask;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.studentold.utils.PublicLoadUtils;
import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.studentold.view.PublicLoadLayout;
import com.common.core.view.xlistview.XListView;

/**
 * Created by Administrator on 2015/9/24.
 */
public class RankingListActivity extends TopViewBaseActivity{
    private static final String TAG=RankingListActivity.class.getSimpleName();
    private RankListAdapter adapter;
    private RequestRankListTask task;
    private XListView xListView;
    private TextView myRankText;
    private boolean isFirstLoad=true;
    private RelativeLayout rankingRootView;
    private final static String NO_RANKING="0";
    private final int SCREEN_960_HEIGHT=960;
    private ImageView rankingBg,rankingIcon;
    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mPublicLayout= PublicLoadUtils.createCustormPage(this, R.layout.ranking_list_activity);
        FrameLayout.LayoutParams publicLayout=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mPublicLayout.setLayoutParams(publicLayout);
        mPublicLayout.setContentBackground(getResources().getColor(R.color.trans));
        mPublicLayout.setBackgroundColor(getResources().getColor(R.color.trans));
        initView();
        mPublicLayout.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {

                requestTask();
            }
        });
        requestTask();
        return mPublicLayout;
    }

    private void loading(){
        mPublicLayout.loading(true);
    }



    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.addView(getRankingBgView(), INDEX_ZERO);
        rootView.addView(getRankingIcon(), INDEX_THRIRD);
    }



    private View getRankingIcon(){
        rankingIcon=new ImageView(this);
        rankingIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        rankingIcon.setImageResource(R.drawable.ranking_icon);
        RelativeLayout.LayoutParams iconParams= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(isChangeMarginTop()){
            iconParams.topMargin= CommonCoreUtil.dipToPx(this, 10);
        }else{
            iconParams.topMargin= CommonCoreUtil.dipToPx(this, 76);
        }

        iconParams.width= CommonCoreUtil.dipToPx(this, 235);
        iconParams.height= CommonCoreUtil.dipToPx(this, 102);
        iconParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rankingIcon.setLayoutParams(iconParams);
        return rankingIcon;
    }


    private View getRankingBgView(){
        rankingBg=new ImageView(this);
        rankingBg.setScaleType(ImageView.ScaleType.FIT_XY);
        rankingBg.setImageResource(R.drawable.ranking_bg);
        RelativeLayout.LayoutParams rankingBgParams= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        rankingBgParams.height= CommonCoreUtil.dipToPx(473);
        rankingBg.setLayoutParams(rankingBgParams);
        return rankingBg;
    }

    /**
     * 手机分辨率 小于960px 需要调整marginTop
     * @return
     */
    private boolean isChangeMarginTop(){
        return CommonCoreUtil.getScreenHeight()>=SCREEN_960_HEIGHT?false:true;
    }



    @Override
    protected void setContentContainerView() {
        super.setContentContainerView();
        contentContainer.setPadding(CommonCoreUtil.dipToPx(this, 4), CommonCoreUtil.dipToPx(this, 4), CommonCoreUtil.dipToPx(this, 4),0);
        RelativeLayout.LayoutParams contentParams= (RelativeLayout.LayoutParams) contentContainer.getLayoutParams();
        if(isChangeMarginTop()){
            contentParams.topMargin= CommonCoreUtil.dipToPx(this, 42);
        }else{
            contentParams.topMargin= CommonCoreUtil.dipToPx(this, 95);
        }

        contentParams.leftMargin= CommonCoreUtil.dipToPx(this, 23);
        contentParams.rightMargin= CommonCoreUtil.dipToPx(this, 23);
        contentParams.bottomMargin= CommonCoreUtil.dipToPx(this, 25);
        contentContainer.setLayoutParams(contentParams);
        contentContainer.setBackgroundResource(R.drawable.rank_list_container);


    }

    @Override
    protected void setTopView() {
        super.setTopView();
        topRootView.findViewById(R.id.topTitleView).setBackgroundResource(R.color.trans);
        topRootView.findViewById(R.id.lineView).setVisibility(View.GONE);
    }

    private void requestTask() {
        loading();
        cancelTask();
        task=new RequestRankListTask(this,callBack);
        task.start();
    }

    private AsyncCallBack callBack=new AsyncCallBack() {
        @Override
        public void update(YanxiuBaseBean result) {
            mPublicLayout.finish();
            RankResultBean resultBean= (RankResultBean) result;
            if(!checkParamas(resultBean)){
                return;
            }
            if(resultBean.getStatus().getCode()==DataStatusEntityBean.REQUEST_SUCCESS){
                finishPublicLayout();
                rankingRootView.setVisibility(View.VISIBLE);
                upDateUI(resultBean);
            }else{
                showErrorWithFlag(ErrorCode.JOSN_PARSER_ERROR,"",isFirstLoad);
            }
        }

        @Override
        public void dataError(int type, String msg) {
             stopExecute();
             mPublicLayout.finish();
            showErrorWithFlag(type,msg,isFirstLoad);


        }
    };

    private boolean checkParamas(RankResultBean resultBean){
        return !(resultBean == null || resultBean.getStatus() == null);
    }

    private void initView() {
        titleText.setVisibility(View.GONE);
        rankingRootView=(RelativeLayout)mPublicLayout.findViewById(R.id.rankingRootView);
        rankingRootView.setVisibility(View.INVISIBLE);
        xListView=(XListView)mPublicLayout.findViewById(R.id.xListView);
        xListView.setPullLoadEnable(false);
        xListView.setPullRefreshEnable(false);
        xListView.setFooterDividersEnabled(false);
        myRankText=(TextView)mPublicLayout.findViewById(R.id.myRankText);

        View footView=View.inflate(this,R.layout.rank_foot_view,null);
        xListView.addFooterView(footView);
        adapter=new RankListAdapter(this);
        xListView.setAdapter(adapter);
    }

    private void stopExecute(){
        xListView.stopLoadMore();
        xListView.stopRefresh();
    }
    private void  upDateUI(RankResultBean rankResultBean){
        if(rankResultBean==null){
            return;
        }
        setRankText(rankResultBean);
        if(rankResultBean.getData()==null||rankResultBean.getData().size()==0){
            showDataError(isFirstLoad);
            return;
        }else{
            isFirstLoad=false;
            adapter.setList(rankResultBean.getData());
        }

    }


    private void setRankText(RankResultBean rankResultBean){
        if(rankResultBean.getProperty()!=null){
            if(!StringUtils.isEmpty(rankResultBean.getProperty().getMyRank())){
                if(NO_RANKING.equalsIgnoreCase(rankResultBean.getProperty().getMyRank())){
                    myRankText.setText(getResources().getString(R.string.no_my_rank));
                }else{
                    myRankText.setText(Html.fromHtml("我的排名：第" + rankResultBean.getProperty().getMyRank() + "位"));
                }
            }else{
                myRankText.setText(getResources().getString(R.string.no_my_rank));
            }
        }else{
            myRankText.setText(getResources().getString(R.string.no_my_rank));
        }
    }




    @Override
    protected void setContentListener() {
        mPublicLayout.setmRefreshData(refreshData);
    }

    private PublicLoadLayout.RefreshData refreshData=new PublicLoadLayout.RefreshData() {
        @Override
        public void refreshData() {
            requestTask();
        }
    };


    @Override
    protected void destoryData() {
        cancelTask();
        adapter=null;
        xListView=null;
        myRankText=null ;
        rankingRootView=null;
        rankingBg=null;
        rankingIcon=null;
        System.gc();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){

        }
    }


    private void cancelTask(){
        if(task!=null){
            task.cancel();
            task=null;
        }
    }
    @Override
    protected void initLaunchIntentData() {

    }
}
