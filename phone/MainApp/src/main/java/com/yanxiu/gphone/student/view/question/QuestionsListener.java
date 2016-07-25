package com.yanxiu.gphone.student.view.question;

import android.view.View;

import com.yanxiu.gphone.student.bean.AnswerBean;

/**
 * Created by Administrator on 2015/7/12.
 * QuestionsListener 问题回调接口
 */
public interface QuestionsListener {


    /**
     * Change the viewPager next
     *
     */
    void flipNextPager(QuestionsListener listener);


    /**
     * set data sources
     * @param bean
     */
    void setDataSources(AnswerBean bean);


    /**
     * init view with data and answer
     * @param bean
     */
    void initViewWithData(AnswerBean bean);

    /**
     * view click
     * */
    void answerViewClick();

}
