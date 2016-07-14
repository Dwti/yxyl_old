package com.yanxiu.gphone.student.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/31.
 */
public class ReadingAnswer extends SrtBaseBean  {
    private int qid;
    private List<String> answer;

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
}
