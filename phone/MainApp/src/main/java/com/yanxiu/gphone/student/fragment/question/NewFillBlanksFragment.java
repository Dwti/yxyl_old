package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.inter.AnswerCallback;
import com.yanxiu.gphone.student.view.FillBlankAnswerView;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2016/8/23.
 */
public class NewFillBlanksFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex {
    private View mView;
    private FillBlankAnswerView answerView;
    private QuestionsListener listener;
    //本地的保存数据bean
    private AnswerBean bean;
    private int typeId;
    private AnswerCallback callback;
    private int position;
    private YXiuAnserTextView tvQuestion;
    private List<String> listAnswer = new ArrayList<>();
    private String data = "这是一道填空题：请填第一个空(_)，请填第二个空(_)，请填第三个空(_)<span style=\\\"color: #333333; font-family: 宋体, 'Microsoft YaHei', arial, sans-serif; font-size: 12px; line-height: 26px; background-color: #FFFFFF;\\\">如图，在等腰梯形ABCD中，AB∥DC，AD=BC=5cm，AB=12cm，CD=6cm，点Q从C开始沿CD边向D移动，速度是每秒1厘米，点P从A开始沿AB向B移动，速度是点Q速度的a倍，如果点P，Q分别从A，C同时出发，当其中一点到达终点时运动停止．设运动时间为t秒．已知当t=</span><img src=\\\"http://tiku.21cnjy.com/tikupic/c1/a2/c1da22e63296ca27f7a073a711490e33.png\\\" style=\\\"border: 0px; color: rgb(51, 51, 51); font-family: 宋体, 'Microsoft YaHei', arial, sans-serif; font-size: 12px; line-height: 26px; white-space: normal; vertical-align: middle; -webkit-user-select: text !important; background-color: rgb(255, 255, 255);\\\"/><span style=\\\"color: #333333; font-family: 宋体, 'Microsoft YaHei', arial, sans-serif; font-size: 12px; line-height: 26px; background-color: #FFFFFF;\\\">时，四边形APQD是平行四边形．<br/><img src=\\\"http://tiku.21cnjy.com/tikupic/b8/45/b80458c951161c0dd5fa1a5ed13252e3.png\\\"/></span>";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.typeId = (getArguments() != null) ? getArguments().getInt("typeId") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_new_fill_blank_question, null);
        LinearLayout ll_answer_content = (LinearLayout) mView.findViewById(R.id.ll_answer_content);
        View view_line_ccc4a3_2 = mView.findViewById(R.id.view_line_ccc4a3_2);
        answerView = (FillBlankAnswerView) mView.findViewById(R.id.cq_item);
        if (callback != null) {
            ll_answer_content.setVisibility(View.GONE);
            view_line_ccc4a3_2.setVisibility(View.GONE);
        }
        tvQuestion = (YXiuAnserTextView) mView.findViewById(R.id.yxiu_tv);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
        setStemAndAnswerTemplate(questionsEntity);
        return mView;
    }

    private void setStemAndAnswerTemplate(QuestionEntity question) {
        if (question != null) {
            if (!StringUtils.isEmpty(question.getStem())) {
                StringBuilder sb = new StringBuilder(question.getStem());
                sb.append("  \n  \n");
                int blankCount = 0;
                int index = sb.indexOf("(_)");
                while (index != -1) {
                    blankCount++;
                    sb.replace(index, index + 3, "(" + blankCount + ")");
                    index = sb.indexOf("(_)");
                }
                tvQuestion.setTextHtml(sb.toString());
                answerView.setAnswerTemplate(blankCount);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(listAnswer.size()>0)
            answerView.setAnswerList(listAnswer);
    }

    @Override
    public void onPause() {
        super.onPause();
        listAnswer=answerView.getAnswerList();
    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
        this.listener = listener;
    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        this.bean = bean;
    }

    @Override
    public void answerViewClick() {

    }
}
