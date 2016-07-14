package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.StringUtils;
import com.common.core.view.roundview.RoundedImageView;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.ClassDetailBean;
import com.yanxiu.gphone.hd.student.bean.ClassInfoBean;
import com.yanxiu.gphone.hd.student.bean.GroupEventRefresh;
import com.yanxiu.gphone.hd.student.bean.RequestBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestCancelClassTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestClassInfoTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestExitClassTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.view.DelDialog;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/2/1.
 */
public class GroupDetailsFragment extends TopBaseFragment {

    private static final String TAG = GroupDetailsFragment.class.getSimpleName();
    public final static int ADD_CLASS = 1;
    public final static int EXIT_CLASS = 2;
    public final static int CANCEL_CLASS = 3;

    private GroupInfoContainerFragment fg;
    private TextView classNameView;
    private RelativeLayout classInfoLayout;
    private TextView classNumView;
    private TextView banzhurenView;
    private TextView peopleView;
    private TextView clickView;

    private RoundedImageView groupInfoIcon;
    private int type;

    private ClassInfoBean mClassInfoBean;

    private String classId;

    private DelDialog delDialog;

    private DelDialog cancelDialog;

    private RequestExitClassTask requestExitClassTask;

    private RequestCancelClassTask requestCancelClassTask;

    private RequestClassInfoTask requestGroupInfoTask;

    private RequestBean requestBean;

    public static Fragment newInstance (int type, int classId) {
        GroupDetailsFragment groupDetailsFragment = new GroupDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("classId", classId);
        groupDetailsFragment.setArguments(bundle);
        return groupDetailsFragment;
    }

    public static Fragment newInstance (int type, ClassInfoBean classInfoBean) {
        GroupDetailsFragment groupDetailsFragment = new GroupDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putSerializable("classInfoBean", classInfoBean);
        groupDetailsFragment.setArguments(bundle);
        return groupDetailsFragment;
    }

    @Override
    protected void setTopView () {
        super.setTopView();
        titleText.setText(R.string.group_info_title);
        leftView.setImageResource(R.drawable.group_cancel_selector);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finish();
            }
        });
    }

    @Override
    protected boolean isAttach () {
        return false;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected View getContentView () {
        fg = (GroupInfoContainerFragment) getParentFragment();
        classId = getArguments().getInt("classId", 0) + "";
        type = getArguments().getInt("type", 1);
        mClassInfoBean = (ClassInfoBean) getArguments().getSerializable("classInfoBean");
        mPublicLayout = PublicLoadUtils.createPage(getActivity(), R.layout.group_info_layout);
        mPublicLayout.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData () {
                requestGroupInfo();
            }
        });
        findView();
        return mPublicLayout;
    }

    @Override
    protected void initLoadData () {
        if (mClassInfoBean == null) {
            requestGroupInfo();
        } else {
            setData();
        }
    }

    private void findView () {
        groupInfoIcon = (RoundedImageView) mPublicLayout.findViewById(R.id.group_info_icon);
        groupInfoIcon.setCornerRadius(getResources().getDimensionPixelOffset(R.dimen.dimen_12));
        classNameView = (TextView) mPublicLayout.findViewById(R.id.group_info_name);
        classInfoLayout = (RelativeLayout) mPublicLayout.findViewById(R.id.group_layout);
        classNumView = (TextView) mPublicLayout.findViewById(R.id.group_info_num);
        banzhurenView = (TextView) mPublicLayout.findViewById(R.id.group_info_kemu_text);
        peopleView = (TextView) mPublicLayout.findViewById(R.id.group_info_people_text);
        clickView = (TextView) mPublicLayout.findViewById(R.id.group_info_click);
        if (type == ADD_CLASS) {
            clickView.setText(R.string.group_info_add);
        } else if (type == EXIT_CLASS) {
            clickView.setText(R.string.group_info_del);
        } else if (type == CANCEL_CLASS) {
            clickView.setText(R.string.class_info_cel);
        }
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (type == ADD_CLASS) {
                    fg.mIFgManager.addFragment(GroupEditPersonNameFragment.newInstance
                            (mClassInfoBean), true, GroupEditPersonNameFragment.class.getName());
                } else if ((type == EXIT_CLASS)) {
                    if (delDialog == null) {
                        String title = getResources()
                                .getString(R.string.class_del_title);
                        String sure = getResources()
                                .getString(R.string.class_cel_sure);
                        String cel = getResources()
                                .getString(R.string.class_del_cel);
                        delDialog = new DelDialog(getActivity(), title,
                                sure, cel, new DelDialog.DelCallBack() {
                            @Override
                            public void del () {
                                exitClass();
                            }

                            @Override
                            public void sure () {

                            }

                            @Override
                            public void cancel () {

                            }
                        });
                    }
                    delDialog.show();
                } else if ((type == CANCEL_CLASS)) {
                    if (cancelDialog == null) {
                        String title = getResources()
                                .getString(R.string.class_cel_title);
                        String sure = getResources()
                                .getString(R.string.class_cel_sure);
                        String cel = getResources()
                                .getString(R.string.class_del_cel);
                        cancelDialog = new DelDialog(getActivity(), title,
                                sure, cel, new DelDialog.DelCallBack() {
                            @Override
                            public void del () {
                                cancelClass();
                            }

                            @Override
                            public void sure () {
                            }

                            @Override
                            public void cancel () {

                            }
                        });
                    }
                    cancelDialog.show();
                }
            }
        });

    }

    private void requestGroupInfo () {
        mPublicLayout.loading(true);
        requestGroupInfoTask = new RequestClassInfoTask(getActivity(), classId, new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
                mPublicLayout.finish();
                if (result != null) {
                    mClassInfoBean = (ClassInfoBean) result;
                    if (mClassInfoBean.getStatus().getCode() == YanXiuConstant.SERVER_0) {
                        classInfoLayout.setVisibility(View.VISIBLE);
                        setData();
                    } else {
                        classInfoLayout.setVisibility(View.GONE);
                        mPublicLayout.netError(true);
                    }
                }
            }

            @Override
            public void dataError (int type, String msg) {
                classInfoLayout.setVisibility(View.GONE);
                mPublicLayout.netError(true);
            }
        });
        requestGroupInfoTask.start();
    }

    private void setData () {
        if (mClassInfoBean != null && mClassInfoBean.getData().size() > 0) {
            classInfoLayout.setVisibility(View.VISIBLE);
            ClassDetailBean dataEntity = mClassInfoBean.getData().get(0);
            classNameView.setText(dataEntity.getName());
            classNumView.setText(getResources()
                    .getString(R.string.group_info_num, dataEntity.getId()));
            banzhurenView.setText(dataEntity.getAdminName());
            peopleView.setText(dataEntity.getStdnum() + "");
        }
    }

    private void exitClass () {
        if (mClassInfoBean != null && mClassInfoBean.getData().size() > 0) {
            ClassDetailBean dataEntity = mClassInfoBean.getData().get(0);
            requestExitClassTask = new RequestExitClassTask(getActivity(), dataEntity.getId(), new
                    AsyncCallBack() {
                        @Override
                        public void update (YanxiuBaseBean result) {
                            if (result != null) {
                                requestBean = (RequestBean) result;
                                if (requestBean.getStatus().getCode() == YanXiuConstant.SERVER_0) {
                                    Util.showUserToast(R.string.group_item_del_success, -1, -1);
                                    finish();
                                    EventBus.getDefault().post(new GroupEventRefresh());//刷新作业首页数据
                                } else {
                                    Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                                }
                            }
                        }

                        @Override
                        public void dataError (int type, String msg) {
                            if (!StringUtils.isEmpty(msg)) {
                                Util.showUserToast(msg, null, null);
                            } else {
                                Util.showUserToast(R.string.net_null_one, -1, -1);
                            }
                        }
                    });
            requestExitClassTask.start();
        }
    }

    private void cancelClass () {
        if (mClassInfoBean != null && mClassInfoBean.getData().size() > 0) {
            ClassDetailBean dataEntity = mClassInfoBean.getData().get(0);
            requestCancelClassTask = new RequestCancelClassTask(getActivity(), dataEntity.getId()
                    , new
                    AsyncCallBack() {
                        @Override
                        public void update (YanxiuBaseBean result) {
                            if (result != null) {
                                requestBean = (RequestBean) result;
                                if (requestBean.getStatus().getCode() == YanXiuConstant.SERVER_0) {
                                    Util.showUserToast(R.string.group_item_cancel_success, -1, -1);
                                    finish();
                                    EventBus.getDefault().post(new GroupEventRefresh());//刷新作业首页数据
                                } else if (requestBean.getStatus().getCode() == 75) {
                                    Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                                    finish();
                                } else {
                                    Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                                }
                            }
                        }

                        @Override
                        public void dataError (int type, String msg) {
                            if (!StringUtils.isEmpty(msg)) {
                                Util.showUserToast(msg, null, null);
                            } else {
                                Util.showUserToast(R.string.net_null_one, -1, -1);
                            }
                        }
                    });
            requestCancelClassTask.start();
        }
    }

    private void cancelTask () {
        if (requestCancelClassTask != null) {
            requestCancelClassTask.cancel();
            requestCancelClassTask = null;
        }
    }

    @Override
    public void onClick (View view) {
        super.onClick(view);
    }

    @Override
    protected void setContentListener () {

    }

    @Override
    protected void destoryData () {
        cancelTask();
        finish();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        fg = null;
        classInfoLayout = null;
        groupInfoIcon = null;
        mClassInfoBean = null;
        delDialog = null;
        cancelDialog = null;
        requestBean = null;
    }

    private void finish () {
        if(fg!=null&&fg.mIFgManager!=null){
            fg.mIFgManager.popStack();
        }

    }

    @Override
    protected IFgManager getFragmentManagerFromSubClass () {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass () {
        return 0;
    }

    @Override
    public void onReset () {
        destoryData();
    }
}
