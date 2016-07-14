package com.yanxiu.gphone.hd.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.School;

import java.util.ArrayList;

/**
 */
public class SearchAdapter extends BaseAdapter{

    private Context mContext;

    private ArrayList<School> mSchoolList;


    public SearchAdapter (ArrayList<School> schoolList, Context context){
        mSchoolList = schoolList;
        mContext = context;
    }

    @Override public int getCount() {
            return mSchoolList == null  ? 0 : mSchoolList.size();
    }

    public void setList(ArrayList<School> list) {
        if (list != null && list.size() > 0) {
            mSchoolList = list;
        }
        notifyDataSetChanged();
    }

    @Override public Object getItem(int position) {
        return mSchoolList == null ? null : mSchoolList.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView,
            ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_item_layout,null);
            holder.name = (TextView)convertView.findViewById(R.id.school_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        School school = mSchoolList.get(position);
        holder.name.setText(school.getName());
        return convertView;
    }

    class ViewHolder{
        private TextView name;
    }

}
