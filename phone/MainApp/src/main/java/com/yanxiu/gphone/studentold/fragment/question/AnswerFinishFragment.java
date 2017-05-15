package com.yanxiu.gphone.studentold.fragment.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.TimeUtils;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.activity.AnswerReportActivity;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;

/**
 * Created by Administrator on 2015/7/7.
 */
public class AnswerFinishFragment extends Fragment implements View.OnClickListener{

    private View rootView;

    private Button btnFinish;

    private int comeFrom;
    private SubjectExercisesItemBean dataSources;

    private TextView finishCotent;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        this.comeFrom = (arguments == null? 0: arguments.getInt("comeFrom"));
        LogInfo.log("geny", "AnswerCardFragment comeFrom------" + comeFrom);

        dataSources = (arguments == null? null:(SubjectExercisesItemBean) arguments.getSerializable("subjectExercisesItemBean"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_answer_finish,null);
        rootView.setOnClickListener(this);
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }


    private void initView(){
        btnFinish = (Button) rootView.findViewById(R.id.btn_question_finish);
        btnFinish.setOnClickListener(this);

        finishCotent = (TextView) rootView.findViewById(R.id.iv_finish_content);
        String formatTime = "";
        if(dataSources != null && dataSources.getData() != null && dataSources.getData().get(0) != null){

            long groupEndtime = dataSources.getData().get(0).getEndtime();//作业练习截止时间
            formatTime = TimeUtils.getTimeLongYMD(groupEndtime);
        }

        switch (comeFrom){
            case YanXiuConstant.HOMEWORK_REPORT:
                finishCotent.setText(getResources().getString(R.string.question_finish_hw_tips));
                break;
            case YanXiuConstant.INTELLI_REPORT:
            case YanXiuConstant.KPN_REPORT:
            case YanXiuConstant.HISTORY_REPORT:
                finishCotent.setText(getResources().getString(R.string.question_finish_ex_tips));
                break;
            case YanXiuConstant.END_TIME:
                finishCotent.setVisibility(View.GONE);
                rootView.findViewById(R.id.rl_finish_content).setVisibility(View.VISIBLE);
                TextView endTime = (TextView) rootView.findViewById(R.id.question_finish_hw_end_time_tips);
                endTime.setText(this.getResources().getString(R.string.question_finish_hw_end_time_tips) + formatTime);
                break;
        }
    }

    @Override
    public boolean getUserVisibleHint() {
        this.setUserVisibleHint(true);
        return super.getUserVisibleHint();
    }

    @Override
    public void onClick(final View v) {
        if(v == btnFinish && comeFrom!=YanXiuConstant.END_TIME){
            if(comeFrom == AnswerCardFragment.GROUP){
                AnswerReportActivity.launch(AnswerFinishFragment.this.getActivity(), dataSources, YanXiuConstant.HOMEWORK_REPORT, Intent.FLAG_ACTIVITY_FORWARD_RESULT, true);
            }else if(comeFrom == YanXiuConstant.HISTORY_REPORT){
                AnswerReportActivity.launch(AnswerFinishFragment.this.getActivity(), dataSources, YanXiuConstant.HISTORY_REPORT, Intent.FLAG_ACTIVITY_FORWARD_RESULT, true);
            }else if(comeFrom==YanXiuConstant.THIRD_EXAMPOINT){
                AnswerReportActivity.launch(AnswerFinishFragment.this.getActivity(), dataSources, YanXiuConstant.INTELLI_REPORT,Intent.FLAG_ACTIVITY_FORWARD_RESULT, false);
            }else if(comeFrom==YanXiuConstant.KPN_REPORT){
                AnswerReportActivity.launch(AnswerFinishFragment.this.getActivity(), dataSources, YanXiuConstant.KPN_REPORT,Intent.FLAG_ACTIVITY_FORWARD_RESULT, false);
            }else{
                AnswerReportActivity.launch(AnswerFinishFragment.this.getActivity(), dataSources, YanXiuConstant.INTELLI_REPORT,Intent.FLAG_ACTIVITY_FORWARD_RESULT, false);
            }
            AnswerFinishFragment.this.getActivity().finish();
        }else if(v == btnFinish && comeFrom==YanXiuConstant.END_TIME){
            AnswerFinishFragment.this.getActivity().finish();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
