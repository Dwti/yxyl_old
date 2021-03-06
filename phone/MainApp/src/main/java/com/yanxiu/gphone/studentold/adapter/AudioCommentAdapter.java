package com.yanxiu.gphone.studentold.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.bean.AudioCommentBean;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.view.AudioCommentPlayer;

import java.util.List;

/**
 * Created by sp on 17-2-10.
 */

public class AudioCommentAdapter extends BaseAdapter {
    private Context mContext;
    private List<AudioCommentBean> datas;
    private LayoutInflater inflater;
    private static final int BASE_LENGTH = Util.dipToPx(80);

    public AudioCommentAdapter(Context context, List<AudioCommentBean> datas) {
        this.mContext = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.audio_comment_item,parent,false);
        }
        AudioCommentPlayer audioCommentPlayer = (AudioCommentPlayer) convertView.findViewById(R.id.simple_voice_player);
        audioCommentPlayer.setDataSource(datas.get(position).getUrl());
        audioCommentPlayer.setDuration(datas.get(position).getLength());
        int width = (int) (BASE_LENGTH +datas.get(position).getLength() / 180.0 * parent.getWidth());
        if(width > parent.getWidth())
            width = parent.getWidth();
        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        layoutParams.width = width;
        convertView.setLayoutParams(layoutParams);
        return convertView;
    }
}
