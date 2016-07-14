package com.yanxiu.gphone.hd.student.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.UserLocationSelectActivity;
import com.yanxiu.gphone.hd.student.bean.CityModel;
import com.yanxiu.gphone.hd.student.bean.DistrictModel;
import com.yanxiu.gphone.hd.student.bean.ProvinceModel;

/**
 * Created by Administrator on 2015/7/13.
 */
public class YanxiuUserLocationAdapter extends YXiuCustomerBaseAdapter<YanxiuBaseBean>{
    private LayoutInflater layoutInflater;
    private int type;
    private int selectItemIndex = -1;
    public YanxiuUserLocationAdapter (Activity context, final int type) {
        super(context);
        this.type = type;
        layoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if( null == convertView){
            mViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.search_public_item_layout, null);
            mViewHolder.locationName = (TextView) convertView.findViewById(R.id.item_name);
            mViewHolder.itemSelect = (ImageView) convertView.findViewById(R.id.item_select_view);
            mViewHolder.rightArrow = (ImageView) convertView.findViewById(R.id.item_right_arrow);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if(type == UserLocationSelectActivity.LOCATION_CONSTANT_PROVINCE ){
            mViewHolder.rightArrow.setVisibility(View.VISIBLE);
            mViewHolder.itemSelect.setVisibility(View.GONE);
            final ProvinceModel provinceModel = (ProvinceModel) getItem(position);
            mViewHolder.locationName.setText(provinceModel.getName());
        } else if(type == UserLocationSelectActivity.LOCATION_CONSTANT_CITY){
            mViewHolder.rightArrow.setVisibility(View.VISIBLE);
            mViewHolder.itemSelect.setVisibility(View.GONE);
            final CityModel cityModel = (CityModel) getItem(position);
            mViewHolder.locationName.setText(cityModel.getName());
        } else {
            mViewHolder.rightArrow.setVisibility(View.INVISIBLE);
            final DistrictModel districtModel = (DistrictModel) getItem(position);
            if(position == selectItemIndex){
                mViewHolder.itemSelect.setVisibility(View.VISIBLE);
            } else {
                mViewHolder.itemSelect.setVisibility(View.GONE);
            }
            mViewHolder.locationName.setText(districtModel.getName());
        }
        return convertView;
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
