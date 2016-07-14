package com.yanxiu.gphone.hd.student.fragment;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.common.core.view.xlistview.XListView;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.adapter.RankListAdapter;
import com.yanxiu.gphone.hd.student.bean.DataStatusEntityBean;
import com.yanxiu.gphone.hd.student.bean.RankResultBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestRankListTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

/**
 * Created by Administrator on 2016/1/21.
 */
public class RankingFgment extends  TopBaseFragment {

    private static final String TAG=RankingFgment.class.getSimpleName();
    private RankListAdapter adapter;
    private RequestRankListTask task;
    private XListView xListView;
    private TextView myRankText;
    private boolean isFirstLoad=true;
    private RelativeLayout rankingRootView;
    private final static String NO_RANKING="0";
    private ImageView rankingBg,rankingIcon;
    public static Fragment newInstance(){

        return new RankingFgment();
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mPublicLayout= PublicLoadUtils.createCustormPage(getActivity(), R.layout.ranking_list_activity);
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
        rootView.setBackgroundResource(R.drawable.wood_bg);
        rootView.addView(getRankingBgView(), INDEX_ZERO);
        rootView.addView(getRankingIcon(), INDEX_THRIRD);
    }

    private View getRankingIcon(){
        rankingIcon=new ImageView(getActivity());
        rankingIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        rankingIcon.setImageResource(R.drawable.ranking_icon);
        RelativeLayout.LayoutParams iconParams= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(isChangeMarginTop()){
            iconParams.topMargin= CommonCoreUtil.dipToPx(getActivity(), 10);
        }else{
            iconParams.topMargin= CommonCoreUtil.dipToPx(getActivity(), 76);
        }

        iconParams.width= CommonCoreUtil.dipToPx(getActivity(), 235);
        iconParams.height= CommonCoreUtil.dipToPx(getActivity(), 102);
        iconParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rankingIcon.setLayoutParams(iconParams);
        return rankingIcon;
    }


    private View getRankingBgView(){
        rankingBg=new ImageView(getActivity());
        rankingBg.setScaleType(ImageView.ScaleType.FIT_XY);
        rankingBg.setImageResource(R.drawable.ranking_bg);
        RelativeLayout.LayoutParams rankingBgParams= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        rankingBgParams.leftMargin=CommonCoreUtil.dipToPx(getActivity(), 15);
        rankingBgParams.rightMargin=CommonCoreUtil.dipToPx(getActivity(),15);
        rankingBgParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rankingBg.setLayoutParams(rankingBgParams);
        return rankingBg;
    }

    /**
     * 手机分辨率 小于960px 需要调整marginTop
     * @return
     */
    private boolean isChangeMarginTop(){
        int SCREEN_960_HEIGHT = 960;
        return CommonCoreUtil.getScreenHeight()>= SCREEN_960_HEIGHT ?false:true;
    }

    @Override
    protected void setContentContainerView() {
        super.setContentContainerView();
        contentContainer.setPadding(CommonCoreUtil.dipToPx(getActivity(), 4), CommonCoreUtil.dipToPx(getActivity(), 4), CommonCoreUtil.dipToPx(getActivity(), 4),0);
        RelativeLayout.LayoutParams contentParams= (RelativeLayout.LayoutParams) contentContainer.getLayoutParams();
        if(isChangeMarginTop()){
            contentParams.topMargin= CommonCoreUtil.dipToPx(getActivity(), 42);
        }else{
            contentParams.topMargin= CommonCoreUtil.dipToPx(getActivity(), 95);
        }

        contentParams.leftMargin= CommonCoreUtil.dipToPx(getActivity(), 40);
        contentParams.rightMargin= CommonCoreUtil.dipToPx(getActivity(), 43);
        contentParams.bottomMargin= CommonCoreUtil.dipToPx(getActivity(), 25);
        contentContainer.setLayoutParams(contentParams);
        contentContainer.setBackgroundResource(R.drawable.rank_list_container);

    }

    @Override
    protected void setTopView() {
        super.setTopView();
        topRootView.findViewById(R.id.topTitleView).setBackgroundResource(R.color.trans);
        topRootView.findViewById(R.id.lineView).setVisibility(View.GONE);
        topRootView.findViewById(R.id.bottom_view).setVisibility(View.GONE);
        leftView.setVisibility(View.GONE);
    }

    private void requestTask() {
        loading();
        cancelTask();
        task=new RequestRankListTask(getActivity(),callBack);
        task.start();
    }

    private final AsyncCallBack callBack=new AsyncCallBack() {
        @Override
        public void update(YanxiuBaseBean result) {
            stopExecute();
            mPublicLayout.finish();
            RankResultBean resultBean= (RankResultBean) result;
            if(!checkParamas(resultBean)){
                return;
            }
            if(resultBean.getStatus().getCode()== DataStatusEntityBean.REQUEST_SUCCESS){
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

        adapter=new RankListAdapter(getActivity());
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
    protected void initLoadData() {

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
    }

    @Override
    public void onDestroy() {
        cancelTask();
        adapter=null;
        xListView=null;
        myRankText=null ;
        rankingRootView=null;
        rankingBg=null;
        rankingIcon=null;
        System.gc();
        super.onDestroy();
    }

    private void cancelTask(){
        if(task!=null){
            task.cancel();
            task=null;
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
