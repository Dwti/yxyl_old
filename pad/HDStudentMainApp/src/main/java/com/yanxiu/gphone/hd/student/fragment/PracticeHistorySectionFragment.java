package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.eventbusbean.ExHistoryEventBus;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.view.ChapterTabTitleLayout;
import com.yanxiu.gphone.hd.student.view.TitleTabLayout;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/2/17.
 */
public class PracticeHistorySectionFragment extends TopBaseFragment  implements View.OnClickListener {

    private ExercisesContainerHisFragment mFg;
    private static final String TITLE = "title";
    private static final String SUBJECTID = "subjectId";
    private static final String EDITIONID = "editionId";
    private static final String HAS_KNP = "has_knp";

    private ChapterTabTitleLayout chapterTabTitleLayout;
    private View rightView;
    private TextView rightTextView;
    private ImageView rightImageView;
    private int currItem = -1;


    private String title;
    private String subjectId;
    private String editionId;
    private boolean hasKnp;

    public FragmentManager fragmentManager;
    public ChapterHistoryFragment chapterHistoryFragment;
    public TestCenterHistoryFragment testCenterHistoryFragment;



    private static PracticeHistorySectionFragment practiceHistorySectionFragment;

    public static Fragment newInstance(String title, String subjectId, String editionId, int has_knp){
        if(practiceHistorySectionFragment == null){
            practiceHistorySectionFragment =new PracticeHistorySectionFragment();
            Bundle bundle=new Bundle();
            bundle.putString(TITLE, title);
            bundle.putString(SUBJECTID, subjectId);
            bundle.putString(EDITIONID, editionId);
            bundle.putInt(HAS_KNP, has_knp);
            practiceHistorySectionFragment.setArguments(bundle);
        }

        return practiceHistorySectionFragment;
    }


    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.blue_bg);
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mFg= (ExercisesContainerHisFragment) getParentFragment();
        mPublicLayout= PublicLoadUtils.createPage(getActivity(), R.layout.activity_history_layout);
        mPublicLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mPublicLayout.setContentBackground(android.R.color.transparent);
        findView();
        getArgumentsData();
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgumentsData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        mFg= (ExercisesContainerHisFragment) getParentFragment();
        mPublicLayout= PublicLoadUtils.createPage(getActivity(), R.layout.activity_history_layout);
        mPublicLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mPublicLayout.setContentBackground(android.R.color.transparent);
        mPublicLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //解决当前Framgemt被下一个Fragment覆盖后还能响应事件的Bug
                return true;
            }
        });
        findView();

        initData();

        return mPublicLayout;
    }


    @Override
    protected void initLoadData() {

    }

    protected void initData(){
        fragmentManager = this.getChildFragmentManager();
        if (!TextUtils.isEmpty(title)) {
            chapterTabTitleLayout.setLeftTitle("");
            chapterTabTitleLayout.setCenterTitle(title);
        }
        hideAndShowFragment(AbsHistoryFragment.CHAPTER_HISTORY);
    }


    private void getArgumentsData(){
        title=getArguments().getString(TITLE);
        subjectId=getArguments().getString(SUBJECTID);
        editionId=getArguments().getString(EDITIONID);
        hasKnp = getArguments().getInt(HAS_KNP, 0) == 1;

    }

    private void findView () {
        chapterTabTitleLayout = (ChapterTabTitleLayout) mPublicLayout.findViewById(R.id.rl_top_title);
        chapterTabTitleLayout.getLlRightBack().setOnClickListener(this);
        rightView = mPublicLayout.findViewById(R.id.rl_right);
        rightTextView = (TextView) mPublicLayout.findViewById(R.id.intell_volume_txt);
        rightImageView = (ImageView)mPublicLayout.findViewById(R.id.iv_right);
        TitleTabLayout titleTabLayout = (TitleTabLayout) mPublicLayout.findViewById(R.id.view_tab);

        chapterTabTitleLayout.setLeftTitle("");
        chapterTabTitleLayout.setCenterTitle(title);
        if(hasKnp){
            titleTabLayout.setVisibility(View.VISIBLE);
        }
        titleTabLayout.setOnTitleTabClick(new TitleTabLayout.OnTitleTabClick() {
            @Override
            public void onLeftClick() {
                LogInfo.log("geny", "onLeftClick =====  ");
                hideAndShowFragment(AbsHistoryFragment.CHAPTER_HISTORY);
            }

            @Override
            public void onRightClick() {
                LogInfo.log("geny", "onRightClick =====  ");
                hideAndShowFragment(AbsHistoryFragment.TEST_CENTER_HISTORY);
            }
        });
    }

    public void onEventMainThread(ExHistoryEventBus exHistoryEventBus){
        switch (currItem) {
            case AbsHistoryFragment.CHAPTER_HISTORY:
                if (chapterHistoryFragment != null) {
                    chapterHistoryFragment.refreshData();
                }
                break;
            case AbsHistoryFragment.TEST_CENTER_HISTORY:
                if (testCenterHistoryFragment != null) {
                    testCenterHistoryFragment.refreshData();
                }
                break;
        }
    }



    public void hideAndShowFragment(int index){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(currItem == index){
            return;
        }
        currItem = index;
        if (chapterHistoryFragment != null) {
            transaction.hide(chapterHistoryFragment);
        }
        if (testCenterHistoryFragment != null) {
            transaction.hide(testCenterHistoryFragment);
        }
        switch (currItem) {
            case AbsHistoryFragment.CHAPTER_HISTORY:
                if (chapterHistoryFragment == null) {
                    chapterHistoryFragment = new ChapterHistoryFragment();
                    chapterHistoryFragment.setArguments(getDataBudle());
                    chapterHistoryFragment.setRightView(rightView,rightTextView,rightImageView );
                    transaction.add(R.id.view_content, chapterHistoryFragment);
                } else {
                    transaction.show(chapterHistoryFragment);
                    chapterHistoryFragment.setRightViewVisible(true);
                }
                break;
            case AbsHistoryFragment.TEST_CENTER_HISTORY:
                if (testCenterHistoryFragment == null) {
                    testCenterHistoryFragment = new TestCenterHistoryFragment();
                    testCenterHistoryFragment.setArguments(getDataBudle());
                    testCenterHistoryFragment.setRightView(rightView, rightTextView, rightImageView );
                    transaction.add(R.id.view_content, testCenterHistoryFragment);
                } else {
                    transaction.show(testCenterHistoryFragment);
                    testCenterHistoryFragment.setRightViewVisible(false);
                }
                break;
        }
        transaction.commit();
    }

    private Bundle getDataBudle(){
        Bundle args = new Bundle();
        args.putSerializable("title", title);
        args.putString("subjectId", subjectId);
        args.putString("editionId", editionId);
        return args;
    }


    @Override
    protected void setContentListener() {

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
    public void onClick(View v) {
        if(v == chapterTabTitleLayout.getLlRightBack()){
            executeFinish();
        }
    }

    @Override
    protected void destoryData() {

        finish();
    }
    private void finish() {
        currItem = -1;
        if(mFg!=null&&mFg.mIFgManager!=null){
            mFg.mIFgManager.popStack();
        }
        practiceHistorySectionFragment = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mFg=null;
        chapterTabTitleLayout=null;
        rightView=null;
        rightTextView=null;
        rightImageView=null;
        fragmentManager=null;
        chapterHistoryFragment=null;
        testCenterHistoryFragment=null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            executeFinish();
            return true;
        }
        return false;
    }


    @Override
    public void onReset() {
        destoryData();
    }
}
