package com.yanxiu.gphone.parent.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.ParentLocationSelectActivity;
import com.yanxiu.gphone.parent.bean.ParentCityModel;
import com.yanxiu.gphone.parent.bean.ParentDistrictModel;
import com.yanxiu.gphone.parent.bean.ParentProvinceModel;

/**
 * Created by Administrator on 2015/7/13.
 */
public class ParentLocationAdapter extends YXiuCustomerBaseAdapter<YanxiuBaseBean>{
    private LayoutInflater layoutInflater;
    private int type;
    private int selectItemIndex = -1;
    public ParentLocationAdapter(Activity context, final int type) {
        super(context);
        this.type = type;
        layoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if( null == convertView){
            mViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.parent_search_public_item_layout, null);
            mViewHolder.locationName = (TextView) convertView.findViewById(R.id.item_name);
            mViewHolder.itemSelect = (ImageView) convertView.findViewById(R.id.item_select_view);
            mViewHolder.rightArrow = (ImageView) convertView.findViewById(R.id.item_right_arrow);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if(type == ParentLocationSelectActivity.LOCATION_CONSTANT_PROVINCE ){
            mViewHolder.rightArrow.setVisibility(View.VISIBLE);
            mViewHolder.itemSelect.setVisibility(View.GONE);
            final ParentProvinceModel ParentProvinceModel = (ParentProvinceModel) getItem(position);
            mViewHolder.locationName.setText(ParentProvinceModel.getName());
        } else if(type == ParentLocationSelectActivity.LOCATION_CONSTANT_CITY){
            mViewHolder.rightArrow.setVisibility(View.VISIBLE);
            mViewHolder.itemSelect.setVisibility(View.GONE);
            final ParentCityModel ParentCityModel = (ParentCityModel) getItem(position);
            mViewHolder.locationName.setText(ParentCityModel.getName());
        } else {
            mViewHolder.rightArrow.setVisibility(View.INVISIBLE);
            final ParentDistrictModel ParentDistrictModel = (ParentDistrictModel) getItem(position);
            if(position == selectItemIndex){
                mViewHolder.itemSelect.setVisibility(View.VISIBLE);
            } else {
                mViewHolder.itemSelect.setVisibility(View.GONE);
            }
            mViewHolder.locationName.setText(ParentDistrictModel.getName());
        }
        return convertView;
    }

    @Override
    public void clearDataSrouces() {

    }

    public int getSelectItemIndex() {
        return selectItemIndex;
    }

    public void setSelectItemIndex(int selectItemIndex) {
        this.selectItemIndex = selectItemIndex;
        notifyDataSetChanged();
    }

    public final class ViewHolder{
        public TextView locationName;
        public ImageView rightArrow;
        public ImageView itemSelect;
    }
}
