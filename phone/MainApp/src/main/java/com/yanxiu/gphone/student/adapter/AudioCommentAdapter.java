package com.yanxiu.gphone.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AudioCommentBean;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.SimpleVoicePlayer;

import java.util.List;

/**
 * Created by sp on 17-2-10.
 */

public class AudioCommentAdapter extends BaseAdapter {
    private Context mContext;
    private List<AudioCommentBean> datas;
    private LayoutInflater inflater;
    private final static int BASE_LENGTH = Util.dipToPx(40);
    private final static int DIVISOR = 5;

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
        SimpleVoicePlayer simpleVoicePlayer = (SimpleVoicePlayer) convertView.findViewById(R.id.simple_voice_player);
        simpleVoicePlayer.setDataSource(datas.get(position).getUrl());
        simpleVoicePlayer.setDuration(datas.get(position).getLength());
        int width = datas.get(position).getLength() / DIVISOR * BASE_LENGTH;
        if(width < BASE_LENGTH)
            width = BASE_LENGTH;
        if(width > parent.getWidth())
            width = parent.getWidth();
        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        layoutParams.width = width;
        convertView.setLayoutParams(layoutParams);
        return convertView;
    }
}
