package com.yanxiu.gphone.student.inter;

import android.net.Uri;

import com.yanxiu.gphone.student.bean.DeleteImageBean;

/**
 * com.yanxiu.gphone.student.inter
 * Created by cangHaiXiao.
 * Time : 2016/11/30 16:53.
 * Function :
 */

public interface CorpListener {
    void oncorp(Uri url);
    void ondelete(DeleteImageBean bean);
}
