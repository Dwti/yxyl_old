package com.yanxiu.gphone.studentold.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.NetWorkTypeUtils;
import com.common.core.utils.StringUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.activity.MistakeAllActivity;
import com.yanxiu.gphone.studentold.activity.MistakeDetailsActivity;
import com.yanxiu.gphone.studentold.activity.WrongAnswerViewActivity;
import com.yanxiu.gphone.studentold.adapter.MistakeAllFragmentAdapter;
import com.yanxiu.gphone.studentold.bean.MistakeAllFragBean;
import com.yanxiu.gphone.studentold.bean.MistakeAllFragmentBean;
import com.yanxiu.gphone.studentold.bean.MistakeRefreshAllBean;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.requestTask.RequestWrongChapterask;
import com.yanxiu.gphone.studentold.requestTask.RequestWrongKongledgeask;
import com.yanxiu.gphone.studentold.utils.QuestionUtils;
import com.yanxiu.gphone.studentold.utils.Util;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/31 17:00.
 * Function :
 */

public class MistakeAllFragment extends Fragment implements MistakeAllFragmentAdapter.OnItemClickListener {

    private Context mContext;
    private MistakeAllFragmentAdapter adapter;
    private String mType;
    private String stageId = "";
    private String subjectId = "";
    private String editionId = "";
    private List<MistakeAllFragBean> chapter_list = new ArrayList<>();
    private List<MistakeAllFragBean> kongledge_list = new ArrayList<>();
    private RelativeLayout rlConverLoadView;
    private RelativeLayout rlNoDataView;
    private TextView tvDescView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            stageId = bundle.getString("stageId", "");
            subjectId = bundle.getString("subjectId", "");
            editionId = bundle.getString("editionId", "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_mistakeall, container, false);
        initView(view);
        initData();
        initListener();
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initView(View view) {
        RecyclerView recyProSelect = (RecyclerView) view.findViewById(R.id.recy_cpter_koledge);
        recyProSelect.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MistakeAllFragmentAdapter(mContext);
        recyProSelect.setAdapter(adapter);

        rlNoDataView = (RelativeLayout) view.findViewById(R.id.relative_layout);
        rlConverLoadView = (RelativeLayout) view.findViewById(R.id.rl_conver_loading);
        tvDescView = (TextView) view.findViewById(R.id.text_dese);

        ImageView pbLoading = (ImageView) view.findViewById(R.id.pb_loaing);
        pbLoading.clearAnimation();
        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.xlistview_header_progress);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        pbLoading.startAnimation(operatingAnim);
    }

    private void initData() {

    }

    private void initListener() {
        adapter.setOnItemClickListener(this);
    }

    public void onEventMainThread(MistakeRefreshAllBean event) {
        if (event != null) {
            requestMistakeChapterData();
            requestMistakeKongledgeData();
        }
    }

    public void onEventMainThread(MistakeAllActivity.MistakeFragRefreshBean event) {
        if (event != null) {
            requestMistakeChapterData();
            requestMistakeKongledgeData();
        }
    }

    public void onEventMainThread(WrongAnswerViewActivity.WrongAnswerDeleteBean bean) {
        if (bean != null) {
            requestMistakeChapterData();
            requestMistakeKongledgeData();
        }
    }

    private void requestMistakeChapterData() {
        rlConverLoadView.setVisibility(View.VISIBLE);
        if (!NetWorkTypeUtils.isNetAvailable()) {
            Util.showUserToast(getString(R.string.public_loading_net_null_errtxt), null, null);
            return;
        }
        RequestWrongChapterask chapterask = new RequestWrongChapterask(mContext, stageId, subjectId, editionId, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                rlConverLoadView.setVisibility(View.GONE);
                MistakeAllFragmentBean fragmentBean = (MistakeAllFragmentBean) result;
                chapter_list.clear();
                if (fragmentBean != null && fragmentBean.getStatus() != null && fragmentBean.getStatus().getCode().equals("0")) {
                    chapter_list.addAll(fragmentBean.getData());
                    QuestionUtils.checkMistakeAllFragmentBean(0, chapter_list);
                } else {
                    if (mType.equals(MistakeAllActivity.MISTAKE_CHAPTER)) {
                        rlNoDataView.setVisibility(View.VISIBLE);
//                        tvDescView.setText(R.string.no_mistake_chapter);
                        rlNoDataView.setBackgroundResource(R.drawable.mis_no_chapter);
                    }
                }
                if (mType.equals(MistakeAllActivity.MISTAKE_CHAPTER)) {
                    setAdapterNotify(chapter_list);
                }
            }

            @Override
            public void dataError(int type, String msg) {
                rlConverLoadView.setVisibility(View.GONE);
                if (!StringUtils.isEmpty(msg)) {
                    Util.showUserToast(msg, null, null);
                } else {
                    Util.showUserToast(R.string.net_null_one, -1, -1);
                }
            }
        });
        chapterask.start();
    }

    private void requestMistakeKongledgeData() {
        rlConverLoadView.setVisibility(View.VISIBLE);
        if (!NetWorkTypeUtils.isNetAvailable()) {
            Util.showUserToast(getString(R.string.public_loading_net_null_errtxt), null, null);
            return;
        }
        RequestWrongKongledgeask kongledgeask = new RequestWrongKongledgeask(mContext, stageId, subjectId, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                MistakeAllFragmentBean fragmentBean = (MistakeAllFragmentBean) result;
                kongledge_list.clear();
                if (fragmentBean != null && fragmentBean.getStatus() != null && fragmentBean.getStatus().getCode().equals("0")) {
                    kongledge_list.addAll(fragmentBean.getData());
                    QuestionUtils.checkMistakeAllFragmentBean(0, kongledge_list);
                } else {
                    if (mType.equals(MistakeAllActivity.MISTAKE_KONGLEDGE)) {
                        rlNoDataView.setVisibility(View.VISIBLE);
//                        tvDescView.setText(R.string.no_mistake_kongledge);
                        rlNoDataView.setBackgroundResource(R.drawable.mis_no_questions);
                    }
                }
                if (mType.equals(MistakeAllActivity.MISTAKE_KONGLEDGE)) {
                    setAdapterNotify(kongledge_list);
                }
                rlConverLoadView.setVisibility(View.GONE);
            }

            @Override
            public void dataError(int type, String msg) {
                rlConverLoadView.setVisibility(View.GONE);
                if (!StringUtils.isEmpty(msg)) {
                    Util.showUserToast(msg, null, null);
                } else {
                    Util.showUserToast(R.string.net_null_one, -1, -1);
                }
            }
        });
        kongledgeask.start();
    }

    private void setAdapterNotify(List<MistakeAllFragBean> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    public void setData(String type) {
        if (rlNoDataView!=null) {
            rlNoDataView.setVisibility(View.GONE);
        }
        List<MistakeAllFragBean> list = new ArrayList<>();
        list.addAll(adapter.getData());
        switch (type) {
            case MistakeAllActivity.MISTAKE_CHAPTER:
                if (!type.equals(mType)) {
                    kongledge_list.clear();
                    kongledge_list.addAll(list);
                }else {
                    chapter_list.clear();
                    chapter_list.addAll(list);
                }
                this.mType = type;
                if (chapter_list != null && chapter_list.size() > 0) {
                    setAdapterNotify(chapter_list);
                } else {
                    setAdapterNotify(null);
                    requestMistakeChapterData();
                }
                break;
            case MistakeAllActivity.MISTAKE_KONGLEDGE:
                if (!type.equals(mType)) {
                    chapter_list.clear();
                    chapter_list.addAll(list);
                }else {
                    kongledge_list.clear();
                    kongledge_list.addAll(list);
                }
                this.mType = type;
                if (kongledge_list != null && kongledge_list.size() > 0) {
                    setAdapterNotify(kongledge_list);
                } else {
                    setAdapterNotify(null);
                    requestMistakeKongledgeData();
                }
                break;
        }
    }

    @Override
    public void itemClickListener(MistakeAllFragBean bean) {
        if (bean.getQids() != null && bean.getQids().size() > 0) {
            MistakeDetailsActivity.launch(getActivity(), bean.getName(), subjectId, bean.getQids());
        } else {
            Util.showUserToast(getString(R.string.no_mistake_kongledge), null, null);
        }
    }
}
