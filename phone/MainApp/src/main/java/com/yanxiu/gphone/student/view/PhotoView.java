package com.yanxiu.gphone.student.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.core.view.roundview.RoundedImageView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.ImagePreviewActivity;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by sp on 17-2-28.
 */

public class PhotoView extends FrameLayout {
    private Context mContext;
    private GridView gridView;
    private TextView tv_add;
    private int maxCount = 9;
    private ArrayList<String> mData = new ArrayList<>();
    private GridAdapter adapter;
    private AdapterView.OnItemClickListener onGridItemClickListener;

    public PhotoView(Context context) {
        super(context);
        initView(context);
    }

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View view = inflate(context, R.layout.photo_view, this);
        gridView = (GridView) view.findViewById(R.id.gridView);
        tv_add = (TextView) view.findViewById(R.id.tv_add);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        initListener();
    }

    private void initListener() {
        tv_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                photoAdd();
            }
        });

    }

    public void setData(ArrayList<String> data) {
        if (data != null && data.size() != 0) {
            //data.size > MAX_COUNT的情况应该去除多余的
            if (data.size() > maxCount) {
                for (int i = 0; i < maxCount; i++) {
                    ListIterator<String> iterator = data.listIterator();
                    mData.add(iterator.next());
                }
            }
            mData = data;
            initGridView(mData);
        } else if (gridView.getVisibility() == VISIBLE) {
            mData = data;
            gridView.setVisibility(GONE);
            tv_add.setVisibility(VISIBLE);
        }
    }

    public void setAddButtonText(String text){
        if(tv_add !=null)
            tv_add.setText(text);
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    private void initGridView(List<String> data) {
        adapter = new GridAdapter(mContext, mData);
        tv_add.setVisibility(GONE);
        gridView.setVisibility(VISIBLE);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < mData.size()) {
                    photoPreview(position, mData);
                } else {
                    photoAdd();
                }
            }
        });

    }

    private void photoPreview(int position, ArrayList<String> data) {
        ImagePreviewActivity.lanuch((Activity) mContext, data, position);

    }

    private void photoAdd() {
        MediaUtils.openLocalCamera(((Activity) mContext), MediaUtils.CAPATURE_AND_CROP);
    }

    public void add(String path) {
        if (!TextUtils.isEmpty(path)) {
            if(mData == null)
                mData = new ArrayList<>();
            if (mData.size() == 0) {
                mData.add(path);
                initGridView(mData);
            } else {
                mData.add(path);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void delete(String path) {
        if (!TextUtils.isEmpty(path) && mData.size() > 0) {
            mData.remove(path);
            if (mData.size() > 0) {
                adapter.notifyDataSetChanged();
            } else {
                gridView.setVisibility(GONE);
                tv_add.setVisibility(VISIBLE);
            }
        }
    }

    public void delete(int position) {
        if (mData.size() > 0 && position >= 0 && position < mData.size()) {
            mData.remove(position);
            if (mData.size() > 0) {
                adapter.notifyDataSetChanged();
            } else {
                gridView.setVisibility(GONE);
                tv_add.setVisibility(VISIBLE);
            }
        }
    }

    public void refresh() {
        if (adapter != null && mData.size() > 0) {
            adapter.notifyDataSetChanged();
        } else {
            gridView.setVisibility(GONE);
            tv_add.setVisibility(VISIBLE);
        }
    }

    public ArrayList<String> getPhotos() {
        return mData;
    }

    private class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private Context context;
        private int imgWidth;
        private List<String> data = new ArrayList<>();

        public GridAdapter(Context context, List<String> list) {
            this.context = context;
            if (list != null)
                this.data = list;
            inflater = LayoutInflater.from(context);
            imgWidth = Util.convertDpToPx(context, 35);
        }

        @Override
        public int getCount() {
            return data.size() < maxCount ? data.size() + 1 : maxCount;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.no_scroll_grid_item,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (RoundedImageView) convertView
                        .findViewById(R.id.item_grida_image);
                holder.image.setCornerRadius(getResources().getDimensionPixelOffset(R.dimen.dimen_10));
                holder.decorateImage = (ImageView) convertView.findViewById(R.id.item_grid_decorate);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == data.size()) {
                holder.decorateImage.setBackgroundResource(0);
                holder.image.setImageResource(R.drawable.add_pic_selector);
            } else {
                holder.decorateImage.setBackgroundResource(R.drawable.upload_pic);
                String absolutePath = data.get(position);
                Glide.with(context)
                        .load(absolutePath).asBitmap()
                        .placeholder(R.drawable.image_default)
                        .error(R.drawable.image_default)
                        .override(imgWidth, imgWidth).into(holder.image);
            }

            return convertView;
        }

        private class ViewHolder {
            public RoundedImageView image;
            public ImageView decorateImage;

        }

    }
}
