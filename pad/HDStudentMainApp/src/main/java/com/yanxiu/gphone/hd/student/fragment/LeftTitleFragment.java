package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.view.badgeview.BadgeView;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.adapter.LeftTitleAdapter;
import com.yanxiu.gphone.hd.student.bean.TitleBean;
import com.yanxiu.gphone.hd.student.utils.HeadInfoObserver;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.view.MyHeaderInfoView;

import java.util.Date;


/**
 * Created by Administrator on 2016/1/19.
 */
public class LeftTitleFragment extends BaseFragment implements View.OnClickListener {
    private View mRootView;
    private final static String TAG=LeftTitleFragment.class.getSimpleName();
    private final static String DATA_FORMAT="yyyy.MM.dd";
    private MyHeaderInfoView headerInfo;
    private TitleSelectedListener mTitleSelect;
    private ListView mTitleListView;
    private LeftTitleAdapter adapter;
    private Button existBtn;//,homeBtn;
    private RelativeLayout groupViewLayout;
    private BadgeView badgeGroup;
    @Override
    public void onReset() {
        LogInfo.log(TAG,"onReset");
    }

    public interface TitleSelectedListener{
        void onTitleSelected(YanXiuConstant.TITLE_ENUM title_enum);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        mRootView= inflater.inflate(R.layout.left_title_fragment, null);
        mTitleListView=(ListView)mRootView.findViewById(R.id.titleListView);
        setCurrentData();

        headerInfo= (MyHeaderInfoView) mRootView.findViewById(R.id.myHeaderInfoView);
        HeadInfoObserver.getInstance().addObserver(headerInfo);

        View titleHeadView= View.inflate(getActivity(), R.layout.title_head_view, null);
        existBtn=(Button)titleHeadView.findViewById(R.id.exiseBtn);
        setSelectViewBg(existBtn, true);//默认是选择练习
        groupViewLayout=(RelativeLayout)titleHeadView.findViewById(R.id.group_view);
        badgeGroup = (BadgeView) titleHeadView.findViewById(R.id.groupBadgeView);
        badgeGroup.setBadgeTextSize(15);
        badgeGroup.setBackgroundResource(R.drawable.bg_oval_with_gradient);
        badgeGroup.setPadding(CommonCoreUtil.dipToPx(8), CommonCoreUtil.dipToPx(1),
                CommonCoreUtil.dipToPx(8), CommonCoreUtil.dipToPx(1));
        mTitleListView.addHeaderView(titleHeadView,null,false);
        adapter=new LeftTitleAdapter(getActivity());
        mTitleListView.setAdapter(adapter);
        setListener();
    }
    private void setSelectViewBg(View view, boolean isSelected){
        view.setBackgroundResource(isSelected ? R.drawable.left_btn_sel : R.drawable.left_btn_not_sel);
    }
    /**
     * 作业小红点更新 
     * @param num
     */
    public void updateGroupRedDot(int num){
        badgeGroup.setBadgeCount(num);
    }
    private void setCurrentData() {
        TextView dataText=(TextView)mRootView.findViewById(R.id.dataText);
        TextView weekText=(TextView)mRootView.findViewById(R.id.weekText);
        String date=CommonCoreUtil.getDateByFormatString(DATA_FORMAT);
        String week=CommonCoreUtil.getWeekOfDate(new Date(System.currentTimeMillis()));
        dataText.setText(date);
        weekText.setText(week);
    }


    private void setListener() {
        mTitleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mTitleSelect != null) {
                    LogInfo.log(TAG,"点击索引： "+position);
                    int pos=position - mTitleListView.getHeaderViewsCount();
                    TitleBean bean = adapter.getTitleBean(pos);
                    if(bean==null){
                        return;
                    }
                    tPushUpdate(bean.getTitle_enum());
                    adapter.setSelecteFlag(pos);
                }
            }
        });
        existBtn.setOnClickListener(this);
        groupViewLayout.setOnClickListener(this);
        headerInfo.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mRootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTitleSelect= (TitleSelectedListener) getActivity();
    }
    public void tPushUpdate(YanXiuConstant.TITLE_ENUM title_enum){
        resetSelect();
        mTitleSelect.onTitleSelected(title_enum);
        if (title_enum == YanXiuConstant.TITLE_ENUM.HOMEWORK_ENUM){
            setSelectViewBg(groupViewLayout, true);
        } else if(title_enum == YanXiuConstant.TITLE_ENUM.EXIST_ENUM){
            setSelectViewBg(existBtn, true);
        } else if(title_enum == YanXiuConstant.TITLE_ENUM.MYUSERINFO_ENUM){
            headerInfo.setSelected(true);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exiseBtn:
                LogInfo.log(TAG, "R.id.exiseBtn");
                if(mTitleSelect!=null){
                    tPushUpdate(YanXiuConstant.TITLE_ENUM.EXIST_ENUM);
                }
                break;
            case R.id.group_view:
                LogInfo.log(TAG,"R.id.homeworkBtn");
                if(mTitleSelect!=null){
                    tPushUpdate(YanXiuConstant.TITLE_ENUM.HOMEWORK_ENUM);
                }
                break;

            case R.id.myHeaderInfoView:
                if(mTitleSelect!=null){
                    tPushUpdate(YanXiuConstant.TITLE_ENUM.MYUSERINFO_ENUM);
                }
                break;
        }
    }

    private void resetSelect(){
        setSelectViewBg(existBtn, false);
        setSelectViewBg(groupViewLayout, false);
        headerInfo.setSelected(false);
        adapter.setSelecteFlag(-1);
    }

    @Override
    public void onDestroy() {
        HeadInfoObserver.getInstance().deleteObserver(headerInfo);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
