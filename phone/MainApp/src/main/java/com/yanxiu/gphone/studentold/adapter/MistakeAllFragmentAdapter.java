package com.yanxiu.gphone.studentold.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.bean.MistakeAllFragBean;
import com.yanxiu.gphone.studentold.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/4/1 16:38.
 * Function :
 */

public class MistakeAllFragmentAdapter extends RecyclerView.Adapter<MistakeAllFragmentAdapter.MyViewHolder> {
    private Context mContext;
    private List<MistakeAllFragBean> mDatas = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private List<View> view_list = new ArrayList<>();

    public MistakeAllFragmentAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<MistakeAllFragBean> list) {
        this.mDatas.clear();
        if (list != null) {
            this.mDatas.addAll(list);
        }
    }

    public List<MistakeAllFragBean> getData() {
        return this.mDatas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_misallfragment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MistakeAllFragBean bean = mDatas.get(position);
        holder.tvContentView.setText(bean.getName());
        holder.tvNumberView.setText(String.valueOf(bean.getQuestion_num()));
        if (bean.isExPand()) {
            holder.ivButtonView.setBackgroundResource(R.drawable.chapter_center_examination_down);
        } else {
            holder.ivButtonView.setBackgroundResource(R.drawable.chapter_center_examination_up);
        }

        if (bean.getHierarchy() == 0) {
            holder.llItemBodyView.setBackgroundResource(R.drawable.father_selector_examination_bg);
            holder.tvContentView.setTextSize(17);
            holder.tvNumberView.setTextSize(17);
            setImageParams(holder.ivButtonView, 34);
        } else if (bean.getHierarchy() == 1) {
            holder.llItemBodyView.setBackgroundResource(R.drawable.child_selector_examination_bg);
            holder.tvContentView.setTextSize(16);
            holder.tvNumberView.setTextSize(16);
            setImageParams(holder.ivButtonView, 28);
        } else {
            holder.llItemBodyView.setBackgroundResource(R.drawable.grandson_selector_examination_bg);
            holder.tvContentView.setTextSize(14);
            holder.tvNumberView.setTextSize(14);
            setImageParams(holder.ivButtonView, 24);
        }

        int count = holder.llAddChildrenPlaceView.getChildCount();
        for (int j = 0; j < count; j++) {
            View view = holder.llAddChildrenPlaceView.getChildAt(0);
            view_list.add(view);
            holder.llAddChildrenPlaceView.removeView(view);
        }

        for (int i = 0; i < bean.getHierarchy(); i++) {
            if (view_list.size() > 0) {
                holder.llAddChildrenPlaceView.addView(view_list.get(0));
                view_list.remove(0);
            } else {
                View view = LayoutInflater.from(mContext).inflate(R.layout.mistake_all_place_holder, holder.llAddChildrenPlaceView, false);
                holder.llAddChildrenPlaceView.addView(view);
            }
        }

        if (bean.isHaveChildren()) {
            holder.ivButtonView.setVisibility(View.VISIBLE);
            holder.ivButtonView.setClickable(true);
        } else {
            holder.ivButtonView.setVisibility(View.INVISIBLE);
            holder.ivButtonView.setClickable(false);
        }
    }

    private void setImageParams(View view, int dip) {
        int px = Util.dipToPx(dip);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(px, px);
        view.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public interface OnItemClickListener {
        void itemClickListener(MistakeAllFragBean bean);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivButtonView;
        RelativeLayout llItemBodyView;
        TextView tvContentView;
        TextView tvNumberView;
        LinearLayout llAddChildrenPlaceView;

        MyViewHolder(View itemView) {
            super(itemView);
            llAddChildrenPlaceView = (LinearLayout) itemView.findViewById(R.id.ll_place_children);
            ivButtonView = (ImageView) itemView.findViewById(R.id.iv_left_icon);
            llItemBodyView = (RelativeLayout) itemView.findViewById(R.id.ll_item_body);
            tvContentView = (TextView) itemView.findViewById(R.id.tv_exa_title);
            tvNumberView = (TextView) itemView.findViewById(R.id.tv_exa_error);
            tvNumberView.setVisibility(View.VISIBLE);
            ivButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    MistakeAllFragBean bean = mDatas.get(position);
                    List<MistakeAllFragBean> list = bean.getChildren();
                    if (list == null || list.size() == 0) {
                        return;
                    }
                    int count = list.size();
                    int dataCount = mDatas.size();
                    if (bean.isExPand()) {
                        ivButtonView.setBackgroundResource(R.drawable.chapter_center_examination_up);
                        setRemove(mDatas, list);
                        MistakeAllFragmentAdapter.this.notifyItemRangeRemoved(position + 1, dataCount - mDatas.size());
                        bean.setExPand(false);
                    } else {
                        ivButtonView.setBackgroundResource(R.drawable.chapter_center_examination_down);
                        mDatas.addAll(position + 1, list);
                        MistakeAllFragmentAdapter.this.notifyItemRangeInserted(position + 1, count);
                        bean.setExPand(true);
                    }
                }
            });
            llItemBodyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    MistakeAllFragBean bean = mDatas.get(position);
                    if (onItemClickListener != null) {
                        onItemClickListener.itemClickListener(bean);
                    }
                }
            });
        }

        private void setRemove(List<MistakeAllFragBean> defult_list, List<MistakeAllFragBean> remove_list) {
            for (int i = 0; i < remove_list.size(); i++) {
                MistakeAllFragBean fragBean = remove_list.get(i);
                setRemoveSelf(defult_list, fragBean);
                if (fragBean.getChildren() != null && fragBean.getChildren().size() > 0) {
                    setRemove(defult_list, fragBean.getChildren());
                }
            }
        }

        private void setRemoveSelf(List<MistakeAllFragBean> list, MistakeAllFragBean fragBean) {
            int index = -1;
            for (int j = 0; j < list.size(); j++) {
                MistakeAllFragBean bean = list.get(j);
                if (bean.getId() == fragBean.getId()) {
                    bean.setExPand(false);
                    index = j;
                }
            }
            if (index != -1) {
                list.remove(index);
            }
        }
    }
}
